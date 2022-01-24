from telethon.sync import TelegramClient
from flask import Flask, render_template
from telethon.tl.types import InputPeerChannel, InputChatUploadedPhoto
from telethon.tl.functions.channels import EditPhotoRequest, InviteToChannelRequest
from telethon import functions, types
from firebase_admin import db
import firebase_admin
from firebase_admin import credentials
from Global import *
import asyncio
import requests


app = Flask(__name__, template_folder="{}/templates".format(root))

if not firebase_admin._apps:
    cred = credentials.Certificate("{}/secure/serviceAccountKey.json".format(root))
    firebase_admin.initialize_app(cred, {
        'databaseURL': 'https://moviebot-11e34-default-rtdb.asia-southeast1.firebasedatabase.app/'
    })

def send_movie(channel_id, channel_access_hash, data):

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    client = TelegramClient(name, api_id, api_hash)
    client.connect()

    with app.app_context():
        receiver = InputPeerChannel(channel_id, channel_access_hash)
        client.send_message(receiver, parse_mode = "HTML", message = render_template("movie_page.html".format(root), movie_data = data))

    client.disconnect()


def add_channel(channel_link):

    data = {}
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    client = TelegramClient(name, api_id, api_hash)
    client.connect()

    channel = client.get_entity(channel_link)

    data["channelLink"] = "https://t.me/{}".format(channel_link)
    data["channelTitle"] = channel.__dict__["title"]
    data["channelId"] = channel.__dict__["id"]
    data["channelAccessHash"] = channel.__dict__["access_hash"]

    ref = db.reference("Members")
    members = ref.get()
    users_to_add = []
    i = 0
    for member in members or []:
        # users_to_add.append(client.get_entity(int(members[member]["memberId"])))
        if i == 100:
            break
        i += 1
        try:
            client(InviteToChannelRequest(InputPeerChannel(data["channelId"], data["channelAccessHash"]), [client.get_entity(members[member]["memberId"])]))
            print(i)
            # users_to_add.append()
        except:
            continue

    # print(users_to_add)
    #
    # client(InviteToChannelRequest(InputPeerChannel(data["channelId"], data["channelAccessHash"]), users_to_add))

    client.disconnect()

    ref = db.reference("Movies")
    movies = ref.get()
    for movie in movies or []:
        send_movie(data["channelId"], data["channelAccessHash"], movies[movie])

    return data

def delete_channel(channel_id, channel_access_hash):

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    client = TelegramClient(name, api_id, api_hash)
    client.connect()

    try:
        client(functions.channels.DeleteChannelRequest(types.InputPeerChannel(channel_id, channel_access_hash)))
    except:
        client.disconnect()
        return False
    client.disconnect()
    return True

def create_private_channel(data):

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    client = TelegramClient(name, api_id, api_hash)
    client.connect()

    about = ""
    i = 0
    for quality in data["movieLinks"]:
        about += "\n🔗️ {} 👉 {}\n".format(quality["linkValue"], quality["linkKey"])
        if i == 5:
            break
        i += 1

    created_private_channel = client(functions.channels.CreateChannelRequest(
        title= data["movieTitle"],
        about= about
    ))
    new_channel_id = created_private_channel.__dict__["chats"][0].__dict__["id"]
    new_channel_access_hash = created_private_channel.__dict__["chats"][0].__dict__["access_hash"]

    open("{}/image.jpg".format(root), 'wb').write(requests.get(data["movieThumbnail"]).content)
    upload_file_result = client.upload_file(file= "{}/image.jpg".format(root))
    input_chat_uploaded_photo = InputChatUploadedPhoto(upload_file_result)
    client(EditPhotoRequest(types.InputPeerChannel(channel_id=new_channel_id, access_hash=new_channel_access_hash), photo=input_chat_uploaded_photo))

    link = client(functions.messages.ExportChatInviteRequest(types.InputPeerChannel(channel_id=new_channel_id, access_hash=new_channel_access_hash)))

    with app.app_context():
        receiver = InputPeerChannel(new_channel_id, new_channel_access_hash)
        client.send_message(receiver, parse_mode = "HTML", message = render_template("message.html".format(root), movie_data = data), file = data["movieThumbnail"])

    client.disconnect()
    return link.__dict__["link"], new_channel_id, new_channel_access_hash
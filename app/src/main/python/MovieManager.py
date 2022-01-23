import math
import urllib
import string
import random
import firebase_admin
from firebase_admin import db
from bs4 import BeautifulSoup
from torrentool.api import Torrent
from firebase_admin import credentials
from urllib.parse import urlparse, urlencode
from ChannelManager import *
from Global import *

cred = credentials.Certificate("{}/secure/serviceAccountKey.json".format(root))
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://moviebot-11e34-default-rtdb.asia-southeast1.firebasedatabase.app/'
})

def convert_size(size_bytes):
    if size_bytes == 0:
        return "0B"
    size_name = ("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
    i = int(math.floor(math.log(size_bytes, 1024)))
    p = math.pow(1024, i)
    s = round(size_bytes / p, 2)
    return "%s %s" % (s, size_name[i])

def short_url(file_url):

    url = 'https://droplink.co/api'
    alias = str(''.join(random.choices(string.ascii_uppercase + string.digits, k = 5)))
    params = {'api': API, 'url': file_url, 'alias': alias}

    response = requests.get(url, params=params).json()
    return response["shortenedUrl"]

def get_movie(link, movie_title):

    data = {}

    page = requests.get(link)
    soup = BeautifulSoup(page.content, "html.parser")

    keys = []
    links = []
    for key in soup.find_all("span", style="color:#0000ff;"):
        if key.parent.name == "a":

            final_tracker = ""

            torrent_link = key.parent['href']
            metadata = Torrent.from_string(requests.get(torrent_link).content)

            for tracker in metadata.announce_urls:
                final_tracker += '&tr=' + urllib.parse.quote_plus(tracker[0])

            encoded = urlencode(dict(file=metadata.name, pwd='/', magnet=metadata.magnet_link, dn=metadata.name.replace(' ', '+')))
            keys.append(convert_size(metadata.total_size))
            links.append(short_url(f"https://webtor.io/#/show?{encoded}{final_tracker}"))

    movie_links = []
    for i in range(len(links)):
        movie_data = {}
        movie_data['linkKey'] = links[i]
        movie_data['linkValue'] = keys[i]
        movie_links.append(movie_data)

    thumbnail = soup.find_all("img", class_="ipsImage")[0]['src']
    data['movieThumbnail'] = thumbnail
    data['movieTitle'] = movie_title
    data['movieLinks'] = movie_links
    data['channelLink'], data['channelId'], data['channelAccessHash'] = create_private_channel(data)

    ref = db.reference("Channels")
    channels = ref.get()
    for channel in channels:
        send_movie(channels[channel]["channelId"], channels[channel]["channelAccessHash"], data)

    return data
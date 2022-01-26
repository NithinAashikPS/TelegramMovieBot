package com.dappsindia.telegrammoviebot;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class AddChannelTask extends AsyncTask<Void, Void, Void> {

    private String channelLink;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Python python;
    private PyObject channelManager;

    private UploadStatus uploadStatus;

    public AddChannelTask(String channelLink, UploadStatus uploadStatus) {
        this.channelLink = channelLink;

        this.uploadStatus = uploadStatus;

        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("Channels");

        this.python = Python.getInstance();
        this.channelManager = python.getModule("ChannelManager");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String data = channelManager.callAttr("add_channel", channelLink).toString();
            String key = myRef.push().getKey();
            Gson gson = new Gson();
            ChannelModel channelModel = gson.fromJson(data, ChannelModel.class);
            myRef.child(key).setValue(channelModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    uploadStatus.uploaded(false);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    uploadStatus.uploaded(true);
                }
            });
        } catch (Exception e) {
            uploadStatus.uploaded(true);
        }
        return null;
    }
}

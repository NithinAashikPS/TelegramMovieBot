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


public class AddMovieTask extends AsyncTask<Void, Void, Void> {

    private String movieLink;
    private String movieTitle;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Python python;
    private PyObject movieManager;

    private UploadStatus uploadStatus;

    public AddMovieTask(String movieLink, String movieTitle, UploadStatus uploadStatus) {
        this.movieLink = movieLink;
        this.movieTitle = movieTitle;

        this.uploadStatus = uploadStatus;

        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("Movies");

        this.python = Python.getInstance();
        this.movieManager = python.getModule("MovieManager");
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            String data = movieManager.callAttr("get_movie",movieLink, movieTitle).toString();
            String key = myRef.push().getKey();
            Gson gson = new Gson();
            MovieModel movieModel = gson.fromJson(data, MovieModel.class);
            myRef.child(key).setValue(movieModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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

package com.dappsindia.telegrammoviebot;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddMovieFragment extends BottomSheetDialogFragment {

    private TextView movieTitle;
    private TextView movieLink;

    private Button addMovie;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_movie, container, false);

        movieTitle = view.findViewById(R.id.movie_title);
        movieLink = view.findViewById(R.id.movie_thumbnail);

        addMovie = view.findViewById(R.id.add_movie);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Uploading Movie...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                new AddMovieTask(movieLink.getText().toString(), movieTitle.getText().toString(), new UploadStatus() {
                    @Override
                    public void uploaded(boolean error) {
                        progressDialog.dismiss();
//                        Handler handler =  new Handler(requireContext().getMainLooper());
//                        handler.post( new Runnable(){
//                            public void run(){
//                                if (error) {
//                                    Toast.makeText(requireContext(), "Movie Upload Failed.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(requireContext(), "Movie Successfully Uploaded.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                    }
                }).execute();
                dismiss();
            }
        });
    }
}
package com.dappsindia.telegrammoviebot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView addMovie;
    private TextView allMovies;
    private TextView addChannel;
    private TextView allChannels;
    private LinearLayout switchMenu;

    private MovieAndChannelUpdater movieAndChannelUpdater;

    private RecyclerView movieRecyclerView;
    private RecyclerView channelRecyclerView;

    private MovieAdapter movieAdapter;
    private ChannelAdapter channelAdapter;

    private List<MovieModel> movieModels;
    private List<ChannelModel> channelModels;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        addMovie = findViewById(R.id.add_movie);
        allMovies = findViewById(R.id.all_movies);
        addChannel = findViewById(R.id.add_channel);
        allChannels = findViewById(R.id.all_channels);
        switchMenu = findViewById(R.id.switch_menu);

        movieAndChannelUpdater = MovieAndChannelUpdater.getInstance();

        movieRecyclerView = findViewById(R.id.movie_recycler);
        channelRecyclerView = findViewById(R.id.channel_recycler);
        movieModels = new ArrayList<>();
        channelModels = new ArrayList<>();

        movieAdapter = new MovieAdapter(movieModels);
        channelAdapter = new ChannelAdapter(channelModels);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        channelRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        movieRecyclerView.setAdapter(movieAdapter);
        channelRecyclerView.setAdapter(channelAdapter);

        database = FirebaseDatabase.getInstance();
        getMovies();

        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMovieFragment addMovieFragment = new AddMovieFragment();
                addMovieFragment.show(getSupportFragmentManager(), addMovieFragment.getTag());
            }
        });

        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChannelFragment addChannelFragment = new AddChannelFragment();
                addChannelFragment.show(getSupportFragmentManager(), addChannelFragment.getTag());
            }
        });

        switchMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieAndChannelUpdater.updateMenu();
            }
        });

        movieAndChannelUpdater.setMenuListener(new CurrentMenu() {
            @Override
            public void menu(boolean isMovie) {
                if (!isMovie) {
                    addMovie.setVisibility(View.VISIBLE);
                    addChannel.setVisibility(View.GONE);
                    getMovies();
                    movieRecyclerView.setVisibility(View.VISIBLE);
                    channelRecyclerView.setVisibility(View.GONE);
                    allMovies.setText(getResources().getString(R.string.all_movies));
                    allChannels.setText(getResources().getString(R.string.all_channels));
                } else {
                    addMovie.setVisibility(View.GONE);
                    addChannel.setVisibility(View.VISIBLE);
                    getChannels();
                    movieRecyclerView.setVisibility(View.GONE);
                    channelRecyclerView.setVisibility(View.VISIBLE);
                    allMovies.setText(getResources().getString(R.string.all_channels));
                    allChannels.setText(getResources().getString(R.string.all_movies));
                }
            }
        });
    }

    private void getMovies() {

        myRef = database.getReference("Movies");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        MovieModel movieModel = snapshot.getValue(MovieModel.class);
                        movieModel.setKey(snapshot.getKey());
                        movieModels.add(movieModel);
                    } catch (Exception e) {
                        getMovies();
                    }
                }
                Collections.reverse(movieModels);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getChannels() {

        myRef = database.getReference("Channels");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                channelModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        ChannelModel channelModel = snapshot.getValue(ChannelModel.class);
                        channelModel.setKey(snapshot.getKey());
                        channelModels.add(channelModel);
                    } catch (Exception e) {
                        getChannels();
                    }
                }
                Collections.reverse(channelModels);
                channelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
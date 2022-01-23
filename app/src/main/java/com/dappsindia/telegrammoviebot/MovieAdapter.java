package com.dappsindia.telegrammoviebot;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieModel> movieModels;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Python python;
    private PyObject channelManager;

    public MovieAdapter(List<MovieModel> movieModels) {
        this.movieModels = movieModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Movies");
        python = Python.getInstance();
        channelManager = python.getModule("ChannelManager");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String channelLink = movieModels.get(position).getChannelLink();
        holder.movieTitle.setText(movieModels.get(position).getMovieTitle());
        holder.count.setText(String.valueOf(position + 1));
        holder.qualities.setText(channelLink);
        String key = movieModels.get(position).getKey();
        Long channelId = movieModels.get(position).getChannelId();
        Long channelAccessHash = movieModels.get(position).getChannelAccessHash();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(channelLink)));
            }
        });
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menuDelete:
                                if(channelManager.callAttr("delete_channel", channelId, channelAccessHash).toBoolean()) {
                                    myRef.child(key).getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(holder.itemView.getContext(), "Movie Deleted.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView movieTitle;
        private TextView qualities;
        private TextView count;

        private ImageView options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieTitle = itemView.findViewById(R.id.movie_title);
            qualities = itemView.findViewById(R.id.qualities);
            count = itemView.findViewById(R.id.count);
            options = itemView.findViewById(R.id.options);
        }
    }
}

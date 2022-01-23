package com.dappsindia.telegrammoviebot;

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

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

    private List<ChannelModel> channelModels;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Python python;
    private PyObject channelManager;

    public ChannelAdapter(List<ChannelModel> channelModels) {
        this.channelModels = channelModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Channels");
        python = Python.getInstance();
        channelManager = python.getModule("ChannelManager");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.channelId.setText(channelModels.get(position).getChannelId().toString());
        holder.channelTitle.setText(channelModels.get(position).getChannelTitle());
        holder.count.setText(String.valueOf(position + 1));

        String key = channelModels.get(position).getKey();
        Long channelId = channelModels.get(position).getChannelId();
        Long channelAccessHash = channelModels.get(position).getChannelAccessHash();

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
                                            Toast.makeText(holder.itemView.getContext(), "Channel Deleted.", Toast.LENGTH_SHORT).show();
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
        return channelModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView channelId;
        private TextView channelTitle;
        private TextView count;

        private ImageView options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            channelId = itemView.findViewById(R.id.channel_id);
            channelTitle = itemView.findViewById(R.id.channel_title);
            count = itemView.findViewById(R.id.count);
            options = itemView.findViewById(R.id.options);
        }
    }
}

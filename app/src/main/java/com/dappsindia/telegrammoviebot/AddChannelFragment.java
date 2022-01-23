package com.dappsindia.telegrammoviebot;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddChannelFragment extends BottomSheetDialogFragment {

    private EditText channelLink;

    private Button createChannel;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_channel, container, false);

        channelLink = root.findViewById(R.id.channel_link);
        createChannel = root.findViewById(R.id.create_channel);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Creating Channel...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                new AddChannelTask(channelLink.getText().toString(), new UploadStatus() {
                    @Override
                    public void uploaded(boolean error) {
                        progressDialog.dismiss();
                    }
                }).execute();
                dismiss();
            }
        });
    }
}
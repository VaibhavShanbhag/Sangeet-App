package com.example.sangeet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapter extends ArrayAdapter<String> {
    private String[] songs;
    public CustomAdapter(@NonNull Context context, int resource, @NonNull String[] songs) {
        super(context, resource, songs);
        this.songs = songs;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return songs[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_screen_layout,parent,false);
        TextView textView = convertView.findViewById(R.id.music);
        textView.setText(getItem(position));
        textView.setSelected(true);
        return convertView;
    }
}

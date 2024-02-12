package com.example.muaupl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {
    private List<Track> tracks;
    private int layoutResource;

    public TrackAdapter(Context context, int resource, List<Track> tracks) {
        super(context, resource, tracks);
        this.layoutResource = resource;
        this.tracks = tracks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TrackHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResource, parent, false);

            holder = new TrackHolder();
            holder.txtTitle = row.findViewById(R.id.txtTitle);
            holder.txtTime = row.findViewById(R.id.txtTime);

            row.setTag(holder);
        } else {
            holder = (TrackHolder) row.getTag();
        }

        Track track = tracks.get(position);
        holder.txtTitle.setText(track.getTitle());
        holder.txtTime.setText(track.getTime());

        return row;
    }

    private static class TrackHolder {
        TextView txtTitle;
        TextView txtTime;
    }
}

package com.example.muaupl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {
    private List<Track> tracks;
    private int layoutResource;
    private List<Track> filteredTracks;

    public TrackAdapter(Context context, int resource, List<Track> tracks) {
        super(context, resource, tracks);
        this.layoutResource = resource;
        this.tracks = tracks;
        this.filteredTracks = new ArrayList<>(tracks);
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
    public void resetFilter() {
        clear();
        addAll(filteredTracks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Track> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(filteredTracks);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Track track : filteredTracks) {
                        if (track.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(track);
                        }
                    }
                }

                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<Track>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private static class TrackHolder {
        TextView txtTitle;
        TextView txtTime;
    }
}

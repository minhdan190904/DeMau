package com.androchef.bai1_song;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends ArrayAdapter<Song> {

    private Context context;
    private List<Song> songs;

    public ListAdapter(Context context, List<Song> songs) {
        super(context, R.layout.item_song, songs);
        this.context = context;
        this.songs = songs;
    }

    public void updateList(List<Song> list){
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    public void searchSong(String key, List<Song> allSongBase){
        if(key.isEmpty()){
            updateList(allSongBase);
        } else {
            List<Song> orderKeyList = new ArrayList<>();
            for(Song song: allSongBase){
                if(song.getName().toLowerCase().contains(key.toLowerCase())){
                    orderKeyList.add(song);
                }
            }
            updateList(orderKeyList);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_song, parent, false);

        TextView tvSongName = rowView.findViewById(R.id.tvName);
        TextView tvSinger = rowView.findViewById(R.id.tvSinger);
        TextView tvTime = rowView.findViewById(R.id.tvTime);

        //code
        Song song = getItem(position);
        tvSongName.setText(song.getName());
        tvSinger.setText(song.getSinger());

        // Format time to display minutes and seconds
        Integer minutes = song.getTime().intValue();
        Integer seconds = (int) (song.getTime() * 60 - minutes * 60);
        tvTime.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));
        return rowView;
    }
}

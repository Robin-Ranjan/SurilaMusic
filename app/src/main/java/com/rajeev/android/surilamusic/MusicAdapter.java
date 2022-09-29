package com.rajeev.android.surilamusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

   ArrayList<ModelClass> songList;
   Context context;
// create constructor here
    public MusicAdapter(ArrayList<ModelClass> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item,parent,false);
      return  new MusicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        ModelClass songData = songList.get(position);
        holder.songName.setText(songData.getTitle());

        if (MyMediaPlayer.currentIndex==position){
            holder.songName.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.songName.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,PlayingMusic.class);
                intent.putExtra("LIST",songList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView songName;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

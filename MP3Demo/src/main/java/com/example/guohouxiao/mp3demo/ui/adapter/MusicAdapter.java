package com.example.guohouxiao.mp3demo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guohouxiao.mp3demo.R;
import com.example.guohouxiao.mp3demo.bean.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {
    private static final String TAG = "MusicAdapter";
    private List<Music>musicList = new ArrayList<>();
    private Context context;
    private OnclickListener onclickListener;
    public interface OnclickListener{
        void click(View view,int position);
    }
    public void setOnclickListener(OnclickListener onclickListener){
        this.onclickListener = onclickListener;
    }

    public MusicAdapter(List<Music> musicList, Context context) {
        this.musicList = musicList;
        this.context = context;
    }

    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicAdapter.MyViewHolder holder, int position) {
        holder.music_name_item.setText(musicList.get(position).getTitle());
        holder.music_artist_item.setText(musicList.get(position).getArtist());
        if (musicList.get(position).getCover()!= null) {
            Glide.with(context)
                    .load(musicList.get(position).getCover())
                    .into(holder.imageView);
        }else {
        }

        holder.itemView.setOnClickListener(view -> {
            Log.i(TAG, "onBindViewHolder: click success");
            onclickListener.click(holder.itemView, holder.getLayoutPosition());
        }

        );

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView music_name_item,music_artist_item;
        private ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            music_artist_item = itemView.findViewById(R.id.music_artist_item);
            music_name_item = itemView.findViewById(R.id.music_name_item);
            imageView = itemView.findViewById(R.id.image_item);
        }
    }
}

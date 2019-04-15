package com.example.android.tablayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VideoInfoHolder> {

    //these ids are the unique id for each video
    private List<Details> list = new ArrayList<>();
    Context ctx;

    public RecyclerAdapter(Context context) {
        this.ctx = context;
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // ctx = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                Log.i("onThumbnailError1", "onThumbnailError1");

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.titleView.setText(list.get(position).getVideoTitle());
                Log.i("onThumbnailLoaded1", "onThumbnailLoaded1");
            }
        };

        holder.youTubeThumbnailView.initialize(Config.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(list.get(position).getVideoId());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);

                Log.i("success1", "success1");
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Log.i("failure1", "failure1");
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;
        protected TextView titleView;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            Log.i("VideoInfoHolder", "VideoInfoHolder");
            playButton = (ImageView) itemView.findViewById(R.id.btnYoutube_player);
            titleView = (TextView)itemView.findViewById(R.id.title_view);
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
        }

        @Override
        public void onClick(View v) {
            Log.i("onClick", "onClick");
            try {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, Config.YOUTUBE_API_KEY, list.get(getLayoutPosition()).getVideoId(), 0, true, true);
                ctx.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setData(List<Details> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}

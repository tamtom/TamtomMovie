package udacity.com.tamtommovie.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.tamtommovie.R;
import udacity.com.tamtommovie.model.Video;

/**
 * Created by omaraltamimi on 4/17/18.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private OnVideoSelectedListener listener;
    private List<Video> videos;

    public VideosAdapter(List<Video> videos,OnVideoSelectedListener listener) {
        this.listener = listener;
        this.videos = videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        if (videos == null) return 0;
        return videos.size();
    }

    public void updateData(List<Video> newVideos) {
        this.videos = newVideos;
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoThumbnail)
        ImageView videoThumbnail;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openVideo();
                }
            });
        }

        public void bind(Video video) {
            Picasso.get().load("https://img.youtube.com/vi/" + video.getKey() +
                    "/mqdefault.jpg").into(videoThumbnail);
        }

        public void openVideo() {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;
            listener.onVideoSelected(videos.get(getAdapterPosition()));
        }
    }

    public interface OnVideoSelectedListener {
        void onVideoSelected(Video video);
    }
}

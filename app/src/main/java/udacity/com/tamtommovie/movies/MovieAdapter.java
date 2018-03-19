package udacity.com.tamtommovie.movies;

import android.support.v4.view.ViewCompat;
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
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.util.ImageUtil;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movies;
    private OnMovieClickedListener mOnMovieClickedListener;


    public MovieAdapter(List<Movie> movies, OnMovieClickedListener movieClickedListener) {
        this.movies = movies;
        mOnMovieClickedListener = movieClickedListener;
    }

    public void updateMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMoreMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false), mOnMovieClickedListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION)
            return;
        holder.bind(movies.get(position));

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie_poster)
        ImageView ivPoster;
        private OnMovieClickedListener mOnMovieClickedListener;

        public ViewHolder(View itemView, OnMovieClickedListener onMovieClickedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnMovieClickedListener = onMovieClickedListener;
        }

        public void bind(Movie movie) {
            Picasso.get()
                    .load(ImageUtil.getPosterImage(movie.getPosterPath()))
                    .placeholder(R.drawable.ic_movie_blue_grey_400_48dp)
                    .into(ivPoster);
            ViewCompat.setTransitionName(ivPoster, String.valueOf(movie.getId()));
            ivPoster.setOnClickListener(view -> mOnMovieClickedListener.onMovieClicked(movie,
                    ivPoster));
        }
    }

    public interface OnMovieClickedListener {
        void onMovieClicked(Movie movie, ImageView sharedImage);
    }
}

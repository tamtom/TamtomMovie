package udacity.com.tamtommovie.movies;

import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.tamtommovie.R;
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.util.ImageUtil;

import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry
        .COLUMN_NAME_POSTER_URL;
import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry
        .COLUMN_NAME_ID;
import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry
        .COLUMN_NAME_TITLE;

/**
 * Created by omaraltamimi on 6/2/18.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private static final String TAG = FavoriteAdapter.class.getSimpleName();
    private OnFavoriteClick mOnMovieClickedListener;
    private Cursor cursor;


    public FavoriteAdapter(OnFavoriteClick movieClickedListener) {
        mOnMovieClickedListener = movieClickedListener;
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
        holder.bind(getMovieFromCursor(cursor, position));

    }

    @Override
    public int getItemCount() {

        if (cursor == null) return 0;
        return cursor.getCount();
    }

    public void updateCursor(Cursor c) {
        if (c != null) {
            this.cursor = c;
            this.notifyDataSetChanged();
        }
    }
    private Movie getMovieFromCursor(Cursor cursor, int position) {
        boolean success = cursor.moveToPosition(position);
        Movie movie;

        if (success) {
            movie = new Movie();
            movie.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_POSTER_URL)));
            return movie;
        } else {
            Log.e(TAG, "getMovieFromCursor: returning null instead of moviePreview!");
            return null;
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie_poster)
        ImageView ivPoster;
        private OnFavoriteClick mOnMovieClickedListener;

        public ViewHolder(View itemView, OnFavoriteClick onMovieClickedListener) {
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
            ivPoster.setOnClickListener(view -> mOnMovieClickedListener.onFavClicked(movie,
                    ivPoster));
        }
    }

    public interface OnFavoriteClick {
        void onFavClicked(Movie movie, ImageView sharedImage);
    }
}


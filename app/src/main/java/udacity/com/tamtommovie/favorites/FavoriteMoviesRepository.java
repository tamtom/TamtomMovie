package udacity.com.tamtommovie.favorites;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import udacity.com.tamtommovie.MyApplication;
import udacity.com.tamtommovie.model.Movie;

import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry
        .COLUMN_NAME_POSTER_URL;
import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry
        .COLUMN_NAME_ID;
import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry
        .COLUMN_NAME_TIMESTAMP;
import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry
        .COLUMN_NAME_TITLE;
import static udacity.com.tamtommovie.favorites.FavoriteMoviesDbContract.FavoritesEntry.CONTENT_URI;

public class FavoriteMoviesRepository {
    private static final String TAG = FavoriteMoviesRepository.class.getSimpleName();
    private static volatile FavoriteMoviesRepository mInstance;

    private FavoriteMoviesRepository() {
    }

    public static FavoriteMoviesRepository getInstance() {
        if (mInstance == null) {
            synchronized (FavoriteMoviesRepository.class) {
                if (mInstance == null) {
                    mInstance = new FavoriteMoviesRepository();
                }
            }
        }
        return mInstance;
    }


    public void addMovieToFavorites(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_ID, movie.getId());
        cv.put(COLUMN_NAME_TITLE, movie.getTitle());
        cv.put(COLUMN_NAME_POSTER_URL, movie.getPosterPath());

        Completable.fromAction(() -> MyApplication.getInstance().getContentResolver()
                .insert(CONTENT_URI, cv))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "addMovieToFavorites: Added to favorites " + movie.getId() + "/" + movie.getTitle()),
                        e -> Log.e(TAG, "addMovieToFavorites: couldn't add " + movie.getId() + "/" + movie.getTitle() + "to favorites db", e)
                );
    }

    public void removeMovieFromFavorites(Movie movie) {
        Uri uri = CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        Completable.fromAction(() -> MyApplication.getInstance().getContentResolver()
                .delete(uri, null, null))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "removeMovieFromFavorites: Removed " + movie.getId() + "/" + movie.getTitle() + "from favorites"),
                        e -> Log.e(TAG, "removeMovieFromFavorites: couldn't remove " + movie.getId() + "/" + movie.getTitle() + "from favorites", e)
                );
    }

    public Observable<Boolean> checkMovieInFavorites(long id) {
        Uri uri = CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        return Observable.fromCallable(() -> MyApplication.getInstance().getContentResolver()
                .query(uri, null, "id=?", new String[]{String.valueOf(id)}, null))
                .subscribeOn(Schedulers.io())
                .map(cursor -> {
                    Boolean isInFavorites = cursor.getCount() > 0;
                    cursor.close();
                    return isInFavorites;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Cursor> queryAllFavoriteMovies() {
        return Observable.fromCallable(() -> MyApplication.getInstance().getContentResolver()
                .query(CONTENT_URI, null, null, null, COLUMN_NAME_TIMESTAMP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

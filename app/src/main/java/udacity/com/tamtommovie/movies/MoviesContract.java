package udacity.com.tamtommovie.movies;

import android.database.Cursor;
import android.widget.ImageView;

import java.util.List;

import io.reactivex.Observable;
import udacity.com.tamtommovie.base.BasePresenter;
import udacity.com.tamtommovie.base.BaseView;
import udacity.com.tamtommovie.model.MoviesResult;
import udacity.com.tamtommovie.model.Movie;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public interface MoviesContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMovies(List<Movie> movies);

        void showMovieDetailsUi(Movie movie, ImageView sharedImage);

        void loadMoreMovies(List<Movie> movies);

        void showLoadingMoviesError(ErrorType errorType);

        void showFavorites(Cursor cursor);


    }

    interface Presenter extends BasePresenter {
        void loadMovies(boolean loadMore);

        void openMovieDetails(Movie movie, ImageView sharedImage);

        Observable<MoviesResult> getMoviesObservable(String type);

        String getMoviesType();
        void loadFavorites();
    }
}

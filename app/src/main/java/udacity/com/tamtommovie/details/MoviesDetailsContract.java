package udacity.com.tamtommovie.details;

import udacity.com.tamtommovie.base.BasePresenter;
import udacity.com.tamtommovie.base.BaseView;
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.model.Video;

/**
 * Created by Omar AlTamimi on 3/20/2018.
 */

public class MoviesDetailsContract {
    interface Presenter extends BasePresenter {
             void loadMovieDetail(long id);
             void getFavoriteStatus(long id);
             void addToFavorite(Movie movie);
             void removeFromFavorite(Movie movie);
    }

    interface View extends BaseView<Presenter> {
              void onMovieLoaded(Movie movie);
              void lunchYoutube(String id);
              void updateFavoriteButton(boolean isFavorite);
    }

}

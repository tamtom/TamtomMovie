package udacity.com.tamtommovie.details;

import udacity.com.tamtommovie.base.BasePresenter;
import udacity.com.tamtommovie.base.BaseView;
import udacity.com.tamtommovie.model.Movie;

/**
 * Created by Omar AlTamimi on 3/20/2018.
 */

public class MoviesDetailsContract {
    interface Presenter extends BasePresenter {
             void loadMovieDetail(long id);
    }

    interface View extends BaseView<Presenter> {
              void onMovieLoaded(Movie movie);
    }
}

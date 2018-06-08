package udacity.com.tamtommovie.movies;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import udacity.com.tamtommovie.MyApplication;
import udacity.com.tamtommovie.api.APIService;
import udacity.com.tamtommovie.favorites.FavoriteMoviesRepository;
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.model.MoviesResult;
import udacity.com.tamtommovie.util.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public class MoviesPresenter implements MoviesContract.Presenter {
    @NonNull
    private final APIService mAPIService;

    @NonNull
    private final MoviesContract.View mMoviesView;
    @NonNull
    private CompositeDisposable mCompositeDisposable;
    private final FavoriteMoviesRepository mFavoriteMoviesRepository;
    int mCurrentPopularPage = 1;
    int mCurrentTopRatedPage = 1;

    public MoviesPresenter(@NonNull APIService apiService, @NonNull MoviesContract.View
            moviesView) {
        mAPIService = checkNotNull(apiService, "RetrofitWrapper Cannot be" +
                " null");
        mMoviesView = checkNotNull(moviesView, "Movie View Cannot be null");
        mCompositeDisposable = new CompositeDisposable();
        mFavoriteMoviesRepository = FavoriteMoviesRepository.getInstance();
        mMoviesView.setPresenter(this);
    }


    @Override
    public void subscribe() {
        if ((Constants.TabsType.FAVORITE_TAB).equals(MyApplication.getPrefManager().getString
                (Constants.PrefKeys.LAST_SELECTED_TAB))) {
            loadFavorites();
            return;
        }
        loadMovies(false);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }


    @Override
    public void loadMovies(boolean loadMore) {
        handlePagination(loadMore);
        mMoviesView.setLoadingIndicator(!loadMore);
        mCompositeDisposable.add(getMoviesObservable(getMoviesType())
                .map(MoviesResult::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Movie>>() {
                    @Override
                    public void onNext(List<Movie> movies) {
                        if (loadMore) {
                            mMoviesView.loadMoreMovies(movies);
                        } else {
                            mMoviesView.showMovies(movies);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMoviesView.setLoadingIndicator(false);
                        mMoviesView.showLoadingMoviesError(ErrorType.SERVER);
                    }

                    @Override
                    public void onComplete() {
                        mMoviesView.setLoadingIndicator(false);
                    }
                }));
    }

    private void handlePagination(boolean loadMore) {
        if (MyApplication.getPrefManager().getString(Constants.PrefKeys.LAST_SELECTED_TAB).equals
                (Constants.TabsType.POPULAR_TAB)) {
            mCurrentPopularPage = loadMore ? (mCurrentPopularPage += 1) : 1;

        } else {
            mCurrentTopRatedPage = loadMore ? (mCurrentTopRatedPage += 1) : 1;
        }
    }


    @Override
    public void openMovieDetails(Movie movie, ImageView sharedImage) {
        checkNotNull(movie);
        mMoviesView.showMovieDetailsUi(movie, sharedImage);
    }

    private Observable<Cursor> getFavorites() {
        return mFavoriteMoviesRepository.queryAllFavoriteMovies();
    }

    @Override
    public Observable<MoviesResult> getMoviesObservable(String type) {
        return Constants.MoviesType.POPULAR.equals(type) ?
                mAPIService.getPopularMovies(mCurrentPopularPage)
                : mAPIService.getTopRatedMovies(mCurrentTopRatedPage);
    }

    @Override
    public String getMoviesType() {
        return MyApplication.getPrefManager().getString(Constants.PrefKeys.MOVIES_TYPE_KEY,
                Constants.MoviesType.TOP_RATED);
    }

    @Override
    public void loadFavorites() {
        mMoviesView.setLoadingIndicator(true);
        mCompositeDisposable.add(getFavorites().subscribeWith(new DisposableObserver<Cursor>() {
            @Override
            public void onNext(Cursor c) {
                mMoviesView.showFavorites(c);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }
}

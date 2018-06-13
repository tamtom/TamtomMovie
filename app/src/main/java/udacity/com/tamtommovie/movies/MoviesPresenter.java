package udacity.com.tamtommovie.movies;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.util.ArrayList;
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
    private int mCurrentPopularPage = 1;
    private int mCurrentTopRatedPage = 1;
    private ArrayList<Movie> savedMoviesList;
    private Parcelable rvState;


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
    public void loadMovies(boolean refresh) {
        if (!refresh && savedMoviesList != null && !savedMoviesList.isEmpty()) {
            mMoviesView.showMovies(savedMoviesList);
            return;
        }
        mMoviesView.setLoadingIndicator(true);
        mCompositeDisposable.add(getMoviesObservable(getMoviesType())
                .map(MoviesResult::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Movie>>() {
                    @Override
                    public void onNext(List<Movie> movies) {
                        mMoviesView.showMovies(movies);
                        if (refresh && savedMoviesList != null)
                            savedMoviesList.clear();
                        if (savedMoviesList == null)
                            savedMoviesList = new ArrayList<>();
                        savedMoviesList.addAll(movies);
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

    @Override
    public void onSaveState(Bundle outstate, Parcelable rvState) {
        outstate.putParcelable(Constants.RV_STATE, rvState);
        outstate.putParcelableArrayList(Constants.MOVIES_STATE, savedMoviesList);


    }

    @Override
    public void onRestoreState(Bundle savedState) {
        savedMoviesList = savedState.getParcelableArrayList(Constants.MOVIES_STATE);
        rvState = savedState.getParcelable(Constants.RV_STATE);

    }

    @Override
    public void setMovies(ArrayList<Movie> movies) {
        savedMoviesList = movies;
    }

    @Override
    public Parcelable getRvState() {
        return rvState;
    }
}

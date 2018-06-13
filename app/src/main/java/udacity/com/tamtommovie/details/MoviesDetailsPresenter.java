package udacity.com.tamtommovie.details;

import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import udacity.com.tamtommovie.api.APIService;
import udacity.com.tamtommovie.api.RetrofitWrapper;
import udacity.com.tamtommovie.favorites.FavoriteMoviesRepository;
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.model.MovieReviews;
import udacity.com.tamtommovie.model.Video;
import udacity.com.tamtommovie.model.Videos;
import udacity.com.tamtommovie.util.Constants;

/**
 * Created by Omar AlTamimi on 3/20/2018.
 */

public class MoviesDetailsPresenter implements MoviesDetailsContract.Presenter {
    @NonNull
    private final APIService mAPIService;
    private final CompositeDisposable mCompositeDisposable;
    MoviesDetailsContract.View mView;
    private final FavoriteMoviesRepository mFavoriteMoviesRepository;

    public MoviesDetailsPresenter(MoviesDetailsContract.View view) {
        mView = view;
        mAPIService = RetrofitWrapper.getInstance().getApiService();
        mCompositeDisposable = new CompositeDisposable();
        mFavoriteMoviesRepository = FavoriteMoviesRepository.getInstance();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void loadMovieDetail(long id) {
        mCompositeDisposable.add(mAPIService.getMovieDetails(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Movie>() {
                    @Override
                    public void onNext(Movie movie) {
                        mView.onMovieLoaded(movie);
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
    public void loadMovieTrailers(long id) {
        mCompositeDisposable.add(mAPIService.getMovieTrailers(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Videos>() {
                    @Override
                    public void onNext(Videos videos) {
                        mView.onTrailersLoaded(videos);
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
    public void loadMovieReviews(long id) {
        mCompositeDisposable.add(mAPIService.getMovieReviws(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MovieReviews>() {
                    @Override
                    public void onNext(MovieReviews movieReviews) {
                        mView.onReviewsLoaded(movieReviews);
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
    public void getFavoriteStatus(long id) {
        mCompositeDisposable.add(mFavoriteMoviesRepository.checkMovieInFavorites(id)
                .subscribeWith(new DisposableObserver<Boolean>() {

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.updateFavoriteButton(aBoolean);
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
    public void addToFavorite(Movie movie) {
        mFavoriteMoviesRepository.addMovieToFavorites(movie);
    }

    @Override
    public void removeFromFavorite(Movie movie) {
        mFavoriteMoviesRepository.removeMovieFromFavorites(movie);

    }


}

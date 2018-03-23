package udacity.com.tamtommovie.details;

import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import udacity.com.tamtommovie.api.APIService;
import udacity.com.tamtommovie.api.RetrofitWrapper;
import udacity.com.tamtommovie.model.Movie;

/**
 * Created by Omar AlTamimi on 3/20/2018.
 */

public class MoviesDetailsPresenter implements MoviesDetailsContract.Presenter {
    @NonNull
    private final APIService mAPIService;
    private final CompositeDisposable mCompositeDisposable;
    MoviesDetailsContract.View mView;

    public MoviesDetailsPresenter(MoviesDetailsContract.View view) {
        mView = view;
        mAPIService = RetrofitWrapper.getInstance().getApiService();
        mCompositeDisposable = new CompositeDisposable();
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
        mAPIService.getMovieDetails(id)
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
                });
    }

}

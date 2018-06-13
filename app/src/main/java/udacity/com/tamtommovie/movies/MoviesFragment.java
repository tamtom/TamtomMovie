package udacity.com.tamtommovie.movies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import udacity.com.tamtommovie.MyApplication;
import udacity.com.tamtommovie.R;
import udacity.com.tamtommovie.base.BaseFragment;
import udacity.com.tamtommovie.details.MoviesDetailsActivity;
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.util.Constants;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;
import static com.google.common.base.Preconditions.checkNotNull;


public class MoviesFragment extends BaseFragment implements MoviesContract.View {
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_movies)
    RecyclerView mRvMovies;
    private Snackbar mSnackbar;
    @BindView(R.id.load_more_items)
    FrameLayout mLoadMoreView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mGridLayoutManager;

    private MoviesContract.Presenter mPresenter;
    private Parcelable rvState;


    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadMovies(true));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_movies;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void setPresenter(@NonNull MoviesContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter);

    }

    @Override
    public void setLoadingIndicator(boolean show) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(show));
        if (!show && mLoadMoreView.getVisibility() == View.VISIBLE) {
            mLoadMoreView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMovies(List<Movie> movies) {
        if (mMovieAdapter == null) {
            mMovieAdapter = new MovieAdapter(movies, (movie, sharedImage) -> {
                mPresenter.openMovieDetails(movie, sharedImage);

            });
            mRvMovies.setAdapter(mMovieAdapter);
            mGridLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R
                    .integer.grid_count));
            if (mPresenter.getRvState() != null)
                mGridLayoutManager.onRestoreInstanceState(mPresenter.getRvState());
            mRvMovies.setLayoutManager(mGridLayoutManager);
        } else {
            if (mPresenter.getRvState() != null)
                mGridLayoutManager.onRestoreInstanceState(mPresenter.getRvState());
            mGridLayoutManager.setSpanCount(getResources().getInteger(R
                    .integer.grid_count));
            mMovieAdapter.updateMovies(movies);
        }
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }

    }

    @Override
    public void showMovieDetailsUi(Movie movieItem, ImageView shareElement) {
        if (getActivity() == null)
            return;
        Intent intent = new Intent(getActivity(), MoviesDetailsActivity.class);
        intent.putExtra(MoviesDetailsActivity.EXTRA_MOVIE, movieItem);
        intent.putExtra(MoviesDetailsActivity.EXTRA_TRANSITION_NAME, ViewCompat.getTransitionName
                (shareElement));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                shareElement,
                ViewCompat.getTransitionName(shareElement));

        startActivity(intent, options.toBundle());
    }


    @Override
    public void showLoadingMoviesError(ErrorType errorType) {
        if (getView() == null)
            return;
        mSnackbar = Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), errorType
                .errorText, LENGTH_INDEFINITE).setAction
                (R.string.refresh, v -> {
                    mPresenter.loadMovies(true);
                    mSnackbar.dismiss();
                });
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                mSnackbar.getView().getLayoutParams();
        params.setMargins(0, 0, 0, getActivity().findViewById(R.id.navigation).getHeight());
        mSnackbar.getView().setLayoutParams(params);
        mSnackbar.show();

    }

    @Override
    public void showFavorites(Cursor cursor) {
        mMovieAdapter = null;
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter((movie, sharedImage) -> mPresenter
                .openMovieDetails(movie, sharedImage));
        mRvMovies.setAdapter(favoriteAdapter);
        mGridLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R
                .integer.grid_count));
        mRvMovies.setLayoutManager(mGridLayoutManager);
        favoriteAdapter.updateCursor(cursor);
        setLoadingIndicator(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mGridLayoutManager != null)
            mPresenter.onSaveState(outState, mGridLayoutManager.onSaveInstanceState());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPresenter.onRestoreState(savedInstanceState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMovieAdapter != null && mMovieAdapter.getItemCount() > 0)
            return;
        mPresenter.subscribe();
    }
}

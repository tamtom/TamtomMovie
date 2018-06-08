package udacity.com.tamtommovie.details;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import mehdi.sakout.fancybuttons.FancyButton;
import udacity.com.tamtommovie.R;
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.util.DateUtil;
import udacity.com.tamtommovie.util.ImageUtil;

import static com.google.common.base.Preconditions.checkNotNull;
import static udacity.com.tamtommovie.util.AnimationUtil.setWindowFlag;
import static udacity.com.tamtommovie.util.AnimationUtil.startAlphaAnimation;

public class MoviesDetailsActivity extends AppCompatActivity implements MoviesDetailsContract.View {

    public static final String EXTRA_MOVIE = "movie";
    public static final String EXTRA_TRANSITION_NAME = "transition_name";
    private static final int TOOLBAR_TITLE_VISIBILITY_THRESHOLD = 20; // in px
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 300;
    private boolean isToolbarTitleVisible = true;
    @BindView(R.id.moviePoster)
    ImageView ivPoster;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarTitle)
    AppCompatTextView mToolbarTitle;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appBarRoot)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.releaseDate)
    FancyButton tvReleaseDate;
    @BindView(R.id.movieTitle)
    TextView tvMovieTitle;
    @BindView(R.id.movieDuration)
    TextView tvMovieDuration;
    @BindView(R.id.movieRating)
    TextView tvMovieRating;
    @BindView(R.id.voteCount)
    TextView tvVoteCount;
    @BindView(R.id.movieOverview)
    TextView tvMovieOverview;
    @BindView(R.id.durationIcon)
    ImageView ivDurationIcon;
    @BindView(R.id.ratingBar)
    MaterialRatingBar mMaterialRatingBar;
    @BindView(R.id.movieBackdrop)
    ImageView ivmovieCover;
    @BindView(R.id.trailerProgress)
    ProgressBar trailerProgress;
    @BindView(R.id.trailers)
    RecyclerView rvTrailers;

    @BindView(R.id.reviewProgress)
    ProgressBar reviewProgress;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.addToFavoriteButton)
    FancyButton favoriteToggle;
    private MoviesDetailsContract.Presenter mPresenter;
    private Movie mMovie;
    private boolean isMovieInFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Bundle extras = getIntent().getExtras();
        checkNotNull(extras);
        Movie movie = extras.getParcelable(EXTRA_MOVIE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(EXTRA_TRANSITION_NAME);
            ivPoster.setTransitionName(imageTransitionName);
            supportPostponeEnterTransition();
        }
        mPresenter = new MoviesDetailsPresenter(this);
        mPresenter.loadMovieDetail(movie.getId());
        favoriteToggle.setEnabled(false);
        mPresenter.getFavoriteStatus(movie.getId());
        setupViews(movie);
        mMovie = movie;

    }

    private void setupViews(Movie movie) {
        Picasso.get()
                .load(ImageUtil.getPosterImage(movie.getPosterPath()))
                .noFade()
                .into(ivPoster, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        supportStartPostponedEnterTransition();
                    }

                });
        Picasso.get().load(ImageUtil.getCoverImage(movie.getBackdropPath()))
                .into(ivmovieCover);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mCollapsingToolbarLayout.setScrimAnimationDuration(DEFAULT_SCRIM_ANIMATION_DURATION);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, negOffset) -> {
            int totalScroll = appBarLayout.getTotalScrollRange();
            int currentScroll = totalScroll + negOffset;
            int toolbarHeight = mToolbar.getHeight();
            if (currentScroll <= toolbarHeight + TOOLBAR_TITLE_VISIBILITY_THRESHOLD) {
                if (!isToolbarTitleVisible) {
                    startAlphaAnimation(mToolbarTitle, DEFAULT_SCRIM_ANIMATION_DURATION, View
                            .VISIBLE);
                    isToolbarTitleVisible = true;
                }
            } else {
                if (isToolbarTitleVisible) {
                    startAlphaAnimation(mToolbarTitle, DEFAULT_SCRIM_ANIMATION_DURATION, View
                            .INVISIBLE);
                    isToolbarTitleVisible = false;
                }
            }
        });
        tvMovieTitle.setText(movie.getTitle());
        mToolbarTitle.setText(movie.getTitle());



    }

    @Override
    public void setPresenter(MoviesDetailsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onMovieLoaded(Movie movie) {
        tvMovieDuration.setText(formatMovieDuration(movie.getRuntime()));
        tvMovieDuration.setVisibility(View.VISIBLE);
        ivDurationIcon.setVisibility(View.VISIBLE);
        trailerProgress.setVisibility(View.GONE);
        reviewProgress.setVisibility(View.GONE);
        tvReleaseDate.setText(DateUtil.getYear(movie.getReleaseDate()));
        tvMovieOverview.setText(movie.getOverview());
        tvMovieRating.setText(String.valueOf(movie.getVoteAverage()));
        mMaterialRatingBar.setRating(mapNumberToRange(movie.getVoteAverage(), 0.0f, 10.0f, 0.0f,
                5.0f));
        Picasso.get().load(ImageUtil.getCoverImage(movie.getBackdropPath()))
                .into(ivmovieCover);

        setUpTrailers(movie);
        setUpReviews(movie);
        mMovie = movie;

    }

    private void setUpReviews(Movie movie) {
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(movie.getReviews().getReviews());
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setNestedScrollingEnabled(false);
        rvReviews.setAdapter(reviewsAdapter);

    }

    private void setUpTrailers(Movie movie) {
        VideosAdapter videosAdapter = new VideosAdapter(movie.getVideos().getVideos(), video -> {
            lunchYoutube(video.getKey());
        });
        rvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        rvTrailers.setAdapter(videosAdapter);
    }

    @Override
    public void lunchYoutube(String id) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube" +
                ".com/watch?v=" + id));
        String title = getResources().getString(R.string.youtube_video_choser_title);
        Intent chooser = Intent.createChooser(intent, title);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }

    }

    @Override
    public void updateFavoriteButton(boolean isFavorite) {
        isMovieInFav = isFavorite;
        favoriteToggle.setEnabled(true);
        favoriteToggle.setText(isFavorite ? getString(R.string.remove_from_favorites) : getString
                (R.string.add_to_favorites));
    }

    public float mapNumberToRange(float value, float iStart, float iEnd, float oStart, float oEnd) {
        return (value - iStart) * ((oEnd - oStart) / (iEnd - iStart)) + oStart;
    }

    public String formatMovieDuration(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format(Locale.US, "%dh %dm", hours, minutes);
    }

    @OnClick(R.id.addToFavoriteButton)
    void onFavoriteClick() {
        if (isMovieInFav) {
            mPresenter.removeFromFavorite(mMovie);
            Snackbar.make(findViewById(R.id.root), R.string.remove_msg, Snackbar.LENGTH_SHORT)
                    .show();
        } else {

            mPresenter.addToFavorite(mMovie);
            Snackbar.make(findViewById(R.id.root), R.string.add_msg, Snackbar.LENGTH_SHORT).show();

        }
        mPresenter.getFavoriteStatus(mMovie.getId());
    }


    @Override
    protected void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }

}

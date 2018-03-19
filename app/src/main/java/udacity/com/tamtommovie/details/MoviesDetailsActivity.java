package udacity.com.tamtommovie.details;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.tamtommovie.R;
import udacity.com.tamtommovie.model.Movie;
import udacity.com.tamtommovie.util.ImageUtil;

public class MoviesDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";
    public static final String EXTRA_TRANSITION_NAME = "transition_name";
    @BindView(R.id.moviePoster)
    ImageView ivPoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);


        Bundle extras = getIntent().getExtras();
        Movie movie = extras.getParcelable(EXTRA_MOVIE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(EXTRA_TRANSITION_NAME);
            ivPoster.setTransitionName(imageTransitionName);
            supportPostponeEnterTransition();
        }

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
    }
}

package udacity.com.tamtommovie.movies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.tamtommovie.BottomNavigationViewBehavior;
import udacity.com.tamtommovie.MyApplication;
import udacity.com.tamtommovie.R;
import udacity.com.tamtommovie.api.RetrofitWrapper;
import udacity.com.tamtommovie.util.Constants;

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    MyApplication.getPrefManager().putString(Constants.PrefKeys.MOVIES_TYPE_KEY,
                            Constants.MoviesType.POPULAR);
                    MyApplication.getPrefManager().putString(Constants.PrefKeys
                            .LAST_SELECTED_TAB, Constants.TabsType.POPULAR_TAB);
                    mMoviesPresenter.loadMovies(false);
                    setTitle(R.string.popular_movies);
                    return true;
                case R.id.navigation_top_rated:
                    MyApplication.getPrefManager().putString(Constants.PrefKeys.MOVIES_TYPE_KEY,
                            Constants.MoviesType.TOP_RATED);
                    MyApplication.getPrefManager().putString(Constants.PrefKeys
                            .LAST_SELECTED_TAB, Constants.TabsType.TOP_RATED_TAB);
                    mMoviesPresenter.loadMovies(false);
                    setTitle(R.string.top_rated_movies);
                    return true;
                case R.id.navigation_favorite:
                    MyApplication.getPrefManager().putString(Constants.PrefKeys
                            .LAST_SELECTED_TAB, Constants.TabsType.FAVORITE_TAB);
                    mMoviesPresenter.loadFavorites();
                    setTitle(R.string.title_menu_favorite);
                    return true;
            }
            return false;
        }
    };
    private MoviesPresenter mMoviesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                mNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        MoviesFragment moviesFragment =
                (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id
                        .fragment_container);
        if (moviesFragment == null) {
            // Create the fragment
            moviesFragment = MoviesFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, moviesFragment);
            transaction.commit();
        }
        // Create the presenter
        mMoviesPresenter = new MoviesPresenter(RetrofitWrapper.getInstance().getApiService(),
                moviesFragment);
        String lastSelectedTab = MyApplication.getPrefManager().getString(Constants.PrefKeys
                .LAST_SELECTED_TAB, Constants.TabsType.POPULAR_TAB);
        switch (lastSelectedTab) {
            case Constants.TabsType.POPULAR_TAB:
                mNavigationView.setSelectedItemId(R.id.navigation_popular);
                setTitle(R.string.popular_movies);
                break;
            case Constants.TabsType.TOP_RATED_TAB:
                mNavigationView.setSelectedItemId(R.id.navigation_top_rated);
                setTitle(R.string.top_rated_movies);
                break;
            case Constants.TabsType.FAVORITE_TAB:
                mNavigationView.setSelectedItemId(R.id.navigation_favorite);
                setTitle(R.string.title_menu_favorite);
                break;
        }
        mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

}

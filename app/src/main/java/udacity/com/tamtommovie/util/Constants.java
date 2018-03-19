package udacity.com.tamtommovie.util;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public interface Constants {
    String API_BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    String API_KEY = "cae403782bdd6c7ddad2bbffbff5c405";

    String PATH_POSTER_W185 = "w342";
    String PATH_BACKDROP_W780 = "w780";
    String POSTER_URL = IMAGE_BASE_URL + PATH_POSTER_W185;
    String BACKDROP_URL = IMAGE_BASE_URL + PATH_BACKDROP_W780;

    interface PrefKeys {
        String MOVIES_TYPE_KEY = "movies_type";
        String LAST_SELECTED_TAB = "last_tab";
    }

    interface MoviesType {
        String POPULAR = "popular";
        String TOP_RATED = "top_rated";
    }

    interface TabsType {
        String POPULAR_TAB = "popular_tab";
        String TOP_RATED_TAB = "top_rated_tab";
        String FAVORITE_TAB = "favorite_tab";
    }
}

package udacity.com.tamtommovie.favorites;

import android.net.Uri;
import android.provider.BaseColumns;

public final class FavoriteMoviesDbContract {
    private FavoriteMoviesDbContract() {}

    public static final String AUTHORITY = "udacity.com.tamtommovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favoriteMovies";

    public static class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favoriteMovies";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
        public static final String COLUMN_NAME_POSTER_URL = "posterUrl";

    }
}

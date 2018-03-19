package udacity.com.tamtommovie.movies;

import android.support.annotation.StringRes;

import udacity.com.tamtommovie.R;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public enum ErrorType {
    NETWORK(R.string.network_error), SERVER(R.string.server_error);
    @StringRes
    int errorText;

    ErrorType(@StringRes int errorText) {
        this.errorText = errorText;
    }
}

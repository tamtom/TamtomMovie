package udacity.com.tamtommovie.util;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public class ImageUtil {

    public  static String getPosterImage(String uriPath){
        return Constants.POSTER_URL + uriPath;
    }
    public  static String getCoverImage(String uriPath){
        return Constants.BACKDROP_URL + uriPath;
    }
}

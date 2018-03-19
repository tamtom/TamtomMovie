package udacity.com.tamtommovie.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import udacity.com.tamtommovie.model.MoviesResult;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public interface APIService {

    @GET("movie/popular")
    Observable<MoviesResult> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int
            page);

    @GET("movie/top_rated")
    Observable<MoviesResult> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page")
            int page);

}

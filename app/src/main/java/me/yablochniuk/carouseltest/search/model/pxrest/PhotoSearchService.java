package me.yablochniuk.carouseltest.search.model.pxrest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public interface PhotoSearchService {

    @GET("/v1/photos/search")
    Call<PhotosInfo> searchPhotos(
            @Query("term") String request,
            @Query("consumer_key") String token,
            @Query("rpp") int count);
}

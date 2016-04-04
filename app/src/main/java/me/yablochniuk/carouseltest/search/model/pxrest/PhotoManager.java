package me.yablochniuk.carouseltest.search.model.pxrest;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public class PhotoManager {
    private static final String BASE_URL = "https://api.500px.com";
    private static final String TOKEN = "REaIyphQMx5OIQfIAm65HBDkzyeU95QpDq2F78e9";

    private PhotoSearchService photoService;

    public PhotoManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        photoService = retrofit.create(PhotoSearchService.class);
    }

    public Call<PhotosInfo> getPhotos(String request, int count) {
        return photoService.searchPhotos(request, TOKEN, count);
    }
}

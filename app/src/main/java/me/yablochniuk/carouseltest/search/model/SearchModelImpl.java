package me.yablochniuk.carouseltest.search.model;

import java.util.ArrayList;
import java.util.List;

import me.yablochniuk.carouseltest.search.model.pxrest.PhotoManager;
import me.yablochniuk.carouseltest.search.model.pxrest.PhotosInfo;
import me.yablochniuk.carouseltest.search.presenter.RequiredSearchPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public class SearchModelImpl implements SearchModel {
    private static final int SEARCH_COUNT = 10;

    private RequiredSearchPresenter mPresenter;

    public SearchModelImpl(RequiredSearchPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void load(String request) {
        PhotoManager photoManager = new PhotoManager();
        Call<PhotosInfo> call = photoManager.getPhotos(request, SEARCH_COUNT);

        call.enqueue(new Callback<PhotosInfo>() {
            @Override
            public void onResponse(Call<PhotosInfo> call, Response<PhotosInfo> response) {
                List<String> urls = new ArrayList<>();
                for (PhotosInfo.Photo photo : response.body().getPhotos()) {
                    urls.add(photo.getImageUrl());
                }
                if (urls.isEmpty()) {
                    mPresenter.onError("Cannot find content");
                } else {
                    mPresenter.onLoaded(urls);
                }
            }

            @Override
            public void onFailure(Call<PhotosInfo> call, Throwable t) {
                mPresenter.onError(t.getMessage());
            }
        });
    }
}

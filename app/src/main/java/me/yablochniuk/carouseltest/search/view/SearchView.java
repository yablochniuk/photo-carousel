package me.yablochniuk.carouseltest.search.view;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public interface SearchView {
    void showPhotos(List<String> photos);
    void showLoading();
    void hideLoading();
    void showError(String message);
}

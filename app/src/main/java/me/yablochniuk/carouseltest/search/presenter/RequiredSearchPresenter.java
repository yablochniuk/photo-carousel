package me.yablochniuk.carouseltest.search.presenter;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public interface RequiredSearchPresenter {
    void onLoaded(List<String> photos);
    void onError(String message);
}

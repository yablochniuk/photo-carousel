package me.yablochniuk.carouseltest.search.presenter;

import java.lang.ref.WeakReference;
import java.util.List;

import me.yablochniuk.carouseltest.search.model.SearchModelImpl;
import me.yablochniuk.carouseltest.search.model.SearchModel;
import me.yablochniuk.carouseltest.search.view.SearchView;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public class SearchPresenterImpl implements RequiredSearchPresenter, OpsSearchPresenter {
    private WeakReference<SearchView> mView;
    private SearchModel mModel;

    public SearchPresenterImpl(SearchView view) {
        mView = new WeakReference<>(view);
        mModel = new SearchModelImpl(this);
    }

    @Override
    public void onLoaded(List<String> photos) {
        mView.get().hideLoading();
        mView.get().showPhotos(photos);
    }

    @Override
    public void onError(String message) {
        mView.get().hideLoading();
        mView.get().showError(message);
    }

    @Override
    public void requestPhotos(String request) {
        mView.get().showLoading();
        mModel.load(request);
    }
}

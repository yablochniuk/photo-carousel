package me.yablochniuk.carouseltest.search.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.yablochniuk.carouseltest.R;
import me.yablochniuk.carouseltest.carousel.CarouselActivity;
import me.yablochniuk.carouseltest.search.presenter.SearchPresenterImpl;
import me.yablochniuk.carouseltest.search.presenter.OpsSearchPresenter;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public class SearchActivity extends Activity implements SearchView {

    private OpsSearchPresenter mPresenter;
    private EditText mRequest;
    private Button mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        mPresenter = new SearchPresenterImpl(this);

        mRequest = (EditText) findViewById(R.id.request);
        mSearch = (Button) findViewById(R.id.search);

        mSearch.setOnClickListener(v -> {
            String r = mRequest.getText().toString().trim();
            mPresenter.requestPhotos(r);
        });

        mRequest.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                mSearch.performClick();
            }
            return false;
        });
    }

    @Override
    public void showPhotos(List<String> photos) {
        Intent intent = new Intent(this, CarouselActivity.class);
        intent.putStringArrayListExtra(CarouselActivity.EXTRA_IMAGES, (ArrayList<String>) photos);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        mSearch.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        mSearch.setEnabled(true);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

package me.yablochniuk.carouseltest.carousel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.yablochniuk.carouseltest.R;
import me.yablochniuk.carouseltest.details.DetailsActivity;

/**
 * Created by Vitalii Yablochniuk on 4/1/16
 */
public class CarouselActivity extends Activity {

    public static final String EXTRA_IMAGES = "images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout container = new CarouselLayout(this);
        setContentView(container);

        List<String> imageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGES);
        if (imageUrls != null && !imageUrls.isEmpty()) {
            fill(imageUrls, container);
        } else {
            Toast.makeText(this, getString(R.string.load_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void fill(List<String> imageUrls, FrameLayout container) {
        Display display = getWindowManager().getDefaultDisplay();
        Point resolution = new Point();
        display.getSize(resolution);

        int scale = 7;
        int childWidth = resolution.x / scale;
        int childHeight = resolution.y / scale;
        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(childWidth, childHeight);
        childParams.gravity = Gravity.CENTER;

        for (int i = 0; i < imageUrls.size(); i++) {
            PhotoView photoView = new PhotoView(this);
            photoView.setLayoutParams(childParams);

            float deg = i * 360f / imageUrls.size() - 90f;
            float rad = (float) (deg * Math.PI / 180f);

            photoView.setTranslationX((float) (300 * Math.cos(rad)));
            photoView.setTranslationY((float) (300 * Math.sin(rad)));

            photoView.setRotation(deg + 90f);

            container.addView(photoView);

            photoView.startLoading();
            Picasso.with(this).load(imageUrls.get(i)).into(photoView.getImageView(), new Callback() {
                @Override
                public void onSuccess() {
                    photoView.showImage();
                }

                @Override
                public void onError() {
                    photoView.showError();
                }
            });

            photoView.setOnClickListener(v -> {
                PhotoView photo = (PhotoView) v;
                if (photo.isLoaded()) {
                    Bitmap img = ((BitmapDrawable) photo.getImageView().getDrawable()).getBitmap();
                    Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.EXTRA_IMAGE, img);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.load_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

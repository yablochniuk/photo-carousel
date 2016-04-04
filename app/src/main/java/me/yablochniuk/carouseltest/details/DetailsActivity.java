package me.yablochniuk.carouseltest.details;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Vitalii Yablochniuk on 4/2/16
 */
public class DetailsActivity extends Activity {
    public static final String EXTRA_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView detail = new ImageView(this);
        setContentView(detail);

        Bitmap image = getIntent().getParcelableExtra(EXTRA_IMAGE);
        if (image != null) {
            detail.setImageBitmap(image);
        }
    }
}

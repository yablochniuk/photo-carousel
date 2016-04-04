package me.yablochniuk.carouseltest.carousel;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.yablochniuk.carouseltest.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Vitalii Yablochniuk on 4/1/16
 */
public class PhotoView  extends FrameLayout {

    private ImageView image;
    private ProgressBar progress;
    private TextView error;
    private boolean isLoaded;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        image = new ImageView(context);
        progress = new ProgressBar(context);
        LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        progress.setLayoutParams(layoutParams);

        error = new TextView(context);
        error.setText(context.getText(R.string.load_error));
        error.setLayoutParams(layoutParams);
    }

    public ImageView getImageView() {
        return image;
    }

    public void showImage() {
        isLoaded = true;
        removeAllViews();
        addView(image);
    }

    public void showError() {
        isLoaded = false;
        removeAllViews();
        addView(error);
    }

    public void startLoading() {
        isLoaded = false;
        removeAllViews();
        addView(progress);
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}

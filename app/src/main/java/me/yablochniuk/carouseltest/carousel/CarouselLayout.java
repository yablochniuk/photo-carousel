package me.yablochniuk.carouseltest.carousel;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import static java.lang.Math.abs;

/**
 * Created by Vitalii Yablochniuk on 4/2/16
 */
public class CarouselLayout extends FrameLayout {
    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 10;

    private static final int MIN_DURATION = 1000;
    private static final int MAX_DURATION = 10000;
    private static final int DEFAULT_DURATION = 3000;

    private boolean isClockwise;
    private ObjectAnimator rotateAnimator;
    private long animationPlayTime;
    private GestureDetector gestureDetector =
            new GestureDetector(getContext(), new GestureAnimator());

    public CarouselLayout(Context context) {
        this(context, null);
    }

    public CarouselLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startRotation();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CarouselLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        startRotation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return gestureDetector.onTouchEvent(ev);
    }

    private void startRotation() {
        rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        rotateAnimator.setDuration(DEFAULT_DURATION);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.start();
        isClockwise = true;
    }

    private void changeRotation(boolean clockwise, int speed) {
        pause();

        int duration = (MAX_DURATION + MIN_DURATION) - speed * MIN_DURATION;
        long currentDuration = rotateAnimator.getDuration();
        float circlePercent = (float) animationPlayTime / currentDuration;
        if (clockwise != isClockwise) circlePercent = 1 - circlePercent;

        rotateAnimator.setDuration(duration);
        isClockwise = clockwise;
        animationPlayTime = (long) (duration * circlePercent);

        resume();
    }

    private int getSpeed(float velocity) {
        if (velocity == 0) {
            return MIN_SPEED;
        }

        int max = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        float percent = Math.abs(velocity / max);

        int speed = (int) (MAX_SPEED * percent);
        return speed != 0 ? speed : MIN_SPEED;
    }

    private void pause() {
        animationPlayTime = rotateAnimator.getCurrentPlayTime();
        rotateAnimator.cancel();
    }

    private void resume() {
        if (!isClockwise) {
            rotateAnimator.reverse();
        } else {
            rotateAnimator.start();
        }
        rotateAnimator.setCurrentPlayTime(animationPlayTime);
    }

    private void onSwipe(Direction direction, Quarter quarter, float velocity) {
        boolean clockwise = true;
        switch (direction) {
            case LEFT:
                switch (quarter) {
                    case LEFT_BOTTOM:
                    case RIGHT_BOTTOM:
                        clockwise = true;
                        break;
                    case LEFT_TOP:
                    case RIGHT_TOP:
                        clockwise = false;
                        break;
                }
                break;
            case RIGHT:
                switch (quarter) {
                    case LEFT_TOP:
                    case RIGHT_TOP:
                        clockwise = true;
                        break;
                    case RIGHT_BOTTOM:
                    case LEFT_BOTTOM:
                        clockwise = false;
                        break;
                }
                break;
            case UP:
                switch (quarter) {
                    case LEFT_BOTTOM:
                    case LEFT_TOP:
                        clockwise = true;
                        break;
                    case RIGHT_TOP:
                    case RIGHT_BOTTOM:
                        clockwise = false;
                        break;
                }
                break;
            case DOWN:
                switch (quarter) {
                    case LEFT_BOTTOM:
                    case LEFT_TOP:
                        clockwise = false;
                        break;
                    case RIGHT_TOP:
                    case RIGHT_BOTTOM:
                        clockwise = true;
                        break;
                }
                break;
        }
        int speed = getSpeed(velocity);
        changeRotation(clockwise, speed);
    }

    private enum Direction {
        NONE, LEFT, RIGHT, UP, DOWN
    }

    private enum Quarter {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    private class GestureAnimator extends GestureDetector.SimpleOnGestureListener {

        private static final int FLING_THRESHOLD = 10;

        @Override
        public void onLongPress(MotionEvent e) {
            if (rotateAnimator.isRunning()) {
                pause();
            } else {
                resume();
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = Math.abs(velocityX);
            float y = Math.abs(velocityY);
            float velocity = x > y ? x : y;
            Direction direction = getDirection(e1, e2);
            Quarter quarter = getQuarter(e2);
            onSwipe(direction, quarter, velocity);
            return true;
        }

        private Quarter getQuarter(MotionEvent e) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            float x = e.getRawX();
            float y = e.getRawY();

            if (x <= w / 2) {
                if (y <= h / 2) {
                    return Quarter.LEFT_TOP;
                } else {
                    return Quarter.LEFT_BOTTOM;
                }
            } else {
                if (y < h / 2) {
                    return Quarter.RIGHT_TOP;
                } else {
                    return Quarter.RIGHT_BOTTOM;
                }
            }
        }

        private Direction getDirection(MotionEvent e1, MotionEvent e2) {
            float diffY = e2.getRawY() - e1.getRawY();
            float diffX = e2.getRawX() - e1.getRawX();
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > FLING_THRESHOLD) {
                    if (diffX > 0) {
                        return Direction.RIGHT;
                    } else {
                        return Direction.LEFT;
                    }
                }
            } else {
                if (abs(diffY) > FLING_THRESHOLD) {
                    if (diffY > 0) {
                        return Direction.DOWN;
                    } else {
                        return Direction.UP;
                    }
                }
            }

            return Direction.NONE;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }
}

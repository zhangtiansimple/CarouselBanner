package com.link.carouselbannerlibrary;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselBanner extends FrameLayout {

    //是否展示指示器
    private boolean isShowIndicator;

    //选中、未选中的指示器icon
    private Drawable mSelectDrawable, mUnSelectDrawable;

    //指示器的大小、间距
    private int size, space;

    //触摸点
    private int startX, startY;

    //轮播间隔
    private int mInterval;

    private int currentIndex;

    private boolean isPlaying;
    private boolean isTouched;
    private boolean isAutoPlaying;

    private List<Object> mData = new ArrayList<>();

    private Handler mHandler = new Handler();

    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;

    private CarouselAdapter mAdapter;

    private Runnable playTask = new Runnable() {
        @Override
        public void run() {
            mRecyclerView.smoothScrollToPosition(++currentIndex);
            if (isShowIndicator) {
                switchIndicator();
            }
            mHandler.postDelayed(this, mInterval);
        }
    };

    public void setCarouselBannerClickListener(CarouselBannerClickListener carouselBannerClickListener) {
        mAdapter.setClickListener(carouselBannerClickListener);
    }

    public void setCarouselSwitchBannerListener(CarouselSwitchBannerListener carouselSwitchBannerListener) {
        mAdapter.setListener(carouselSwitchBannerListener);
    }

    public CarouselBanner(@NonNull Context context) {
        this(context, null);
    }

    public CarouselBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarouselBanner);

        mInterval = typedArray.getInt(R.styleable.CarouselBanner_carousel_interval, CarouselConfig.INTERVAL);
        isShowIndicator = typedArray.getBoolean(R.styleable.CarouselBanner_carousel_interval, CarouselConfig.SHOW_INDICATOR);
        isAutoPlaying = typedArray.getBoolean(R.styleable.CarouselBanner_carousel_autoPlaying, CarouselConfig.AUTO_PLAYING);
        size = typedArray.getDimensionPixelSize(R.styleable.CarouselBanner_carousel_indicatorSize, 0);
        space = typedArray.getDimensionPixelSize(R.styleable.CarouselBanner_carousel_indicatorSpace, dp2px(CarouselConfig.DEFAULT_INDICATOR_SIZE));
        int margin = typedArray.getDimensionPixelSize(R.styleable.CarouselBanner_carousel_indicatorMargin, dp2px(CarouselConfig.DEFAULT_MARGIN));
        int g = typedArray.getInt(R.styleable.CarouselBanner_carousel_indicatorGravity, 1);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }

        Drawable selectDrawable = typedArray.getDrawable(R.styleable.CarouselBanner_carousel_indicatorSelected);
        Drawable unSelectDrawable = typedArray.getDrawable(R.styleable.CarouselBanner_carousel_indicatorUnselected);
        if (selectDrawable == null) {
            mSelectDrawable = generateDefaultDrawable(CarouselConfig.DEFAULT_SELECTED_COLOR);
        } else {
            if (selectDrawable instanceof ColorDrawable) {
                mSelectDrawable = generateDefaultDrawable(((ColorDrawable) selectDrawable).getColor());
            } else {
                mSelectDrawable = selectDrawable;
            }
        }
        if (unSelectDrawable == null) {
            mUnSelectDrawable = generateDefaultDrawable(CarouselConfig.DEFAULT_UNSELECTED_COLOR);
        } else {
            if (unSelectDrawable instanceof ColorDrawable) {
                mUnSelectDrawable = generateDefaultDrawable(((ColorDrawable) unSelectDrawable).getColor());
            } else {
                mUnSelectDrawable = unSelectDrawable;
            }
        }

        typedArray.recycle();

        mRecyclerView = new RecyclerView(context);
        mLinearLayout = new LinearLayout(context);

        new CarouselPagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new CarouselAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int next = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastVisibleItemPosition();
                if (next != currentIndex) {
                    currentIndex = next;
                    if (isShowIndicator && isTouched) {
                        isTouched = false;
                        switchIndicator();
                    }
                }
            }
        });
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);

        LayoutParams viewGroupParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.gravity = Gravity.BOTTOM | gravity;
        linearLayoutParams.setMargins(margin, margin, margin, margin);
        addView(mRecyclerView, viewGroupParams);
        addView(mLinearLayout, linearLayoutParams);
    }

    private void switchIndicator() {
        if (mLinearLayout != null && mLinearLayout.getChildCount() > 0) {
            for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
                ((AppCompatImageView) mLinearLayout.getChildAt(i)).setImageDrawable(
                        i == currentIndex % mData.size() ? mSelectDrawable : mUnSelectDrawable);
            }
        }
    }

    private GradientDrawable generateDefaultDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(dp2px(CarouselConfig.INDICATOR_SIZE), dp2px(CarouselConfig.INDICATOR_SIZE));
        gradientDrawable.setCornerRadius(dp2px(CarouselConfig.INDICATOR_SIZE));
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    private synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying) {
            if (!isPlaying && playing && mAdapter != null && mAdapter.getItemCount() > 2) {
                mHandler.postDelayed(playTask, mInterval);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                mHandler.removeCallbacksAndMessages(null);
                isPlaying = false;
            }
        }
    }

    private void createIndicators() {
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mData.size(); i++) {
            AppCompatImageView imageView = new AppCompatImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = space / 2;
            params.rightMargin = space / 2;
            if (size >= dp2px(CarouselConfig.DEFAULT_INDICATOR_SIZE)) {
                params.width = params.height = size;
            } else {
                // 最小设置为2dp
                imageView.setMinimumWidth(dp2px(CarouselConfig.DEFAULT_INDICATOR_MIN_SIZE));
                imageView.setMinimumHeight(dp2px(CarouselConfig.DEFAULT_INDICATOR_MIN_SIZE));
            }
            imageView.setImageDrawable(i == 0 ? mSelectDrawable : mUnSelectDrawable);
            mLinearLayout.addView(imageView, params);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //手动触摸的时候，停止自动播放，根据手势变换
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = moveX - startX;
                int disY = moveY - startY;
                //如果是横向滑动 则该事件由子View处理 如果为竖向滑动 则还是由父View处理
                boolean hasMoved = 2 * Math.abs(disX) > Math.abs(disY);
                getParent().requestDisallowInterceptTouchEvent(hasMoved);
                if (hasMoved) {
                    setPlaying(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isPlaying) {
                    isTouched = true;
                    setPlaying(true);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            setPlaying(false);
        } else if (visibility == VISIBLE) {
            setPlaying(true);
        }
        super.onWindowVisibilityChanged(visibility);
    }

    //以下为公有Api，供上层调用

    /**
     * 设置轮播数据集
     *
     * @param data Banner对象列表
     */
    public void setBannerData(List data) {
        setPlaying(false);
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        if (mData.size() > 1) {
            mAdapter.notifyDataSetChanged();
            currentIndex = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % mData.size();
            // 将起始点设为最靠近的 MAX_VALUE/2 的，且为mData.size()整数倍的位置
            mRecyclerView.scrollToPosition(currentIndex);
            if (isShowIndicator) {
                createIndicators();
            }
            setPlaying(true);
        } else {
            currentIndex = 0;
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置是否显示指示器导航点
     *
     * @param isShowIndicator 显示
     */
    public void isShowIndicator(boolean isShowIndicator) {
        this.isShowIndicator = isShowIndicator;
    }

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    public void setIndicatorInterval(int millisecond) {
        this.mInterval = millisecond;
    }

    /**
     * 设置是否禁止滚动播放
     *
     * @param isAutoPlaying
     * true:自动滚动播放 false:禁止自动滚动
     */
    public void setAutoPlaying(boolean isAutoPlaying) {
        this.isAutoPlaying = isAutoPlaying;
    }
}

package com.link.carouselbannerlibrary;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselAdapter extends RecyclerView.Adapter {

    private List<Object> list;
    private CarouselSwitchBannerListener listener;
    private CarouselBannerClickListener clickListener;

    CarouselAdapter(List<Object> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppCompatImageView imageView = new AppCompatImageView(parent.getContext());
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setId(R.id.carousel_image_view_id);
        imageView.setScaleType(AppCompatImageView.ScaleType.CENTER_CROP);
        return new RecyclerView.ViewHolder(imageView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AppCompatImageView imageView = holder.itemView.findViewById(R.id.carousel_image_view_id);
        if (listener != null) {
            listener.switchBanner(position % list.size(), imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(position % list.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size() < 2 ? list.size() : Integer.MAX_VALUE;
    }

    void setListener(CarouselSwitchBannerListener listener) {
        this.listener = listener;
    }

    void setClickListener(CarouselBannerClickListener listener) {
        this.clickListener = listener;
    }
}

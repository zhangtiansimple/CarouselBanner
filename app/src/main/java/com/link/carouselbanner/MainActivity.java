package com.link.carouselbanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.link.carouselbannerlibrary.CarouselBanner;
import com.link.carouselbannerlibrary.CarouselBannerClickListener;
import com.link.carouselbannerlibrary.CarouselSwitchBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<DataBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initCarouselBanner();
    }

    private void initData() {
        list.add(new DataBean(1, "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3903033279,2047048455&fm=26&gp=0.jpg"));
        list.add(new DataBean(2, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572414810272&di=e3756d0be24706ad904c8c8e8b7774f2&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201310%2F06%2F20131006014627_nXvCT.jpeg"));
        list.add(new DataBean(3, "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1227022093,2462118970&fm=26&gp=0.jpg"));
    }

    private void initCarouselBanner() {
        CarouselBanner carouselBanner = findViewById(R.id.carousel_banner);
        carouselBanner.setBannerData(list);
        carouselBanner.setCarouselSwitchBannerListener(new CarouselSwitchBannerListener() {
            @Override
            public void switchBanner(int position, AppCompatImageView bannerView) {
                Glide.with(MainActivity.this).load(list.get(position).getSrc()).into(bannerView);
            }
        });
        carouselBanner.setCarouselBannerClickListener(new CarouselBannerClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, String.valueOf(list.get(position).getId()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

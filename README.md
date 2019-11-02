# CarouselBanner
### 简单活好事少的自动轮播Banner

### 效果图
<center class="half">
    <img src="https://img-blog.csdnimg.cn/20191030170834381.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzMzMTUwNDE3,size_16,color_FFFFFF,t_70" width="300"/><img src="https://img-blog.csdnimg.cn/20191030170933466.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzMzMTUwNDE3,size_16,color_FFFFFF,t_70" width="300"/>
</center>

### 使用

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```
dependencies {
    implementation 'com.github.zhangtiansimple:CarouselBanner:1.0.0'
}
```

```
<com.link.carouselbannerlibrary.CarouselBanner
        android:id="@+id/carousel_banner"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

```
private List<DataBean> list = new ArrayList<>();

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

```

|可选属性 |取值|
|---|---|
| carousel_interval|轮播间隔，默认300毫秒|
| carousel_showIndicator|是否展示底部指示器，true or false|
| carousel_indicatorSelected|指示器选中，color or reference|
| carousel_indicatorUnselected|指示器未选中，color or reference|
| carousel_indicatorSize|指示器大小，dimension or reference|
| carousel_indicatorSpace|指示器间隔，dimension or reference|
| carousel_indicatorMargin|指示器Margin，dimension or reference|
| carousel_autoPlaying|是否自动轮播，true or false|
| carousel_indicatorGravity|指示器Gravity|

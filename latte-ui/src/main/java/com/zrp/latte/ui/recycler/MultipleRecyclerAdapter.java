package com.zrp.latte.ui.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.latte.ui.R;
import com.zrp.latte.ui.banner.BannerCreator;

import java.util.ArrayList;
import java.util.List;

public class MultipleRecyclerAdapter extends
        BaseMultiItemQuickAdapter<MultipleItemEntity,MultipleViewHolder>
        implements BaseQuickAdapter.SpanSizeLookup, OnItemClickListener {


    private boolean mIsInitBanner = false;

    private static final RequestOptions RECYCLE_OPTIONS =
            new RequestOptions()
                    //.centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    public static MultipleRecyclerAdapter create(List<MultipleItemEntity> data){
        return new MultipleRecyclerAdapter(data);
    }

    public static MultipleRecyclerAdapter create(DataConverter converter){
        return new MultipleRecyclerAdapter(converter.convert());
    }

    protected MultipleRecyclerAdapter(List<MultipleItemEntity> data) {
        super(data);
        init();
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        final String text;
        final String imgUrl;
        final ArrayList<String> bannerImages;

        switch (holder.getItemViewType()){
            case ItemType.SORT:
                text = entity.getField(MultipleFields.TEXT);
                imgUrl = entity.getField(MultipleFields.IMAGE_URL);
                holder.setText(R.id.tv_sort, text);
                Glide.with(mContext)
                        .load(imgUrl)
                        .apply(RECYCLE_OPTIONS)
                        .into((ImageView) holder.getView(R.id.iv_sort));
                break;
            case ItemType.TEXT_IMAGE:
                text = entity.getField(MultipleFields.TEXT);
                imgUrl = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(imgUrl)
                        .apply(RECYCLE_OPTIONS)
                        .into((ImageView) holder.getView(R.id.img_multiple));
                holder.setText(R.id.tv_multiple, text);
                break;
            case ItemType.IMAGE:
                imgUrl = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(imgUrl)
                        .apply(RECYCLE_OPTIONS)
                        .into((ImageView) holder.getView(R.id.img_single));
            case ItemType.BANNER:
                if(!mIsInitBanner){
                    //初始化
                    bannerImages = entity.getField(MultipleFields.BANNERS);
                    final ConvenientBanner<String> convenientBanner = holder.getView(R.id.banner_recycler_item);
                    //BannerCreator
                    BannerCreator.setDefault(convenientBanner, bannerImages,this);
                    mIsInitBanner = true;
                }
                String gifUrl = entity.getField(MultipleFields.BANNER_GIF);
                String textUrl = entity.getField(MultipleFields.BANNER_TEXT);
                Glide.with(mContext)
                        .load(gifUrl)
                        .apply(RECYCLE_OPTIONS)
                        .into((ImageView) holder.getView(R.id.iv_index_gif));
                Glide.with(mContext)
                        .load(textUrl)
                        .apply(RECYCLE_OPTIONS)
                        .into((ImageView) holder.getView(R.id.iv_index_ad));
        }
    }
    private void init(){
        //设置不同的Item布局
        //addItemType(ItemType.TEXT, R.layout.item_multiple_text);
        //addItemType(ItemType.IMAGE, R.layout.item_multiple_image);
        //addItemType(ItemType.TEXT_IMAGE, R.layout.item_multiple_image_text);
        addItemType(ItemType.BANNER, R.layout.item_multiple_banner_new);
        addItemType(ItemType.SORT, R.layout.item_multiple_sort);

        //设置宽度的监听
        setSpanSizeLookup(this);
        openLoadAnimation();
        //show anim when load the data every time
        isFirstOnly(false);

    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return getData().get(position).getField(MultipleFields.SPAN_SIZE);
    }

    @Override
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        super.setSpanSizeLookup(spanSizeLookup);
    }

    @Override
    protected MultipleViewHolder createBaseViewHolder(View view) {
        return MultipleViewHolder.create(view);
    }

    @Override
    public void onItemClick(int position) {

    }

}

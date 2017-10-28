package com.johndon.cmcc.zixun;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by wanglin on 17-4-6.
 */

public class PushMessageAdapter extends RecyclerView.Adapter {
    private ArrayList<Story> mStories;
    private RVItemClickListener mItemClickListener;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public PushMessageAdapter(Context context) {
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void setItemClickListener(RVItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setStories(ArrayList<Story> stories) {
        mStories = stories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pushmessage,parent,false);
        return new PushMessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Story story = mStories.get(position);
        ((PushMessageViewHolder)holder).mTitleTv.setText(story.getTitle());
        mImageLoader.displayImage(story.getImageUrl(),((PushMessageViewHolder) holder).mImageView,mOptions);
    }

    @Override
    public int getItemCount() {
        return mStories != null ? mStories.size() : 0;
    }

    private class PushMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTv;
        private ImageView mImageView;

        private PushMessageViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.pushmessage_title);
            mImageView = (ImageView) itemView.findViewById(R.id.pushmessage_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}

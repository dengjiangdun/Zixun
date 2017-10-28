package com.johndon.cmcc.zixun;

/**
 * Created by wanglin on 17-4-6.
 */

public class Story {
    private String mId;
    private String mTitle;
    private String mImageUrl;

    public Story() {
        mTitle = "";
        mImageUrl = "";
    }

    public Story(String title, String imageUrl, String id) {
        mTitle = title;
        mImageUrl = imageUrl;
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}

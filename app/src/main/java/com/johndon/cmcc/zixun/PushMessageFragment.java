package com.johndon.cmcc.zixun;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * Created by wanglin on 17-4-6.
 */

public class PushMessageFragment extends BaseFragment {
    private static final String HTTP_LASTEST_STORY = "http://news-at.zhihu.com/api/4/news/latest";
    private static final String HTTP_BEFORE_STORY = "http://news.at.zhihu.com/api/4/news/before/";
    private Context mContext;
    private ArrayList<Story> mLatestStories;
    private ArrayList<Story> mBeforeStories;
    private ArrayList<Story> mStories;
    private PushMessageAdapter mAdapter;
    private boolean isGettingNews;
    private SwipeRefreshLayout mRefreshLayout;

    private HttpUtils mHttpUtils;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isGettingNews = false;
            mRefreshLayout.setRefreshing(false);
            if (msg.what == 1) {
                updateStories();
                mAdapter.setStories(mStories);
                mAdapter.notifyDataSetChanged();
            }else if (msg.what == 2){
                int position = mStories.size();
                updateStories();
                mAdapter.setStories(mStories);
                mAdapter.notifyItemInserted(position);
            }
        }
    };

    @Override
    protected int getResId() {
        return R.layout.swiperefresh_recyclerview;
    }

    @Override
    protected void init(View view, Context context) {
        mContext = context;
        mHttpUtils = new HttpUtils(getContext());
        mLatestStories = new ArrayList<>();
        mBeforeStories = new ArrayList<>();
        mStories = new ArrayList<>();
        isGettingNews = false;

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new PushMessageAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new RVItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("newsTitle", mStories.get(position).getTitle());
                intent.putExtra("newsDetails", mStories.get(position).getImageUrl());
                intent.putExtra("id", mStories.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    getBeforeNews();
                }
            }
        });


        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLatestNews();
            }
        });
        getLatestNews();
    }

    private void getLatestNews() {
        if (mHttpUtils.isNetWorkAvailable() && !isGettingNews) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        isGettingNews = true;
                        Message msg = new Message();
                        msg.what = 0;
                        String json = mHttpUtils.getUrl(HTTP_LASTEST_STORY);
                        if (!json.equals(HttpUtils.CONNECT_FAIL)) {
                            JSONTokener jsonTokener = new JSONTokener(json);
                            JSONObject news = (JSONObject) jsonTokener.nextValue();
                            JSONArray stories = news.getJSONArray("stories");
                            if (stories.length() != mLatestStories.size()) {
                                mLatestStories.clear();
                                for (int i = 0; i < stories.length(); i++) {
                                    JSONObject story = (JSONObject) stories.get(i);
                                    JSONArray images = story.getJSONArray("images");
                                    Story pushMessage = new Story(story.getString("title"), images.get(0).toString(), String.valueOf(story.getInt("id")));
                                    mLatestStories.add(pushMessage);
                                }
                                msg.what = 1;
                            }
                        }
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private long date = 20170331;

    private void getBeforeNews() {
        if (mHttpUtils.isNetWorkAvailable() && !isGettingNews) {
            final String s = String.valueOf(date);
            date -= 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        isGettingNews = true;
                        Message msg = new Message();
                        msg.what = 0;
                        String json = mHttpUtils.getUrl(HTTP_BEFORE_STORY + s);
                        if (!json.equals(HttpUtils.CONNECT_FAIL)) {
                            JSONTokener jsonTokener = new JSONTokener(json);
                            JSONObject news = (JSONObject) jsonTokener.nextValue();
                            JSONArray stories = news.getJSONArray("stories");
                            for (int i = 0; i < stories.length(); i++) {
                                JSONObject story = (JSONObject) stories.get(i);
                                JSONArray images = story.getJSONArray("images");
                                Story pushMessage = new Story(story.getString("title"), images.get(0).toString(), String.valueOf(story.getInt("id")));
                                mBeforeStories.add(pushMessage);
                            }
                            msg.what = 2;
                        }
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void updateStories() {
        mStories.clear();
        mStories.addAll(0, mBeforeStories);
        mStories.addAll(0, mLatestStories);
    }
}

package com.johndon.cmcc.zixun;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class HttpUtils {
    public static final String CONNECT_FAIL = "获取数据失败";
    private Context mContext;

    public HttpUtils(Context context) {
        mContext = context;
    }

    public boolean isNetWorkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        } else {
            return false;
        }
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }
        InputStream in = connection.getInputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = in.read(buffer))>0){
            out.write(buffer,0,bytesRead);
        }
        in.close();
        out.close();
        connection.disconnect();
        return out.toByteArray();
    }

    public String getUrl(String urlSpec) throws IOException{
        byte[] urlBytes = getUrlBytes(urlSpec);
        if (urlBytes != null) {
            return new String(urlBytes);
        }else{
            return CONNECT_FAIL;
        }
    }
}

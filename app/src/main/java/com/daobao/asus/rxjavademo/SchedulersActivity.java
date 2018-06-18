package com.daobao.asus.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


/**
 * Created by db on 2018/6/16.
 */

public class SchedulersActivity extends AppCompatActivity {
    private TextView mTextView;
    private String TAG = "Rxjava";
    private Retrofit retrofit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulers);
        mTextView = findViewById(R.id.mText_schedulers);
        OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        //获取Retrofit对象，设置地址
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost")
                .client(OK_HTTP_CLIENT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void OnClick(View view) {

        RequestServices requestServices = retrofit.create(RequestServices.class);
        Map<String, String> pamars1 = new HashMap<>();
        pamars1.put("key", "value");
        Map<String, String> pamars2 = new HashMap<>();
        pamars2.put("key", "value");
        requestServices.post("请求url地址",pamars1)//这里我们即可获得一个Observable对象
                .mergeWith(requestServices.post("请求url地址",pamars2))//合并两条网络请求
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Log.d(TAG,responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    public interface RequestServices {
        @GET//定义返回的方法，返回的响应体使用了ResponseBody
        Observable<ResponseBody> getString(@Url String url);
        @FormUrlEncoded
        @POST
        Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> params);
    }
}

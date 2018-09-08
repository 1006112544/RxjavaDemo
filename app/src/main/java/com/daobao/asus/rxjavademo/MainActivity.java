package com.daobao.asus.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


public class MainActivity extends AppCompatActivity {

    private String TAG = "Rxjava";
    private String text = "xlx";
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.mText);
    }

    public void OnClick(View view) {
        Observable<String> observable = getObservable();
        Observer<String> observer = getObserver();
        observable.subscribe(observer);
//        Observable<String> observable = getObservable();
//        observable.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, "accept:");
//                mTextView.setText(s);
//            }
//        });
    }

    public Observable<String> getObservable(){
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                emitter.onNext(text);
//                //emitter.onComplete();
//            }
//        });
//
//        return Observable.fromCallable(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return "发送一条信息";
//            }
//        });

        return Observable.just("发送一条消息1","发送一条消息2").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return "收到："+s;
            }
        });//just和fromArray等同
    }

    public Observer<String> getObserver(){
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                //d.dispose();取消订阅
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: "+s);
                mTextView.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        return observer;
    }
}

package com.yisingle.app.rx;


import com.yisingle.app.http.ApiException;
import com.yisingle.app.http.HttpResult;
import com.yisingle.app.http.NetworkDisconnectException;
import com.yisingle.app.utils.NetUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yu on 2017/5/9.
 */

public class RxUtils {

    /**
     * 访问网络通用Transformer，判断了网络，切换线程，剥离结果  返回子类
     */
    public static <T> Observable.Transformer<HttpResult<T>, T> apiChildTransformer() {
        return new Observable.Transformer<HttpResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<HttpResult<T>> tObservable) {
                return tObservable
                        .onBackpressureLatest()
                        .compose(checkNetwork())
                        .compose(ioToMain())
                        .compose(parseResult());
            }
        };
    }

    /**
     * 访问网络通用Transformer，判断了网络，切换线程，剥离结果 返回相同的参数类
     */
    public static <T> Observable.Transformer<HttpResult<T>, HttpResult<T>> apiSameTransformer() {
        return new Observable.Transformer<HttpResult<T>, HttpResult<T>>() {
            @Override
            public Observable<HttpResult<T>> call(Observable<HttpResult<T>> tObservable) {
                return tObservable
                        .onBackpressureLatest()
                        .compose(checkNetwork())
                        .compose(ioToMain())
                        .compose(parseSameResult());
            }
        };
    }

    /**
     * io线程到主线程
     */
    public static <T> Observable.Transformer<T, T> ioToMain() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 检查网络
     */
    public static <T> Observable.Transformer<T, T> checkNetwork() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!NetUtils.isOnline())
                                    throw new NetworkDisconnectException();
                            }
                        });
            }
        };
    }

    /**
     * 剥离接口返回的对象
     */
    public static <T> Observable.Transformer<HttpResult<T>, T> parseResult() {
        return new Observable.Transformer<HttpResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<HttpResult<T>> tObservable) {
                return tObservable
                        .flatMap(result -> {
                            if (0 == result.getRet_code())
                                return Observable.just(result.getResponse());
                            return Observable.error(new ApiException(result.getRet_code() + "", result.getRet_string()));

                        });
            }
        };
    }


    /**
     * 剥离接口返回的对象
     */
    public static <T> Observable.Transformer<HttpResult<T>, HttpResult<T>> parseSameResult() {


        return new Observable.Transformer<HttpResult<T>, HttpResult<T>>() {
            @Override
            public Observable<HttpResult<T>> call(Observable<HttpResult<T>> httpResultObservable) {
                return httpResultObservable.flatMap(new Func1<HttpResult<T>, Observable<HttpResult<T>>>() {
                    @Override
                    public Observable<HttpResult<T>> call(HttpResult<T> result) {
                        if (0 == result.getRet_code())
                            return Observable.just(result);
                        return Observable.error(new ApiException(result.getRet_code() + "", result.getRet_string()));
                    }
                });
            }
        };

    }


}

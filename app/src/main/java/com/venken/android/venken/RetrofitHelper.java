package com.venken.android.venken;

import android.text.TextUtils;

import com.fota.android.commonlib.http.HttpsUtils;
import com.fota.android.commonlib.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.fota.android.commonlib.base.AppConfigs.getIpAddress;

public class RetrofitHelper {

    private static Retrofit retrofit;

    public static String loginId;
    public static String authorization;
    private static Api api;

    /**
     * @return retrofit的底层利用反射的方式, 获取所有的api接口的类
     */
    public static Api api() {
        if (api == null) {
            api = getRetrofit().create(Api.class);
        }
        return api;
    }


    /**
     * 获取网络
     *
     * @return
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitHelper.class) {
                //添加一个log拦截器,打印所有的log
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                //可以设置请求过滤的水平,body,basic,headers
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                /**
                 * 创建Retrofit实例时需要通过Retrofit.Builder,并调用baseUrl方法设置URL。
                 * Retrofit2的baseUlr 必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
                 */
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://jia3.tmsf.com/") // 设置 网络请求 Url
                        .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                        .build();
                HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
                OkHttpClient.Builder builder = new OkHttpClient
                        .Builder()
                        .hostnameVerifier(sslParams.hostnameVerifier)
                        .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                        .addInterceptor(addHeaderInterceptor(null)) // token过滤
                        .connectTimeout(5L, TimeUnit.SECONDS)
                        .readTimeout(10L, TimeUnit.SECONDS)
                        .writeTimeout(20L, TimeUnit.SECONDS);
                if (BuildConfig.DEBUG) {
                    builder.addInterceptor(httpLoggingInterceptor); //日志,debug下可看到
                }
                OkHttpClient client = builder.build();
                // 获取retrofit的实例
                retrofit = new Retrofit
                        .Builder()
                        .baseUrl("http://jia3.tmsf.com/")  //自己配置
                        .client(client)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create()) //这里是用的fastjson的
                        .build();
                return retrofit;
            }
        }
        return retrofit;
    }

    /**
     * 设置头
     */
    private static Interceptor addHeaderInterceptor(final String apiVersion) {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        // Provide your custom header here
                        .header("Host", "jia3.tmsf.com")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent", "Mozilla/5.0 (Linux; Android 9; LYA-AL00 Build/HUAWEILYA-AL00L; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36")
                        .header("Accept-Language", "zh-CN,en-US;q=0.9")
                        .header("Cookie", "ROUTEID=.lb4; JSESSIONID=AD347341025760A27FDC92AECEA994B1.lb4")
                        .header("X-Requested-With", "com.tmsf.jia")
                        .method(originalRequest.method(), originalRequest.body());
                if (!TextUtils.isEmpty(apiVersion)) {
                    requestBuilder.header("API-Version", apiVersion);
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }




}

package com.atmk.base.http.retrofit;
import android.app.Activity;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.atmk.base.manager.ActivityManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Okhttp接口请求拦截器，判断token是否过期，若token过期则跳转至登录页面
 */
public class TokenInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        //根据和服务端的约定判断token过期
        if (isTokenExpired(response)) {
            //token过期 跳转到登录页
            Activity obj = (Activity) ARouter.getInstance()
                    .build("/login/loginAc")
                    .navigation();
            ActivityManager.Companion.getInstance().finishAllActivities(obj.getClass());
            //清空登录信息
//            SharepreferenceUtils.clearUser(AppContext.getInstance());
//            SharepreferenceUtils.clearIsAlive(AppContext.getInstance());

        }
        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     */
    private boolean isTokenExpired(Response response) {
        if (response.code() == 401) {
            return true;
        }
        return false;
    }
}

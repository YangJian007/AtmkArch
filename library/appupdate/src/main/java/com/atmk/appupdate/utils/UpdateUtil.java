package com.atmk.appupdate.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.atmk.appupdate.AppUpdateManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Time:2020/12/31
 * <p>
 * Author:jiangshuang
 * <p>
 * Description:类描述
 */
public class UpdateUtil {

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    public static void getAndUpdate(Activity activity, String apkId) {
        HashMap reqBody = new HashMap();
        Map<String, String> reqBody2 = new HashMap();
        reqBody2.put("apkId", apkId);
        reqBody2.put("versionNumber", getVerName(activity));
        JSONObject json = new JSONObject(reqBody2);
        reqBody.put("name", "appOpex.update");
        reqBody.put("data", json.toString());
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet("https://app.bjatmk.com/atmk/api", reqBody, new NetUtils.MyNetCall() {
            @Override
            public void success(Call call, Response response) throws IOException, JSONException {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                String download_url = jsonObject2.optString("download_url");
                String version_msg = jsonObject2.optString("version_msg");
                int force_update = jsonObject2.optInt("force_update");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppUpdateManager.Builder builder = new AppUpdateManager.Builder(activity);
                        builder.apkUrl(download_url)
                                .updateContent(version_msg.split(","))
                                .updateForce(force_update == 0 ? false : true).build();
                    }
                });

            }

            @Override
            public void failed(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "没有可以更新的版本", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

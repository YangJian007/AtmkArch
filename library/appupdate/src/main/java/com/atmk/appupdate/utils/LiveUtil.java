package com.atmk.appupdate.utils;

import android.app.Activity;
import android.widget.Toast;

import com.atmk.appupdate.AppUpdateManager;
import com.atmk.feedback.SuggestFeedBackActivity;

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
public class LiveUtil {
    public static void addActivityCount(String apkId) {
        HashMap reqBody = new HashMap();
        Map<String, String> reqBody2 = new HashMap();
        reqBody2.put("apkId", apkId);
        JSONObject json = new JSONObject(reqBody2);
        reqBody.put("name", "appOpex.addActivityCount");
        reqBody.put("data", json.toString());
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet("https://app.bjatmk.com/atmk/api", reqBody, new NetUtils.MyNetCall() {
            @Override
            public void success(Call call, Response response) throws IOException, JSONException {


            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });
    }
}

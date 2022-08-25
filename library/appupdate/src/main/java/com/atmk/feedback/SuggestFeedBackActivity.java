package com.atmk.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.atmk.appupdate.utils.NetUtils;
import com.hailong.appupdate.R;

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
public class SuggestFeedBackActivity extends Activity {
    private EditText editContent;
    private ImageView ivBack;
    private Button btSubmit;
    private String apkId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appupdate_update_activity_feed_back);
        editContent = (EditText) findViewById(R.id.et_content);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btSubmit = (Button) findViewById(R.id.bt_submit);
        apkId = getIntent().getStringExtra("apkId");
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(apkId) && !TextUtils.isEmpty(editContent.getText().toString())) {
                    sendFeedBack(editContent.getText().toString(),apkId);

                } else {
                    Toast.makeText(SuggestFeedBackActivity.this, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void sendFeedBack(String feedbackMsg, String apkId) {
        Map<String, String> reqBody = new HashMap();
        Map<String, String> reqBody2 = new HashMap();
        reqBody2.put("apkId", apkId);
        reqBody2.put("feedbackMsg", feedbackMsg);
        JSONObject json = new JSONObject(reqBody2);
        reqBody.put("name", "appOpex.feedbackSubmit");
        reqBody.put("data", json.toString());
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet("https://app.bjatmk.com/atmk/api", reqBody, new NetUtils.MyNetCall() {
            @Override
            public void success(Call call, Response response) throws IOException, JSONException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SuggestFeedBackActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void failed(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SuggestFeedBackActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

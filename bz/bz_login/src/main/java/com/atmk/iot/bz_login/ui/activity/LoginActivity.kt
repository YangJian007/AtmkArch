package com.atmk.iot.bz_login.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.atmk.iot.bz_login.R
import com.atmk.base.aop.Log
import com.atmk.base.aop.SingleClick
import com.atmk.base.app.AppActivity

import com.atmk.base.http.model.HttpData
import com.atmk.base.manager.InputTextManager
import com.atmk.base.other.KeyboardWatcher
import com.atmk.iot.bz_login.api.LoginApi
import com.atmk.iot.bz_login.api.VersionCodeApi
import com.gyf.immersionbar.ImmersionBar
import com.hjq.http.EasyConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.widget.view.SubmitButton

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 登录界面
 */
@Route(path = "/login/loginAc")
class LoginActivity : AppActivity(), KeyboardWatcher.SoftKeyboardStateListener,
        TextView.OnEditorActionListener {

    companion object {

        private const val INTENT_KEY_IN_USERNAME: String = "username"
        private const val INTENT_KEY_IN_PASSWORD: String = "password"

        @Log
        fun start(context: Context, username: String?, password: String?) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_USERNAME, username)
            intent.putExtra(INTENT_KEY_IN_PASSWORD, password)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val logoView: ImageView? by lazy { findViewById(R.id.iv_login_logo) }
    private val bodyLayout: ViewGroup? by lazy { findViewById(R.id.ll_login_body) }
    private val usernameView: EditText? by lazy { findViewById(R.id.et_login_name) }
    private val passwordView: EditText? by lazy { findViewById(R.id.et_login_password) }
    private val commitView: SubmitButton? by lazy { findViewById(R.id.btn_login_commit) }
    private val etVersionCode: EditText? by lazy { findViewById(R.id.et_version_code) }
    private val ivVersionCode: ImageView? by lazy { findViewById(R.id.iv_version_code) }
    private lateinit var key:String

    /** logo 缩放比例 */
    private val logoScale: Float = 0.8f

    /** 动画时间 */
    private val animTime: Int = 300

    override fun getLayoutId(): Int {
        return R.layout.login_activity
    }

    override fun initView() {
        setOnClickListener(commitView, ivVersionCode)
        passwordView?.setOnEditorActionListener(this)
        commitView?.let {
            InputTextManager.with(this)
                    .addView(usernameView)
                    .addView(passwordView)
                    .addView(etVersionCode)
                    .setMain(it)
                    .build()
        }
    }

    override fun initData() {
        postDelayed({
            KeyboardWatcher.with(this@LoginActivity)
                    .setListener(this@LoginActivity)
        }, 500)

        getVersionCode()
        // 自动填充手机号和密码
        usernameView?.setText(getString(INTENT_KEY_IN_USERNAME))
        passwordView?.setText(getString(INTENT_KEY_IN_PASSWORD))
//        usernameView?.setText("yangjian")
//        passwordView?.setText("YAJyaj381")
    }


    @SingleClick
    override fun onClick(view: View) {

        if (view === commitView) {
            // 隐藏软键盘
            hideKeyboard(currentFocus)
            EasyHttp.post(this)
                    .api(LoginApi().apply {
                        username=usernameView?.text.toString()
                        password=passwordView?.text.toString()
                        verifyCode=etVersionCode?.text.toString()
                        verifyKey=key
                    })
                    .request(object : OnHttpListener<HttpData<LoginApi.Bean?>> {
                        override fun onSucceed(result: HttpData<LoginApi.Bean?>?) {
                            // 更新 Token
                            EasyConfig.getInstance().addHeader("X-Access-Token", result?.getData()?.token)
//                            toast(result?.getData()?.token)
                            postDelayed({
                                commitView?.showSucceed()
                                postDelayed({
                                    // 跳转到首页
                                    toast("登陆成功")
//                                    HomeActivity.start(getContext(), HomeFragment::class.java)
                                    startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                                    finish()
                                }, 1000)
                            }, 1000)
//                        test()
                        }

                        override fun onFail(e: java.lang.Exception?) {
                            commitView?.showError(3000)
                            toast("登录失败")
                        }

                    })
            return
        }
        if (view === ivVersionCode) {
            getVersionCode()
            return
        }
    }

    /**
     * 获取验证码
     */
    private fun getVersionCode() {
        EasyHttp.get(this)
                .api(VersionCodeApi())
                .request(object : OnHttpListener<HttpData<VersionCodeApi.Bean?>> {
                    override fun onSucceed(result: HttpData<VersionCodeApi.Bean?>?) {
                        //展示验证码图片
                        val decodedString: ByteArray = Base64.decode(
                                result?.getData()?.base64?.split(",")?.toTypedArray()?.get(1),
                                Base64.DEFAULT
                        )
                        val decodedByte =
                                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        ivVersionCode?.setImageBitmap(decodedByte)
                        //存储key
                        key = result?.getData()?.key.toString()
                    }

                    override fun onFail(e: java.lang.Exception?) {
                        toast("验证码获取失败")
                    }

                })
    }



    /**
     * [KeyboardWatcher.SoftKeyboardStateListener]
     */
    override fun onSoftKeyboardOpened(keyboardHeight: Int) {
        // 执行位移动画
        bodyLayout?.let {
            val objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(
                    it,
                    "translationY", 0f, (-(commitView?.height?.toFloat() ?: 0f))
            )
            objectAnimator.duration = animTime.toLong()
            objectAnimator.interpolator = AccelerateDecelerateInterpolator()
            objectAnimator.start()
        }

        // 执行缩小动画
        logoView?.let {
            it.pivotX = it.width / 2f
            it.pivotY = it.height.toFloat()
            val animatorSet = AnimatorSet()
            val scaleX = ObjectAnimator.ofFloat(it, "scaleX", 1f, logoScale)
            val scaleY = ObjectAnimator.ofFloat(it, "scaleY", 1f, logoScale)
            val translationY = ObjectAnimator.ofFloat(
                    it, "translationY",
                    0f, (-(commitView?.height?.toFloat() ?: 0f))
            )
            animatorSet.play(translationY).with(scaleX).with(scaleY)
            animatorSet.duration = animTime.toLong()
            animatorSet.start()
        }
    }

    override fun onSoftKeyboardClosed() {
        // 执行位移动画
        bodyLayout?.let {
            val objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(
                    it,
                    "translationY", it.translationY, 0f
            )
            objectAnimator.duration = animTime.toLong()
            objectAnimator.interpolator = AccelerateDecelerateInterpolator()
            objectAnimator.start()
        }

        // 执行放大动画
        logoView?.let {
            it.pivotX = it.width / 2f
            it.pivotY = it.height.toFloat()

            if (it.translationY == 0f) {
                return
            }

            val animatorSet = AnimatorSet()
            val scaleX: ObjectAnimator = ObjectAnimator.ofFloat(it, "scaleX", logoScale, 1f)
            val scaleY: ObjectAnimator = ObjectAnimator.ofFloat(it, "scaleY", logoScale, 1f)
            val translationY: ObjectAnimator = ObjectAnimator.ofFloat(
                    it,
                    "translationY", it.translationY, 0f
            )
            animatorSet.play(translationY).with(scaleX).with(scaleY)
            animatorSet.duration = animTime.toLong()
            animatorSet.start()
        }
    }

    /**
     * [TextView.OnEditorActionListener]
     */
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // 模拟点击提交按钮
            commitView?.let {
                if (it.isEnabled) {
                    // 模拟点击登录按钮
                    onClick(it)
                    return true
                }
            }
        }
        return false
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig()
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.white)
    }
}
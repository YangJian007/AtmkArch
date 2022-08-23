package com.atmk.iot.bz_login.ui.activity

import android.content.Intent
import android.os.Bundle

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.launcher.ARouter
import com.atmk.iot.bz_login.R
import com.atmk.base.app.AppActivity
import com.atmk.base.app.AppFragment
import com.atmk.base.manager.ActivityManager
import com.atmk.base.other.DoubleClickHelper
//import com.atmk.base.ui.fragment.HomeFragment
//import com.atmk.base.ui.fragment.StatisticsFragment
import com.atmk.iot.bz_login.ui.adapter.NavigationAdapter
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.FragmentPagerAdapter



class HomeActivity : AppActivity(), NavigationAdapter.OnNavigationListener {
    companion object {

        private const val INTENT_KEY_IN_FRAGMENT_INDEX: String = "fragmentIndex"
        private const val INTENT_KEY_IN_FRAGMENT_CLASS: String = "fragmentClass"

//        @JvmOverloads
//        fun start(context: Context, fragmentClass: Class<out AppFragment<*>?>? = HomeFragment::class.java) {
//            val intent = Intent(context, HomeActivity::class.java)
//            intent.putExtra(INTENT_KEY_IN_FRAGMENT_CLASS, fragmentClass)
//            if (context !is Activity) {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(intent)
//        }
    }


    private val viewPager: ViewPager? by lazy { findViewById(R.id.vp_home_pager) }
    private val navigationView: RecyclerView? by lazy { findViewById(R.id.rv_home_navigation) }
    private var navigationAdapter: NavigationAdapter? = null
    private var pagerAdapter: FragmentPagerAdapter<AppFragment<*>>? = null
    override fun getLayoutId(): Int {
        return R.layout.login_home_activity
    }

    override fun initView() {
        navigationAdapter = NavigationAdapter(this).apply {
            addItem(NavigationAdapter.MenuItem(getString(R.string.login_home_nav_equipment),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.login_home_home_selector)))
            addItem(NavigationAdapter.MenuItem(getString(R.string.login_home_nav_statistics),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.login_home_found_selector)))
            setOnNavigationListener(this@HomeActivity)
            navigationView?.adapter = this
        }
    }

    override fun initData() {

      val  fragment1= ARouter.getInstance().build("/device/homeFm").navigation() as AppFragment<*>
      val  fragment2= ARouter.getInstance().build("/statistics/statisticsFm").navigation() as AppFragment<*>

        pagerAdapter = FragmentPagerAdapter<AppFragment<*>>(this).apply {
            addFragment(fragment1)
            addFragment(fragment2)
//            addFragment(HomeFragment.newInstance())
//            addFragment(StatisticsFragment.newInstance())
            viewPager?.adapter = this
        }
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pagerAdapter?.let {
            switchFragment(it.getFragmentIndex(getSerializable(INTENT_KEY_IN_FRAGMENT_CLASS)))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewPager?.let {
            // 保存当前 Fragment 索引位置
            outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, it.currentItem)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复当前 Fragment 索引位置
        switchFragment(savedInstanceState.getInt(INTENT_KEY_IN_FRAGMENT_INDEX))
    }

    private fun switchFragment(fragmentIndex: Int) {
        if (fragmentIndex == -1) {
            return
        }
        when (fragmentIndex) {
            0, 1 -> {
                viewPager?.currentItem = fragmentIndex
                navigationAdapter?.setSelectedPosition(fragmentIndex)
            }
        }
    }

    /**
     * [NavigationAdapter.OnNavigationListener]
     */
    override fun onNavigationItemSelected(position: Int): Boolean {
        return when (position) {
            0, 1 -> {
                viewPager?.currentItem = position
                true
            }
            else -> false
        }
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig() // 指定导航栏背景颜色
                .navigationBarColor(R.color.white)
    }

    override fun onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint)
            return
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false)
        postDelayed({
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities()
        }, 300)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.adapter = null
        navigationView?.adapter = null
        navigationAdapter?.setOnNavigationListener(null)
    }

}
package com.atmk.iot.bz_statistics.room.v

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.atmk.base.app.AppVMActivity
import com.atmk.iot.bz_statistic.databinding.ActivityRoomBinding
import com.atmk.iot.bz_statistics.room.vm.RoomViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn

/**
 * @author        YJ
 * @date          2022-09-20
 * @description
 */
class RoomActivity : AppVMActivity<ActivityRoomBinding>() {

    override fun getViewBinding(): ActivityRoomBinding {
        return ActivityRoomBinding.inflate(layoutInflater)
    }

    private val mViewModel: RoomViewModel by viewModels()
    private lateinit var adapter: PersonAdapter

    override fun initView() {

        mViewBinding.apply {

            btnInsert.setOnClickListener {
                mViewModel.insert(getName(), getAge())
            }
            btnDelete.setOnClickListener {
                mViewModel.delete(getName(), getAge())
            }
            btnUpdate.setOnClickListener {
                mViewModel.update(getName(), getAge())
            }
            adapter = PersonAdapter(this@RoomActivity)
            rvResult.adapter = adapter
        }
        lifecycleScope.launchWhenResumed {
            mViewModel.query()
//                .stateIn(this)
                .collect {
                    adapter.clearData()
                    adapter.setData(it.toMutableList())
                }
        }

    }

    override fun initData() {

    }

    private fun getName() = mViewBinding.etLoginAccount.text.toString().trim()

    private fun getAge() = mViewBinding.etLoginPassword.text.toString().trim().toIntOrNull() ?: 0
}
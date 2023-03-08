package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.NavItem
import com.example.myapplication.entity.UserRewardBean
import com.example.myapplication.http.UserRewardUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MineFragment : Fragment() {

    private var binding: ActivityBaseListBinding? = null

    private val list = mutableListOf<NavItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityBaseListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.baseToolbar?.visibility = View.GONE

        val adapter = KotlinDataAdapter.Builder<NavItem>()
            .setLayoutId(R.layout.item_nav)
            .setData(list)
            .addBindView { itemView, itemData ->
                val icon = itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = itemView.findViewById<TextView>(R.id.item_nav_title)

                icon.setImageResource(R.drawable.ic_nav_login)
                title.text = itemData.title
            }.create()
        binding?.baseRecyclerView?.adapter = adapter
        binding?.baseRecyclerView?.setOnItemClickListener { holder, position ->
            val id = list[position].id
            when (id) {
                "6" -> {
                    Prefs.userInfo = ""
                    Prefs.isSaveStatus = false

                    UserViewModel.user = null
                    backUp()
                }
                "7" -> {
                    CustomDialog.Builder2(requireContext())
                        .setIcon(R.drawable.ic_alert_ask)
                        .setTitle("将删除此账户所有数据，包括已获得的积分，是否继续？")
                        .setCancelListener {  }
                        .setConfirmListener {
                            UserViewModel.logout(lifecycleScope, UserViewModel.user?.account!!, UserViewModel.user?.password!!)
                        }
                        .show()
                }

            }
        }


        val user = UserViewModel.user
        if (user == null) {
            binding?.baseRecyclerView?.emptyText = "未登录"
            return
        } else {
            val role = when (user.role) {
                0 -> "普通用户"
                1 -> "工作人员"
                2 -> "管理员"
                else -> null
            }

            list.add(NavItem(true, "0", "名称：" + user.nickname, 0))
            list.add(NavItem(true, "1", "账号：" + user.account, 0))
            list.add(NavItem(true, "2", "角色：" + role, 0))
            list.add(NavItem(true, "3", "是否在申请管理员：" + if (user.isApply == 0) "否" else "是", 0))
            list.add(NavItem(true, "4", "申请时间：" + user.applyTime.toString(), 0))

            lifecycleScope.launch(Dispatchers.IO) {
                val reward = try {
                    UserRewardUtils.getReward(
                        Gson().toJson(
                            UserRewardBean().apply {
                                this.uid = UserViewModel.user?.id
                                this.pin = UserViewModel.region?.pin
                            }
                        )
                    )
                } catch (e:Exception){
                    e.printStackTrace()
                    0
                }
                withContext(Dispatchers.Main){
                    list.add(NavItem(true, "5", "积分数量：" + reward, 0))
                    adapter.notifyDataSetChanged()
                }

            }

            list.add(NavItem(true, "6", "退出登录", 0))
            list.add(NavItem(true, "7", "注销账号", 0))

            adapter.notifyDataSetChanged()
        }


        LiveDataBus.with("livebus_login").observe(this) {
            val pair = it as Pair<Boolean, String>
            if (pair.first) {
                Toast.makeText(requireContext(), "注销成功", Toast.LENGTH_SHORT).show()

                backUp()
            } else {
                Toast.makeText(requireContext(), "注销失败", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun backUp() {
        requireActivity().onNavigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
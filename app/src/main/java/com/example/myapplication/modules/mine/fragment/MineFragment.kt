package com.example.myapplication.modules.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.modules.user.activity.LoginActivity
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.NavItem
import com.example.myapplication.entity.UserBean
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel

class MineFragment : Fragment() {

    private var binding: ActivityBaseListBinding? = null

    private val list = mutableListOf<NavItem>()
    private lateinit var adapter: KotlinDataAdapter<NavItem>

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

        adapter = KotlinDataAdapter.Builder<NavItem>()
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
                "-1" -> {
                    LoginActivity.start(requireContext(), "login")
                }
                "6" -> {
                    CustomDialog.Builder2(requireContext())
                        .setIcon(R.drawable.ic_alert_ask)
                        .setTitle("?????????????????????")
                        .setCancelListener {  }
                        .setConfirmListener {
                            Prefs.userInfo = ""
                            Prefs.isSaveStatus = false

                            UserViewModel.user = null
                            backUp()
                        }
                        .show()


                }
                "7" -> {
                    CustomDialog.Builder2(requireContext())
                        .setIcon(R.drawable.ic_alert_ask)
                        .setTitle("???????????????????????????????????????????????????????????????????????????")
                        .setCancelListener {  }
                        .setConfirmListener {
                            UserViewModel.logout(lifecycleScope, UserViewModel.user?.account!!, UserViewModel.user?.password!!)
                        }
                        .show()
                }

            }
        }

        LiveDataBus.with("livebus_login").observe(this) {
            val pair = it as Pair<Boolean, String>
            if (pair.first) {
                Toast.makeText(requireContext(), "????????????", Toast.LENGTH_SHORT).show()
                backUp()
            } else {
                Toast.makeText(requireContext(), "????????????", Toast.LENGTH_SHORT).show()
            }
        }
        LiveDataBus.with("livebus_user_change").observe(this) {
            val user = it as UserBean?
            updateUser(user)
        }

        val user = UserViewModel.user
        updateUser(user)
    }

    private fun updateUser(user: UserBean?) {
        list.clear()
        if (user == null) {
            list.add(NavItem(true, "-1", "?????????", 0))
            adapter.notifyDataSetChanged()
        } else {
            val role = when (user.role) {
                0 -> "????????????"
                1 -> "????????????"
                2 -> "?????????"
                else -> null
            }

            list.add(NavItem(true, "0", "?????????" + user.nickname, 0))
            list.add(NavItem(true, "1", "?????????" + user.account, 0))
            list.add(NavItem(true, "2", "?????????" + role, 0))
            list.add(NavItem(true, "3", "???????????????????????????" + if (user.isApply == 0) "???" else "???", 0))
            list.add(NavItem(true, "4", "???????????????" + user.applyTime.toString(), 0))
            list.add(NavItem(true, "5", "???????????????${user.reward}", 0))
            list.add(NavItem(true, "6", "????????????", 0))
            list.add(NavItem(true, "7", "????????????", 0))
            adapter.notifyDataSetChanged()
        }
    }

    private fun backUp() {
        (requireActivity() as? AppCompatActivity)?.onSupportNavigateUp()
//        requireActivity().onNavigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
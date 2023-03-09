package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRegisterAdminBinding
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel

class RegisterAdminFragment : Fragment() {

    private var binding: FragmentRegisterAdminBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterAdminBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = UserViewModel.user
        if (user == null) {
            Toast.makeText(requireContext(), "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }

        LiveDataBus.with("livebus_login").observe(this) {
            val pair = it as Pair<Boolean, String>
            if (pair.first) {
                Toast.makeText(requireContext(), pair.second, Toast.LENGTH_LONG).show()
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), pair.second, Toast.LENGTH_LONG).show()
            }
        }

        LiveDataBus.with("livebus_apply_admin").observe(this) {
            val flag = it as Boolean
            if (flag) {
                CustomDialog.Builder2(requireContext())
                    .setIcon(R.drawable.ic_alert_wait)
                    .setTitle("已提交申请，请等待后台确认...")
                    .setConfirmText(android.R.string.ok)
                    .setConfirmListener {  }
                    .show()
            } else {
                Toast.makeText(requireContext(), "失败", Toast.LENGTH_LONG).show()
            }
        }

        binding?.editTextTextPersonName?.setText(user.phone)
        binding?.editTextTextPhone?.setText(user.phone)

        binding?.buttonLogin?.setOnClickListener {
            UserViewModel.loginWithAdmin(lifecycleScope, user.phone!!, true)
        }

        binding?.buttonApply?.setOnClickListener {
            UserViewModel.applyAdmin(lifecycleScope, user.account!!, user.password!!, "1")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
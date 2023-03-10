package com.example.myapplication.modules.user.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.MyPagerAdapter
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.databinding.ViewLoginPager1Binding
import com.example.myapplication.databinding.ViewLoginPager2Binding
import com.example.myapplication.utils.PhoneUtils
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.viewmodel.UserViewModel
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class UserLoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val permissions = mutableListOf<String>()
//        permissions.add(Manifest.permission.READ_PHONE_STATE)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            permissions.add(Manifest.permission.READ_PHONE_NUMBERS)
//        }
//        val hasPermission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        ActivityCompat.requestPermissions(
//            this,
//            permissions.toTypedArray(),
//            100
//        )


        val list = mutableListOf<View>()

        var realNumber = PhoneUtils.trimTelNum(PhoneUtils.getNativePhoneNumber(requireContext()))?.replace("+", "")
        var fakeNumber = realNumber?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(),"$1****$2") ?: "未知号码"

        val v1 = ViewLoginPager1Binding.inflate(LayoutInflater.from(requireContext()))
        val listener1 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val str = v1.tvPhoneNumber.text.trim().toString()
                v1.btnAutoLogin.setStatus(Utils.isMobileNO(str))
            }
        }
        v1.tvPhoneNumber.addTextChangedListener(listener1)
        v1.tvPhoneNumber.text = fakeNumber
        v1.tvTip.text = ""
        v1.btnAutoLogin.setOnClickListener {
            if (Utils.isMobileNO(realNumber)) {
                UserViewModel.loginWithPhone(lifecycleScope, realNumber!!, saveState = true)
            } else {
                Toast.makeText(requireContext(), "未知号码，自动登录失败", Toast.LENGTH_SHORT).show()
            }
        }
        val v2 = ViewLoginPager2Binding.inflate(LayoutInflater.from(requireContext()))

        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val len1 = v2.editTextTextPersonName.text.trim().toString().length
                val len2 = v2.editTextTextPassword.text.trim().toString().length

                v2.buttonLogin.setStatus(len1 in 6..18 && len2 in 6..18)
            }
        }
        v2.editTextTextPersonName.addTextChangedListener(listener)
        v2.editTextTextPassword.addTextChangedListener(listener)
        v2.editTextTextPersonName.setText("")
        v2.editTextTextPassword.setText("")
        v2.buttonLogin.setOnClickListener {
            val username = v2.editTextTextPersonName.text.toString()
            val password = v2.editTextTextPassword.text.toString()

            if (username.length !in 6..18) {
                Toast.makeText(requireContext(), "用户名应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length !in 6..18) {
                Toast.makeText(requireContext(), "密码应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserViewModel.login(lifecycleScope, username, password, v2.saveStatus.isChecked)
        }
        v2.loginRegister.setOnClickListener {
            val rf = RegisterUserFragment()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_content, rf, "login")
                .addToBackStack(null)
                .commit()
        }
        v2.loginForgetable.setOnClickListener {
            val fp = ForgetPasswordFragment()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_content, fp, "login")
                .addToBackStack(null)
                .commit()
        }


        list.add(v1.root)
        list.add(v2.root)

        binding?.viewPager?.adapter = MyPagerAdapter(list)
        binding?.tabLayout?.setupWithViewPager(binding?.viewPager)

        binding?.tabLayout?.getTabAt(0)?.text = "一键登录"
        binding?.tabLayout?.getTabAt(1)?.text = "密码登录"

        XXPermissions.with(this).permission(Permission.READ_PHONE_NUMBERS, Permission.READ_PHONE_STATE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        v1.tvTip.text = "部分权限未正常授予，可能获取手机号失败"
                        return;
                    }
                    v1.tvTip.text = ""

                    ///
                    realNumber = PhoneUtils.trimTelNum(PhoneUtils.getNativePhoneNumber(requireContext()))?.replace("+", "")
                    fakeNumber = realNumber?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(),"$1****$2") ?: "未知号码"
                    v1.tvPhoneNumber.text = fakeNumber
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    super.onDenied(permissions, doNotAskAgain)
                    if (doNotAskAgain) {
                        v1.tvTip.text = "被永久拒绝授权，请手动授予权限"
                    } else {
                        v1.tvTip.text = "获取权限失败"
                    }
                }
            })



        LiveDataBus.with("livebus_login").observe(this) {
            val pair = it as Pair<Boolean, String>
            if (pair.first) {
                Toast.makeText(requireContext(), pair.second, Toast.LENGTH_LONG).show()
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), pair.second, Toast.LENGTH_LONG).show()
            }
        }
    }
}
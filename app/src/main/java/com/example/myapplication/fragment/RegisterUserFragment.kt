package com.example.myapplication.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.viewmodel.UserViewModel

class RegisterUserFragment : Fragment() {

    private var binding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val username = binding?.editTextTextPersonName?.text?.trim().toString()
                val password = binding?.editTextTextPassword?.text?.trim().toString()
                val phone = binding?.editTextTextPhone?.text?.trim().toString()

                val flag =
                    username.length in 6..18 && password.length in 6..18 && Utils.isMobileNO(phone)
                binding?.buttonRegister?.setStatus(flag)
            }
        }
        binding?.editTextTextPersonName?.addTextChangedListener(listener)
        binding?.editTextTextPassword?.addTextChangedListener(listener)
        binding?.editTextTextPhone?.addTextChangedListener(listener)

        binding?.editTextTextPersonName?.setText("")
        binding?.editTextTextPassword?.setText("")
        binding?.editTextTextPhone?.setText("")

        binding?.buttonRegister?.setOnClickListener {
            val username = binding?.editTextTextPersonName?.text?.trim().toString()
            val password = binding?.editTextTextPassword?.text?.trim().toString()
            val phone = binding?.editTextTextPhone?.text?.trim().toString()


            if (username.length !in 6..18) {
                Toast.makeText(requireContext(), "用户名应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length !in 6..18) {
                Toast.makeText(requireContext(), "密码应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Utils.isMobileNO(phone)) {
                Toast.makeText(requireContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            UserViewModel.register(lifecycleScope, username, password, phone)
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
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
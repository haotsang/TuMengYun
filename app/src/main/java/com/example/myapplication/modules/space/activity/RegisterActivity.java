package com.example.myapplication.modules.space.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.utils.MD5Utils;
import com.example.myapplication.modules.space.utils.UserRW;

public class RegisterActivity extends AppCompatActivity {
private TextView textViewGoBack,textViewTitle;
private EditText editTextAddUsername,editTextAddPassword,editTextConfirmPassword;
private Button buttonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textViewGoBack = findViewById(R.id.textViewGoBack);
        textViewGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextAddUsername = findViewById(R.id.editTextAddUsername);
        editTextAddPassword = findViewById(R.id.editTextAddPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewTitle.setText("注册");
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextAddUsername.getText().toString();
                String password = editTextAddPassword.getText().toString();
                String confirmpassword = editTextConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    editTextAddUsername.requestFocus();//获得焦点
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    editTextAddPassword.requestFocus();//获得焦点
                    return;
                }
                if (TextUtils.isEmpty(confirmpassword)) {
                    Toast.makeText(RegisterActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
                    editTextAddPassword.requestFocus();//获得焦点
                    return;
                }
                if (!(password.equals(confirmpassword))) {
                    Toast.makeText(RegisterActivity.this, "密码与确认密码不一致！", Toast.LENGTH_SHORT).show();
                    editTextAddPassword.requestFocus();//获得焦点
                    editTextAddPassword.selectAll();
                    return;
                }
                String pwd= UserRW.getString(RegisterActivity.this,username);
                if (TextUtils.isEmpty(pwd)){
                    password= MD5Utils.stringToMD5(password);
                    if (UserRW.writeString(RegisterActivity.this,username,password)){
                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, "注册失败，请检查系统！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "当前用户已存在！", Toast.LENGTH_SHORT).show();
                    editTextAddUsername.requestFocus();
                    editTextAddUsername.selectAll();
                }
            }

        });
    }
}
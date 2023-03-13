package com.example.myapplication.modules.space.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.utils.MD5Utils;
import com.example.myapplication.modules.space.utils.UserRW;

public class LoginActivity extends AppCompatActivity {
private EditText editTextUsername,editTextPassword;
private Button buttonLogin;
private TextView textViewRegister,textViewFindPassword;
private ImageView textViewGoBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_space);
        editTextUsername=findViewById(R.id.editTextUsername);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonLogin=findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=editTextUsername.getText().toString();
                String password=editTextPassword.getText().toString();
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    editTextUsername.requestFocus();//获得焦点
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                    editTextPassword.requestFocus();//获得焦点
                    return;
                }
                password= MD5Utils.stringToMD5(password);
                if (password.equals(UserRW.getString(LoginActivity.this,username))){
                    Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                    UserRW.writeString(LoginActivity.this,"LoginUsername",username);
                    UserRW.writeBoolean(LoginActivity.this,"LoginState",true);
                    setResult(100);//结果码
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                    editTextUsername.requestFocus();
                    editTextUsername.selectAll();//全选
                }
            }
        });
        textViewRegister=findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        textViewFindPassword=findViewById(R.id.textViewFindPassword);
        textViewFindPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,FindPasswordActivity.class);
                intent.putExtra("from","FindPassword");
                startActivity(intent);
            }
        });
        textViewGoBack=findViewById(R.id.textViewGoBack);
        textViewGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
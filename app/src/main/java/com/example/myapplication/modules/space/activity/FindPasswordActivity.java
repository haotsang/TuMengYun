package com.example.myapplication.modules.space.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.utils.MD5Utils;
import com.example.myapplication.modules.space.utils.RandomSecurityCode;
import com.example.myapplication.modules.space.utils.SendMailUtil;
import com.example.myapplication.modules.space.utils.UserRW;
import com.example.myapplication.modules.space.view.TitleBarView;

import androidx.appcompat.app.AppCompatActivity;

public class FindPasswordActivity extends AppCompatActivity {
    private EditText editTextValidateEmail,editViewValidateCode,editTextUserName;
    private TextView textViewValidateCode,textViewUserName,textViewRosePassword;
    private Button buttonValidate;
    private String securityCode=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        final String from=getIntent().getStringExtra("from");
        textViewValidateCode=findViewById(R.id.textViewValidateCode);
        editViewValidateCode=findViewById(R.id.editViewValidateCode);
        textViewUserName=findViewById(R.id.textViewUserName);
        editTextUserName=findViewById(R.id.editTextUserName);
        textViewRosePassword=findViewById(R.id.textViewRosePassword);
        if (from.equals("SecuritySetting")){
            new TitleBarView(this,"设置密保邮箱！");
        }else {
            new TitleBarView(this,"找回密码");
            textViewUserName.setVisibility(View.VISIBLE);
            editTextUserName.setVisibility(View.VISIBLE);
        }
        editTextValidateEmail=findViewById(R.id.editTextValidateEmail);
        buttonValidate=findViewById(R.id.buttonValidate);
        //调用按钮
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("SecuritySetting")){
                    buttonForSecuritySetting();
                }else {
                    buttonForFindPassword();
                }
            }
        });

    }

    public void sendTextMail(String title,String content) {
        RandomSecurityCode randomSecurityCode=new RandomSecurityCode();//生产随机类
        securityCode=new String(randomSecurityCode.getSecurityCode());
        //调用随机类
        content+=securityCode+",请尽快使用！！！";
        SendMailUtil.send(editTextValidateEmail .getText().toString(),title,content);
    }
    private void buttonForSecuritySetting(){
        //获取按钮文字
        String text = buttonValidate.getText().toString();
        if (text.equals("发送验证码")) {
            //调用方法
            sendTextMail("设置密保邮箱","您正在设置密保邮箱，验证码为：");//发送验证码
            //将隐藏的控件设置可见
            textViewValidateCode.setVisibility(View.VISIBLE);
            editViewValidateCode.setVisibility(View.VISIBLE);
            buttonValidate.setText("验证邮箱");
        } else {
            String inputsSecurityCode = editViewValidateCode.getText().toString();//获取文本
            if (inputsSecurityCode.equals(securityCode)) {
                //保存密保邮箱
                String textEmail=editTextValidateEmail.getText().toString();
                String currentUser= UserRW.getString(FindPasswordActivity.this,"LoginUsername");
                if (UserRW.writeString(FindPasswordActivity.this,currentUser+"_validateMail",textEmail)) {
                    Toast.makeText(FindPasswordActivity.this, "密保邮箱设置成功！：", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FindPasswordActivity.this, "密保邮箱设置失败！：", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                //提示错误信息
                Toast.makeText(FindPasswordActivity.this, "验证码错误！！", Toast.LENGTH_SHORT).show();
                editViewValidateCode.requestFocus();
                editViewValidateCode.selectAll();
            }
        }
    }
    private  void buttonForFindPassword(){
        String text = buttonValidate.getText().toString();
        String username=editTextUserName.getText().toString();//判定是否存在
        if (text.equals("发送验证码")){
        //判定用户名是否为空
            if (username.isEmpty()){
                Toast.makeText(FindPasswordActivity.this, "请输入用户名！！", Toast.LENGTH_SHORT).show();
                editTextUserName.requestFocus();
                return;
            }
        //判定用户名是否存在
            String password=UserRW.getString(this,username);
            if (password.equals("")){
                Toast.makeText(FindPasswordActivity.this, "用户名不存在！！", Toast.LENGTH_SHORT).show();
                editTextUserName.requestFocus();
                editTextUserName.selectAll();
                return;
            }
        //判定邮箱是否输入
            String email=editTextValidateEmail.getText().toString();//判定是否存在
            if (email.isEmpty()){
                Toast.makeText(FindPasswordActivity.this, "请输入密保邮箱！！", Toast.LENGTH_SHORT).show();
                editTextValidateEmail.requestFocus();
                return;
            }
        //判定邮箱与用户名是否匹配
            String emailAtFile=UserRW.getString(this,username+"_validateMail");
            if (!(emailAtFile.equals(email))){
                Toast.makeText(FindPasswordActivity.this, "密保邮箱输入错误！！", Toast.LENGTH_SHORT).show();
                editTextValidateEmail.requestFocus();
                editTextValidateEmail.selectAll();
                return;
            }
          //发送验证码
            sendTextMail("找回密码","您正在设置找回密码，验证码为：");//发送验证码
            //显示验证码控件
            textViewValidateCode.setVisibility(View.VISIBLE);
            editViewValidateCode.setVisibility(View.VISIBLE);
            //修改按钮显示文字
            buttonValidate.setText("找回密码");
        }else {
            //判定验证码是否为空
            String validateCode=editViewValidateCode.getText().toString();
            if (validateCode.isEmpty()){
                Toast.makeText(FindPasswordActivity.this, "请输入验证码！！", Toast.LENGTH_SHORT).show();
                editViewValidateCode.requestFocus();
                return;
            }
            //判定验证码是否正确
            if (!(validateCode.equals(securityCode))){
                Toast.makeText(FindPasswordActivity.this, "验证码输入错误！！", Toast.LENGTH_SHORT).show();
                editViewValidateCode.requestFocus();
                editViewValidateCode.selectAll();
                return;
            }
            //初始化密码
            if (UserRW.writeString(this,username, MD5Utils.stringToMD5("123"))){
                textViewRosePassword.setVisibility(View.VISIBLE);
                textViewRosePassword.setText("找回密码成功，初始化密码为”123”,请登录系统后尽快修改！！");
            }else {
                Toast.makeText(FindPasswordActivity.this, "初始化密码失败！ ！！", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

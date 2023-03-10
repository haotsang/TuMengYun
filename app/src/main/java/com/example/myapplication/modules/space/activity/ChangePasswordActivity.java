package com.example.myapplication.modules.space.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.utils.MD5Utils;
import com.example.myapplication.modules.space.utils.UserRW;
import com.example.myapplication.modules.space.view.TitleBarView;

import androidx.appcompat.app.AppCompatActivity;


public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editTextOldPassword, editTextNewPassword, editTextConfirmNewPassword;
    private Button buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        TitleBarView titleBarView=new TitleBarView(this,"修改密码");
        editTextOldPassword=findViewById(R.id.editTextOldPassword);
        editTextNewPassword=findViewById(R.id.editTextNewPassword);
        editTextConfirmNewPassword=findViewById(R.id.editTextConfirmNewPassword);
        buttonSave=findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword=editTextOldPassword.getText().toString();
                String newPassword=editTextNewPassword.getText().toString();
                String confirmNewPassword=editTextConfirmNewPassword.getText().toString();
                //判定原密码不能为空
                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
                    editTextOldPassword.requestFocus();//获得焦点
                    return;
                }
                //判定新密码不能为空
                if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    editTextNewPassword.requestFocus();//获得焦点
                    return;
                }
                //判定确认密码不能为空
                if (TextUtils.isEmpty(confirmNewPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
                    editTextConfirmNewPassword.requestFocus();//获得焦点
                    return;
                }
                //判定原密码是否正确（1.2.3）
                  //1.获取登录的用户名
                //因为在内部类View.OnClickListener()，所以不能直接写this,要写ChangePasswordActivity.this
                String username= UserRW.getString(ChangePasswordActivity.this,"LoginUsername");
                  //2.根据用户获取密码
                String spPassword=UserRW.getString(ChangePasswordActivity.this, username);
                  //3.将获取的密码与原密码进行比较
                if (!(MD5Utils.stringToMD5(oldPassword).equals(spPassword))){
                    Toast.makeText(ChangePasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                    editTextOldPassword.requestFocus();//获得焦点
                    editTextOldPassword.selectAll();
                    return;
                }
                //判定新密码与确认密码是否一致
                if (!(newPassword.equals(confirmNewPassword))) {
                    Toast.makeText(ChangePasswordActivity.this, "新密码与确认密码不相等", Toast.LENGTH_SHORT).show();
                    editTextNewPassword.requestFocus();//获得焦点
                    editTextNewPassword.selectAll();//全选
                    return;
                }
                //提交保存
                if(UserRW.writeString(ChangePasswordActivity.this,username,MD5Utils.stringToMD5(newPassword))){
                    Toast.makeText(ChangePasswordActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}

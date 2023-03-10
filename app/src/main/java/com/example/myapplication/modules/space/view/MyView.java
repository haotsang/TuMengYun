package com.example.myapplication.modules.space.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.activity.LoginActivity;
import com.example.myapplication.modules.space.activity.SettingActivity;
import com.example.myapplication.modules.space.activity.UserInfoActivity;
import com.example.myapplication.modules.space.model.Exercises;

public class MyView implements View.OnClickListener {
    private LayoutInflater layoutInflater;//布局填充器
    private Activity activity;
    private RelativeLayout relativeLayoutSetting;
    //布局和Java文件连接一起
    //创建视图
    private View infoView;
    private LinearLayout linearLayoutClickLogin;
    private TextView textViewClickLogin;



    public  MyView(Activity activity){
        this.activity=activity;
        layoutInflater=LayoutInflater.from(activity);//获取布局填充器
    }

    public void creadView(){
        infoView=layoutInflater.inflate(R.layout.activity_my,null);
        linearLayoutClickLogin=infoView.findViewById(R.id.linearLayoutClickLogin);
        linearLayoutClickLogin.setOnClickListener(this);
        textViewClickLogin=infoView.findViewById(R.id.textViewClickLogin);
        relativeLayoutSetting=infoView.findViewById(R.id.relativeLayoutSetting);
        relativeLayoutSetting.setOnClickListener(this);
    }
    public View getView() {
        if (infoView==null){
            creadView();
        }
        return infoView;
    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.linearLayoutClickLogin:
                if (textViewClickLogin.getText().toString().equals("点击登录")) {
                    intent = new Intent(activity, LoginActivity.class);
                } else {
                    intent = new Intent(activity, UserInfoActivity.class);
//                    activity.startActivity(intent);
                }
                break;
            case R.id.relativeLayoutSetting:
                if (textViewClickLogin.getText().toString().equals("点击登录"))
                    return;
                intent = new Intent(activity, SettingActivity.class);
                break;
        }
        if (intent!=null){
            activity.startActivityForResult(intent,100);//请求码
        }
    }
    public void setTextViewClickLoginText(String text) {
        textViewClickLogin.setText(text);
    }
}

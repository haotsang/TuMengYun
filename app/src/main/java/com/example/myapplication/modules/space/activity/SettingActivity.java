package com.example.myapplication.modules.space.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.modules.space.utils.UserRW;
import com.example.myapplication.modules.space.view.TitleBarView;
import com.example.myapplication.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout relativeLayoutExit,relativeLayoutRevisePsw,relativeLayoutSeetingPsw;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeting);
        TitleBarView titleBarView=new TitleBarView(this,"设置");
        relativeLayoutExit=findViewById(R.id.relativeLayoutExit);
        relativeLayoutExit.setOnClickListener(this);
        relativeLayoutRevisePsw=findViewById(R.id.relativeLayoutRevisePsw);
        relativeLayoutRevisePsw.setOnClickListener(this);
        relativeLayoutSeetingPsw=findViewById(R.id.relativeLayoutSeetingPsw);
        relativeLayoutSeetingPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            //退出
            case R.id.relativeLayoutExit:
                UserRW.writeString(this,"LoginUsername","");
                UserRW.writeBoolean(this,"LoginState",false);
                setResult(100);//结果码
                finish();
                break;
                //修改密码
            case R.id.relativeLayoutRevisePsw:
                    intent=new Intent(this,ChangePasswordActivity.class);
                    startActivity(intent);
                    break;
                    //设置密保
            case R.id.relativeLayoutSeetingPsw:
                intent=new Intent(this,FindPasswordActivity.class);
                intent.putExtra("from","SecuritySetting");
                startActivity(intent);
                break;

        }
        if (intent!=null){
            startActivityForResult(intent,100);
        }
    }
}

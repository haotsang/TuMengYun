package com.example.myapplication.modules.space.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.model.Course;
import com.example.myapplication.modules.space.utils.UserRW;
import com.example.myapplication.modules.space.view.CourseView;
import com.example.myapplication.modules.space.view.ExercisesView;
import com.example.myapplication.modules.space.view.MyView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SpaceActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewGoBack, textViewTitle, textViewCourse, textViewExercises, textViewMy;
    private ImageView imageViewCourse, imageViewExercises, imageViewMy;
    private LinearLayout linearLayoutCourse, linearLayoutExercises, linearLayoutMy;
    private RelativeLayout relativeLayoutTitleBar;
    private FrameLayout frameLayoutCenter;
    private MyView myView;
    private ExercisesView exercisesView;
    private CourseView courseView;
    private ArrayList<Course> courseList = null;
    private ListView listViewCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        connectComponent();
        //设置进入主界面的索引是2的“我的”界面
        setBottomBarState(0);//1

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue_bright));
    }

    //关联界面控件
    private void connectComponent() {
        textViewGoBack = findViewById(R.id.textViewGoBack);
        textViewTitle = findViewById(R.id.textViewTitle);
//        textViewCourse = findViewById(R.id.textViewCourse);
//        textViewExercises = findViewById(R.id.textViewExercises);
//        textViewMy = findViewById(R.id.textViewMy);
//        imageViewCourse = findViewById(R.id.imageViewCourse);
//        imageViewExercises = findViewById(R.id.imageViewExercises);
//        imageViewMy = findViewById(R.id.imageViewMy);
//        linearLayoutCourse = findViewById(R.id.linerLayoutCourse);
//        linearLayoutExercises = findViewById(R.id.linerLayoutExercises);
//        linearLayoutMy = findViewById(R.id.linerLayoutMy);
        relativeLayoutTitleBar = findViewById(R.id.relativeLayoutTitleBar);
        frameLayoutCenter = findViewById(R.id.frameLayoutCenter);
//        linearLayoutCourse.setOnClickListener(this);
//        linearLayoutExercises.setOnClickListener(this);
//        linearLayoutMy.setOnClickListener(this);
        textViewGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //清楚底部工具栏状态
//    private void clearBottomBarState() {
//        imageViewCourse.setImageResource(R.drawable.main_course_icon);
//        textViewCourse.setTextColor(Color.parseColor("#666666"));
//        imageViewExercises.setImageResource(R.drawable.main_exercises_icon);
//        textViewExercises.setTextColor(Color.parseColor("#666666"));
//        imageViewMy.setImageResource(R.drawable.main_my_icon);
//        textViewMy.setTextColor(Color.parseColor("#666666"));
//    }

    //设置底部工具栏状态
    private void setBottomBarState(int index) {
//        clearBottomBarState();
        frameLayoutCenter.removeAllViews();
        relativeLayoutTitleBar.setBackgroundColor(Color.parseColor("#0097F7"));
        switch (index) {
            case 0:
//                imageViewCourse.setImageResource(R.drawable.main_course_icon_selected);
//                textViewCourse.setTextColor(Color.parseColor("#0097F7"));
//                relativeLayoutTitleBar.setVisibility(View.VISIBLE);
//                textViewTitle.setText("课程");
                if (courseView == null) {
                    courseView = new CourseView(this);
                }
                frameLayoutCenter.addView(courseView.getView());//获取视图
                break;

            case 1:
                imageViewExercises.setImageResource(R.drawable.main_exercises_icon_selected);
                textViewExercises.setTextColor(Color.parseColor("#0097F7"));
                relativeLayoutTitleBar.setVisibility(View.VISIBLE);
                textViewTitle.setText("习题");
                if (exercisesView == null) {
                    exercisesView = new ExercisesView(this);
                }
                frameLayoutCenter.addView(exercisesView.getView());//获取视图
                break;
            case 2:
//                imageViewMy.setImageResource(R.drawable.main_my_icon_selected);
//                textViewMy.setTextColor(Color.parseColor("#0097F7"));
                relativeLayoutTitleBar.setVisibility(View.GONE);
                if (myView == null) {
                    myView = new MyView(this);
                }
                frameLayoutCenter.addView(myView.getView());//获取视图
                setClickLoginText();//使用户保持登录状态
        }
    }

    @Override
    public void onClick(View view) {
//        clearBottomBarState();
        int index = -1;
        switch (view.getId()) {
//            case R.id.linerLayoutCourse:
//                index = 0;
//                break;
//            case R.id.linerLayoutExercises:
//                index = 1;
//                break;
//            case R.id.linerLayoutMy:
//                index = 2;
        }
        if (index != -1) {
            setBottomBarState(index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            setClickLoginText();
        }
    }

    private void setClickLoginText() {
        //初始值为"点击登录"
        String text = "点击登录";
        //判断
        if (UserRW.getBoolean(this, "LoginState")) {
            text = UserRW.getString(this, "LoginUsername");
        }
        myView.setTextViewClickLoginText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (courseView != null) {
            courseView.setRunning(false);
        }
    }
}
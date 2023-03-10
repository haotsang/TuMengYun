package com.example.myapplication.modules.space.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.adapter.CourseDetailAdapter;
import com.example.myapplication.modules.space.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView TextViewIntroduce, TextViewVideo, TextViewChapterIntroduce;
    private ListView ListViewVideoList;
    private ScrollView ScrollViewChapterIntroduce;
    private ArrayList<Video> videoList;
    private CourseDetailAdapter courseDetailAdapter;
    private int chapterId;
    private String intro;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        init();


    }
    /**
     * 初始化界面UI控件
     */
    private void init() {
        TextViewIntroduce = findViewById(R.id.TextViewIntroduce);
        TextViewVideo = findViewById(R.id.TextViewVideo);
        ListViewVideoList = findViewById(R.id.ListViewVideoList);
        TextViewChapterIntroduce = findViewById(R.id.TextViewChapterIntroduce);
        ScrollViewChapterIntroduce=findViewById(R.id.ScrollViewChapterIntroduce);

        intro=getIntent().getStringExtra("intro");
        TextViewChapterIntroduce.setText(intro);
        chapterId=getIntent().getIntExtra("ID",0);

        TextViewIntroduce.setOnClickListener(this);
        TextViewVideo.setOnClickListener(this);
    }

    /**
     * 设置视频列表本地数据
     */
    private void initData() {
        //解析字符串
        JSONArray jsonArray;
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("data.json");
            jsonArray = new JSONArray(read(inputStream));
            videoList = new ArrayList<Video>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Video video = new Video();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("chapterId") == chapterId) {
                    video.chapterId = jsonObject.getInt("chapterId");// 章节Id
                    //video.videoId = jsonObject.getInt("videoId");// 视频Id
                    video.title = jsonObject.getString("title");// 章节标题
                    video.secondTitle = jsonObject.getString("secondTitle");// 视频标题
                    video.videoPath = jsonObject.getString("videoPath");// 视频播放地址
                    videoList.add(video);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取数据流,参数in是数据流
     */
    private String read(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = null;
        String line = null;
        try {
//            实例化一个StringBuilder对象
            stringBuilder= new StringBuilder();
            //用InputStreamReader把in这个字节流转换成字符流BufferedReader
            bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            //从reader中读取一行的内容判断是否为空
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        try {
            if (inputStream!=null){
                inputStream.close();
                if (bufferedReader!=null)
                    bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 控件的点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.TextViewIntroduce:// 简介
                ListViewVideoList.setVisibility(View.GONE);
                ScrollViewChapterIntroduce.setVisibility(View.VISIBLE);
                TextViewIntroduce.setBackgroundColor(Color.parseColor("#30B4FF"));
                TextViewVideo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TextViewIntroduce.setTextColor(Color.parseColor("#FFFFFF"));
                TextViewVideo.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.TextViewVideo:// 视频
                ListViewVideoList.setVisibility(View.VISIBLE);
                ScrollViewChapterIntroduce.setVisibility(View.GONE);
                TextViewIntroduce.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TextViewVideo.setBackgroundColor(Color.parseColor("#30B4FF"));
                TextViewIntroduce.setTextColor(Color.parseColor("#000000"));
                TextViewVideo.setTextColor(Color.parseColor("#FFFFFF"));
                initData();
                CourseDetailAdapter courseDetailAdapter=new CourseDetailAdapter(this,videoList);
                ListViewVideoList.setAdapter(courseDetailAdapter);
                break;

        }

    }


}
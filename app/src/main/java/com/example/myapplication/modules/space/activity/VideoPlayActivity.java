package com.example.myapplication.modules.space.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.myapplication.R;
import com.example.myapplication.utils.RetrofitUtils;
import com.example.myapplication.utils.ViewUtils;


public class VideoPlayActivity extends AppCompatActivity {
    private VideoView videoViewPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.INSTANCE.setFullscreenCompat(this, true);

        setContentView(R.layout.activity_video_play);
        //横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        videoViewPlay=findViewById(R.id.videoViewPlay);

//        String video=getIntent().getStringExtra("video");
//        int videoId=getResources().getIdentifier(video,"raw",getPackageName());
//        String uri = "android.resource://" + getPackageName() + "/" + videoId;


        String uri2 = RetrofitUtils.TOMCAT_ROOT_URL + getIntent().getStringExtra("videoUrl");

        videoViewPlay.setVideoURI(Uri.parse(uri2));
        MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(videoViewPlay);
        videoViewPlay.start();
    }
}
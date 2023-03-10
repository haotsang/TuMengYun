package com.example.myapplication.modules.space.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.adapter.ExercisesDetailAdapter;
import com.example.myapplication.modules.space.model.ExercisesDetail;
import com.example.myapplication.modules.space.utils.ReadFromXML;
import com.example.myapplication.modules.space.view.TitleBarView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ExercisesDetailActivity extends AppCompatActivity {
    private ArrayList<ExercisesDetail> exercisesDetailList=null;
    private ListView listViewExercisesDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_detail);
        listViewExercisesDetail=findViewById(R.id.listViewExercisesDetail);
        getData();
        ExercisesDetailAdapter exercisesDetailAdapter=new ExercisesDetailAdapter(this,exercisesDetailList);
        listViewExercisesDetail.setAdapter(exercisesDetailAdapter);//关联
        TitleBarView titleBarView=new TitleBarView(this,getIntent().getStringExtra("title"));
    }
    private void getData(){
        try {
            int id=getIntent().getIntExtra("ID",0);
            InputStream inputStream=getAssets().open("chapter"+id+".xml");
            try {
                exercisesDetailList= ReadFromXML.getExercisesDetail(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
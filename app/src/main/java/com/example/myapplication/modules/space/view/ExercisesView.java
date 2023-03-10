package com.example.myapplication.modules.space.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.activity.ExercisesDetailActivity;
import com.example.myapplication.modules.space.adapter.ExercisesAdapter;
import com.example.myapplication.modules.space.model.Exercises;

import java.util.ArrayList;

public class ExercisesView implements View.OnClickListener {
    private LayoutInflater layoutInflater;//布局填充器
    private Activity activity;
    //布局和Java文件连接一起

    private View exercisesView;
    private ListView listViewExercises;

    public ExercisesView(Activity activity){
        this.activity=activity;
        layoutInflater=LayoutInflater.from(activity);//根据activity获取布局填充器
    }
    //创建视图
    public void creatView(){
        exercisesView=layoutInflater.inflate(R.layout.main_view_exercises,null);
        listViewExercises=exercisesView.findViewById(R.id.listViewExercises);
        final ArrayList<Exercises> exercisesArrayList=getData();//获取数据
        listViewExercises.setAdapter(new ExercisesAdapter(activity,getData()));//关联


        listViewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(activity,ExercisesDetailActivity.class);
                intent.putExtra("ID",position+1);
                intent.putExtra("title",exercisesArrayList.get(position).getTitle());//点某个位置的标题获取出来
                activity.startActivity(intent);
            }
        });



    }
    public View getView() {
        if (exercisesView==null){
            creatView();
        }
        return exercisesView;
    }
    public ArrayList<Exercises> getData() {

        ArrayList<Exercises> exercisesArrayList = new ArrayList<>();//实例化
        for (int i = 0; i < 10; i++) {
            Exercises exercises = new Exercises();
            switch (i) {
                case 0:
                    exercises.setTitle("第1章 Android基础入门");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_1);
                break;
                case 1:
                    exercises.setTitle("第2章 Android UI开发");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_2);
                break;
                case 2:
                    exercises.setTitle("第3章 Activity");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_3);
                    break;
                case 3:
                    exercises.setTitle("第4章 数据存储");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_4);
                    break;
                case 4:
                    exercises.setTitle("第5章 SQLite数据库");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_1);
                    break;
                case 5:
                    exercises.setTitle("第6章 广播接收者");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_2);
                    break;
                case 6:
                    exercises.setTitle("第7章 服务");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_3);
                    break;
                case 7:
                    exercises.setTitle("第8章 内容提供者");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_4);
                    break;
                case 8:
                    exercises.setTitle("第9章 网络编程");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_1);
                    break;
                case 9:
                    exercises.setTitle("第10章 高级编程");
                    exercises.setContent("共计5题");
                    exercises.setBackground(R.drawable.exercises_bg_2);
            }
            exercisesArrayList.add(exercises);
        }
        return exercisesArrayList;
    }
    @Override
    public void onClick(View view) {

    }
}

package com.example.myapplication.modules.space.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import okhttp3.OkHttpClient;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.adapter.CourseAdapter;
import com.example.myapplication.modules.space.model.Course;
import com.example.myapplication.modules.space.utils.CarouselShow;
import com.example.myapplication.modules.space.utils.KotlinUtils;
import com.example.myapplication.modules.space.utils.ReadFromXML;
import com.example.myapplication.utils.RetrofitUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CourseView {
    private LayoutInflater layoutInflater;//布局填充器
    private Activity activity;
    //布局和Java文件连接一起
    private View courseView;
    private ListView listViewCourse;

    private ViewPager ViewPage_Detail;
    private LinearLayout point_detail;
    private Context context;
    private ArrayList<Course> courseArrayList = new ArrayList<>();


    //轮播图图片资源
    private final int[] viewpage_images = {R.drawable.aa, R.drawable.bb, R.drawable.cc, R.drawable.dd};
    private ArrayList<ImageView> viewpage_imageList;
    //判断是否自动滚动ViewPage
    private boolean isRunning = true;
    private int categorize;
    public CourseView(Activity activity, int categorize){
        this.categorize = categorize;
        this.activity=activity;
        layoutInflater=LayoutInflater.from(activity);//根据activity获取布局填充器
    }
    public View getView() {
        if (courseView == null) {
            createView();
        }
        return courseView;
    }
    //创建视图
    public void createView(){
        //获取视图
        courseView=layoutInflater.inflate(R.layout.main_course,null);
        Instantiation();

        //获取数据(适配器和数据关联)
        listViewCourse = courseView.findViewById(R.id.listViewCourses);
//        getCourseData();
        CourseAdapter courseAdapter = new CourseAdapter(activity, courseArrayList);
        listViewCourse.setAdapter(courseAdapter);

        TextView tv = courseView.findViewById(R.id.courseType);
        String type = null;
        switch (categorize) {
            case 0:
                type = "物理科学";
                break;
            case 1:
                type = "化学";
                break;
            case 2:
                type = "生物";
                break;
            case 3:
                type = "数学";
                break;
            case 4:
                type = "生命安全";
                break;
            case 5:
                type = "DIY小制作";
                break;
        }
        tv.setText(type);

        getCourseData();
    }

//轮播
    public void Instantiation() {
        //关联控件
        ViewPage_Detail = courseView.findViewById(R.id.ViewPage_Detail);
        point_detail = courseView.findViewById(R.id.point_detail);

        //初始化图片资源
        viewpage_imageList = new ArrayList<ImageView>();
        //遍历图片资源
        for (int i : viewpage_images) {
            // 初始化图片资源
            ImageView imageView = new ImageView(activity);
            imageView.setBackgroundResource(i);
            viewpage_imageList.add(imageView);

            // 添加指示小点
            ImageView point = new ImageView(activity);
            //设置小点的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            params.rightMargin = 10;
            params.bottomMargin = 15;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.indicator_off);
            if (i == R.drawable.a) {
                //默认聚焦在第一张
                point.setBackgroundResource(R.drawable.indicator_on);
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            point_detail.addView(point);

        }

        //首页轮播
        CarouselShow carouselShow = new CarouselShow(context, viewpage_imageList);
        carouselShow.CarouselShow_Info_Detail(courseView);
        handler.sendEmptyMessageDelayed(0, 3000);//发送信息
    }
    //控制器（每三秒换一张图片）
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            // 执行滑动到下一个页面
            ViewPage_Detail.setCurrentItem(ViewPage_Detail.getCurrentItem() + 1);
            if (isRunning) {
                // 在发一个handler延时
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    };
    
//    public ArrayList<Course> getData() {
//
//        ArrayList<Course> coursesArrayList = new ArrayList<>();//实例化
//        for (int i = 0; i < 10; i++) {
//            Course course = new Course();
//            switch (i) {
//                case 0:
//                    course.setImgTitle("Android 开发环境搭建");
//                    course.setTitle("第1章 Android 基础入门");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                break;
//                case 1:
//                    course.setImgTitle("Android 五大布局");
//                    course.setTitle("第2章 Android UI开发");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                break;
//                case 2:
//                    course.setImgTitle("Activity 的使用");
//                    course.setTitle("第3章 Activity");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                    break;
//                case 3:
//                    course.setImgTitle("数据存储方式与文件存储");
//                    course.setTitle("第4章 数据存储");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                    break;
//                case 4:
//                    course.setImgTitle("SQLite 数据库与ListView");
//                    course.setTitle("第5章 SQLite 数据库");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                    break;
//                case 5:
//                    course.setImgTitle("广播接收者的类型与使用");
//                    course.setTitle("第6章 广播接收者");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                    break;
//                case 6:
//                    course.setImgTitle("服务创建、启动与生命周期");
//                    course.setTitle("第7章 服务");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                    break;
//                case 7:
//                    course.setImgTitle("内容提供者的使用");
//                    course.setTitle("第8章 内容提供者");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                    break;
//                case 8:
//                    course.setImgTitle("访问网络与数据提交方式");
//                    course.setTitle("第9章 网络编程");
//                    course.setIcon(R.drawable.chapter_1_icon);
//                    break;
//                case 9:
//                    course.setImgTitle("动画、多媒体、传感器等");
//                    course.setTitle("第10章 高级编程");
//                    course.setIcon(R.drawable.chapter_1_icon);
//            }
//            coursesArrayList.add(course);
//        }
//        return coursesArrayList;
//    }
//    @Override
//    public void onClick(View view) {
//
//    }
    /**
     * 获取课程信息
     */
    private void getCourseData() {
        String type = null;
        switch (categorize) {
            case 0:
                type = "physical";
                break;
            case 1:
                type = "chemistry";
                break;
            case 2:
                type = "biology";
                break;
            case 3:
                type = "math";
                break;
            case 4:
                type = "safety";
                break;
            case 5:
                type = "diy";
                break;
        }
        String url = RetrofitUtils.TOMCAT_ROOT_URL + "video/" + type + "/chapter.xml";
        try {
            new Thread(() -> {
                try {
                    InputStream inputStream2 = KotlinUtils.INSTANCE.getCourseData(url);


                    ArrayList<Course> subList = new ArrayList<>(ReadFromXML.getCourseInfos(inputStream2));

                    activity.runOnUiThread(()-> {
                        courseArrayList.clear();
                        courseArrayList.addAll(subList);
                        ((CourseAdapter) listViewCourse.getAdapter()).notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

//            InputStream inputStream = null;
//            inputStream = activity.getAssets().open("chaptertitle.xml");
//            courseArrayList = ReadFromXML.getCourseInfos(inputStream);//getCourseInfos(is)方法在下面会有说明
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public  void setRunning(boolean running){
        isRunning=running;
    }
}


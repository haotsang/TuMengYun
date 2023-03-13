package com.example.myapplication.modules.space.utils;

import com.example.myapplication.modules.space.model.Course;
import com.example.myapplication.modules.space.model.ExercisesDetail;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

public class ReadFromXML {
    public static ArrayList<ExercisesDetail> getExercisesDetail(InputStream xml)throws Exception
    {
        //XmlPullParserFactory pullPaser = XmlPullParserFactory.newInstance();
        ArrayList<ExercisesDetail> exercisesDetailArrayList = null;
        ExercisesDetail exercisesDetail = null;
        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(xml, "UTF-8");
        // 获得事件的类型
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    exercisesDetailArrayList = new ArrayList<ExercisesDetail>();//生产列表对象
                    break;
                case XmlPullParser.START_TAG:
                    if ("exercises".equals(parser.getName())) {
                        exercisesDetail = new ExercisesDetail();
                        // 取出属性值
                        int id = Integer.parseInt(parser.getAttributeValue(0));
                        exercisesDetail.setId(id);
                    } else if ("subject".equals(parser.getName())) {
                        String subject = parser.nextText();// 获取该节点的内容
                        exercisesDetail.setSubject(subject);
                    } else if ("a".equals(parser.getName())) {
                        String a = parser.nextText();// 获取该节点的内容
                        exercisesDetail.setAnswerA(a);
                    }else if ("b".equals(parser.getName())) {
                        String b = parser.nextText();// 获取该节点的内容
                        exercisesDetail.setAnswerB(b);
                    }else if ("c".equals(parser.getName())) {
                        String c = parser.nextText();// 获取该节点的内容
                        exercisesDetail.setAnswerC(c);
                    }else if ("d".equals(parser.getName())) {
                        String d = parser.nextText();// 获取该节点的内容
                        exercisesDetail.setAnswerD(d);
                    }
                    else if ("answer".equals(parser.getName())) {
                        int answer = Integer.parseInt(parser.nextText());
                        exercisesDetail.setAnswer(answer);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("exercises".equals(parser.getName())) {
                        exercisesDetailArrayList.add(exercisesDetail);
                        exercisesDetail = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return exercisesDetailArrayList;
    }


    /**解析课程*/
    public static ArrayList<Course> getCourseInfos(InputStream xml) throws Exception {

        //XmlPullParserFactory pullPaser = XmlPullParserFactory.newInstance();
        ArrayList<Course> coursesArrayList = null;
        Course course = null;
        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(xml, "UTF-8");
        // 获得事件的类型
        int eventType = parser.getEventType();
        // 如果文件没有读完
        while (eventType != XmlPullParser.END_DOCUMENT) {
            // 判断事件类型
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    coursesArrayList = new ArrayList<Course>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("course".equals(parser.getName())) {
                        course = new Course();
                        // 取出属性值
                        int id = Integer.parseInt(parser.getAttributeValue(0));
                        course.setId(id);
                    } else if ("imgtitle".equals(parser.getName())) {
                        String imgTitle = parser.nextText();// 获取该节点的内容
                        course.setImgTitle(imgTitle);
                    } else if ("title".equals(parser.getName())) {
                        String title = parser.nextText();// 获取该节点的内容
                        course.setTitle(title);
                    } else if ("intro".equals(parser.getName())) {
                        String intro = parser.nextText();// 获取该节点的内容
                        course.setIntro(intro);
                    } else if ("cover".equals(parser.getName())) { //封面图片
                        course.setCoverUrl(parser.nextText());
                    } else if ("video".equals(parser.getName())) { //视频链接
                        course.setVideoUrl(parser.nextText());
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("course".equals(parser.getName())) {
                        coursesArrayList.add(course);
                        course = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return coursesArrayList;
    }

}

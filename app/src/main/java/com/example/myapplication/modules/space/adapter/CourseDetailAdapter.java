package com.example.myapplication.modules.space.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.activity.VideoPlayActivity;
import com.example.myapplication.modules.space.model.Video;


import java.util.ArrayList;

public class CourseDetailAdapter extends BaseAdapter {
    private ArrayList<Video> mList;
    private Context mContext;
    private int selectedPosition = -1;//点击时选中的位置

    public CourseDetailAdapter(Context context, ArrayList<Video> list) {
        this.mContext = context;
        this.mList = list;
        //设置数据更新
        notifyDataSetChanged();
    }
    public void setSelected(int position) {
        selectedPosition = position;
    }
    /**
     * 获取Item的总数
     */
    @Override
    public int getCount() {
        return mList.size();
    }
    /**
     * 根据position得到对应Item的对象
     */
    @Override
    public Video  getItem(int position) {
        return mList.get(position);
    }
    /**
     * 根据position得到对应Item的id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 得到相应position对应的Item视图，
     * position是当前Item的位置，
     * convertView参数就是滚出屏幕的Item的View
     */
    //适配器
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            //通过xml文件填充界面
            convertView = LayoutInflater.from(mContext).inflate(R.layout.video_list_item, null);//填充界面
            holder = new ViewHolder();//创建视图
            //关联各个控件
            holder.TextViewVideoTitle = convertView.findViewById(R.id.TextViewVideoTitle);
            holder.ImageViewLeftImage = convertView.findViewById(R.id.ImageViewLeftImage);

            //存放再视图
            convertView.setTag(holder);
        } else {
            //复用convertView
            holder = (ViewHolder) convertView.getTag();
        }

        //赋值
        Video video= getItem(position);
        holder.ImageViewLeftImage.setImageResource(R.drawable.course_bar_icon);
        holder.TextViewVideoTitle.setTextColor(Color.parseColor("#333333"));
        if (video != null) {
            holder.TextViewVideoTitle.setText(video.secondTitle);
            // 设置选中效果
            if (selectedPosition == position) {
                holder.ImageViewLeftImage.setImageResource(R.drawable.course_intro_icon);
                holder.TextViewVideoTitle.setTextColor(Color.parseColor("#009958"));
            } else {
                holder.ImageViewLeftImage.setImageResource(R.drawable.course_bar_icon);
                holder.TextViewVideoTitle.setTextColor(Color.parseColor("#333333"));
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition=position;
                Intent intent=new Intent(mContext, VideoPlayActivity.class);
                intent.putExtra("video",getItem(position).getVideoPath());
                mContext.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder{//和item中的控件进行关联
        public TextView TextViewVideoTitle;
        public ImageView ImageViewLeftImage;
    }


}

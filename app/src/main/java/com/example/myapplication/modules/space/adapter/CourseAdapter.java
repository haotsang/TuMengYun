package com.example.myapplication.modules.space.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.activity.CourseDetailActivity;
import com.example.myapplication.modules.space.model.Course;

import java.util.ArrayList;

public class CourseAdapter extends BaseAdapter {
    private ArrayList<Course> mList;
    private Context mContext;

    public CourseAdapter(Context context, ArrayList<Course> list) {
        this.mContext = context;
        this.mList = list;
        //设置数据更新
        notifyDataSetChanged();
    }

    /**
     * 获取Item的总数
     */
    @Override
    public int getCount() {
        return mList.size() / 2;
    }

    /**
     * 根据position得到对应Item的对象
     */
    @Override
    public Course getItem(int position) {
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
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.course_list_item, null);//填充界面
            holder = new ViewHolder();//创建视图
            //关联各个控件
            holder.TextViewTitleLeft = convertView.findViewById(R.id.TextViewTitleLeft);
            holder.TextViewLeft = convertView.findViewById(R.id.TextViewLeft);
            holder.TextViewTitleRight = convertView.findViewById(R.id.TextViewTitleRight);
            holder.TextViewRight = convertView.findViewById(R.id.TextViewRight);
            holder.ImageViewLeft = convertView.findViewById(R.id.ImageViewLeft);
            holder.ImageViewRight = convertView.findViewById(R.id.ImageViewRight);
            holder.LinearLayoutLeft = convertView.findViewById(R.id.LinearLayoutLeft);
            holder.LinearLayoutRight = convertView.findViewById(R.id.LinearLayoutRight);
            //存放在视图中
            convertView.setTag(holder);
        } else {
            //复用convertView
            holder = (ViewHolder) convertView.getTag();
        }
        //左边
        holder.TextViewTitleLeft.setText(getItem(position * 2).getImgTitle());
        holder.TextViewLeft.setText(getItem(position * 2).getTitle());
        setImage(holder.ImageViewLeft, position * 2);

        holder.LinearLayoutLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳转到课程详情界面
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra("ID", getItem(position * 2).getId());
                    intent.putExtra("intro", getItem(position * 2).getIntro());
                    mContext.startActivity(intent);
                }
            });
        /**
        //防止下标溢出
        if (position*2+1<mList.size()){
            holder.TextViewTitleRight.setText(getItem(position * 2 + 1).getImgTitle());
            holder.TextViewRight.setText(getItem(position * 2 + 1).getTitle());
            setImage(holder.ImageViewRight, position * 2 + 1);
            holder.LinearLayoutRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra("ID", getItem(position * 2 + 1).getId());
                    intent.putExtra("intro", getItem(position * 2 + 1).getIntro());
                    mContext.startActivity(intent);
                }
            });
        }else{
            holder.TextViewTitleRight.setText("");
            holder.TextViewRight.setText("");
        }*/

    //右边
        holder.TextViewTitleRight.setText(getItem(position * 2 + 1).getImgTitle());
        holder.TextViewRight.setText(getItem(position * 2 + 1).getTitle());
        setImage(holder.ImageViewRight, position * 2 + 1);
        holder.LinearLayoutRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra("ID", getItem(position * 2 + 1).getId());
                    intent.putExtra("intro", getItem(position * 2 + 1).getIntro());
                    mContext.startActivity(intent);
                }
            });


        return convertView;
    }

    private void setImage(ImageView imageView, int position) {
        switch (position) {
            case 0:
                imageView.setBackgroundResource(R.drawable.chapter_1);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.chapter_2);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.chapter_3);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.chapter_4);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.chapter_5);
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.chapter_6);
                break;
            case 6:
                imageView.setBackgroundResource(R.drawable.chapter_7);
                break;
            case 7:
                imageView.setBackgroundResource(R.drawable.chapter_8);
                break;
            case 8:
                imageView.setBackgroundResource(R.drawable.chapter_9);
                break;
            case 9:
                imageView.setBackgroundResource(R.drawable.chapter_10);
                break;
        }
    }

    class ViewHolder {
        public TextView TextViewTitleLeft, TextViewLeft, TextViewTitleRight, TextViewRight;
        public ImageView ImageViewLeft, ImageViewRight;
        public LinearLayout LinearLayoutLeft, LinearLayoutRight;
    }
}


package com.example.myapplication.modules.space.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.model.Exercises;

import java.util.ArrayList;

public class ExercisesAdapter extends BaseAdapter {

    private ArrayList<Exercises> mList;
    private Context mContext;

    public ExercisesAdapter(Context context,ArrayList<Exercises> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Exercises getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {//获取当前位置的id
        return position;
    }
//适配器
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            //通过xml文件填充界面
            convertView = LayoutInflater.from(mContext).inflate(R.layout.exercises_list_item,null);//填充界面
            holder = new ViewHolder();//创建视图
            //关联各个控件
            holder.TextViewOrder = convertView.findViewById(R.id.TextViewOrder);
            holder.TextViewTitle = convertView.findViewById(R.id.TextViewTitle);
            holder.TextViewContent =convertView.findViewById(R.id.TextViewContent);
            convertView.setTag(holder);//存放视图
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.TextViewOrder.setText(position+1+"");//+""可以将整形转换为字符串
        holder.TextViewOrder.setBackgroundResource(getItem(position).getBackground());//资源
        holder.TextViewTitle.setText(getItem(position).getTitle());
        holder.TextViewContent.setText(getItem(position).getContent());
        return convertView;
    }

     class ViewHolder{//和item中的控件进行关联
        public TextView TextViewOrder,TextViewTitle,TextViewContent;
    }
}
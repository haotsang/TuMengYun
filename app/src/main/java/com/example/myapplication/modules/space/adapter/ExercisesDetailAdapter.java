package com.example.myapplication.modules.space.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.modules.space.model.ExercisesDetail;

import java.util.ArrayList;


public class ExercisesDetailAdapter extends BaseAdapter {
    private ArrayList<ExercisesDetail> mList;
    private Context mContext;

    public ExercisesDetailAdapter(Context context, ArrayList<ExercisesDetail> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ExercisesDetail getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //适配器
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            //通过xml文件填充界面
            convertView = LayoutInflater.from(mContext).inflate(R.layout.exercises_detail_items, null);//填充界面
            holder = new ViewHolder();//创建视图
            //关联各个控件
            holder.textViewSubject = convertView.findViewById(R.id.textViewSubject);
            holder.textViewA = convertView.findViewById(R.id.textViewA);
            holder.textViewB = convertView.findViewById(R.id.textViewB);
            holder.textViewC = convertView.findViewById(R.id.textViewC);
            holder.textViewD = convertView.findViewById(R.id.textViewD);
            holder.imageViewA = convertView.findViewById(R.id.imageViewA);
            holder.imageViewB = convertView.findViewById(R.id.imageViewB);
            holder.imageViewC = convertView.findViewById(R.id.imageViewC);
            holder.imageViewD = convertView.findViewById(R.id.imageViewD);
            holder.linerLayoutA = convertView.findViewById(R.id.linerLayoutA);
            holder.linerLayoutB = convertView.findViewById(R.id.linerLayoutB);
            holder.linerLayoutC = convertView.findViewById(R.id.linerLayoutC);
            holder.linerLayoutD = convertView.findViewById(R.id.linerLayoutD);
            //存放再视图
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        holder.textViewSubject.setText(getItem(position).getSubject());
        holder.textViewA.setText(getItem(position).getAnswerA());
        holder.textViewB.setText(getItem(position).getAnswerB());
        holder.textViewC.setText(getItem(position).getAnswerC());
        holder.textViewD.setText(getItem(position).getAnswerD());

        setImage(holder,position);//调用
        holder.linerLayoutA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(position).setSelected(1);//设置答案
                setImage(holder,position);
                notifyDataSetChanged();
            }
        });
        holder.linerLayoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(position).setSelected(2);
                setImage(holder,position);
                notifyDataSetChanged();
            }
        });
        holder.linerLayoutC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(position).setSelected(3);
                setImage(holder,position);
                notifyDataSetChanged();
            }
        });
        holder.linerLayoutD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(position).setSelected(4);
                setImage(holder,position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
    private void setImage(ViewHolder holder,int position){
        //初始化
        holder.imageViewA.setImageResource(R.drawable.exercises_a);
        holder.imageViewB.setImageResource(R.drawable.exercises_b);
        holder.imageViewC.setImageResource(R.drawable.exercises_c);
        holder.imageViewD.setImageResource(R.drawable.exercises_d);
        int selected=getItem(position).getSelected();
        if (selected!=0){
            switch (selected){
                case 1:holder.imageViewA.setImageResource(R.drawable.exercises_error_icon);
                    break;
                case 2:holder.imageViewB.setImageResource(R.drawable.exercises_error_icon);
                    break;
                case 3:holder.imageViewC.setImageResource(R.drawable.exercises_error_icon);
                    break;
                case 4:holder.imageViewD.setImageResource(R.drawable.exercises_error_icon);
                    break;
            }
            int answer=getItem(position).getAnswer();
            switch (answer){
                case 1:holder.imageViewA.setImageResource(R.drawable.exercises_right_icon);
                    break;
                case 2:holder.imageViewB.setImageResource(R.drawable.exercises_right_icon);
                    break;
                case 3:holder.imageViewC.setImageResource(R.drawable.exercises_right_icon);
                    break;
                case 4:holder.imageViewD.setImageResource(R.drawable.exercises_right_icon);
                    break;
            }
            holder.linerLayoutA.setEnabled(false);//设置属性不可用
            holder.linerLayoutB.setEnabled(false);
            holder.linerLayoutC.setEnabled(false);
            holder.linerLayoutD.setEnabled(false);
        }else {
            holder.linerLayoutA.setEnabled(true);
            holder.linerLayoutB.setEnabled(true);
            holder.linerLayoutC.setEnabled(true);
            holder.linerLayoutD.setEnabled(true);
        }
    }
        class ViewHolder{//和item中的控件进行关联
            public TextView textViewSubject,textViewA,textViewB,textViewC,textViewD;
            public ImageView imageViewA,imageViewB,imageViewC,imageViewD;
            public LinearLayout linerLayoutA,linerLayoutB,linerLayoutC,linerLayoutD;
        }

}
package com.example.myapplication.modules.space.view;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class TitleBarView {
    private TextView textViewGoBack,textViewTitle;
    private RelativeLayout relativeLayoutTitleBar;

    public TitleBarView(final Activity activity,String title) {
        textViewGoBack=activity.findViewById(R.id.textViewGoBack);
        textViewTitle=activity.findViewById(R.id.textViewTitle);
        relativeLayoutTitleBar=activity.findViewById(R.id.relativeLayoutTitleBar);
        relativeLayoutTitleBar.setBackgroundColor(Color.parseColor("#0097F7"));
        textViewTitle.setText(title);
        textViewGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
}

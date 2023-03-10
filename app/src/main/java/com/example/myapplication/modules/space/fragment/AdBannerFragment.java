package com.example.myapplication.modules.space.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;

import androidx.fragment.app.Fragment;

/*创建AdBannerFragment类去设置ViewPager控件中的数据*/
public class AdBannerFragment extends Fragment {
	private String ab;// 广告
	private ImageView image;// 图片
	public static AdBannerFragment newInstance(Bundle args) {
		AdBannerFragment af = new AdBannerFragment();
		af.setArguments(args);
		return af;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arg = getArguments();
		// 获取广告图片名称
		ab = arg.getString("ad");

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onResume() {
		super.onResume();


		if (ab != null) {
			if ("banner_1".equals(ab)) {
				image.setImageResource(R.drawable.banner_1);
			} else if ("banner_2".equals(ab)) {
				image.setImageResource(R.drawable.banner_2);
			} else if ("banner_3".equals(ab)) {
				image.setImageResource(R.drawable.banner_3);
			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (image != null) {
			image.setImageDrawable(null);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 创建广告图片控件
		image = new ImageView(getActivity());
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		image.setLayoutParams(lp);// 设置图片宽高参数
		image.setScaleType(ImageView.ScaleType.FIT_XY);// 把图片塞满整个控件
		return image;
	}
}
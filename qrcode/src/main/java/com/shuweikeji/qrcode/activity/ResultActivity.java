package com.shuweikeji.qrcode.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.widget.TextView;

import com.shuweikeji.qrcode.R;


/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description: 显示二维码图像扫描解析结果的界面
 */
public class ResultActivity extends AppCompatActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_code_result);
		String result = getIntent().getStringExtra("result");
		TextView tv = (TextView) findViewById(R.id.qrcode_result_text);
		tv.setText(result);
		Linkify.addLinks(tv, Linkify.ALL);
	}
}

package com.blueyea.mobilesafe.activity;

import com.blueyea.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AntitheftGuideActivity3 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	public void previous(View view){
//		Intent intent = new Intent(this, AntitheftGuideActivity2.class);
//		startActivity(intent);
		this.finish();
	}
	
	public void next(View view){
		Intent intent = new Intent(this, AntitheftGuideActivity4.class);
		startActivity(intent);
	}
}

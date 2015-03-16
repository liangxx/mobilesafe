package com.blueyea.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blueyea.mobilesafe.R;

public class AntitheftGuideActivity1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
		
	}
	
	public void next(View view){
		Intent intent = new Intent(this, AntitheftGuideActivity2.class);
		startActivity(intent);
	}
}

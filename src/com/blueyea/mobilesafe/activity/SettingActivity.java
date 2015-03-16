package com.blueyea.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.blueyea.mobilesafe.R;
import com.blueyea.mobilesafe.ui.SettingItem;

public class SettingActivity extends Activity {
	private SettingItem setting_autoupdate;
	private SharedPreferences setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		setting_autoupdate = (SettingItem) findViewById(R.id.autoupdate);
		
		setting = getSharedPreferences("setting", MODE_PRIVATE);
		boolean autoupdate = setting.getBoolean("autoupdate", false);
		setting_autoupdate.setCheck(autoupdate);
		
		setting_autoupdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting_autoupdate.isCheck()){
					setting_autoupdate.setCheck(false);
					setAutoUpdate(false);
				}else {
					setting_autoupdate.setCheck(true);
					setAutoUpdate(true);
				}
			}
		});
	}

	protected void setAutoUpdate(boolean b) {
		Editor editor = setting.edit();
		editor.putBoolean("autoupdate", b);
		editor.commit();
	}
}

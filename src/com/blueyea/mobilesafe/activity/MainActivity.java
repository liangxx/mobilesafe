package com.blueyea.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueyea.mobilesafe.R;

public class MainActivity extends Activity {

	private SharedPreferences setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setting = getSharedPreferences("setting", MODE_PRIVATE);

		GridView gv_list = (GridView) findViewById(R.id.gv_list);
		gv_list.setAdapter(new HomeAdapter());

		gv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					boolean b = setting.getBoolean("antitheft_guide", false);
					if (!b) {
						checkPwd();
					} else
						enterGuide();
					break;
				case 8:
					Intent intent = new Intent(MainActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 输入验证密码
	 */
	protected void checkPwd() {
		View view = View.inflate(this, R.layout.ui_checkpwd, null);
		AlertDialog dialog = new AlertDialog.Builder(this).setView(view)
				.create();
		dialog.show();
	}

	/**
	 * 进入防盗功能引导界面
	 */
	protected void enterGuide() {

	}

	/**
	 * 程序主界面菜单适配器
	 * 
	 * @author liangxu
	 * 
	 */
	private class HomeAdapter extends BaseAdapter {

		String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
				"缓存清理", "高级应用", "设置中心" };
		int[] images = { R.drawable.safe, R.drawable.callmsgsafe,
				R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
				R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
				R.drawable.settings };

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView == null ? View.inflate(MainActivity.this,
					R.layout.home_list_item, null) : convertView;
			ImageView home_item_iv = (ImageView) view
					.findViewById(R.id.home_item_iv);
			TextView home_item_tv = (TextView) view
					.findViewById(R.id.home_item_tv);
			home_item_iv.setImageResource(images[position]);
			home_item_tv.setText(names[position]);

			return view;
		}

	}
}

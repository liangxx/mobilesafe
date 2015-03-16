package com.blueyea.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.Toast;

import com.blueyea.mobilesafe.R;
import com.blueyea.mobilesafe.utils.Md5Utils;

public class MainActivity extends Activity {

	private SharedPreferences setting;
	private AlertDialog dialog;

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
					String antitheft_pwd = setting.getString("antitheft_pwd", null);
					if(antitheft_pwd == null ){
						setAntitheftPwd();
					}else if (b) {
						checkPwd();
					}else {
						enterGuide();
					}
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
	 * 设置防盗功能密码
	 */
	protected void setAntitheftPwd() {
		View view = View.inflate(this, R.layout.dialog_setpwd, null);
		dialog = new AlertDialog.Builder(this).create();
		dialog.setView(view, 0, 0, 0, 0);
		
		final EditText et_pwd = (EditText) view.findViewById(R.id.pwd);
		final EditText et_pwd_confirm = (EditText) view.findViewById(R.id.pwd_confirm);
		Button sure = (Button) view.findViewById(R.id.alert_sure);
		Button cancel = (Button) view.findViewById(R.id.alert_cancel);
		
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd = et_pwd.getText().toString().trim();
				String pwd_confirm = et_pwd_confirm.getText().toString().trim();
				if("".equals(pwd)) {
					Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(pwd_confirm)) {
					Toast.makeText(MainActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!pwd.equals(pwd_confirm)){
					Toast.makeText(MainActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				Editor edit = setting.edit();
				edit.putString("antitheft_pwd", Md5Utils.encode(pwd));
				edit.commit();
				enterAntitheft();
				dialog.dismiss();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		
		dialog.show();
	}

	/**
	 * 输入验证密码
	 */
	protected void checkPwd() {
		View view = View.inflate(this, R.layout.dialog_checkpwd, null);
		dialog = new AlertDialog.Builder(this).create();
		dialog.setView(view, 0, 0, 0, 0);
		
		final EditText et_pwd = (EditText) view.findViewById(R.id.chpwd);
		Button sure = (Button) view.findViewById(R.id.alert_sure);
		Button cancel = (Button) view.findViewById(R.id.alert_cancel);
		
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd = et_pwd.getText().toString().trim();
				String antitheft_pwd = setting.getString("antitheft_pwd", null);
				if("".equals(pwd)) {
					Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!Md5Utils.encode(pwd).equals(antitheft_pwd)){
					Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
					return;
				}
				enterAntitheft();
				dialog.dismiss();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		
		dialog.show();
	}

	/**
	 * 进入防盗功能界面
	 */
	protected void enterAntitheft() {
		Intent intent = new Intent(this, AntitheftActivity.class);
		startActivity(intent);
		
	}

	/**
	 * 进入防盗功能引导界面
	 */
	protected void enterGuide() {
		Intent intent = new Intent(this, AntitheftGuideActivity1.class);
		startActivity(intent);
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
					R.layout.item_home_list, null) : convertView;
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

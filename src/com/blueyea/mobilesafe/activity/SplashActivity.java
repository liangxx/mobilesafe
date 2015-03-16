package com.blueyea.mobilesafe.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.blueyea.mobilesafe.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SplashActivity extends Activity {

	private static final int NET_ERROR = 1;
	private static final int JSON_ERROR = 2;
	private static final int ENTER_HOME = 3;
	private static final int SHOW_UPDATE_DIALOG = 4;

	private String description;
	private String appurl;

	private TextView splash_updatepb;

	private AsyncHttpClient client;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(getApplicationContext(), "网络错误",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(), "JSON解析错误",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case ENTER_HOME:

				enterHome();
				break;
			case SHOW_UPDATE_DIALOG:
				Toast.makeText(getApplicationContext(),
						"-----------------------", Toast.LENGTH_SHORT).show();
				showUpdateDialog();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
		if(setting.getBoolean("autoupdate", false)) checkandupdate();
		else {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					enterHome();
				}
			};
			timer.schedule(task, 3000);
		}

	}

	private void checkandupdate() {
		final long starttime = System.currentTimeMillis();
		splash_updatepb = (TextView) findViewById(R.id.splash_update_pb);

		client = new AsyncHttpClient();
		client.setConnectTimeout(5000);
		client.get("http://192.168.199.173/ROOT/version.html",
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						System.out
								.println("onStart--------------------------------");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						Message msg = Message.obtain();
						System.out.println(new String(response)
								+ "-----------------------------");
						try {
							JSONObject json = new JSONObject(new String(
									response));
							int versionCode = json.getInt("version");

							if (versionCode > getAppVersionCode()) {
								// 显示是否升级对话框
								description = json.getString("description");
								appurl = json.getString("appurl");
								msg.what = SHOW_UPDATE_DIALOG;
							} else
								msg.what = ENTER_HOME;
						} catch (JSONException e) {
							e.printStackTrace();
							msg.what = JSON_ERROR;
						}
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						Toast.makeText(getApplicationContext(), "onFailure------", Toast.LENGTH_SHORT).show();
						long endtime = System.currentTimeMillis();
						long dtime = endtime - starttime;
						Message msg = Message.obtain();
						if (dtime < 3000) {
							try {
								Thread.sleep(3000 - dtime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						msg.what = NET_ERROR;
						handler.sendMessage(msg);
					}

					@Override
					public void onRetry(int retryNo) {
						System.out.println("onRetry--------------------------------");
					}
				});
	}

	protected void showUpdateDialog() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Toast.makeText(getApplicationContext(), appurl,
					// Toast.LENGTH_LONG).show();
					updateApp();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					enterHome();
					break;
				}
			}
		};

		new AlertDialog.Builder(this) // 下面方法返回值都是Builder, 可以连续调用
				.setTitle("更新提醒") // 标题
				.setMessage(description) // 具体消息
				.setCancelable(false) // 是否可取消
				.setPositiveButton("升级", listener) // 右边按钮
				.setNegativeButton("下次再说", listener) // 左边按钮
				.show();
	}

	protected void updateApp() {
		//Toast.makeText(getApplicationContext(),Environment.getExternalStorageDirectory().getAbsolutePath(),Toast.LENGTH_LONG).show();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			// 下载最新的APK apkurl
			FinalHttp http = new FinalHttp();
			http.download(appurl, Environment.getExternalStorageDirectory().getAbsolutePath() + "/update.apk",new AjaxCallBack<File>() {

				@Override
				public void onFailure(Throwable t, int errorNo,String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					t.printStackTrace();
					Toast.makeText(SplashActivity.this, "下载失败", 1).show();
				}

				@Override
				public void onLoading(long count, long current) {
					super.onLoading(count, current);
					int progress = (int) (current * 100 / count);
					splash_updatepb.setText("下载进度：" + progress + "%");
				}

				@Override
				public void onSuccess(File t) {
					super.onSuccess(t);
					installAPK(t);
				}

				private void installAPK(File t) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setDataAndType(Uri.fromFile(t),
							"application/vnd.android.package-archive");
					startActivity(intent);

				}

			});
		} else {
			Toast.makeText(SplashActivity.this, "sd卡不存在", 1).show();
		}
	}

	protected void enterHome() {
		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	protected int getAppVersionCode() {
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
}

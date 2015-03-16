package com.blueyea.mobilesafe.ui;

import com.blueyea.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItem extends RelativeLayout {

	private CheckBox settint_item_cb;
	private TextView setting_item_desc;
	private TextView setting_item_title;
	
	String setting_item_title_str;
	String setting_item_descOn_str;
	String setting_item_descOff_str;

	private void initView(Context context) {
		View view = View.inflate(context, R.layout.setting_item, SettingItem.this);
		
		settint_item_cb = (CheckBox) view.findViewById(R.id.setting_item_cb);
		setting_item_desc = (TextView) view.findViewById(R.id.setting_item_desc);
		setting_item_title = (TextView) view.findViewById(R.id.setting_item_title);
	}
	
	private void setTitle() {
		setting_item_title.setText(setting_item_title_str);
	}

	protected void setDesc(String string) {
		this.setting_item_desc.setText(string);
	}

	public void setCheck(boolean b) {
		this.settint_item_cb.setChecked(b);
		if(b) setting_item_desc.setText(setting_item_descOn_str);
		else setting_item_desc.setText(setting_item_descOff_str);
	}

	public boolean isCheck() {
		return this.settint_item_cb.isChecked();
	}

	public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public SettingItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		setting_item_title_str = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.blueyea.mobilesafe", "setting_item_title");
		setting_item_descOn_str = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.blueyea.mobilesafe", "setting_item_descOn");
		setting_item_descOff_str = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.blueyea.mobilesafe", "setting_item_descOff");
		setTitle();
	}

	public SettingItem(Context context) {
		super(context);
		initView(context);
	}

	
	
}

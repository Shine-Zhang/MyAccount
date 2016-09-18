package com.example.zs.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.zs.myaccount.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class KeyboardUtil {
	private Context ctx;
	private Activity act;
	private KeyboardView keyboardView;
	private Keyboard k1;// 字母键盘
	private Keyboard k2;// 数字键盘
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写
	private KeyBoardConfirmListener mKeyBoardConfirmListenerL;
	private String rule;
	private EditText ed;
	private PopupWindow popupwindow;
	private final View mView;

	public KeyboardUtil(Activity act, Context ctx, EditText edit) {
		this.act = act;
		this.ctx = ctx;
		this.ed = edit;
		k1 = new Keyboard(ctx, R.xml.qwerty);
		k2 = new Keyboard(ctx, R.xml.symbols);
		mView = View.inflate(ctx, R.layout.show_account_budget_sta_keybordview,null);
		keyboardView = (KeyboardView) mView.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k2);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			int length = editable.length();
			if(length>0){
				ed.setSelection(length);
				start = length;
			}
			//String tmp =Character.toString((char) primaryCode);
			char tmp = (char) primaryCode;
			if(tmp>='0'&&tmp<='9'||tmp=='.'){
				if (!stringFilter(ed.getText().toString() + tmp)) {
					return;
				}
			}
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				if(mKeyBoardConfirmListenerL!=null){
					mKeyBoardConfirmListenerL.toConfirm();
				}
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start >= 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(k1);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
					keyboardView.setKeyboard(k2);
				}
			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else {

				if ("0".equals(ed.getText().toString())) {
					ed.setText(Character.toString((char) primaryCode));
				}

				editable.insert(start, Character.toString((char) primaryCode));
			}
		}

	};

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {//大写切换小写
			isupper = false;
			for(Key key:keylist){
				if (key.label!=null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0]+32;
				}
			}
		} else {//小写切换大写
			isupper = true;
			for(Key key:keylist){
				if (key.label!=null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0]-32;
				}
			}
		}
	}

	public void showKeyboard(View view) {
		Log.i("haha","showKeyboard");


		if(!TextUtils.isEmpty(ed.getText().toString())){
			ed.setText("");
		}
		//弹出PopWindow供用户选择
		View contentView= mView;

		//初始化popupwindow
		popupwindow = new PopupWindow();
		//获得焦点
		popupwindow.setFocusable(true);
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		//设置popupwindow弹出和退出时的动画效果
		popupwindow.setAnimationStyle(R.style.AnimationBottomFade);
		//将popup_view部署到popupWindow上
		popupwindow.setContentView(contentView);
		//设置popupWindow的宽高（必须要设置）
		popupwindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
		popupwindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
		//设置popupwindow显示的位置
		popupwindow.showAtLocation(view, Gravity.BOTTOM,0,0);

		//keyboardView.setVisibility(View.VISIBLE);

	}

	public void hideKeyboard() {
/*		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.GONE);
		}*/

			if (popupwindow != null) {
				popupwindow.dismiss();//隐藏气泡
				popupwindow = null;
			}
	}

	private boolean isword(String str){
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase())>-1) {
			return true;
		}
		return false;
	}

	public  boolean stringFilter(String str)throws PatternSyntaxException {

		Pattern p   =   Pattern.compile(rule);
		Matcher m   =   p.matcher(str);
		return   m.matches();
	}


	public void setNumberFormat(int intDigits) {
		rule = "^\\d{1,"+intDigits+"}|^\\d{1,"+intDigits+"}\\.\\d{0,2}";
	}

	public void setOnkeyBoardConfirmListener(KeyBoardConfirmListener keyBoardConfirmListenerL){

		this.mKeyBoardConfirmListenerL = keyBoardConfirmListenerL;


	}
	public interface KeyBoardConfirmListener{

		public void toConfirm();

	}

}
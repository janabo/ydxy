package com.dk.mp.oldoa.dialog;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.mp.oldoa.R;

/**
 * 作者：janabo on 2017/2/20 14:45
 */
public class MsgDialog {
/**
 * 公共对话框.
 *
 * @param title
 *            标题
 * @param content
 *            内容
 * @param okClick
 *            确定按钮点击事件.
 * @return AlertDialog
 */
//	public static void show(Context context, String message) {
//		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//
//	}


        /**
         * 公共对话框.
         *
         * AlertDialog
         */
        public static void show(Context context, String message) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.core_toast, null);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(Html.fromHtml(message));
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }

}

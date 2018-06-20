
package hawk.com.zjjapplication.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import hawk.com.zjjapplication.R;

/**
 * @author jack_sj
 * @company yjlc - 易捷联创
 * @name UIUtil.java
 * @create 2014-10-13
 * @descript
 */
public class UIUtil {
    protected static AlertDialog alert;
    protected static Dialog dialog;
    protected static Dialog mTextDialog;
    protected static CustomProgressdialog customProgressdialog;

    public static void showProgressDialog(Context ctx) {
        if (null != dialog)
            return;
        try {
            customProgressdialog = new CustomProgressdialog(ctx,"加载中",true,true);
//            dialog = new AlertDialog.Builder(ctx).create();
//            dialog.show();
//            dialog.setCancelable(false);
////			dialog.setContentView(ResourceUtil.getLayoutId(ctx, "loading_process_dialog_anim"));
//            dialog.setContentView(R.layout.loading_process_dialog_anim);
//
//            ImageView imageView = (ImageView) dialog.findViewById(R.id.loading_process_dialog_imageview);
//            final AnimationDrawable someAnimation = (AnimationDrawable) imageView.getBackground();
//            imageView.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    someAnimation.start();
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressDialog(Context ctx, String name, OnCancelListener listener) {
        if (null != mTextDialog)
            return;
        try {
            mTextDialog = new AlertDialog.Builder(ctx).create();
            mTextDialog.show();
            mTextDialog.setCancelable(true);
            mTextDialog.setOnCancelListener(listener);
            mTextDialog.setContentView(R.layout.loading_process_dialog_anim);
            ImageView imageView = (ImageView) mTextDialog.findViewById(R.id.loading_process_dialog_imageview);
            TextView proName = (TextView) mTextDialog.findViewById(R.id.progress_name);
            if (null != name) {
                proName.setVisibility(View.VISIBLE);
                proName.setText(name);
            } else {
                proName.setVisibility(View.GONE);
            }

            final AnimationDrawable someAnimation = (AnimationDrawable) imageView.getBackground();
            imageView.post(new Runnable() {

                @Override
                public void run() {
                    someAnimation.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (customProgressdialog != null && customProgressdialog.isShowing()) {
                customProgressdialog.dismiss();
                customProgressdialog = null;
//                dialog.dismiss();
//                dialog = null;
            }
            if (mTextDialog != null && mTextDialog.isShowing()) {
                mTextDialog.dismiss();
                mTextDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismissProgressDialog(Context context) {
        try {
            if (context != null && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            if (mTextDialog != null && mTextDialog.isShowing()) {
                mTextDialog.dismiss();
                mTextDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void hideInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static int getIntOfString(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        } else {
            int number = 0;
            try {
                number = Integer.parseInt(str);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return number;
        }
    }

    public static void closeAlert() {
        if (null != alert)
            alert.cancel();
        alert = null;
    }

    /**
     * 动态设置ListView组建的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        /** params.height += 5;
         // if without this statement,the listview will be a little short
         listView.getDividerHeight()获取子项间分隔符占用的高度
         params.height最后得到整个ListView完整显示需要的高度
         */
        listView.setLayoutParams(params);
    }

    public static boolean isEmptyString(String str) {
        return null == str || "".equals(str);
    }


    public interface TimePicker {
        public void pickTime(String time);
    }


    public interface MulitySelecter {
        public void selectOver(HashMap<Integer, Boolean> isSelected);
    }

}

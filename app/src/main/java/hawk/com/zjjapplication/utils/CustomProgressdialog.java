
package hawk.com.zjjapplication.utils;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import hawk.com.zjjapplication.R;


/**
 * 自定义滚动框
 * 
 * @author wangyue
 */
public class CustomProgressdialog extends CustomDialog {

	/** 显示的信息 */
	private String message = null;
	/** 是否取消 */
	private boolean cancelable = false;


	public CustomProgressdialog(Context context, String message,
                                boolean cancelable, boolean isShow) {
		super(context);
		this.message = message;
		this.cancelable = cancelable;
		if (isShow) {
			show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setCancelable(cancelable);
		setContentView(R.layout.custom_dialog_progress_layout);

		TextView textView = (TextView) this.findViewById(R.id.dialog_progress_layout_tv_message);
		if (!TextUtils.isEmpty(message)) {
			textView.setText(message);
		} else {
			textView.setVisibility(View.GONE);
		}
	}

}

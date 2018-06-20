
package hawk.com.zjjapplication.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import hawk.com.zjjapplication.R;


/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title CustomTextViewDialog.java
 * @Description: 自定义纯文本显示对话框
 * @date 2016-3-17 下午2:16:39
 */
public class CustomTextViewDialog extends CustomDialog {

    private TextView textview_content;
    private TextView certainButton;
    private TextView cancelButton;

    public CustomTextViewDialog(Context context) {
        super(context);
        if (null != context) {
            this.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_textview_layout);
        textview_content = (TextView) findViewById(R.id.dialog_textview_layout_tv_content);
        textview_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        certainButton = (TextView) findViewById(R.id.dialog_textview_layout_btn_1);
        cancelButton = (TextView) findViewById(R.id.dialog_textview_layout_btn_2);
    }

    public TextView getContentTextview() {
        return textview_content;
    }

    /**
     * 设置内容
     *
     * @param message
     */
    public void setMessage(String message) {
        if (textview_content != null) {
            textview_content.setText(message);
        }
    }

    /**
     * 设置内容
     *
     * @param messageId
     */
    public void setMessage(int messageId) {
        if (textview_content != null) {
            textview_content.setText(messageId);
        }
    }

    public void showCertainButton(boolean flag) {
        if (flag) {
            if (certainButton != null) {
                certainButton.setVisibility(View.VISIBLE);
            }
        } else {
            if (certainButton != null) {
                certainButton.setVisibility(View.GONE);
            }
        }
    }

    public void showButtonVisable() {
        certainButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置确定按键
     *
     * @param id
     * @param onClickListener
     */
    public void setCertainButton(int id, View.OnClickListener onClickListener) {
        if (certainButton != null) {
            certainButton.setText(id);
            certainButton.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置确定按键
     *
     * @param id
     * @param onClickListener
     */
    public void setCertainButton(String id, View.OnClickListener onClickListener) {
        if (certainButton != null) {
            certainButton.setText(id);
            certainButton.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置取消按键
     *
     * @param id
     * @param onClickListener
     */
    public void setCancelButton(int id, View.OnClickListener onClickListener) {
        if (cancelButton != null) {
            cancelButton.setText(id);
            cancelButton.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置取消按键
     *
     * @param id
     * @param onClickListener
     */
    public void setCancelButton(String id, View.OnClickListener onClickListener) {
        if (cancelButton != null) {
            cancelButton.setText(id);
            cancelButton.setOnClickListener(onClickListener);
        }
    }

}

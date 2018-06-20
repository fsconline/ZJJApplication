package hawk.com.zjjapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hawk.com.zjjapplication.BaseActivity;
import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.net.RequestListener;
import hawk.com.zjjapplication.request.FormLoginRequest;
import hawk.com.zjjapplication.utils.Tools;
import hawk.com.zjjapplication.utils.ToolsPreferences;
import hawk.com.zjjapplication.utils.UIUtil;
import hawk.com.zjjapplication.view.ButtomBtn;

/**
 * 登录页面
 * Created by Administrator on 2017/2/28 0028.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.bomBtnMsg)
    ButtomBtn bomBtnMsg;
    @BindView(R.id.bomBtnBack)
    ButtomBtn bomBtnBack;

    @BindView(R.id.ll_title)
    TextView titlename;
    @BindView(R.id.cbDisplayPassword)
    CheckBox cbDisplayPassword;


    private EditText username;
    private EditText password;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout_fsc);

        ButterKnife.bind(this);

        assignViews();
        initTopBtn();
    }

    private void assignViews() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        String name = ToolsPreferences.getPreferences(ToolsPreferences.USERNAME);
        String psd = ToolsPreferences.getPreferences(ToolsPreferences.PASSWORD);
        if (!TextUtils.isEmpty(name)) {
            username.setText(name);
        }
        if (!TextUtils.isEmpty(psd)) {
            password.setText(psd);
        }
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    Tools.Toast("请先输入用户名", false);
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Tools.Toast("请先输入密码", false);
                } else {
                    login();
                }
            }
        });

        cbDisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


    }

    private void login() {

        UIUtil.showProgressDialog(this);
        FormLoginRequest request = new FormLoginRequest(this, new RequestListener() {
            @Override
            public void successBack(Object object) {

                ToolsPreferences.setPreferences(ToolsPreferences.USERNAME, username.getText().toString());
                ToolsPreferences.setPreferences(ToolsPreferences.PASSWORD, password.getText().toString());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                UIUtil.dismissProgressDialog();
                LoginActivity.this.finish();
            }

            @Override
            public void failBack(Object object) {
                Tools.Toast(object.toString(), false);
                UIUtil.dismissProgressDialog();
            }
        });
        request.setLogin_name(username.getText().toString());
        request.setPassword(password.getText().toString());
        request.startRequest();
    }

    private void initTopBtn() {

//        bomBtnBack.setIvAndTv(R.drawable.arrow_back01, "返回");
        titlename.setText("登录");
    }

    @OnClick(R.id.bomBtnBack)
    public void onClickBack(View v) {
        finish();
    }

}

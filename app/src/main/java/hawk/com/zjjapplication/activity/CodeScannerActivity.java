package hawk.com.zjjapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;

import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.utils.AppConstant;

/**
 * 二维码扫描
 */
public class CodeScannerActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnQrCode; // 扫码
    TextView tvResult; // 结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner_fsc);

        initView();
    }

    private void initView() {
        btnQrCode = (Button) findViewById(R.id.btn_qrcode);
        btnQrCode.setOnClickListener(this);

        tvResult = (TextView) findViewById(R.id.txt_result);
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(CodeScannerActivity.this, new String[]{Manifest.permission.CAMERA}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(CodeScannerActivity.this, CaptureActivity.class);
        startActivityForResult(intent, AppConstant.REQ_QR_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_qrcode:
                startQrCode();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == AppConstant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(AppConstant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            tvResult.setText(scanResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(CodeScannerActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
package com.example.kq.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.example.kq.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class phoneRegister extends BaseActivity implements View.OnClickListener {

    private Button validateNum_btn;
    private Button landing_btn;
    private EditText userName;
    private EditText validateNum;
    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
    }
    private void initEvent(){
        userName = (EditText) findViewById(R.id.et_account);
        validateNum = (EditText) findViewById(R.id.et_phonecode);
        validateNum_btn = (Button) findViewById(R.id.et_phonecode_btn);
        landing_btn = (Button) findViewById(R.id.btn_register);
        validateNum_btn.setOnClickListener(this);
        landing_btn.setOnClickListener(this);
        mTimeCount = new TimeCount(60000, 1000);
    }
    private void init(){
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                        navigateTo(RegisterActivity.class); //页面跳转
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功

                    } else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){ //返回支持发送验证码的国家列表‘【

                    }
                } else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_phonecode_btn:
                if(!userName.getText().toString().trim().equals("")){
                    if (checkTel(userName.getText().toString().trim())) {
                        SMSSDK.getVerificationCode("+86",userName.getText().toString());//获取验证码
                        mTimeCount.start();
                    }else{
                        Toast.makeText(phoneRegister.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(phoneRegister.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                if (!userName.getText().toString().trim().equals("")) {
                    if (checkTel(userName.getText().toString().trim())) {
                        if (!validateNum.getText().toString().trim().equals("")) {
                            SMSSDK.submitVerificationCode("+86",userName.getText().toString().trim(),validateNum.getText().toString().trim());//提交验证
                        }else{
                            Toast.makeText(phoneRegister.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(phoneRegister.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(phoneRegister.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean checkTel(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            validateNum_btn.setClickable(false);
            validateNum_btn.setText(l/1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            validateNum_btn.setClickable(true);
            validateNum_btn.setText("获取验证码");
        }
    }
}

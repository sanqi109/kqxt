package com.example.kq.activity;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import com.example.kq.R;
import com.example.kq.util.MD5;
import com.example.kq.util.StringUtils;
import com.example.kq.util.UserDao;

public class RegisterActivity extends BaseActivity {

    private static  final  String TAG="RegisterActivity";
    private EditText etAccount;
    private EditText etPwd;
    private Button btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        View btnphoneregister = findViewById(R.id.link_phone_register);
        btnphoneregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(phoneRegister.class);
            }
        });
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();//trim去掉前后空格
                String pwd = etPwd.getText().toString().trim();
                MD5 jm = new MD5();
                String newAccount = jm.md5(account);
                String newPwd = jm.md5(pwd);
                register(newAccount, newPwd);
            }
        });
    }
    private  void register(final String account, final String pwd){
        if(StringUtils.isEmpty(account)){
            showToast("请输入账号");
            return;
        }
        if(StringUtils.isEmpty(pwd)){
            showToast("请输入密码");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao ud = new UserDao();
                boolean result = ud.register(account,pwd);
                if(!result){
                    Looper.prepare();
                    Toast toast = Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT);
                    toast.show();
                    Looper.loop();
                }
                Log.i(TAG,"fun" + result);
            }
        }).start();
    }
}

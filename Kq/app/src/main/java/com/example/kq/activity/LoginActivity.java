package com.example.kq.activity;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import com.example.kq.R;
import com.example.kq.util.MD5;
import com.example.kq.util.StringUtils;
import com.example.kq.util.UserDao;

public class LoginActivity extends BaseActivity {
    private static  final  String TAG="LoginActivity";
    private EditText etAccount;
    private EditText etPwd;
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        View btnphoneregister = findViewById(R.id.link_phone_login);
        btnphoneregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(phoneLoginActivity.class);
            }
        });
        View btnretrievepassword = findViewById(R.id.link_retrieve_password);
        btnretrievepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(RetrievePassword.class);
            }
        });
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                MD5 jm = new MD5();
                login(jm.md5(account), jm.md5(pwd));
            }
        });
    }

    private  void login(final String account, final String pwd){
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
                boolean result = ud.login(account,pwd);
                if(!result){
                    Looper.prepare();
                    Toast toast = Toast.makeText(LoginActivity.this,"用户不存在或密码错误",Toast.LENGTH_SHORT);
                    toast.show();
                    Looper.loop();
                }else{
                    Looper.prepare();
                    Toast toast = Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT);
                    toast.show();
                    Looper.loop();
                }
            }
        }).start();
    }
}
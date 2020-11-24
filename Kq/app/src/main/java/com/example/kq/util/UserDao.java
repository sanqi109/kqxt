package com.example.kq.util;

import android.util.Log;
import android.widget.Toast;
import com.example.kq.activity.RegisterActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static android.content.ContentValues.TAG;

public class UserDao {
    JdbcUtil jdbcUtil = JdbcUtil.getInstance();
    Connection conn = jdbcUtil.getConnection("kqxt","kqxt","111111");

    public boolean register(String name,String password){
        if(conn == null){
            Log.i(TAG,"register:com is null");
            return true;
        }else {
            String sql = "insert into user(name,password) values(?,?)";
            try {
                PreparedStatement pre = conn.prepareStatement(sql);
                pre.setString(1,name);
                pre.setString(2,password);
                return pre.execute();
            }catch (SQLException e){
                return false;
            }finally {
                try {
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean login(String name,String password){
        if(conn == null){
            return false;
        }else {
            String sql = "select * from user where name=? and password=?";
            try{
                PreparedStatement pres = conn.prepareStatement(sql);
                pres.setString(1,name);
                pres.setString(2,password);
                ResultSet res = pres.executeQuery();
                boolean t = res.next();
                return  t;
            }catch (SQLException e){
                return false;
            }
        }
    }
}

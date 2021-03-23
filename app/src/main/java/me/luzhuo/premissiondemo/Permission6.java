package me.luzhuo.premissiondemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Description: Android6.0 运行时权限
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/9/11 0:32
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class Permission6 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission6);
    }

    public void onClick(View view) {
        // 执行原生的Android6.0运行时权限

        // 是否获取了相关权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            run();
        } else {
            // 未申请相关权限, 去申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x01);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 处理权限申请结果
        switch (requestCode){
            case 0x01:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户授予了对应的权限
                    run();
                } else {
                    // 用于拒绝了对应的权限
                    Toast.makeText(this, "您拒绝了相关的权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void run(){
        Toast.makeText(this, "成功执行了程序", Toast.LENGTH_SHORT).show();
    }
}

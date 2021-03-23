package me.luzhuo.premissiondemo;

import androidx.appcompat.app.AppCompatActivity;
import me.luzhuo.lib_permission.Permission;
import me.luzhuo.lib_permission.PermissionCallback;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // startActivity(new Intent(this, Permission6.class));
    }

    public void onClick(View view) {
        Permission.request(this, new PermissionCallback() {
            @Override
            public void onRequst(boolean isAllGranted, List<String> denieds) {
                if(isAllGranted) Toast.makeText(MainActivity.this, "权限通过", Toast.LENGTH_SHORT).show();
                else Toast.makeText(MainActivity.this, "权限拒绝", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "所有权限都通过", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenieds(List<String> denieds) {
                Toast.makeText(MainActivity.this, "有权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }
}
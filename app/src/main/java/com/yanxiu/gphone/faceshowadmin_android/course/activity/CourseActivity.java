package com.yanxiu.gphone.faceshowadmin_android.course.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.fragment.CourseFragment;

/**
 * Created by 朱晓龙 on 2018/4/26 14:08.
 * 用于首页 课程tab 的跳转显示
 * 内容装填为原首页的 courseFragment
 */

public class CourseActivity extends FaceShowBaseActivity {


    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, CourseActivity.class);
        activity.startActivity(intent);
    }

    public static void invoke(Activity activity, int result_code) {
        activity.setResult(result_code);
        activity.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new CourseFragment())
                .commit();
    }
}

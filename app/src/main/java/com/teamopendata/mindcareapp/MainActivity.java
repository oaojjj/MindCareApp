package com.teamopendata.mindcareapp;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amitshekhar.DebugDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        DebugDB.getAddressLog();

//        SharedPreferences pref = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
//        boolean checkFisrt = pref.getBoolean("checkFirst",false);
//        if(checkFisrt==false)//첫 시작시 튜토리얼 액티비티 실행
//        {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putBoolean("checkFirst",true);
//            editor.commit();
//
//            Intent intent  = new Intent(MainActivity.this, SelectActivity.class);
//            startActivity(intent);
//
//            finish();
//        }else
//        {
//
//        }
        //!-- fragment 네이게이션 만드는 코드
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_records, R.id.navigation_graph)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Log.d(TAG, "itemSelected: " + destination);
            displayHomeAsUpEnabled(destination.getId() != R.id.navigation_home);
        });

    }


    /**
     * 현재 포커싱된 EditText range외의 view를 클릭하면 포커스 해제
     * {@link com.teamopendata.mindcareapp.ui.records.adapter.TaskAdapter}
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}

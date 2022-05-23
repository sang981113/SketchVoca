package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    private static final String TAG = "Splash";

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep((1000));
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(MainActivity.class);
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}

package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.jbnu.se.mobile.sketchvoca.MusicService;
import kr.ac.jbnu.se.mobile.sketchvoca.PreferenceManager;
import kr.ac.jbnu.se.mobile.sketchvoca.R;


public class MainActivity extends BasicActivity {
    final static private int MUSIC_ON_STATE = 1;
    final static private int MUSIC_OFF_STATE = 0;
    private static final String TAG = "Main";
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHome fragHome;
    private FragMyWordBook fragMyWordBook;
    private FragPost fragPost;
    private FragMore fragMore;
    private FragPostUp frag5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(PreferenceManager.getInt(this, "music") == MUSIC_OFF_STATE) {
            stopService(new Intent(getApplicationContext(), MusicService.class));// 배경음악 종료
        } else {
            startService(new Intent(getApplicationContext(), MusicService.class));// 배경음악 재생
        }


        init();
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mitem) {
                switch (mitem.getItemId()) {
                    case R.id._home:
                        setFrag(0);
                        break;
                    case R.id._vocabulary:
                        setFrag(1);
                        break;
                    case R.id._board:
                        setFrag(2);
                        break;
                    case R.id._more:
                        setFrag(3);
                        break;
                    case R.id._post:
                        setFrag(4);

                }
                return true;
            }
        });
        fragHome = new FragHome();
        fragMyWordBook = new FragMyWordBook();
        fragPost = new FragPost();
        fragMore = new FragMore();
        frag5 = new FragPostUp();
        setFrag(0); // 첫화면 지정
    }


    private  void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, fragHome);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, fragMyWordBook);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, fragPost);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, fragMore);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame, frag5);
                ft.commit();
                break;
        }
    }



    private void init() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // 로그인 확인
        if (firebaseUser == null) {//로그인 하지 않았으면
            startActivity(LoginActivity.class);
        } else {//로그인 했으면
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                startActivity(MemberInfoActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }



    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
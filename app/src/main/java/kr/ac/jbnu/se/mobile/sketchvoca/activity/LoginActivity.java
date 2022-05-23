package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kr.ac.jbnu.se.mobile.sketchvoca.R;

public class LoginActivity extends BasicActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoSignUpButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.loginButton:
                    login(((EditText)findViewById(R.id.emailEditText)).getText().toString(),
                            ((EditText)findViewById(R.id.passwordEditText)).getText().toString());
                    break;
                case R.id.gotoSignUpButton:
                    startActivity(SignUpActivity.class);
                    break;
            }
        }
    };

    private void login(String email, String password) {
        if(email.length() > 0 && password.length() > 0) {
            final ConstraintLayout loaderLayout = findViewById(R.id.loaderLayout);
            loaderLayout.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loaderLayout.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                makeToast("로그인에 성공하였습니다.");
                                startActivity(MainActivity.class);
                            } else {
                                // If sign in fails, display a message to the user.
                                if(task.getException() != null){
                                    makeToast(task.getException().toString().substring(65));
                                }
                            }
                        }
                    });
        } else{
            makeToast("이메일 또는 비밀번호를 입력하세요.");
        }
    }

    private void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

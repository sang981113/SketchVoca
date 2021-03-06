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

public class SignUpActivity extends BasicActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signUpButton:
                    createAccount(((EditText)findViewById(R.id.emailEditText)).getText().toString(),
                            ((EditText)findViewById(R.id.passwordEditText)).getText().toString());
                    break;
                case R.id.gotoLoginButton:
                    startActivity(LoginActivity.class);
                    break;
            }
        }
    };

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();

        if(email.length() > 10 && password.length() > 6 && passwordCheck.length() > 6) {
            if (password.equals(passwordCheck)) {
                final ConstraintLayout loaderLayout = findViewById(R.id.loaderLayout);
                loaderLayout.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loaderLayout.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    makeToast("??????????????? ?????????????????????.");
                                    startActivity(LoginActivity.class);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if(task.getException() != null){
                                        makeToast(task.getException().toString().substring(60));
                                    }

                                }
                            }
                        });
                // [END create_user_with_email]
            } else {
                makeToast("??????????????? ???????????? ????????????.");
            }
        } else if(email.length() <= 10){
            makeToast("???????????? ?????? ????????????.");
        } else if(password.length() <= 6){
            makeToast("??????????????? ?????? ????????????.");
        } else{
            makeToast("????????? ???????????????.");
        }
    }

    private void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}

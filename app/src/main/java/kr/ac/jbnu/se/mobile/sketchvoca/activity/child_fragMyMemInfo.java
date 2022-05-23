package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import kr.ac.jbnu.se.mobile.sketchvoca.MemberInfo;
import kr.ac.jbnu.se.mobile.sketchvoca.R;


public class child_fragMyMemInfo extends Fragment {
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView nameEditText;
    private TextView phoneNumberEditText;
    private TextView birthdayEditText;
    private ImageView profileImageView;
    private MemberInfo memberInfo = null;
    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_child_fragmymeminfo, container, false);
        nameEditText = view.findViewById(R.id.fragNameEditText);
        phoneNumberEditText = view.findViewById(R.id.fragPhoneNumberEditText);
        birthdayEditText = view.findViewById(R.id.fragBirthdayEditText);
        profileImageView = view.findViewById(R.id.fragProfileImageView);
        Button logoutButton = (Button) view.findViewById(R.id.LogoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(LoginActivity.class);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
        return view;
    }

    private void init() {
        firebaseFirestore.collection("users").document(firebaseUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    nameEditText.setText(task.getResult().get("name").toString());
                    phoneNumberEditText.setText(task.getResult().get("phoneNumber").toString());
                    birthdayEditText.setText(task.getResult().get("birthday").toString());
                   if(task.getResult().get("photoUri").toString()!=null) {
                        Glide.with(getActivity()).load(task.getResult().get("photoUri").toString()).centerCrop().override(500).into(profileImageView);
                    }
                } else{
                    Log.e("cannot", "download");
                }
            }
                });
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }
}
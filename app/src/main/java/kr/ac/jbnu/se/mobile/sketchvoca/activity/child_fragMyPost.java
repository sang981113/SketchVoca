package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kr.ac.jbnu.se.mobile.sketchvoca.R;
import kr.ac.jbnu.se.mobile.sketchvoca.WordInfo;
import kr.ac.jbnu.se.mobile.sketchvoca.adapter.MyWordBookAdapter;

import static android.content.ContentValues.TAG;


public class child_fragMyPost extends Fragment {
    private ArrayList<WordInfo> EngWords = new ArrayList<>();
    private View view;
    private static kr.ac.jbnu.se.mobile.sketchvoca.adapter.MyWordBookAdapter MyWordBookAdapter;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_child_fragmypost, container, false);
        EngWords = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
        return  view;
    }

    private void init() {
        firebaseFirestore.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<WordInfo> wordInfoArrayListList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.getData().get("publisher").toString().equals(firebaseUser.getUid())){
                                    Log.e("publisher", "OK");
                                    wordInfoArrayListList.add(new WordInfo(
                                            document.getData().get("engWord").toString(),
                                            document.getData().get("wordMeaning").toString(),
                                            document.getData().get("wordImagePath").toString(),
                                            document.getData().get("publisher").toString()));
                                } else{
                                    Log.e("Log", "firebaseUser Uid err");
                                }
                            }
                            RecyclerView recyclerView = view.findViewById(R.id.myPostRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(child_fragMyPost.this.getContext()));

                            RecyclerView.Adapter mAdapter = new MyWordBookAdapter(child_fragMyPost.this.getActivity(), wordInfoArrayListList);
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

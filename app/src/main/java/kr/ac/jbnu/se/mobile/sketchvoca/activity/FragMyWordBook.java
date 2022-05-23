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

public class FragMyWordBook extends Fragment {

    private static final String TAG = "MyWordBookActivity";
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private View rootview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.frag_mywordbook, container, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
        return  rootview;
    }


    private void init() {
        firebaseFirestore.collection(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<WordInfo> wordInfoArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                wordInfoArrayList.add(new WordInfo(
                                        document.getData().get("engWord").toString(),
                                        document.getData().get("wordMeaning").toString(),
                                        document.getData().get("wordImagePath").toString(),
                                        document.getData().get("publisher").toString()));
                            }
                            RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(FragMyWordBook.this.getContext()));

                            RecyclerView.Adapter mAdapter = new MyWordBookAdapter(FragMyWordBook.this.getActivity(), wordInfoArrayList);
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

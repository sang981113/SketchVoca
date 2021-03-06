package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kr.ac.jbnu.se.mobile.sketchvoca.R;
import kr.ac.jbnu.se.mobile.sketchvoca.WordInfo;
import kr.ac.jbnu.se.mobile.sketchvoca.adapter.WordBookPostAdapter;

import static android.content.ContentValues.TAG;

public class FragPostUp extends Fragment {
    private View view;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private WordInfo selectedWordInfo = null;
    private Button postWordButton;
    private int recentPos;
    private View j;
    private WordInfo ColorWordInfo = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_postup, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        postWordButton = view.findViewById(R.id.postWordButton);
        postWordButton.setOnClickListener(onClickListener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
        return  view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.postWordButton:
                    if(selectedWordInfo != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                wordInfoUploader(selectedWordInfo);
                            }
                        }).start();
                    } else{
                        makeToast("????????? ??????????????????");
                    }
            }
        }
    };

    private void init() {
        firebaseFirestore.collection(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<WordInfo> wordInfoArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                wordInfoArrayList.add(new WordInfo(
                                        document.getData().get("engWord").toString(),
                                        document.getData().get("wordMeaning").toString(),
                                        document.getData().get("wordImagePath").toString(),
//                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getData().get("publisher").toString()));
                            }
                            RecyclerView recyclerView = view.findViewById(R.id.postRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(FragPostUp.this.getContext()));

                            WordBookPostAdapter mAdapter = new WordBookPostAdapter(FragPostUp.this.getActivity(), wordInfoArrayList);
                            recyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new WordBookPostAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    selectedWordInfo = wordInfoArrayList.get(position);
                                    //colorwordinfo??? null ??? ?????? , color= selected ?????????, color!=selected ?????????
                                    // view j ??? v, wordinfo color??? select??? ???????????? ?????? ????????? ???????????? ????????? ??????
                                    if(ColorWordInfo == null) {
                                        // ?????? ????????? ???
                                        v.setBackgroundColor(Color.GRAY);
                                        j = v;
                                        ColorWordInfo = selectedWordInfo;
                                    }
                                    else if (ColorWordInfo == selectedWordInfo) {
                                        //?????? ??? ??? ????????? ???
                                        v.setBackgroundColor(Color.TRANSPARENT);
                                        ColorWordInfo = null;
                                    }
                                    else if(ColorWordInfo != selectedWordInfo) {
                                        //?????? ??? ????????? ???
                                        j.setBackgroundColor(Color.TRANSPARENT);
                                        v.setBackgroundColor(Color.GRAY);
                                        j = v;
                                        ColorWordInfo = selectedWordInfo;
                                    }
                                    if (postWordButton.getVisibility() == View.GONE)  {
                                        postWordButton.setVisibility(View.VISIBLE);
                                    } else if (postWordButton.getVisibility() == View.VISIBLE && recentPos == position) {
                                        postWordButton.setVisibility(View.GONE);
                                    }
                                    recentPos = position;
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void wordInfoUploader(WordInfo wordInfo){//????????? ????????? ?????????
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("posts").add(wordInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        makeToast("????????? ?????????????????????.");
                        //finish();//?????? ????????????.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast("?????? ?????? ??????");
                        Log.e(TAG, "Error");
                    }
                });
    }

    private void makeToast(String msg){
        Toast.makeText(FragPostUp.this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}


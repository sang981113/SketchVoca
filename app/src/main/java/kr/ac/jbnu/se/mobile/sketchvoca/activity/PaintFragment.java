package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import kr.ac.jbnu.se.mobile.sketchvoca.R;
import kr.ac.jbnu.se.mobile.sketchvoca.WordInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaintFragment extends Fragment {

    ConstraintLayout loaderLayout;
    String wordPath = null;
    FirebaseUser user;
    private View view;

    // 그림을 그릴 CustomView
    private kr.ac.jbnu.se.mobile.sketchvoca.DrawingView drawingView;

    // 색상 선택 버튼
    private ImageButton[] colorImageButtons;

    // 초기화 버튼, 저장 버튼
    private Button resetButton, saveButton;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paint, container, false);
        //getChildFragmentManager().beginTransaction().add(R.id.child_fragment, new child_fragment1()).commit();

        drawingView = (kr.ac.jbnu.se.mobile.sketchvoca.DrawingView) view.findViewById(R.id.drawingView);

        colorImageButtons = new ImageButton[3];
        colorImageButtons[0] = (ImageButton) view.findViewById(R.id.blackColorBtn);
        colorImageButtons[1] = (ImageButton) view.findViewById(R.id.redColorBtn);
        colorImageButtons[2] = (ImageButton) view.findViewById(R.id.blueColorBtn);
        for (ImageButton colorImageButton : colorImageButtons) {
            colorImageButton.setOnClickListener(this::onClick);
        }

        resetButton = (Button) view.findViewById(R.id.resetBtn);
        resetButton.setOnClickListener(this::onClick);
        saveButton = (Button) view.findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(this::onClick);

        return view;


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blackColorBtn:
                drawingView.setColor(Color.BLACK);
                break;
            case R.id.redColorBtn:
                drawingView.setColor(Color.RED);
                break;
            case R.id.blueColorBtn:
                drawingView.setColor(Color.BLUE);
                break;
            case R.id.resetBtn:
                drawingView.reset();
                break;
            case R.id.saveBtn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wordUpdate(drawingView.save(PaintFragment.this.getContext(), getActivity().getExternalFilesDir(null)));
                        startActivity(MainActivity.class);

                    }
                }).start();
                break;
        }
    }

    private void wordUpdate(String wordPath){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        final StorageReference wordImagesRef = storageRef.child("wordImages/"+ user.getUid() +"/wordImage.jpg");
        final String engWord = ((EditText)view.findViewById(R.id.engWordSketchTextView)).getText().toString();
        final String wordMeaning = ((EditText)view.findViewById(R.id.wordMeaningSketchTextView)).getText().toString();

        if(engWord.length() > 0 && wordMeaning.length() >0){
            try{
                InputStream stream = new FileInputStream(new File(wordPath));
                UploadTask uploadTask = wordImagesRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return wordImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            WordInfo wordInfo = new WordInfo(engWord, wordMeaning, downloadUri.toString(), user.getUid());
                            wordUploader(wordInfo);
                        } else {
                            makeToast("잘못된 형식의 단어입니다.");
                        }
                    }
                });
            }catch(FileNotFoundException e){
                Log.e("Log", "fileStreamErr");
            }
        } else{
            makeToast("단어를 작성해주세요.");
        }
    }

    private void wordUploader(WordInfo wordInfo){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(user.getUid()).document(wordInfo.getEngWord()).set(wordInfo)//내단어장 document에 기본키 필요
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void void1) {

                        makeToast("단어를 게시하였습니다.");
                       /* FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().remove(PaintFragment.this).commit();
                        fragmentManager.popBackStack(); */
                        //finish();//창을 종료한다.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        makeToast("단어 게시 실패");
                    }
                });
    }

//    private void wordUploader(WordInfo wordInfo){
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseFirestore.collection("posts").add(wordInfo)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        loaderLayout.setVisibility(View.GONE);
//                        makeToast("단어를 게시하였습니다.");
//                        finish();//창을 종료한다.
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        loaderLayout.setVisibility(View.GONE);
//                        makeToast("단어 게시 실패");
//                    }
//                });
//    }

    private void makeToast(String msg){
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }
}
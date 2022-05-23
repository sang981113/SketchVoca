package kr.ac.jbnu.se.mobile.sketchvoca.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import kr.ac.jbnu.se.mobile.sketchvoca.MemberInfo;
import kr.ac.jbnu.se.mobile.sketchvoca.R;

public class MemberInfoActivity extends BasicActivity {
    private Boolean isPermission = true;
    private  final int GET_GALLERY_IMAGE = 1;
    private static final String TAG = "MemberInfo";
    ConstraintLayout loaderLayout;
    private ImageView profileImageView;
    private FirebaseUser user;
    String profilePath = null;
    Uri profileUri = null;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberinfo);
        loaderLayout = findViewById(R.id.loaderLayout);
        profileImageView = findViewById(R.id.profileImageView);

        findViewById(R.id.profileImageView).setOnClickListener((onClickListener));
        findViewById(R.id.saveButton).setOnClickListener(onClickListener);
        findViewById(R.id.cameraButton).setOnClickListener(onClickListener);
        findViewById(R.id.galleryButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0 ://카메라로 촬영한 profileImage
                if(resultCode == Activity.RESULT_OK){
                    profilePath = data.getStringExtra("profilePath");
                    profileUri = Uri.fromFile(new File(profilePath.toString()));
                    Glide.with(this).load(profileUri).centerCrop().override(500).into(profileImageView);
                }
                break;
            case 1: //갤러리를 통해 선택한 profileImage
                if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

                    Uri selectedImageUri = data.getData();
                    File file = new File(this.getExternalFilesDir(null), "galleryImage.jpg");
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        Glide.with(this).load(selectedImageUri).centerCrop().override(500).into(profileImageView);
                        if(file.createNewFile()){
                            OutputStream outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.close();
                        }
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT);
                    }
                    profilePath = file.toString();
                }
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.saveButton:
                    profileUpdate();
                    break;
                case R.id.profileImageView:
                    CardView cardView = findViewById(R.id.pictureControlCardView);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.cameraButton:
                    if (ContextCompat.checkSelfPermission(MemberInfoActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        startActivity(CameraActivity.class);
                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(MemberInfoActivity.this,
                            Manifest.permission.CAMERA)){
                        ActivityCompat.requestPermissions(MemberInfoActivity.this,
                                new String[]{Manifest.permission.CAMERA},0);
                    } else{
                        makeToast("권한이 없습니다.");
                        ActivityCompat.requestPermissions(MemberInfoActivity.this,
                                new String[]{Manifest.permission.CAMERA},0);
                    }
                    break;
                case R.id.galleryButton:
                    if (ContextCompat.checkSelfPermission(MemberInfoActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/external/images/media"));
                        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, GET_GALLERY_IMAGE);
                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(MemberInfoActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(MemberInfoActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                    } else{
                        makeToast("권한이 없습니다.");
                        ActivityCompat.requestPermissions(MemberInfoActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                    }
                    break;
            }
        }
    };


    private void profileUpdate() {
        final String name = ((EditText)findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText)findViewById(R.id.phoneNumberText)).getText().toString();
        final String birthday = ((EditText)findViewById(R.id.birthdayEditText)).getText().toString();

        if(name.length() > 0 && phoneNumber.length() == 11 && birthday.length() == 6) {
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference profileImagesRef = storageRef.child("users/"+ user.getUid() +"/profile.jpg");
            //firebaseStorage 저장

            if(profilePath == null){//사진이 따로 없는 경우 회원정보 등록
                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthday);
                profileUploader(memberInfo);
            } else{//사진이 있는 경우 회원정보 등록
                try{
                    InputStream stream = new FileInputStream(new File(profilePath));

                    UploadTask uploadTask = profileImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return profileImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthday, downloadUri.toString());
                                profileUploader(memberInfo);
                            } else {
                                makeToast("회원정보를 보내는 데 실패하였습니다.");
                            }
                        }
                    });
                }catch(FileNotFoundException e){
                    Log.e("Log", "fileStreamErr");
                }
            }
        } else{
            makeToast("회원정보를 알맞게 입력하세요.");
        }
    }

    private void profileUploader(MemberInfo memberInfo){//Firebase database에 회원정보 등록 확인
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void void1) {
                        loaderLayout.setVisibility(View.GONE);
                        makeToast("회원정보를 등록하였습니다.");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loaderLayout.setVisibility(View.GONE);
                        makeToast("회원정보 등록 실패");
                        Log.w(TAG, "Error", e);
                    }
                });
    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}

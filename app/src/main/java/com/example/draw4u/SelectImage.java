package com.example.draw4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectImage extends AppCompatActivity {
    Button mDown;
    ImageView mImgTrans;
    Bitmap mBitmap;
    Button phoneImage;
    Button selGan;
    public String fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_select_image);

        Intent intent = getIntent();
        fname = intent.getExtras().getString("fname");

        mDown = (Button) findViewById(R.id.btn_down);
        mImgTrans = (ImageView) findViewById(R.id.imgTranslate);
        phoneImage = (Button) findViewById(R.id.btn_imageFromPhone);
        selGan = (Button) findViewById(R.id.btn_sel);

        //String image_url = "https://firebasestorage.googleapis.com/v0/b/drawforyou-51628.appspot.com/o/images%2F1gtanzcT5kcGkdPQVkEmRMbSG4x120201115_18011gtanzcT5kcGkdPQVkEmRMbSG4x1.png?alt=media";
        String image_url = "https://lh3.googleusercontent.com/proxy/TygmmySA-qQg1PGz48QBCo2bFCI2U6Rn2sz4FQ5vnG1_jZ9EmZddhQ1ghmeoLffXMfj4K-KmpuoHDyR3ictvrrqSqLXe1Q";

        mDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                new LoadImage()
                        .execute(image_url);

            }
        });
        selGan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //업로드
                uploadFile();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        phoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PhotoFromPhone.class);
                intent.putExtra("fname",fname);
                startActivity(intent);
                finish();

            }
        });

    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectImage.this);
            pDialog.setMessage("이미지 로딩중입니다...");
            pDialog.show();
        }

        protected Bitmap doInBackground(String... args) {
            try {
                mBitmap = BitmapFactory
                        .decodeStream((InputStream) new URL(args[0])
                                .getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if (image != null) {
                mImgTrans.setImageBitmap(image);
                pDialog.dismiss();

            } else {
                pDialog.dismiss();
                Toast.makeText(SelectImage.this, "이미지가 존재하지 않습니다.",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (mBitmap != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + currentUser.getUid() + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://drawforyou-51628.appspot.com/")
                    .child("images/" + currentUser.getUid()+ filename);

            String fileURL = "https://firebasestorage.googleapis.com/v0/b/drawforyou-51628.appspot.com/o/images%2F"
                    + currentUser.getUid()+ filename + "?alt=media";//firebase storage에 올린 주소

            storageRef.putFile(this.getImageUri(this,mBitmap))
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기

                            db.collection(currentUser.getUid()).document(fname)
                                    .update("imageURL",fileURL);

                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
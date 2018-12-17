package com.example.qpdjg.a2018_seoulapp_owner.Activitys;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qpdjg.a2018_seoulapp_owner.R;
import com.example.qpdjg.a2018_seoulapp_owner.Util_Data.Gallery_Data;
import com.example.qpdjg.a2018_seoulapp_owner.WebView_for_search_location;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class NewGallery extends AppCompatActivity {

    Spinner spinner;
    String[] Gallery_locations_list;
    EditText Gallery_name;
    EditText Gallery_explain;
    EditText Owner_explain;
    EditText Owner_insta;
    EditText Gallery_location;
    EditText Gallery_time;
    EditText Gallery_fee;
    Button submit_button;
    String[] G_location_from_list;
    String G_name;
    String G_explain;
    String O_explain;
    String O_insta;
    String G_location;
    String G_time;
    String G_fee;
    ImageButton serach_location_button;
    EditText Gallery_location_detail;

    private static final int GALLERY_CODE1 = 10;
    private static final int GALLERY_CODE2 = 11;
    private static final int GALLERY_CODE3 = 12;
    private static final int GALLERY_CODE4 = 13;
    private static final int GALLERY_CODE5 = 14;
    private static final int GALLERY_CODE6 = 15;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;

    private String imagePath1="";
    private String imagePath2="";
    private String imagePath3="";
    private String imagePath4="";
    private String imagePath5="";
    private String imagePath6="";

    private Uri downloadUri;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    String save_email;
    String email;
    private String G_location_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gallery);

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.parseColor("#321c54"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        int index = email.indexOf("@");
        save_email = email.substring(0,index);

        storage = FirebaseStorage.getInstance();
        Gallery_name = (EditText)findViewById(R.id.Gallery_name);
        Gallery_explain= (EditText)findViewById(R.id.Gallery_explain);
        Owner_explain= (EditText)findViewById(R.id.Owner_explain);
        Owner_insta= (EditText)findViewById(R.id.Owner_insta);
        Gallery_location= (EditText)findViewById(R.id.Gallery_location);
        Gallery_time= (EditText)findViewById(R.id.Gallery_time);
        Gallery_fee= (EditText)findViewById(R.id.Gallery_fee);
        submit_button = (Button)findViewById(R.id.submit_button);
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);
        imageView5 = (ImageView)findViewById(R.id.imageView5);
        imageView6 = (ImageView)findViewById(R.id.imageView6);
        serach_location_button = (ImageButton)findViewById(R.id.search_location_button);
        Gallery_location_detail = (EditText)findViewById(R.id.Gallery_location_detail) ;


        G_location_from_list = new String[1];

        Gallery_locations_list = new String[]{"강서구", "마포구", "영등포구", "양천구", "구로구", "금천구", "관악구", "동작구", "용산구", "서초구", "강남구", "송파구", "강동구", "광진구", "성동구", "중구", "용산구", "서대문구", "은평구", "종로구", "성북수", "동대문구", "중랑구", "강북구", "노원구", "도봉구"};

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.locations,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                G_location_from_list[0] = (String) Gallery_locations_list[position];
                //Toast.makeText(getApplicationContext(), Gallery_locations_list[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE1);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE2);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE3);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE4);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE5);
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE6);
            }
        });

        serach_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewGallery.this, WebView_for_search_location.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });

    }

    public void submit_new_Gallery(View view) {
        G_name = Gallery_name.getText().toString();
        G_explain = Gallery_explain.getText().toString();
        O_explain = Owner_explain.getText().toString();
        O_insta = Owner_insta.getText().toString();
        G_location = Gallery_location.getText().toString();
        G_location_detail = Gallery_location_detail.getText().toString();
        G_time = Gallery_time.getText().toString();
        G_fee = Gallery_fee.getText().toString();

        if(G_name.equals("")||G_explain.equals("")||O_explain.equals("")||O_insta.equals("")||G_location.equals("")||G_time.equals("")||G_fee.equals("")){

            Toast.makeText(getApplicationContext(),"빠진 정보를 입력하세요.",Toast.LENGTH_LONG).show();
            return;
        }

        mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]);
        Gallery_Data gallery_data = new Gallery_Data();
        gallery_data.Gallery_name = G_name;
        gallery_data.Gallery_explain = G_explain;
        gallery_data.Owner_explain = O_explain;
        gallery_data.Owner_insta = O_insta;
        gallery_data.Gallery_location_from_list=G_location_from_list[0];
        gallery_data.Gallery_location = G_location +" "+ G_location_detail;
        gallery_data.Gallery_time = G_time;
        gallery_data.Gallery_fee = G_fee;

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        gallery_data.Gallery_owner_email = email;

        mReference.child(G_name).setValue(gallery_data);

        Gallery_name.setText("");
        Gallery_explain.setText("");
        Owner_explain.setText("");
        Owner_insta.setText("");
        Gallery_location.setText("");
        Gallery_time.setText("");
        Gallery_fee.setText("");
        Gallery_location_detail.setText("");

        mReference = mDatabase.getReference("OwnerProfile/"+save_email+"/MyGallerys/"+G_name);
        mReference.child("My_Gallery_name").setValue(G_name);
        mReference.child("My_Gallery_location").setValue(G_location_from_list[0]);


        if(!imagePath1.equals("")){
        upload_img(imagePath1);
        }
        if(!imagePath2.equals("")){
        upload_img(imagePath2);
        }
        if(!imagePath3.equals("")){
            upload_img(imagePath3);
        }
        if(!imagePath4.equals("")){
            upload_img(imagePath4);
        }
        if(!imagePath5.equals("")){
            upload_img(imagePath5);
        }
        if(!imagePath6.equals("")){
            upload_img(imagePath6);
        }
        Toast.makeText(getApplicationContext(), "갤러리 등록중입니다... 기다려주세요...", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data != null){

            if(requestCode == SEARCH_ADDRESS_ACTIVITY){

                super.onActivityResult(requestCode, resultCode, data);

                if(resultCode == RESULT_OK){

                    String Location_from_web = data.getExtras().getString("data");
                    if (data != null)
                        Gallery_location.setText(Location_from_web);
                }
            }

            if(requestCode == GALLERY_CODE1){
            imagePath1 = getPath(data.getData());
            File f = new File(imagePath1);
            imageView1.setImageURI(Uri.fromFile(f));
            }
            if(requestCode == GALLERY_CODE2){
                imagePath2 = getPath(data.getData());
                File f = new File(imagePath2);
                imageView2.setImageURI(Uri.fromFile(f));
            }
            if(requestCode == GALLERY_CODE3){
                imagePath3 = getPath(data.getData());
                File f = new File(imagePath3);
                imageView3.setImageURI(Uri.fromFile(f));
            }
            if(requestCode == GALLERY_CODE4){
                imagePath4 = getPath(data.getData());
                File f = new File(imagePath4);
                imageView4.setImageURI(Uri.fromFile(f));
            }
            if(requestCode == GALLERY_CODE5){
                imagePath5 = getPath(data.getData());
                File f = new File(imagePath5);
                imageView5.setImageURI(Uri.fromFile(f));
            }
            if(requestCode == GALLERY_CODE6){
                imagePath6 = getPath(data.getData());
                File f = new File(imagePath6);
                imageView6.setImageURI(Uri.fromFile(f));
            }
        }
    }
    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    private void upload_img(final String uri){
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(uri));
        final StorageReference riversRef = storageRef.child(G_name+"/"+file.getLastPathSegment());
        final UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            if(uri.equals(imagePath1)){
                                downloadUri = task.getResult();
                                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+G_name+"/Gallery_imgs");
                                mReference.child("01").setValue(downloadUri.toString());

                                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+G_name);
                                mReference.child("Main_img").setValue(downloadUri.toString());

                                mReference = mDatabase.getReference("OwnerProfile/"+save_email+"/MyGallerys/"+G_name);
                                mReference.child("My_Gallery_img").setValue(downloadUri.toString());

                            }else if(uri.equals(imagePath2)){
                                downloadUri = task.getResult();
                                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+G_name+"/Gallery_imgs");
                                mReference.child("02").setValue(downloadUri.toString());
                            }else if(uri.equals(imagePath3)){
                                downloadUri = task.getResult();
                                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+G_name+"/Gallery_imgs");
                                mReference.child("03").setValue(downloadUri.toString());
                            }else if(uri.equals(imagePath4)){
                                downloadUri = task.getResult();
                                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+G_name+"/Gallery_imgs");
                                mReference.child("04").setValue(downloadUri.toString());
                            }else if(uri.equals(imagePath5)){
                                downloadUri = task.getResult();
                                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+G_name+"/Gallery_imgs");
                                mReference.child("05").setValue(downloadUri.toString());
                            }else if(uri.equals(imagePath6)){
                                downloadUri = task.getResult();
                                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+G_name+"/Gallery_imgs");
                                mReference.child("06").setValue(downloadUri.toString());
                            }
                            Toast.makeText(getApplicationContext(), "갤러리 등록이 완료 되었습니다. 사용자 어플에서 확인해보세요.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });

    }
}

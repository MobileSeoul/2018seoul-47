package com.example.qpdjg.a2018_seoulapp_owner.Activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qpdjg.a2018_seoulapp_owner.R;
import com.example.qpdjg.a2018_seoulapp_owner.Util_Data.Gallery_Data;
import com.example.qpdjg.a2018_seoulapp_owner.Util_Data.comment_data;
import com.example.qpdjg.a2018_seoulapp_owner.Util_Data.starstar;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Detail_Gallery extends AppCompatActivity {
    Spinner spinner;
    String[] Gallery_locations_list;
    TextView Gallery_name;
    EditText Gallery_explain;
    EditText Owner_explain;
    EditText Owner_insta;
    EditText Gallery_location;
    EditText Gallery_time;
    TextView welcome_text;
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
    Button Delete_Button;
    private static final int GALLERY_CODE1 = 10;
    private static final int GALLERY_CODE2 = 11;
    private static final int GALLERY_CODE3 = 12;
    private static final int GALLERY_CODE4 = 13;
    private static final int GALLERY_CODE5 = 14;
    private static final int GALLERY_CODE6 = 15;
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
    private DatabaseReference GReference = mDatabase.getReference();
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    String save_email;
    String email;
    String Detail_Loc;
    String Detail_Name;

    public int starCount = 0;
    private FirebaseAuth auth;
    public List<String> star_uid_list = new ArrayList<String>();
    public List<String> star_list = new ArrayList<String>();
    public List<String> commeter_list = new ArrayList<String>();
    public List<comment_data> Comments_list = new ArrayList<comment_data>();
    private List<String> img_lists = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__gallery);

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.parseColor("#321c54"));
        }

        Intent intent=getIntent();
        Detail_Loc = intent.getExtras().getString("Location");
        Detail_Name = intent.getExtras().getString("Name");

        Toast.makeText(this,"사진 로드까지 시간이 소요됩니다. 기다려주세요...",Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        int index = email.indexOf("@");
        save_email = email.substring(0,index);

        storage = FirebaseStorage.getInstance();
        //welcome_text = (TextView)findViewById(R.id.textView7);
        Gallery_name = (TextView)findViewById(R.id.Gallery_name);
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
        Delete_Button = (Button)findViewById(R.id.delete_button);

        submit_button.setText(Detail_Name +" 갤러리 정보 수정");
        Delete_Button.setText(Detail_Name + " 갤러리 정보 삭제");
        G_location_from_list = new String[1];

        Gallery_locations_list = new String[]{"강서구", "마포구", "영등포구", "양천구", "구로구", "금천구", "관악구", "동작구", "용산구", "서초구", "강남구", "송파구", "강동구", "광진구", "성동구", "중구", "용산구", "서대문구", "은평구", "종로구", "성북수", "동대문구", "중랑구", "강북구", "노원구", "도봉구"};

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.locations,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setEnabled(false);
        spinner.setAlpha(0.5f);

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

        DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Gal_Ref2 = rootRef2.child("Gallerys");
        DatabaseReference category_Ref2 = Gal_Ref2.child(Detail_Loc);

        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(Detail_Name.equals(ds.getKey().toString())){
                        starCount = Integer.parseInt(ds.child("starCount").getValue().toString());

                        for (DataSnapshot ds2 : ds.child("Comments").getChildren()) {
                            Comments_list.add(ds2.getValue(comment_data.class));
                            commeter_list.add(ds2.getKey());
                        }

                        for (DataSnapshot ds2 : ds.child("stars").getChildren()) {
                            star_uid_list.add(ds2.getKey());
                            star_list.add(Boolean.toString((Boolean) ds2.getValue()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        category_Ref2.addListenerForSingleValueEvent(valueEventListener2);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Gal_Ref = rootRef.child("Gallerys");
        DatabaseReference category_Ref = Gal_Ref.child(Detail_Loc);
        //DatabaseReference real_Gal_Ref = category_Ref.child(Detail_Name);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(Detail_Name.equals(ds.getKey().toString())){
                        Gallery_name.setText(ds.child("Gallery_name").getValue().toString());
                        Gallery_explain.setText(ds.child("Gallery_explain").getValue().toString());
                        Owner_explain.setText(ds.child("Owner_explain").getValue().toString());
                        Owner_insta.setText(ds.child("Owner_insta").getValue().toString());
                        Gallery_location.setText(ds.child("Gallery_location").getValue().toString());
                        Gallery_time.setText(ds.child("Gallery_time").getValue().toString());
                        Gallery_fee.setText(ds.child("Gallery_fee").getValue().toString());
                        //welcome_text.setText(Detail_Name + " 갤러리 관리 페이지");
                        if(ds.child("Gallery_imgs").hasChild("01")){
                            img_lists.add(ds.child("Gallery_imgs").child("01").getValue().toString());
                        }
                        if(ds.child("Gallery_imgs").hasChild("02")){
                            img_lists.add(ds.child("Gallery_imgs").child("02").getValue().toString());
                        }
                        if(ds.child("Gallery_imgs").hasChild("03")){
                            img_lists.add(ds.child("Gallery_imgs").child("03").getValue().toString());
                        }
                        if(ds.child("Gallery_imgs").hasChild("04")){
                            img_lists.add(ds.child("Gallery_imgs").child("04").getValue().toString());
                        }
                        if(ds.child("Gallery_imgs").hasChild("05")){
                            img_lists.add(ds.child("Gallery_imgs").child("05").getValue().toString());
                        }
                        if(ds.child("Gallery_imgs").hasChild("06")){
                            img_lists.add(ds.child("Gallery_imgs").child("06").getValue().toString());
                        }
                        push_imgs(img_lists);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        category_Ref.addListenerForSingleValueEvent(valueEventListener);

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


    }

    public void submit_new_Gallery(View view) {

        G_name = Gallery_name.getText().toString();
        G_explain = Gallery_explain.getText().toString();
        O_explain = Owner_explain.getText().toString();
        O_insta = Owner_insta.getText().toString();
        G_location = Gallery_location.getText().toString();
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
        gallery_data.Gallery_location = G_location;
        gallery_data.Gallery_time = G_time;
        gallery_data.Gallery_fee = G_fee;
        gallery_data.starCount = starCount;

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        gallery_data.Gallery_owner_email = email;

        mReference.child(G_name).setValue(gallery_data);

        mReference = mDatabase.getReference("OwnerProfile/"+save_email+"/MyGallerys/"+G_name);
        mReference.child("My_Gallery_name").setValue(G_name);
        mReference.child("My_Gallery_location").setValue(G_location_from_list[0]);

        for(int i = 0; i<img_lists.size();i++){
            mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+Detail_Name+"/Gallery_imgs");
            int j = i+1;
            mReference.child("0"+j).setValue(img_lists.get(i));
            if(i == 0 ){
                mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+Detail_Name);
                mReference.child("Main_img").setValue(img_lists.get(i));
            }
        }

        for(int i = 0; i<Comments_list.size();i++){
            mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+Detail_Name+"/Comments");
            mReference.child(commeter_list.get(i)).setValue(Comments_list.get(i));
        }

        for(int i = 0; i<star_list.size();i++){
            mReference = mDatabase.getReference("Gallerys/"+G_location_from_list[0]+"/"+Detail_Name+"/stars");
            auth = FirebaseAuth.getInstance();
            mReference.child(star_uid_list.get(i)).setValue(Boolean.valueOf(star_list.get(i)));
        }

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
        Toast.makeText(getApplicationContext(), "갤러리 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this,MyPageActivity.class);
        finish();
        startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data != null){
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

                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });

    }
    public void push_imgs(List img_list){
        try{
        if(img_list.get(0) != null){
            Glide.with(this).load(img_list.get(0)).into(imageView1);
        }
        if(img_list.get(1) != null){
            Glide.with(this).load(img_list.get(1)).into(imageView2);
        }
        if(img_list.get(2) != null){
            Glide.with(this).load(img_list.get(2)).into(imageView3);
        }
        if(img_list.get(3) != null){
            Glide.with(this).load(img_list.get(3)).into(imageView4);
        }
        if(img_list.get(4) != null){
            Glide.with(this).load(img_list.get(4)).into(imageView5);
        }
        if(img_list.get(5) != null){
            Glide.with(this).load(img_list.get(5)).into(imageView6);
        }
        }catch (Exception e){

        }
    }

    public void delete_Gallery(View view) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        int index = email.indexOf("@");
        String save_email = email.substring(0,index);
        mReference = mDatabase.getReference("OwnerProfile/"+save_email+"/MyGallerys/"+Detail_Name);
        GReference = mDatabase.getReference("Gallerys/"+Detail_Loc+"/"+Detail_Name);

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        alert_confirm.setMessage("정말 갤러리를 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        mReference.removeValue();
                        GReference.removeValue();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MyPageActivity.class));
                                    }
                                });
                    }
                }
        );
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert_confirm.show();
    }
}

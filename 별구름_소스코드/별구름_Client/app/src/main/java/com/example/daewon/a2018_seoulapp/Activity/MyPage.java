package com.example.daewon.a2018_seoulapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daewon.a2018_seoulapp.GalleryList;
import com.example.daewon.a2018_seoulapp.R;
import com.example.daewon.a2018_seoulapp.Util.Like_gal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MyPage extends BaseActivity {

    private ImageButton best5, find_gallery, mypage;
    int i = 3;
    private TextView textViewUserEmail;
    private ImageButton Logout;
    private ImageButton Delete;
    private List<Like_gal> like_gal_list = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        Logout = (ImageButton) findViewById(R.id.Logout);
        Delete = (ImageButton) findViewById(R.id.Delete);
        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);

        ListView listview ;
        final Like_list_Adapter adapter;

        Intent intent = getIntent();
        ArrayList<Like_gal> list = (ArrayList<Like_gal>) intent.getSerializableExtra("list");

        // Adapter 생성
        adapter = new Like_list_Adapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview_like);
        listview.setAdapter(adapter);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        textViewUserEmail.setText("반갑습니다. "+ user.getEmail()+"님\n오늘도 행복한 하루되세요!");
        best5 = findViewById(R.id.best5);
        find_gallery = findViewById(R.id.find_gallery);
        mypage = findViewById(R.id.mypage);

        firebaseAuth = FirebaseAuth.getInstance();


        for(int i = 0;i<list.size();i++){
            adapter.addItem(list.get(i).main_img_src, list.get(i).like_name,list.get(i).like_location);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        best5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i != 1) {
                    best5.setImageResource(R.drawable.best5_on);
                    find_gallery.setImageResource(R.drawable.gallery_off);
                    mypage.setImageResource(R.drawable.mypage_off);
                    i =1;
                    Intent intent = new Intent(MyPage.this, GalleryList.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

        find_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i != 2) {
                    best5.setImageResource(R.drawable.best5_off);
                    find_gallery.setImageResource(R.drawable.gallery_on);
                    mypage.setImageResource(R.drawable.mypage_off);
                    i = 2;
                    Intent intent = new Intent(MyPage.this, MapActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i != 3) {
                    best5.setImageResource(R.drawable.best5_off);
                    find_gallery.setImageResource(R.drawable.gallery_off);
                    mypage.setImageResource(R.drawable.mypage_on);
                    i = 3;

                }
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                Intent intent = new Intent(MyPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tokenID = FirebaseInstanceId.getInstance().getToken();
                mReference = mDatabase.getReference("UserProfile/"+tokenID);

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MyPage.this);
                alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                mReference.removeValue();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(MyPage.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                                            }
                                        });
                            }
                        }
                );
                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MyPage.this, "취소", Toast.LENGTH_LONG).show();
                    }
                });
                alert_confirm.show();

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                final Like_gal item = (Like_gal) parent.getItemAtPosition(position) ;

                String titleStr = item.like_name ;
                String descStr = item.like_location;

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),Detail_Gallery.class);
                        intent.putExtra("Location",item.like_location);
                        intent.putExtra("Name",item.like_name);
                        startActivity(intent);
                    }
                });

                // TODO : use item data.
            }
        }) ;

    }

}
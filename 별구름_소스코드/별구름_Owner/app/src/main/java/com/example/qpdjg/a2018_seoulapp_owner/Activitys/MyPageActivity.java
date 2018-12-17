package com.example.qpdjg.a2018_seoulapp_owner.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qpdjg.a2018_seoulapp_owner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MyPageActivity";
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    //view objects
    private TextView textViewUserEmail;
    private ImageButton buttonLogout;
    private ImageButton EnterNewGallery_button;
    private ImageButton MyGallerys_info_button;
    private ImageButton textivewDelete;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.parseColor("#321c54"));
        }

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);
        buttonLogout = (ImageButton) findViewById(R.id.buttonLogout);
        EnterNewGallery_button = (ImageButton) findViewById(R.id.EnterNewGallery);
        MyGallerys_info_button = (ImageButton)findViewById(R.id.MyGallery_info_button);
        textivewDelete = (ImageButton) findViewById(R.id.textviewDelete);
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //textViewUserEmail의 내용을 변경해 준다.
        textViewUserEmail.setText("반갑습니다. "+ user.getEmail()+"님\n오늘도 행복한 하루!");
        //logout button event
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
        EnterNewGallery_button.setOnClickListener(this);
        MyGallerys_info_button.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {

        if(view == MyGallerys_info_button){
            startActivity(new Intent(this, MyGallerys.class));

        }

        if(view == EnterNewGallery_button){

            startActivity(new Intent(this, NewGallery.class));
        }

        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        //회원탈퇴를 클릭하면 회원정보를 삭제한다. 삭제전에 컨펌창을 하나 띄워야 겠다.
        if(view == textivewDelete) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String email = user.getEmail();
            int index = email.indexOf("@");
            String save_email = email.substring(0,index);
            mReference = mDatabase.getReference("OwnerProfile/"+save_email);
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MyPageActivity.this);
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            mReference.removeValue();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MyPageActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }
                                    });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MyPageActivity.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }
    }
}
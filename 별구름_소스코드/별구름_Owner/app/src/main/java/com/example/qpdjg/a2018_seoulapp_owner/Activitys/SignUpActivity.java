package com.example.qpdjg.a2018_seoulapp_owner.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qpdjg.a2018_seoulapp_owner.R;
import com.example.qpdjg.a2018_seoulapp_owner.Util_Data.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{
    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPassword_confirm;
    EditText editTextName;
    ImageButton buttonSignup;
    TextView textviewSingin;
    TextView textviewMessage;
    TextView promise_text;
    ProgressDialog progressDialog;
    CheckBox checkBox_forEnter;
    //define firebase object
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();
    private ChildEventListener mChild;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            if (Build.VERSION.SDK_INT >= 21) {
                // 21 버전 이상일 때
                getWindow().setStatusBarColor(Color.parseColor("#321c54"));
            }

            firebaseAuth = FirebaseAuth.getInstance();
            if(firebaseAuth.getCurrentUser() != null) {
                finish();
                startActivity(new Intent(getApplicationContext(), MyPageActivity.class));
            }
            //initializing views
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            editTextPassword_confirm = findViewById(R.id.editTextPassword_confirm);
            textviewSingin= (TextView) findViewById(R.id.textViewSignin);
            textviewMessage = (TextView) findViewById(R.id.textviewMessage);
            buttonSignup = (ImageButton) findViewById(R.id.buttonSignup);
            editTextName = findViewById(R.id.editTextName);
            checkBox_forEnter = (CheckBox)findViewById(R.id.checkBox_forEnter);
            progressDialog = new ProgressDialog(this);
            promise_text = (TextView)findViewById(R.id.textView14) ;

            promise_text.setText(" [수집하는 개인정보의 항목]\n" +
                    " 회사는 회원가입의 서비스를 제공하기 위해 아래와 같은 개인정보를 수집하고 있습니다.\n" +
                    " 1. 필수입력정보\n" +
                    " 이름, 아이디, 비밀번호, 휴대전화, 이메일\n" +
                    " [개인정보 수집 목적]\n" +
                    " 1. 회원가입을 위한 본인확인 및 개인식별");
            //button click event
            buttonSignup.setOnClickListener(this);
            textviewSingin.setOnClickListener(this);
        }
        //Firebse creating a new user
        private void registerUser(){
            if(checkBox_forEnter.isChecked()) {

            }
            else{
                Toast.makeText(this,"약관에 동의해 주세요.",Toast.LENGTH_SHORT).show();
                return;
            }

            //사용자가 입력하는 email, password를 가져온다.
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String password_confirm = editTextPassword_confirm.getText().toString().trim();
            String tokenID = FirebaseInstanceId.getInstance().getToken();
            int index = email.indexOf("@");
            String save_email = email.substring(0,index);
            //        mReference.child("message").push().setValue("2");
            mReference = mDatabase.getReference("OwnerProfile");
            if(!TextUtils.isEmpty(tokenID)) {
                ProfileData profileData = new ProfileData();
                profileData.firebaseKey = tokenID;
                profileData.UserName = editTextName.getText().toString().trim();
                profileData.E_mail = email;
                mReference.child(save_email).setValue(profileData);
            }
            if(!(password.equals(password_confirm))){
                Toast.makeText(this,"Password가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            //email과 password가 비었는지 아닌지를 체크 한다.
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            //email과 password가 제대로 입력되어 있다면 계속 진행된다.
            progressDialog.setMessage("등록중입니다. 기다려 주세요...");
            progressDialog.show();
            //creating a new user
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                finish();
                                startActivity(new Intent(getApplicationContext(), MyPageActivity.class));
                            } else {
                                //에러발생시
                                textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                                Toast.makeText(SignUpActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        //button click event
        @Override
        public void onClick(View view) {
            if(view == buttonSignup) {
                //TODO
                registerUser();
            }
            if(view == textviewSingin) {
                //TODO
                startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
            }
        }
    }
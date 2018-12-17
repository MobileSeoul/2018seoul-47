package com.example.daewon.a2018_seoulapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.daewon.a2018_seoulapp.ImageDTO;
import com.example.daewon.a2018_seoulapp.R;
import com.example.daewon.a2018_seoulapp.comment_data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Detail_Gallery extends BaseActivity  {

    TextView Welcome_text;
    TextView Gallery_name;
    TextView Gallery_explain;
    TextView Owner_explain;
    TextView Owner_insta;
    Button submit_commets;
    TextView Gallery_location;
    TextView Gallery_time;
    TextView Gallery_fee;
    TextView star_textView;
    ImageView map_marker_img;
    private String Detail_Loc;
    private String Detail_Name;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    EditText comments;
    private FirebaseDatabase database;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    private List<ImageDTO> imageDTOs = new ArrayList<>();
    //private List<String> uidLists = new ArrayList<>();
    private ImageView starButton2;
    private RecyclerView recyclerView;
    private List<comment_data> comment_datas = new ArrayList<>();
    public int detail_position =0 ;
    private List<String> img_lists = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__gallery);


        Detail_Loc = getIntent().getStringExtra("Location");
        Detail_Name = getIntent().getStringExtra("Name");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        //String email = user.getEmail();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Welcome_text = (TextView)findViewById(R.id.textView_title);
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);
        imageView5 = (ImageView)findViewById(R.id.imageView5);
        imageView6 = (ImageView)findViewById(R.id.imageView6);
        map_marker_img =(ImageView)findViewById(R.id.map_marker_image);
        comments = (EditText)findViewById(R.id.Comments);
        Gallery_name = (TextView)findViewById(R.id.Gallery_name);
        Gallery_explain= (TextView)findViewById(R.id.Gallery_explain);
        Owner_explain= (TextView)findViewById(R.id.Owner_explain);
        Owner_insta= (TextView)findViewById(R.id.Owner_insta);
        Gallery_location= (TextView)findViewById(R.id.Gallery_location);
        submit_commets=(Button)findViewById(R.id.comment_submit);
        Gallery_time= (TextView)findViewById(R.id.Gallery_time);
        Gallery_fee= (TextView)findViewById(R.id.Gallery_fee);
        starButton2 = findViewById(R.id.starButton2_imageView);
        star_textView = findViewById(R.id.star_textView);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Gal_Ref = rootRef.child("Gallerys");
        DatabaseReference category_Ref = Gal_Ref.child(Detail_Loc);

        String email = firebaseAuth.getCurrentUser().getEmail();
        int index = email.indexOf("@");
        final String user_email = email.substring(0,index);


        database.getReference().child("Gallerys").child(Detail_Loc).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageDTOs.clear();
                //uidLists.clear();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    ImageDTO imageDTO = ds.getValue(ImageDTO.class);
                    //String uidKey = ds.getKey();
                    imageDTOs.add(imageDTO);
                    //uidLists.add(uidKey);
                    if(Detail_Name.equals(ds.getKey().toString())){
                        Welcome_text.setText(Detail_Name+ " 갤러리 소개 페이지");
                        Gallery_name.setText(ds.child("Gallery_name").getValue().toString());
                        Gallery_explain.setText(ds.child("Gallery_explain").getValue().toString());
                        Owner_explain.setText(ds.child("Owner_explain").getValue().toString());
                        Owner_insta.setText("작가 Insta : " + ds.child("Owner_insta").getValue().toString());
                        Gallery_location.setText(ds.child("Gallery_location").getValue().toString());
                        Gallery_time.setText(ds.child("Gallery_time").getValue().toString());
                        Gallery_fee.setText(ds.child("Gallery_fee").getValue().toString());

                        map_marker_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Detail_Gallery.this, GooglemapActivity.class);
                                intent.putExtra("location",Gallery_location.getText().toString());
                                startActivity(intent);
                            }
                        });
                        if(ds.child("stars").hasChild(auth.getCurrentUser().getUid())) {
                            starButton2.setImageResource(R.drawable.star);
                            star_textView.setText(ds.child("starCount").getValue().toString());
                        } else {
                            starButton2.setImageResource(R.drawable.offstar);
                            star_textView.setText(ds.child("starCount").getValue().toString());
                        }

                        starButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //onStarClicked(database.getReference().child("Gallerys").child(Detail_Loc).child(uidLists.get(detail_position)));
                                onStarClicked(database.getReference().child("Gallerys").child(Detail_Loc).child(Detail_Name));

                                if(ds.child("stars").hasChild(auth.getCurrentUser().getUid())) {
                                    starButton2.setImageResource(R.drawable.star);
                                    star_textView.setText(ds.child("starCount").getValue().toString());
                                    database.getReference().child("UserProfile").child(user_email).child("Like").child(Detail_Name).removeValue();
                                } else {
                                    starButton2.setImageResource(R.drawable.offstar);
                                    star_textView.setText(ds.child("starCount").getValue().toString());
                                    database.getReference().child("UserProfile").child(user_email).child("Like").child(Detail_Name).setValue(Detail_Loc);

                                }
                            }
                        });

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
                        return;
                    }
                    detail_position++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit_commets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = comments.getText().toString();

                comment_data commentData = new comment_data();
                mReference = mDatabase.getReference("Gallerys/"+Detail_Loc+"/"+Detail_Name+"/Comments");
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String email = user.getEmail();
                int index = email.indexOf("@");
                String save_email = email.substring(0,index);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String getTime = sdf.format(date);


                commentData.email = email;
                commentData.comment = comment;
                commentData.date = getTime;

                mReference.child("comment"+save_email).setValue(commentData);

                comments.setText("");
                Toast.makeText(getBaseContext(),"리뷰 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BoardRecylcerViewAdapter boardRecylcerViewAdapter = new BoardRecylcerViewAdapter();
        recyclerView.setAdapter(boardRecylcerViewAdapter);
        database = FirebaseDatabase.getInstance();

        database.getReference().child("Gallerys/"+Detail_Loc+"/"+Detail_Name+"/Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_datas.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    comment_data cd = snapshot.getValue(comment_data.class);
                    //System.out.println(my_gallery_read_data.My_Gallery_name);
                    comment_datas.add(cd);
                }
                boardRecylcerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ImageDTO imageDTO = mutableData.getValue(ImageDTO.class);
                if (imageDTO == null) {
                    return Transaction.success(mutableData);
                }

                if (imageDTO.stars.containsKey(auth.getCurrentUser().getUid())) {
                    // Unstar the post and remove self from stars
                    imageDTO.starCount = imageDTO.starCount - 1;
                    imageDTO.stars.remove(auth.getCurrentUser().getUid());
                } else {
                    // Star the post and add self to stars
                    imageDTO.starCount = imageDTO.starCount + 1;
                    imageDTO.stars.put(auth.getCurrentUser().getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(imageDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed

            }
        });
    }

    public void push_imgs(final List img_list){

        if(img_list.size() == 1){
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
            imageView4.setVisibility(View.GONE);
            imageView5.setVisibility(View.GONE);
            imageView6.setVisibility(View.GONE);
        }
        if(img_list.size() == 2){
            imageView3.setVisibility(View.GONE);
            imageView4.setVisibility(View.GONE);
            imageView5.setVisibility(View.GONE);
            imageView6.setVisibility(View.GONE);
        }
        if(img_list.size() == 3){
            imageView4.setVisibility(View.GONE);
            imageView5.setVisibility(View.GONE);
            imageView6.setVisibility(View.GONE);
        }
        if(img_list.size() == 4){
            imageView5.setVisibility(View.GONE);
            imageView6.setVisibility(View.GONE);
        }
        if(img_list.size() == 5){
            imageView6.setVisibility(View.GONE);
        }
        try{
            if(img_list.get(0) != null){
                Glide.with(this).load(img_list.get(0)).into(imageView1);
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = (String)img_list.get(0);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
            if(img_list.get(1) != null){
                Glide.with(this).load(img_list.get(1)).into(imageView2);
                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = (String)img_list.get(1);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
            if(img_list.get(2) != null){
                Glide.with(this).load(img_list.get(2)).into(imageView3);
                imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = (String)img_list.get(2);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
            if(img_list.get(3) != null){
                Glide.with(this).load(img_list.get(3)).into(imageView4);
                imageView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = (String)img_list.get(3);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
            if(img_list.get(4) != null){
                Glide.with(this).load(img_list.get(4)).into(imageView5);
                imageView5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = (String)img_list.get(4);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
            if(img_list.get(5) != null){
                Glide.with(this).load(img_list.get(5)).into(imageView6);
                imageView6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = (String)img_list.get(5);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }

        }catch (Exception e){

        }
    }


    class BoardRecylcerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).textView.setText("작성 자 : "+comment_datas.get(position).email);
            ((CustomViewHolder)holder).textView2.setText("내용 : "+comment_datas.get(position).comment);
            ((CustomViewHolder)holder).textView_date.setText("작성 날짜 : "+comment_datas.get(position).date);

        }

        @Override
        public int getItemCount() {
            return comment_datas.size();
        }



        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView textView2;
            TextView textView_date;

            public CustomViewHolder(View view) {
                super(view);
                textView = (TextView)view.findViewById(R.id.commet_name);
                textView2 = (TextView)view.findViewById(R.id.comment_content);
                textView_date = (TextView)view.findViewById(R.id.comment_date);

            }
        }
    }

}

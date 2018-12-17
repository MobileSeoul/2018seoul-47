package com.example.daewon.a2018_seoulapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daewon.a2018_seoulapp.GalleryList;
import com.example.daewon.a2018_seoulapp.ImageDTO;
import com.example.daewon.a2018_seoulapp.R;
import com.example.daewon.a2018_seoulapp.Util.Like_gal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Map_sub_Activity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<ImageDTO> imageDTOs = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    String region;
    int temp;
    private ArrayList<Like_gal> like_gal_list = new ArrayList<>();

    private String temp1,temp2,temp3,temp4,temp5;
    private int count=0;

    private ImageButton best5, find_gallery, mypage;
    int i = 4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sub);

        firebaseAuth = FirebaseAuth.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
        int index = email.indexOf("@");
        final String user_email = email.substring(0,index);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference user_Ref = rootRef.child("UserProfile");
        DatabaseReference category_Ref = user_Ref.child(user_email);
        DatabaseReference like_Ref = category_Ref.child("Like");

        //        appcall db내 앱이름으로 불러옴
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                like_gal_list.clear();

                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Like_gal temp = new Like_gal();
                    temp.like_name = ds.getKey().toString();
                    temp.like_location = ds.getValue().toString();

                    final String[] temp_img_src = new String[1];
                    database.getReference().child("Gallerys").child(temp.like_location).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for( DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if(snapshot.getKey().equals(ds.getKey().toString())){
                                    temp_img_src[0] = snapshot.child("Main_img").getValue().toString();
                                    temp.main_img_src = temp_img_src[0];
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    like_gal_list.add(temp);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        like_Ref.addListenerForSingleValueEvent(valueEventListener);

        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        if(intent != null){
            region = intent.getStringExtra("name");
        }

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BoardRecylcerViewAdapter boardRecylcerViewAdapter = new BoardRecylcerViewAdapter();
        recyclerView.setAdapter(boardRecylcerViewAdapter);

        best5 = findViewById(R.id.best5);
        find_gallery = findViewById(R.id.find_gallery);
        mypage = findViewById(R.id.mypage);

        database.getReference().child("Gallerys").child(region).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageDTOs.clear();
                uidLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                    String uidKey = snapshot.getKey();
                    imageDTOs.add(imageDTO);
                    uidLists.add(uidKey);
                }
                boardRecylcerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        best5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i != 1) {
                    best5.setImageResource(R.drawable.best5_on);
                    find_gallery.setImageResource(R.drawable.gallery_off);
                    mypage.setImageResource(R.drawable.mypage_off);
                    Intent intent = new Intent(Map_sub_Activity.this, GalleryList.class);
                    finish();
                    startActivity(intent);
                    i =1;
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
                    Intent intent = new Intent(Map_sub_Activity.this, MapActivity.class);
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
                    Intent intent = new Intent(Map_sub_Activity.this, MyPage.class);
                    intent.putExtra("list",like_gal_list);

                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    class BoardRecylcerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){

            String email = firebaseAuth.getCurrentUser().getEmail();
            int index = email.indexOf("@");
            final String user_email = email.substring(0,index);

            ((CustomViewHolder)holder).textView.setText(imageDTOs.get(position).Gallery_name);
            ((CustomViewHolder)holder).textView2.setText(imageDTOs.get(position).Gallery_location_from_list);
            ((CustomViewHolder)holder).star_textView.setText(Integer.toString(imageDTOs.get(position).starCount));
            Glide.with(holder.itemView.getContext()).load(imageDTOs.get(position).Main_img).into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).starButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStarClicked(database.getReference().child("Gallerys").child(region).child(uidLists.get(position)));
                }
            });

            if (imageDTOs.get(position).stars.containsKey(auth.getCurrentUser().getUid())) {
                temp = position;
                ((CustomViewHolder)holder).starButton.setImageResource(R.drawable.best_star);
                database.getReference().child("UserProfile").child(user_email).child("Like").child(imageDTOs.get(position).Gallery_name).setValue(imageDTOs.get(position).Gallery_location_from_list);
            } else {
                temp = position;
                ((CustomViewHolder)holder).starButton.setImageResource(R.drawable.best_offstar);
                database.getReference().child("UserProfile").child(user_email).child("Like").child(imageDTOs.get(position).Gallery_name).removeValue();
            }
        }

        @Override
        public int getItemCount() {
            return imageDTOs.size();
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

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView textView2;
            ImageView starButton;
            TextView star_textView;
            public CustomViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.item_imageView);
                textView = view.findViewById(R.id.item_textView);
                textView2 = view.findViewById(R.id.item_textView2);
                starButton = view.findViewById(R.id.starButton_imageView);
                star_textView = view.findViewById(R.id.star_textView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),Detail_Gallery.class);
                        intent.putExtra("Location",textView2.getText().toString());
                        intent.putExtra("Name",textView.getText().toString());
                        intent.putExtra("position", temp);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}

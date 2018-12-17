package com.example.daewon.a2018_seoulapp;

import android.annotation.SuppressLint;
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
import com.example.daewon.a2018_seoulapp.Activity.BaseActivity;
import com.example.daewon.a2018_seoulapp.Activity.Detail_Gallery;
import com.example.daewon.a2018_seoulapp.Activity.MapActivity;
import com.example.daewon.a2018_seoulapp.Activity.MyPage;
import com.example.daewon.a2018_seoulapp.Util.Like_gal;
import com.example.daewon.a2018_seoulapp.Util.OnSwipeTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GalleryList extends BaseActivity {

    private RecyclerView recyclerView;
    private List<ImageDTO> imageDTOs = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String temp1,temp2,temp3,temp4,temp5;
    private int count=0;
    private FirebaseAuth firebaseAuth;

    private ArrayList<Like_gal> like_gal_list = new ArrayList<>();

    String[] Gallery_locations_list;

         private ImageButton best5, find_gallery, mypage;
        int i = 1;
        @SuppressLint("ClickableViewAccessibility")
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Gallery_locations_list = new String[]{"강서구", "마포구", "영등포구", "양천구", "구로구", "금천구", "관악구", "동작구", "용산구", "서초구", "강남구", "송파구", "강동구", "광진구", "성동구", "중구", "용산구", "서대문구", "은평구", "종로구", "성북수", "동대문구", "중랑구", "강북구", "노원구", "도봉구"};


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


        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final GalleryListAdapter galleryListAdapter = new GalleryListAdapter();
        recyclerView.setAdapter(galleryListAdapter);


        best5 = findViewById(R.id.best5);
        find_gallery = findViewById(R.id.find_gallery);
        mypage = findViewById(R.id.mypage);

        recyclerView.setOnTouchListener(new OnSwipeTouchListener(GalleryList.this) {

            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                finish();
                startActivity(intent);
            }
            public void onSwipeLeft() {
               Intent intent = new Intent(getApplicationContext(), MapActivity.class);
               finish();
               startActivity(intent);
            }

        });

        imageDTOs.clear();

       for(int k = 0; k<Gallery_locations_list.length;k++) {
           database.getReference().child("Gallerys").child(Gallery_locations_list[k]).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   uidLists.clear();
                   for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                       System.out.println(imageDTO.Gallery_name);
                       String uidKey = snapshot.getKey();
                       imageDTOs.add(imageDTO);
                       uidLists.add(uidKey);
                   }
                   galleryListAdapter.notifyDataSetChanged();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }

        //ArrayList temp = new ArrayList();
       // temp.add(imageDTOs);

        best5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i != 1) {
                    best5.setImageResource(R.drawable.best5_on);
                    find_gallery.setImageResource(R.drawable.gallery_off);
                    mypage.setImageResource(R.drawable.mypage_off);
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
                    Intent intent = new Intent(GalleryList.this, MapActivity.class);
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
                    Intent intent = new Intent(GalleryList.this, MyPage.class);
                    intent.putExtra("list",like_gal_list);

                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    class GalleryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery,parent,false);
            return new CustomViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

            for(int j = 0;j<imageDTOs.size();j++){
                String temp_name = imageDTOs.get(j).Gallery_name;
                for(int k = j+1;k<imageDTOs.size();k++){
                    if(temp_name.equals(imageDTOs.get(k).Gallery_name)){
                        imageDTOs.remove(j);
                    }
                }
            }

            ArrayList temp = new ArrayList();
            temp.add(imageDTOs);
            for (int i = 0; i < temp.size(); i++) {
                Collections.sort((List<ImageDTO>) temp.get(i), new Comparator<ImageDTO>() {
                    @Override
                    public int compare(ImageDTO imageDTO, ImageDTO t1) {
                        if (imageDTO.starCount > t1.starCount) {
                            return 1;
                        } else if (imageDTO.starCount < t1.starCount) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                Collections.reverse((List<?>) temp.get(i));
            }

            if(imageDTOs.size() > 5) {
                imageDTOs = imageDTOs.subList(0, 5);
            }
            ((CustomViewHolder)holder).textView.setText(imageDTOs.get(position).Gallery_name);
            ((CustomViewHolder)holder).textView2.setText(imageDTOs.get(position).Gallery_location_from_list);
            ((CustomViewHolder)holder).star_textView.setText(Integer.toString(imageDTOs.get(position).starCount));
            Glide.with(holder.itemView.getContext()).load(imageDTOs.get(position).Main_img).into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).starButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //onStarClicked(database.getReference().child("Gallerys").child((String) ((CustomViewHolder)holder).textView2.getText()).child(imageDTOs.get(position).Gallery_name));
                }
            });

            /*if (imageDTOs.get(position).stars.containsKey(auth.getCurrentUser().getUid())) {
                ((CustomViewHolder)holder).starButton.setImageResource(R.drawable.baseline_favorite_black_24);
>>>>>>> 8395bc550ef6c029e717b21471a2b3c40b05b5cd
            } else {
                ((CustomViewHolder)holder).starButton.setImageResource(R.drawable.baseline_favorite_border_black_24);
            }*/

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
                        startActivity(intent);
                    }
                });
            }
        }
    }
}

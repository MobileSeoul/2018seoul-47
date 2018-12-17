package com.example.daewon.a2018_seoulapp.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daewon.a2018_seoulapp.R;
import com.example.daewon.a2018_seoulapp.Util.Like_gal;

import java.util.ArrayList;

public class Like_list_Adapter extends BaseAdapter {

    private ArrayList<Like_gal> listViewItemList = new ArrayList<Like_gal>() ;

    public Like_list_Adapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_gallerys_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.MyGallery_imageView) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.MyGallery_Name) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.MyGallery_Location) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Like_gal listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(context).load(listViewItem.main_img_src).into(iconImageView);
        //iconImageView.setImageDrawable(listViewItem.main_img_src);
        titleTextView.setText(listViewItem.like_name);
        descTextView.setText(listViewItem.like_location);

        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String icon, String title, String desc) {
        Like_gal item = new Like_gal();

        item.main_img_src = icon;
        item.like_name = title;
        item.like_location = desc;

        listViewItemList.add(item);
    }

}

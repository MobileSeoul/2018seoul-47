package com.example.daewon.a2018_seoulapp.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.daewon.a2018_seoulapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GooglemapActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    private EditText editText;
    private String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        //editText = findViewById(R.id.editText);

       // button = findViewById(R.id.button);
        location = getIntent().getStringExtra("location");
        //editText.setText(location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                //마커타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng : 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude,longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
            }
        });

//        //버튼 이벤트
//        button.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str = editText.getText().toString();
//                List<Address> addressList = null;
//                try {
//                    //editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오코딩을 이용해 변환
//                    addressList = geocoder.getFromLocationName(str,10);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println(addressList.get(0).toString());
//                //콤마를 기준으로 split
//                String []splitStr = addressList.get(0).toString().split(",");
//                //주소
//                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() -2 );
//                System.out.println(address);
//
//                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
//                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
//                System.out.println(latitude);
//                System.out.println(longitude);
//
//                // 좌표(위도, 경도) 생성
//                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//                // 마커 생성
//                MarkerOptions mOptions2 = new MarkerOptions();
//                mOptions2.title("search result");
//                mOptions2.snippet(address);
//                mOptions2.position(point);
//                // 마커 추가
//                mMap.addMarker(mOptions2);
//                // 해당 좌표로 화면 줌
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
//            }
//        });


        ///////////////////

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        //button.performClick();

        String str = location;
        List<Address> addressList = null;
        try {
            //editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오코딩을 이용해 변환
            addressList = geocoder.getFromLocationName(str,10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(addressList.get(0).toString());
        //콤마를 기준으로 split
        String []splitStr = addressList.get(0).toString().split(",");
        //주소
        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() -2 );
        System.out.println(address);

        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
        System.out.println(latitude);
        System.out.println(longitude);

        // 좌표(위도, 경도) 생성
        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        // 마커 생성
        MarkerOptions mOptions2 = new MarkerOptions();
        mOptions2.title("search result");
        mOptions2.snippet(address);
        mOptions2.position(point);
        // 마커 추가
        mMap.addMarker(mOptions2);
        // 해당 좌표로 화면 줌
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
    }
}
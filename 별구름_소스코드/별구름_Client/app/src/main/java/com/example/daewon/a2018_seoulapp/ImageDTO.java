package com.example.daewon.a2018_seoulapp;

import java.util.HashMap;
import java.util.Map;

public class ImageDTO {
    public String Gallery_name;
    public String Gallery_location_from_list;
    public String Main_img;
    public String Gallery_location;
    public String Gallery_explain;
    public String Gallery_fee;
    public String Gallery_time;
    public String Owner_explain;
    public String Owner_insta;
    public String Gallery_owner_email;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public Map<String, comment_data> Comments = new HashMap<>();
    public Map<String, String> Gallery_imgs = new HashMap<>();
}

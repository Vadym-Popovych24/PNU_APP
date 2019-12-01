package com.social_network.pnu_app.entity;

import java.util.HashMap;
import java.util.Map;

public class Student {

    String seriesIDcard;
    String name;
    String LastName;
    int id;
    String email;
    String password;
    String phone;
    boolean verify;

    public Student(){}

    public Student(boolean verify, String seriesIDcard, String name, String LastName, int id, String email,
            String password,String phone){

        this.seriesIDcard = seriesIDcard;
        this.name = name;
        this.LastName = LastName;
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.verify = verify;

    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> MapDatabase = new HashMap<>();
        MapDatabase.get(password);
        MapDatabase.put("seriesIDcard", seriesIDcard);
        MapDatabase.put("name", name);
        MapDatabase.put("LastName", LastName);
        MapDatabase.put("id", id);
        MapDatabase.put("email", email);
        MapDatabase.put("password", password);
        MapDatabase.put("phone", phone);
        MapDatabase.put("verify",verify);

        return MapDatabase;
    }
}

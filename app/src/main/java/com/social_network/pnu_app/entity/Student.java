package com.social_network.pnu_app.entity;

import java.util.HashMap;
import java.util.Map;

public class Student {

    public static HashMap<Object, Object> student = new HashMap();
    public String seriesIDcard;
    public String name;
    public String lastName;
    public int id;
    public String email;
    public String password;
    public String phone;
    public boolean verify;
    public String uid;

    public Student(){}

    public Student(String seriesIDcard, String name, String lastName, int id, String email,
            String password,String phone, boolean verify, String uid){

        this.seriesIDcard = seriesIDcard;
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.verify = verify;
        this.uid = uid;

    }

    public Student(boolean verify, String password,String phone,String uid){

        this.verify = verify;
        this.password = password;
        this.phone = phone;
        this.uid = uid;

    }

    public Map<String, Object> toMapUpdateChild(){
        HashMap<String, Object> MapDatabase = new HashMap<>();
        MapDatabase.put("verify",verify);
        MapDatabase.put("password", password);
        MapDatabase.put("uid", uid);
        MapDatabase.put("phone", phone);

        return MapDatabase;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> MapDatabase = new HashMap<>();
        MapDatabase.get(password);
        MapDatabase.put("seriesIDcard", seriesIDcard);
        MapDatabase.put("name", name);
        MapDatabase.put("lastName", lastName);
        MapDatabase.put("id", id);
        MapDatabase.put("email", email);
        MapDatabase.put("password", password);
        MapDatabase.put("phone", phone);
        MapDatabase.put("uid", uid);
        MapDatabase.put("verify",verify);

        return MapDatabase;
    }
}

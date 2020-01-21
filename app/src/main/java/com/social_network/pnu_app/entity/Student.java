package com.social_network.pnu_app.entity;

import java.util.HashMap;
import java.util.Map;

public class Student {

    String seriesIDcard;
    String name;
    String lastName;
    int id;
    String email;
    String password;
    String phone;
    boolean verify;

    public Student(){}

    public Student(boolean verify, String seriesIDcard, String name, String lastName, int id, String email,
            String password,String phone){

        this.seriesIDcard = seriesIDcard;
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.verify = verify;

    }

    public Student(boolean verify,  String email, String password,String phone){

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
        MapDatabase.put("lastName", lastName);
        MapDatabase.put("id", id);
        MapDatabase.put("email", email);
        MapDatabase.put("password", password);
        MapDatabase.put("phone", phone);
        MapDatabase.put("verify",verify);

        return MapDatabase;
    }
}

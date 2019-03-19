package com.social_network.pnu_app.entity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.social_network.pnu_app.localdatabase.StudentSqlLite;
import com.social_network.pnu_app.pages.MainStudentPage;
import com.social_network.pnu_app.registration.Registration;

import java.util.HashMap;
import java.util.Map;

public class Student {

    public StudentSqlLite studentLocalSet = new StudentSqlLite();


    public static HashMap<Object, Object> student = new HashMap(){{
        put("faculty", "faculty");
        put("group", "group");
        put("dateOfEntry", "dateOfEntry");
        put("formStudying", "formStudying");
    }};
    public static String seriesIDcard;
    public String name;
    public String lastName;
    public int id;
    public String email;
    public String password;
    public String phone;
    public boolean verify;
    public String uid;
    PhoneAuthCredential credentiall;

    AppDatabase StudentDatabase;

    public Student(){}

    public Student(String seriesIDcard){
        this.seriesIDcard = seriesIDcard;
    }

    public Student(String seriesIDcard2, String name, String lastName, int id, String email,
            String password,String phone, boolean verify, String uid){

        seriesIDcard = seriesIDcard2;
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.verify = verify;
        this.uid = uid;

    }

    public Student(PhoneAuthCredential credential, boolean verify, String password,String phone,String uid){
        this.credentiall = credential;
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

    public void synchronizationSQLiteSignIn(final AppDatabase db){
        StudentDatabase = db;
        if (db.studentDao().getSeriesIDcard(seriesIDcard) != null){
            System.out.println("updateStudetnSQLite() getSeriesIDcard(seriesIDcard)= " + (db.studentDao().getSeriesIDcard(seriesIDcard)));
            System.out.println("studentUpdate() = " + student);
            updateStudetnSQLite();

        }
        else {
            System.out.println("setStudentSQLite() getSeriesIDcard(seriesIDcard)= " + (db.studentDao().getSeriesIDcard(seriesIDcard)));
            System.out.println("studentSet() = " + student);
            setStudentSQLite();

        }
    }

    public void updateStudetnSQLite(){
        StudentDatabase.studentDao().updateAllField(
                student.get("seriesIDcard").toString(),
                student.get("name").toString(),
                student.get("lastName").toString(),
                Integer.parseInt(student.get("id").toString()),
                student.get("email").toString(),
                student.get("password").toString(),
                student.get("phone").toString(),
                student.get("uid").toString(),
                (Boolean) student.get("verify")
                );
    }

    public void setStudentSQLite(){

        studentLocalSet.setSeriesIDcard(student.get("seriesIDcard").toString());
        studentLocalSet.setFirstName(student.get("name").toString());
        studentLocalSet.setLastName(student.get("lastName").toString());
        studentLocalSet.setUid(Integer.parseInt(student.get("id").toString()));
        studentLocalSet.setEmail(student.get("email").toString());
        studentLocalSet.setPassword(student.get("password").toString());
        studentLocalSet.setPhone(student.get("phone").toString());
        studentLocalSet.setKeyFireBase(student.get("uid").toString());
        studentLocalSet.setVerify(Boolean.parseBoolean(student.get("verify").toString()));

        studentLocalSet.setPatronym(student.get("patronym").toString());
        studentLocalSet.setFaculty(student.get("faculty").toString());
        studentLocalSet.setGroupStudent(student.get("group").toString());
        studentLocalSet.setDateOfEntry(student.get("dateOfEntry").toString());
        studentLocalSet.setFormStudying(student.get("formStudying").toString());

        studentLocalSet.setCurrentStudent(student.get("id").toString());

        StudentDatabase.studentDao().insertStudent(studentLocalSet);
    }
}

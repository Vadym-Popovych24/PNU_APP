package com.social_network.pnu_app.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static java.sql.Types.VARCHAR;

@Entity(tableName = "student", indices = {@Index(value = {"email", "phone", "seriesIDcard"}, unique = true)})
public class Student {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    private int uid;

    @NonNull
    @ColumnInfo(name = "first_name", typeAffinity = VARCHAR)
    private String firstName;

    @NonNull
    @ColumnInfo(name = "last_name", typeAffinity = VARCHAR)
    private String lastName;


    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "email", typeAffinity = VARCHAR)
    private String email;

    @NonNull
    @ColumnInfo(name = "seriesIDcard")
    private String seriesIDcard;



    @ColumnInfo(name = "phone", typeAffinity = VARCHAR)
    private String phone;

    @NonNull
    @ColumnInfo(name = "verify")
    private boolean verify;



   /*@ColumnInfo(name = "dataPublishIDcar", typeAffinity = DATE)
    private String dataPublishIDcar;


    @ColumnInfo(name = "dataValidIDcar", typeAffinity = DATE)
    private String dataValidIDcar;*/


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



   @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }


    @NonNull
    public String getSeriesIDcard() {
        return seriesIDcard;
    }

    public void setSeriesIDcard(@NonNull String series_id_card) {
        this.seriesIDcard = series_id_card;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

}

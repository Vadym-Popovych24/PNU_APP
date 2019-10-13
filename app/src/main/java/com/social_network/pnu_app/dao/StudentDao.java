package com.social_network.pnu_app.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.social_network.pnu_app.entity.Student;

import java.util.List;


@Dao
public interface StudentDao {


    @Query("SELECT * FROM student")
    List<Student> getAll();

    @Query("SELECT seriesIDcard FROM student WHERE seriesIDcard = :seriesIDcard ")
    String getSeriesIDcard(String seriesIDcard);

    @Query("SELECT first_name FROM student WHERE u_id = :uid")
    String getFirstName(int uid);

    @Query("SELECT last_name FROM student WHERE u_id = :uid")
    String getLastName(int uid);

    @Query("SELECT u_id  FROM student WHERE seriesIDcard = :seriesIDcard")
    int getIdstudentByIDcard(String seriesIDcard);

    @Query("SELECT u_id  FROM student WHERE password = :password")
    int getIdstudentByIDPassword(String password);

    @Query("SELECT password FROM student WHERE password = :password ")
    String getPassword(String password);

    @Query("SELECT * FROM student WHERE first_name LIKE  :firstName AND last_name LIKE :lastName")
    Student findByName(String firstName, String lastName);

    @Query("SELECT COUNT(*) FROM student")
    int countStudent();

    @Query("SELECT verify FROM student WHERE u_id = :uid")
    boolean getVerify(int uid);

    @Query("UPDATE student SET verify=:verify WHERE u_id= :id")
    void updateVerify(boolean verify, int id);





    @Insert
    void insertAll(Student... students);

    @Delete
    void delete(Student student);
}

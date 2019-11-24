package com.social_network.pnu_app.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.social_network.pnu_app.database.AppDatabase;
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

    @Query("SELECT u_id  FROM student WHERE password = :password AND u_id =:idSeriesIDCard")
    int getIdstudentByIDPassword(String password, int idSeriesIDCard);

    @Query("SELECT password FROM student WHERE password = :password AND u_id= :id")
    String getPassword(String password , int id);

    @Query("SELECT * FROM student WHERE first_name LIKE :firstName AND last_name LIKE :lastName")
    Student findByName(String firstName, String lastName);

    @Query("SELECT COUNT(*) FROM student")
    int countStudent();

    @Query("SELECT verify FROM student WHERE u_id = :uid")
    boolean getVerify(int uid);

    @Query("SELECT password FROM student WHERE u_id = :uid")
    String getPasswordById(int uid);

    @Query("SELECT email FROM student WHERE u_id = :uid")
    String getEmailById(int uid);

    @Query("SELECT seriesIDcard FROM student WHERE u_id = :uid")
    String getSeriesBYId(int uid);

    @Query("UPDATE student SET verify=:verify WHERE u_id= :id")
    void updateVerify(boolean verify, int id);

    @Query("UPDATE student SET password=:password WHERE u_id= :id")
    void setPassword(String password, int id);

    @Query("UPDATE student SET email=:email WHERE u_id= :id")
    void setEmail(String email, int id);

   // @Query("DELETE FROM student")
   // void DeleteAllTableStudent();





    @Insert
    void insertAll(Student... students);

    @Delete
    void delete(Student student);
}

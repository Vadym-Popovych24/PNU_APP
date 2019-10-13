package com.social_network.pnu_app.utils;


import android.os.AsyncTask;

import android.util.Log;

import androidx.annotation.NonNull;


import com.social_network.pnu_app.database.AppDatabase;
import com.social_network.pnu_app.entity.Student;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static Student addStudent(final AppDatabase db, Student student) {
        db.studentDao().insertAll(student);
        return student;
    }

    private static void populateWithTestData(AppDatabase db) {
        Student user = new Student();

        Student student1 = new Student();
        student1.setFirstName("Name1");
        student1.setLastName("LastName1");
        student1.setEmail("Email1@gmail.com");
        student1.setVerify(true);
        student1.setSeriesIDcard("BA1");
        student1.setPassword("pass1");

        addStudent(db, student1);

        Student student2 = new Student();
        student2.setFirstName("Name2");
        student2.setLastName("LastName2");
        student2.setEmail("Email2@gmail.com");
        student2.setVerify(true);
        student2.setSeriesIDcard("BA2");
        student2.setPassword("pass2");

        addStudent(db, student2);

        Student student3 = new Student();
        student3.setFirstName("Name3");
        student3.setLastName("LastName3");
        student3.setEmail("Email3@gmail.com");
        student3.setVerify(true);
        student3.setSeriesIDcard("BA3");
        student3.setPassword("pass3");

        addStudent(db, student3);

        Student student4 = new Student();
        student4.setFirstName("Name4");
        student4.setLastName("LastName4");
        student4.setEmail("Email4@gmail.com");
        student4.setVerify(true);
        student4.setSeriesIDcard("BA4");
        student4.setPassword("pass4");

        addStudent(db, student4);

        Student student5 = new Student();
        student5.setFirstName("Name5");
        student5.setLastName("LastName5");
        student5.setEmail("Email5@gmail.com");
        student5.setVerify(true);
        student5.setSeriesIDcard("BA5");
        student5.setPassword("pass5");

        addStudent(db, student5);

        Student student6 = new Student();
        student6.setFirstName("Name6");
        student6.setLastName("LastName6");
        student6.setEmail("Email6@gmail.com");
        student6.setVerify(true);
        student6.setSeriesIDcard("BA6");
        student6.setPassword("pass6");

        addStudent(db, student6);

        Student student7 = new Student();
        student7.setFirstName("Name7");
        student7.setLastName("LastName7");
        student7.setEmail("Email7@gmail.com");
        student7.setVerify(true);
        student7.setSeriesIDcard("BA7");
        student7.setPassword("pass7");

        addStudent(db, student7);

        Student student8 = new Student();
        student8.setFirstName("Name8");
        student8.setLastName("LastName8");
        student8.setEmail("Email8@gmail.com");
        student8.setVerify(true);
        student8.setSeriesIDcard("BA8");
        student8.setPassword("pass8");

        addStudent(db, student8);


        Student student9 = new Student();
        student9.setFirstName("Name9");
        student9.setLastName("LastName9");
        student9.setEmail("Email9@gmail.com");
        student9.setVerify(true);
        student9.setSeriesIDcard("BA9");
        student9.setPassword("pass9");

        addStudent(db, student9);

        Student student10 = new Student();
        student10.setFirstName("Name10");
        student10.setLastName("LastName10");
        student10.setEmail("Email10@gmail.com");
        student10.setVerify(true);
        student10.setSeriesIDcard("BA10");
        student10.setPassword("pass10");

        addStudent(db, student10);


        List<Student> userList = db.studentDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + userList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}

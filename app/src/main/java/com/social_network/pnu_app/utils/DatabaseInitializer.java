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
        student1.setFirstName("StudentName1");
        student1.setLastName("StudentLastName1");
        student1.setEmail("Email1@gmail.com");
        student1.setVerify(true);
        student1.setSeriesIDcard("BA12345671");
        student1.setPassword("qwerty1%");

        addStudent(db, student1);

        Student student2 = new Student();
        student2.setFirstName("StudentName2");
        student2.setLastName("StudentLastName2");
        student2.setEmail("Email2@gmail.com");
        student2.setVerify(true);
        student2.setSeriesIDcard("BA12345672");
        student2.setPassword("qwerty2%");

        addStudent(db, student2);

        Student student3 = new Student();
        student3.setFirstName("StudentName3");
        student3.setLastName("StudentLastName3");
        student3.setEmail("Email3@gmail.com");
        student3.setVerify(true);
        student3.setSeriesIDcard("BA12345673");
        student3.setPassword("qwerty3%");

        addStudent(db, student3);

        Student student4 = new Student();
        student4.setFirstName("StudentName4");
        student4.setLastName("StudentLastName4");
        student4.setEmail("Email4@gmail.com");
        student4.setVerify(true);
        student4.setSeriesIDcard("BA12345674");
        student4.setPassword("qwerty4%");

        addStudent(db, student4);

        Student student5 = new Student();
        student5.setFirstName("StudentName5");
        student5.setLastName("StudentLastName5");
        student5.setEmail("Email5@gmail.com");
        student5.setVerify(true);
        student5.setSeriesIDcard("BA12345675");
        student5.setPassword("qwerty5%");

        addStudent(db, student5);

        Student student6 = new Student();
        student6.setFirstName("StudentName6");
        student6.setLastName("StudentLastName6");
        student6.setEmail("Email6@gmail.com");
        student6.setVerify(true);
        student6.setSeriesIDcard("BA12345676");
        student6.setPassword("qwerty7%");

        addStudent(db, student6);

        Student student7 = new Student();
        student7.setFirstName("StudentName7");
        student7.setLastName("StudentLastName7");
        student7.setEmail("Email7@gmail.com");
        student7.setVerify(true);
        student7.setSeriesIDcard("BA12345677");
        student7.setPassword("qwerty7%");

        addStudent(db, student7);

        Student student8 = new Student();
        student8.setFirstName("StudentName8");
        student8.setLastName("StudentLastName8");
        student8.setEmail("Email8@gmail.com");
        student8.setVerify(true);
        student8.setSeriesIDcard("BA12345678");
        student8.setPassword("qwerty8%");

        addStudent(db, student8);


        Student student9 = new Student();
        student9.setFirstName("StudentName9");
        student9.setLastName("StudentLastName9");
        student9.setEmail("Email9@gmail.com");
        student9.setVerify(true);
        student9.setSeriesIDcard("BA12345679");
        student9.setPassword("qwerty9%");

        addStudent(db, student9);

        Student student10 = new Student();
        student10.setFirstName("StudentName10");
        student10.setLastName("StudentLastName10");
        student10.setEmail("Email10@gmail.com");
        student10.setVerify(true);
        student10.setSeriesIDcard("BA12345670");
        student10.setPassword("qwerty10%");

        addStudent(db, student10);


        Student vadym = new Student();
        vadym.setFirstName("Vadym");
        vadym.setLastName("Popovych");
       // vadym.setEmail("vadympopovychn24@gmail.com");
        vadym.setVerify(false);
        vadym.setSeriesIDcard("BA11582136");
        //vadym.setPassword("vadym24;");

        addStudent(db, vadym);


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

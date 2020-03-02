package com.social_network.pnu_app.localdatabase;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class DatabaseInitializer {
 static String valueIDSERIES="";
    static String valueIDSERIES2="";
    static String valueIDSERIES3="";

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
       task.execute();
    }
    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static StudentSqlLite addStudent(final AppDatabase db, StudentSqlLite student) {
        db.studentDao().insertAll(student);
        return student;
    }

    private static void populateWithTestData(AppDatabase db) {
       final int countStudent = 10;
        StudentSqlLite student[] = new StudentSqlLite[countStudent];
        String studentPass;
      //  Registration encrypt = new Registration();
     /*   for (int i = 0; i < countStudent; i++){
            student[i].setFirstName("StudentName" + String.valueOf(i));
            student[i].setLastName("StudentLastName" + String.valueOf(i));
            student[i].setEmail("Email1@" + String.valueOf(i) + "gmail.com");
            student[i].setVerify(true);
            student[i].setSeriesIDcard("BA1234567" + String.valueOf(i));
            // studentPass = "qwerty" + i + "%";
            // studentPass = encrypt.encryptionPassword(studentPass);
            student[i].setPassword("qwerty" + String.valueOf(i) + "%");

            addStudent(db, student[i]);
        }*/


        //Registration encrypt = new Registration();




        int i =1;
        int valueIDSeriesIDcard;
        valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDSERIES);

        StudentSqlLite student1 = new StudentSqlLite();

            if (valueIDSERIES == "") {

                valueIDSERIES =db.studentDao().getSeriesIDcard("BA11582136");

                StudentSqlLite vadym = new StudentSqlLite();
                vadym.setFirstName("Vadym");
                vadym.setLastName("Popovych");
                // vadym.setEmail("vadympopovychn24@gmail.com");
                vadym.setVerify(false);
                vadym.setSeriesIDcard("BA11582136");
                //vadym.setPassword("vadym24;");

                addStudent(db, vadym);
            }

        if (valueIDSERIES2 == "") {

            valueIDSERIES2 =db.studentDao().getSeriesIDcard("BA11582137");

            StudentSqlLite vadym2 = new StudentSqlLite();
            vadym2.setFirstName("Vadym2");
            vadym2.setLastName("Popovych2");
            // vadym2.setEmail("vadympopovychn24@gmail.com");
            vadym2.setVerify(false);
            vadym2.setSeriesIDcard("BA11582137");
            //vadym2.setPassword("vadym24;");

            addStudent(db, vadym2);
        }
        if (valueIDSERIES3 == "") {

            valueIDSERIES3 = db.studentDao().getSeriesIDcard("BA11582138");

            StudentSqlLite vadym3 = new StudentSqlLite();
            vadym3.setFirstName("Vadym3");
            vadym3.setLastName("Popovych3");
            // vadym3.setEmail("vadympopovychn24@gmail.com");
            vadym3.setVerify(false);
            vadym3.setSeriesIDcard("BA11582138");
            //vadym3.setPassword("vadym24;");

            addStudent(db, vadym3);

        }

            else {

                student1.setFirstName("StudentName" + String.valueOf(i));
                student1.setLastName("StudentLastName" + String.valueOf(i));
                student1.setEmail("Email" + String.valueOf(i) + "@gmail.com");
                student1.setVerify(true);
                student1.setSeriesIDcard("BA1234567" + String.valueOf(i));
                studentPass = "qwerty" + String.valueOf(i) + "%";
                //studentPass = encrypt.encryptionPassword(studentPass);
                student1.setPassword(studentPass);

                addStudent(db, student1);


                StudentSqlLite student2 = new StudentSqlLite();
                student2.setFirstName("StudentName2");
                student2.setLastName("StudentLastName2");
                student2.setEmail("Email2@gmail.com");
                student2.setVerify(true);
                student2.setSeriesIDcard("BA12345672");
                student2.setPassword("qwerty2%");

                addStudent(db, student2);

                StudentSqlLite student3 = new StudentSqlLite();
                student3.setFirstName("StudentName3");
                student3.setLastName("StudentLastName3");
                student3.setEmail("Email3@gmail.com");
                student3.setVerify(true);
                student3.setSeriesIDcard("BA12345673");
                student3.setPassword("qwerty3%");

                addStudent(db, student3);

                StudentSqlLite student4 = new StudentSqlLite();
                student4.setFirstName("StudentName4");
                student4.setLastName("StudentLastName4");
                student4.setEmail("Email4@gmail.com");
                student4.setVerify(true);
                student4.setSeriesIDcard("BA12345674");
                student4.setPassword("qwerty4%");

                addStudent(db, student4);

                StudentSqlLite student5 = new StudentSqlLite();
                student5.setFirstName("StudentName5");
                student5.setLastName("StudentLastName5");
                student5.setEmail("Email5@gmail.com");
                student5.setVerify(true);
                student5.setSeriesIDcard("BA12345675");
                student5.setPassword("qwerty5%");

                addStudent(db, student5);

                StudentSqlLite student6 = new StudentSqlLite();
                student6.setFirstName("StudentName6");
                student6.setLastName("StudentLastName6");
                student6.setEmail("Email6@gmail.com");
                student6.setVerify(true);
                student6.setSeriesIDcard("BA12345676");
                student6.setPassword("qwerty7%");

                addStudent(db, student6);

                StudentSqlLite student7 = new StudentSqlLite();
                student7.setFirstName("StudentName7");
                student7.setLastName("StudentLastName7");
                student7.setEmail("Email7@gmail.com");
                student7.setVerify(true);
                student7.setSeriesIDcard("BA12345677");
                student7.setPassword("qwerty7%");

                addStudent(db, student7);

                StudentSqlLite student8 = new StudentSqlLite();
                student8.setFirstName("StudentName8");
                student8.setLastName("StudentLastName8");
                student8.setEmail("Email8@gmail.com");
                student8.setVerify(true);
                student8.setSeriesIDcard("BA12345678");
                student8.setPassword("qwerty8%");

                addStudent(db, student8);


                StudentSqlLite student9 = new StudentSqlLite();
                student9.setFirstName("StudentName9");
                student9.setLastName("StudentLastName9");
                student9.setEmail("Email9@gmail.com");
                student9.setVerify(true);
                student9.setSeriesIDcard("BA12345679");
                student9.setPassword("qwerty9%");

                addStudent(db, student9);

                StudentSqlLite student10 = new StudentSqlLite();
                student10.setFirstName("StudentName10");
                student10.setLastName("StudentLastName10");
                student10.setEmail("Email10@gmail.com");
                student10.setVerify(true);
                student10.setSeriesIDcard("BA12345670");
                student10.setPassword("qwerty10%");

                addStudent(db, student10);


            }



        List<StudentSqlLite> userList = db.studentDao().getAll();
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
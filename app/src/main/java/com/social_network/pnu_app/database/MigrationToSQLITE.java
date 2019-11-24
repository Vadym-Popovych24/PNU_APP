package com.social_network.pnu_app.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.entity.Student;

public class MigrationToSQLITE {
    static DataSnapshot snapshotInit;
    public static void CreateSnapshot(){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("students");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public DataSnapshot onDataChange(@NonNull DataSnapshot dataSnapshot) {
               snapshotInit = dataSnapshot;
               return dataSnapshot;
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private static Student addStudent(final AppDatabase db, Student student) {
        db.studentDao().insertAll(student);
        return student;
    }

    public static void addDatatoSqliteFromFirebase(final AppDatabase db) {


        Student studentObj = new Student();

        for (DataSnapshot student : snapshotInit.getChildren()) {

            for (DataSnapshot field : student.getChildren()) {

                if (field.getKey().toString().equals("name"))
                    studentObj.setFirstName(field.getValue().toString());

                if (field.getKey().toString().equals("last_name"))
                    studentObj.setLastName(field.getValue().toString());

                if (field.getKey().toString().equals("aSeriesIDcard"))
                    studentObj.setSeriesIDcard(field.getValue().toString());

                if (field.getKey().toString().equals("email"))
                    studentObj.setEmail(field.getValue().toString());

                if (field.getKey().toString().equals("password"))
                    studentObj.setPassword(field.getValue().toString());

                if (field.getKey().toString().equals("phone"))
                    studentObj.setPhone(field.getValue().toString());

                if (field.getKey().toString().equals("verify")) {

                    if (field.getValue().toString().equals("1")) {
                        studentObj.setVerify(true);
                    } else {
                        studentObj.setVerify(false);
                    }

                }


            }
            addStudent(db, studentObj);
        }
    }

    }
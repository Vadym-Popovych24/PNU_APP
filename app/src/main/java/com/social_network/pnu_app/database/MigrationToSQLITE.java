package com.social_network.pnu_app.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.entity.Student;

public class MigrationToSQLITE {

    public static void addDatatoSqliteFromFirebase(final AppDatabase db){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("students");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Student studentObj = new Student();
                String valueIDcardDB;
                String fieldValue;
                String serie = null;


                for (DataSnapshot student : dataSnapshot.getChildren()) {

                    for (DataSnapshot field : student.getChildren()) {

                        fieldValue=field.getValue().toString();


                            if (field.getKey().toString().equals("aSeriesIDcard") &&
                                    ( db.studentDao().getSeriesIDcard(fieldValue)== null) ){
                                studentObj.setSeriesIDcard(fieldValue);
                                serie = db.studentDao().getSeriesIDcard(fieldValue);
                            }

                            if (field.getKey().toString().equals("name") &&
                                    (db.studentDao().getFirstName(fieldValue)== null))
                                studentObj.setFirstName(fieldValue);

                            if (field.getKey().toString().equals("last_name")&&
                                    (db.studentDao().getLastName(fieldValue)== null))
                                studentObj.setLastName(fieldValue);

                            if (field.getKey().toString().equals("email") &&
                                    (db.studentDao().getEmail(fieldValue) == null))
                                studentObj.setEmail(fieldValue);

                            if (field.getKey().toString().equals("password") &&
                                    (db.studentDao().getPassword(fieldValue) == null))
                                studentObj.setPassword(fieldValue);

                            if (field.getKey().toString().equals("verify") &&
                                    (db.studentDao().getVerify(fieldValue) == null)) {

                                if (field.getValue().toString().equals("1")) {
                                    studentObj.setVerify("1");
                                } else {
                                    studentObj.setVerify("0");
                                }

                            }


                    /*     if (field.getKey().toString().equals("phone") &&
                                (db.studentDao().getPhone(fieldValue) == null ))
                            studentObj.setPhone(fieldValue); */




                    }
                 addStudent(db, studentObj);
                }
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


    }
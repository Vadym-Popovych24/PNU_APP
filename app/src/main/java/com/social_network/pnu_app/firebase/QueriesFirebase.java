package com.social_network.pnu_app.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.registration.Registration;

import java.util.HashMap;
import java.util.Map;

public class QueriesFirebase {
   public static Object idserie;
    ValueEventListener valueEventListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("students");
    Registration regObj = new Registration();

    public void getSeriesIDcardFB(String seriesIDcard) {

        Query querySeriesIDcard = database.getReference("students")
                .orderByChild("seriesIDcard")
                .equalTo("BA11582136");

        querySeriesIDcard.addListenerForSingleValueEvent(valueEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap student = null;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        student = (HashMap) snapshot.getValue();
                    }
                    idserie = student.get("seriesIDcard");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public void addStudent(){

        String id = reference.push().getKey();

        Student newStudent = new Student(false, "BA115821901", "Petro", "Golovetsky", 4,
                "", "", "");

        Map<String, Object> studentValues = newStudent.toMap();

        Map<String, Object> student = new HashMap<>();
        student.put(id, studentValues);

        reference.updateChildren(student);

    }
}

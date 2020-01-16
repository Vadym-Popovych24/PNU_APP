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

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");

  static int counter = 5;

    public void addStudent(){

        String id = reference.push().getKey();

        Student newStudent = new Student(false, "BA115821901", "Petro", "Golovetsky", counter++,
                "", "", "");

        Map<String, Object> studentValues = newStudent.toMap();

        Map<String, Object> student = new HashMap<>();
        student.put(id, studentValues);

        reference.updateChildren(student);

    }
}
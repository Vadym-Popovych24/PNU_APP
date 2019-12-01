
package com.social_network.pnu_app.registration;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.social_network.pnu_app.entity.Student;
//import com.social_network.pnu_app.signin.SignIn;

import java.util.HashMap;
import java.util.Map;


public class Registration extends AppCompatActivity {

    MaterialEditText IDcardField;
    MaterialEditText EmailField;
    MaterialEditText PassField;
    MaterialEditText ConfirmPassField;

    FirebaseDatabase database;
    DatabaseReference reference;
 public static String KeyStudent;

    TextView ExampleText;

    String ErrorText = null;
    boolean verify = false;

    Button btnRegister;

   private String valueIDcardField;

   public String getValueIDcardField(){
       return valueIDcardField;
   }
    String valueEmailField;
    String valuePassField;
    String valueConfirmPassField;
    String KeyStd;


    private String valueIDcardDB;
    private static int valueIDSeriesIDcard;

    public static int getValueIDSeriesIDcard() {
        return valueIDSeriesIDcard;
    }

    public boolean valueVerify;

    boolean error = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        verifycationData();

         //getDataFromFirebase();

    }


    public void alertErrorReg(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(Registration.this);
        a_builder.setMessage(ErrorText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("PERFORMANCE");
        alert.show();
    }

    public String encryptionPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xDF) | 0x104).substring(1, 3));  // 0xFF | 0x100
            }
            password = sb.toString();
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }

        return password;
    }

    public boolean verifycationData(){

        btnRegister = findViewById(R.id.btnRegister2);

        View.OnClickListener btnRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initFieldInput();

                if (    verifycationSeriesIDcard() == false &&
                        verifycationEmail() == false &&
                        verifycationPassword() == false &&
                        verifycationConfirmPassword() == false &&
                verifycationSeriesIDcardOnExists() == false){


                        // valuePassField = encryptionPassword(valuePassField);  TODO this coderow encrypt password in database (comment for example)


                /*    reference.child(KeyStd).child("email").setValue(valueEmailField);
                    reference.child(KeyStd).child("password").setValue(valuePassField);
                    reference.child(KeyStd).child("verify").setValue(1);*/

                        ErrorText = "SUCCESS REGISTRATION " + "valueIDSeriesIDcard = " + String.valueOf(valueIDSeriesIDcard) +
                        " valueVerify = " + String.valueOf(valueVerify);
                        alertErrorReg();

                //    }


                    // TODO sign in

                }
                else {

                    alertErrorReg();
                }

            }
        };

        btnRegister.setOnClickListener(btnRegisterListener);

        return error;
    }
    DataSnapshot snapshot2;

    public void initFieldInput(){

        IDcardField = findViewById(R.id.SeriesAndNumField);
        valueIDcardField = (String.valueOf(IDcardField.getText())).trim();

        EmailField = findViewById(R.id.emailField);
        valueEmailField = (String.valueOf(EmailField.getText())).trim();

        PassField = findViewById(R.id.passFieldReg);
        valuePassField = (String.valueOf(PassField.getText())).trim();

        ConfirmPassField = findViewById(R.id.confirmPassField);
        valueConfirmPassField = (String.valueOf(ConfirmPassField.getText())).trim();


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("students");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference reference2 = database2.getReference("students");

        Iterable<DataSnapshot> adas = snapshot2.getChildren();

        for (DataSnapshot d : snapshot2.getChildren()) {
            for (DataSnapshot fieldd : d.getChildren()) {
                ExampleText.append("fieldd = " + fieldd.getValue().toString() + "\n");
            }
        }
        String query = String.valueOf(reference2.child("student1").getKey());

     //   ExampleText.append("adas = " + adas.toString() + "\n");
    }


    public boolean verifycationSeriesIDcard(){


        boolean resultSeriesIDcard = valueIDcardField.matches("^[A-Z]{2}([0-9]){8}$");

        if (resultSeriesIDcard) {
            error = false;
        }
        else {
            error=true;
            ErrorText = "Enter the correct Series and Number ID card(example-ВА12345678): should be the first two upper letters and 8 digits";
        }

        return error;
    }
    Map<String, Object> studentValues;
    Map<String, Object> student = new HashMap<>();
    String idStudent;

   /* public void getDataFromFirebase(){
     database = FirebaseDatabase.getInstance();
     reference = database.getReference("students");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Student registerDataStudent = new Student();


                String aseriesIDcard = null;
                String name = null;
                String LastName = null;
                int id = 0;
                String email = null;
                String password = null;
                String phone = null;

                String value;
                String key;
                boolean bverify = false;

                Registration regObj = new Registration();

                ExampleText = findViewById(R.id.ExampleText);
                String SeriesIDcard;

              //  String passwordb = db.studentDao().getPasswordById(valueIDSeriesIDcard);
              //  String emailb = db.studentDao().getEmailById(valueIDSeriesIDcard);

                for (DataSnapshot snapshotStudent : dataSnapshot.getChildren()) {

                    idStudent = snapshotStudent.getKey();
                    for (DataSnapshot field : snapshotStudent.getChildren()) {
                        value = field.getValue().toString();
                        key = field.getKey();

                        if (key.equals("bverify")) {//&&(KeyStudent.equals(snapshotStudent.getKey().toString())) )
                            //reference.child(KeyStudent).child("verify").setValue(1);
                            bverify = Boolean.parseBoolean(value);
                        }
                        if (key.equals("aseriesIDcard") && (value.equals(valueIDcardField))) {
                         KeyStudent = snapshotStudent.getKey().toString();
                        }
                        if (key.equals("aseriesIDcard")) {
                            aseriesIDcard = value;
                         //   KeyStudent = snapshotStudent.getKey().toString();
                            //  ExampleText.append(student.getKey().toString() + "\n");

                        }

                        if (key.equals("email")) { // && (KeyStudent.equals(snapshotStudent.getKey().toString())) ){
                            //  ExampleText.append("email = " + email + "\n");
                            email = value;
                            // reference.child(KeyStudent).child("email").setValue(email);
                        }

                        if (key.equals("id")) { //  && (KeyStudent.equals(snapshotStudent.getKey().toString())) ){
                            //  ExampleText.append("email = " + email + "\n");
                            id = Integer.parseInt(value);
                            // reference.child(KeyStudent).child("email").setValue(email);

                        }

                        if (key.equals("name")) { // && (KeyStudent.equals(snapshotStudent.getKey().toString())) ){
                            //  ExampleText.append("email = " + email + "\n");
                            name = value;
                            // reference.child(KeyStudent).child("email").setValue(email);

                        }

                        if (key.equals("last_name")) { // && (KeyStudent.equals(snapshotStudent.getKey().toString())) ){
                            //  ExampleText.append("email = " + email + "\n");
                            LastName = value;
                            // reference.child(KeyStudent).child("email").setValue(email);

                        }


                        if (key.equals("password")) {//&&(KeyStudent.equals(snapshotStudent.getKey().toString())) ) {
                            // ExampleText.append("password + " + password + "\n");
                            // reference.child(KeyStudent).child("password").setValue(password);
                            password = value;
                        }

                        if (key.toString().equals("phone")) {
                            //     studentObj.setPhone(field.getValue().toString());
                            phone = value;
                        }

                    }
                    registerDataStudent = new Student(bverify,aseriesIDcard, name, LastName, id, email, password, phone);
                    studentValues = registerDataStudent.toMap();
                    student.put(idStudent, studentValues);
                   ExampleText.append(student.values().toString());
                    student.containsValue(valueIDcardField);

                }
                System.out.println(student.values().toString());
                boolean b = student.containsValue("BA11582183");
                System.out.println("Contains seriesIDcard = " + b);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

*/

    Student st;
    public boolean verifycationSeriesIDcardOnExists() {

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("students");
        boolean eq= false;

      //  DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("students");
     //   Query query = rootRef.child("student1");
      //  query.orderByKey().orderByValue();
     //  eq = query.equals(true);

           reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
snapshot2 = dataSnapshot;

             /*   String value=null;
                String key;

                ExampleText = findViewById(R.id.ExampleText);
                boolean wasChanged = false;
                error = false;
                String studentExist = null;
                String registered = null;
                for (DataSnapshot snapshotStudent : dataSnapshot.getChildren()) {

                    for (DataSnapshot field : snapshotStudent.getChildren()) {

                        value = field.getValue().toString();
                        key = field.getKey().toString();

                            if (key.equals("aseriesIDcard") && (value.equals(valueIDcardField))) {
                                studentExist = value;
                                KeyStudent = snapshotStudent.getKey().toString();
                                KeyStd = KeyStudent;
                                ExampleText.append("KeyStudent = " + KeyStudent + "\n");
                                //  ExampleText.append("seriesIDcard = " + valueIDcardField);
                                error = false;
                            }

                             else if (key.equals("bverify") &&
                                    (KeyStudent.equals(snapshotStudent.getKey().toString()))
                                    && !value.equals("1")) {
                                reference.child(KeyStudent).child("email").setValue(valueEmailField);
                                reference.child(KeyStudent).child("password").setValue(valuePassField);
                                reference.child(KeyStudent).child("bverify").setValue(1);
                                error = false;
                                wasChanged = true;

                            }  if (key.equals("bverify") &&
                                    (KeyStudent.equals(snapshotStudent.getKey().toString()))
                                    && value.equals("1")) {
                                registered = "1";

                                error = true;
                            }


                    }
                }
                if (studentExist == null){
                    error = true;
                    ErrorText = "Student with such id series does not exist";
                   ExampleText.append("Student with such id series does not exist");
                    ExampleText.append("value = " + value + "\n");
                  //  ExampleText.append("valueIDcardField = " + valueIDcardField + "\n");
                  //  alertErrorReg();
                }
                if (registered=="1" && wasChanged == true) {
                    error = true;
                      ExampleText.append("Student already registered " + value);
                    ErrorText = "Student already registered ";
                  //  alertErrorReg();
                }*/

                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            return error;
    }

    public boolean verifycationEmail(){

        boolean resultEmail = valueEmailField.matches("[a-zA-Z0-9]{1,32}[@]{1}[a-zA-Z]{2,32}[.]{1}+[a-z]{2,7}");

        if (resultEmail) {
            error = false;
        }
        else {
            error=true;
            ErrorText = "Enter the correct email adress";
        }

        return error;

    }

    public boolean verifycationPassword(){


        boolean resultPass = valuePassField.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^.;:&+=])(?=\\S+$).{8,32}$");

        if (resultPass) {
            error = false;
        }
        else {
            error=true;
            ErrorText = "Enter the correct password: The password must be at least 8 characters long and contain 1 digit " +
                    "and 1 letter can also contain uppercase letters or such characters - (@ # $% ^ _-.;: & + =)";
        }
        return error;

    }

    public boolean verifycationConfirmPassword(){

        boolean resultConfirmPass = valueConfirmPassField.equals(valuePassField) ? true : false;

        if (resultConfirmPass) {
            error = false;
        }
        else {
            error=true;
            ErrorText = "Passwords isn't equals.Password must be same.";
        }
        return error;
    }






}


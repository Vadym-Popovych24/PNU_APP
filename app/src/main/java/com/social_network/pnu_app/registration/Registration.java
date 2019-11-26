
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
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.database.AppDatabase;
import com.social_network.pnu_app.entity.Student;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.social_network.pnu_app.signin.SignIn;


public class Registration extends AppCompatActivity {

    MaterialEditText IDcardField;
    MaterialEditText EmailField;
    MaterialEditText PassField;
    MaterialEditText ConfirmPassField;

    TextView ExampleText;

    String ErrorText = null;

    Button btnRegister;

   private String valueIDcardField;

   public String getValueIDcardField(){
       return valueIDcardField;
   }
    String valueEmailField;
    String valuePassField;
    String valueConfirmPassField;


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
        verifycationData(AppDatabase.getAppDatabase(Registration.this));

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

    public boolean verifycationData(final AppDatabase db){


        btnRegister = findViewById(R.id.btnRegister2);
        IDcardField = findViewById(R.id.SeriesAndNumField);
        EmailField =findViewById(R.id.emailField);
        PassField = findViewById(R.id.passFieldReg);
        ConfirmPassField= findViewById(R.id.confirmPassField);

        View.OnClickListener btnRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifycationConfirmPassword();
                verifycationPassword();
                verifycationEmail();
                verifycationSeriesIDcard();

                if (    verifycationSeriesIDcard() == false &&
                        verifycationSeriesIDcardOnExists(AppDatabase.getAppDatabase(Registration.this)) == false &&
                        verifycationEmail() == false &&
                        verifycationPassword() == false &&
                        verifycationConfirmPassword() == false){

                    valueIDcardField = String.valueOf(IDcardField.getText()).trim();
                    valueIDcardDB = db.studentDao().getSeriesIDcard(valueIDcardField);
                    valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardDB);
                    valueVerify =db.studentDao().getVerifyByID(valueIDSeriesIDcard);


                    if (valueIDcardDB.equals(valueIDcardField) &&  valueVerify == false ) {

                        // valuePassField = encryptionPassword(valuePassField);  TODO this coderow encrypt password in database (comment for example)

                        db.studentDao().updateVerify(true, valueIDSeriesIDcard);
                        db.studentDao().setPassword(valuePassField, valueIDSeriesIDcard);
                        db.studentDao().setEmail(valueEmailField , valueIDSeriesIDcard);


                        addDatatoFirebaseFromSqlite(AppDatabase.getAppDatabase(Registration.this));

                        ErrorText = "SUCCESS REGISTRATION " + "valueIDSeriesIDcard = " + String.valueOf(valueIDSeriesIDcard) +
                        " valueVerify = " + String.valueOf(valueVerify);
                        alertErrorReg();

                    }
                    else if(valueVerify == true){
                        ErrorText = "Student already registered";
                        alertErrorReg();
                    }


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

    public boolean verifycationSeriesIDcard(){


        IDcardField = findViewById(R.id.SeriesAndNumField);
        valueIDcardField = (String.valueOf(IDcardField.getText())).trim();

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


    public  void addDatatoFirebaseFromSqlite(final AppDatabase db){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("students");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Registration regObj = new Registration();

                Student studentObj = new Student();
                ExampleText = findViewById(R.id.ExampleText);
                String SeriesIDcard;
                String KeyStudent = "default";

                String password =db.studentDao().getPasswordById(valueIDSeriesIDcard);
                String email = db.studentDao().getEmailById(valueIDSeriesIDcard);

                for(DataSnapshot student : dataSnapshot.getChildren()){

                    for (DataSnapshot field : student.getChildren()) {

                        if ( (field.getKey().toString().equals("aSeriesIDcard") &&
                                (field.getValue().toString().equals(valueIDcardField)) )) {
                            KeyStudent = student.getKey().toString();
                            //  ExampleText.append(student.getKey().toString() + "\n");

                        }

                        if ( (field.getKey().toString().equals("email")) &&
                                (KeyStudent.equals(student.getKey().toString())) ){
                            //  ExampleText.append("email = " + email + "\n");
                            reference.child(KeyStudent).child("email").setValue(email);

                        }

                        if ( (field.getKey().toString().equals("password")) &&
                                (KeyStudent.equals(student.getKey().toString())) ) {
                            // ExampleText.append("password + " + password + "\n");
                            reference.child(KeyStudent).child("password").setValue(password);
                        }

                        //   if (field.getKey().toString().equals("phone"))
                        //     studentObj.setPhone(field.getValue().toString());

                        if ( (field.getKey().toString().equals("verify")) &&
                                (KeyStudent.equals(student.getKey().toString())) )
                            reference.child(KeyStudent).child("verify").setValue(1);




                    }
                    // addStudent(db, studentObj);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public boolean verifycationSeriesIDcardOnExists(final AppDatabase db) {

        IDcardField = findViewById(R.id.SeriesAndNumField);
        valueIDcardField = (String.valueOf(IDcardField.getText())).trim();

        valueIDcardDB = db.studentDao().getSeriesIDcard(valueIDcardField);


        if (valueIDcardDB == null) {
                error = true;
            ErrorText = "Student with such id series does not exist";
            }
        else {
                error = false;
            }

            return error;

    }

    public boolean verifycationEmail(){

        EmailField = findViewById(R.id.emailField);
        valueEmailField = (String.valueOf(EmailField.getText())).trim();
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

        PassField = findViewById(R.id.passFieldReg);
        valuePassField = (String.valueOf(PassField.getText())).trim();
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

        ConfirmPassField = findViewById(R.id.confirmPassField);
        valueConfirmPassField = (String.valueOf(ConfirmPassField.getText())).trim();
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


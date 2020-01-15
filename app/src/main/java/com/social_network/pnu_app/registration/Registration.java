
package com.social_network.pnu_app.registration;


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
import com.social_network.pnu_app.firebase.QueriesFirebase;
//import com.social_network.pnu_app.signin.SignIn;

import java.util.HashMap;


public class Registration extends AppCompatActivity {


    MaterialEditText IDcardField;
    MaterialEditText EmailField;
    MaterialEditText PassField;
    MaterialEditText ConfirmPassField;

    public TextView ExampleText;

    String ErrorText = null;

    Button btnRegister;

    private String valueIDcardField;
    String valueEmailField;
    String valuePassField;
    String valueConfirmPassField;

    QueriesFirebase fb = new QueriesFirebase();

    private String valueIDcardDB;
    private static int valueIDSeriesIDcard;

    public boolean FBverify;
    public static String FBidSerie;
    public String FBpassword;
    public String FBemail;
    public String FBid;

    boolean error = true;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        verifycationData();

        ExampleText = findViewById(R.id.ExampleText);

    }
    ValueEventListener valueEventListener;

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

    public void queryFB(){
        initFieldInput();
        Query querySeriesIDcard = FirebaseDatabase.getInstance().getReference("students")
                .orderByChild("seriesIDcard")
                .equalTo(valueIDcardField);

        querySeriesIDcard.addListenerForSingleValueEvent(valueEventListener);
    }
    String KeyStudent;

    public boolean verifycationData(){
        btnRegister = findViewById(R.id.btnRegister2);

        View.OnClickListener btnRegisterListener = new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                initFieldInput();
                  //   fb.addStudent();


                if (    verifycationSeriesIDcard() == false &&
                        verifycationEmail() == false &&
                        verifycationPassword() == false &&
                        verifycationConfirmPassword() == false){

                    valueEventListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap student = null;
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    student = (HashMap) snapshot.getValue();

                                    FBidSerie = student.get("seriesIDcard").toString();
                                    FBverify = (boolean) student.get("verify");
                                    FBid = student.get("id").toString();

                                    KeyStudent = snapshot.getKey();


                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    queryFB();
                                if (FBidSerie != null ) {

                                    if (FBidSerie.equals(valueIDcardField) && FBverify == false) {


                                        ErrorText = "FBidSerie = " + FBidSerie +
                                                "FBverify = " + FBverify +
                                                "KeyStudent = " + KeyStudent;
                                        alertErrorReg();
                                        // valuePassField = encryptionPassword(valuePassField);  TODO this coderow encrypt password in database (comment for example)


                                        reference.child(KeyStudent).child("email").setValue(valueEmailField);
                                        reference.child(KeyStudent).child("password").setValue(valuePassField);
                                        reference.child(KeyStudent).child("verify").setValue(true);

                                        ErrorText = "SUCCESS REGISTRATION " + "valueIDSeriesIDcard = " + String.valueOf(FBid) +
                                                " FBverify = " + String.valueOf(FBverify);
                                        alertErrorReg();

                                    } else if (FBverify == true && FBidSerie.equals(valueIDcardField)) {
                                        ErrorText = "Student already registered";
                                        alertErrorReg();
                                    }
                                    else
                                        ErrorText = "Student with such id series does not exist";
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

    public void initFieldInput(){

        IDcardField = findViewById(R.id.SeriesAndNumField);
        valueIDcardField = (String.valueOf(IDcardField.getText())).trim();

        EmailField = findViewById(R.id.emailField);
        valueEmailField = (String.valueOf(EmailField.getText())).trim();

        PassField = findViewById(R.id.passFieldReg);
        valuePassField = (String.valueOf(PassField.getText())).trim();

        ConfirmPassField = findViewById(R.id.confirmPassField);
        valueConfirmPassField = (String.valueOf(ConfirmPassField.getText())).trim();



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

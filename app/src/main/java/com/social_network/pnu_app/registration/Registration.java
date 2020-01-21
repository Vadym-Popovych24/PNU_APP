
package com.social_network.pnu_app.registration;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

    Button idBtnRegister;

    private String valueIDcardField;
    static String valuePhoneField;
    static String valuePassField;
    String valueConfirmPassField;

    QueriesFirebase fb = new QueriesFirebase();

    private String valueIDcardDB;
    private static int valueIDSeriesIDcard;

    public boolean FBverify;
    public static String FBidSerie ="";
    public String FBpassword = "";
    public String FBemail = "";
    public static Object FBid = 0;

    boolean error = true;


    static String KeyStudent ="default";
    HashMap<Object, Object> student = new HashMap();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ExampleText = findViewById(R.id.ExampleText);

        verifycationData();

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


    public HashMap<Object, Object> getStudentFB(){

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //       if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    student = (HashMap) snapshot.getValue();
                    KeyStudent = snapshot.getKey();

                }

                FBidSerie = (String) student.get("seriesIDcard");
                FBid =  student.get("id");
                try {
                    FBverify = (boolean) student.get("verify");
                    ExampleText.setText("value FBverif = " + FBverify + "\n"+ "KeyStudent = " + KeyStudent + "\n"
                    + "FBidSerie = " + FBidSerie + "\n");
                }
                catch (Exception castToBool){
                        FBverify = false;
                        ExampleText.setText("value FBverif catch= " + FBverify + "\n" +
                                "Database Error! " + "\n" +
                                "Catch KeyStudent = " + KeyStudent + "\n" +
                                "Catch FBidSerie = " + FBidSerie + "\n");
                        ErrorText = "Error Database, please try again!";
                }

                if (FBidSerie != null) {

                    if (FBverify == true && FBidSerie.equals(valueIDcardField)) {
                        ErrorText = "Student already registered";
                        alertErrorReg();

                    } else if (!FBidSerie.equals(valueIDcardField)) {
                        ErrorText = "Student with such id series does not exist";
                        alertErrorReg();

                    }
                    else if (FBidSerie.equals(valueIDcardField) && FBverify == false) {


                        // valuePassField = encryptionPassword(valuePassField);  TODO this coderow encrypt password in database (comment for example)

                        valuePhoneField = valuePhoneField.replaceAll(" ", "");

                      /*  reference.child(KeyStudent).child("phone").setValue(valuePhoneField);
                        reference.child(KeyStudent).child("password").setValue(valuePassField);
                        reference.child(KeyStudent).child("verify").setValue(true);*/
                      //  FBverify = true;

                        Intent intentFromRegistration = new Intent("com.social_network.pnu_app.registration.PhoneAuthentication");
                        startActivity(intentFromRegistration);

    /*                    ExampleText.setText('\n'  + "SUCCESS REGISTRATION " + " valueIDSeriesIDcard = " + (FBidSerie) +
                                " FBverify = " + (FBverify) + " FBid = " + FBid + "KeyStudnt = " + KeyStudent  + '\n' );*/

                    }
                }

                else{
                    ErrorText = "Student with such id series does not exist";
                     alertErrorReg();
                }

                // TODO sign in

            }


            // }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return student;
    }

    public boolean verifycationData(){

        idBtnRegister = findViewById(R.id.btnRegister);


        View.OnClickListener btnRegisterListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initFieldInput();
                  //   fb.addStudent();
                getStudentFB();

                if (    verifycationSeriesIDcard() == false &&
                        verifycationPhone() == false &&
                        verifycationPassword() == false &&
                        verifycationConfirmPassword() == false){

                    queryFB();
                }

                else {
                    alertErrorReg();
                }

            }
        };

        idBtnRegister.setOnClickListener(btnRegisterListener);

        return error;
    }

    public void initFieldInput(){


        IDcardField = findViewById(R.id.SeriesAndNumField);
        valueIDcardField = (String.valueOf(IDcardField.getText())).trim();

        EmailField = findViewById(R.id.phoneField);
        valuePhoneField = (String.valueOf(EmailField.getText())).trim();

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


    public boolean verifycationPhone(){

        boolean resultEmail = valuePhoneField.matches("^\\+?(?:[0-9] ?){9,11}[0-9]$");

        if (resultEmail) {
            error = false;
        }
        else {
            error=true;
            ErrorText = "Enter the correct phone number example(+380751334582)";
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

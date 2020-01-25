package com.social_network.pnu_app.signin;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.firebase.QueriesFirebase;
import com.social_network.pnu_app.registration.Registration;
import com.social_network.pnu_app.R;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;


public class SignIn extends AppCompatActivity {

    Button btnSignIn;
    MaterialEditText IDcardField;
    MaterialEditText passField;
    String valueIDcardField;

    private String yourLastName;
    private String yourName;

    private String valueIDcardDB;
    String valuePassField="";
    public String ErrorText=null;

    boolean error = true;

    public static int valueIDSeriesIDcard;
    private int valueIDPassword;

    private String valuePassDB="";

    static ValueEventListener valueEventListener;

    public boolean FBverify;
    public static String FBidSerie ="";
    public String FBpassword = "1";
    public String FBemail = "";
    public static Object FBid = 0;
    public String FBName;
    public String FBLastName;

    String KeyStudent ="default";
    QueriesFirebase qf = new QueriesFirebase();
   static HashMap<Object, Object> student = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        verifycationStudentIn();
    }

    public void alertErrorSign(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(SignIn.this);
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

/*
    public boolean verifyRegistered(final AppDatabase db){

        valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardField);
        boolean valueVerify= false;
        valueVerify = db.studentDao().getVerifyByID(valueIDSeriesIDcard);
        return valueVerify;
    }

   public boolean verifySignInSeriesIDcard(final AppDatabase db){


        valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardField);
        valueIDcardDB = db.studentDao().getSeriesIDcard(valueIDcardField);

        error = (valueIDcardDB != null) && valueIDcardDB.equals(valueIDcardField);

        return error;
    }

   public boolean verifySignInPassword(final AppDatabase db){


        valueIDPassword = db.studentDao().getIdstudentByIDPassword(valuePassField,valueIDSeriesIDcard);
        valuePassDB = db.studentDao().getPasswordById(valuePassField,valueIDSeriesIDcard);


        error = (valuePassDB != null) && valuePassDB.equals(valuePassField);

        return error;
    }*/

    public void initFieldInput(){

        IDcardField = findViewById(R.id.IDcardField);
        valueIDcardField = String.valueOf(IDcardField.getText()).trim();

        passField = findViewById(R.id.passFieldSignIn);
        valuePassField = String.valueOf(passField.getText()).trim();

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
            Intent intentFromSignIn;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //       if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    student = (HashMap) snapshot.getValue();
                    KeyStudent = snapshot.getKey();

                }

                FBidSerie = (String) student.get("seriesIDcard");
                FBpassword = (String) student.get("password");
                FBid = student.get("id");

                Registration encrypt = new Registration();
                valuePassField = encrypt.encryptionPassword(valuePassField); // TODO this coderow encrypt password in database (comment for example)

                try {
                    FBverify = (boolean) student.get("verify");
                } catch (Exception castToBool) {
                    FBverify = false;
                }
                if ((FBidSerie != null && FBpassword != null)) {

                 if (!FBidSerie.equals(valueIDcardField)) {
                    ErrorText = "Student with the such series id does not exist";
                    alertErrorSign();
                } else if (FBidSerie.equals(valueIDcardField) && (FBverify != true)) {
                    ErrorText = "IDPassword = " + valueIDPassword +
                            "IDSeriesIDCard = " + valueIDSeriesIDcard;
                    ErrorText = "Student with the such series id does not registered";
                    alertErrorSign();
                }  else if (!(FBpassword.equals(valuePassField)) && FBverify == true) { // TODO
                    ErrorText = "Wrong password";
                    alertErrorSign();
                }


                    if (FBverify == true && FBidSerie.equals(valueIDcardField) && FBpassword.equals(valuePassField)) {
                        FBName = (String) student.get("name");
                        FBLastName = (String) student.get("lastName");

                        Student.student = student;



                        intentFromSignIn = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                        startActivity(intentFromSignIn);

                    }
                } else {
                    ErrorText = "Student with such id series does not exist 2";
                    alertErrorSign();
                }

                // TODO sign in

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return student;
    }




    public void verifycationStudentIn(){

        btnSignIn = findViewById(R.id.btnSignIn);


        View.OnClickListener listenerSignIn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent intentFromSignIn;
                initFieldInput();
                getStudentFB();
                if (view.getId() == R.id.btnSignIn) {

                    if (valueIDcardField.isEmpty()) { // TODO
                        ErrorText = "Enter Series ID";
                        alertErrorSign();
                    } else if (valuePassField.isEmpty()) { // TODO
                        ErrorText = "Enter password";
                        alertErrorSign();
                    } else {
                        if (verifycationSeriesIDcard() == false &&
                                verifycationPassword() == false) {
                            queryFB();
                        }
                        else {
                            alertErrorSign();
                        }
                    }
          /*             if (verifySignInSeriesIDcard(AppDatabase.getAppDatabase(SignIn.this)) &&
                               verifySignInPassword(AppDatabase.getAppDatabase(SignIn.this)) &&
                               (valueIDSeriesIDcard == valueIDPassword)) {

                           getYourName(AppDatabase.getAppDatabase(SignIn.this));
                           getYourLastName(AppDatabase.getAppDatabase(SignIn.this));

                           ErrorText = getYourName() + " " + getYourLastName(yourLastName);
                           alertErrorSign();


                           intentFromSignIn = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                           startActivity(intentFromSignIn);

                   }
                       else if (valueIDcardField.isEmpty() ) { // TODO
                           ErrorText = "Enter Series ID";
                           alertErrorSign();
                       }
                       else if (!verifySignInSeriesIDcard(AppDatabase.getAppDatabase(SignIn.this))) {
                           ErrorText = "Student with the such series id does not exist";
                           alertErrorSign();
                       }
                       else if (verifySignInSeriesIDcard(AppDatabase.getAppDatabase(SignIn.this)) &&
                               !(verifyRegistered(AppDatabase.getAppDatabase(SignIn.this)) ) ) {
                       ErrorText = "IDPassword = " + String.valueOf(valueIDPassword) +
                               "IDSeriesIDCard = " + String.valueOf(valueIDSeriesIDcard);
                           ErrorText = "Student with the such series id does not registered";
                           alertErrorSign();
                       }
                       else if (valuePassField.isEmpty() ) { // TODO
                           ErrorText = "Enter password";
                           alertErrorSign();
                       }
                       else if ( (valuePassDB == null) && (!valuePassField.isEmpty()) ){ // TODO
                           ErrorText = "Wrong password";
                           alertErrorSign();
                       }
*/
                }
          /*     else {
                   ErrorText = "Wrong password";
                   alertErrorSign();
               }*/
            }

        };

        btnSignIn.setOnClickListener(listenerSignIn);

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

/*    public String getValueIDcardDB() {
        return valueIDcardDB;
    }

    public void setValueIDcardDB(String valueIDcardDB) {
        this.valueIDcardDB = valueIDcardDB;
    }
    public int getValueIDSeriesIDcard() {
        return valueIDSeriesIDcard;
    }

    public void setValueIDSeriesIDcard(int valueIDSeriesIDcard) {
        this.valueIDSeriesIDcard = valueIDSeriesIDcard;
    }
    public int getValueIDPassword() {
        return valueIDPassword;
    }

    public void setValueIDPassword(int valueIDPassword) {
        this.valueIDPassword = valueIDPassword;
    }

    public String getYourLastName(final AppDatabase db)
    {
        yourLastName = db.studentDao().getLastNameById(getValueIDSeriesIDcard());
        return yourLastName;
    }

    public void setYourLastName(String yourLastName) {
        this.yourLastName = yourLastName;
    }

    public Button getBtnSignIn() {
        return btnSignIn;
    }

    public void setBtnSignIn(Button btnSignIn) {
        this.btnSignIn = btnSignIn;
    }

    public String getYourName(final AppDatabase db)
    {
        yourName =db.studentDao().getFirstNameById(getValueIDSeriesIDcard());
        return yourName;
    }

    public void setYourName(String yourName) { this.yourName = yourName; }*/
    }



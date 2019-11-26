package com.social_network.pnu_app.signin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.social_network.pnu_app.registration.Registration;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.database.AppDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        verifyStudentIn(AppDatabase.getAppDatabase(SignIn.this));
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

    public boolean verifyRegistered(final AppDatabase db){

        valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardField);
        boolean valueVerify= false;
        valueVerify = db.studentDao().getVerifyByID(valueIDSeriesIDcard);
        return valueVerify;
    }

    public boolean verifySignInSeriesIDcard(final AppDatabase db){

        IDcardField = findViewById(R.id.IDcardField);

        valueIDcardField = String.valueOf(IDcardField.getText());
        valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardField);
        valueIDcardDB = db.studentDao().getSeriesIDcard(valueIDcardField);

        error = (valueIDcardDB != null) && valueIDcardDB.equals(valueIDcardField);

        return error;
    }

    public boolean verifySignInPassword(final AppDatabase db){

        Registration encrypt = new Registration();
        passField = findViewById(R.id.passFieldSignIn);

        valuePassField = String.valueOf(passField.getText());
        // valuePassField = encrypt.encryptionPassword(valuePassField); TODO this coderow encrypt password in database (comment for example)

        valueIDPassword = db.studentDao().getIdstudentByIDPassword(valuePassField,valueIDSeriesIDcard);
        valuePassDB = db.studentDao().getPasswordById(valuePassField,valueIDSeriesIDcard);


        error = (valuePassDB != null) && valuePassDB.equals(valuePassField);

        return error;
    }

    public void verifyStudentIn(final AppDatabase db){

        btnSignIn = findViewById(R.id.btnSignIn);


        View.OnClickListener listenerSignIn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFromSignIn;
                verifySignInSeriesIDcard(AppDatabase.getAppDatabase(SignIn.this));
                verifySignInPassword(AppDatabase.getAppDatabase(SignIn.this));

               if (view.getId() == R.id.btnSignIn) {
                       if (verifySignInSeriesIDcard(AppDatabase.getAppDatabase(SignIn.this)) &&
                               verifySignInPassword(AppDatabase.getAppDatabase(SignIn.this)) &&
                               (valueIDSeriesIDcard == valueIDPassword)) {

                           getYourName(AppDatabase.getAppDatabase(SignIn.this));
                           getYourLastName(AppDatabase.getAppDatabase(SignIn.this));

                      /*     ErrorText = getYourName() + " " + getYourLastName(yourLastName);
                           alertErrorSign();*/


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

               }
               else {
                   ErrorText = "Wrong password";
                   alertErrorSign();
               }
           }
        };

        btnSignIn.setOnClickListener(listenerSignIn);

            }

    public String getValueIDcardDB() {
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

    public void setYourName(String yourName) { this.yourName = yourName; }
    }


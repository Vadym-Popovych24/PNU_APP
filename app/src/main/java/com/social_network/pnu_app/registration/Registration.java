
package com.social_network.pnu_app.registration;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.social_network.pnu_app.R;
import com.social_network.pnu_app.database.AppDatabase;
import com.social_network.pnu_app.entity.Student;
import com.rengwuxian.materialedittext.MaterialEditText;


public class Registration extends AppCompatActivity {

    MaterialEditText IDcardField;
    MaterialEditText EmailField;
    MaterialEditText PassField;
    MaterialEditText ConfirmPassField;
    String ErrorText = null;


    Button btnRegister;
    TextView errorTextView, errorTextView2, errorTextView3, errorTextView4;

    String valueIDcardField;
    String valueEmailField;
    String valuePassField;
    String valueConfirmPassField;

    boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        verificationData(AppDatabase.getAppDatabase(Registration.this));

    }

    private static Student addStudent(final AppDatabase db, Student student) {
        db.studentDao().insertAll(student);
        return student;
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

    public boolean verificationData(final AppDatabase db){

        btnRegister = findViewById(R.id.btnRegister2);
        errorTextView = findViewById(R.id.ErrorText);
        errorTextView2 = findViewById(R.id.ErrorTextP2);
        errorTextView3 = findViewById(R.id.ErrorTextP3);
        errorTextView4 = findViewById(R.id.ErrorTextP4);

        View.OnClickListener btnRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificationConfirmPassword();
                verificationPassword();
                verificationEmail();
                verificationSeriesIDcard();

                if ( verificationSeriesIDcard() == false && verificationEmail() == false &&
                        verificationPassword() == false && verificationConfirmPassword() == false){

                    errorTextView.setTextColor(Color.GREEN);
                   // errorTextView.setText("All data is right");
                }
                else {
                    alertErrorReg();
                }

            }
        };

        btnRegister.setOnClickListener(btnRegisterListener);

        return error;
    }

    public boolean verificationSeriesIDcard(){


        IDcardField = findViewById(R.id.SeriesAndNumField);
        valueIDcardField = (String.valueOf(IDcardField.getText())).trim();

        boolean resultSeriesIDcard = valueIDcardField.matches("^[A-Z]{2}([0-9]){8}$");

        if (resultSeriesIDcard) {
            error = false;
            errorTextView.setText(valueIDcardField);

        }
        else {
            error=true;
            ErrorText = "Enter the correct Series and Number ID card(example-ВА12345678): should be the first two upper letters and 8 digits";
            errorTextView.setTextColor(Color.RED);
            errorTextView.setText(ErrorText);

        }

        return error;

    }

    public boolean verificationEmail(){

        EmailField = findViewById(R.id.emailField);
        valueEmailField = (String.valueOf(EmailField.getText())).trim();
        boolean resultEmail = valueEmailField.matches("[a-zA-Z0-9]{1,32}[@]{1}[a-zA-Z]{2,32}[.]{1}+[a-z]{2,7}");

        if (resultEmail) {
            error = false;
            errorTextView2.setText(valueEmailField);

        }
        else {
            error=true;
            ErrorText = "Enter the correct email adress";
            errorTextView.setTextColor(Color.RED);
            errorTextView.setText(ErrorText);
        }

        return error;

    }

    public boolean verificationPassword(){

        PassField = findViewById(R.id.passFieldReg);
        valuePassField = (String.valueOf(PassField.getText())).trim();
        boolean resultPass = valuePassField.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^.;:&+=])(?=\\S+$).{8,32}$");

        if (resultPass) {
            error = false;
            errorTextView3.setText(valuePassField);
        }
        else {
            error=true;
            ErrorText = "Enter the correct password: The password must be at least 8 characters long and contain 1 digit " +
                    "and 1 letter can also contain uppercase letters or such characters - (@ # $% ^ _-.;: & + =)";
            errorTextView.setTextColor(Color.RED);
            errorTextView.setText(ErrorText);
        }
        return error;

    }

    public boolean verificationConfirmPassword(){

        ConfirmPassField = findViewById(R.id.confirmPassField);
        valueConfirmPassField = (String.valueOf(ConfirmPassField.getText())).trim();
        boolean resultConfirmPass = valueConfirmPassField.equals(valuePassField) ? true : false;

        if (resultConfirmPass) {
            error = false;
            errorTextView4.setText(valueConfirmPassField);
        }
        else {
            error=true;
            ErrorText = "Passwords isn't equals.Password must be same.";
            errorTextView.setTextColor(Color.RED);
            errorTextView.setText(ErrorText);
        }
        return error;
    }






}


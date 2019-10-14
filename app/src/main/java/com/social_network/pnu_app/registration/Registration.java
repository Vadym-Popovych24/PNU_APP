
package com.social_network.pnu_app.registration;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    String valueIDcardField;
    String valueEmailField;
    String valuePassField;
    String valueConfirmPassField;

    private String valueIDcardDB;
    public int valueIDSeriesIDcard;
    public boolean valueVerify;

    boolean error = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        verifycationData(AppDatabase.getAppDatabase(Registration.this));

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

                if ( verifycationSeriesIDcard() == false && verifycationEmail() == false &&
                        verifycationPassword() == false && verifycationConfirmPassword() == false){

                    valueIDcardField = String.valueOf(IDcardField.getText());
                    valueIDcardDB = db.studentDao().getSeriesIDcard(valueIDcardField);
                    valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardDB);
                    valueVerify =db.studentDao().getVerify(valueIDSeriesIDcard);


                    if (valueIDcardDB.equals(valueIDcardField) &&  valueVerify == false ) {

                        db.studentDao().updateVerify(true, valueIDSeriesIDcard);
                        db.studentDao().setPassword(valuePassField, valueIDSeriesIDcard);
                        db.studentDao().setEmail(valueEmailField , valueIDSeriesIDcard);

                        ErrorText = "SUCCESS REGISTRATION " + "valueIDSeriesIDcard = " + String.valueOf(valueIDSeriesIDcard) +
                        " valueVerify = " + String.valueOf(valueVerify);
                        alertErrorReg();

                    }
                    else if(valueVerify == true){
                        ErrorText = "Student already registered";
                        alertErrorReg();
                    }
                    else if (!valueIDcardDB.equals(valueIDcardField)){
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


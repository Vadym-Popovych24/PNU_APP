package com.social_network.pnu_app.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.social_network.pnu_app.R;
import com.social_network.pnu_app.database.AppDatabase;
import com.social_network.pnu_app.entity.Student;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    Button btnSignIn;
    MaterialEditText IDcardField;
    MaterialEditText passField;
    String valueIDcardField;


    private String yourLastName;
    private String yourName;

    private String valueIDcardDB;
    String valuePassField;


    public int valueIDSeriesIDcard;
    private int valueIDPassword;

    private String valuePassDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        verifyStudentIn(AppDatabase.getAppDatabase(SignIn.this));
    }


    public void verifyStudentIn(final AppDatabase db){
        final Student student = new Student();
        btnSignIn = findViewById(R.id.btnSignIn);
        IDcardField = findViewById(R.id.IDcardField);
        passField = findViewById(R.id.passFieldSignIn);

        View.OnClickListener listenerSignIn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFromSignIn;
                String warning = "Data is wrong: Try again";

                valueIDcardField = String.valueOf(IDcardField.getText());
                valuePassField = String.valueOf(passField.getText());

                valuePassDB = db.studentDao().getPassword(valuePassField);
                valueIDcardDB = db.studentDao().getSeriesIDcard(valueIDcardField);

                valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardField);
                valueIDPassword = db.studentDao().getIdstudentByIDPassword(valuePassField);

               if (view.getId() == R.id.btnSignIn && (valueIDcardDB != null && valuePassDB != null )) {
                       if (valueIDcardDB.equals(valueIDcardField) && (valuePassDB.equals(valuePassField) &&
                               (valueIDSeriesIDcard == valueIDPassword))) {

                           yourName =db.studentDao().getFirstName(getValueIDSeriesIDcard());
                           yourLastName = db.studentDao().getLastName(getValueIDSeriesIDcard());


                           intentFromSignIn = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                           startActivity(intentFromSignIn);

                   }
                   else {

                   }
               }
               else {

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

    public String getYourLastName() {
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

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }
    }


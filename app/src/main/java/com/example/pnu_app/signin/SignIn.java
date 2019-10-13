package com.example.pnu_app.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pnu_app.R;
import com.example.pnu_app.database.AppDatabase;
import com.example.pnu_app.entity.Student;
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
    TextView exam, exam2 , exam3, exam4, exam5 ,exam6;
    TextView tv;

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
        exam = findViewById(R.id.ExampleTextView);
        exam2 = findViewById(R.id.ExampleTextView2);
        exam3 = findViewById(R.id.ExampleTextView3);
        exam4 = findViewById(R.id.ExampleTextView4);
        exam5 = findViewById(R.id.ExampleTextView5);
        exam6 = findViewById(R.id.ExampleTextView6);
        tv = findViewById(R.id.tvPage);

        View.OnClickListener listenerSignIn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFromSignIn;
                String warning = "Data is wrong: Try again";

                valueIDcardField = String.valueOf(IDcardField.getText());
                valuePassField = String.valueOf(passField.getText());

                // valueIDSeriesIDcard = db.studentDao().getFstudent(12);

                valuePassDB = db.studentDao().getPassword(valuePassField);
                valueIDcardDB = db.studentDao().getSeriesIDcard(valueIDcardField);

                valueIDSeriesIDcard =db.studentDao().getIdstudentByIDcard(valueIDcardField);
                valueIDPassword = db.studentDao().getIdstudentByIDPassword(valuePassField);

               if (view.getId() == R.id.btnSignIn && (valueIDcardDB != null && valuePassDB != null )) {
                       if (valueIDcardDB.equals(valueIDcardField) && (valuePassDB.equals(valuePassField) &&
                               (valueIDSeriesIDcard == valueIDPassword))) {

                        /*   yourName =db.studentDao().getFirstName(getValueIDSeriesIDcard());
                           yourLastName = db.studentDao().getLastName(getValueIDSeriesIDcard());

                          exam.setText(yourName);*/


                    /*       exam.setText("Value from field SeriesID card: " + valueIDcardField);
                           exam2.setText("Value from fiels Password: " + valuePassField);

                           exam3.setText("Value from DB SeriesID card: " + String.valueOf(valueIDcardDB));
                           exam4.setText("Value from DB password: " + String.valueOf(valuePassDB));

                           exam5.setText("Value from DB id SeriesID card: " + String.valueOf(valueIDSeriesIDcard));
                           exam6.setText("Value from DB Id password: " + String.valueOf(valueIDPassword));*/

                            intentFromSignIn = new Intent("com.example.pnu_app.pages.MainStudentPage");
                            startActivity(intentFromSignIn);

                   }
                   else {
                       exam.setText(warning);
                      /* exam.setText("Value1 from field SeriesID card: " + valueIDcardField);
                       exam2.setText("Value1 from fiels Password: " + valuePassField);
                       exam3.setText(warning);
                       exam4.setText(warning);
                       exam5.setText(warning);
                       exam6.setText(warning);*/
                   }
               }
               else {
                   exam.setText(warning);
             /*      exam.setText("Value2 from field SeriesID card: " + valueIDcardField);
                   exam2.setText("Value2 from fiels Password: " + valuePassField);
                   exam3.setText(warning);
                   exam4.setText(warning);
                   exam5.setText(warning);
                   exam6.setText(warning);*/
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


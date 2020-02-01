package com.social_network.pnu_app.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.firebase.QueriesFirebase;
import com.social_network.pnu_app.network.NetworkStatus;
import com.social_network.pnu_app.registration.PhoneAuthentication;
import com.social_network.pnu_app.registration.Registration;
import com.social_network.pnu_app.R;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class SignIn extends AppCompatActivity {

    Button btnSignIn;
    MaterialEditText IDcardField;
    MaterialEditText passField;
    String valueIDcardField;

    private FirebaseAuth mAuth;

    static String valuePassField="";
    static String valuePhoneField="";
    public String ErrorText=null;

    boolean error = true;

    public static int valueIDSeriesIDcard;
    private int valueIDPassword;

    private String valuePassDB="";

    static ValueEventListener valueEventListener;
    private static final String TAG ="TAG";

    public boolean FBverify;
    public static String FBidSerie ="";
    public String FBpassword = "1";
    public String FBemail = "";
    public String FBphone = "";
    public static Object FBid = 0;
    public String FBName;
    public String FBLastName;

    TextView textView;
    String KeyStudent ="default";
    QueriesFirebase qf = new QueriesFirebase();
   static HashMap<Object, Object> student = new HashMap();
    private ProgressBar progressBar;
    String codeSent;

    NetworkStatus network = new NetworkStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        progressBar = findViewById(R.id.progressbarSignIn);
        progressBar.setVisibility(View.GONE);
        textView = findViewById(R.id.ExampleTextSignIN);
        mAuth = FirebaseAuth.getInstance();
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

        if(!network.isOnline()){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SignIn.this, " Please Connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
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
                FBphone = (String) student.get("phone");
                Registration encrypt = new Registration();
                valuePassField = encrypt.encryptionPassword(valuePassField); // TODO this coderow encrypt password in database (comment for example)

                try {
                    FBverify = (boolean) student.get("verify");
                } catch (Exception castToBool) {
                    FBverify = false;
                }
                if ((FBidSerie != null && FBpassword != null)) {

                 if (!FBidSerie.equals(valueIDcardField)) {
                     progressBar.setVisibility(View.GONE);
                    ErrorText = "Student with the such series id does not exist";
                    alertErrorSign();
                } else if (FBidSerie.equals(valueIDcardField) && (FBverify != true)) {
                     progressBar.setVisibility(View.GONE);
                    ErrorText = "IDPassword = " + valueIDPassword +
                            "IDSeriesIDCard = " + valueIDSeriesIDcard;
                    ErrorText = "Student with the such series id does not registered";
                    alertErrorSign();
                }  else if (!(FBpassword.equals(valuePassField)) && FBverify == true) { // TODO
                     progressBar.setVisibility(View.GONE);
                    ErrorText = "Wrong password";
                    alertErrorSign();
                }


                    if (FBverify == true && FBidSerie.equals(valueIDcardField) && FBpassword.equals(valuePassField)) {
                        FBName = (String) student.get("name");
                        FBLastName = (String) student.get("lastName");

                        Student.student = student;

                        progressBar.setVisibility(View.GONE);

                        intentFromSignIn = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                        startActivity(intentFromSignIn);

                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    ErrorText = "Student with such id series does not exist 2";
                    alertErrorSign();
                }

                // TODO sign in

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(!network.isOnline()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignIn.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
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
                progressBar.setVisibility(View.VISIBLE);
                initFieldInput();
                getStudentFB();
                if (view.getId() == R.id.btnSignIn) {

                    if (valueIDcardField.isEmpty()) { // TODO
                        progressBar.setVisibility(View.GONE);
                        ErrorText = "Enter Series ID";
                        alertErrorSign();
                    } else if (valuePassField.isEmpty()) { // TODO
                        progressBar.setVisibility(View.GONE);
                        ErrorText = "Enter password";
                        alertErrorSign();
                    } else {
                        if (verifycationSeriesIDcard() == false &&
                                verifycationPassword() == false) {
                            queryFB();
                            textView.setText("FBphone =" + FBphone);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            alertErrorSign();
                        }
                    }


                }

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

    }



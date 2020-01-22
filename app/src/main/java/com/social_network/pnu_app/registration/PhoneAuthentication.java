package com.social_network.pnu_app.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.FirebaseUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;

import java.util.Map;

public class PhoneAuthentication extends AppCompatActivity {

    private static final String TAG ="";
    private FirebaseAuth mAuth;
    FirebaseUI a;

    String verificationCode = "1";
    String valueVerificationCode;

    TextView tx;

    MaterialEditText idVerificationCode;
    Button idbtnVerifyRegister;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        idVerificationCode = findViewById(R.id.verificationCode);
        idbtnVerifyRegister = findViewById(R.id.btnVerifyRegister);
        tx = findViewById(R.id.ExampleTextAuth);
        tx.append(String.valueOf(Registration.FBid));
        chckDate();


    }



     boolean alreadyReg = false;


    boolean verfy;
    String email ="default".concat(String.valueOf(Registration.FBid)).concat("@gmail.com");
    String password = Registration.valuePassField;
    String phone;
    String uid;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
      //  updateUI(currentUser);
    }

    public void getVerificationCode() {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");

                        Toast.makeText(PhoneAuthentication.this, "Registration Success",
                                Toast.LENGTH_LONG).show();

                        verfy = true;
                        phone = Registration.valuePhoneField;
                        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Student studentValue = new Student(verfy, email, password, phone, uid);

                        reference.child(Registration.KeyStudent).updateChildren((studentValue.toMapUpdateChild()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "update RealTimeDatabasesuccess");
                                Toast.makeText(PhoneAuthentication.this, "Success update RealTimeDatabase",
                                        Toast.LENGTH_LONG).show();

                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //display a failure message in logs
                                Log.w(TAG, "update RealTimeDatabase:failure", e.fillInStackTrace());
                                Toast.makeText(PhoneAuthentication.this, "Registration Not Success", Toast.LENGTH_LONG).show();
                            }
                        });


                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If sign in fails, display a message to the user and write logs in outputs.
                Log.w(TAG, "createUserWithEmail:failure", e.fillInStackTrace());
                Toast.makeText(PhoneAuthentication.this, "Registration Not 2 Success email = " + email +
                                " Registration.valuePassField = " + password,
                        Toast.LENGTH_LONG).show();
                //     updateUI(null);
            }
        });
    }

    public void chckDate(){
        View.OnClickListener btnVerifyRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueVerificationCode = String.valueOf(idVerificationCode.getText()).trim();
                valueVerificationCode = valueVerificationCode.replaceAll(" ", "");

                if (verificationCode.equals(valueVerificationCode)) {

                getVerificationCode();


            } else {
                // TODO Something wrong
            }

        }


        };
        idbtnVerifyRegister.setOnClickListener(btnVerifyRegisterListener);
    }


}


package com.social_network.pnu_app.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;

public class PhoneAuthentication extends AppCompatActivity {
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
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

        mAuth = FirebaseAuth.getInstance();
        idVerificationCode = findViewById(R.id.verificationCode);
        idbtnVerifyRegister = findViewById(R.id.btnVerifyRegister);
        tx = findViewById(R.id.ExampleTextAuth);
        chckDate();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    tx.append(user.getEmail() + "\n");
                    tx.append("Signed In" + "\n");

                } else {
                    tx.append("Not found email" + "\n");
                    tx.append("Signed Out" + "\n");

                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
mAuth.addAuthStateListener(authListener);
        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    boolean alreadyReg = false;

/*
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            // handle already login student
            alreadyReg = true;
        }

        View.OnClickListener btnVerifyRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getVerificationCode();

            }


        };
        idbtnVerifyRegister.setOnClickListener(btnVerifyRegisterListener);
    }
*/

    String key;
    public boolean getVerificationCode() {
        boolean verify = false;
        final String email = "vadympopovychn24gmail.com";
        final String phone = "+380731222559";
        final boolean verf = true;
      //  String password = Registration.valuePassField;
final String password = "qwerty36;";

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                Student user = new Student(verf, email, password, phone);

                                Registration.KeyStudent = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                key = Registration.KeyStudent;

                                  /*     reference.child(Registration.KeyStudent).child("phone").setValue(Registration.valuePhoneField);
                                  reference.child(Registration.KeyStudent).child("password").setValue(Registration.valuePassField);
                                  reference.child(Registration.KeyStudent).child("verify").setValue(true);*/

                                reference.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task2) {
                                        if (task2.isSuccessful()) {
                                            Toast.makeText(PhoneAuthentication.this, "Registration Success",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(PhoneAuthentication.this, "Registration Not Success",
                                                    Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });
                            } else {
                                Toast.makeText(PhoneAuthentication.this, "Registration Not 2 Success email = " + email +
                                                " Registration.valuePassField = "+ password,
                                        Toast.LENGTH_LONG).show();
                            }


                        }
                    });




        return verify;
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


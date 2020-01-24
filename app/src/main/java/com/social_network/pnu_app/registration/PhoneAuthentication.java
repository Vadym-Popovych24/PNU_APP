package com.social_network.pnu_app.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;

import java.util.concurrent.TimeUnit;

public class PhoneAuthentication extends AppCompatActivity {

    private static final String TAG ="TAG";
    private FirebaseAuth mAuth;
    String codeSent;

    String valueVerificationCode;

    TextView tx;

    MaterialEditText idVerificationCode;
    Button idbtnVerifyRegister;
    FirebaseUser currentUser;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        mAuth = FirebaseAuth.getInstance();
        idVerificationCode = findViewById(R.id.verificationCode);
        idbtnVerifyRegister = findViewById(R.id.btnVerifyRegister);
        tx = findViewById(R.id.ExampleTextAuth);

        sendCodeVerification();
        verifyCodeSent();


    }

    boolean verfy;
    String password = Registration.valuePassField;
    String phone;
    String uid;

    String ErrorText;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        currentUser = null;
    }

    public void alertErrorPhoneAuthentication(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
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

   public void sendCodeVerification(){
       PhoneAuthProvider.getInstance().verifyPhoneNumber(
               Registration.valuePhoneField,        // Phone number to verify
               60,                               // Timeout duration
               TimeUnit.SECONDS,                     // Unit of timeout
               this,                          // Activity (for callback binding)
               mCallbacks);                          // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

            Toast.makeText(PhoneAuthentication.this, "On verification completed, please sign in ", Toast.LENGTH_LONG).show();

            // THIS METHOD IS AN AUTO SIGN IN , HE CALLS WHEN USER ALREADY GET CODE VERIFY AND REGISTERED

            // This case almost unreal but, I add this part of code for better work app. But student can
            // sign in a MainStudentPage without confirms verify code it happend if he confirm code verify earlier
            // and it is norm, but previous ones verifing methods and setters phone number in RealTimeDatabase don't allow do it


                            Intent intentFromPhoneAuthentication = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                            startActivity(intentFromPhoneAuthentication);


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.w(TAG, "onVerificationFailed", e.fillInStackTrace());
            ErrorText = "On Verification Failed, please input correct verification code.";
            alertErrorPhoneAuthentication();
        //    Toast.makeText(PhoneAuthentication.this, "on Verification Failed input correct verification code ", Toast.LENGTH_LONG).show();
        }


        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };

    public void verifyCodeSent(){
        View.OnClickListener btnVerifyRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser == null) {


                    valueVerificationCode = String.valueOf(idVerificationCode.getText()).trim();
                    valueVerificationCode = valueVerificationCode.replaceAll(" ", "");

                    if (valueVerificationCode != null && codeSent != null && valueVerificationCode != "") {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, valueVerificationCode);
                        signInWithPhoneAuthCredential(credential);
                    } else {
                        Toast.makeText(PhoneAuthentication.this, "Input Code verification ", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    ErrorText = "Error with sending SMS, current user already in sign in and registered.";
                    alertErrorPhoneAuthentication();
                //    Toast.makeText(PhoneAuthentication.this, "Error with sending SMS, current user already in sign in.", Toast.LENGTH_LONG).show();
                }
        }


        };
        idbtnVerifyRegister.setOnClickListener(btnVerifyRegisterListener);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");

           /*             Toast.makeText(PhoneAuthentication.this, "SignIn Success",
            Toast.LENGTH_LONG).show();
*/
                    verfy = true;
                    phone = Registration.valuePhoneField;
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Student studentValue = new Student(verfy, password, phone, uid);

                    reference.child(Registration.KeyStudent).updateChildren((studentValue.toMapUpdateChild()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "update RealTime Database Success");
                                    Intent intentFromPhoneAuthentication = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                                    startActivity(intentFromPhoneAuthentication);

                                }


                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //display a failure message in logs
                            Log.w(TAG, "update RealTimeDatabase:failure", e.fillInStackTrace());
                            ErrorText = "Registration not success, check your internet connection and try again";
                            alertErrorPhoneAuthentication();
                            //  Toast.makeText(PhoneAuthentication.this, "Registration not success, check your internet" +
                            //          " connection and try again ", Toast.LENGTH_LONG).show();
                        }
                    });


                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // If sign in fails, display a message to the user and write logs in outputs.
                    Log.w(TAG, "signInWithCredential:failure", e.fillInStackTrace());
                    ErrorText = "Registration failure incorrect verification code or problem with sending response. " +
                            "To solve this problem verify that the verification code, which you get in SMS,  inputted correct." +
                            " If code verification, inputted correct you need restart your phone and repeat registration.";
                    alertErrorPhoneAuthentication();

           /* Toast.makeText(PhoneAuthentication.this, "Registration failure incorrect verification code or problem with sending response. " +
                            "To solve this problem verify that the verification code, which you get in SMS,  inputted correct." +
                            " If code verification, inputted correct you need restart your phone and repeat registration.",
                    Toast.LENGTH_LONG).show();*/
                }

            });
        }
        else {
            ErrorText = "Current user already registered";
                    alertErrorPhoneAuthentication();
        }
    }


}


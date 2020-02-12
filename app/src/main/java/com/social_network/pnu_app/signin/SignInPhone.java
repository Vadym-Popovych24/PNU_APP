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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.network.NetworkStatus;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import com.social_network.pnu_app.R;

public class SignInPhone extends AppCompatActivity {

    private static final String TAG ="TAG";
    private FirebaseAuth mAuth;
    String codeSent;

    String valueVerificationCode;

    TextView tx;
    private ProgressBar progressBar;
    MaterialEditText idVerificationCode;
    Button idbtnVerifyRegister;
    FirebaseUser currentUser;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");
    ValueEventListener listerQueryBySerieIDcatdStudent;
    static boolean onVerificationCompleted =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_phone);

        mAuth = FirebaseAuth.getInstance();
        idVerificationCode = findViewById(R.id.verificationCodeSignInAuto);
        idbtnVerifyRegister = findViewById(R.id.btnVerifyPhoneSignInAuto);
        tx = findViewById(R.id.ExampleTextPhoneSignInAuto);
        progressBar = findViewById(R.id.progressbarPhoneSignInAuto);
        progressBar.setVisibility(View.GONE);

        verifyCodeSent();
    }

    String ErrorText;
    public static HashMap<Object, Object> student = new HashMap();

    NetworkStatus network = new NetworkStatus();

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
     //   currentUser = mAuth.getCurrentUser();
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



    public void verifyCodeSent(){
        tx.append(" codeSent = " + codeSent);
        View.OnClickListener btnVerifyRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getStudentBySerieIDcardFB();

                valueVerificationCode = String.valueOf(idVerificationCode.getText()).trim();
                valueVerificationCode = valueVerificationCode.replaceAll(" ", "");

                if (valueVerificationCode != null && codeSent !=null && valueVerificationCode != "") {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, valueVerificationCode);
                    signInWithPhoneAuthCredential(credential);
                    queryBySerieIDcardFB();
                }
                else if (valueVerificationCode == null || valueVerificationCode == "") {
                    ErrorText = "Input verification code!";
                    alertErrorPhoneAuthentication();
                }

                else{
                    progressBar.setVisibility(View.GONE);
                    tx.append(" codeSent = " + codeSent);
                    tx.append(" (Registration.FBidSerie: " + SignIn.FBidSerie);
                    ErrorText = "Error with sending SMS, current user already sign in and registered.Please SIGN IN";
                    alertErrorPhoneAuthentication();
                }
            }


        };
        idbtnVerifyRegister.setOnClickListener(btnVerifyRegisterListener);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success");

           /*             Toast.makeText(PhoneAuthentication.this, "SignIn Success",
            Toast.LENGTH_LONG).show();
*/



            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                // If sign in fails, display a message to the user and write logs in outputs.
                Log.w(TAG, "signInWithCredential:failure", e.fillInStackTrace());
                if (!network.isOnline()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignInPhone.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    ErrorText = "Registration failure incorrect verification code or problem with sending response. " +
                            "To solve this problem verify that the verification code, which you get in SMS,  inputted correct." +
                            " If code verification, inputted correct you need restart your phone and repeat registration.";
                    alertErrorPhoneAuthentication();
                }
            }

        });
    }

    public void queryBySerieIDcardFB(){
        Query querySeriesIDcard = FirebaseDatabase.getInstance().getReference("students")
                .orderByChild("seriesIDcard")
                .equalTo(SignIn.FBidSerie);

        querySeriesIDcard.addListenerForSingleValueEvent(listerQueryBySerieIDcatdStudent);
    }

    public HashMap<Object, Object> getStudentBySerieIDcardFB(){

        listerQueryBySerieIDcatdStudent = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    student = (HashMap) snapshot.getValue();
                }
                Student.student = student;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignInPhone.this, "Error connect to Database, check your " +
                        "internet connection and try again " , Toast.LENGTH_LONG).show();
            }
        };
        return student;
    }

}


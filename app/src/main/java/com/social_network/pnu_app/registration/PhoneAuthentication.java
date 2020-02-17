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

public class PhoneAuthentication extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        mAuth = FirebaseAuth.getInstance();
        idVerificationCode = findViewById(R.id.verificationCode);
        idbtnVerifyRegister = findViewById(R.id.btnVerifyRegister);
        tx = findViewById(R.id.ExampleTextAuth);
        progressBar = findViewById(R.id.progressbarPhoneAuth);
        progressBar.setVisibility(View.GONE);
        sendCodeVerification();
        verifyCodeSent();


    }

    boolean verfy;
    String password = Registration.valuePassField;
    String phone;
    String uid;
    String ErrorText;
   public static HashMap<Object, Object> student = new HashMap();

    NetworkStatus network = new NetworkStatus();

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
           // tx.append(" codeSent = " + codeSent);
                Toast.makeText(PhoneAuthentication.this, "On verification completed, please sign in ", Toast.LENGTH_LONG).show();
            /*     if (codeSent == null) {
                    Toast.makeText(PhoneAuthentication.this, "Code sent == nul OnVerificationCompleted ", Toast.LENGTH_LONG).show();
                // THIS METHOD IS AN AUTO SIGN IN , HE CALLS WHEN USER ALREADY GET CODE VERIFY BUT NOT CONFIRM HIS VERIFY CODE

                // This case almost unreal but, I add this code for better works app. But student can
                // sign in a MainStudentPage without confirms verify code it happend if he gets code verify earlier but not confirm it
                // and it is norm. If user input phone number which he previously ones use, verifing methods and setters phone number
                // in RealTimeDatabase don't allow do register agen.


                verfy = true;
                phone = Registration.valuePhoneField;
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Student studentValue = new Student(verfy, password, phone, uid);

                reference.child(Registration.KeyStudent).updateChildren((studentValue.toMapUpdateChild()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "update RealTime Database Success");
                                Intent intentFromPhoneAuthentication = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                                startActivity(intentFromPhoneAuthentication);
                                idVerificationCode.setText("");
                            }


                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        //display a failure message in logs
                        Log.w(TAG, "update RealTimeDatabase:failure", e.fillInStackTrace());
                        ErrorText = "Registration not success, check your internet connection and try again";
                        alertErrorPhoneAuthentication();
                        //  Toast.makeText(PhoneAuthentication.this, "Registration not success, check your internet" +
                        //          " connection and try again ", Toast.LENGTH_LONG).show();
                    }
                });
            }*/
         /*   else{
                ErrorText = "On Verification Failed, You already registered! just Sign In";
                alertErrorPhoneAuthentication();
            }*/

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Log.w(TAG, "onVerificationFailed", e.fillInStackTrace());
            ErrorText = "On Verification Sending SMS Failed! You are often do requests due to unusual activity and was blocked. Try Later! After 4 hours" ;
            alertErrorPhoneAuthentication();
        }


        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };

    public void verifyCodeSent(){
     //   tx.append(" codeSent = " + codeSent);
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
                //    tx.append(" codeSent = " + codeSent);
                //    tx.append(" (Registration.FBidSerie: " + Registration.FBidSerie);
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

                    verfy = true;
                    phone = Registration.valuePhoneField;
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Student studentValue = new Student(verfy, password, phone, uid);

                    reference.child(Registration.KeyStudent).updateChildren((studentValue.toMapUpdateChild()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.d(TAG, "update RealTime Database Success");
                                    codeSent = null;
                                    Intent intentFromPhoneAuthentication = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                                    startActivity(intentFromPhoneAuthentication);
                                    idVerificationCode.setText("");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            if (!network.isOnline()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(PhoneAuthentication.this, " Please Connect to Internet",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                //display a failure message in logs
                                Log.w(TAG, "update RealTimeDatabase:failure", e.fillInStackTrace());
                                ErrorText = "Registration not success, check your internet connection and try again";
                                alertErrorPhoneAuthentication();
                            }
                        }
                    });


                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    // If sign in fails, display a message to the user and write logs in outputs.
                    Log.w(TAG, "signInWithCredential:failure", e.fillInStackTrace());
                    if (!network.isOnline()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PhoneAuthentication.this, " Please Connect to Internet",
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
                .equalTo(Registration.FBidSerie);

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
                Toast.makeText(PhoneAuthentication.this, "Error connect to Database, check your " +
                        "internet connection and try again " , Toast.LENGTH_LONG).show();
            }
        };
        return student;
    }

    }

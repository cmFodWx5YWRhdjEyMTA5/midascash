package prima.optimasi.indonesia.primacash.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.generator;


public class VerificationHeader extends AppCompatActivity {

    private String TAG = "Verification Header";

    ProgressDialog dialog;

    TextInputEditText headnum , footnum ;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView tv_coundown;
    private CountDownTimer countDownTimer;

    String fullname, username,email,password,phonehead,phonefoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_header);
        initToolbar();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle intent = getIntent().getExtras();

        phonehead = intent.getString("phonehead","");
        phonefoot = intent.getString("phonefoot","");
        username = intent.getString("username","");
        password = intent.getString("password","");
        email  = intent.getString("email","");
        fullname = intent.getString("fullname","");

        headnum = findViewById(R.id.verifyheadnum);
        footnum = findViewById(R.id.verifyheadnum);

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                dialog = new ProgressDialog(VerificationHeader.this,R.style.AppCompatAlertDialogStyle);
                dialog.setTitle("Please Wait");
                dialog.setMessage("Signing Up...");
                dialog.setCancelable(false);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(VerificationHeader.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("fullname", fullname);
                                    data.put("username", username);
                                    data.put("phonehead", phonehead);
                                    data.put("email", email);
                                    data.put("phonefoot", phonefoot);

                                    db.collection(user.getUid()).document("identification")
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                    generator.activitychecker = 1;

                                                    setResult(RESULT_OK);

                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

                                    Snackbar.make(findViewById(R.id.coordinatorsignup), "Authentication Success, Registering Data.", Snackbar.LENGTH_LONG).show();
                                    user.getUid();

                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Snackbar.make(findViewById(R.id.coordinatorsignup), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });

                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                generator.verificationid = verificationId;
                generator.token = token;

                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonehead+phonefoot,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        tv_coundown = (TextView) findViewById(R.id.tv_coundown);
        countDownTimer();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void countDownTimer() {
        countDownTimer = new CountDownTimer(1000 * 60 * 2, 1000) {
            @Override
            public void onTick(long l) {
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(l) % 60);
                tv_coundown.setText(text);
            }

            @Override
            public void onFinish() {
                tv_coundown.setText("00:00");
            }
        };
        countDownTimer.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(Activity.RESULT_CANCELED);

        finish();
    }

    /* PhoneAuthProvider.getInstance().verifyPhoneNumber(
                1234,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);*/


}

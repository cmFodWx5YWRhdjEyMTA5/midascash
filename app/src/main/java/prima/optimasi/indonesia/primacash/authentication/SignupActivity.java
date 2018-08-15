package prima.optimasi.indonesia.primacash.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Printer;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.generator;

public class SignupActivity extends AppCompatActivity {

    private String TAG = "SignupUsers";

    ProgressDialog dialog ;

    AutoCompleteTextView username,email,fullname;
    TextInputEditText phonenumberhead,phonenumberfoot;
    EditText password,retype;

    Button signup;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;
    CoordinatorLayout cordinator;

    int VERIFCATION_RESULT =4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_form_sign_up);

        username = findViewById(R.id.signupusername);
        email = findViewById(R.id.signupemail);
        password = findViewById(R.id.signuppass);
        retype = findViewById(R.id.signupretypepass);
        fullname = findViewById(R.id.signupfullname);
        phonenumberhead = findViewById(R.id.signupheadnum);
        phonenumberhead.setVisibility(View.GONE);
        phonenumberfoot = findViewById(R.id.signupfootnum);
        phonenumberfoot.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signup = findViewById(R.id.email_sign_in_button);


        cordinator = findViewById(R.id.coordinatorsignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullname.getText().toString().equals("")){
                    fullname.setError("Your Name Must be filled");
                }
                else {
                    if(username.getText().toString().equals("")){
                        username.setError("Username is Required");
                    }else {
                        if(email.getText().toString().equals("")){
                            email.setError("Email is Required");
                        }
                        else {
                            if(isvalidemail(email.getText().toString())){
                                if(phonenumberhead.getText().toString().equals("")){
                                    phonenumberhead.setError("Please Check Your Phone Number Prefix");
                                }
                                else {
                                    /*if(phonenumberfoot.getText().toString().equals("")){
                                        phonenumberfoot.setError("Please Check Your Phone Number");
                                    }
                                    else {
                                        if(phonenumberfoot.getText().toString().length()<=8){
                                            phonenumberfoot.setError("Your Phone Number is too short");
                                        }
                                        else {

                                        }
                                    }*/

                                    if(password.length()<8){
                                        password.setError("Password Too Short");
                                    }
                                    else {
                                        if(isvalidpassword(password.getText().toString())){
                                            if(retype.getText().toString().equals(password.getText().toString())){

                                                dialog = new ProgressDialog(SignupActivity.this,R.style.AppCompatAlertDialogStyle);
                                                dialog.setTitle("Please Wait");
                                                dialog.setMessage("Signing Up...");
                                                dialog.setCancelable(false);
                                                dialog.show();


                                                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Sign in success, update UI with the signed-in user's information
                                                                    Log.d(TAG, "createUserWithEmail:success");
                                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                                    Map<String, Object> data = new HashMap<>();
                                                                    data.put("fullname", fullname.getText().toString());
                                                                    data.put("username", username.getText().toString());
                                                                    //data.put("phonehead", phonenumberhead.getText().toString());
                                                                    data.put("email", email.getText().toString());
                                                                    //data.put("phonefoot", phonenumberfoot.getText().toString());

                                                                    db.collection(user.getUid()).document("identification")
                                                                            .set(data)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                                    generator.activitychecker = 1;

                                                                                    if(dialog.isShowing())
                                                                                        dialog.dismiss();

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


                                                //Intent verify = new Intent(SignupActivity.this,VerificationHeader.class);
                                                //Bundle extras = new Bundle();
                                                //extras.putString("username", username.getText().toString());
                                                //extras.putString("fullname",fullname.getText().toString());
                                                //extras.putString("email",email.getText().toString());
                                                //extras.putString("phonehead",phonenumberhead.getText().toString());
                                                //extras.putString("phonefoot",phonenumberfoot.getText().toString());
                                                //extras.putString("password",password.getText().toString());
                                                //extras.putInt("verify",0);
                                                //verify.putExtras(extras);
                                                //startActivityForResult(verify,VERIFCATION_RESULT);


                                            }
                                            else {
                                                password.setError("Password Doesn't Match");
                                                retype.setError("Password Doesn't Match");
                                            }
                                        }else {
                                            password.setError("Password must be at least 8 character with Alphanumeric combination");
                                        }
                                    }
                                }
                            }
                            else {
                                email.setError("Invalid Email , Please Check Again");
                            }
                        }
                    }
                }


            }
        });
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

    public boolean isvalidemail (final String email){

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();

    }

    public boolean isvalidpassword (final String password){

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(Activity.RESULT_CANCELED);

        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VERIFCATION_RESULT && resultCode == RESULT_OK) {
            this.finish();
        }else  if(requestCode == VERIFCATION_RESULT && resultCode != RESULT_OK){

        }
    }

}

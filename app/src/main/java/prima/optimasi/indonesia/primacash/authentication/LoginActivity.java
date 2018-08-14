package prima.optimasi.indonesia.primacash.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prima.optimasi.indonesia.primacash.MainActivity;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.generator;


public class LoginActivity extends AppCompatActivity {

    LayoutInflater inflater ;


    private static final String TAG = "LoginActivity";
    private View parent_view;

    EditText emailname ;
    EditText password ;

    ProgressDialog dialog ;

    SignInButton gsignin ;
    Button signin;
    TextView signup;

    Snackbar snackbar;

    private FirebaseAuth mAuth;

    int RC_SIGN_IN = 2;

    int SIGN_UP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_simple_dark);

        emailname = findViewById(R.id.passvalue);
        password = findViewById(R.id.usrvalue);

        dialog = new ProgressDialog(LoginActivity.this,R.style.AppCompatAlertDialogStyle);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Signing in...");
        dialog.setCancelable(false);

        parent_view = findViewById(android.R.id.content);

        inflater = LayoutInflater.from(this);

        View V = inflater.inflate(R.layout.activity_login_simple_dark,null);
        CoordinatorLayout cordinator = V.findViewById(R.id.coordinatorsnake);

        mAuth = FirebaseAuth.getInstance();





        gsignin = findViewById(R.id.google_button);
        signin = findViewById(R.id.login_auth);
        signup = findViewById(R.id.sign_up);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(LoginActivity.this,SignupActivity.class);
                startActivityForResult(data,SIGN_UP);
            }
        });
        //Tools.setSystemBarColor(this, R.color.blue_grey_900);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailname.getText().toString().equals("")){
                    Snackbar.make(findViewById(R.id.coordinatorsnake), "Please Input Email", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if(isvalidemail(emailname.getText().toString())){
                        Snackbar.make(findViewById(R.id.coordinatorsnake), "Invalid Email", Snackbar.LENGTH_SHORT).show();
                    }else {
                        if(password.getText().toString().equals("")){
                            Snackbar.make(findViewById(R.id.coordinatorsnake), "Please Input Password", Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            mAuth.signInWithEmailAndPassword(emailname.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                //updateUI(user);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                Snackbar.make(findViewById(R.id.coordinatorsnake), "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                                               // updateUI(null);
                                            }

                                            // ...
                                        }
                                    });
                        }
                    }
                }


            }
        });

        gsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setCancelable(false);
                dialog.show(); //Keeps ProgressBar at 0 opacity after animation

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .requestId()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        ((View) findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Forgot Password", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        if (requestCode == SIGN_UP && resultCode == RESULT_OK) {
            this.finish();
        }else  if(requestCode == SIGN_UP && resultCode != RESULT_OK){

        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Snackbar.make(findViewById(R.id.coordinatorsnake), "Authentication Success.", Snackbar.LENGTH_SHORT).show();

                           /* SharedPreferences pref = getSharedPreferences("primacash",MODE_PRIVATE);
                            SharedPreferences.Editor  edit =pref.edit();

                            if (acct != null) {
                                String personName = acct.getDisplayName();
                                String personGivenName = acct.getGivenName();
                                String personFamilyName = acct.getFamilyName();
                                String personEmail = acct.getEmail();
                                String personId = acct.getId();
                                Uri personPhoto = acct.getPhotoUrl();
                            }

                            edit.putString("email",user.getEmail().toString());
                            edit.putString("userid",user.getUid().toString());

                            edit.apply();*/

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));

                            finish();

                            if(dialog.isShowing()){
                                dialog.dismiss();
                            }

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.coordinatorsnake), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public boolean isvalidpassword (final String password){

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean isvalidemail (final String email){

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        generator.acct = null ;
    }

}
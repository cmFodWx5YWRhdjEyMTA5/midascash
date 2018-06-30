package midascash.indonesia.optima.prima.midascash;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class splashscreen extends Activity {
    LinearLayout barbanner;
    FirebaseFirestore db;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Used to load the 'native-lib' library on application startup.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        db = FirebaseFirestore.getInstance();

        prefs = getSharedPreferences("midascash", MODE_PRIVATE);
        editor = getSharedPreferences("midascash", MODE_PRIVATE).edit();

        barbanner = (LinearLayout) findViewById(R.id.barbanner);
        barbanner.setVisibility(View.GONE);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                barbanner.setVisibility(View.VISIBLE);
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Intent i = new Intent(splashscreen.this,MainActivity.class);
                            startActivity(i);
                            finish();
                    }
                }, 3000);
            }
        }, 1000);

        /**/
        // Example of a call to a native method


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}

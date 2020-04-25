package com.project.groceryStore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText inpEmail, inpPwd;
    TextView unregUser;
    ProgressBar progressBar;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    Button loginBtn;
    String userID;
    String flag = "0";

    public static boolean isValidPassword(String password) {
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})").matcher(password);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inpEmail= findViewById(R.id.logemail);
        inpPwd= findViewById(R.id.logPwd);
        unregUser= findViewById(R.id.unregisted_txt);
        progressBar= findViewById(R.id.progressBarlog);
        loginBtn= findViewById(R.id.login_btn);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        unregUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inpEmail.getText().toString().trim();
                String pwd= inpPwd.getText().toString().trim();

                if (email.isEmpty() || isEmailValid(email)) {
                    inpEmail.setError("Email is Unrecognisable ");

                } else if (pwd.isEmpty() || isValidPassword(pwd)) {
                    inpPwd.setError("The password is badly formatted ");

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    fauth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Logged in SuccessFully", Toast.LENGTH_SHORT).show();
                                userID = Objects.requireNonNull(fauth.getCurrentUser()).getUid();
                                DocumentReference documentReference = fstore.collection("Applications").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Flag", flag);
                                documentReference.set(user, SetOptions.merge());

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

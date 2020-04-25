package com.project.groceryStore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText inpName, inpEmail,inpPhno, inpPwd;
    Button regBtn;
    TextView registeredText;
    ProgressBar progressBar;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userID;
    String flag = "0";

    public static boolean isValidPassword(String password) {
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})").matcher(password);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inpName=findViewById(R.id.inp_name);
        inpEmail=findViewById(R.id.inp_email);
        inpPhno=findViewById(R.id.inp_phno);
        inpPwd=findViewById(R.id.inp_pwd);

        regBtn=findViewById(R.id.register_btn);
        registeredText=findViewById(R.id.registed_txt);
        progressBar=findViewById(R.id.progressBar);

        fauth=FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();


        if(fauth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        registeredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inpEmail.getText().toString().trim();
                final String pwd = inpPwd.getText().toString().trim();
                final String fname = inpName.getText().toString();
                final String mphno = inpPhno.getText().toString().trim();

                if (fname.isEmpty()) {
                    inpName.setError("UserName Field is Compulsory ");
                } else if (email.isEmpty()) {
                    inpEmail.setError("Email is Badly Formatted");
                } else if (pwd.isEmpty()) {
                    inpPwd.setError("Password is empty or Badly Formatted (atleast 1 each of Upper Case,Lower Case and Special Character)");
                } else if (mphno.isEmpty() /*|| isValidMobile(mphno)*/) {
                    inpPhno.setError("Mobile Number Badly Formatted");
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    fauth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                                userID = fauth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("Users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("FullName", fname);
                                user.put("Email", email);
                                user.put("PhoneNo", mphno);
                                user.put("Flag", flag);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "onSuccess: user profile created for " + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "onFailure: Error " + e.toString());
                                    }
                                });

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 9 && phone.length() <= 11;
        }
        return false;
    }
}

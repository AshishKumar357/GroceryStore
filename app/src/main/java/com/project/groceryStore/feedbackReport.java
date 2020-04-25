package com.project.groceryStore;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class feedbackReport extends AppCompatActivity {

    static int ifeedback = 1;
    FloatingActionButton submitBtn;
    EditText titleTxt, descriptionTxt;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myacc_feedback);

        titleTxt = findViewById(R.id.feedbackTopic);
        descriptionTxt = findViewById(R.id.briefFeedback);
        submitBtn = findViewById(R.id.fabSubmitfeedback);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final String title = titleTxt.getText().toString();
                final String brief = descriptionTxt.getText().toString();

                if (!title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Subject for the problem", Toast.LENGTH_SHORT).show();
                } else if (!brief.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Describe the problem", Toast.LENGTH_SHORT).show();
                } else {
                    UserID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    DocumentReference documentReference = fStore.collection("UserFeedback").document(UserID);
                    Map<String, Object> fb = new HashMap<>();
                    fb.put("Subject " + ifeedback, title);
                    fb.put("Description " + ifeedback, brief);

                    documentReference.set(fb, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ifeedback++;
//                       for navigating to next activity
//                            Intent intent = new Intent(getApplicationContext(), NextActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Thank you for submitting feedback", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure: Error " + e.toString());
                            Toast.makeText(feedbackReport.this, e.toString() + " ", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }
}

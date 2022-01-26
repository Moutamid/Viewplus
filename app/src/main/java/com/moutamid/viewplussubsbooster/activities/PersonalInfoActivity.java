package com.moutamid.viewplussubsbooster.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplussubsbooster.R;

public class PersonalInfoActivity extends AppCompatActivity {
    private static final String TAG = "PersonalInfoActivity";
    private static final String USER_INFO = "userinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        TextView dateTextView = findViewById(R.id.email_textview);
        dateTextView.setText("Account: " + auth.getCurrentUser().getEmail());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(USER_INFO).child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("vip_expiration_date").exists()) {
                        TextView email = findViewById(R.id.vip_textview);
                        email.setText(snapshot.child("vip_expiration_date").getValue(String.class));
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }
}
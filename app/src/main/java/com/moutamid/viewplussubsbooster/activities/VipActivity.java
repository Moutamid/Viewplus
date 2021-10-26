package com.moutamid.viewplussubsbooster.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplussubsbooster.R;
import com.moutamid.viewplussubsbooster.databinding.ActivityVipBinding;

public class VipActivity extends AppCompatActivity {
    private ActivityVipBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityVipBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        getCoinsAmount();

    }

    private void getCoinsAmount() {

        FirebaseAuth mAuth;
        TextView coinsTextView;
        String USER_INFO = "userinfo";
        coinsTextView = findViewById(R.id.coins_text_view_vip);
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(USER_INFO).child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String value = String.valueOf(snapshot.child("coins").getValue(Integer.class));
                    coinsTextView.setText(value);
                } else {
                    coinsTextView.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String TAG = "oncancelled";
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }
}
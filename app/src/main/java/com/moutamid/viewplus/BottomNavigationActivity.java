package com.moutamid.viewplus;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class BottomNavigationActivity extends AppCompatActivity {
    private static final String TAG = "BottomNavigationActivit";

    private FirebaseAuth mAuth;
    private TextView coinsTextView;
    private static final String USER_INFO = "userinfo";

    private RelativeLayout topHeaderLayout;
    private BottomNavigationView navView;

    public void hideNavBar(){
        if (navView==null){
            navView = findViewById(R.id.nav_view);
        }
        navView.setVisibility(View.GONE);
    }

    public void showNavBar(){
        if (navView==null){
            navView = findViewById(R.id.nav_view);
        }
        navView.setVisibility(View.VISIBLE);
    }

    public void hideTopHeader(){
        if (topHeaderLayout == null){
            topHeaderLayout = findViewById(R.id.top_header_layout_bottom_navigation);
        }

        topHeaderLayout.setVisibility(View.GONE);
    }

    public void showTopHeader(){
        if (topHeaderLayout == null){
            topHeaderLayout = findViewById(R.id.top_header_layout_bottom_navigation);
        }

        topHeaderLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        coinsTextView = findViewById(R.id.coins_text_view_bottom_navigation);
        topHeaderLayout = findViewById(R.id.top_header_layout_bottom_navigation);
        navView = findViewById(R.id.nav_view);

        mAuth = FirebaseAuth.getInstance();

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_moutamid);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.okayBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CODE HERE
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            finish();
            startActivity(new Intent(BottomNavigationActivity.this, MainActivity.class));
            return;
        }
        initNavigationMenu();

        getCoinsAmount();
    }

    private void getCoinsAmount() {
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
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    private void initNavigationMenu() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_view_youtube, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

}
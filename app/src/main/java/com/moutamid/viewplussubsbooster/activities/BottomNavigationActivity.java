package com.moutamid.viewplussubsbooster.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplussubsbooster.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class BottomNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "BottomNavigationActivit";

    private DrawerLayout drawerLayout;
    //    private FrameLayout frameLayout;
    private NavigationView navigationView;

    private FirebaseAuth mAuth;
    private TextView coinsTextView;
    private static final String USER_INFO = "userinfo";

    private RelativeLayout topHeaderLayout;
    private BottomNavigationView navView;

    public void hideNavBar() {

        if (navView == null) {
            navView = findViewById(R.id.nav_view);
        }
        navView.setVisibility(View.GONE);
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
//        dialog.show();
//        dialog.getWindow().setAttributes(layoutParams);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            finish();
            startActivity(new Intent(BottomNavigationActivity.this, MainActivity.class));
            return;
        }
        initNavigationMenu();

        getCoinsAmount();

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState, 0);

    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
        navView.setSelectedItemId(R.id.navigation_view);
    }

    private void toggleDrawer() {
//        new ActionBarDrawerToggle()

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.buy_points_nav_option:
                navView.getMenu().getItem(0).setChecked(true);
                navView.setSelectedItemId(R.id.navigation_points);
                closeDrawer();
                break;
            case R.id.subscribe_nav_option:
                navView.getMenu().getItem(1).setChecked(true);
                navView.setSelectedItemId(R.id.navigation_subscribe);
                closeDrawer();
                break;
            case R.id.like_nav_option:
                navView.getMenu().getItem(2).setChecked(true);
                navView.setSelectedItemId(R.id.navigation_like);
                closeDrawer();
                break;
            case R.id.view_nav_option:
                navView.getMenu().getItem(3).setChecked(true);
                navView.setSelectedItemId(R.id.navigation_view);
                closeDrawer();
                break;
            case R.id.campaign_nav_option:
                navView.getMenu().getItem(4).setChecked(true);
                navView.setSelectedItemId(R.id.navigation_campaign);
                closeDrawer();
                break;
            case R.id.privacy_policy_nav_option:
                closeDrawer();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.stackoverflow.com")));
                break;
            case R.id.exit_nav_option:
                closeDrawer();
                finish();
                break;

        }
        return true;
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    private void initializeViews() {
        drawerLayout = findViewById(R.id.container);
//        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.menu_option_bottomm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
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
                R.id.navigation_campaign, R.id.navigation_view, R.id.navigation_points)
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

    public void showNavBar() {
        if (navView == null) {
            navView = findViewById(R.id.nav_view);
        }
        navView.setVisibility(View.VISIBLE);
    }

    public void hideTopHeader() {
        if (topHeaderLayout == null) {
            topHeaderLayout = findViewById(R.id.top_header_layout_bottom_navigation);
        }

        topHeaderLayout.setVisibility(View.GONE);
    }

    public void showTopHeader() {
        if (topHeaderLayout == null) {
            topHeaderLayout = findViewById(R.id.top_header_layout_bottom_navigation);
        }

        topHeaderLayout.setVisibility(View.VISIBLE);
    }
}
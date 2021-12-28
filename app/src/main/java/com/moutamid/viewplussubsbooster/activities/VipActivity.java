package com.moutamid.viewplussubsbooster.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.billingclient.api.Purchase;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseData;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.PurchaseState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplussubsbooster.R;
import com.moutamid.viewplussubsbooster.databinding.ActivityBuyPointsBinding;
import com.moutamid.viewplussubsbooster.databinding.ActivityVipBinding;
import com.moutamid.viewplussubsbooster.utils.Constants;
import com.moutamid.viewplussubsbooster.utils.Utils;

import java.util.HashMap;

public class VipActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    private static final String TAG = "VipActivity";
    private ActivityVipBinding b;

    private Context context = VipActivity.this;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityVipBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        getCoinsAmount();

        /**
         * ALSO ADDED A SUBSCRIPTIONS CHECK IN MAIN-ACTIVITY
         * THAT IF SUBSCRIPTIONS ARE CANCELLED OR ACTIVE
         * */

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        b.oneWeekVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.subscribe(VipActivity.this, Constants.ONE_WEEK_SUBSCRIPTION);
            }
        });
        b.oneMonthVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.subscribe(VipActivity.this, Constants.ONE_MONTH_SUBSCRIPTION);
            }
        });
        b.threeMonthsVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.subscribe(VipActivity.this, Constants.THREE_MONTHS_SUBSCRIPTION);
            }
        });

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

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        Utils.toast("Purchase Successful!");
        Utils.toast(productId);

        Utils.store(Constants.VIP_STATUS, true);

        Utils.store(productId, true);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.USER_ID, mAuth.getUid());
        hashMap.put(Constants.PRODUCT_TYPE, productId);
        hashMap.put(Constants.PURCHASE_DATE, Utils.getDate());

        // ADD VALUES OF NEW PURCHASE TO DATABASE
        databaseReference.child(Constants.PATH_SUBSCRIPTIONS)
                .push()
                .setValue(hashMap);

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Utils.toast("onBillingError: code: " + errorCode + " \n" + error.getMessage());
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
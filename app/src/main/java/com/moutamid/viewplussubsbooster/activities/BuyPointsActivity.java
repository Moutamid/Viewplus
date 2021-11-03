package com.moutamid.viewplussubsbooster.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplussubsbooster.R;
import com.moutamid.viewplussubsbooster.databinding.ActivityBuyPointsBinding;
import com.moutamid.viewplussubsbooster.utils.Constants;
import com.moutamid.viewplussubsbooster.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BuyPointsActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    private static final String TAG = "BuyPointsActivity";
    private Context context = BuyPointsActivity.this;

    private ActivityBuyPointsBinding b;

    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityBuyPointsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        getCoinsAmount();

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();
//        inAppPurchases();

        b.buyPoints1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*if (getPurchaseSharedPreference()) {
                    Toast.makeText(context, "Already Subscribed", Toast.LENGTH_SHORT).show();
                } else {

                    if (billingClient.isReady()) {
                        Utils.toast("continue");
                        skuQueryOnContinue();
                    } else {
                        Toast.makeText(context, "Something wrong!", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });
    }

    public void btSubscribe(View view) {

    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        Utils.toast("onProductPurchased");
        Utils.toast(productId);
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Utils.toast("onPurchaseHistoryRestored");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Utils.toast("onBillingError: code: " + errorCode + " \n" + error.getMessage());
    }

    @Override
    public void onBillingInitialized() {
        Utils.toast("onBillingInitialized");

        if (bp.isConnected()){
            Utils.toast("CONNECTED!");
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }


    private void getCoinsAmount() {

        FirebaseAuth mAuth;
        TextView coinsTextView;
        String USER_INFO = "userinfo";
        coinsTextView = findViewById(R.id.coins_text_view_buy_points);
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

    /*//------------------------------------------------------------------------------------------
//    private BillingClient billingClient;
//    private List<String> skuList;

    public void inAppPurchases() {
        // To be implemented in a later section.
        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(context.getString(R.string.adsubscribed), Boolean.TRUE);
                    edit.apply();
                    Toast.makeText(context, "Thanks for purchase.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ///create client billing
        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        //make connect to google play store
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
//        skuProductList();
    }

    public void btSubscribe(View view) {
        if (getPurchaseSharedPreference()) {
            Toast.makeText(context, "Already Subscribed", Toast.LENGTH_SHORT).show();
        } else {

            if (billingClient.isReady()) {
                skuQueryOnContinue();
            } else {
                Toast.makeText(context, "Something wrong!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void skuQueryOnContinue() {
        try {
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            if (skuList.size() > 0) {

                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            // crashes due to null list of SkuDetails objects //
                            try {
                                assert list != null;
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(list.get(0))
                                        .build();
                                int responseCode = billingClient.launchBillingFlow(BuyPointsActivity.this,
                                        flowParams).getResponseCode();
                                Utils.toast(responseCode + "");
                            } catch (Exception ignored) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.toast("ERROR: App need to be published!");//+ignored.getMessage());
                                    }
                                });
                            }
                        } else {
                            Utils.toast("Billing response is not ok");
                        }
                    }
                });
            } else Utils.toast("Size is 0");
        } catch (Exception ignored) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.toast("ERROR: App need to be published!");//"FIRST CATCH: "+ignored.getMessage());
                }
            });
        }
    }

    public void skuProductList() {
        skuList = new ArrayList<>();
        skuList.add(getResources().getString(R.string.per_6_month_subcription));
    }

    public boolean getPurchaseSharedPreference() {
        SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        return prefs.getBoolean(this.getString(R.string.adsubscribed), false);
    }*/
}
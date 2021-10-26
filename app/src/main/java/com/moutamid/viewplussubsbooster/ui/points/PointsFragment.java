package com.moutamid.viewplussubsbooster.ui.points;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.moutamid.viewplussubsbooster.R;
import com.moutamid.viewplussubsbooster.activities.BuyPointsActivity;
import com.moutamid.viewplussubsbooster.activities.VipActivity;
import com.moutamid.viewplussubsbooster.databinding.FragmentPointsBinding;

public class PointsFragment extends Fragment {
    private static final String TAG = "PointsFragment";

    private FragmentPointsBinding b;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentPointsBinding.inflate(inflater, container, false);

        b.feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedBackDialog();
            }
        });

        b.buyPointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), BuyPointsActivity.class));

            }
        });

        b.vipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), VipActivity.class));
            }
        });

        /*root.findViewById(R.id.buy_coins_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), BuyCoinsActivity.class));
            }
        });

        root.findViewById(R.id.personal_information_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
            }
        });

        *//*root.findViewById(R.id.logout_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                getActivity().finish();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });*//*
         */
        return b.getRoot();
    }

    private void showFeedBackDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.laterBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CODE HERE
                dialog.dismiss();
            }
        });

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                final String appPackageName = requireContext().getPackageName();

                dialog.dismiss();

                final String appPackageName = "com.whatsapp";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }
}
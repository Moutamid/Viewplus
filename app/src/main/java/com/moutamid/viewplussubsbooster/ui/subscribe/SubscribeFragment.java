package com.moutamid.viewplussubsbooster.ui.subscribe;

import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;
import static com.moutamid.viewplussubsbooster.R.color.lighterGrey;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplussubsbooster.R;
import com.moutamid.viewplussubsbooster.databinding.FragmentSubscribeBinding;
import com.moutamid.viewplussubsbooster.models.SubscribeTaskModel;
import com.moutamid.viewplussubsbooster.utils.Constants;
import com.moutamid.viewplussubsbooster.utils.Helper;
import com.moutamid.viewplussubsbooster.utils.Utils;

import java.util.ArrayList;

public class SubscribeFragment extends Fragment {


    public SubscribeFragment() {
        // Required empty public constructor
    }

    private FragmentSubscribeBinding b;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<SubscribeTaskModel> subscribeTaskModelArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    int currentCounter = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentSubscribeBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.child(Constants.SUBSCRIBE_TASKS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    progressDialog.dismiss();
                    Utils.toast("No data exist!");
                    return;
                }

                subscribeTaskModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    SubscribeTaskModel model = dataSnapshot.getValue(SubscribeTaskModel.class);
                    model.setSubscribed(false);

                    if (dataSnapshot.child(Constants.SUBSCRIBER_PATH).child(mAuth.getUid()).exists()) {
                        model.setSubscribed(true);
                    }

                    subscribeTaskModelArrayList.add(model);

                }
                progressDialog.dismiss();
                setDataOnViews(0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b.seeNextBtnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCounter++;

                if (currentCounter >= subscribeTaskModelArrayList.size()) {
                    Utils.toast("End of tasks!");

                } else setDataOnViews(currentCounter);

            }
        });

        b.subscribeBtnSubscribeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utils.toast("Coming soon!");

            }
        });

        return b.getRoot();
    }

    boolean isTimerRunning = false;

    private void setDataOnViews(int counter) {
        progressDialog.show();

        with(requireContext())
                .asBitmap()
                .load(subscribeTaskModelArrayList.get(counter).getThumbnailUrl())
                .apply(new RequestOptions()
                        .placeholder(lighterGrey)
                        .error(lighterGrey)
                )
                .diskCacheStrategy(DATA)
                .into(b.videoImageSubscribe);


        b.videoIdSubscribe.setText(
                "Video Id: " + Helper.getVideoId(subscribeTaskModelArrayList.get(counter).getVideoUrl())
        );

        progressDialog.dismiss();
/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        })*/
    }
}
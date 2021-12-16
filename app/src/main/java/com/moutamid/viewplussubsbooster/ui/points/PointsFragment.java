package com.moutamid.viewplussubsbooster.ui.points;

import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static com.google.firebase.database.FirebaseDatabase.getInstance;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.viewplussubsbooster.R;
import com.moutamid.viewplussubsbooster.activities.AddTaskActivity;
import com.moutamid.viewplussubsbooster.activities.BuyPointsActivity;
import com.moutamid.viewplussubsbooster.activities.VipActivity;
import com.moutamid.viewplussubsbooster.databinding.FragmentPointsBinding;
import com.moutamid.viewplussubsbooster.models.LikeTaskModel;
import com.moutamid.viewplussubsbooster.models.SubscribeTaskModel;
import com.moutamid.viewplussubsbooster.utils.Constants;
import com.moutamid.viewplussubsbooster.utils.Helper;

import java.util.Random;

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
//                startActivity(new Intent(getActivity(), TestCoinsActivity.class));
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


/*//        String thumb1 = "https://i.ytimg.com/vi/kLklz6JZHpA/hqdefault.jpg";
        String part1 = "https://i.ytimg.com/vi/";
//        String id = "kLklz6JZHpA";
        String part2 = "/hqdefault.jpg";

        for (String videoUrl : linksArray) {
            String thumb = part1 + Helper.getVideoId(videoUrl) + part2;
            uploadViewVideos(videoUrl, thumb);
            uploadLikeVideos(videoUrl, thumb);
            uploadSubscribeVideos(videoUrl, thumb);
        }*/

        return b.getRoot();
    }

    private void uploadViewVideos(String videoUrl, String thumbnailUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String key = databaseReference.child("tasks").push().getKey();

        PointsFragment.Task task1 = new PointsFragment.Task();
        task1.setVideoUrl(videoUrl);
        task1.setThumbnailUrl(thumbnailUrl);
        task1.setTotalViewsQuantity("" + new Random().nextInt(100) + 1500);
        task1.setTotalViewTimeQuantity("" + 60);
        task1.setCompletedDate("error");
        task1.setCurrentViewsQuantity(0);
        task1.setPosterUid(auth.getCurrentUser().getUid());
        task1.setTaskKey(key);

        databaseReference.child("tasks").child(key).setValue(task1);

    }

    private void uploadLikeVideos(String videoUrl, String thumbnailUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String key = databaseReference.child(Constants.LIKE_TASKS).push().getKey();

        LikeTaskModel task1 = new LikeTaskModel();
        task1.setVideoUrl(videoUrl);
        task1.setThumbnailUrl(thumbnailUrl);
        task1.setTotalLikesQuantity("" + new Random().nextInt(100) + 1500);
        task1.setCompletedDate("error");
        task1.setCurrentLikesQuantity(0);
        task1.setPosterUid(auth.getCurrentUser().getUid());
        task1.setTaskKey(key);

        databaseReference.child(Constants.LIKE_TASKS).child(key).setValue(task1);

    }

    private void uploadSubscribeVideos(String videoUrl, String thumbnailUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String key = databaseReference.child(Constants.SUBSCRIBE_TASKS).push().getKey();

        SubscribeTaskModel task1 = new SubscribeTaskModel();
        task1.setVideoUrl(videoUrl);
        task1.setThumbnailUrl(thumbnailUrl);
        task1.setTotalSubscribesQuantity("" + new Random().nextInt(100) + 1500);
        task1.setCompletedDate("error");
        task1.setCurrentSubscribesQuantity(0);
        task1.setPosterUid(auth.getCurrentUser().getUid());
        task1.setTaskKey(key);

        databaseReference.child(Constants.SUBSCRIBE_TASKS).child(key).setValue(task1);

    }

    private static class Task {

        private String videoUrl, thumbnailUrl, posterUid, taskKey,
                totalViewsQuantity, totalViewTimeQuantity, completedDate;
        private int currentViewsQuantity;

        public Task(String videoUrl, String thumbnailUrl, String posterUid, String taskKey, String totalViewsQuantity, String totalViewTimeQuantity, String completedDate, int currentViewsQuantity) {
            this.videoUrl = videoUrl;
            this.thumbnailUrl = thumbnailUrl;
            this.posterUid = posterUid;
            this.taskKey = taskKey;
            this.totalViewsQuantity = totalViewsQuantity;
            this.totalViewTimeQuantity = totalViewTimeQuantity;
            this.completedDate = completedDate;
            this.currentViewsQuantity = currentViewsQuantity;
        }

        public String getPosterUid() {
            return posterUid;
        }

        public void setPosterUid(String posterUid) {
            this.posterUid = posterUid;
        }

        public String getTaskKey() {
            return taskKey;
        }

        public void setTaskKey(String taskKey) {
            this.taskKey = taskKey;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getTotalViewsQuantity() {
            return totalViewsQuantity;
        }

        public void setTotalViewsQuantity(String totalViewsQuantity) {
            this.totalViewsQuantity = totalViewsQuantity;
        }

        public String getTotalViewTimeQuantity() {
            return totalViewTimeQuantity;
        }

        public void setTotalViewTimeQuantity(String totalViewTimeQuantity) {
            this.totalViewTimeQuantity = totalViewTimeQuantity;
        }

        public String getCompletedDate() {
            return completedDate;
        }

        public void setCompletedDate(String completedDate) {
            this.completedDate = completedDate;
        }

        public int getCurrentViewsQuantity() {
            return currentViewsQuantity;
        }

        public void setCurrentViewsQuantity(int currentViewsQuantity) {
            this.currentViewsQuantity = currentViewsQuantity;
        }

        Task() {
        }
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

    public static String[] linksArray = {"https://www.youtube.com/watch?v=0qCnTs06Fvw",
            "https://www.youtube.com/watch?v=0qCnTs06Fvw",
            "https://www.youtube.com/watch?v=AuX9Yucn9qQ",
            "https://www.youtube.com/watch?v=AuX9Yucn9qQ",
            "https://www.youtube.com/watch?v=S3lwAFED8Pg",
            "https://www.youtube.com/watch?v=S3lwAFED8Pg",
            "https://www.youtube.com/watch?v=ck6uvB1xlss",
            "https://www.youtube.com/watch?v=ck6uvB1xlss",
            "https://www.youtube.com/watch?v=-vlskP9VcMA",
            "https://www.youtube.com/watch?v=-vlskP9VcMA",
            "https://www.youtube.com/watch?v=L2CjW0uPB94",
            "https://www.youtube.com/watch?v=L2CjW0uPB94",
            "https://www.youtube.com/watch?v=V8HAdmsAjLw",
            "https://www.youtube.com/watch?v=V8HAdmsAjLw",
            "https://www.youtube.com/watch?v=k62uAgk9_Mo",
            "https://www.youtube.com/watch?v=k62uAgk9_Mo",
            "https://www.youtube.com/watch?v=ciag2xlZZAU",
            "https://www.youtube.com/watch?v=ciag2xlZZAU",
            "https://www.youtube.com/watch?v=t_p0IOiIOE8",
            "https://www.youtube.com/watch?v=t_p0IOiIOE8",
            "https://www.youtube.com/watch?v=1Ms-4IaYywk",
            "https://www.youtube.com/watch?v=1Ms-4IaYywk",
            "https://www.youtube.com/watch?v=eay0stZYE04",
            "https://www.youtube.com/watch?v=eay0stZYE04",
            "https://www.youtube.com/watch?v=Dd6iSXSW_N8",
            "https://www.youtube.com/watch?v=Dd6iSXSW_N8",
            "https://www.youtube.com/watch?v=LQ60Mkle4FY",
            "https://www.youtube.com/watch?v=LQ60Mkle4FY",
            "https://www.youtube.com/watch?v=4T_SVy13mUk",
            "https://www.youtube.com/watch?v=4T_SVy13mUk",
            "https://www.youtube.com/watch?v=Nf35GryPv_M",
            "https://www.youtube.com/watch?v=Nf35GryPv_M",
            "https://www.youtube.com/watch?v=Lhi6ebubwNQ",
            "https://www.youtube.com/watch?v=Lhi6ebubwNQ",
            "https://www.youtube.com/watch?v=Z-olKNNBdAM",
            "https://www.youtube.com/watch?v=Z-olKNNBdAM",
            "https://www.youtube.com/watch?v=9LQsMwV4lvA",
            "https://www.youtube.com/watch?v=9LQsMwV4lvA",
            "https://www.youtube.com/watch?v=u5SSuIKRNnk",
            "https://www.youtube.com/watch?v=u5SSuIKRNnk",
            "https://www.youtube.com/watch?v=4aezXUeyNFM",
            "https://www.youtube.com/watch?v=4aezXUeyNFM",
            "https://www.youtube.com/watch?v=nVTmfbvBw_w",
            "https://www.youtube.com/watch?v=nVTmfbvBw_w",
            "https://www.youtube.com/watch?v=AnKSnJQMWAY",
            "https://www.youtube.com/watch?v=AnKSnJQMWAY",
            "https://www.youtube.com/watch?v=iwRQ5eSRKrE",
            "https://www.youtube.com/watch?v=iwRQ5eSRKrE",
            "https://www.youtube.com/watch?v=U55xX9F2kqo",
            "https://www.youtube.com/watch?v=U55xX9F2kqo",
            "https://www.youtube.com/watch?v=V9d7Gr0MC8o",
            "https://www.youtube.com/watch?v=V9d7Gr0MC8o",
            "https://www.youtube.com/watch?v=tyD2kYAbJEg",
            "https://www.youtube.com/watch?v=tyD2kYAbJEg",
            "https://www.youtube.com/watch?v=ilPj6UnjcmE",
            "https://www.youtube.com/watch?v=ilPj6UnjcmE",
            "https://www.youtube.com/watch?v=gjeIJBJ-yww",
            "https://www.youtube.com/watch?v=gjeIJBJ-yww",
            "https://www.youtube.com/watch?v=URpU7I1OB-0",
            "https://www.youtube.com/watch?v=URpU7I1OB-0",
            "https://www.youtube.com/watch?v=FKSrA95LfAE",
            "https://www.youtube.com/watch?v=FKSrA95LfAE",
            "https://www.youtube.com/watch?v=m8i8gNAdXi4",
            "https://www.youtube.com/watch?v=m8i8gNAdXi4",
            "https://www.youtube.com/watch?v=uqMgzjOKiuc",
            "https://www.youtube.com/watch?v=uqMgzjOKiuc",
            "https://www.youtube.com/watch?v=3NW0Awfwhw4",
            "https://www.youtube.com/watch?v=3NW0Awfwhw4",
            "https://www.youtube.com/watch?v=2Xg59f9UEqk",
            "https://www.youtube.com/watch?v=2Xg59f9UEqk",
            "https://www.youtube.com/watch?v=obfKz0E3XXU",
            "https://www.youtube.com/watch?v=obfKz0E3XXU",
            "https://www.youtube.com/watch?v=e-LE4O_DVJg",
            "https://www.youtube.com/watch?v=e-LE4O_DVJg",
            "https://www.youtube.com/watch?v=OoBxoYbXM_U",
            "https://www.youtube.com/watch?v=OoBxoYbXM_U",
            "https://www.youtube.com/watch?v=pshLMg0_uq8",
            "https://www.youtube.com/watch?v=pshLMg0_uq8",
            "https://www.youtube.com/watch?v=9753gP09EvA",
            "https://www.youtube.com/watch?v=9753gP09EvA",
            "https://www.youtube.com/watch?v=763b0wbVrD8",
            "https://www.youtube.com/watch?v=763b0wbVrD8",
            "https://www.youtube.com/watch?v=6OfCfe8LMXU",
            "https://www.youtube.com/watch?v=6OfCfe8LMXU",
            "https://www.youtube.com/watch?v=uRKeeelqWxw",
            "https://www.youtube.com/watch?v=uRKeeelqWxw",
            "https://www.youtube.com/watch?v=sdMbVpzIPDg",
            "https://www.youtube.com/watch?v=sdMbVpzIPDg",
            "https://www.youtube.com/watch?v=_CqXt3irYhU",
            "https://www.youtube.com/watch?v=_CqXt3irYhU",
            "https://www.youtube.com/watch?v=FVZrhX1TFK0",
            "https://www.youtube.com/watch?v=FVZrhX1TFK0",
            "https://www.youtube.com/watch?v=oUEJD4-Tt0E",
            "https://www.youtube.com/watch?v=oUEJD4-Tt0E",
            "https://www.youtube.com/watch?v=QmO-lTyT3DA",
            "https://www.youtube.com/watch?v=QmO-lTyT3DA",
            "https://www.youtube.com/watch?v=-nkfj3wk7eg",
            "https://www.youtube.com/watch?v=-nkfj3wk7eg",
            "https://www.youtube.com/watch?v=gux54Essx8Q",
            "https://www.youtube.com/watch?v=gux54Essx8Q",
            "https://www.youtube.com/watch?v=JWgeRmKP72o",
            "https://www.youtube.com/watch?v=JWgeRmKP72o",
            "https://www.youtube.com/watch?v=Hv-KfIZ1Us0",
            "https://www.youtube.com/watch?v=Hv-KfIZ1Us0",
            "https://www.youtube.com/watch?v=MobGYXeUlvE",
            "https://www.youtube.com/watch?v=MobGYXeUlvE",
            "https://www.youtube.com/watch?v=YbBcugBQ8l4",
            "https://www.youtube.com/watch?v=YbBcugBQ8l4",
            "https://www.youtube.com/watch?v=UGkrKOyUJZ4",
            "https://www.youtube.com/watch?v=UGkrKOyUJZ4",
            "https://www.youtube.com/watch?v=vDTLzzPGw0k",
            "https://www.youtube.com/watch?v=vDTLzzPGw0k",
            "https://www.youtube.com/watch?v=bjVoW3iNEjA",
            "https://www.youtube.com/watch?v=bjVoW3iNEjA",
            "https://www.youtube.com/watch?v=pM2AJCv3nXA",
            "https://www.youtube.com/watch?v=pM2AJCv3nXA",
            "https://www.youtube.com/watch?v=yBqbUBg2HjM",
            "https://www.youtube.com/watch?v=yBqbUBg2HjM",
            "https://www.youtube.com/watch?v=6rmBZjO0P-8",
            "https://www.youtube.com/watch?v=6rmBZjO0P-8",
            "https://www.youtube.com/watch?v=1NGtF1veteQ",
            "https://www.youtube.com/watch?v=1NGtF1veteQ",
            "https://www.youtube.com/watch?v=HJ1ZXmpi8Tw",
            "https://www.youtube.com/watch?v=HJ1ZXmpi8Tw",
            "https://www.youtube.com/watch?v=FvjSSCZWTYQ",
            "https://www.youtube.com/watch?v=FvjSSCZWTYQ",
            "https://www.youtube.com/watch?v=BQcp1ZNWgTs",
            "https://www.youtube.com/watch?v=BQcp1ZNWgTs",
            "https://www.youtube.com/watch?v=fhbZT0Dpipc",
            "https://www.youtube.com/watch?v=fhbZT0Dpipc",
            "https://www.youtube.com/watch?v=bwEXBhZ7Olo",
            "https://www.youtube.com/watch?v=bwEXBhZ7Olo",
            "https://www.youtube.com/watch?v=hMb4qC-_wKY",
            "https://www.youtube.com/watch?v=hMb4qC-_wKY",
            "https://www.youtube.com/watch?v=UMa5jwanij0",
            "https://www.youtube.com/watch?v=UMa5jwanij0",
            "https://www.youtube.com/watch?v=jat-a3aG3tc",
            "https://www.youtube.com/watch?v=jat-a3aG3tc",
            "https://www.youtube.com/watch?v=MDILEkMNFUI",
            "https://www.youtube.com/watch?v=MDILEkMNFUI",
            "https://www.youtube.com/watch?v=5TvwDgYKirU",
            "https://www.youtube.com/watch?v=5TvwDgYKirU",
            "https://www.youtube.com/watch?v=0eNNJ26MmgI",
            "https://www.youtube.com/watch?v=0eNNJ26MmgI",
            "https://www.youtube.com/watch?v=k_WcjMnuqSg",
            "https://www.youtube.com/watch?v=k_WcjMnuqSg",
            "https://www.youtube.com/watch?v=FbYheQxhoDw",
            "https://www.youtube.com/watch?v=FbYheQxhoDw",
            "https://www.youtube.com/watch?v=uXXPbti2nVM",
            "https://www.youtube.com/watch?v=uXXPbti2nVM",
            "https://www.youtube.com/watch?v=f66GFlYghv8",
            "https://www.youtube.com/watch?v=f66GFlYghv8",
            "https://www.youtube.com/watch?v=ftqCKMzdS5k",
            "https://www.youtube.com/watch?v=ftqCKMzdS5k",
            "https://www.youtube.com/watch?v=89gNSvqzCME",
            "https://www.youtube.com/watch?v=89gNSvqzCME",
            "https://www.youtube.com/watch?v=403FGqa-Uv8",
            "https://www.youtube.com/watch?v=403FGqa-Uv8",
            "https://www.youtube.com/watch?v=fBNHHVu5Njo",
            "https://www.youtube.com/watch?v=fBNHHVu5Njo",
            "https://www.youtube.com/watch?v=gMO9wJZ4vAw",
            "https://www.youtube.com/watch?v=gMO9wJZ4vAw",
            "https://www.youtube.com/watch?v=sO9EMiez-ZA",
            "https://www.youtube.com/watch?v=sO9EMiez-ZA",
            "https://www.youtube.com/watch?v=syEDu6AihFs",
            "https://www.youtube.com/watch?v=syEDu6AihFs",
            "https://www.youtube.com/watch?v=Rmrbl3Y28IM",
            "https://www.youtube.com/watch?v=Rmrbl3Y28IM",
            "https://www.youtube.com/watch?v=xEa8W4b5vzQ",
            "https://www.youtube.com/watch?v=xEa8W4b5vzQ",
            "https://www.youtube.com/watch?v=YGKyFhd4DbU",
            "https://www.youtube.com/watch?v=YGKyFhd4DbU",
            "https://www.youtube.com/watch?v=wURAlc6ociw",
            "https://www.youtube.com/watch?v=wURAlc6ociw",
            "https://www.youtube.com/watch?v=alYkT0pvGd4",
            "https://www.youtube.com/watch?v=alYkT0pvGd4",
            "https://www.youtube.com/watch?v=mQ-6RD9x2tc",
            "https://www.youtube.com/watch?v=mQ-6RD9x2tc",
            "https://www.youtube.com/watch?v=UKNIeUuGCkM",
            "https://www.youtube.com/watch?v=UKNIeUuGCkM",
            "https://www.youtube.com/watch?v=_4K-5CAl648",
            "https://www.youtube.com/watch?v=_4K-5CAl648",
            "https://www.youtube.com/watch?v=oPYiFeVyVE0",
            "https://www.youtube.com/watch?v=oPYiFeVyVE0",
            "https://www.youtube.com/watch?v=z8Epapm6Lic",
            "https://www.youtube.com/watch?v=z8Epapm6Lic",
            "https://www.youtube.com/watch?v=W5CMQjyejrE",
            "https://www.youtube.com/watch?v=W5CMQjyejrE",
            "https://www.youtube.com/watch?v=byDe7X_E83g",
            "https://www.youtube.com/watch?v=byDe7X_E83g",
            "https://www.youtube.com/watch?v=eE8Bu257MzA",
            "https://www.youtube.com/watch?v=eE8Bu257MzA",
            "https://www.youtube.com/watch?v=jMAS_JCCUEw",
            "https://www.youtube.com/watch?v=jMAS_JCCUEw",
            "https://www.youtube.com/watch?v=_VoLRNABvhc",
            "https://www.youtube.com/watch?v=_VoLRNABvhc",
            "https://www.youtube.com/watch?v=6_hTrNahtFE",
            "https://www.youtube.com/watch?v=6_hTrNahtFE",
            "https://www.youtube.com/watch?v=d51n7RRsNVo",
            "https://www.youtube.com/watch?v=d51n7RRsNVo",
            "https://www.youtube.com/watch?v=tqKqJn8_d00",
            "https://www.youtube.com/watch?v=tqKqJn8_d00",
            "https://www.youtube.com/watch?v=b3H4M10rxgI",
            "https://www.youtube.com/watch?v=b3H4M10rxgI",
            "https://www.youtube.com/watch?v=ZlbDBQrt2tc",
            "https://www.youtube.com/watch?v=ZlbDBQrt2tc",
            "https://www.youtube.com/watch?v=xhBGmBL2168",
            "https://www.youtube.com/watch?v=xhBGmBL2168",
            "https://www.youtube.com/watch?v=XLgeR52GD5M",
            "https://www.youtube.com/watch?v=XLgeR52GD5M",
            "https://www.youtube.com/watch?v=1XNKMaF7M-U",
            "https://www.youtube.com/watch?v=1XNKMaF7M-U",
            "https://www.youtube.com/watch?v=7kQs26pROso",
            "https://www.youtube.com/watch?v=7kQs26pROso",
            "https://www.youtube.com/watch?v=5zLmvjSK0_A",
            "https://www.youtube.com/watch?v=5zLmvjSK0_A",
            "https://www.youtube.com/watch?v=G4d2oO2rEGQ",
            "https://www.youtube.com/watch?v=G4d2oO2rEGQ",
            "https://www.youtube.com/watch?v=mH3b7fhjrNU",
            "https://www.youtube.com/watch?v=mH3b7fhjrNU",
            "https://www.youtube.com/watch?v=fQ_wP-XTmQ0",
            "https://www.youtube.com/watch?v=fQ_wP-XTmQ0",
            "https://www.youtube.com/watch?v=1iP7Tph3fH4",
            "https://www.youtube.com/watch?v=1iP7Tph3fH4",
            "https://www.youtube.com/watch?v=0u5SU9iDzxg",
            "https://www.youtube.com/watch?v=0u5SU9iDzxg",
            "https://www.youtube.com/watch?v=Eqy_35Wwu-8",
            "https://www.youtube.com/watch?v=Eqy_35Wwu-8",
            "https://www.youtube.com/watch?v=Bet7pI3p4_g",
            "https://www.youtube.com/watch?v=Bet7pI3p4_g",
            "https://www.youtube.com/watch?v=7RbTKT-DeTM",
            "https://www.youtube.com/watch?v=7RbTKT-DeTM",
            "https://www.youtube.com/watch?v=jTzg53ldvWE",
            "https://www.youtube.com/watch?v=jTzg53ldvWE",
            "https://www.youtube.com/watch?v=sJmiQ0sST94",
            "https://www.youtube.com/watch?v=sJmiQ0sST94",
            "https://www.youtube.com/watch?v=b8LxR1xo51Q",
            "https://www.youtube.com/watch?v=b8LxR1xo51Q",
            "https://www.youtube.com/watch?v=xLedU39S3Ys",
            "https://www.youtube.com/watch?v=xLedU39S3Ys",
            "https://www.youtube.com/watch?v=bfV22J6QXps",
            "https://www.youtube.com/watch?v=bfV22J6QXps",
            "https://www.youtube.com/watch?v=_gJbqAjobsA",
            "https://www.youtube.com/watch?v=_gJbqAjobsA",
            "https://www.youtube.com/watch?v=B5oxmUJB-WQ",
            "https://www.youtube.com/watch?v=B5oxmUJB-WQ",
            "https://www.youtube.com/watch?v=b-Bkxz6ylSs",
            "https://www.youtube.com/watch?v=b-Bkxz6ylSs",
            "https://www.youtube.com/watch?v=-icQWPBBPZE",
            "https://www.youtube.com/watch?v=-icQWPBBPZE",
            "https://www.youtube.com/watch?v=KvPYiINBUK0",
            "https://www.youtube.com/watch?v=KvPYiINBUK0",
            "https://www.youtube.com/watch?v=ZXGWYe01Ya8",
            "https://www.youtube.com/watch?v=ZXGWYe01Ya8",
            "https://www.youtube.com/watch?v=lXltmnnyiCE",
            "https://www.youtube.com/watch?v=lXltmnnyiCE",
            "https://www.youtube.com/watch?v=-lvcZeXlVpE",
            "https://www.youtube.com/watch?v=-lvcZeXlVpE",
            "https://www.youtube.com/watch?v=ARTmkTQnxRc",
            "https://www.youtube.com/watch?v=ARTmkTQnxRc",
            "https://www.youtube.com/watch?v=-S4tqUyf0ik",
            "https://www.youtube.com/watch?v=-S4tqUyf0ik",
            "https://www.youtube.com/watch?v=3RXnBrWkm9Q",
            "https://www.youtube.com/watch?v=3RXnBrWkm9Q",
            "https://www.youtube.com/watch?v=BC_JlETHf8M",
            "https://www.youtube.com/watch?v=BC_JlETHf8M",
            "https://www.youtube.com/watch?v=7oWvpt9k9WM",
            "https://www.youtube.com/watch?v=7oWvpt9k9WM",
            "https://www.youtube.com/watch?v=xSs5Syrfq3g",
            "https://www.youtube.com/watch?v=xSs5Syrfq3g",
            "https://www.youtube.com/watch?v=BY8Gcogiu0M",
            "https://www.youtube.com/watch?v=BY8Gcogiu0M",
            "https://www.youtube.com/watch?v=5wrnyHTgXaw",
            "https://www.youtube.com/watch?v=5wrnyHTgXaw",
            "https://www.youtube.com/watch?v=HoFizLtAZMo",
            "https://www.youtube.com/watch?v=HoFizLtAZMo",
            "https://www.youtube.com/watch?v=3cxoR-HIAaU",
            "https://www.youtube.com/watch?v=3cxoR-HIAaU",
            "https://www.youtube.com/watch?v=Vq2_AlW8m4A",
            "https://www.youtube.com/watch?v=Vq2_AlW8m4A",
            "https://www.youtube.com/watch?v=Nnrj9E-E6hY",
            "https://www.youtube.com/watch?v=Nnrj9E-E6hY",
            "https://www.youtube.com/watch?v=BFObB_slgsM",
            "https://www.youtube.com/watch?v=BFObB_slgsM",
            "https://www.youtube.com/watch?v=OaN6BYzwCZ0",
            "https://www.youtube.com/watch?v=OaN6BYzwCZ0",
            "https://www.youtube.com/watch?v=AkOp2mNNM1M",
            "https://www.youtube.com/watch?v=AkOp2mNNM1M",
            "https://www.youtube.com/watch?v=38G_mbs7TZ4",
            "https://www.youtube.com/watch?v=38G_mbs7TZ4",
            "https://www.youtube.com/watch?v=j0XL1JMol0Q",
            "https://www.youtube.com/watch?v=j0XL1JMol0Q",
            "https://www.youtube.com/watch?v=R6cxc-ujUc4",
            "https://www.youtube.com/watch?v=R6cxc-ujUc4",
            "https://www.youtube.com/watch?v=Uxhc0mq_c48",
            "https://www.youtube.com/watch?v=Uxhc0mq_c48",
            "https://www.youtube.com/watch?v=Z3aZABXrAeI",
            "https://www.youtube.com/watch?v=Z3aZABXrAeI",
            "https://www.youtube.com/watch?v=fpbXvUZ8b20",
            "https://www.youtube.com/watch?v=fpbXvUZ8b20",
            "https://www.youtube.com/watch?v=8aQqdUW1S1I",
            "https://www.youtube.com/watch?v=8aQqdUW1S1I",
            "https://www.youtube.com/watch?v=Fqofl_nR7tU",
            "https://www.youtube.com/watch?v=Fqofl_nR7tU",
            "https://www.youtube.com/watch?v=x76iJz_9JO0",
            "https://www.youtube.com/watch?v=x76iJz_9JO0",
            "https://www.youtube.com/watch?v=tU5VO1OfAsE",
            "https://www.youtube.com/watch?v=tU5VO1OfAsE",
            "https://www.youtube.com/watch?v=i2ydtYdVNqM",
            "https://www.youtube.com/watch?v=i2ydtYdVNqM",
            "https://www.youtube.com/watch?v=YPBMaBNdHYs",
            "https://www.youtube.com/watch?v=YPBMaBNdHYs",
            "https://www.youtube.com/watch?v=a_Gfuzpdk9M",
            "https://www.youtube.com/watch?v=a_Gfuzpdk9M",
            "https://www.youtube.com/watch?v=C71o6KR3Qk8",
            "https://www.youtube.com/watch?v=C71o6KR3Qk8",
            "https://www.youtube.com/watch?v=fiiyNhovsrI",
            "https://www.youtube.com/watch?v=fiiyNhovsrI",
            "https://www.youtube.com/watch?v=pZnfm2eYoxU",
            "https://www.youtube.com/watch?v=pZnfm2eYoxU",
            "https://www.youtube.com/watch?v=lWxsby4Dx1A",
            "https://www.youtube.com/watch?v=lWxsby4Dx1A",
            "https://www.youtube.com/watch?v=TpjLNPxekwU",
            "https://www.youtube.com/watch?v=TpjLNPxekwU",
            "https://www.youtube.com/watch?v=S7XFmrOUx4g",
            "https://www.youtube.com/watch?v=S7XFmrOUx4g",
            "https://www.youtube.com/watch?v=WhSmUIZl0T0",
            "https://www.youtube.com/watch?v=WhSmUIZl0T0",
            "https://www.youtube.com/watch?v=CVP1CwEBz_Y",
            "https://www.youtube.com/watch?v=CVP1CwEBz_Y",
            "https://www.youtube.com/watch?v=QyzuzFISNJ0",
            "https://www.youtube.com/watch?v=QyzuzFISNJ0",
            "https://www.youtube.com/watch?v=TiSZ8h2EuuM",
            "https://www.youtube.com/watch?v=TiSZ8h2EuuM",
            "https://www.youtube.com/watch?v=0BGV-FsFoLY",
            "https://www.youtube.com/watch?v=0BGV-FsFoLY",
            "https://www.youtube.com/watch?v=jM_bU4xqs-Q",
            "https://www.youtube.com/watch?v=jM_bU4xqs-Q",
            "https://www.youtube.com/watch?v=YRaSAvw-RhQ",
            "https://www.youtube.com/watch?v=YRaSAvw-RhQ",
            "https://www.youtube.com/watch?v=xdzSDBl4Tck",
            "https://www.youtube.com/watch?v=xdzSDBl4Tck",
            "https://www.youtube.com/watch?v=xNgQOHwsIbg",
            "https://www.youtube.com/watch?v=xNgQOHwsIbg",
            "https://www.youtube.com/watch?v=H1TP6yBioOs",
            "https://www.youtube.com/watch?v=H1TP6yBioOs",
            "https://www.youtube.com/watch?v=_INfu5aAY3w",
            "https://www.youtube.com/watch?v=_INfu5aAY3w",
            "https://www.youtube.com/watch?v=3QXTtRqFWe8",
            "https://www.youtube.com/watch?v=3QXTtRqFWe8",
            "https://www.youtube.com/watch?v=as0uWN5ad64",
            "https://www.youtube.com/watch?v=as0uWN5ad64",
            "https://www.youtube.com/watch?v=Iw_66pl6Ulo",
            "https://www.youtube.com/watch?v=Iw_66pl6Ulo",
            "https://www.youtube.com/watch?v=-2clEkMi24E",
            "https://www.youtube.com/watch?v=-2clEkMi24E",
            "https://www.youtube.com/watch?v=c7LpCOrL6Rs",
            "https://www.youtube.com/watch?v=c7LpCOrL6Rs",
            "https://www.youtube.com/watch?v=4pgG5OPGxbE",
            "https://www.youtube.com/watch?v=4pgG5OPGxbE",
            "https://www.youtube.com/watch?v=dse1afiGbx4",
            "https://www.youtube.com/watch?v=dse1afiGbx4",
            "https://www.youtube.com/watch?v=kZZj54qbXBE",
            "https://www.youtube.com/watch?v=kZZj54qbXBE",
            "https://www.youtube.com/watch?v=IzhO0o-vHPE",
            "https://www.youtube.com/watch?v=IzhO0o-vHPE",
            "https://www.youtube.com/watch?v=PQbJw3eN8qg",
            "https://www.youtube.com/watch?v=PQbJw3eN8qg",
            "https://www.youtube.com/watch?v=5z3l_s5SEUo",
            "https://www.youtube.com/watch?v=5z3l_s5SEUo",
            "https://www.youtube.com/watch?v=mNLt6RcBPLM",
            "https://www.youtube.com/watch?v=mNLt6RcBPLM",
            "https://www.youtube.com/watch?v=kBE6HOt9rNE",
            "https://www.youtube.com/watch?v=kBE6HOt9rNE",
            "https://www.youtube.com/watch?v=65z1yxnLAfI",
            "https://www.youtube.com/watch?v=65z1yxnLAfI",
            "https://www.youtube.com/watch?v=WMH0uyEMC0k",
            "https://www.youtube.com/watch?v=WMH0uyEMC0k",
            "https://www.youtube.com/watch?v=wQyB_JtkK6Q",
            "https://www.youtube.com/watch?v=wQyB_JtkK6Q",
            "https://www.youtube.com/watch?v=yH3yLCwhKN8",
            "https://www.youtube.com/watch?v=yH3yLCwhKN8",
            "https://www.youtube.com/watch?v=hn0Uk4YQv0Q",
            "https://www.youtube.com/watch?v=hn0Uk4YQv0Q",
            "https://www.youtube.com/watch?v=RRGkBsILaTA",
            "https://www.youtube.com/watch?v=RRGkBsILaTA",
            "https://www.youtube.com/watch?v=UfxygJgEPuA",
            "https://www.youtube.com/watch?v=UfxygJgEPuA",
            "https://www.youtube.com/watch?v=tvYmocBWIos",
            "https://www.youtube.com/watch?v=tvYmocBWIos",
            "https://www.youtube.com/watch?v=LqJBXtG9xxk",
            "https://www.youtube.com/watch?v=LqJBXtG9xxk",
            "https://www.youtube.com/watch?v=4IRobBxErZc",
            "https://www.youtube.com/watch?v=4IRobBxErZc",
            "https://www.youtube.com/watch?v=aoiIjNKnh7c",
            "https://www.youtube.com/watch?v=aoiIjNKnh7c",
            "https://www.youtube.com/watch?v=bsVJBDeo7yQ",
            "https://www.youtube.com/watch?v=bsVJBDeo7yQ",
            "https://www.youtube.com/watch?v=GQlKIZF0abM",
            "https://www.youtube.com/watch?v=GQlKIZF0abM",
            "https://www.youtube.com/watch?v=gmhB2qKcB1o",
            "https://www.youtube.com/watch?v=gmhB2qKcB1o",
            "https://www.youtube.com/watch?v=jPCVf6ac73k",
            "https://www.youtube.com/watch?v=jPCVf6ac73k",
            "https://www.youtube.com/watch?v=dEYv5Ckpkrw",
            "https://www.youtube.com/watch?v=dEYv5Ckpkrw",
            "https://www.youtube.com/watch?v=kNCaqP79Qpk",
            "https://www.youtube.com/watch?v=kNCaqP79Qpk",
            "https://www.youtube.com/watch?v=brhg2n5v7po",
            "https://www.youtube.com/watch?v=brhg2n5v7po",
            "https://www.youtube.com/watch?v=j5-M_JBxGLk",
            "https://www.youtube.com/watch?v=j5-M_JBxGLk",
            "https://www.youtube.com/watch?v=V99KJgOMne0",
            "https://www.youtube.com/watch?v=V99KJgOMne0",
            "https://www.youtube.com/watch?v=0wAAH_Ieq78",
            "https://www.youtube.com/watch?v=0wAAH_Ieq78",
            "https://www.youtube.com/watch?v=LjCTda4TvFg",
            "https://www.youtube.com/watch?v=LjCTda4TvFg",
            "https://www.youtube.com/watch?v=MQy-iXt4haQ",
            "https://www.youtube.com/watch?v=MQy-iXt4haQ",
            "https://www.youtube.com/watch?v=SwI9EmmwQFY",
            "https://www.youtube.com/watch?v=SwI9EmmwQFY",
            "https://www.youtube.com/watch?v=irBqtdVM-LU",
            "https://www.youtube.com/watch?v=irBqtdVM-LU",
            "https://www.youtube.com/watch?v=BB_WkU0Xhmw",
            "https://www.youtube.com/watch?v=BB_WkU0Xhmw",
            "https://www.youtube.com/watch?v=1qtYgAEzaGE",
            "https://www.youtube.com/watch?v=1qtYgAEzaGE",
            "https://www.youtube.com/watch?v=CuTo7oCq3J8",
            "https://www.youtube.com/watch?v=CuTo7oCq3J8",
            "https://www.youtube.com/watch?v=md2TBEVsK_8",
            "https://www.youtube.com/watch?v=md2TBEVsK_8",
            "https://www.youtube.com/watch?v=JJL59YqBG1Y",
            "https://www.youtube.com/watch?v=JJL59YqBG1Y",
            "https://www.youtube.com/watch?v=FncTDZxNbM4",
            "https://www.youtube.com/watch?v=FncTDZxNbM4",
            "https://www.youtube.com/watch?v=hdgqW2LW0eU",
            "https://www.youtube.com/watch?v=hdgqW2LW0eU",
            "https://www.youtube.com/watch?v=3dQf7dQ_y4M",
            "https://www.youtube.com/watch?v=3dQf7dQ_y4M",
            "https://www.youtube.com/watch?v=7o9KoRxPyA4",
            "https://www.youtube.com/watch?v=7o9KoRxPyA4",
            "https://www.youtube.com/watch?v=W9kwMKt3Udo",
            "https://www.youtube.com/watch?v=W9kwMKt3Udo",
            "https://www.youtube.com/watch?v=6rII5ex88QM",
            "https://www.youtube.com/watch?v=6rII5ex88QM",
            "https://www.youtube.com/watch?v=KNyhQTxQcAU",
            "https://www.youtube.com/watch?v=KNyhQTxQcAU",
            "https://www.youtube.com/watch?v=Dk20-E0yx_s",
            "https://www.youtube.com/watch?v=Dk20-E0yx_s",
            "https://www.youtube.com/watch?v=iLQQZ6yqoUI",
            "https://www.youtube.com/watch?v=iLQQZ6yqoUI",
            "https://www.youtube.com/watch?v=Cr4ri7g-ysk",
            "https://www.youtube.com/watch?v=Cr4ri7g-ysk",
            "https://www.youtube.com/watch?v=T4O4wuVR4TI",
            "https://www.youtube.com/watch?v=T4O4wuVR4TI",
            "https://www.youtube.com/watch?v=3w3DiFRPcgg",
            "https://www.youtube.com/watch?v=3w3DiFRPcgg",
            "https://www.youtube.com/watch?v=jMXY7n7beuA",
            "https://www.youtube.com/watch?v=jMXY7n7beuA",
            "https://www.youtube.com/watch?v=KiSuAzXFiNI",
            "https://www.youtube.com/watch?v=KiSuAzXFiNI",
            "https://www.youtube.com/watch?v=4_UlzOeF3Qg",
            "https://www.youtube.com/watch?v=4_UlzOeF3Qg",
            "https://www.youtube.com/watch?v=YY5QWEuZ3BQ",
            "https://www.youtube.com/watch?v=YY5QWEuZ3BQ",
            "https://www.youtube.com/watch?v=_jvIzecBP3Y",
            "https://www.youtube.com/watch?v=_jvIzecBP3Y",
            "https://www.youtube.com/watch?v=jrabuVhrfTk",
            "https://www.youtube.com/watch?v=jrabuVhrfTk",
            "https://www.youtube.com/watch?v=8SkpkCgYJ2I",
            "https://www.youtube.com/watch?v=8SkpkCgYJ2I",
            "https://www.youtube.com/watch?v=fxu8o3SyUUU",
            "https://www.youtube.com/watch?v=fxu8o3SyUUU",
            "https://www.youtube.com/watch?v=0zmFaeGaMq8",
            "https://www.youtube.com/watch?v=0zmFaeGaMq8",
            "https://www.youtube.com/watch?v=s1iexbop6ms",
            "https://www.youtube.com/watch?v=s1iexbop6ms",
            "https://www.youtube.com/watch?v=EnUzZgGk1wg",
            "https://www.youtube.com/watch?v=EnUzZgGk1wg",
            "https://www.youtube.com/watch?v=Z6HMvpqV9Dg",
            "https://www.youtube.com/watch?v=Z6HMvpqV9Dg",
            "https://www.youtube.com/watch?v=w3B3sKs8dvM",
            "https://www.youtube.com/watch?v=w3B3sKs8dvM",
            "https://www.youtube.com/watch?v=aCVYpjw8Hpc",
            "https://www.youtube.com/watch?v=aCVYpjw8Hpc",
            "https://www.youtube.com/watch?v=BrRZZ_8NTkg",
            "https://www.youtube.com/watch?v=BrRZZ_8NTkg",
            "https://www.youtube.com/watch?v=OwbVC1_tO_I",
            "https://www.youtube.com/watch?v=OwbVC1_tO_I",
            "https://www.youtube.com/watch?v=Zkn-nsN_Iq0",
            "https://www.youtube.com/watch?v=Zkn-nsN_Iq0",
            "https://www.youtube.com/watch?v=OuU0lnRhylA",
            "https://www.youtube.com/watch?v=OuU0lnRhylA",
            "https://www.youtube.com/watch?v=4-2jjC2WK0c",
            "https://www.youtube.com/watch?v=4-2jjC2WK0c",
            "https://www.youtube.com/watch?v=Sdktvc1vKR8",
            "https://www.youtube.com/watch?v=Sdktvc1vKR8",
            "https://www.youtube.com/watch?v=Jvuy-s7EcSU",
            "https://www.youtube.com/watch?v=Jvuy-s7EcSU",
            "https://www.youtube.com/watch?v=Nrl0YLC-KtM",
            "https://www.youtube.com/watch?v=Nrl0YLC-KtM",
            "https://www.youtube.com/watch?v=i_47ehqPsLI",
            "https://www.youtube.com/watch?v=i_47ehqPsLI",
            "https://www.youtube.com/watch?v=VGIL50g6HyY",
            "https://www.youtube.com/watch?v=VGIL50g6HyY",
            "https://www.youtube.com/watch?v=hHu-rYTbLd0",
            "https://www.youtube.com/watch?v=hHu-rYTbLd0",
            "https://www.youtube.com/watch?v=6WDdzN1KwJk",
            "https://www.youtube.com/watch?v=6WDdzN1KwJk",
            "https://www.youtube.com/watch?v=s2QVHrIE_v8",
            "https://www.youtube.com/watch?v=s2QVHrIE_v8",
            "https://www.youtube.com/watch?v=3GaDbwO2lVM",
            "https://www.youtube.com/watch?v=3GaDbwO2lVM",
            "https://www.youtube.com/watch?v=WpjHLimZmmQ",
            "https://www.youtube.com/watch?v=WpjHLimZmmQ",
            "https://www.youtube.com/watch?v=tlwkzuq9Co8",
            "https://www.youtube.com/watch?v=tlwkzuq9Co8",
            "https://www.youtube.com/watch?v=Dzj0vA_as-8",
            "https://www.youtube.com/watch?v=Dzj0vA_as-8",
            "https://www.youtube.com/watch?v=xBN5LoCdcKM",
            "https://www.youtube.com/watch?v=xBN5LoCdcKM",
            "https://www.youtube.com/watch?v=MCgTDLtxJzQ",
            "https://www.youtube.com/watch?v=MCgTDLtxJzQ",
            "https://www.youtube.com/watch?v=h1rA2jMS-6I",
            "https://www.youtube.com/watch?v=h1rA2jMS-6I",
            "https://www.youtube.com/watch?v=xxHZtHila0o",
            "https://www.youtube.com/watch?v=xxHZtHila0o",
            "https://www.youtube.com/watch?v=jLslhwIMMy8",
            "https://www.youtube.com/watch?v=jLslhwIMMy8",
            "https://www.youtube.com/watch?v=cBOTCmafKUs",
            "https://www.youtube.com/watch?v=cBOTCmafKUs",
            "https://www.youtube.com/watch?v=5XtTx94RPIw",
            "https://www.youtube.com/watch?v=5XtTx94RPIw",
            "https://www.youtube.com/watch?v=yVtyPj4JEFo",
            "https://www.youtube.com/watch?v=yVtyPj4JEFo",
            "https://www.youtube.com/watch?v=hJLxJhMstrg",
            "https://www.youtube.com/watch?v=hJLxJhMstrg",
            "https://www.youtube.com/watch?v=2lGvj3aapiQ",
            "https://www.youtube.com/watch?v=2lGvj3aapiQ",
            "https://www.youtube.com/watch?v=EaWdJq9OO_s",
            "https://www.youtube.com/watch?v=EaWdJq9OO_s",
            "https://www.youtube.com/watch?v=GV1BKcgBr18",
            "https://www.youtube.com/watch?v=GV1BKcgBr18",
            "https://www.youtube.com/watch?v=1WqwHJCUs18",
            "https://www.youtube.com/watch?v=1WqwHJCUs18",
            "https://www.youtube.com/watch?v=tziqpdbpriY",
            "https://www.youtube.com/watch?v=tziqpdbpriY",
            "https://www.youtube.com/watch?v=gp_4-JhkX9I",
            "https://www.youtube.com/watch?v=gp_4-JhkX9I",
            "https://www.youtube.com/watch?v=bAykS2oh8Dg",
            "https://www.youtube.com/watch?v=bAykS2oh8Dg",
            "https://www.youtube.com/watch?v=TfyJ4zD0gdc",
            "https://www.youtube.com/watch?v=TfyJ4zD0gdc",
            "https://www.youtube.com/watch?v=2HlGH1MWX6Q",
            "https://www.youtube.com/watch?v=2HlGH1MWX6Q",
            "https://www.youtube.com/watch?v=Qsa2BbtOq3o",
            "https://www.youtube.com/watch?v=Qsa2BbtOq3o",
            "https://www.youtube.com/watch?v=eQbNdZR2Eqg",
            "https://www.youtube.com/watch?v=eQbNdZR2Eqg",
            "https://www.youtube.com/watch?v=6xJza1aT3u8",
            "https://www.youtube.com/watch?v=6xJza1aT3u8",
            "https://www.youtube.com/watch?v=G45L3hskSpg",
            "https://www.youtube.com/watch?v=G45L3hskSpg",
            "https://www.youtube.com/watch?v=FOSN8IbSNS8",
            "https://www.youtube.com/watch?v=FOSN8IbSNS8",
            "https://www.youtube.com/watch?v=XkM0b0rtYQc",
            "https://www.youtube.com/watch?v=XkM0b0rtYQc",
            "https://www.youtube.com/watch?v=D53m7CJ7wYI",
            "https://www.youtube.com/watch?v=D53m7CJ7wYI",
            "https://www.youtube.com/watch?v=cEEhBmEJMEc",
            "https://www.youtube.com/watch?v=cEEhBmEJMEc",
            "https://www.youtube.com/watch?v=AAWGqynU2sc",
            "https://www.youtube.com/watch?v=AAWGqynU2sc",
            "https://www.youtube.com/watch?v=bmQWrZbpwYo",
            "https://www.youtube.com/watch?v=bmQWrZbpwYo",
            "https://www.youtube.com/watch?v=l4bRfNalqm0",
            "https://www.youtube.com/watch?v=l4bRfNalqm0",
            "https://www.youtube.com/watch?v=3An3vYvie24",
            "https://www.youtube.com/watch?v=3An3vYvie24",
            "https://www.youtube.com/watch?v=wVq3rVFnWaY",
            "https://www.youtube.com/watch?v=wVq3rVFnWaY",
            "https://www.youtube.com/watch?v=lk3BbhzsBgs",
            "https://www.youtube.com/watch?v=lk3BbhzsBgs",
            "https://www.youtube.com/watch?v=J2st9L2IDIc",
            "https://www.youtube.com/watch?v=J2st9L2IDIc",
            "https://www.youtube.com/watch?v=XZOfPgq7QxY",
            "https://www.youtube.com/watch?v=XZOfPgq7QxY",
            "https://www.youtube.com/watch?v=x2qFO9-7CY4",
            "https://www.youtube.com/watch?v=x2qFO9-7CY4",
            "https://www.youtube.com/watch?v=dtdGrIDHf2g",
            "https://www.youtube.com/watch?v=dtdGrIDHf2g",
            "https://www.youtube.com/watch?v=W2XlqUIBU3A",
            "https://www.youtube.com/watch?v=W2XlqUIBU3A",
            "https://www.youtube.com/watch?v=oDoegcuUBfk",
            "https://www.youtube.com/watch?v=oDoegcuUBfk",
            "https://www.youtube.com/watch?v=SlqFt8SDxl4",
            "https://www.youtube.com/watch?v=SlqFt8SDxl4",
            "https://www.youtube.com/watch?v=qt-iccuOSLI",
            "https://www.youtube.com/watch?v=qt-iccuOSLI",
            "https://www.youtube.com/watch?v=x6y1SOOCiDs",
            "https://www.youtube.com/watch?v=x6y1SOOCiDs",
            "https://www.youtube.com/watch?v=lkDBImBAmN0",
            "https://www.youtube.com/watch?v=lkDBImBAmN0",
            "https://www.youtube.com/watch?v=aqiqA45xSIw",
            "https://www.youtube.com/watch?v=aqiqA45xSIw",
            "https://www.youtube.com/watch?v=L3xveE1O3FA",
            "https://www.youtube.com/watch?v=L3xveE1O3FA",
            "https://www.youtube.com/watch?v=a41dPdT4Dys",
            "https://www.youtube.com/watch?v=a41dPdT4Dys",
            "https://www.youtube.com/watch?v=OI9f7oi0wxc",
            "https://www.youtube.com/watch?v=OI9f7oi0wxc",
            "https://www.youtube.com/watch?v=IZIyE6iShe8",
            "https://www.youtube.com/watch?v=IZIyE6iShe8",
            "https://www.youtube.com/watch?v=Wcf5b3mENJU",
            "https://www.youtube.com/watch?v=Wcf5b3mENJU",
            "https://www.youtube.com/watch?v=ZaD0cnafciU",
            "https://www.youtube.com/watch?v=ZaD0cnafciU",
            "https://www.youtube.com/watch?v=tiMY7-h3xnE",
            "https://www.youtube.com/watch?v=tiMY7-h3xnE",
            "https://www.youtube.com/watch?v=-dVbkxekzKA",
            "https://www.youtube.com/watch?v=-dVbkxekzKA",
            "https://www.youtube.com/watch?v=BIkDOJu4508",
            "https://www.youtube.com/watch?v=BIkDOJu4508",
            "https://www.youtube.com/watch?v=BElkCNmtEvI",
            "https://www.youtube.com/watch?v=BElkCNmtEvI",
            "https://www.youtube.com/watch?v=tElbDd2UzRU",
            "https://www.youtube.com/watch?v=tElbDd2UzRU",
            "https://www.youtube.com/watch?v=60ODKPe2yJ4",
            "https://www.youtube.com/watch?v=60ODKPe2yJ4",
            "https://www.youtube.com/watch?v=lQAG4JcI0x8",
            "https://www.youtube.com/watch?v=lQAG4JcI0x8",
            "https://www.youtube.com/watch?v=dPszGLynA00",
            "https://www.youtube.com/watch?v=dPszGLynA00",
            "https://www.youtube.com/watch?v=OZQH5UbEjqE",
            "https://www.youtube.com/watch?v=OZQH5UbEjqE",
            "https://www.youtube.com/watch?v=zC3vWeQRX0s",
            "https://www.youtube.com/watch?v=zC3vWeQRX0s",
            "https://www.youtube.com/watch?v=UdOcA55zmVk",
            "https://www.youtube.com/watch?v=UdOcA55zmVk",
            "https://www.youtube.com/watch?v=IwFiz6aT1mU",
            "https://www.youtube.com/watch?v=IwFiz6aT1mU",
            "https://www.youtube.com/watch?v=gMFc7agO09w",
            "https://www.youtube.com/watch?v=gMFc7agO09w",
            "https://www.youtube.com/watch?v=FQhzc8oXKzU",
            "https://www.youtube.com/watch?v=FQhzc8oXKzU",
            "https://www.youtube.com/watch?v=omhW0Y3W1kQ",
            "https://www.youtube.com/watch?v=omhW0Y3W1kQ",
            "https://www.youtube.com/watch?v=3wAHb4aNEtg",
            "https://www.youtube.com/watch?v=3wAHb4aNEtg",
            "https://www.youtube.com/watch?v=nFhGZdo5mrI",
            "https://www.youtube.com/watch?v=nFhGZdo5mrI",
            "https://www.youtube.com/watch?v=HSYiQWpmg-s",
            "https://www.youtube.com/watch?v=HSYiQWpmg-s",
            "https://www.youtube.com/watch?v=dy02o7MW3VA",
            "https://www.youtube.com/watch?v=dy02o7MW3VA",
            "https://www.youtube.com/watch?v=pvY0-V3v9YU",
            "https://www.youtube.com/watch?v=pvY0-V3v9YU",
            "https://www.youtube.com/watch?v=LEAHiGZioic",
            "https://www.youtube.com/watch?v=LEAHiGZioic",
            "https://www.youtube.com/watch?v=4KSHfVWbCsg",
            "https://www.youtube.com/watch?v=4KSHfVWbCsg",
            "https://www.youtube.com/watch?v=nUNr7RkFSGQ",
            "https://www.youtube.com/watch?v=nUNr7RkFSGQ",
            "https://www.youtube.com/watch?v=Z7Bfm6QQeEw",
            "https://www.youtube.com/watch?v=Z7Bfm6QQeEw",
            "https://www.youtube.com/watch?v=6Zl1RfZV7qo",
            "https://www.youtube.com/watch?v=6Zl1RfZV7qo",
            "https://www.youtube.com/watch?v=flgt1n5nfxQ",
            "https://www.youtube.com/watch?v=flgt1n5nfxQ",
            "https://www.youtube.com/watch?v=W_9FUb8ubOA",
            "https://www.youtube.com/watch?v=W_9FUb8ubOA",
            "https://www.youtube.com/watch?v=Hv2FMHIgxic",
            "https://www.youtube.com/watch?v=Hv2FMHIgxic",
            "https://www.youtube.com/watch?v=IibRcxNaOC8",
            "https://www.youtube.com/watch?v=IibRcxNaOC8",
            "https://www.youtube.com/watch?v=i-mx5A2uL5w",
            "https://www.youtube.com/watch?v=i-mx5A2uL5w",
            "https://www.youtube.com/watch?v=Mop-dVqRYRc",
            "https://www.youtube.com/watch?v=Mop-dVqRYRc",
            "https://www.youtube.com/watch?v=dEIgI-t_xDc",
            "https://www.youtube.com/watch?v=dEIgI-t_xDc",
            "https://www.youtube.com/watch?v=nLb1T8C8rq4",
            "https://www.youtube.com/watch?v=nLb1T8C8rq4",
            "https://www.youtube.com/watch?v=kBhajEk1ph8",
            "https://www.youtube.com/watch?v=kBhajEk1ph8",
            "https://www.youtube.com/watch?v=otC_QHQtQBk",
            "https://www.youtube.com/watch?v=otC_QHQtQBk",
            "https://www.youtube.com/watch?v=56Qu3MY5EnU",
            "https://www.youtube.com/watch?v=56Qu3MY5EnU",
            "https://www.youtube.com/watch?v=5jlGU_ntmq8",
            "https://www.youtube.com/watch?v=5jlGU_ntmq8",
            "https://www.youtube.com/watch?v=_bnETxx5Qrk",
            "https://www.youtube.com/watch?v=_bnETxx5Qrk",
            "https://www.youtube.com/watch?v=5uFLLtjGbL8",
            "https://www.youtube.com/watch?v=5uFLLtjGbL8",
            "https://www.youtube.com/watch?v=DM2-No-Nv-A",
            "https://www.youtube.com/watch?v=DM2-No-Nv-A",
            "https://www.youtube.com/watch?v=b2fmawcPGUI",
            "https://www.youtube.com/watch?v=b2fmawcPGUI",
            "https://www.youtube.com/watch?v=LEOPzikTioQ",
            "https://www.youtube.com/watch?v=LEOPzikTioQ",
            "https://www.youtube.com/watch?v=AQeSyjjHvR4",
            "https://www.youtube.com/watch?v=AQeSyjjHvR4",
            "https://www.youtube.com/watch?v=0H4z6ze4SWA",
            "https://www.youtube.com/watch?v=0H4z6ze4SWA",
            "https://www.youtube.com/watch?v=00vZraHIcXI",
            "https://www.youtube.com/watch?v=00vZraHIcXI",
            "https://www.youtube.com/watch?v=JiAalrujjyA",
            "https://www.youtube.com/watch?v=JiAalrujjyA",
            "https://www.youtube.com/watch?v=B8aYWyH1bUY",
            "https://www.youtube.com/watch?v=B8aYWyH1bUY",
            "https://www.youtube.com/watch?v=l-KihisijD4",
            "https://www.youtube.com/watch?v=l-KihisijD4",
            "https://www.youtube.com/watch?v=lf5j95FXkWk",
            "https://www.youtube.com/watch?v=lf5j95FXkWk",
            "https://www.youtube.com/watch?v=AWIt-24PJks",
            "https://www.youtube.com/watch?v=AWIt-24PJks",
            "https://www.youtube.com/watch?v=8AT4hA7SOuk",
            "https://www.youtube.com/watch?v=8AT4hA7SOuk",
            "https://www.youtube.com/watch?v=FarsyCWkLcU",
            "https://www.youtube.com/watch?v=FarsyCWkLcU",
            "https://www.youtube.com/watch?v=kZ-GPyZwdvI",
            "https://www.youtube.com/watch?v=kZ-GPyZwdvI",
            "https://www.youtube.com/watch?v=sfFmVAOftG4",
            "https://www.youtube.com/watch?v=sfFmVAOftG4",
            "https://www.youtube.com/watch?v=midVPRrvED0",
            "https://www.youtube.com/watch?v=midVPRrvED0",
            "https://www.youtube.com/watch?v=ou_zUSML1qA",
            "https://www.youtube.com/watch?v=ou_zUSML1qA",
            "https://www.youtube.com/watch?v=_4EnAI6dExE",
            "https://www.youtube.com/watch?v=_4EnAI6dExE",
            "https://www.youtube.com/watch?v=YPNYxqSbDMA",
            "https://www.youtube.com/watch?v=YPNYxqSbDMA",
            "https://www.youtube.com/watch?v=nA6EemoFjP4",
            "https://www.youtube.com/watch?v=nA6EemoFjP4",
            "https://www.youtube.com/watch?v=MvsVDbYjWas",
            "https://www.youtube.com/watch?v=MvsVDbYjWas",
            "https://www.youtube.com/watch?v=5oMDd1lB5Tk",
            "https://www.youtube.com/watch?v=5oMDd1lB5Tk",
            "https://www.youtube.com/watch?v=u1hj8YHC41Y",
            "https://www.youtube.com/watch?v=u1hj8YHC41Y",
            "https://www.youtube.com/watch?v=N3gyM7uAXsw",
            "https://www.youtube.com/watch?v=N3gyM7uAXsw",
            "https://www.youtube.com/watch?v=f5rmduJ-G5U",
            "https://www.youtube.com/watch?v=f5rmduJ-G5U",
            "https://www.youtube.com/watch?v=QIy0uSdF8Us",
            "https://www.youtube.com/watch?v=QIy0uSdF8Us",
            "https://www.youtube.com/watch?v=T_XTH2ejGO0",
            "https://www.youtube.com/watch?v=T_XTH2ejGO0",
            "https://www.youtube.com/watch?v=OmfvSr6OXlA",
            "https://www.youtube.com/watch?v=OmfvSr6OXlA",
            "https://www.youtube.com/watch?v=IP5-7B__4G4",
            "https://www.youtube.com/watch?v=IP5-7B__4G4",
            "https://www.youtube.com/watch?v=qk3kU9J2qs4",
            "https://www.youtube.com/watch?v=qk3kU9J2qs4",
            "https://www.youtube.com/watch?v=znu7Mr-k1iU",
            "https://www.youtube.com/watch?v=znu7Mr-k1iU",
            "https://www.youtube.com/watch?v=gwQ4bTDzFAI",
            "https://www.youtube.com/watch?v=gwQ4bTDzFAI",
            "https://www.youtube.com/watch?v=r835EbYPaNE",
            "https://www.youtube.com/watch?v=r835EbYPaNE",
            "https://www.youtube.com/watch?v=-wbKPKeqn0o",
            "https://www.youtube.com/watch?v=-wbKPKeqn0o",
            "https://www.youtube.com/watch?v=ZDzGKkayFng",
            "https://www.youtube.com/watch?v=ZDzGKkayFng",
            "https://www.youtube.com/watch?v=3KRK_pm2bBI",
            "https://www.youtube.com/watch?v=3KRK_pm2bBI",
            "https://www.youtube.com/watch?v=4J2UgEdsCe8",
            "https://www.youtube.com/watch?v=4J2UgEdsCe8",
            "https://www.youtube.com/watch?v=BCntSgWdsSU",
            "https://www.youtube.com/watch?v=BCntSgWdsSU",
            "https://www.youtube.com/watch?v=VQn92DZCeyw",
            "https://www.youtube.com/watch?v=VQn92DZCeyw",
            "https://www.youtube.com/watch?v=Ig7lnVS6wEI",
            "https://www.youtube.com/watch?v=Ig7lnVS6wEI",
            "https://www.youtube.com/watch?v=GLeby9KMoJ4",
            "https://www.youtube.com/watch?v=GLeby9KMoJ4",
            "https://www.youtube.com/watch?v=h_rN9ZdT20M",
            "https://www.youtube.com/watch?v=h_rN9ZdT20M",
            "https://www.youtube.com/watch?v=y2hvlVtAXiM",
            "https://www.youtube.com/watch?v=y2hvlVtAXiM",
            "https://www.youtube.com/watch?v=m8i8gNAdXi4",
            "https://www.youtube.com/watch?v=m8i8gNAdXi4",
            "https://www.youtube.com/watch?v=hFWGWl10vuQ",
            "https://www.youtube.com/watch?v=hFWGWl10vuQ",
            "https://www.youtube.com/watch?v=lkDBImBAmN0",
            "https://www.youtube.com/watch?v=lkDBImBAmN0",
            "https://www.youtube.com/watch?v=omhW0Y3W1kQ",
            "https://www.youtube.com/watch?v=omhW0Y3W1kQ",
            "https://www.youtube.com/watch?v=QoqohmccTSc",
            "https://www.youtube.com/watch?v=QoqohmccTSc",
            "https://www.youtube.com/watch?v=wnHW6o8WMas",
            "https://www.youtube.com/watch?v=wnHW6o8WMas",
            "https://www.youtube.com/watch?v=az6NibAUf7Y",
            "https://www.youtube.com/watch?v=az6NibAUf7Y",
            "https://www.youtube.com/watch?v=BrRZZ_8NTkg",
            "https://www.youtube.com/watch?v=BrRZZ_8NTkg",
            "https://www.youtube.com/watch?v=TInEEu3dwCc",
            "https://www.youtube.com/watch?v=TInEEu3dwCc",
            "https://www.youtube.com/watch?v=lYGGpc2mMno",
            "https://www.youtube.com/watch?v=lYGGpc2mMno",
            "https://www.youtube.com/watch?v=VSceuiPBpxY",
            "https://www.youtube.com/watch?v=VSceuiPBpxY",
            "https://www.youtube.com/watch?v=Km8h1zyZDcQ",
            "https://www.youtube.com/watch?v=Km8h1zyZDcQ",
            "https://www.youtube.com/watch?v=gMFc7agO09w",
            "https://www.youtube.com/watch?v=gMFc7agO09w",
            "https://www.youtube.com/watch?v=AuWoEYPBfrw",
            "https://www.youtube.com/watch?v=AuWoEYPBfrw",
            "https://www.youtube.com/watch?v=Dk20-E0yx_s",
            "https://www.youtube.com/watch?v=Dk20-E0yx_s",
            "https://www.youtube.com/watch?v=tbnzAVRZ9Xc",
            "https://www.youtube.com/watch?v=tbnzAVRZ9Xc",
            "https://www.youtube.com/watch?v=Dd6iSXSW_N8",
            "https://www.youtube.com/watch?v=Dd6iSXSW_N8",
            "https://www.youtube.com/watch?v=Z3aZABXrAeI",
            "https://www.youtube.com/watch?v=Z3aZABXrAeI",
            "https://www.youtube.com/watch?v=zNZzyVD0LzY",
            "https://www.youtube.com/watch?v=zNZzyVD0LzY",
            "https://www.youtube.com/watch?v=hn0Uk4YQv0Q",
            "https://www.youtube.com/watch?v=hn0Uk4YQv0Q",
            "https://www.youtube.com/watch?v=h1rA2jMS-6I",
            "https://www.youtube.com/watch?v=0qCnTs06Fvw",
            "https://www.youtube.com/watch?v=h1rA2jMS-6I",
            "https://www.youtube.com/watch?v=OuU0lnRhylA",
            "https://www.youtube.com/watch?v=403FGqa-Uv8",
            "https://www.youtube.com/watch?v=irBqtdVM-LU",
            "https://www.youtube.com/watch?v=wQyB_JtkK6Q",
            "https://www.youtube.com/watch?v=syEDu6AihFs",
            "https://www.youtube.com/watch?v=k62uAgk9_Mo",
            "https://www.youtube.com/watch?v=9753gP09EvA",
            "https://www.youtube.com/watch?v=_CqXt3irYhU",
            "https://www.youtube.com/watch?v=Z3aZABXrAeI",
            "https://www.youtube.com/watch?v=YbBcugBQ8l4",
            "https://www.youtube.com/watch?v=0qCnTs06Fvw",
            "https://www.youtube.com/watch?v=AuX9Yucn9qQ",
            "https://www.youtube.com/watch?v=S3lwAFED8Pg",
            "https://www.youtube.com/watch?v=ck6uvB1xlss",
            "https://www.youtube.com/watch?v=-vlskP9VcMA",
            "https://www.youtube.com/watch?v=V8HAdmsAjLw",
            "https://www.youtube.com/watch?v=k62uAgk9_Mo",
            "https://www.youtube.com/watch?v=ciag2xlZZAU",
            "https://www.youtube.com/watch?v=t_p0IOiIOE8",
            "https://www.youtube.com/watch?v=1Ms-4IaYywk",
            "https://www.youtube.com/watch?v=eay0stZYE04",
            "https://www.youtube.com/watch?v=Dd6iSXSW_N8",
            "https://www.youtube.com/watch?v=gMFc7agO09w",
            "https://www.youtube.com/watch?v=FncTDZxNbM4",
            "https://www.youtube.com/watch?v=lkDBImBAmN0",
            "https://www.youtube.com/watch?v=h1rA2jMS-6I",
            "https://www.youtube.com/watch?v=irBqtdVM-LU",
            "https://www.youtube.com/watch?v=hn0Uk4YQv0Q",
            "https://www.youtube.com/watch?v=tiMY7-h3xnE",
            "https://www.youtube.com/watch?v=Z3aZABXrAeI",
            "https://www.youtube.com/watch?v=Dk20-E0yx_s",
            "https://www.youtube.com/watch?v=9753gP09EvA",
            "https://www.youtube.com/watch?v=Wcf5b3mENJU",
            "https://www.youtube.com/watch?v=CVP1CwEBz_Y",
            "https://www.youtube.com/watch?v=hpPANEuEbws",
            "https://www.youtube.com/watch?v=403FGqa-Uv8",
            "https://www.youtube.com/watch?v=syEDu6AihFs",
            "https://www.youtube.com/watch?v=uXXPbti2nVM",
            "https://www.youtube.com/watch?v=UKNIeUuGCkM",
            "https://www.youtube.com/watch?v=oPYiFeVyVE0",
            "https://www.youtube.com/watch?v=YGKyFhd4DbU",
            "https://www.youtube.com/watch?v=_VoLRNABvhc",
            "https://www.youtube.com/watch?v=k_WcjMnuqSg",
            "https://www.youtube.com/watch?v=5TvwDgYKirU",
            "https://www.youtube.com/watch?v=alYkT0pvGd4",
            "https://www.youtube.com/watch?v=jat-a3aG3tc",
            "https://www.youtube.com/watch?v=WhSmUIZl0T0",
            "https://www.youtube.com/watch?v=pZnfm2eYoxU",
            "https://www.youtube.com/watch?v=x76iJz_9JO0",
            "https://www.youtube.com/watch?v=jM_bU4xqs-Q",
            "https://www.youtube.com/watch?v=i2ydtYdVNqM",
            "https://www.youtube.com/watch?v=YPBMaBNdHYs",
            "https://www.youtube.com/watch?v=lWxsby4Dx1A",
            "https://www.youtube.com/watch?v=0BGV-FsFoLY",
            "https://www.youtube.com/watch?v=H1TP6yBioOs",
            "https://www.youtube.com/watch?v=_INfu5aAY3w",
            "https://www.youtube.com/watch?v=3QXTtRqFWe8",
            "https://www.youtube.com/watch?v=Iw_66pl6Ulo",
            "https://www.youtube.com/watch?v=hn0Uk4YQv0Q",
            "https://www.youtube.com/watch?v=V7R1auV3UP4",
            "https://www.youtube.com/watch?v=xBN5LoCdcKM",
            "https://www.youtube.com/watch?v=DM2-No-Nv-A",
            "https://www.youtube.com/watch?v=0wAAH_Ieq78",
            "https://www.youtube.com/watch?v=aoiIjNKnh7c",
            "https://www.youtube.com/watch?v=dy02o7MW3VA",
            "https://www.youtube.com/watch?v=HloRqgygPW0",
            "https://www.youtube.com/watch?v=acbS5-1LLrw",
            "https://www.youtube.com/watch?v=znu7Mr-k1iU",
            "https://www.youtube.com/watch?v=VQn92DZCeyw",
            "https://www.youtube.com/watch?v=gMFc7agO09w",
            "https://www.youtube.com/watch?v=YY5QWEuZ3BQ",
            "https://www.youtube.com/watch?v=8WGAs8ktni4",
            "https://www.youtube.com/watch?v=6uS_GvteBcY",
            "https://www.youtube.com/watch?v=w3B3sKs8dvM",
            "https://www.youtube.com/watch?v=MvsVDbYjWas",
            "https://www.youtube.com/watch?v=ySx4xb26njg",
            "https://www.youtube.com/watch?v=tiMY7-h3xnE",
            "https://www.youtube.com/watch?v=9yB1oHjbL9w",
            "https://www.youtube.com/watch?v=zf91Hy-97XA",
            "https://www.youtube.com/watch?v=YTxNwPEJnEw",
            "https://www.youtube.com/watch?v=07ZZFaPdOe0",
            "https://www.youtube.com/watch?v=CJVtCPtJznY",
            "https://www.youtube.com/watch?v=QbKpbxRKZek",
            "https://www.youtube.com/watch?v=tlY0PkWxCW8",
            "https://www.youtube.com/watch?v=L3V7LKYPIUQ",
            "https://www.youtube.com/watch?v=L8UT0iVne24",
            "https://www.youtube.com/watch?v=6uS_GvteBcY",
            "https://www.youtube.com/watch?v=Bg_Q7KYWG1g",
            "https://www.youtube.com/watch?v=3sK3wJAxGfs",
            "https://www.youtube.com/watch?v=sIshbVIrdaM",
            "https://www.youtube.com/watch?v=ySx4xb26njg",
            "https://www.youtube.com/watch?v=yxUYMMhqBbs",
            "https://www.youtube.com/watch?v=_yBwzuVI6sA",
            "https://www.youtube.com/watch?v=p-aSBSEj6GE",
            "https://www.youtube.com/watch?v=L2CjW0uPB94"};

}
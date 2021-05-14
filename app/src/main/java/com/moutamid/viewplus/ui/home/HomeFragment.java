package com.moutamid.viewplus.ui.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplus.AddTaskActivity;
import com.moutamid.viewplus.R;
import com.moutamid.viewplus.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private View root;

    private ArrayList<Task> tasksArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.child("tasks").orderByChild("posterUid")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            new Utils().storeBoolean(getActivity(), "canAddNewTask", true);

                            progressDialog.dismiss();
                            return;
                        }

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            HomeFragment.Task task = dataSnapshot.getValue(HomeFragment.Task.class);

                            tasksArrayList.add(task);

                        }

                        initRecyclerView();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        new Utils().storeBoolean(getActivity(), "canAddNewTask", true);

                    }
                });


        setAddTaskButton();

        return root;
    }

    private void setAddTaskButton() {
        root.findViewById(R.id.addTaskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = root.findViewById(R.id.youtube_video_url_edittext);

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setError("Please enter a url!");
                    return;
                }

                if (TextUtils.isEmpty(getVideoId(editText.getText().toString()))) {
                    editText.setError("Wrong url!");
                } else {

                    Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                    intent.putExtra("url", editText.getText().toString().trim());

                    editText.setText("");

                    startActivity(intent);
                }
            }
        });
    }

    private static String getVideoId(@NonNull String videoUrl) {
        String videoId = "";
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }

    private void initRecyclerView() {

        conversationRecyclerView = root.findViewById(R.id.home_recyclerview);
        adapter = new RecyclerViewAdapterMessages();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 3) {

            new Utils().storeBoolean(getActivity(), "canAddNewTask", false);


            //        noChatsLayout.setVisibility(View.GONE);
            //        chatsRecyclerView.setVisibility(View.VISIBLE);

        } else {
            new Utils().storeBoolean(getActivity(), "canAddNewTask", true);

        }

        progressDialog.dismiss();

    }


    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_home, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {
            HomeFragment.Task task = tasksArrayList.get(position);

            holder.totalViews.setText(
                    "" + task.getCurrentViewsQuantity()
                            + "/" +
                            task.getTotalViewsQuantity() + " View");

            holder.watchSecondsDefined.setText("Watch Seconds:" + task.getTotalViewTimeQuantity());

            if (!task.getCompletedDate().equals("error")) {
                holder.completedTime.setText("complete:" + task.getCompletedDate());
            }

            if (task.getTotalViewsQuantity().equals(String.valueOf(
                    task.getCurrentViewsQuantity()
            ))) {
                holder.doneTickImageview.setVisibility(View.VISIBLE);
            }

            Glide.with(getActivity())
                    .load(task.getThumbnailUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                    )
                    .into(holder.thumbnailImage);

            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new Utils().showDialog(getActivity(),
                            "Are you sure?",
                            "Do you really want to delete this task?",
                            "Yes",
                            "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    databaseReference.child("tasks")
                                            .child(task.getTaskKey())
                                            .removeValue();

                                    dialogInterface.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }, true);

                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            if (tasksArrayList == null)
                return 0;
            return tasksArrayList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {
            ImageView thumbnailImage, doneTickImageview;
            TextView completedTime, watchSecondsDefined, totalViews;
            RelativeLayout parentLayout;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                completedTime = v.findViewById(R.id.done_time_textview_youtube);
                thumbnailImage = v.findViewById(R.id.youtube_thumbnail_imageview);
                watchSecondsDefined = v.findViewById(R.id.watch_seconds_defined_textview_youtube);
                doneTickImageview = v.findViewById(R.id.done_tick_imageview_youtube);
                totalViews = v.findViewById(R.id.total_youtube_views_textview);
                parentLayout = v.findViewById(R.id.parent_layout_task_home);

            }
        }

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

}
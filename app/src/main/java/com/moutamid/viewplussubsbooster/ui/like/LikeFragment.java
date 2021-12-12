package com.moutamid.viewplussubsbooster.ui.like;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;
import static com.moutamid.viewplussubsbooster.R.color.lighterGrey;
import static com.moutamid.viewplussubsbooster.R.color.red;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.viewplussubsbooster.R;
import com.moutamid.viewplussubsbooster.databinding.FragmentLikeBinding;
import com.moutamid.viewplussubsbooster.models.LikeTaskModel;
import com.moutamid.viewplussubsbooster.utils.Constants;
import com.moutamid.viewplussubsbooster.utils.Helper;
import com.moutamid.viewplussubsbooster.utils.Utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LikeFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "LikeFragment";

    int remainingDailyLimitInt = Utils.getInt(Utils.getDate() + "Like", 0);

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String BUTTON_TEXT = "Call YouTube Data API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_FORCE_SSL};
    private static final String CLIENT_SECRETS = "client_secret.json";
    private static final Collection<String> SCOPE =
            Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl", "https://www.googleapis.com/auth/youtube");

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    Video video;

    GoogleAccountCredential mCredential;
    String currentVideoLink;
    ProgressDialog mProgress;
    String currentVideoId = "";
//    private TextView mOutputText;
//    private Button mCallApiButton;

    //-------------------------------------------------------------------------------------------------------

    private FragmentLikeBinding b;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<LikeTaskModel> likeTaskModelArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    int currentCounter = 0;

    public LikeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentLikeBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.child(Constants.LIKE_TASKS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    progressDialog.dismiss();
                    Utils.toast("No data exist!");
                    return;
                }

                likeTaskModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

//                    model.setSubscribed(false);

                    if (dataSnapshot.child(Constants.LIKERS_PATH).child(mAuth.getUid()).exists()) {
//                        model.setSubscribed(true);
                    } else {
                        LikeTaskModel model = dataSnapshot.getValue(LikeTaskModel.class);
                        likeTaskModelArrayList.add(model);
                    }


                }
                progressDialog.dismiss();
                setDataOnViews(0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b.seeNextBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCounter++;

                if (currentCounter >= likeTaskModelArrayList.size()) {
                    Utils.toast("End of tasks!");

                } else setDataOnViews(currentCounter);

            }
        });

        //--------------------------------------------------------------------------------------------
        b.likeBtnLikeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utils.toast("Coming soon!");

                // LIKE USER TO CHANNEL
                likeUserToVideo();

            }
        });


        mProgress = new ProgressDialog(requireContext());
        mProgress.setMessage("Calling YouTube Data API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(//TODO: requireContext() is added
                requireContext().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        b.autoPlaySwitchLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bi) {
                if (bi) {
                    b.autoPlaySwitchLike.setText(
                            "Auto Play ( Daily Limit: "
                                    + remainingDailyLimitInt + "/30)"
                    );
                    isAutoPlay = true;
                    likeUserToVideo();
                } else {
                    b.autoPlaySwitchLike.setText("Auto Play");
                    isAutoPlay = false;
                }
            }
        });

        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.mask_group, "", ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.mask_group, "", ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.mask_group, "", ScaleTypes.CENTER_INSIDE));

        b.imageSlider.setImageList(imageList);

        return b.getRoot();
    }

    private void likeUserToVideo() {

        if (remainingDailyLimitInt == 30) {
            Utils.toast("Your daily limit reached!");
            return;
        }

        if (likeTaskModelArrayList.size() == 0)
            return;

//        if (currentVideoLink.isEmpty()) {
//            currentVideoLink.setError("Required");
//            return;
//        } else {
        currentVideoId = Helper.getVideoId(currentVideoLink);
//        }

//        if (currentVideoId.isEmpty()) {
//            currentVideoLink.setError("Invalid Video Url");
//            return;
//        }

//        mCallApiButton.setEnabled(false);
//        mOutputText.setText("");
        getResultsFromApi();
//        mCallApiButton.setEnabled(true);


    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
//            mOutputText.setText("No network connection available.");
            b.outputTextViewLike.setText("No network connection available.");
            Utils.toast("No network connection");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                requireContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = requireContext().getSharedPreferences("dev.moutamid.viewplussubsbooster", Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
                    Utils.toast("This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                requireContext().getSharedPreferences("dev.moutamid.viewplussubsbooster", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

//    public YouTube getService() throws GeneralSecurityException, IOException {
//        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
////        Credential credential = authorize(httpTransport);
//        return new YouTube.Builder(httpTransport, JSON_FACTORY, mCredential)
//                .setApplicationName(getString(R.string.app_name))
//                .build();
//    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(requireContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(requireContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                requireActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /*private void subscribeToYoutubeChannel() throws GeneralSecurityException, IOException {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        YouTube youtubeService = new com.google.api.services.youtube.YouTube.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("YouTube Data API Android Quickstart")
                .build();

        Subscription subscription = new Subscription();

        // Add the snippet object property to the Subscription object.
        SubscriptionSnippet snippet = new SubscriptionSnippet();
        ResourceId resourceId = new ResourceId();
        resourceId.setChannelId(video.getSnippet().getChannelId());
        resourceId.setKind("youtube#channel");
        snippet.setResourceId(resourceId);
        subscription.setSnippet(snippet);

        // Define and execute the API request
        YouTube.Subscriptions.Insert request = youtubeService.subscriptions()
                .insert("snippet", subscription);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Subscription response = request.execute();
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            b.outputTextViewLike.setText(response.toString());
                            uploadAddedSubscribers();
//                            mOutputText.setText(mOutputText + response.toString());
//                            Toast.makeText(requireContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }*/

    private void uploadAddedLikers() {
//        mProgress.hide();
//        progressDialog.show();

        /*databaseReference.child("tasks")
                .child(taskArrayList.get(currentPosition).getTaskKey())*/
        //.child("currentViewsQuantity")
        databaseReference
                .child(Constants.LIKE_TASKS)
                .child(likeTaskModelArrayList.get(currentCounter).getTaskKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        LikeTaskModel taskk = snapshot.getValue(LikeTaskModel.class);

                        String currentViews = String.valueOf(taskk.getCurrentLikesQuantity());

                        if (currentViews.equals(taskk.getTotalLikesQuantity())) {

                            databaseReference
                                    .child(Constants.LIKE_TASKS)
                                    .child(likeTaskModelArrayList.get(currentCounter).getTaskKey())
                                    .child("completedDate")
                                    .setValue(Utils.getDate())
//                                    .setValue(new Utils().getDate())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            likeTaskModelArrayList.remove(currentCounter);

                                            uploadAddedCoins();
                                            // UPLOAD COINS AND THEN RESTART VIDEO PLAYER

                                        }
                                    });

                        } else {

                            databaseReference
                                    .child(Constants.LIKE_TASKS)
                                    .child(likeTaskModelArrayList.get(currentCounter).getTaskKey())
                                    .child("currentLikesQuantity")
                                    .setValue(taskk.getCurrentLikesQuantity() + 1)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    uploadAddedCoins();
                                                }
                                            }
                                    );

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgress.hide();
                    }
                });
    }

    private void uploadAddedCoins() {
        databaseReference.child("userinfo").child(mAuth.getCurrentUser().getUid())
                .child("coins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int value = snapshot.getValue(Integer.class);

                databaseReference.child("userinfo").child(mAuth.getCurrentUser().getUid())
                        .child("coins")
                        .setValue(value + 120)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                databaseReference
                                        .child(Constants.LIKE_TASKS)
                                        .child(likeTaskModelArrayList.get(currentCounter).getTaskKey())
                                        .child(Constants.LIKERS_PATH)
                                        .child(mAuth.getUid())
                                        .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mProgress.hide();
                                        Utils.toast("Liked!");


                                        // INCREMENT DAILY LIMIT
                                        remainingDailyLimitInt++;
                                        Utils.store(Utils.getDate() + "Like",
                                                remainingDailyLimitInt);
                                        b.autoPlaySwitchLike.setText(
                                                "Auto Play ( Daily Limit: "
                                                        + remainingDailyLimitInt + "/30)"
                                        );

                                        currentCounter++;

                                        if (currentCounter >= likeTaskModelArrayList.size()) {
                                            Utils.toast("End of tasks!");
                                            b.videoImageLike.setBackgroundResource(0);
                                            b.videoIdLike.setText("Empty");
                                        } else setDataOnViews(currentCounter);

                                    }
                                });

                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.hide();
            }
        });
    }

    /*private void uploadSubscribeDoneToDB() {



    }*/

    private void likeVideo() throws GeneralSecurityException, IOException {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        YouTube youtubeService = new com.google.api.services.youtube.YouTube.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("YouTube Data API Android Quickstart")
                .build();

        YouTube.Videos.Rate request = youtubeService.videos()
                .rate(currentVideoId, "like");


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    request.execute();
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            b.outputTextViewLike.setText(response.toString());
                            uploadAddedLikers();
//                            Toast.makeText(MainActivity.this, "Liked", Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /**
     * An asynchronous task that handles the YouTube Data API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.youtube.YouTube mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("YouTube Data API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call YouTube Data API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch information about the "GoogleDevelopers" YouTube channel.
         *
         * @return List of Strings containing information about the channel.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // Get a list of up to 10 files.
            List<String> channelInfo = new ArrayList<String>();

            YouTube.Videos.List request = mService.videos()
                    .list("snippet,contentDetails,statistics,status");
            VideoListResponse response = request.setId(currentVideoId).execute();

            Log.d("clima", response.getItems().get(0).getSnippet().getChannelId());


            video = response.getItems().get(0);

            channelInfo.add("This video's title is " + video.getSnippet().getTitle() + ". " +
                    "Channel title is '" + video.getSnippet().getChannelTitle() + ", " +
                    "and it has " + video.getStatistics().getLikeCount() + " likes."

            );

            return channelInfo;
        }

        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {

            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
                b.outputTextViewLike.setText("No results returned.");
                Utils.toast("No results returned.");
            } else {
                output.add(0, "Data retrieved using the YouTube Data API:");
//                mOutputText.setText(TextUtils.join("\n", output));
                String text = TextUtils.join("\n", output);
                b.outputTextViewLike.setText(text);
//                Utils.toast(text);
            }

            try {
                likeVideo();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);//TODO: MainActivity is removed
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                    b.outputTextViewLike.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                    Utils.toast("The following error occurred:\n"
                            + mLastError.getMessage());
                    Log.d("TAGSUB", "onCancelled: " + "The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
//                mOutputText.setText("Request cancelled.");
                b.outputTextViewLike.setText("Request cancelled.");
                Utils.toast("Request cancelled.");
            }
        }
    }

    boolean isAutoPlay = false;

    //-------------------------------------------------------------------------------------------------
    boolean isTimerRunning = false;

    int isError = 0;

    private void setDataOnViews(int counter) {

        if (likeTaskModelArrayList.size() == 0)
            return;

        // IF FIRST TIME
        if (counter == 0) {
            progressDialog.show();

            b.videoImageLike.setScaleType(ImageView.ScaleType.CENTER_CROP);
            with(requireContext())
                    .asBitmap()
                    .load(likeTaskModelArrayList.get(counter).getThumbnailUrl())
                    .apply(new RequestOptions()
                            .placeholder(lighterGrey)
                            .error(lighterGrey)
                    )
                    .addListener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            isError++;

                            /*currentCounter++;

                            b.videoImageLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            b.videoImageLike.setImageResource(R.drawable.ic_baseline_access_time_filled_24);

                            if (currentCounter >= likeTaskModelArrayList.size()) {
                                Utils.toast("End of tasks!");
                                b.videoImageLike.setBackgroundResource(0);
                                b.videoIdLike.setText("Empty");
                            } else setDataOnViews(currentCounter);*/


                            currentCounter++;

                            if (currentCounter >= likeTaskModelArrayList.size()) {
                                Utils.toast("End of tasks!");

                            } else setDataOnViews(currentCounter);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .diskCacheStrategy(DATA)
                    .into(b.videoImageLike);


            b.videoIdLike.setText(
                    "Video Id: " + Helper.getVideoId(likeTaskModelArrayList.get(counter).getVideoUrl())
            );

            currentVideoLink = likeTaskModelArrayList.get(counter).getVideoUrl();

            progressDialog.dismiss();

            if (isError > 0) {
                b.videoImageLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
                b.videoImageLike.setImageResource(R.drawable.ic_baseline_access_time_filled_24);
                return;
            }

            if (isAutoPlay)
                likeUserToVideo();
            return;
        }

        // IF SECOND OR THIRD TIME
        b.videoImageLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
        b.videoImageLike.setImageResource(R.drawable.ic_baseline_access_time_filled_24);
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                b.videoImageLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
                b.videoImageLike.setImageResource(R.drawable.ic_baseline_access_time_filled_24);
                b.videoImageLike.animate().rotation(b.videoImageLike.getRotation() + 20)
                        .setDuration(100).start();
                b.videoIdLike.setText("" + millisUntilFinished / 1000);
                b.autoPlaySwitchLike.setEnabled(false);
            }

            public void onFinish() {
                b.autoPlaySwitchLike.setEnabled(true);
                b.videoImageLike.setRotation(0);

                progressDialog.show();

                b.videoImageLike.setScaleType(ImageView.ScaleType.CENTER_CROP);
                with(requireContext())
                        .asBitmap()
                        .load(likeTaskModelArrayList.get(counter).getThumbnailUrl())
                        .apply(new RequestOptions()
                                .placeholder(lighterGrey)
                                .error(lighterGrey)
                        )
                        .addListener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                isError++;

                                /*b.videoImageLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                b.videoImageLike.setImageResource(R.drawable.ic_baseline_access_time_filled_24);

                                currentCounter++;

                                if (currentCounter >= likeTaskModelArrayList.size()) {
                                    Utils.toast("End of tasks!");
                                    b.videoImageLike.setBackgroundResource(0);
                                    b.videoIdLike.setText("Empty");
                                } else setDataOnViews(currentCounter);
*/

                                currentCounter++;

                                if (currentCounter >= likeTaskModelArrayList.size()) {
                                    Utils.toast("End of tasks!");

                                } else setDataOnViews(currentCounter);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .diskCacheStrategy(DATA)
                        .into(b.videoImageLike);


                b.videoIdLike.setText(
                        "Video Id: " + Helper.getVideoId(likeTaskModelArrayList.get(counter).getVideoUrl())
                );

                currentVideoLink = likeTaskModelArrayList.get(counter).getVideoUrl();

                progressDialog.dismiss();

                if (isError>0) {
                    b.videoImageLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    b.videoImageLike.setImageResource(R.drawable.ic_baseline_access_time_filled_24);
                    return;
                }

                if (isAutoPlay)
                    likeUserToVideo();
            }
        }.start();


/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        })*/
    }
}
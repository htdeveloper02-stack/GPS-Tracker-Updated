package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
import static gps.trackerid.location.utils.Global.mLog;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ads.module.ads.ERainAd;

import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.RequestAdapter;
import gps.trackerid.location.database.FirebaseRequestHelper;
import gps.trackerid.location.databinding.ActivityRequestBinding;
import gps.trackerid.location.models.users.CurrentUserUtil;
import gps.trackerid.location.models.users.DataUser;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class RequestActivity extends BaseActivity {
    ActivityRequestBinding binding;
    RequestActivity activity;
    RequestAdapter requestAdapter;
    FirebaseRequestHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = this;
        firebaseHelper = FirebaseRequestHelper.INSTANCE;
        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        requestAdapter = new RequestAdapter(RequestActivity.this, new RequestAdapter.OnClickUserBy() {
            @Override
            public void onClickAdd(DataUser datauser) {
                mAddUser(datauser);
            }

            @Override
            public void onClickCancel(DataUser data) {
                mCancelRequest(data);
            }

        });
        binding.mRvUserList.setAdapter(requestAdapter);
        binding.mRvUserList.setLayoutManager(new GridLayoutManager(this, 1));
        findRequests();
        if (Global.banner_all && Global.isInternetConnected(activity)) {
            binding.mRlBanner.setVisibility(VISIBLE);
            ERainAd.getInstance().loadBanner(this, getBannerAll());
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }

    private void onBackCall() {
        finish();
    }

    private void mCancelRequest(DataUser user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (user == null) return;

                DataUser currentUser = CurrentUserUtil.getCurrentUser(activity);
                String currentUserCode = (currentUser != null && currentUser.getCode() != null) ? currentUser.getCode() : "";

                FirebaseRequestHelper.INSTANCE.confirmRequest(user.getCode(), currentUserCode, false, success -> {
                    if (success) {
                        findRequests();
                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.error_request), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void mAddUser(DataUser datauser) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (datauser == null) return;

                DataUser currentUser = CurrentUserUtil.getCurrentUser(activity);
                String currentUserCode = (currentUser != null && currentUser.getCode() != null) ? currentUser.getCode() : "";

                FirebaseRequestHelper.INSTANCE.confirmRequest(datauser.getCode(), currentUserCode, true, success -> {
                    if (success) {
                        findRequests();
                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.error_request), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void findRequests() {
        DataUser currentUser = CurrentUserUtil.getCurrentUser(activity);
        String currentUserCode = (currentUser != null && currentUser.getCode() != null)
                ? currentUser.getCode()
                : "";


        firebaseHelper.getAllRequestsForUser(currentUserCode,
                new FirebaseRequestHelper.OnResultListener() {
                    @Override
                    public void onResult(Object result) {
                        updateRequestsUI((List<DataUser>) result);
                    }

//                    @Override
//                    public void onSuccess(List<DataUser> requests) {
//                        updateRequestsUI(requests);
//                    }
                },
                new FirebaseRequestHelper.OnErrorListener() {
                    @Override
                    public void onError(Exception e) {
                        handleRequestsError(e);
                    }
                });
    }

    /**
     * Updates the UI when requests are successfully fetched.
     */
    private void updateRequestsUI(List<DataUser> requests) {
        mLog("FirebaseRequestHelper=====", "List :: " + requests);

        binding.progressUser.setVisibility(GONE);

        if (requests != null && !requests.isEmpty()) {
            binding.mRvUserList.setVisibility(VISIBLE);
            requestAdapter.setRequests(requests);
            binding.tvNoData.setVisibility(GONE);
        } else {
            // No requests
            binding.mRvUserList.setVisibility(GONE);
            binding.tvNoData.setVisibility(VISIBLE);

        }
    }

    /**
     * Handles errors while fetching requests.
     */
    private void handleRequestsError(Exception e) {
        mLog("FirebaseRequestHelper", "Failed to fetch requests"+e.getMessage());

        binding.mRvUserList.setVisibility(GONE);
        binding.progressUser.setVisibility(GONE);

    }

}
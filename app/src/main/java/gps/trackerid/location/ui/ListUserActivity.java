package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.adapter.FriendListAdapter;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.database.DataUserDao;
import gps.trackerid.location.database.FirebaseRequestHelper;
import gps.trackerid.location.databinding.ActivityListuserBinding;
import gps.trackerid.location.models.users.CurrentUserUtil;
import gps.trackerid.location.models.users.DataUser;
import gps.trackerid.location.models.users.DataUserViewModelFactory;
import gps.trackerid.location.models.users.USerListViewModel;
import gps.trackerid.location.models.zonedata.AppDatabase;
import gps.trackerid.location.ui.baseui.BaseActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ListUserActivity extends BaseActivity {
    ActivityListuserBinding listuserBinding;
    FriendListAdapter friendListAdapter;
    USerListViewModel uSerListViewModel;
    List<DataUser> Data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listuserBinding = ActivityListuserBinding.inflate(getLayoutInflater());
        setContentView(listuserBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DataUserDao dao = AppDatabase.getInstance(this).dataUserDao();

        uSerListViewModel = new ViewModelProvider(
                this,
                new DataUserViewModelFactory(dao)
        ).get(USerListViewModel.class);
        listuserBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        listuserBinding.mIvRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AdsManager.INSTANCE.showInterHome(ListUserActivity.this, () -> {
                    startActivity(new Intent(ListUserActivity.this, RequestActivity.class));
                    return null;
                });
            }
        });
        friendListAdapter = new FriendListAdapter(ListUserActivity.this, new FriendListAdapter.OnClickUserBy() {
            @Override
            public void onClickUser(DataUser dataUser) {
                uSerListViewModel.update(dataUser);
                FirebaseRequestHelper.INSTANCE.updateFriendInFirebase(dataUser);
//                FirebaseUserHelper.INSTANCE.updateUser(dataUser);
            }
        });
        listuserBinding.mRvUserList.setAdapter(friendListAdapter);
        listuserBinding.mRvUserList.setLayoutManager(new GridLayoutManager(this, 1));

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });

        AdsManager.INSTANCE.loadBannerAll(this, listuserBinding.mRlBanner);
    }

    private void onBackCall() {
        AdsManager.INSTANCE.showInterBack(ListUserActivity.this, () -> {
            finish();
            return null;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        findFriends();
    }

    private void findFriends() {
        uSerListViewModel.getAllUsers().observe(this, new Observer<List<DataUser>>() {
            @Override
            public void onChanged(List<DataUser> dataUsers) {
                Data = dataUsers;
                friendListAdapter.setFriends(dataUsers);
            }
        });
        DataUser currentUser = CurrentUserUtil.getCurrentUser(this);
        String currentUserCode = (currentUser != null && currentUser.getCode() != null) ? currentUser.getCode() : "";

        FirebaseRequestHelper.INSTANCE.getFriendsListWithDetails(currentUserCode, new Function1<List<DataUser>, Unit>() {
            @Override
            public Unit invoke(List<DataUser> friends) {
                if (Data.isEmpty() || Data.size() < friends.size()) {
                    handleFriendsResult(friends);
                }
                listuserBinding.progressFindUser.setVisibility(GONE);
                return null;
            }
        });
    }

    /**
     * Handles the result of fetching friends.
     */
    private void handleFriendsResult(List<DataUser> friends) {
        listuserBinding.progressFindUser.setVisibility(GONE);

        if (friends != null && !friends.isEmpty()) {
            // Show RecyclerView and hide "No data" message
            listuserBinding.mRvUserList.setVisibility(VISIBLE);
            listuserBinding.tvNoData.setVisibility(GONE);

            // Update adapter and insert into local database/viewmodel
            friendListAdapter.setFriends(friends);
            if (uSerListViewModel != null)
                uSerListViewModel.insert(friends);
        } else {
            // No friends: hide RecyclerView, show "No data"
            listuserBinding.mRvUserList.setVisibility(GONE);
            listuserBinding.tvNoData.setVisibility(VISIBLE);
        }
    }

}
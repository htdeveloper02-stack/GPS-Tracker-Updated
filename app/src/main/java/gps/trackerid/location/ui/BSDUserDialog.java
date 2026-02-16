package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.UserListAdapter;
import gps.trackerid.location.databinding.FragmentBtmUserBinding;
import gps.trackerid.location.models.users.DataUser;

public class BSDUserDialog extends BottomSheetDialogFragment {
    static BSDUserDialog bsdUserDialog;
    private List<DataUser> userList=new ArrayList<>();
    FragmentBtmUserBinding binding;
    private OnUserSelectedListener userSelectedListener;
    private OnAddUserListener onAddUserListener;
    UserListAdapter userListAdapter;

    public interface OnUserSelectedListener {
        void onUserSelected(DataUser user);
    }

    public interface OnAddUserListener {
        void onAddUser();
    }

    public static BSDUserDialog newInstance(List<DataUser> userList, OnUserSelectedListener userSelectedListener, OnAddUserListener dismissListener) {
        BSDUserDialog sheet = new BSDUserDialog();
        if (!userList.isEmpty()) {
            sheet.userList.add(null);
            sheet.userList.addAll(userList);
        }
        sheet.userSelectedListener = userSelectedListener;
        sheet.onAddUserListener = dismissListener;
        return sheet;
    }

    public BSDUserDialog() {

    }

    public static BSDUserDialog getInstance() {
        return bsdUserDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtmUserBinding.inflate(getLayoutInflater(), container, false);
        bsdUserDialog = this;
        if (userList.isEmpty()) {
            binding.mRvUserList.setVisibility(GONE);
            binding.tvNoUser.setVisibility(VISIBLE);
        } else {
            binding.mRvUserList.setVisibility(VISIBLE);
            binding.tvNoUser.setVisibility(GONE);

        }
        binding.tvNoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddUserListener.onAddUser();
            }
        });
        userListAdapter = new UserListAdapter(getActivity(), new UserListAdapter.OnClickByUser() {
            @Override
            public void OnClickBy(DataUser dataUser) {
                userSelectedListener.onUserSelected(dataUser);
                dismiss();
            }

        }, new UserListAdapter.OnClickByAddUser() {
            @Override
            public void OnClickAdd(DataUser modelLanguage) {
                onAddUserListener.onAddUser();
                dismiss();
            }
        });
        binding.mRvUserList.setAdapter(userListAdapter);
        binding.mRvUserList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        userListAdapter.setUser(this.userList);
        binding.mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return binding.getRoot();
    }
}

package gps.trackerid.location.ui.onboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import gps.trackerid.location.ui.baseui.BaseFragment;

public class OnBoardAdapter extends FragmentStateAdapter {

    private final List<BaseFragment> fragmentList;

    public OnBoardAdapter(@NonNull FragmentActivity fragmentActivity, List<BaseFragment> fragmentList) {
        super(fragmentActivity);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
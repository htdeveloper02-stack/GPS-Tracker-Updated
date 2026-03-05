package gps.trackerid.location.ui.onboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.SharedUtils;
import gps.trackerid.location.databinding.LayoutIntro4Binding;
import gps.trackerid.location.ui.baseui.BaseFragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class OnBoardFragment4 extends BaseFragment {

    private LayoutIntro4Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = LayoutIntro4Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            AdsManager.INSTANCE.loadNativeOb4(
                    getActivity(),
                    SharedUtils.INSTANCE.getValue(SharedUtils.OPEN_APP, false),
                    binding.frAds
            );
            binding.mIvNext.setOnClickListener(view1 -> {
                AdsManager.INSTANCE.showInterOb(getActivity(), () -> {
                    ((OnBoardActivity) getActivity()).launchHomeScreen();
                    return null;
                });
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
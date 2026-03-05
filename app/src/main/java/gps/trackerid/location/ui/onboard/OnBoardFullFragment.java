package gps.trackerid.location.ui.onboard;

import static gps.trackerid.location.ads.PopulateNativeAdViewKt.populateNativeAdView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.RemoteUtils;
import gps.trackerid.location.databinding.LayoutNativeBinding;
import gps.trackerid.location.ui.baseui.BaseFragment;

public class OnBoardFullFragment extends BaseFragment {

    private LayoutNativeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = LayoutNativeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            binding.mIvNext.setOnClickListener(view1 -> ((OnBoardActivity) getActivity()).nextPage());
            if (AdsManager.INSTANCE.getNativeAdObFull() != null) {
                populateNativeAdView(
                        getActivity(), AdsManager.INSTANCE.getNativeAdObFull(), binding.frAds, binding.shimmerAds.shimmerNative,
                        (int) RemoteUtils.INSTANCE.getCTAButtonHeight()
                );
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
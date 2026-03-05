package gps.trackerid.location.ui.onboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import gps.trackerid.location.databinding.LayoutIntro2Binding;
import gps.trackerid.location.databinding.LayoutIntro3Binding;
import gps.trackerid.location.ui.baseui.BaseFragment;

public class OnBoardFragment3 extends BaseFragment {

    private LayoutIntro3Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = LayoutIntro3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            binding.mIvNext.setOnClickListener(view1 -> ((OnBoardActivity) getActivity()).nextPage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
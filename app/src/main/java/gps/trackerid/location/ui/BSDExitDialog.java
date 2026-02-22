package gps.trackerid.location.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import gps.trackerid.location.R;
import gps.trackerid.location.databinding.FragmentBtmExitBinding;

public class BSDExitDialog extends BottomSheetDialogFragment {
    FragmentBtmExitBinding binding;

    public BSDExitDialog() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtmExitBinding.inflate(getLayoutInflater(), container, false);
        binding.mIvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimestampActivity.getInstance() != null) {
                    TimestampActivity.getInstance().finish();
                }
                dismiss();
            }
        });
        binding.mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return binding.getRoot();
    }
}

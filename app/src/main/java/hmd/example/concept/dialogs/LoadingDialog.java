package hmd.example.concept.dialogs;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.ListFragment;
import hmd.example.concept.R;
import hmd.example.concept.databinding.DialogLoadingBinding;
import hmd.example.concept.fragments.MainViewFragment;

/**
 * Created by squaresoft on 8/17/2017.
 */

public class LoadingDialog extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating dialog layout
        DialogLoadingBinding binding = DataBindingUtil.inflate(inflater,R.layout.dialog_loading,container,false);

        View v = binding.getRoot();

        //making fragment clickable for prevent the underling fragment get click events
        v.setClickable(true);
        v.setFocusable(true);
        ///////////////////////////////////////////////////////////////////////////////
        //making dialog transparent and untitled
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //making dialog not being canceled
        setCancelable(false);
        //showing animation
        binding.avi.show();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainViewFragment)getTargetFragment()).setLoadingDialog(this);
    }
}

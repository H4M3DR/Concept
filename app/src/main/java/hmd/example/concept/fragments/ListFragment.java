package hmd.example.concept.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import hmd.example.concept.R;
import hmd.example.concept.adapters.SimpleRecyclerViewAdapter;
import hmd.example.concept.classes.NumberStorage;
import hmd.example.concept.databinding.FragmentListBinding;
import hmd.example.concept.models.SimpleNumber;

public class ListFragment extends Fragment {
    //fragment binding object
    private FragmentListBinding mBinding;
    //store object for numbers
    public NumberStorage mNumberStorage;
    //adapter object for recyclerView
    private SimpleRecyclerViewAdapter mAdapter;
    //numbers list for recyclerView's adapter
    private List<SimpleNumber> numbers = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //the fragment could not be destroyed by setting this to true
        setRetainInstance(true);
        //initiating the numberStorage
        mNumberStorage = new NumberStorage(10);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating the fragment layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        View v = mBinding.getRoot();
        //making fragment clickable for prevent the underling fragment get click events
        v.setClickable(true);
        v.setFocusable(true);
        ///////////////////////////////////////////////////////////////////////////////

        //initiating recyclerView
        mBinding.simpleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SimpleRecyclerViewAdapter(numbers, (AppCompatActivity) getActivity(),mNumberStorage);
        mBinding.simpleRecyclerView.setAdapter(mAdapter);
        /////////////////////////

        //this is for rotation issue
        if (savedInstanceState == null) {
            if (mNumberStorage != null) {
                //setting observer for initial numbers and adding them to adapter when they are ready
                mNumberStorage.getInitialNumbers().observe(this, simpleNumbers -> {
                    numbers.addAll(simpleNumbers);
                    mAdapter.notifyItemInserted(numbers.size());
                });
                //setting observer for paging numbers and adding them to adapter when they are ready
                mNumberStorage.getLaterNumbers().observe(this, simpleNumbers -> {
                    numbers.addAll(simpleNumbers);
                    mAdapter.notifyItemInserted(numbers.size());
                });
            }
        }
        /////////////////////////////

        return v;
    }

    @Override
    public void onDestroy() {
        //setting ImInCurrentFragment on exit in MainViewFragment
        try {
            ((MainViewFragment)getTargetFragment()).setImInCurrentFragment(true);
        }catch (Exception e){}
        super.onDestroy();
    }
}

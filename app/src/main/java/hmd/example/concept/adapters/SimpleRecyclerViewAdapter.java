package hmd.example.concept.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import hmd.example.concept.R;
import hmd.example.concept.classes.NumberStorage;
import hmd.example.concept.databinding.ItemRecyclerListBinding;
import hmd.example.concept.models.SimpleNumber;
import hmd.example.concept.viewModels.SimpleNumberViewModel;

public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemRecyclerListBinding mBinding;

        private MyViewHolder(ItemRecyclerListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setLifecycleOwner(mActivity);
            mBinding.setViewModel(new SimpleNumberViewModel(mActivity));
        }

        void bind(SimpleNumber number) {
            mBinding.getViewModel().setNumber(number);
            mBinding.executePendingBindings();
        }

        void setPosition(int position)
        {
            mBinding.getViewModel().setPosition(position);
        }
    }

    private AppCompatActivity mActivity;

    private List<SimpleNumber> mSimpleNumberList;

    private NumberStorage mNumberStorage;

    public SimpleRecyclerViewAdapter(List<SimpleNumber> numbers, AppCompatActivity activity ,NumberStorage numberStorage) {
        mSimpleNumberList = numbers;
        mActivity = activity;
        this.mNumberStorage = numberStorage;
    }

    public void setNumberList(List<SimpleNumber> numberList) {
        mSimpleNumberList = numberList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        ItemRecyclerListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_recycler_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SimpleNumber number = mSimpleNumberList.get(position);
        ((MyViewHolder) holder).bind(number);
        ((MyViewHolder) holder).setPosition(position);
        if (position == mSimpleNumberList.size() -1)
            mNumberStorage.getMoreNumbers();

    }

    @Override
    public int getItemCount() {
        return mSimpleNumberList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}


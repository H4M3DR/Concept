package hmd.example.concept.viewModels;

import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hmd.example.concept.R;
import hmd.example.concept.models.SimpleNumber;
import hmd.example.concept.util.Utils;

public class SimpleNumberViewModel extends ViewModel {
    private SimpleNumber number;
    public MutableLiveData<String> groupedNumber = new MutableLiveData<>();
    private AppCompatActivity mActivity;
    //item position in list
    private int position;


    public SimpleNumberViewModel(AppCompatActivity activity) {
        mActivity = activity;
        this.position = position;
        groupedNumber.setValue(mActivity.getString(R.string.please_wait));
    }

    public void setNumber(SimpleNumber number) {
        this.number = number;
        //getting the formatted string in case it is ready
        if (number.getFormattedString() != null) {
            groupedNumber.setValue(number.getFormattedString());
        } else {
            new Thread(() -> {
                //splitting the same sequential repetitive numbers
                String[] out = Utils.splitStringSameCharacters(SimpleNumberViewModel.this.number.getNumber().toString());
                String str;
                //grouping same numbers with ','
                if (Build.VERSION.SDK_INT >= 26) {
                    str = String.join(",", out);
                } else {
                    str = Utils.joinStrings(out, ",");
                }
                //setting the result string to model for quicker later use
                number.setFormattedString(str);
                //updating ui
                groupedNumber.postValue(str);
            }).start();
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //recyclerView item click event
    public void onItemClick() {
        //get back to previous fragment if first item
        if (position == 0)
            mActivity.getSupportFragmentManager().popBackStack();
    }
}

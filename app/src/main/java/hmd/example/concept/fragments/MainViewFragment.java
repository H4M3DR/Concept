package hmd.example.concept.fragments;



import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import hmd.example.concept.R;
import hmd.example.concept.classes.Constants;
import hmd.example.concept.classes.ImageStore;
import hmd.example.concept.classes.UrlProcessingUnit;
import hmd.example.concept.databinding.FragmentMainViewBinding;
import hmd.example.concept.dialogs.LoadingDialog;

public class MainViewFragment extends Fragment {
    //fragment binding object
    private FragmentMainViewBinding mBinding;
    //url processing object that looks for given url load
    private UrlProcessingUnit mUrlProcessingUnit ;
    //an store for image urls that gives a random url
    private ImageStore mImageStore ;
    //caching image url for rotation issue
    private String imageUrl;
    //loading dialog object
    private LoadingDialog mLoadingDialog;
    //if it is in current fragment it can show loading
    private boolean imInCurrentFragment = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //the fragment could not be destroyed by setting this to true
        setRetainInstance(true);
        mImageStore = new ImageStore();
        //initiating the url processing unit and giving the url for detection and lifecycle object for lifecycle awareness
        mUrlProcessingUnit = new UrlProcessingUnit(Constants.urlToProcess,getLifecycle());
        //making url processing unit lifecycle aware
        getLifecycle().addObserver(mUrlProcessingUnit);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating the fragment layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_view,container,false);

        View v = mBinding.getRoot();
        //making fragment clickable for prevent the underling fragment get click events
        v.setClickable(true);
        v.setFocusable(true);

        //setting imageView url using glide
        if(imageUrl != null)
        {
            Glide.with(getActivity()).load(imageUrl).into(mBinding.simpleImageView);
        }

        //giving the webView the processing unit
        mBinding.simpleWebView.setWebViewClient(mUrlProcessingUnit);

        //setting the click event for
        mBinding.simpleImageView.setOnClickListener(view -> {
            //getting a random image url from imageStore
            imageUrl = mImageStore.getRandomPic();
            //setting imageView url using glide
            Glide.with(getActivity()).load(imageUrl).into(mBinding.simpleImageView);
        });

        //this is for rotation issue
        if (savedInstanceState == null) {
            //setting url for webView to load
            mBinding.simpleWebView.loadUrl(Constants.url);
            //url detection listener that is lifecycle aware
            mUrlProcessingUnit.setOnUrlDetected(() -> {
                if(imInCurrentFragment) {
                    imInCurrentFragment = false;
                    ListFragment fragment = new ListFragment();
                    fragment.setTargetFragment(MainViewFragment.this, 0);
                    //going to next fragment
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, Constants.LIST_FRAGMENT_TAG).addToBackStack(Constants.LIST_FRAGMENT_TAG).commit();
                }
            });

            mUrlProcessingUnit.setOnDialogShowHide(state -> {
                if (state == UrlProcessingUnit.DialogState.show)
                {
                    showLoading();
                }
                else {
                    hideDialog();
                }
            });
        }
        else {
            //any url detection must be ignored if previous detection finished before rotation
            mUrlProcessingUnit.setShouldIgnore(true);
            //restoring webView's state on screen rotation
            mBinding.simpleWebView.restoreState(savedInstanceState);
        }

        return v;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //saving the webView's state to bundle object on screen rotation
        mBinding.simpleWebView.saveState(outState);
    }

    //method for showing loading animation
    private void showLoading()
    {
        if (mLoadingDialog == null && imInCurrentFragment)
        {
            mLoadingDialog = new LoadingDialog();
            mLoadingDialog.setTargetFragment(this,0);
            mLoadingDialog.show(getActivity().getSupportFragmentManager(),Constants.LOADING_DIALOG_TAG);
        }
    }

    //method for hiding loading animation
    private void hideDialog()
    {
        if (mLoadingDialog != null)
        {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void setLoadingDialog(LoadingDialog loadingDialog) {
        mLoadingDialog = loadingDialog;
    }

    public void setImInCurrentFragment(boolean imInCurrentFragment) {
        this.imInCurrentFragment = imInCurrentFragment;
    }
}

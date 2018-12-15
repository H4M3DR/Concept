package hmd.example.concept.classes;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class UrlProcessingUnit extends WebViewClient implements LifecycleObserver {

    public enum DialogState{
        show,
        hide
    }

    private String processingDomain;
    private Lifecycle mLifecycle;
    private OnUrlDetected mOnUrlDetected;
    private OnDialogShowHide mOnDialogShowHide;
    private boolean pendingUrlDetection = false;
    private boolean pendingDialog = false;
    private DialogState mDialogState = DialogState.hide;
    private boolean shouldIgnore = false;
    private boolean startLoading = false;

    public UrlProcessingUnit(String processingDomain, Lifecycle lifecycle) {
        this.processingDomain = processingDomain;
        this.mLifecycle = lifecycle;
    }

    public interface OnUrlDetected {
        void onDetect();
    }

    public interface OnDialogShowHide
    {
        void onShowHide(DialogState state);
    }

    public void setOnDialogShowHide(OnDialogShowHide onDialogShowHide) {
        mOnDialogShowHide = onDialogShowHide;
    }

    public void setOnUrlDetected(OnUrlDetected onUrlDetected) {
        mOnUrlDetected = onUrlDetected;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void checkPendingUrlDetection() {
        if (pendingUrlDetection) {
            pendingUrlDetection = false;
            urlDetected();
        }
        if (pendingDialog)
        {
            pendingDialog = false;
            showHideDialog(mDialogState);
        }
    }

    private void showHideDialog(DialogState dialogState){
        if (mLifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            if (mOnDialogShowHide != null)
                mOnDialogShowHide.onShowHide(dialogState);
        }
        else {
            pendingDialog = true;
            mDialogState = dialogState;
        }
    }

    private void urlDetected() {
        if (mLifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            if (mOnUrlDetected != null)
                mOnUrlDetected.onDetect();
        } else {
            pendingUrlDetection = true;
        }
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        synchronized (this) {
            startLoading = true;
        }
        showHideDialog(DialogState.show);
        System.out.println("your current url when webpage loading.." + url);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        System.out.println("your current url when webpage loading.. finish" + url);
        showHideDialog(DialogState.hide);
        boolean ignore;
        synchronized (this) {
            ignore = shouldIgnore;
        }

        if (ignore) {
            synchronized (this) {
                startLoading = false;
                shouldIgnore = false;
            }
            return;
        }
        URL theUrl = null;
        try {
            theUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (theUrl != null) {
            String host = theUrl.getHost();
            if (host.contains(processingDomain))
                urlDetected();
        }
        synchronized (this)
        {
            startLoading = false;
        }
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    public boolean isShouldIgnore() {
        return shouldIgnore;
    }

    public void setShouldIgnore(boolean shouldIgnore) {
        boolean alreadyLoading;
        synchronized (this) {
            alreadyLoading = startLoading;
        }
        if (!alreadyLoading)
            this.shouldIgnore = shouldIgnore;
    }
}

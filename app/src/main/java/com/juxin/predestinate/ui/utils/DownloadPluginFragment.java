package com.juxin.predestinate.ui.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.juxin.predestinate.R;

public class DownloadPluginFragment extends DialogFragment implements View.OnClickListener{
    private ProgressBar progressBar;
    public DownloadPluginFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View rootView = inflater.inflate(R.layout.fragment_download_plugin, container);
        rootView.findViewById(R.id.btnClose).setOnClickListener(this);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbDownload);
        return rootView;
    }

    public void updateProgress(int progress){
        if(progressBar != null){
            progressBar.setProgress(progress);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}

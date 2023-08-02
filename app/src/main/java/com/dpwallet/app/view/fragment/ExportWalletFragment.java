package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dpwallet.app.R;

public class ExportWalletFragment extends Fragment  {

    private static final String TAG = "ExportWalletFragment";

    private OnExportWalletCompleteListener mExportWalletListener;

    public static ExportWalletFragment newInstance() {
        ExportWalletFragment fragment = new ExportWalletFragment();
        return fragment;
    }

    public ExportWalletFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.export_wallet_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView skip = (TextView) getView().findViewById(R.id.textView_export_wallet_skip);
        Button exportWallet = (Button) getView().findViewById(R.id.button_export_wallet_export);
        ProgressBar progressBar =(ProgressBar) getView().findViewById(R.id.progress_loader_export_wallet);

        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mExportWalletListener.onExportWalletComplete(0, progressBar);
            }
        });

        exportWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mExportWalletListener.onExportWalletComplete(1, progressBar);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public static interface OnExportWalletCompleteListener {
        public abstract void onExportWalletComplete(int status, ProgressBar progressBar);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mExportWalletListener = (OnExportWalletCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

}

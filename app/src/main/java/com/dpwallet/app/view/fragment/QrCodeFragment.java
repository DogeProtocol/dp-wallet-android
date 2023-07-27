package com.dpwallet.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;

public class QrCodeFragment extends Fragment  {

    private static final String TAG = "QrCodeFragment";

    public static QrCodeFragment newInstance() {
        QrCodeFragment fragment = new QrCodeFragment();
        return fragment;
    }

    public QrCodeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qr_code_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String walletAddress = getArguments().getString("walletAddress");

        Bundle bundleRoute = new Bundle();
        bundleRoute.putString("walletAddress", walletAddress);

        FragmentManager fragmentManager  = getFragmentManager();
        QrCodeDialogFragment qrDialogFragment = QrCodeDialogFragment.newInstance();
        qrDialogFragment.setCancelable(false);
        qrDialogFragment.setArguments(bundleRoute);
        qrDialogFragment.show(fragmentManager, "");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }
}

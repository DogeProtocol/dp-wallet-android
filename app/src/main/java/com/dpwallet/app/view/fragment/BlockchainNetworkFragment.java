package com.dpwallet.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.utils.GlobalMethods;

public class BlockchainNetworkFragment extends Fragment  {

    private static final String TAG = "BlockchainNetworkFragment";

    public static BlockchainNetworkFragment newInstance() {
        BlockchainNetworkFragment fragment = new BlockchainNetworkFragment();
        return fragment;
    }

    public BlockchainNetworkFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blockchain_network_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {

            String languageKey = getArguments().getString("languageKey");
            String walletAddress = getArguments().getString("walletAddress");

            Bundle bundleRoute = new Bundle();
            bundleRoute.putString("languageKey", languageKey);
            bundleRoute.putString("walletAddress", walletAddress);

            FragmentManager fragmentManager = getFragmentManager();
            BlockchainNetworkDialogFragment blockChainDialogFragment = BlockchainNetworkDialogFragment.newInstance();
            blockChainDialogFragment.setCancelable(false);
            blockChainDialogFragment.setArguments(bundleRoute);
            blockChainDialogFragment.show(fragmentManager, "");

        } catch(Exception e){
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
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
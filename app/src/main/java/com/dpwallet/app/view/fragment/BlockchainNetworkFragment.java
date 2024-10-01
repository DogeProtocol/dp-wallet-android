package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dpwallet.app.R;
import com.dpwallet.app.model.BlockchainNetwork;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.GridAutoFitLayoutManager;
import com.dpwallet.app.utils.Utility;
import com.dpwallet.app.view.adapter.BlockchainNetworkAdapter;
import com.dpwallet.app.viewmodel.JsonViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BlockchainNetworkFragment extends Fragment  {

    private static final String TAG = "BlockchainNetworkFragment";

    private BlockchainNetworkAdapter blockchainNetworkAdapter;
    Unbinder unbinder;

    @BindView(R.id.recycler_blockchain_network)
    RecyclerView recycler;

    private OnBlockchainNetworkCompleteListener mBlockchainNetworkListener;

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
        this.unbinder = ButterKnife.bind((Object) this, view);
        try {
            assert getArguments() != null;

            String languageKey = getArguments().getString("languageKey");

            JsonViewModel jsonViewModel = new JsonViewModel(getContext(), languageKey);

            ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_blockchain_network_back_arrow);
            TextView blockchainNetworkTitleTextView = (TextView) getView().findViewById(R.id.textview_blockchain_network_langValues_networks);

            TextView blockchainNetworkIdTextView = (TextView) getView().findViewById(R.id.textView_blockchain_network_header_langValues_id);
            TextView blockchainNetworkNameTextView = (TextView) getView().findViewById(R.id.textView_blockchain_network_header_langValues_name);
            TextView blockchainNetworkScanApiUrlTextView = (TextView) getView().findViewById(R.id.textView_blockchain_network_header_langValues_scanApiUrl);
            TextView blockchainNetworkTxnApiUrlTextView = (TextView) getView().findViewById(R.id.textView_blockchain_network_header_langValues_txnApiUrl);
            TextView blockchainNetworkBlockExplorerUrlTextView = (TextView) getView().findViewById(R.id.textView_blockchain_network_header_langValues_blockExplorerUrl);

            TextView blockchainNetworkAddNetworkTextView = (TextView) getView().findViewById(R.id.textview_blockchain_network_langValues_add_network);

            ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_blockchain_network);

            blockchainNetworkTitleTextView.setText(jsonViewModel.getNetworksByLangValues());
            blockchainNetworkIdTextView.setText(jsonViewModel.getIdByLangValues());
            blockchainNetworkNameTextView.setText(jsonViewModel.getNameByLangValues());

            blockchainNetworkScanApiUrlTextView.setText(jsonViewModel.getScanApiUrlByLangValues());
            blockchainNetworkTxnApiUrlTextView.setText(jsonViewModel.getTxnApiUrlByLangValues());
            blockchainNetworkBlockExplorerUrlTextView.setText(jsonViewModel.getBlockExplorerUrlByLangValues());

            blockchainNetworkAddNetworkTextView.setText(jsonViewModel.getAddNetworkByLangValues());

            progressBar.setVisibility(View.VISIBLE);

            this.recycler.removeAllViewsInLayout();

            int mNoOfColumns = Utility.calculateNoOfColumns(getContext(), R.id.recycler_blockchain_network);

            GridAutoFitLayoutManager mLayoutManager = new GridAutoFitLayoutManager(getContext(),
                    mNoOfColumns, 1, false);

            List<BlockchainNetwork> blockchainNetworkList = GlobalMethods.BlockChainNetworkRead(getContext());

            this.recycler.setLayoutManager(mLayoutManager);

            this.blockchainNetworkAdapter = new BlockchainNetworkAdapter(getContext(), blockchainNetworkList);

            this.recycler.setAdapter(blockchainNetworkAdapter);

            this.blockchainNetworkAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            backArrowImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mBlockchainNetworkListener.onBlockchainNetworkCompleteByBackArrow();
                }
            });

            blockchainNetworkAddNetworkTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mBlockchainNetworkListener.onBlockchainNetworkCompleteByAdd();
                }
            });

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

    public static interface OnBlockchainNetworkCompleteListener {
        public abstract void onBlockchainNetworkCompleteByBackArrow();
        public abstract void onBlockchainNetworkCompleteByAdd();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mBlockchainNetworkListener = (BlockchainNetworkFragment.OnBlockchainNetworkCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

}
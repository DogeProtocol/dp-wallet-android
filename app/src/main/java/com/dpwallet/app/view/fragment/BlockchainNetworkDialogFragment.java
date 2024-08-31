package com.dpwallet.app.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dpwallet.app.R;
import com.dpwallet.app.model.BlockchainNetwork;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.viewmodel.BlockchainNetworkViewModel;
import com.dpwallet.app.viewmodel.JsonViewModel;
import java.util.List;

public class BlockchainNetworkDialogFragment extends DialogFragment {

    private static final String TAG = "BlockchainNetworkDialogFragment";

    private final int jsonIndex = 0;

    private int blockchainRadio = -1;

    private  OnBlockchainNetworkDialogCompleteListener mBlockchainNetworkDialogListener;

    public static BlockchainNetworkDialogFragment newInstance() {
        BlockchainNetworkDialogFragment fragment = new BlockchainNetworkDialogFragment();
        return fragment;
    }

    public BlockchainNetworkDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blockchain_network_dialog_fragment, container, false);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JsonViewModel jsonViewModel = new JsonViewModel(getContext(), getArguments().getString("languageKey"));

        List<BlockchainNetwork> blockchainNetworkList = GlobalMethods.BlockChainNetworkRead(getContext());

        BlockchainNetworkViewModel blockchainNetworkViewModel = new BlockchainNetworkViewModel(getContext());

        TextView blockchainNetworkDialogTitleTextView = (TextView) getView().findViewById(R.id.textView_blockchain_network_dialog_title);
        RadioGroup blockchainNetworkRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup_blockchain_network_dialog);

        Button blockchaintNetworkDialogCancelButton = (Button) getView().findViewById(R.id.button_blockchain_network_dialog_cancel);
        Button blockchaintNetworkDialogOkButton = (Button) getView().findViewById(R.id.button_blockchain_network_dialog_ok);

        blockchainNetworkDialogTitleTextView.setText(jsonViewModel.getSelectNetworkByLangValues());
        blockchaintNetworkDialogCancelButton.setText(jsonViewModel.getCancelByLangValues());
        blockchaintNetworkDialogOkButton.setText(jsonViewModel.getOkByLangValues());

        blockchainNetworkRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                blockchainRadio = (int) radioButton.getTag();
            }
        });

        blockchaintNetworkDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    getDialog().dismiss();
                    mBlockchainNetworkDialogListener.onBlockchainNetworkDialogComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        blockchaintNetworkDialogOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for(BlockchainNetwork blockchainNetwork : blockchainNetworkList){
            RadioButton blockNetworkRadio = new RadioButton(getContext());
            blockNetworkRadio.setId(View.generateViewId());
            blockNetworkRadio.setText(blockchainNetwork.getBlockchainName() + " ( Network Id " + blockchainNetwork.getNetworkId() + ")"); // +
                    //"(" + blockchainNetworkViewModel.getNetworkId() + ") " + blockchainNetwork.getNetworkId() );
            blockchainNetworkRadioGroup.addView(blockNetworkRadio);
        }

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }


    public static interface OnBlockchainNetworkDialogCompleteListener {
        public abstract void onBlockchainNetworkDialogComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mBlockchainNetworkDialogListener = (OnBlockchainNetworkDialogCompleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

}

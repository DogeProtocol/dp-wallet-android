package com.dpwallet.app.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dpwallet.app.R;
import com.dpwallet.app.model.BlockchainNetwork;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.viewmodel.JsonViewModel;

import org.json.JSONException;

import java.util.List;

public class BlockchainNetworkDialogFragment extends DialogFragment {

    private static final String TAG = "BlockchainNetworkDialogFragment";

    private int blockchainRadio = 0;

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

        String blockchainNetworkIdCurrentIndex = getArguments().getString("blockchainNetworkIdIndex");
        assert blockchainNetworkIdCurrentIndex != null;
        int blockchainNetworkIdIndex = Integer.parseInt(blockchainNetworkIdCurrentIndex);

        List<BlockchainNetwork> blockchainNetworkList = null;
        try {
            blockchainNetworkList = GlobalMethods.BlockChainNetworkRead(getContext());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //BlockchainNetworkViewModel blockchainNetworkViewModel = new BlockchainNetworkViewModel(getContext());

        TextView blockchainNetworkDialogTitleTextView = (TextView) getView().findViewById(R.id.textView_blockchain_network_dialog_title);
        RadioGroup blockchainNetworkRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup_blockchain_network_dialog);
        blockchainNetworkRadioGroup.clearCheck();
        blockchainNetworkRadioGroup.removeAllViews();

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
                    mBlockchainNetworkDialogListener.onBlockchainNetworkDialogCancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        blockchaintNetworkDialogOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    getDialog().dismiss();
                    if(blockchainNetworkIdIndex != blockchainRadio) {
                        PrefConnect.writeInteger(getContext(), PrefConnect.BLOCKCHAIN_NETWORK_ID_INDEX_KEY, blockchainRadio);
                        mBlockchainNetworkDialogListener.onBlockchainNetworkDialogOk();
                    } else {
                        mBlockchainNetworkDialogListener.onBlockchainNetworkDialogCancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        int currentPosition = 0;
        for(BlockchainNetwork blockchainNetwork : blockchainNetworkList){
            RadioButton blockNetworkRadio = new RadioButton(getContext());
            blockNetworkRadio.setText(blockchainNetwork.getBlockchainName() + " ( Network Id " + blockchainNetwork.getNetworkId() + ")"); // +
            blockNetworkRadio.setTag(currentPosition);
            blockchainNetworkRadioGroup.addView(blockNetworkRadio, currentPosition);
            if(blockchainNetworkIdIndex==currentPosition) {
                blockNetworkRadio.setChecked(true);
            }
            currentPosition++;
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
        public abstract void onBlockchainNetworkDialogCancel();
        public abstract void onBlockchainNetworkDialogOk();

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

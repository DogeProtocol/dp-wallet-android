package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dpwallet.app.R;
import com.dpwallet.app.model.BlockchainNetwork;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.viewmodel.JsonViewModel;
import org.json.JSONObject;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BlockchainNetworkAddFragment extends Fragment  {

    private static final String TAG = "BlockchainNetworkAddFragment";

    Unbinder unbinder;

    private JsonViewModel jsonViewModel;

    private BlockchainNetworkAddFragment.OnBlockchainNetworkAddCompleteListener mBlockchainNetworkAddListener;

    public static BlockchainNetworkAddFragment newInstance() {
        BlockchainNetworkAddFragment fragment = new BlockchainNetworkAddFragment();
        return fragment;
    }

    public BlockchainNetworkAddFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blockchain_network_add_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.unbinder = ButterKnife.bind((Object) this, view);
        try {
            assert getArguments() != null;
            jsonViewModel = new JsonViewModel(getContext(), getArguments().getString("languageKey"));

            ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_blockchain_network_add_back_arrow);

            TextView blockchainNetworkAddNetworkTextView = (TextView) getView().findViewById(R.id.textview_blockchain_network_add_langValues_add_network);
            TextView blockchainNetworkEnterNetworkJsonTextView = (TextView) getView().findViewById(R.id.textview_blockchain_network_add_langValues_enter_network_json);
            EditText blockchainNetworkAddNetworkEditText = (EditText) getView().findViewById(R.id.editText_blockchain_network_add_langValues_add_network);

            Button blockchainNetworkAddNetworkButton = (Button) getView().findViewById(R.id.button_blockchain_network_add_langValues_add);

            ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_blockchain_network_add);

            blockchainNetworkAddNetworkEditText.setText(makeJSON().toString());

            blockchainNetworkAddNetworkTextView.setText(jsonViewModel.getAddNetworkByLangValues());
            blockchainNetworkEnterNetworkJsonTextView.setText(jsonViewModel.getEnterNetworkJsonByLangValues());
            blockchainNetworkAddNetworkButton.setText(jsonViewModel.getAddByLangValues());

            backArrowImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mBlockchainNetworkAddListener.onBlockchainNetworkAddComplete();
                }
            });

            blockchainNetworkAddNetworkButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        JSONObject obj = new JSONObject(blockchainNetworkAddNetworkEditText.getText().toString());
                        String scanApiDomain = (String) obj.get("scanApiDomain");
                        String txnApiDomain = (String) obj.get("txnApiDomain");
                        String blockExplorerDomain = (String) obj.get("blockExplorerDomain");
                        String blockchainName = (String) obj.get("blockchainName");
                        String networkId = String.valueOf(obj.get("networkId"));

                        List<BlockchainNetwork> blockchainNetworkList = GlobalMethods.BlockChainNetworkRead(getContext());
                        BlockchainNetwork blockchainNetwork = new BlockchainNetwork();
                        blockchainNetwork.setScanApiDomain(scanApiDomain);
                        blockchainNetwork.setTxnApiDomain(txnApiDomain);
                        blockchainNetwork.setBlockExplorerDomain(blockExplorerDomain);
                        blockchainNetwork.setBlockchainName(blockchainName);
                        blockchainNetwork.setNetworkId(networkId);
                        blockchainNetworkList.add(blockchainNetwork);

                        String jsonString = "";

                        for(BlockchainNetwork blockchainNetwork1 :  blockchainNetworkList ){
                            if(jsonString != "")
                            {
                                jsonString = jsonString + ",";
                            }
                            jsonString = jsonString + "{'scanApiDomain': '" +  blockchainNetwork1.getScanApiDomain() + "'," +
                                    "'txnApiDomain': '" +  blockchainNetwork1.getTxnApiDomain() + "'," +
                                    "'blockExplorerDomain': '" +  blockchainNetwork1.getBlockExplorerDomain() + "'," +
                                    "'blockchainName': '" +  blockchainNetwork1.getBlockchainName() + "'," +
                                    "'networkId': " +  blockchainNetwork1.getNetworkId() + "}";
                        }

                        String json = "{ 'networks' : [" + jsonString+ "]}";
                        PrefConnect.writeString(getContext(), PrefConnect.BLOCKCHAIN_NETWORK_LIST,json);

                        Toast.makeText(getContext(), "Added successfully!",
                                Toast.LENGTH_SHORT).show();

                        mBlockchainNetworkAddListener.onBlockchainNetworkAddComplete();
                    } catch (Exception e) {
                        GlobalMethods.ExceptionError(getContext(), TAG, e);
                    }
                    progressBar.setVisibility(View.GONE);
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

    public static interface OnBlockchainNetworkAddCompleteListener {
        public abstract void onBlockchainNetworkAddComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mBlockchainNetworkAddListener = (BlockchainNetworkAddFragment.OnBlockchainNetworkAddCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }


    public JSONObject makeJSON() {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("scanApiDomain", "scan-example.dpapi.org");
            jObj.put("txnApiDomain",  "txn-example.dpapi.org");
            jObj.put("blockExplorerDomain",  "explorer-example.dpscan.app");
            jObj.put("blockchainName",  "EXAMPLE NET");
            jObj.put("networkId",  312354);
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
        return jObj;
    }



}
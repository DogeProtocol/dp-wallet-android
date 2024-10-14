package com.dpwallet.app.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.dpwallet.app.R;
import com.dpwallet.app.model.BlockchainNetwork;
import com.dpwallet.app.utils.GlobalMethods;

import java.util.List;
import java.util.Map;

public class BlockchainNetworkAdapter extends
        Adapter<BlockchainNetworkAdapter.DataObjectHolder> {

    private static final String TAG = "BlockchainNetworkAdapter";

    public List<BlockchainNetwork> blockchainNetworks;

    private Context context;

    class DataObjectHolder extends ViewHolder {
        TextView textViewId;
        TextView textViewName;
        TextView textViewScanApiUrl;
        TextView textViewTxnApiUrl;
        TextView textViewBlockExplore;

        public DataObjectHolder(View itemView) {
            super(itemView);
            try{
                this.textViewId = (TextView) itemView.findViewById(R.id.textView_blockchain_network_adapter_langValues_id);
                this.textViewName = (TextView) itemView.findViewById(R.id.textView_blockchain_network_adapter_langValues_name);
                this.textViewScanApiUrl = (TextView) itemView.findViewById(R.id.textView_blockchain_network_adapter_langValues_scanApiUrl);
                this.textViewTxnApiUrl = (TextView) itemView.findViewById(R.id.textView_blockchain_network_adapter_langValues_txnApiUrl);
                this.textViewBlockExplore = (TextView) itemView.findViewById(R.id.textView_blockchain_network_adapter_langValues_blockExplorerUrl);
            } catch(Exception ex){
                GlobalMethods.ExceptionError(context, TAG, ex);
            }
        }
    }

    public BlockchainNetworkAdapter(Context context, List<BlockchainNetwork>  blockchainNetworks) {
        this.context = context;
        this.blockchainNetworks = blockchainNetworks;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new DataObjectHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.blockchain_network_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
            holder.textViewId.setText(blockchainNetworks.get(position).getNetworkId());
            holder.textViewName.setText(blockchainNetworks.get(position).getBlockchainName());
            holder.textViewScanApiUrl.setText(blockchainNetworks.get(position).getScanApiDomain());
            holder.textViewTxnApiUrl.setText(blockchainNetworks.get(position).getTxnApiDomain());
            holder.textViewBlockExplore.setText(blockchainNetworks.get(position).getBlockExplorerDomain());
        }catch(Exception ex){
            GlobalMethods.ExceptionError(context, TAG, ex);
        }
    }

    @Override
    public int getItemCount() {
        return this.blockchainNetworks == null ? 0 : this.blockchainNetworks.size();
    }
}

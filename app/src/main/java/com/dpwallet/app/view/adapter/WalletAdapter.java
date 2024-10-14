package com.dpwallet.app.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.dpwallet.app.R;
import com.dpwallet.app.api.read.model.AccountTransactionSummary;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.viewmodel.KeyViewModel;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class WalletAdapter extends
        Adapter<WalletAdapter.DataObjectHolder> {

    private static final String TAG = "WalletAdapter";

    OnWalletItemClickListener clickListener;

    public Map<String, String> walletSummaries;

    public static String walletAddress;

    private Context context;

    class DataObjectHolder extends ViewHolder {
        TextView textViewAddress;
        ImageView imageViewExplore;
        ImageView imageViewRevealSeed;

        public DataObjectHolder(View itemView) {
            super(itemView);
            try{
                this.textViewAddress = (TextView) itemView.findViewById(R.id.textView_waller_adapter_address);
                this.imageViewExplore = (ImageView) itemView.findViewById(R.id.imageView_wallet_adapter_explore);
                this.imageViewRevealSeed = (ImageView) itemView.findViewById(R.id.imageView_wallet_adapter_reveal_seed);
            } catch(Exception ex){
                GlobalMethods.ExceptionError(context, TAG, ex);
            }
        }

       // @Override
       // public void onClick(View v) {
       //     clickListener.onWalletItemClick(v, getAdapterPosition());
       // }
    }

    public WalletAdapter(Context context, Map<String, String> walletSummaries) {
        this.context = context;
        this.walletSummaries = walletSummaries;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new DataObjectHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.wallet_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {

            String address = (walletSummaries.get(String.valueOf(position)).toString());

            holder.textViewAddress.setText(address.substring(2,7) + "..." + address.substring(address.length()-5,address.length()));

            holder.textViewAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onWalletItemClick(v, position);
                }
            });

            holder.imageViewExplore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(GlobalMethods.BLOCK_EXPLORER_URL + GlobalMethods.BLOCK_EXPLORER_ACCOUNT_TRANSACTION_URL.replace("{address}", address)))
                    );
                }
            });

            holder.imageViewRevealSeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onWalletRevealClick(v, position);
                }
            });

        }catch(Exception ex){
            GlobalMethods.ExceptionError(context, TAG, ex);
        }
    }

    @Override
    public int getItemCount() {
        return this.walletSummaries == null ? 0 : this.walletSummaries.size();
    }


    public void SetOnWalletItemClickListener(
            final OnWalletItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnWalletItemClickListener {
        public void onWalletItemClick(View view, int position);
        public void onWalletRevealClick(View view, int position);
    }
}

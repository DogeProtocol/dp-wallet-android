package com.dpwallet.app.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.dpwallet.app.interact.BlockchainNetworkInteract;
import com.dpwallet.app.utils.GlobalMethods;

import org.json.JSONException;

//@HiltViewModel
public class BlockchainNetworkViewModel extends ViewModel{
  //@Inject
    private BlockchainNetworkInteract _blockchainNetworkInteract;

    //@Inject
    public BlockchainNetworkViewModel(Context context) {

    }

    public String getNetWorks() {
        try {
            return _blockchainNetworkInteract.getNetWorks();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getScanApiDomain() {
        try {
            return _blockchainNetworkInteract.getScanApiDomain();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTxnApiDomain() {
        try {
            return _blockchainNetworkInteract.getTxnApiDomain();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBlockExplorerDomain() {
        try {
            return _blockchainNetworkInteract.getBlockExplorerDomain();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBlockchainName() {
        try {
            return _blockchainNetworkInteract.getBlockchainName();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNetworkId() {
        try {
            return _blockchainNetworkInteract.getNetworkId();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

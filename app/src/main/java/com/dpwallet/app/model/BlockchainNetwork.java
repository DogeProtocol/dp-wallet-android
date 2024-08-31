package com.dpwallet.app.model;

import com.google.gson.annotations.SerializedName;

public class BlockchainNetwork {

    public static final String SERIALIZED_NAME_BALANCE = "blockchainNetwork";

    @SerializedName(SERIALIZED_NAME_BALANCE)
    private String scanApiDomain;
    private String txnApiDomain;
    private String blockExplorerDomain;
    private String blockchainName;
    private String networkId;

    public BlockchainNetwork() {

    }

    public BlockchainNetwork(String scanApiDomain, String txnApiDomain, String blockExplorerDomain, String blockchainName, String networkId) {
        this.scanApiDomain = scanApiDomain;
        this.txnApiDomain = txnApiDomain;
        this.blockExplorerDomain = blockExplorerDomain;
        this.blockchainName = blockchainName;
        this.networkId = networkId;
    }

    public String getScanApiDomain() {
        return scanApiDomain;
    }

    public void setScanApiDomain(String scanApiDomain) {
        this.scanApiDomain = scanApiDomain;
    }

    public String getTxnApiDomain() {
        return txnApiDomain;
    }

    public void setTxnApiDomain(String txnApiDomain) {
        this.txnApiDomain = txnApiDomain;
    }

    public String getBlockExplorerDomain() {
        return blockExplorerDomain;
    }

    public void setBlockExplorerDomain(String blockExplorerDomain) {
        this.blockExplorerDomain = blockExplorerDomain;
    }

    public String getBlockchainName() {
        return blockchainName;
    }

    public void setBlockchainName(String blockchainName) {
        this.blockchainName = blockchainName;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    @Override
    public String toString() {
        return "BlockchainNetwork{" +
                "scanApiDomain='" + scanApiDomain + '\'' +
                ", txnApiDomain=" + txnApiDomain + '\'' +
                ", blockExplorerDomain=" + blockExplorerDomain + '\'' +
                ", blockchainName=" + blockchainName + '\'' +
                ", networkId=" + networkId +
                '}';
    }
}

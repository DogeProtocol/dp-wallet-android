package com.dpwallet.app.asynctask.faucet;

import android.content.Context;
import android.os.AsyncTask;

import com.dpwallet.app.api.faucet.ApiException;
import com.dpwallet.app.api.faucet.api.FaucetApi;
import com.dpwallet.app.api.faucet.model.InlineObject;
import com.dpwallet.app.api.faucet.model.FaucetTransactionSummaryResponse;

public class FaucetTransactionRestTask extends AsyncTask<String, Void, Void> {

    private FaucetTransactionSummaryResponse faucetTransactionSummaryResponse;
    private Context context;
    private TaskListener taskListener;
    private ApiException apiException;

    public FaucetTransactionRestTask(Context context,
                               TaskListener listener) {
        this.context = context;
        this.taskListener = listener;
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected */
    @Override
    public Void doInBackground(String... params) {
        String address = params[0];
        InlineObject inlineObject = new InlineObject();
        inlineObject.address(address);

        FaucetApi apiInstance = new FaucetApi();

        try {
            faucetTransactionSummaryResponse = apiInstance.sendFaucetTransaction(inlineObject);
        } catch (ApiException e) {
            apiException = e;
        }
        return (Void)null;
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPostExecute(Void result) {
        super.onPostExecute(result);

        if(this.taskListener != null) {
            if (apiException == null) {
                this.taskListener.onFinished(this.faucetTransactionSummaryResponse);
            } else {
                this.taskListener.onFailure(apiException);
            }
        }
    }

    public interface TaskListener {
        public void onFinished(FaucetTransactionSummaryResponse faucetTransactionSummaryResponse);
        public void onFailure(ApiException apiException);
    }
}

package com.dpwallet.app.asynctask.write;

import android.content.Context;
import android.os.AsyncTask;

import com.dpwallet.app.api.write.ApiException;
import com.dpwallet.app.api.write.api.AccountsApi;
import com.dpwallet.app.api.write.model.InlineObject;
import com.dpwallet.app.api.write.model.TransactionSummaryResponse;

public class TransactionRestTask extends AsyncTask<String, Void, Void> {

    private TransactionSummaryResponse transactionSummaryResponse;
    private Context context;
    private TaskListener taskListener;
    private ApiException apiException;

    public TransactionRestTask(Context context,
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
        String txnData = params[0];
        InlineObject inlineObject = new InlineObject();
        inlineObject.txnData(txnData);

        AccountsApi apiInstance = new AccountsApi();

        try {
            transactionSummaryResponse = apiInstance.sendTransaction(inlineObject);
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
                this.taskListener.onFinished(this.transactionSummaryResponse);
            } else {
                this.taskListener.onFailure(apiException);
            }
        }
    }

    public interface TaskListener {
        public void onFinished(TransactionSummaryResponse transactionSummaryResponse);
        public void onFailure(ApiException apiException);
    }
}

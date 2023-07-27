package com.dpwallet.app.asynctask.read;

import android.content.Context;
import android.os.AsyncTask;

import com.dpwallet.app.api.read.ApiException;
import com.dpwallet.app.api.read.api.AccountsApi;
import com.dpwallet.app.api.read.model.AccountPendingTransactionSummaryResponse;

public class AccountPendingTxnRestTask  extends AsyncTask<String, Void, Void> {

    private AccountPendingTransactionSummaryResponse accountPendingTransactionSummaryResponse;
    private Context context;
    private TaskListener taskListener;
    private ApiException apiException;

    public AccountPendingTxnRestTask(Context context,
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
        int pageindex = Integer.valueOf(params[1]);

        AccountsApi apiInstance = new AccountsApi();

        try {
            accountPendingTransactionSummaryResponse = apiInstance.listAccountPendingTransactions(
                    address,pageindex);
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
                this.taskListener.onFinished(this.accountPendingTransactionSummaryResponse);
            } else {
                this.taskListener.onFailure(apiException);
            }
        }
    }

    public interface TaskListener {
        public void onFinished(AccountPendingTransactionSummaryResponse accountPendingTransactionSummaryResponse);
        public void onFailure(ApiException apiException);

    }
}

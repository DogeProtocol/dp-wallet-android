package com.dpwallet.app.asynctask.read;

import android.content.Context;
import android.os.AsyncTask;

import com.dpwallet.app.api.read.ApiException;
import com.dpwallet.app.api.read.api.AccountsApi;
import com.dpwallet.app.api.read.model.AccountTransactionSummaryResponse;

public class AccountTxnRestTask  extends AsyncTask<String, Void, Void> {

    private AccountTransactionSummaryResponse accountTransactionSummaryResponse;
    private Context context;
    private TaskListener taskListener;
    private ApiException apiException;

    public AccountTxnRestTask(Context context,
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

        //ApiClient defaultClient = Configuration.getDefaultApiClient();
        //((ApiKeyAuth) defaultClient.getAuthentication("signature")).setApiKey(signature);
        //((ApiKeyAuth) defaultClient.getAuthentication("token")).setApiKey(token);

        AccountsApi apiInstance = new AccountsApi();

        try {
            accountTransactionSummaryResponse = apiInstance.listAccountTransactions(
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
                this.taskListener.onFinished(this.accountTransactionSummaryResponse);
            } else {
                this.taskListener.onFailure(apiException);
            }
        }
    }

    public interface TaskListener {
        public void onFinished(AccountTransactionSummaryResponse accountTransactionSummaryResponse);
        public void onFailure(ApiException apiException);
    }
}

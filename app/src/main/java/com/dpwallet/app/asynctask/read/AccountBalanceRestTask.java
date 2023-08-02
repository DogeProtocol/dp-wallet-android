package com.dpwallet.app.asynctask.read;

import android.content.Context;
import android.os.AsyncTask;

import com.dpwallet.app.api.read.ApiException;
import com.dpwallet.app.api.read.api.AccountsApi;
import com.dpwallet.app.api.read.model.BalanceResponse;
import com.dpwallet.app.entity.ServiceException;

public class AccountBalanceRestTask extends AsyncTask<String, Void, Void> {

    private BalanceResponse balanceResponse;
    private Context context;
    private TaskListener taskListener;
    private ApiException apiException;

    public AccountBalanceRestTask(Context context,
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

        AccountsApi apiInstance = new AccountsApi();

        try {
            balanceResponse = apiInstance.getAccountBalance(address);
        } catch (ApiException e) {
            apiException = e;
        }
        return (Void)null;
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPostExecute(Void result) {
        super.onPostExecute(result);
        try {
            if(this.taskListener != null) {
                if (apiException == null) {
                    this.taskListener.onFinished(this.balanceResponse);
                } else {
                    this.taskListener.onFailure(apiException);
                }
            }
        }
        catch (Exception e){

        }
    }

    public interface TaskListener {
        public void onFinished(BalanceResponse balanceResponse) throws ServiceException;
        public void onFailure(ApiException apiException);
    }
}

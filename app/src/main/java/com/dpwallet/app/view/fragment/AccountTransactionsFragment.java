package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dpwallet.app.R;
import com.dpwallet.app.asynctask.read.AccountPendingTxnRestTask;
import com.dpwallet.app.asynctask.read.AccountTxnRestTask;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.GridAutoFitLayoutManager;
import com.dpwallet.app.utils.Utility;
import com.dpwallet.app.api.read.model.AccountTransactionSummary;
import com.dpwallet.app.api.read.model.AccountTransactionSummaryResponse;
import com.dpwallet.app.api.read.model.AccountPendingTransactionSummary;
import com.dpwallet.app.api.read.model.AccountPendingTransactionSummaryResponse;
import com.dpwallet.app.view.adapter.AccountPendingTransactionAdapter;
import com.dpwallet.app.view.adapter.AccountTransactionAdapter;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AccountTransactionsFragment extends Fragment  {

    private static final String TAG = "AccountTransactionsFragment";

    private int pageIndex = 1;
    private int pageCount = 1;

    private AccountTransactionAdapter accountTransactionAdapter;
    private List<AccountTransactionSummary> accountTransactionSummaries;

    private AccountPendingTransactionAdapter accountPendingTransactionAdapter;
    private List<AccountPendingTransactionSummary> accountPendingTransactionSummaries;

    Unbinder unbinder;
    @BindView(R.id.recycler_account_transactions) RecyclerView recycler;

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    private OnAccountTransactionsCompleteListener mAccountTransactionsListener;

    private int transactionStatus = 0;

    public static AccountTransactionsFragment newInstance() {
        AccountTransactionsFragment fragment = new AccountTransactionsFragment();
        return fragment;
    }

    public AccountTransactionsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_transactions_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.unbinder = ButterKnife.bind((Object) this, view);

        String languageKey = getArguments().getString("languageKey");
        String walletAddress = getArguments().getString("walletAddress");

        JsonViewModel jsonViewModel = new JsonViewModel(getContext(), languageKey);

        ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_account_transactions_back_arrow);

        ImageButton accountTransactionRefreshImageButton = (ImageButton) getView().findViewById(R.id.imageButton_account_transactions_refresh);

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_account_transactions);

        ToggleButton accountTransactionCompletedToggleButton = view.findViewById(R.id.toggleButton_account_transactions_langValues_completed);
        ToggleButton accountTransactionPendingToggleButton = view.findViewById(R.id.toggleButton_account_transactions_langValues_pending);;

        TextView accountTransactionHeaderInOutTextView = (TextView) getView().findViewById(R.id.textView_account_transaction_header_langValues_in_out);
        TextView accountTransactionHeaderQuantityTextView = (TextView) getView().findViewById(R.id.textView_account_transaction_header_langValues_quantity);
        TextView accountTransactionHeaderFromTextView = (TextView) getView().findViewById(R.id.textView_account_transaction_header_langValues_from);
        TextView accountTransactionHeaderToTextView = (TextView) getView().findViewById(R.id.textView_account_transaction_header_langValues_to);
        TextView accountTransactionHeaderTransactionHashTextView = (TextView) getView().findViewById(R.id.textView_account_transaction_header_langValues_trans_hash);



        MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.materialButtonToggleGroup_account_transactions_group);
        MaterialButton previousMaterialButton = toggleGroup.findViewById(R.id.materialButton_account_transactions_langValues_previous);
        MaterialButton nextMaterialButton = toggleGroup.findViewById(R.id.materialButton_account_transactions_langValues_next);

        linerLayoutOffline = (LinearLayout) getView().findViewById(R.id.linerLayout_account_transactions_offline);
        imageViewRetry = (ImageView) getView().findViewById(R.id.image_retry);
        textViewTitleRetry = (TextView) getView().findViewById(R.id.textview_title_retry);
        textViewSubTitleRetry = (TextView) getView().findViewById(R.id.textview_subtitle_retry);
        Button buttonRetry = (Button) getView().findViewById(R.id.button_retry);

        accountTransactionCompletedToggleButton.setText(jsonViewModel.getCompletedTransactionsByLangValues());
        accountTransactionPendingToggleButton.setText(jsonViewModel.getPendingTransactionsByLangValues());

        accountTransactionHeaderInOutTextView.setText(jsonViewModel.getInoutByLangValues());
        accountTransactionHeaderQuantityTextView.setText(jsonViewModel.getCoinsByLangValues());
        accountTransactionHeaderFromTextView.setText(jsonViewModel.getFromByLangValues());
        accountTransactionHeaderToTextView.setText(jsonViewModel.getToByLangValues());
        accountTransactionHeaderTransactionHashTextView.setText(jsonViewModel.getHashByLangValues());

        previousMaterialButton.setText("<");
        nextMaterialButton.setText(">");

        this.recycler.removeAllViewsInLayout();
        this.accountTransactionSummaries = new ArrayList<>();
        this.accountPendingTransactionSummaries = new ArrayList<>();

        int mNoOfColumns = Utility.calculateNoOfColumns(getContext(),
                R.id.recycler_account_transactions);

        GridAutoFitLayoutManager mLayoutManager = new GridAutoFitLayoutManager(getContext(),
                mNoOfColumns, 1, false);

        this.recycler.setLayoutManager(mLayoutManager);

        this.accountTransactionAdapter = new AccountTransactionAdapter(getContext(),
                accountTransactionSummaries, walletAddress);

        this.accountPendingTransactionAdapter = new AccountPendingTransactionAdapter(getContext(),
                accountPendingTransactionSummaries, walletAddress);

        this.recycler.setAdapter(accountTransactionAdapter);
        ListAccountTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);

        accountTransactionRefreshImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch(transactionStatus) {
                    case 0:
                        recycler.setAdapter(accountTransactionAdapter);
                        ListAccountTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
                        break;
                    case 1:
                        recycler.setAdapter(accountPendingTransactionAdapter);
                        ListAccountPendingTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
                        break;
                }
            }
        });

        backArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAccountTransactionsListener.onAccountTransactionsComplete();
            }
        });

        accountTransactionCompletedToggleButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                accountTransactionCompletedToggleButton.setChecked(true);
                accountTransactionCompletedToggleButton.setTypeface(accountTransactionCompletedToggleButton.getTypeface(), Typeface.BOLD);
                accountTransactionCompletedToggleButton.setTextColor(getResources().getColor(R.color.colorCommon2));

                accountTransactionPendingToggleButton.setChecked(false);
                accountTransactionPendingToggleButton.setTypeface(accountTransactionPendingToggleButton.getTypeface(), Typeface.NORMAL);
                accountTransactionPendingToggleButton.setTextColor(getResources().getColor(R.color.colorCommon3));

                transactionStatus = 0;
                pageIndex = 1;
                pageCount = 1;

                accountPendingTransactionSummaries.clear();

                recycler.setAdapter(accountTransactionAdapter);
                ListAccountTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
            }
        });

        accountTransactionPendingToggleButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                accountTransactionPendingToggleButton.setChecked(true);
                accountTransactionPendingToggleButton.setTypeface(accountTransactionPendingToggleButton.getTypeface(), Typeface.BOLD);
                accountTransactionPendingToggleButton.setTextColor(getResources().getColor(R.color.colorCommon2));

                accountTransactionCompletedToggleButton.setChecked(false);
                accountTransactionCompletedToggleButton.setTypeface(accountTransactionCompletedToggleButton.getTypeface(), Typeface.NORMAL);
                accountTransactionCompletedToggleButton.setTextColor(getResources().getColor(R.color.colorCommon3));

                transactionStatus = 1;
                pageIndex = 1;
                pageCount = 1;

                accountTransactionSummaries.clear();

                recycler.setAdapter(accountPendingTransactionAdapter);
                ListAccountPendingTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
            }
        });

        //press previous
        previousMaterialButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                switch(transactionStatus) {
                    case 0:
                        recycler.setAdapter(accountTransactionAdapter);
                        ListAccountTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
                        break;
                    case 1:
                        recycler.setAdapter(accountPendingTransactionAdapter);
                        ListAccountPendingTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
                        break;
                }
                if(pageIndex<pageCount) {
                    pageIndex++;
                }
            }
        });

        //press next
        nextMaterialButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(pageIndex>1) {
                    pageIndex--;
                }
                switch(transactionStatus) {
                    case 0:
                        recycler.setAdapter(accountTransactionAdapter);
                        ListAccountTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
                        break;
                    case 1:
                        recycler.setAdapter(accountPendingTransactionAdapter);
                        ListAccountPendingTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
                        break;
                }
            }
        });

        buttonRetry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                switch(transactionStatus) {
                    case 0:
                        recycler.setAdapter(accountTransactionAdapter);
                        ListAccountTransactionByAccount(getContext(), walletAddress, progressBar,pageIndex);
                        break;
                    case 1:
                        recycler.setAdapter(accountPendingTransactionAdapter);
                        ListAccountPendingTransactionByAccount(getContext(), walletAddress, progressBar, pageIndex);
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }



    public static interface OnAccountTransactionsCompleteListener {
        public abstract void onAccountTransactionsComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mAccountTransactionsListener = (OnAccountTransactionsCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }


    private void ListAccountTransactionByAccount(Context context, String address, ProgressBar progressBar, int pageIndex) {
        try{
            linerLayoutOffline.setVisibility(View.GONE);
            if (progressBar.getVisibility() == View.VISIBLE) {
                String message = getResources().getString(R.string.transaction_message_exits);
                GlobalMethods.ShowToast(context, message);
                return;
            }

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {

                String[] taskParams = {address, String.valueOf(pageIndex)};

                progressBar.setVisibility(View.VISIBLE);
                recycler.removeAllViewsInLayout();
                accountTransactionSummaries.clear();

                AccountTxnRestTask task = new AccountTxnRestTask(
                        context, new AccountTxnRestTask.TaskListener() {
                    @Override
                    public void onFinished(AccountTransactionSummaryResponse accountTransactionSummaryResponse) {
                        if (accountTransactionSummaryResponse != null && accountTransactionSummaryResponse.getResult().size() > 0) {
                            pageCount = accountTransactionSummaryResponse.getPageCount();
                            accountTransactionSummaries.addAll(accountTransactionSummaryResponse.getResult());
                            accountTransactionAdapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(com.dpwallet.app.api.read.ApiException e) {
                        progressBar.setVisibility(View.GONE);
                        int code = e.getCode();
                        boolean check = GlobalMethods.ApiExceptionSourceCodeBoolean(code);
                        if(check == true) {
                            GlobalMethods.ApiExceptionSourceCodeRoute(getContext(), code,
                                    getString(R.string.apierror),
                                    TAG + " : AccountTxnRestTask : " + e.toString());
                        } else {
                            GlobalMethods.OfflineOrExceptionError(getContext(),
                                    linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                                    textViewSubTitleRetry, true);
                        }
                    }
                });

                task.execute(taskParams);
            } else {
                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void ListAccountPendingTransactionByAccount(Context context, String address, ProgressBar progressBar, int pageIndex) {
        try{

            linerLayoutOffline.setVisibility(View.GONE);

            if (progressBar.getVisibility() == View.VISIBLE) {
                String message = getResources().getString(R.string.transaction_message_exits);
                GlobalMethods.ShowToast(getContext(), message);
                return;
            }

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {

                String[] taskParams = { address, String.valueOf(pageIndex) };

                progressBar.setVisibility(View.VISIBLE);
                recycler.removeAllViewsInLayout();
                accountPendingTransactionSummaries.clear();

                AccountPendingTxnRestTask task = new AccountPendingTxnRestTask(
                        context, new AccountPendingTxnRestTask.TaskListener() {
                    @Override
                    public void onFinished(AccountPendingTransactionSummaryResponse accountPendingTransactionSummaryResponse) {
                        if (accountPendingTransactionSummaryResponse != null && accountPendingTransactionSummaryResponse.getResult().size()>0) {
                            pageCount = accountPendingTransactionSummaryResponse.getPageCount();
                            accountPendingTransactionSummaries.addAll(accountPendingTransactionSummaryResponse.getResult());
                            accountPendingTransactionAdapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(com.dpwallet.app.api.read.ApiException e) {
                        progressBar.setVisibility(View.GONE);
                        int code = e.getCode();
                        boolean check = GlobalMethods.ApiExceptionSourceCodeBoolean(code);
                        if(check == true) {
                            GlobalMethods.ApiExceptionSourceCodeRoute(getContext(), code,
                                    getString(R.string.apierror),
                                    TAG + " : AccountPendingTxnRestTask : " + e.toString());
                        } else {
                            GlobalMethods.OfflineOrExceptionError(getContext(),
                                    linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                                    textViewSubTitleRetry, true);
                        }
                    }
                });

                task.execute(taskParams);
            } else {
                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }
}

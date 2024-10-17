package com.dpwallet.app.view.fragment;

import static org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.api.read.model.BalanceResponse;
import com.dpwallet.app.api.write.model.TransactionSummaryResponse;
import com.dpwallet.app.asynctask.read.AccountBalanceRestTask;
import com.dpwallet.app.asynctask.write.TransactionRestTask;
import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.view.adapter.SeedWordAutoCompleteAdapter;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;

import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.google.android.gms.common.util.Strings;
import com.opencsv.CSVReader;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

public class GetCoinsDogepTokensFragment extends Fragment  {

    private static final String TAG = "GetCoinsDogepTokensFragment";

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    private OnGetCoinsCompleteListener mGetCoinsListener;

    public static GetCoinsDogepTokensFragment newInstance() {
        GetCoinsDogepTokensFragment fragment = new GetCoinsDogepTokensFragment();
        return fragment;
    }

    public GetCoinsDogepTokensFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            return inflater.inflate(R.layout.getcoins_dogep_tokens_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        String walletAddress  = getArguments().getString("walletAddress");
        String walletPassword = getArguments().getString("walletPassword");
        String languageKey = getArguments().getString("languageKey");

        ArrayList<SnapWallet> ethAddressList = new ArrayList<SnapWallet>();

        JsonViewModel jsonViewModel = new JsonViewModel(getContext(), languageKey);
        KeyViewModel keyViewModel = new KeyViewModel();

        LinearLayout linerLayoutGetCoinsSeedWords = (LinearLayout) getView().findViewById(R.id.linear_layout_getCoins_eth_seed_words);
        ImageButton getCoinBackArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_blockchain_network_back_arrow);

        TextView getCoinsViewGetCoinsForTokensTextView = (TextView) getView().findViewById(R.id.textview_getCoins_langValues_getCoinsForTokens);
        TextView getCoinsViewGetCoinsForTokensInfoTextView = (TextView) getView().findViewById(R.id.textview_getCoins_langValues_getCoinsForTokensInfo);
        TextView getCoinsViewEnterEthSeedTextView = (TextView) getView().findViewById(R.id.textview_getCoins_langValues_enterEthSeed);
        EditText[] getCoinsEditText = GetCoinsEditText();
        Button getCoinSeedNextButton = (Button) getView().findViewById(R.id.button_getCoins_seed_next);

        LinearLayout linerLayoutGetCoinsEthAddress = (LinearLayout) getView().findViewById(R.id.linear_layout_getCoins_ethAddress);
        TextView getCoinsSelectAnOptionTextView = (TextView) getView().findViewById(R.id.textView_getCoins_langValues_select_an_option);
        Spinner selectEthAddressSpinner = (Spinner) getView().findViewById(R.id.spinner_ethAddress);
        TextView getCoinsMessageTextView = (TextView) getView().findViewById(R.id.textview_getCoins_message);
        Button ethAddressNextButton = (Button) getView().findViewById(R.id.button_getCoins_ethAddress_next);

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_blockchain_network);

        linerLayoutOffline = (LinearLayout) getView().findViewById(R.id.linerLayout_send_offline);
        imageViewRetry = (ImageView) getView().findViewById(R.id.image_retry);
        textViewTitleRetry = (TextView) getView().findViewById(R.id.textview_title_retry);
        textViewSubTitleRetry = (TextView) getView().findViewById(R.id.textview_subtitle_retry);

        Button buttonRetry = (Button) getView().findViewById(R.id.button_retry);

        getCoinsViewGetCoinsForTokensTextView.setText(jsonViewModel.getGetCoinsForDogePTokensByLangValues());
        getCoinsViewGetCoinsForTokensInfoTextView.setText(jsonViewModel.getGetCoinsForTokensInfoByLangValues());
        getCoinsViewEnterEthSeedTextView.setText(jsonViewModel.getEnterEthSeedByLangValues());
        getCoinSeedNextButton.setText(jsonViewModel.getNextByLangValues());

        getCoinsSelectAnOptionTextView.setText(jsonViewModel.getSelectAnOptionByLangValues());

        ethAddressNextButton.setText(jsonViewModel.getNextByLangValues());

        //getCoinMessageNextButton.setText(jsonViewModel.getNextByLangValues());

        getCoinBackArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (linerLayoutGetCoinsSeedWords.getVisibility() == View.VISIBLE) {
                    mGetCoinsListener.onGetCoinsCompleteByBackArrow();
                }
                if (linerLayoutGetCoinsEthAddress.getVisibility() == View.VISIBLE) {
                    linerLayoutGetCoinsSeedWords.setVisibility(View.VISIBLE);
                    linerLayoutGetCoinsEthAddress.setVisibility(View.GONE);
                }
            }
        });

        getCoinSeedNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String seedPhrase = "";

                for (int i = 0; i < 12; i++) {
                    String word = getCoinsEditText[i].getText().toString();
                    if (word.trim().length() < 2) {
                        messageDialogFragment(languageKey, jsonViewModel.getEnterEthSeedByLangValues());
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                    if (i == 0) {
                        seedPhrase = word;
                    } else {
                        seedPhrase = seedPhrase + " " + word;
                    }
                }

                byte[] seed = MnemonicUtils.generateSeed(seedPhrase, "");

                if (seed.length > 0)
                {
                    //HDWallet newWallet = new HDWallet(seedPhrase, "");
                    //PrivateKey walletKey = newWallet.getKeyForCoin(CoinType.ETHEREUM);

                    Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);

                    progressBar.setVisibility(View.VISIBLE);
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int index = 0; index < GlobalMethods.ethAddressSeedDrivePathCount; index++) {
                        //PrivateKey privateKey = newWallet.getKey(CoinType.ETHEREUM,"m/44\'/60\'/0\'/0/" + index);
                        //String ethAddress = CoinType.ETHEREUM.deriveAddress(privateKey);

                        final int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, index};
                        Bip32ECKeyPair childKeypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);
                        Credentials credential = Credentials.create(childKeypair);
                        String ethAddress = credential.getAddress();

                        boolean snapShotValidate = CsvValidation(ethAddress);
                        if (snapShotValidate) {
                            assert walletAddress != null;
                            //"\\x19Ethereum Signed Message:\n32" +
                            //byte[] p = privateKey.getPublicKeySecp256k1(false).data();
                            //byte[] e = ethAddress.getBytes();
                            //int[] intArray = new int[e.length];
                            //for (int i = 0; i < e.length; intArray[i] = e[i++]);
                            //System.out.println(Arrays.toString(intArray));
                            //int[] r = new int[] {175, 112, 110, 200, 202, 165, 140, 14, 188, 172, 236, 26, 164, 183, 234, 229, 23, 98, 244, 72};

/*
                            String message =  GlobalMethods.CONVERSION_MESSAGE_TEMPLATE
                                    .replace("[ETH_ADDRESS]",ethAddress.toLowerCase())
                                    .replace("[QUANTUM_ADDRESS]",walletAddress.toLowerCase());

                            byte[] digest = Hash.keccak256(message.getBytes());
                            byte[] bytesArray= privateKey.sign(digest, Curve.SECP256K1);
                            String ethSignature = GlobalMethods.ADDRESS_START_PREFIX + bytesToHex(patchSignatureVComponent(bytesArray));
*/
                            String message =  GlobalMethods.CONVERSION_MESSAGE_TEMPLATE
                                    .replace("[ETH_ADDRESS]",ethAddress.toLowerCase())
                                    .replace("[QUANTUM_ADDRESS]",walletAddress.toLowerCase());

                            byte[] messageBytes = message.getBytes();
                            Sign.SignatureData signature = Sign.signPrefixedMessage(messageBytes, credential.getEcKeyPair());

                            byte[] retval = new byte[65];
                            System.arraycopy(signature.getR(), 0, retval, 0, 32);
                            System.arraycopy(signature.getS(), 0, retval, 32, 32);
                            System.arraycopy(signature.getV(), 0, retval, 64, 1);

                            String ethSignature = Numeric.toHexString(retval);

                            SnapWallet snapWallet = new SnapWallet(ethAddress, message, ethSignature);
                            ethAddressList.add(snapWallet);
                            arrayList.add(ethAddress);
                        }
                    }
                    progressBar.setVisibility(View.GONE);

                    if (!ethAddressList.isEmpty()) {
                        linerLayoutGetCoinsEthAddress.setVisibility(View.VISIBLE);
                        linerLayoutGetCoinsSeedWords.setVisibility(View.GONE);

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectEthAddressSpinner.setAdapter(arrayAdapter);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        messageDialogFragment(languageKey, jsonViewModel.getNoEthConversionWallet());
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    messageDialogFragment(languageKey, jsonViewModel.getEthSeedError());
                }
            }
        });

        selectEthAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String address = parent.getItemAtPosition(position).toString();
                if(address.toLowerCase().equalsIgnoreCase( ethAddressList.get(position).ethAddress)) {
                    String message = ethAddressList.get(position).message;
                    getCoinsMessageTextView.setText(message);
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        ethAddressNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(SnapWallet snapWallet: ethAddressList) {
                    if(snapWallet.ethAddress.equalsIgnoreCase(selectEthAddressSpinner.getSelectedItem().toString())) {
                        if (walletPassword == null || walletPassword.isEmpty()) {
                            unlockDialogFragment(jsonViewModel, keyViewModel, progressBar,
                                    walletAddress, snapWallet.ethAddress, snapWallet.ethSignature, languageKey);
                        } else {
                            sendConversionTransaction(jsonViewModel, keyViewModel, progressBar,
                                    walletAddress, walletPassword, snapWallet.ethAddress, snapWallet.ethSignature, languageKey);
                        }
                        break;
                    }
                }
            }
        });

        buttonRetry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String message = getResources().getString(R.string.send_transaction_message_description);
                messageDialogFragment(languageKey, message);
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

    public static interface OnGetCoinsCompleteListener {
        public abstract void onGetCoinsCompleteByBackArrow();
        public abstract void onGetCoinsCompleteBySendData(String walletPassword);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mGetCoinsListener = (OnGetCoinsCompleteListener)context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

    private EditText[] GetCoinsEditText() {
        EditText[] getCoinsEditText = new EditText[]{
                getView().findViewById(R.id.editText_getCoins_text_1),
                getView().findViewById(R.id.editText_getCoins_text_2),
                getView().findViewById(R.id.editText_getCoins_text_3),
                getView().findViewById(R.id.editText_getCoins_text_4),
                getView().findViewById(R.id.editText_getCoins_text_5),
                getView().findViewById(R.id.editText_getCoins_text_6),
                getView().findViewById(R.id.editText_getCoins_text_7),
                getView().findViewById(R.id.editText_getCoins_text_8),
                getView().findViewById(R.id.editText_getCoins_text_9),
                getView().findViewById(R.id.editText_getCoins_text_10),
                getView().findViewById(R.id.editText_getCoins_text_11),
                getView().findViewById(R.id.editText_getCoins_text_12)
        };
        return getCoinsEditText;
    }

    private void messageDialogFragment(String languageKey, String message) {
        try {
            Bundle bundleRoute = new Bundle();
            bundleRoute.putString("message", message);
            bundleRoute.putString("languageKey", languageKey);
            FragmentManager fragmentManager  = getFragmentManager();
            MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
            messageDialogFragment.setCancelable(false);
            messageDialogFragment.setArguments(bundleRoute);
            messageDialogFragment.show(fragmentManager, "");
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private boolean CsvValidation(String ethAddress ) {
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.dp)));//Specify asset file name
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if(ethAddress.equalsIgnoreCase(nextLine[0].toLowerCase()))
                {
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
           // Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void unlockDialogFragment(JsonViewModel jsonViewModel, KeyViewModel keyViewModel, ProgressBar progressBar,
                                      String walletAddress, String ethAddress, String ethSignature, String languageKey) {
        try {
            //Alert unlock dialog
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle((CharSequence) "").setView((int)
                            R.layout.unlock_dialog_fragment).create();
            dialog.dismiss();
            dialog.setCancelable(false);
            dialog.show();

            TextView unlockWalletTextView = (TextView) dialog.findViewById(R.id.textView_unlock_langValues_unlock_wallet);
            TextView unlockPasswordTextView = (TextView) dialog.findViewById(R.id.textView_unlock_langValues_enter_wallet_password);

            EditText passwordEditText = (EditText) dialog.findViewById(R.id.editText_unlock_langValues_enter_a_password);
            Button unlockButton = (Button) dialog.findViewById(R.id.button_unlock_langValues_unlock);
            Button closeButton = (Button) dialog.findViewById(R.id.button_unlock_langValues_close);

            unlockWalletTextView.setText(jsonViewModel.getUnlockWalletByLangValues());
            unlockPasswordTextView.setText(jsonViewModel.getEnterQuantumWalletPasswordByLangValues());
            passwordEditText.setHint(jsonViewModel.getEnterApasswordByLangValues());
            unlockButton.setText(jsonViewModel.getUnlockByLangValues());
            closeButton.setText(jsonViewModel.getCloseByLangValues());

            passwordEditText.setText("Test123$$Test123$$");

            unlockButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String walletPassword = passwordEditText.getText().toString();
                    if (walletPassword.isEmpty()) {
                        messageDialogFragment(languageKey, jsonViewModel.getEnterApasswordByLangValues());
                    } else {
                        dialog.dismiss();
                        sendConversionTransaction(jsonViewModel, keyViewModel, progressBar,
                                walletAddress, walletPassword, ethAddress, ethSignature, languageKey);
                    }
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void sendConversionTransaction(JsonViewModel jsonViewModel, KeyViewModel keyViewModel, ProgressBar progressBar,
                                           String walletAddress, String walletPassword, String ethAddress, String ethSignature,
                                           String languageKey) {
        try {
            if (progressBar.getVisibility() == View.VISIBLE) {
                String message = getResources().getString(R.string.send_transaction_message_exits);
                GlobalMethods.ShowToast(requireContext(), message);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                String[] keyData = keyViewModel.decryptDataByAccount(getContext(), walletAddress, walletPassword);
                int[] SK_KEY =  GlobalMethods.GetIntDataArrayByString(keyData[0]);
                int[] PK_KEY =  GlobalMethods.GetIntDataArrayByString(keyData[1]);

                //int[] PK_KEY1 =  GlobalMethods.GetIntDataArrayByString(keyViewModel.publicKeyFromPrivateKey(SK_KEY));
                //if(PK_KEY.equals(PK_KEY1)) {

                    getBalanceNonceByAccount(jsonViewModel, keyViewModel, progressBar,
                            walletAddress, walletPassword, ethAddress, ethSignature, languageKey, SK_KEY, PK_KEY);

                //}
            }
            return;
        } catch (KeyServiceException | InvalidKeyException e){
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
        messageDialogFragment(languageKey, jsonViewModel.getWalletPasswordMismatchByErrors());
    }

    private  void getBalanceNonceByAccount(JsonViewModel jsonViewModel, KeyViewModel keyViewModel, ProgressBar progressBar,
                                           String walletAddress, String walletPassword, String ethAddress, String ethSignature,
                                           String languageKey, int[] SK_KEY, int[] PK_KEY) {
        try{
            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {

                String[] taskParams = { walletAddress };

                AccountBalanceRestTask task = new AccountBalanceRestTask(
                        getContext(), new AccountBalanceRestTask.TaskListener() {
                    @Override
                    public void onFinished(BalanceResponse balanceResponse) {
                        getTxData(jsonViewModel, keyViewModel, progressBar,
                                walletAddress, walletPassword, ethAddress, ethSignature, languageKey,
                                SK_KEY, PK_KEY, balanceResponse);
                    }
                    @Override
                    public void onFailure(com.dpwallet.app.api.read.ApiException e) {
                        progressBar.setVisibility(View.GONE);

                        int code = e.getCode();
                        boolean check = GlobalMethods.ApiExceptionSourceCodeBoolean(code);
                        if(check) {
                            GlobalMethods.ApiExceptionSourceCodeRoute(getContext(), code,
                                    getString(R.string.apierror),
                                    TAG + " : AccountBalanceRestTask : " + e.toString());
                        } else {
                            GlobalMethods.OfflineOrExceptionError(getContext(),
                                    linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                                    textViewSubTitleRetry, true);
                        }
                    }
                });

                task.execute(taskParams);
            } else {
                progressBar.setVisibility(View.GONE);
                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    @SuppressLint("VisibleForTests")
    private  void getTxData(JsonViewModel jsonViewModel, KeyViewModel keyViewModel, ProgressBar progressBar,
                            String walletAddress, String walletPassword, String ethAddress, String ethSignature,
                            String languageKey, int[] SK_KEY, int[] PK_KEY, BalanceResponse balanceResponse){
        try {
            String NONCE = "0";

            //Nonce
            if(balanceResponse.getResult().getNonce() != null){
                String nonce = (String)balanceResponse.getResult().getNonce();
                if(!Strings.isEmptyOrWhitespace(nonce)){
                    NONCE = nonce;
                }
            }

            //int[] PK_KEY = (int[]) keyViewModel.publicKeyFromPrivateKey(SK_KEY);

            String contractData = keyViewModel.getContractData("requestConversion", GlobalMethods.CONVERSION_CONTRACT_ABI,
                    ethAddress, ethSignature);

            String s = contractData.toString();

            int[] messageData = keyViewModel.getTxnSigningHash(walletAddress, NONCE, GlobalMethods.CONVERSION_CONTRACT_ADDRESS,
                    "0.0", GlobalMethods.CONVERSION_DOGEP_GAS_LIMIT, contractData, GlobalMethods.NETWORK_ID);

            int[] SIGN = GlobalMethods.GetIntDataArrayByString(keyViewModel.signAccount(messageData, SK_KEY));
            int verify = (int) keyViewModel.verifyAccount(messageData, SIGN, PK_KEY);

            if(verify==0){
                //String txHash = (String) keyViewModel.getTxHash(walletAddress, NONCE, GlobalMethods.CONVERSION_CONTRACT_ADDRESS,
                //        "0.0", GlobalMethods.CONVERSION_DOGEP_GAS_LIMIT, contractData, GlobalMethods.NETWORK_ID, PK_KEY, SIGN);

                String txData = (String) keyViewModel.getTxData(walletAddress, NONCE, GlobalMethods.CONVERSION_CONTRACT_ADDRESS,
                        "0.0", GlobalMethods.CONVERSION_DOGEP_GAS_LIMIT, contractData, GlobalMethods.NETWORK_ID, PK_KEY, SIGN);

                transactionByAccount(progressBar, txData, walletPassword);
            } else {
                progressBar.setVisibility(View.GONE);
                messageDialogFragment(languageKey, getResources().getString(R.string.unlock_message_description));
            }
        } catch (ServiceException e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private  void transactionByAccount(ProgressBar progressBar, String txData, String walletPassword) {
        try{
            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {

                String[] taskParams = { txData };

                TransactionRestTask task = new TransactionRestTask(
                        getContext(), new TransactionRestTask.TaskListener() {
                    @Override
                    public void onFinished(TransactionSummaryResponse transactionSummaryResponse) {
                        sendCompletedDialogFragment(walletPassword);
                    }
                    @Override
                    public void onFailure(com.dpwallet.app.api.write.ApiException e) {
                        progressBar.setVisibility(View.GONE);

                        int code = e.getCode();
                        boolean check = GlobalMethods.ApiExceptionSourceCodeBoolean(code);
                        if(check == true) {
                            GlobalMethods.ApiExceptionSourceCodeRoute(getContext(), code,
                                    getString(R.string.apierror),
                                    TAG + " : TransactionRestTask : " + e.toString());
                        } else {
                            GlobalMethods.OfflineOrExceptionError(getContext(),
                                    linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                                    textViewSubTitleRetry, true);
                        }
                    }
                });
                task.execute(taskParams);
            } else {
                progressBar.setVisibility(View.GONE);
                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void sendCompletedDialogFragment(String walletPassword) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle((CharSequence) "").setView((int)
                            R.layout.send_completed_dialog_fragment).create();
            dialog.setCancelable(false);
            dialog.show();
            TextView textViewOk = (TextView) dialog.findViewById(
                    R.id.textView_send_completed_alert_dialog_ok);
            textViewOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    mGetCoinsListener.onGetCoinsCompleteBySendData(walletPassword);
                }
            });
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    public class SnapWallet
    {
        String ethAddress;
        String message;
        String ethSignature;
        public SnapWallet(String ethAddress, String message, String ethSignature)
        {
            this.ethAddress = ethAddress;
            this.message = message;
            this.ethSignature = ethSignature;
        }
    }

}



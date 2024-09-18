package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dpwallet.app.R;
import com.dpwallet.app.view.adapter.SeedWordAutoCompleteAdapter;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;

import java.io.InputStreamReader;
import com.opencsv.CSVReader;

import wallet.core.jni.CoinType;
import wallet.core.jni.Curve;
import wallet.core.jni.HDWallet;
import wallet.core.jni.Hash;
import wallet.core.jni.Mnemonic;
import wallet.core.jni.PrivateKey;

public class GetCoinsDogepTokensFragment extends Fragment  {

    private static final String TAG = "GetCoinsDogepTokensFragment";

    private JsonViewModel jsonViewModel;

    private KeyViewModel keyViewModel;
    private  int[] tempSeedArrayByCreate;
    private SeedWordAutoCompleteAdapter seedWordAutoCompleteAdapter;
    // private TextView[] homeSeedWordsViewTextViews;
    private AutoCompleteTextView[] homeSeedWordsViewAutoCompleteTextViews;
    private boolean autoCompleteIndexStatus = false;
    private int autoCompleteCurrentIndex = 0;
    private  String  walletPassword = null;
    private String walletIndexKey = "0";

    private GetCoinsDogepTokensFragment.OnGetCoinsCompleteListener mGetCoinsListener;

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

        String walletAddress  = getArguments().getString("walletAddress");
        String walletPassword = getArguments().getString("walletPassword");

        jsonViewModel = new JsonViewModel(getContext(), getArguments().getString("languageKey"));
        keyViewModel = new KeyViewModel();

        ImageButton getCoinBackArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_blockchain_network_back_arrow);
        TextView getCoinsViewGetCoinsForTokensTextView = (TextView) getView().findViewById(R.id.textview_getCoins_langValues_getCoinsForTokens);
        TextView getCoinsViewGetCoinsForTokensInfoTextView = (TextView) getView().findViewById(R.id.textview_getCoins_langValues_getCoinsForTokensInfo);
        TextView getCoinsViewEnterEthSeedTextView = (TextView) getView().findViewById(R.id.textview_getCoins_langValues_enterEthSeed);
        EditText[] getCoinsEditText = GetCoinsEditText();
        Button getCoinNextButton = (Button) getView().findViewById(R.id.button_getCoins_next);
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_blockchain_network);

        getCoinsViewGetCoinsForTokensTextView.setText(jsonViewModel.getGetCoinsForDogePTokensByLangValues());
        getCoinsViewGetCoinsForTokensInfoTextView.setText(jsonViewModel.getGetCoinsForTokensInfoByLangValues());
        getCoinsViewEnterEthSeedTextView.setText(jsonViewModel.getEnterEthSeedByLangValues());
        getCoinNextButton.setText(jsonViewModel.getNextByLangValues());

        getCoinBackArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mGetCoinsListener.onGetCoinsComplete();
            }
        });

        getCoinNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String seedPhrase = "";
                for (int i = 1; i <= 12; i++) {
                    String word = getCoinsEditText[i].getText().toString();
                    if (word == null || word.trim().length() < 2) {
                        return;
                    }
                    if (i == 1) {
                        seedPhrase = word;
                    } else {
                        seedPhrase = seedPhrase + " " + word;
                    }
                }

                String ethAddress = "";
                if (Mnemonic.isValid(seedPhrase))
                {
                    HDWallet newWallet = new HDWallet(seedPhrase, "");
                    PrivateKey pk = newWallet.getKeyForCoin(CoinType.ETHEREUM);
                    ethAddress = CoinType.ETHEREUM.deriveAddress(pk);


                    //storeHDKey(newWallet, false);
                    //checkAuthentication(IMPORT_HD_KEY);
                    CsvValidation(ethAddress);
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

    public static interface OnGetCoinsCompleteListener {
        public abstract void onGetCoinsComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mGetCoinsListener = (GetCoinsDogepTokensFragment.OnGetCoinsCompleteListener)context;
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

}

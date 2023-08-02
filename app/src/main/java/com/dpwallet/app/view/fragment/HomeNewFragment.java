package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;

public class HomeNewFragment extends Fragment  {

    private static final String TAG = "HomeNewFragment";

    private OnHomeNewCompleteListener mHomeNewListener;

    public static HomeNewFragment newInstance() {
        HomeNewFragment fragment = new HomeNewFragment();
        return fragment;
    }

    public HomeNewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_new_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout n1linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_new_middle_1);
        LinearLayout n2linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_new_middle_2);
        LinearLayout n3linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_new_middle_3);
        LinearLayout n4linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_new_middle_4);
        LinearLayout n5linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_new_middle_5);
        LinearLayout n6linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_new_middle_6);
        LinearLayout n7linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_new_middle_7);

        Button next1Button = (Button) getView().findViewById(R.id.button_home_new_next_1);
        Button next2Button = (Button) getView().findViewById(R.id.button_home_new_next_2);
        Button next3Button = (Button) getView().findViewById(R.id.button_home_new_next_3);
        Button next4Button = (Button) getView().findViewById(R.id.button_home_new_next_4);
        Button ok5Button = (Button) getView().findViewById(R.id.button_home_new_ok_5);
        Button submit6Button = (Button) getView().findViewById(R.id.button_home_new_submit_6);
        Button newWallet = (Button) getView().findViewById(R.id.button_home_new_wallet_7);
        Button importWallet = (Button) getView().findViewById(R.id.button_home_import_wallet_7);

        RadioButton radioButton_1 = (RadioButton) getView().findViewById(R.id.radioButton_home_new_1);
        RadioButton radioButton_2 = (RadioButton) getView().findViewById(R.id.radioButton_home_new_2);
        RadioButton radioButton_3 = (RadioButton) getView().findViewById(R.id.radioButton_home_new_3);
        RadioButton radioButton_4 = (RadioButton) getView().findViewById(R.id.radioButton_home_new_4);

        String screenStart = getArguments().getString("screenStart");

        switch(screenStart){
            case "1":
                n1linearLayout.setVisibility(View.VISIBLE);
                break;
            case "7":
                n1linearLayout.setVisibility(View.GONE);
                n7linearLayout.setVisibility(View.VISIBLE);
                break;
        }

        next1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                n1linearLayout.setVisibility(View.GONE);
                n2linearLayout.setVisibility(View.VISIBLE);
            }
        });

        next2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                n2linearLayout.setVisibility(View.GONE);
                n3linearLayout.setVisibility(View.VISIBLE);
            }
        });

        next3Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                n3linearLayout.setVisibility(View.GONE);
                n4linearLayout.setVisibility(View.VISIBLE);
            }
        });

        next4Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                n4linearLayout.setVisibility(View.GONE);
                n5linearLayout.setVisibility(View.VISIBLE);
            }
        });

        ok5Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                n5linearLayout.setVisibility(View.GONE);
                n6linearLayout.setVisibility(View.VISIBLE);
            }
        });

        submit6Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundleRoute = new Bundle();

                if(radioButton_3.isChecked() == true){
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle((CharSequence) "").setView((int)
                                    R.layout.safety_quiz_alert_dialog_fragment).create();
                    dialog.setCancelable(false);
                    dialog.show();

                    TextView textViewOk = (TextView) dialog.findViewById(
                            R.id.textView_safety_quiz_alert_dialog_ok);

                    textViewOk.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            n6linearLayout.setVisibility(View.GONE);
                            n7linearLayout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    });

                } else {
                    bundleRoute.putString("message", getResources().getString(R.string.home_safety_quiz_message_wrong));
                    FragmentManager fragmentManager  = getFragmentManager();
                    MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                    messageDialogFragment.setCancelable(false);
                    messageDialogFragment.setArguments(bundleRoute);
                    messageDialogFragment.show(fragmentManager, "");
                }
            }
        });

        newWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHomeNewListener.onHomeNewComplete(0);
            }
        });

        importWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHomeNewListener.onHomeNewComplete(1);
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

    public static interface OnHomeNewCompleteListener {
        public abstract void onHomeNewComplete(int status);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mHomeNewListener = (OnHomeNewCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

}

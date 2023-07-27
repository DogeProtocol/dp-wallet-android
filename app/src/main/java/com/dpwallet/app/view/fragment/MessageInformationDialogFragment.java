package com.dpwallet.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dpwallet.app.R;
import com.dpwallet.app.utils.GlobalMethods;

public class MessageInformationDialogFragment extends DialogFragment  {

    private static final String TAG = "MessageDialogFragment";

    public static MessageInformationDialogFragment newInstance() {
        MessageInformationDialogFragment fragment = new MessageInformationDialogFragment();
        return fragment;
    }

    public MessageInformationDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_information_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            TextView messageTextView = (TextView) getView().findViewById(R.id.textView_message_information_dialog_description);
            TextView closeTextView = (TextView) getView().findViewById(R.id.textView_message_information_dialog_close);

            String message = getArguments().getString("message");
            messageTextView.setText(message);

            closeTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

}

package com.dpwallet.app.view.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.dpwallet.app.R;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.Utility;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class ReceiveFragment extends Fragment  {

    private static final String TAG = "ReceiveFragment";

    private OnReceiveCompleteListener mReceiveListener;

    public static ReceiveFragment newInstance() {
        ReceiveFragment fragment = new ReceiveFragment();
        return fragment;
    }

    public ReceiveFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.receive_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String languageKey = getArguments().getString("languageKey");
        String walletAddress = getArguments().getString("walletAddress");

        JsonViewModel jsonViewModel = new JsonViewModel(getContext(), languageKey);

        ImageButton receiveBackArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_receive_back_arrow);
        TextView receiveCoinsTextView = (TextView) getView().findViewById(R.id.textview_receive_langValues_receive_coins);
        TextView receiveSendOnlyTextView = (TextView) getView().findViewById(R.id.textView_receive_langValues_send_only);
        TextView receiveAddressTextView = (TextView) getView().findViewById(R.id.textView_receive_wallet_address);

        ImageButton copyClipboardImageButton = (ImageButton) getView().findViewById(R.id.imageButton_receive_copy_clipboard);
        ImageView qrCodeImageView = (ImageView) getView().findViewById(R.id.imageView_receive_qr_code);

        receiveCoinsTextView.setText(jsonViewModel.getReceive_coinsByLangValues());
        receiveSendOnlyTextView.setText(jsonViewModel.getSendOnlyByLangValues());
        receiveAddressTextView.setText(walletAddress);

        receiveBackArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mReceiveListener.onReceiveComplete();
            }
        });

        copyClipboardImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClipboardManager clipBoard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("receiveAddress", receiveAddressTextView.getText());
                clipBoard.setPrimaryClip(clipData);
            }
        });

        try {
            Bitmap qrBitMap = generateQrCode(getContext(), walletAddress);
            qrCodeImageView.setImageBitmap(qrBitMap);
        } catch (WriterException e) {
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

    public static interface OnReceiveCompleteListener {
        public abstract void onReceiveComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mReceiveListener = (OnReceiveCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

    private static Bitmap generateQrCode(Context context, String address) throws WriterException {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // H = 30% damage

        QRCodeWriter writer = new QRCodeWriter();
        try {
            int screenWidth = (Utility.calculateScreenWidthDp(context) * 75 / 100);;
            int screenHeight =  screenWidth;

            BitMatrix bitMatrix = writer.encode(address, BarcodeFormat.QR_CODE, screenWidth, screenHeight);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            GlobalMethods.ExceptionError(newInstance().getContext(), TAG, e);
        }
        return null;
    }

}

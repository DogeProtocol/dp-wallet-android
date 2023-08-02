package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dpwallet.app.R;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.Utility;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QrCodeDialogFragment extends DialogFragment  {

    private static final String TAG = "QrCodeDialogFragment";

    private OnQrCodeDialogCompleteListener mQrCodeDialogListener;

    public static QrCodeDialogFragment newInstance() {
        QrCodeDialogFragment fragment = new QrCodeDialogFragment();
        return fragment;
    }

    public QrCodeDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qr_code_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ImageView qrCodeImageView = (ImageView) getView().findViewById(R.id.imageView_qr_code_dialog_qr_code);
            TextView closeTextView = (TextView) getView().findViewById(R.id.textView_qr_code_dialog_close);

            closeTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getDialog().dismiss();
                    mQrCodeDialogListener.onQrCodeDialogComplete();
                }
            });

            String walletAddress = getArguments().getString("walletAddress");

            Bitmap qrBitMap = generateQrCode(getContext(), walletAddress);
            qrCodeImageView.setImageBitmap(qrBitMap);

            getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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

    public static interface OnQrCodeDialogCompleteListener {
        public abstract void onQrCodeDialogComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mQrCodeDialogListener = (OnQrCodeDialogCompleteListener)context;
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

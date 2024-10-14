package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.dpwallet.app.R;

public class HomeMainFragment extends Fragment  {

    private static final String TAG = "HomeMainFragment";

    private HomeMainFragment.OnHomeMainCompleteListener mHomeMainListener;

    public static HomeMainFragment newInstance() {
        HomeMainFragment fragment = new HomeMainFragment();
        return fragment;
    }

    public HomeMainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            return inflater.inflate(R.layout.home_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHomeMainListener.onHomeMainComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public static interface OnHomeMainCompleteListener {
        public abstract void onHomeMainComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mHomeMainListener = (HomeMainFragment.OnHomeMainCompleteListener)context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

}

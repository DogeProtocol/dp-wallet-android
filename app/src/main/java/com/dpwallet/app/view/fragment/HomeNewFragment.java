package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeNewFragment extends Fragment  {
    private static final String TAG = "HomeNewFragment";

    private int jsonIndex = 0;
    private int quizStepQuizRadioCorrectChoice = -1;
    private JsonViewModel jsonViewModel;

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

        jsonViewModel = new JsonViewModel(getContext(),getArguments().getString("languageKey"));

        LinearLayout homeWelcomeLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_welcome);
        TextView homeWelcomeInfoStepTextView = (TextView) getView().findViewById(R.id.textview_home_welcome_infoStep);
        TextView homeWelcomeInfoStepInfoTileTextView = (TextView) getView().findViewById(R.id.textview_home_welcome_infoStep_info_title);
        TextView homeWelcomeInfoStepInfoDescTextView = (TextView) getView().findViewById(R.id.textview_home_welcome_infoStep_info_desc);
        Button homeWelcomeLangValuesNextButton = (Button) getView().findViewById(R.id.button_home_welcome_langValues_next);


        LinearLayout homeSaftyQuizLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_safety_quiz);
        TextView homeSaftyQuizQuizStepTextView = (TextView) getView().findViewById(R.id.textview_home_safety_quiz_quizStep);
        TextView homeSaftyQuizQuizStepQuizeTitleTextView = (TextView) getView().findViewById(R.id.textview_home_safety_quiz_quizStep_quiz_title);
        TextView homeSaftyQuizQuizStepQuizQuestionTextView = (TextView) getView().findViewById(R.id.textview_home_safety_quiz_quizStep_quiz_question);

        RadioGroup homeSafetyQuizQuizStepQuizRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup_home_safety_quiz_quizStep_quiz);
        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_0 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_0);
        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_1 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_1);
        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_2 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_2);
        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_3 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_3);

        Button homeSaftyQuizLangValuesNextButton = (Button) getView().findViewById(R.id.button_home_safety_quiz_langValues_next);

        homeWelcomeLinearLayout.setVisibility(View.VISIBLE);

        InfoView(homeWelcomeInfoStepTextView,homeWelcomeInfoStepInfoTileTextView,homeWelcomeInfoStepInfoDescTextView,
                homeWelcomeLangValuesNextButton,
                jsonViewModel.getInfoStep(), jsonIndex, jsonViewModel.getInfoLength());

        homeSafetyQuizQuizStepQuizRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                quizStepQuizRadioCorrectChoice = (int) radioButton.getTag();
            }
        });

        homeWelcomeLangValuesNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    //Next
                    if(jsonIndex +1 >= jsonViewModel.getInfoLength()-1){
                        jsonIndex = 0;
                        homeWelcomeLinearLayout.setVisibility(View.GONE);
                        homeSaftyQuizLinearLayout.setVisibility(View.VISIBLE);
                        QuizView(homeSaftyQuizQuizStepTextView,homeSaftyQuizQuizStepQuizeTitleTextView,homeSaftyQuizQuizStepQuizQuestionTextView,
                                homeSaftyQuizLangValuesNextButton,
                                homeSafetyQuizQuizStepQuizChoicesRadioButton_0,homeSafetyQuizQuizStepQuizChoicesRadioButton_1,homeSafetyQuizQuizStepQuizChoicesRadioButton_2,homeSafetyQuizQuizStepQuizChoicesRadioButton_3,
                                jsonViewModel.getQuizStep(), jsonIndex, jsonViewModel.getQuizLength());
                        return;
                    }

                    //View
                    InfoView(homeWelcomeInfoStepTextView,homeWelcomeInfoStepInfoTileTextView,homeWelcomeInfoStepInfoDescTextView,
                            homeWelcomeLangValuesNextButton,
                            jsonViewModel.getInfoStep(), jsonIndex + 1 , jsonViewModel.getInfoLength());

                    jsonIndex = jsonIndex + 1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        homeSaftyQuizLangValuesNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Bundle bundleRoute = new Bundle();
                    String message = "";

                    message = jsonViewModel.getQuizWrongAnswer();
                    if(homeSafetyQuizQuizStepQuizChoicesRadioButton_0.isChecked() == true ||
                            homeSafetyQuizQuizStepQuizChoicesRadioButton_1.isChecked() == true ||
                            homeSafetyQuizQuizStepQuizChoicesRadioButton_2.isChecked() == true ||
                            homeSafetyQuizQuizStepQuizChoicesRadioButton_3.isChecked() == true ) {

                        int correctChoice = jsonViewModel.getCorrectChoiceByQuiz(jsonIndex);

                        if(quizStepQuizRadioCorrectChoice == correctChoice) {
                            AlertDialog dialog = new AlertDialog.Builder(getContext())
                                    .setTitle((CharSequence) "").setView((int)
                                            R.layout.safety_quiz_alert_dialog_fragment).create();


                            dialog.setCancelable(false);
                            dialog.show();

                            TextView homeSaftyQuizAlertDialogMessageTextView = (TextView) dialog.findViewById(R.id.textView_safety_quiz_alert_dialog_message);
                            homeSaftyQuizAlertDialogMessageTextView.setText(jsonViewModel.getAfterQuizInfoByQuiz(jsonIndex));

                            TextView textViewOk = (TextView) dialog.findViewById(
                                    R.id.textView_safety_quiz_alert_dialog_ok);
                            textViewOk.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();

                                    if(jsonIndex +1 >= jsonViewModel.getQuizLength()-1) {
                                        return;
                                    }

                                    QuizView(homeSaftyQuizQuizStepTextView,homeSaftyQuizQuizStepQuizeTitleTextView,homeSaftyQuizQuizStepQuizQuestionTextView,
                                            homeWelcomeLangValuesNextButton,
                                            homeSafetyQuizQuizStepQuizChoicesRadioButton_0,homeSafetyQuizQuizStepQuizChoicesRadioButton_1,homeSafetyQuizQuizStepQuizChoicesRadioButton_2,homeSafetyQuizQuizStepQuizChoicesRadioButton_3,
                                            jsonViewModel.getQuizStep(), jsonIndex + 1, jsonViewModel.getQuizLength());

                                    jsonIndex = jsonIndex + 1;
                                }
                            });
                            return;
                        }

                    } else {
                        message = jsonViewModel.getQuizNoChoice();
                    }


                    bundleRoute.putString("message", message);
                    FragmentManager fragmentManager  = getFragmentManager();
                    MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                    messageDialogFragment.setCancelable(false);
                    messageDialogFragment.setArguments(bundleRoute);
                    messageDialogFragment.show(fragmentManager, "");

                } catch (Exception e) {
                    e.printStackTrace();
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

    public static interface OnHomeNewCompleteListener {
        public abstract void onHomeNewComplete();
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
    private void InfoView(TextView infoStepTextView,TextView infoStepInfoTileTextView,TextView infoStepInfoDescTextView, Button  langValuesNextButton,
                          String infoStep, int index, int length){
        infoStepTextView.setText(infoStep.replace(GlobalMethods.Step, String.valueOf(index +1))
                .replace(GlobalMethods.Total_Steps,String.valueOf(length-1)));

        infoStepInfoTileTextView.setText(jsonViewModel.getTitleByInfo(index));
        infoStepInfoDescTextView.setText(jsonViewModel.getDescByInfo(index));

        langValuesNextButton.setText(jsonViewModel.getNextByLangValues());
    }
    private void QuizView(TextView quizStepTextView, TextView quizStepQuizeTitleTextView, TextView quizStepQuizQuestionTextView, Button  langValuesNextButton,
                       RadioButton quizStepQuizChoicesRadioButton_0, RadioButton quizStepQuizChoicesRadioButton_1, RadioButton quizStepQuizChoicesRadioButton_2,
                      RadioButton quizStepQuizChoicesRadioButton_3, String quizStep, int index, int length){
           quizStepTextView.setText(quizStep.toString().replace(GlobalMethods.Step, String.valueOf(index +1))
                    .replace(GlobalMethods.Total_Steps,String.valueOf(length-1)));

            quizStepQuizeTitleTextView.setText(jsonViewModel.getTitleByQuiz(index));
            quizStepQuizQuestionTextView.setText(jsonViewModel.getQuestionByQuiz(index));

            quizStepQuizChoicesRadioButton_0.setChecked(false);
            quizStepQuizChoicesRadioButton_1.setChecked(false);
            quizStepQuizChoicesRadioButton_2.setChecked(false);
            quizStepQuizChoicesRadioButton_3.setChecked(false);

            quizStepQuizChoicesRadioButton_0.setText(jsonViewModel.getChoicesByQuiz(index).get(0));
            quizStepQuizChoicesRadioButton_1.setText(jsonViewModel.getChoicesByQuiz(index).get(1));
            quizStepQuizChoicesRadioButton_2.setText(jsonViewModel.getChoicesByQuiz(index).get(2));
            quizStepQuizChoicesRadioButton_3.setText(jsonViewModel.getChoicesByQuiz(index).get(3));

            quizStepQuizChoicesRadioButton_0.setTag(1);
            quizStepQuizChoicesRadioButton_1.setTag(2);
            quizStepQuizChoicesRadioButton_2.setTag(3);
            quizStepQuizChoicesRadioButton_3.setTag(4);

            langValuesNextButton.setText(jsonViewModel.getNextByLangValues());
    }
}

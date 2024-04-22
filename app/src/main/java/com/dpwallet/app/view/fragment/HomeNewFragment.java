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

        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_0 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_0);
        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_1 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_1);
        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_2 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_2);
        RadioButton homeSafetyQuizQuizStepQuizChoicesRadioButton_3 = (RadioButton) getView().findViewById(R.id.radioButton_home_safety_quiz_quizStep_quiz_choices_3);

        Button homeSaftyQuizLangValuesNextButton = (Button) getView().findViewById(R.id.button_home_safety_quiz_langValues_next);

        homeWelcomeLinearLayout.setVisibility(View.VISIBLE);

        InfoView(homeWelcomeInfoStepTextView,homeWelcomeInfoStepInfoTileTextView,homeWelcomeInfoStepInfoDescTextView,
                homeWelcomeLangValuesNextButton,
                jsonIndex, jsonViewModel.getInfoStep(), jsonViewModel.getInfo());
        homeWelcomeLangValuesNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    if(jsonIndex +1 >= jsonViewModel.getInfo().length()-1){
                        jsonIndex = 0;
                        homeWelcomeLinearLayout.setVisibility(View.GONE);
                        homeSaftyQuizLinearLayout.setVisibility(View.VISIBLE);
                        QuizView(homeSaftyQuizQuizStepTextView,homeSaftyQuizQuizStepQuizeTitleTextView,homeSaftyQuizQuizStepQuizQuestionTextView,
                                homeSaftyQuizLangValuesNextButton,
                                homeSafetyQuizQuizStepQuizChoicesRadioButton_0,homeSafetyQuizQuizStepQuizChoicesRadioButton_1,homeSafetyQuizQuizStepQuizChoicesRadioButton_2,homeSafetyQuizQuizStepQuizChoicesRadioButton_3,
                                jsonIndex, jsonViewModel.getQuizStep(), jsonViewModel.getQuiz());
                        return;
                    }

                    InfoView(homeWelcomeInfoStepTextView,homeWelcomeInfoStepInfoTileTextView,homeWelcomeInfoStepInfoDescTextView,
                            homeWelcomeLangValuesNextButton,
                            jsonIndex + 1 , jsonViewModel.getInfoStep(), jsonViewModel.getInfo());

                    jsonIndex = jsonIndex + 1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        homeSaftyQuizLangValuesNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    if(jsonIndex + 1 >= jsonViewModel.getQuiz().length()-1){
                        jsonIndex = 0;
                        return;
                    }

                    QuizView(homeSaftyQuizQuizStepTextView,homeSaftyQuizQuizStepQuizeTitleTextView,homeSaftyQuizQuizStepQuizQuestionTextView,
                            homeWelcomeLangValuesNextButton,
                            homeSafetyQuizQuizStepQuizChoicesRadioButton_0,homeSafetyQuizQuizStepQuizChoicesRadioButton_1,homeSafetyQuizQuizStepQuizChoicesRadioButton_2,homeSafetyQuizQuizStepQuizChoicesRadioButton_3,
                            jsonIndex + 1, jsonViewModel.getQuizStep(), jsonViewModel.getQuiz());

                    jsonIndex = jsonIndex + 1;

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
                      int index, String infoStep, JSONArray info){
        try {
            infoStepTextView.setText(infoStep.replace(GlobalMethods.Step, String.valueOf(index +1))
                    .replace(GlobalMethods.Total_Steps,String.valueOf(info.length()-1)));

            final JSONObject i = info.getJSONObject(index);

            infoStepInfoTileTextView.setText(i.getString(GlobalMethods.Title));
            infoStepInfoDescTextView.setText(i.getString(GlobalMethods.Desc));

            langValuesNextButton.setText(jsonViewModel.getLangValues().getString(GlobalMethods.Next));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void QuizView(TextView quizStepTextView, TextView quizStepQuizeTitleTextView, TextView quizStepQuizQuestionTextView, Button  langValuesNextButton,
                       RadioButton quizStepQuizChoicesRadioButton_0, RadioButton quizStepQuizChoicesRadioButton_1, RadioButton quizStepQuizChoicesRadioButton_2,
                      RadioButton quizStepQuizChoicesRadioButton_3, int index,String quizStep, JSONArray quiz){
        try {
           quizStepTextView.setText(quizStep.toString().replace(GlobalMethods.Step, String.valueOf(index +1))
                    .replace(GlobalMethods.Total_Steps,String.valueOf(quiz.length()-1)));

            final JSONObject q = quiz.getJSONObject(index);

            quizStepQuizeTitleTextView.setText(q.getString(GlobalMethods.Title));
            quizStepQuizQuestionTextView.setText(q.getString(GlobalMethods.Question));

            final JSONArray choiceData = q.getJSONArray(GlobalMethods.Choices);

            quizStepQuizChoicesRadioButton_0.setText(choiceData.getString(0));
            quizStepQuizChoicesRadioButton_1.setText(choiceData.getString(1));
            quizStepQuizChoicesRadioButton_2.setText(choiceData.getString(2));
            quizStepQuizChoicesRadioButton_3.setText(choiceData.getString(3));

            langValuesNextButton.setText(jsonViewModel.getLangValues().getString(GlobalMethods.Next));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

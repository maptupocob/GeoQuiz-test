package com.example.maptu.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mResultTextView;
    private TextView mMarkTextView;
    private LinearLayout mAnswerButtonsLayout;
    private LinearLayout mResultLayout;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_america, true),
            new Question(R.string.question_asia, true)
    };

    private String[] answers = new String[mQuestionBank.length];

    private int mCurrentIndex = 0;
    private int mAnswersCount = 0;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_quiz);


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mAnswerButtonsLayout = findViewById(R.id.layout_answer_buttons);
        mResultLayout = findViewById(R.id.result_layout);
        mResultLayout.setVisibility(View.INVISIBLE);
        mQuestionTextView = findViewById(R.id.question_text_view);
        mResultTextView = findViewById(R.id.result_text_view);
        mMarkTextView = findViewById(R.id.mark_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuestion(1);
            }
        });
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuestion(1);
            }
        });
        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuestion(-1);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        Log.d(TAG, mFalseButton.isActivated() ? "activated" : "not activated");
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                updateAnswerButtons();
            }
        });
        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                updateAnswerButtons();
            }
        });
        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private int countCorrect(){
        int i = 0;
        for (String s:answers) {
            if (s.equals(getString(R.string.correct_toast))) i++;
        }
        return i;
    }

    private int mark(){
        return Math.round( 2+3*(float)countCorrect()/(float)mQuestionBank.length);
//        onCreate();
    }

    private void checkAnswer(boolean userPressedTrue) {
        final Toast correctToast = Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);
        final Toast incorrectToast = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
        if (mQuestionBank[mCurrentIndex].isAnswerTrue() ^ userPressedTrue) {
            incorrectToast.show();
            answers[mCurrentIndex] = getString(R.string.incorrect_toast);
        } else {
            correctToast.show();
            answers[mCurrentIndex] = getString(R.string.correct_toast);
        }
        mAnswersCount++;
        if (mAnswersCount == mQuestionBank.length){
            mResultTextView.setText(getString(R.string.result_text) + countCorrect());
            mMarkTextView.setText(getString(R.string.mark_text) + mark());
            mResultLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateAnswerButtons() {
        if (answers[mCurrentIndex] == null) mAnswerButtonsLayout.setVisibility(View.VISIBLE);
        else mAnswerButtonsLayout.setVisibility(View.INVISIBLE);
    }

    private void changeQuestion(int increment) {
        mCurrentIndex = (mQuestionBank.length + mCurrentIndex + increment) % mQuestionBank.length;
        updateQuestion();
        updateAnswerButtons();
    }
}

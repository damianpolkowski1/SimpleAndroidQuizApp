package com.example.android1_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView questionTextView;
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.example.android1_quiz.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown = false;

    private int currentIndex = 0;

    // 0 - correct, 1 - incorrect, 2 - skipped
    int[] result = {0, 0, 0};

    private Question[] questions = new Question[]{
        new Question(R.string.q_biggest_desert, false),
        new Question(R.string.q_longest_river, true),
        new Question(R.string.q_smallest_continent, true),
        new Question(R.string.q_biggest_country_area, true),
        new Question(R.string.q_mount_everest, true),
        new Question(R.string.q_egypt_continent, true),
        new Question(R.string.q_europe_countries, false),
        new Question(R.string.q_deepest_lake, true),
        new Question(R.string.q_biggest_sweet_lake, false)
    };

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;

        if(answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                result[0] += 1;
            } else {
                resultMessageId = R.string.incorrect_answer;
                result[1] += 1;
            }
        }

        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion() {
        if(currentIndex == questions.length) {
            setContentView(R.layout.summary);
            TextView correctTextView = findViewById(R.id.summary_correct_answers);
            TextView incorrectTextView = findViewById(R.id.summary_incorrect_answers);
            TextView skippedTextView = findViewById(R.id.summary_skipped_answers);

            correctTextView.setText("\u2705 Poprawne: " + result[0]);
            incorrectTextView.setText("\u274C Niepoprawne: " + result[1]);
            skippedTextView.setText("\u2753 Pominięte: " + result[2]);

            return;
        }

        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tag", "onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        Button trueButton = findViewById(R.id.true_button);
        Button falseButton = findViewById(R.id.false_button);
        Button nextButton = findViewById(R.id.next_button);
        Button hintButton = findViewById(R.id.hint_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);

                currentIndex += 1;
                setNextQuestion();
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);

                currentIndex += 1;
                setNextQuestion();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result[2] += 1;
                currentIndex += 1;
                answerWasShown = false;
                setNextQuestion();

                Toast.makeText(MainActivity.this, R.string.skipped_question, Toast.LENGTH_SHORT).show();
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });

        setNextQuestion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { return; }
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) { return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("tag", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tag", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tag", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "onDestroy");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("tag", "Wywołana została metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }
}
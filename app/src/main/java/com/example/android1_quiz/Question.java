package com.example.android1_quiz;

public class Question {
    private int questionId;
    private boolean trueAnswer;

    public Question(int questionId, boolean trueAnswer){
        this.questionId = questionId;
        this.trueAnswer = trueAnswer;
    }

    public boolean isTrueAnswer() {
        return this.trueAnswer;
    }

    public int getQuestionId() {
        return this.questionId;
    }
}

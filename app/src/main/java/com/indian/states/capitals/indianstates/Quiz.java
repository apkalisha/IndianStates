package com.indian.states.capitals.indianstates;

public class Quiz {

    //Quiz class
    private String Type;
    private String Question;
    private String Choice1;
    private String Choice2;
    private String Choice3;
    private String Choice4;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getChoice1() {
        return Choice1;
    }

    public void setChoice1(String choice1) {
        Choice1 = choice1;
    }

    public String getChoice2() {
        return Choice2;
    }

    public void setChoice2(String choice2) {
        Choice2 = choice2;
    }

    public String getChoice3() {
        return Choice3;
    }

    public void setChoice3(String choice3) {
        Choice3 = choice3;
    }

    public String getChoice4() {
        return Choice4;
    }

    public void setChoice4(String choice4) {
        Choice4 = choice4;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    private String Answer;
    private String Image;

    public Quiz(){

    }
    public Quiz(String type, String image) {
        Type = type;
        Image = image;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}

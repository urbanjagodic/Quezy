package emp.quezy.quiz;

public class Question {

    private String value, rightAnswer;
    private String[] wrongAnswers;

    Question(String value, String rightAnswer, String [] wrongAnswers) {
        this.value = value;
        this.rightAnswer = rightAnswer;
        this.wrongAnswers = wrongAnswers;
    }

    public String getValue() {
        return value;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public String[] getWrongAnswers() {
        return wrongAnswers;
    }
}

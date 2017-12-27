package emp.quezy.quiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable{

    private String QuestionText, rightAnswer;
    private String[] wrongAnswers;

    Question(String QuestionText, String rightAnswer, String [] wrongAnswers) {
        this.QuestionText = QuestionText;
        this.rightAnswer = rightAnswer;
        this.wrongAnswers = wrongAnswers;
    }

    public String getQuestionText() {
        return QuestionText;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public String[] getWrongAnswers() {
        return wrongAnswers;
    }



    protected Question(Parcel in) {
        QuestionText = in.readString();
        rightAnswer = in.readString();
        wrongAnswers = in.createStringArray();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(QuestionText);
        dest.writeString(rightAnswer);
        dest.writeStringArray(wrongAnswers);
    }
}

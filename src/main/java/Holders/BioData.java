package Holders;

public class BioData
{
    public String question;
    public String answer;

    public String dateLearn;
    public String teacher;

    public BioData(String question, String answer, String dateLearn, String teacher)
    {
        this.question = question;
        this.answer = answer;

        this.dateLearn = dateLearn;
        this.teacher = teacher;
    }

    public BioData(String question, String answer)
    {
        this(question, answer, "", "");
    }
}

package ncollins.magiceightball;

import java.util.concurrent.ThreadLocalRandom;

public class MagicAnswerGenerator {
    private String[] answers;

    public MagicAnswerGenerator(){
        this.answers = Answers.values;
    }

    public String getRandom(){
        return this.answers[ThreadLocalRandom.current().nextInt(0, answers.length)];
    }
}

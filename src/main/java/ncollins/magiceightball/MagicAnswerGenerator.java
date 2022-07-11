package ncollins.magiceightball;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class MagicAnswerGenerator {
    private String[] answers;

    public MagicAnswerGenerator(){
        this.answers = Answers.values;
    }

    public String getRandom(){
        return this.answers[ThreadLocalRandom.current().nextInt(0, answers.length)];
    }
}

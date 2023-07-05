package ncollins.hug;

import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class HugGenerator {
    private String[] phrases;

    public HugGenerator(){
        this.phrases = Phrases.values;
    }

    public String giveHug(String recipient){
        int pIndex = ThreadLocalRandom.current().nextInt(0, phrases.length);

        String phrase = phrases[pIndex].toLowerCase().trim();

        return buildStatement(recipient,phrase);
    }

    private String buildStatement(String recipient, String phrase){
        return phrase.replace("{}", recipient);
    }
}

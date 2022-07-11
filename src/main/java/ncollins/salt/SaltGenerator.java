package ncollins.salt;

import ncollins.salt.words.Adjectives;
import ncollins.salt.words.Nouns;
import ncollins.salt.words.Verbs;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SaltGenerator {
    private String[] nouns;
    private String[] verbs;
    private String[] adjectives;

    public SaltGenerator(){
        this.nouns = Nouns.values;
        this.verbs = Verbs.values;
        this.adjectives = Adjectives.values;
    }

    public String throwSalt(String recipient){
        int nIndex = ThreadLocalRandom.current().nextInt(0, nouns.length);
        int aIndex = ThreadLocalRandom.current().nextInt(0, adjectives.length);
        int vIndex = ThreadLocalRandom.current().nextInt(0, verbs.length);

        String noun = nouns[nIndex].toLowerCase().trim();
        String adjective = adjectives[aIndex].toLowerCase().trim();
        String verb = verbs[vIndex].toLowerCase().trim();

        return buildStatement(recipient,noun,adjective,verb);
    }

    private String buildStatement(String recipient, String noun, String adjective, String verb){
        String statement;
        switch(ThreadLocalRandom.current().nextInt(0,6)){
            case 0: statement = recipient + " is a " + adjective + " " + noun + ".";
                break;
            case 1: statement = recipient + " smells like a " + adjective + " " + noun + ".";
                break;
            case 2: statement = recipient + " looks like a " + adjective + " " + noun + ".";
                break;
            case 3: statement = "i have never seen a " + noun + " as " + adjective + " as " + recipient + ".";
                break;
            case 4: statement = recipient + " is such a " + noun + "!";
                break;
            case 5: statement = "hey " + recipient + ", go " + verb + " a " + noun + "!";
                break;
            default: statement = recipient + " sucks!";
        }

        return statement;
    }
}

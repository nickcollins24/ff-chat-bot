package ncollins.chat.processors;

import ncollins.gif.GifGenerator;
import ncollins.model.chat.ProcessResult;
import ncollins.model.chat.ProcessResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EasterEggProcessor {
    private GifGenerator gifGenerator;

    @Autowired
    public EasterEggProcessor(GifGenerator gifGenerator){
        this.gifGenerator = gifGenerator;
    }

    public ProcessResult processResponse(String text){
        if(text.contains("wonder"))
            return new ProcessResult(ProcessResultType.IMAGE, "https://houseofgeekery.files.wordpress.com/2013/05/tony-wonder-arrested-development-large-msg-132259950538.jpg");
        else if(text.equals("same"))
            return new ProcessResult(ProcessResultType.IMAGE, "https://media1.tenor.com/images/7c981c036a7ac041e66b0c87b42542f2/tenor.gif");
        else if(text.contains("gattaca"))
            return new ProcessResult(ProcessResultType.IMAGE, gifGenerator.search("rafi gattaca"));
        else if(text.matches(".+ de[a]?d$"))
            return new ProcessResult(ProcessResultType.IMAGE, "https://i.groupme.com/498x278.gif.f652fb0c235746b3984a5a4a1a7fbedb.preview");
        else if(text.contains("woof"))
            return new ProcessResult(ProcessResultType.IMAGE, gifGenerator.search("corgi"));
        else if(text.contains("olivia munn"))
            return new ProcessResult(ProcessResultType.IMAGE, gifGenerator.search("olivia munn"));
        else if(text.contains("boobs"))
            return new ProcessResult(ProcessResultType.IMAGE, gifGenerator.search("boobs"));
        else if(text.matches("^.*(hue[ ]*hue)+.*$"))
            return new ProcessResult(ProcessResultType.IMAGE, "https://c.tenor.com/CPmS55KDu60AAAAC/reptile-lol.gif");
        else if(!text.contains(".com") && text.matches(".*6[.+*x/-]?9.*"))
            return new ProcessResult(ProcessResultType.IMAGE, "https://media.giphy.com/media/5xtDaruonEVJXvedMXu/giphy.gif");
        else return new ProcessResult(ProcessResultType.IMAGE, "");
    }
}

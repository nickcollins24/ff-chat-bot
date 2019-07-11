package ncollins.model.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Pin {
    private String text;
    private long creationTime;
    private String user;

    public Pin(String text, String user, long creationTime){
        this.text = text;
        this.user = user;
        this.creationTime = creationTime;
    }

    @Override
    public String toString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
        String date = simpleDateFormat.format(new Date(creationTime));

        StringBuilder sb = new StringBuilder();
        sb.append("''" + text + "''\\n");
        sb.append("-" + this.user + " (" + date + ")");
        return sb.toString();
    }
}

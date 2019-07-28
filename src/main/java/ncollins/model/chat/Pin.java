package ncollins.model.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Pin {
    private String text;
    private long timestamp;
    private String user;

    public Pin(){
        this.setText("");
        this.setUser("");
        this.setTimestamp(0);
    }

    public Pin(String text, String user, long timestamp){
        this.setText(text);
        this.setUser(user);
        this.setTimestamp(timestamp);
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
        String date = simpleDateFormat.format(new Date(getTimestamp()));

        StringBuilder sb = new StringBuilder();
        sb.append("'' " + getText().trim() + " ''\\n");
        sb.append("-" + this.getUser() + " (" + date + ")");
        return sb.toString();
    }
}

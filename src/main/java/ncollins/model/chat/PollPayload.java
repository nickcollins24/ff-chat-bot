package ncollins.model.chat;

public class PollPayload {
    private String subject;
    private String[] options;
    private long expiration;

    public PollPayload(String subject, String[] options, long expiration){
        this.subject = subject;
        this.options = options;
        this.expiration = expiration;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String toString(){
        StringBuilder optionsSb = new StringBuilder();
        optionsSb.append("[");
        for(int i=0; i < options.length; i++){
            optionsSb.append("{\"title\": \"" + options[i] + "\"}");

            if(i < options.length-1)
                optionsSb.append(",");
        }
        optionsSb.append("]");

        return "{" +
                "\"subject\": \"" + this.getSubject() + "\"," +
                "\"options\": " + optionsSb.toString() + "," +
                "\"expiration\": " + this.getExpiration() + "}";
    }
}

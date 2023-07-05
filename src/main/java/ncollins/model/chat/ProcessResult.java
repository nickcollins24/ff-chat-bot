package ncollins.model.chat;

public class ProcessResult {
    private ProcessResultType type;
    private String text;

    public ProcessResult(ProcessResultType type, String text){
        this.type = type;
        this.text = text;
    }

    public ProcessResultType getType() {
        return type;
    }

    public void setType(ProcessResultType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

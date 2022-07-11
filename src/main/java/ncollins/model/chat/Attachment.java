package ncollins.model.chat;

public class Attachment {
    private int[][] charmap;
    private String placeholder;
    private String type;

    public int[][] getCharmap(){
        return this.charmap;
    }

    public void setCharmap(int[][] charmap){
        this.charmap = charmap;
    }

    public String getPlaceholder(){
        return this.placeholder;
    }

    public void setPlaceholder(String placeholder){
        this.placeholder = placeholder;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return "{" + charmap + ", " + placeholder + ", " + type + "}";
    }
}

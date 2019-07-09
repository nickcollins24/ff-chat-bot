package ncollins.model.chat;

import org.apache.commons.lang3.ArrayUtils;

public class ImagePayload {
    private String[] imageUrls;

    public ImagePayload(){
        this.imageUrls = ArrayUtils.EMPTY_STRING_ARRAY;
    }

    public ImagePayload(String imageUrl){
        this.imageUrls = new String[]{imageUrl};
    }

    public ImagePayload(String[] imageUrls){
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String imageUrl : imageUrls){
            sb.append("{\"type\": \"image\",\"url\": \"" + imageUrl + "\"}");
        }
        sb.append("]");

        return sb.toString();
    }
}

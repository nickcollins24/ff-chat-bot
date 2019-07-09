package ncollins.model.chat;

import java.util.Arrays;

public class MentionPayload {
    private int[] userIds;
    private int[][] loci;

    public MentionPayload(int[] userIds, int[][] loci){
        this.userIds = userIds;
        this.loci = loci;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("{\"type\": \"mentions\",\"user_ids\": " + Arrays.toString(userIds) + ",\"loci\": " + Arrays.deepToString(loci) + "}");
        sb.append("]");

        return sb.toString();
    }
}

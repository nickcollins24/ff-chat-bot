package ncollins.helpers;

public class StringHelpers {
    public static Boolean isTrue(String bool){
        if(bool == null) return false;

        return bool.equalsIgnoreCase("true") ||
                bool.equalsIgnoreCase("t");
    }

    public static String mockString(String s){
        char[] sSubstring = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < sSubstring.length; i++){
            sb.append((i+1)%2==0 ? String.valueOf(sSubstring[i]).toLowerCase() : String.valueOf(sSubstring[i]).toUpperCase());
        }

        return sb.toString();
    }
}

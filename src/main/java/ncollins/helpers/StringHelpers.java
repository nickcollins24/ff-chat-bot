package ncollins.helpers;

public class StringHelpers {
    public static Boolean isTrue(String bool){
        if(bool == null) return false;

        return bool.equalsIgnoreCase("true") ||
                bool.equalsIgnoreCase("t");
    }
}

package RSMS.common.helpers;

import java.util.ArrayList;
import java.util.Collections;

public class CollectionHelper {

    public static <T> ArrayList<T> swapBordersAndShuffleBody(ArrayList<T> source) {
        ArrayList<T> result = new ArrayList<T>(source);

        Collections.reverse(result);
        Collections.shuffle(result.subList(1, result.size()-1));

        return result;
    }
}

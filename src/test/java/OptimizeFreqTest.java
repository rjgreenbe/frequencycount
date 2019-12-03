import junit.framework.TestCase;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizeFreqTest extends TestCase {
    private final static int MaxSize = 1000;
    private static String chars = "abcdefghijklmnopqrstuvwxyz";
    private String[] arr;
    String[] pList;

    private void init() {
        arr = new String[MaxSize];
        pList = new String[chars.length()];

        String pp = "";
        for( int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            pList[i] = String.valueOf(c);
        }

        Random rnd = new Random();
        for (int i = 0; i < MaxSize; i++) {
            char c = chars.charAt(rnd.nextInt(chars.length()));
            arr[i] = String.valueOf(c);
            pp += (arr[i] + ",");
        }
        System.out.println(pp);
        Arrays.sort(arr);

    }

    /**
     * Present all known values to the collection
     *
     */
    @Test
    public void test1() {
        init();
        Set<String> sSet = new TreeSet<>();
        sSet.addAll(Arrays.asList(pList));
        int i = 0;
        for( String p : pList) {
            Integer count1 = OptimizedFreq.processRecursive(0, MaxSize - 1, arr, pList[i]);
            Integer count2 = OptimizedFreq.processImperative(arr, pList[i]);
            assertEquals(count1, count2);
        }
    }

    /**
     * Present a value to collection that does not exist, expected to assert against -1
     */
    @Test
    public void test2() {
        init();
        Integer expectedValue = new Integer(-1);
        assertEquals(OptimizedFreq.processRecursive(0, MaxSize - 1, arr, "foo"), expectedValue);
    }
}
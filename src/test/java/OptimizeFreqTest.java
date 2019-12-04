import junit.framework.TestCase;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.junit.jupiter.api.Test;
import java.util.*;

/**
 * Junit for frequency count problem
 */
public class OptimizeFreqTest extends TestCase {
    private final static int MaxSize = 1000;
    private static String chars = "abcdefghijklmnopqrstuvwxyz";
    private String[] arr;
    String[] pList;

    /**
     * Initialization
     */

    private void init() {
        arr = new String[MaxSize];
        pList = new String[chars.length()];

        for( int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            pList[i] = String.valueOf(c);
        }

        Random rnd = new Random();
        for (int i = 0; i < MaxSize; i++) {
            char c = chars.charAt(rnd.nextInt(chars.length()));
            arr[i] = String.valueOf(c);
        }
        Arrays.sort(arr);

    }

    /**
     * Test will present all known values to the collection and test counts
     * that come back from both interative and binary search approaches
     */

    @Test
    public void test1() {
        init();
        Set<String> sSet = new TreeSet<>();
        Map<String, Integer> countMap = new UnifiedMap<>();
        for( String p : pList) {
            if(!sSet.contains(p)) {
                System.out.println("p = " + p);
                Integer count1 = OptimizedFreq.processBinarySearch(0, MaxSize - 1, arr, p);
                Integer count2 = OptimizedFreq.processImperative(0, arr.length - 1, arr, p, countMap);
                assertEquals(count1, count2);
                sSet.add(p);
            }
        }
    }

    /**
     * Present a value to collection that does not exist, expected to assert against -1
     */
    @Test
    public void test2() {
        init();
        Integer expectedValue = new Integer(-1);
        assertEquals(OptimizedFreq.processBinarySearch(0, MaxSize - 1, arr, "foo"), expectedValue);
    }
}
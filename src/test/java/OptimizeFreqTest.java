import junit.framework.TestCase;
import org.eclipse.collections.api.factory.set.MutableSetFactory;
import org.eclipse.collections.api.set.MutableSet;
import org.junit.jupiter.api.Test;
import java.util.*;

public class OptimizeFreqTest extends TestCase {
    private final static int MaxSize = 1000;
    private String chars = "abcdefghijklmnopqrstuvwxyz";
    private String[] arr;
    String[] pList;

    @Test
    public void test1() {
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
        Set<String> sSet = new TreeSet<>();
        sSet.addAll(Arrays.asList(pList));
        int i = 0;
        for( String p : pList) {
            Integer count1 = OptimizedFreq.processRecursive(0, MaxSize - 1, arr, pList[i]);
            Integer count2 = OptimizedFreq.processImperative(arr, pList[i]);
            assertEquals(count1, count2);
        }
    }
}
import junit.framework.TestCase;
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
        int i = 0;
        while(i < chars.length()) {
            char c = chars.charAt(i);
            pList[i++] = String.valueOf(c);
        }
        Random rnd = new Random();
        i = 0;
        while (i < MaxSize) {
            char c = chars.charAt(rnd.nextInt(chars.length()));
            arr[i++] = String.valueOf(c);
        }
        Arrays.sort(arr);
        i = 0;
        Set<String> sSet = new TreeSet<>();
        sSet.addAll(Arrays.asList(pList));
        for( String p : pList) {
            Integer count1 = OptimizedFreq.processRecursive(0, MaxSize - 1, arr, pList[i]);
            Integer count2 = OptimizedFreq.processImperative(arr, pList[i]);
            assertEquals(count1, count2);
        }
    }
}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**
 * R Greenberg. 732.742.4792; count frequency for all array elements in sorted order.
 */
public class OptimizedFreq {
    private static final int MaxSize = 1000;        // Make it a sufficient size 
    private static Logger log = LoggerFactory.getLogger(OptimizedFreq.class);

    public static void main(String... args) {
        try {
            processDataFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private void processDataFile() throws FileNotFoundException {
        File inputFile = new File("data.txt");
        final Scanner scanner = new Scanner(inputFile);

        String nextLine = null;

        // Collect values from data file and store in array

        int i = 0;

        while (scanner.hasNext()) {
            nextLine = scanner.nextLine();
            log.info("computing frequency counts for {}", nextLine);
            String[] arr = nextLine.split(",");
            String[] newArray = new String[arr.length];
            System.arraycopy(arr, 0, newArray, 0, arr.length);
            Arrays.sort(newArray);
            Set<Comparable<? super String>> processed = new HashSet<>();
            processed.clear();
            log.info("**** Starting recursive count computation ****");
            // uses the logic that length( repeating character in a sorted list) = lastIndex - firstIndex + 1)
            for (String val : newArray) {
                if (!processed.contains(val)) {
                    Integer frequencyCount = OptimizedFreq.processRecursive(0, newArray.length - 1, newArray, val);
                    log.info("Frequency of {} is : {}", val, frequencyCount);
                    processed.add(val);
                }
            }

            processed.clear();
            log.info("**** Starting iterative count computation ****");
            for (String val : newArray)
                if (!processed.contains(val)) {
                    log.info("Frequency of {} is : {}", val,
                            OptimizedFreq.processImperative(newArray, val));
                    processed.add(val);
                }
        }
    }

    /*
     This is an O(LogN) comparison time complexity. It uses the fact that each character presented to the array
     to get its frequency count will have its length be its lastIndex - firstIndex + 1.
     */

    public static Integer processRecursive(Integer startIndex, Integer endIndex, String arr[], String element) {
        int lastIndex, firstIndex;
        if (arr[startIndex].equals(element) && arr[endIndex].equals(element)) {
            lastIndex = endIndex;
            firstIndex = startIndex;
        } else {
            lastIndex = OptimizedFreq.findLastIndex(startIndex, endIndex, arr, element);
            firstIndex = OptimizedFreq.findFirstIndex(startIndex, endIndex, arr, element);
        }
        return lastIndex - firstIndex + 1;
    }

    /*
     This is an O(n) function in terms of time complexity - worst case
     is when array has all same element duplicated we have to do a full N comparisons.
     */
    public static Integer processImperative(String arr[], String element) {
        int count = 0;
        for (String s1 : arr) {
            if (s1.equals(element)) count++;
            else if (s1.compareTo(element) > 0) break;
        }
        return count;
    }

    /*
      Binary Search to find first element index in list; O(LogN) time
     */
    private static Integer findFirstIndex(int startIndex, int endIndex, String[] arr, Comparable<String> elem) {
        if (startIndex <= endIndex) {
            int mid = (endIndex + startIndex) / 2;

            // check for either a cardinality of 1 or pivot's prior element < search element and
            // pivot element == search item

            if ((mid == 0 || elem.compareTo(arr[mid - 1]) > 0) && arr[mid].equals(elem)) {
                return mid;
            }
            // if element > pivot element look in latter half of array; otherwise look in
            // first half of array

            else if ((elem.compareTo(arr[mid])) > 0) {
                return findFirstIndex(mid + 1, endIndex, arr, elem);
            } else {
                return findFirstIndex(startIndex, mid - 1, arr, elem);
            }
        }
        return -1;
    }

    /*
     Binary Search to find first last element index in list; O(LogN) time
    */
    private static Integer findLastIndex(int startIndex, int endIndex, String[] arr, Comparable<String> elem) {
        if (startIndex <= endIndex) {
            int mid = (endIndex + startIndex) / 2;

            // check for either a cardinality of 1 or pivot's next element > search element and
            // pivot element == search item

            if ((mid == endIndex || elem.compareTo(arr[mid + 1]) < 0) && arr[mid].equals(elem)) {
                return mid;
            }
            // if element < pivot element look in first half of array; otherwise look in
            // latter half of array
            else if (elem.compareTo(arr[mid]) < 0) {
                return findLastIndex(startIndex, mid - 1, arr, elem);
            } else {
                return findLastIndex(mid + 1, endIndex, arr, elem);
            }
        }
        return -1;
    }
}

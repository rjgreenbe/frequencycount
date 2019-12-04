import org.eclipse.collections.impl.list.mutable.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * R Greenberg. 732.742.4792; count frequency for all array elements in sorted order.
 */
public class OptimizedFreq {
    private static final int MaxSize = 1000;        // Make it a sufficient size 
    private static Logger log = LoggerFactory.getLogger(OptimizedFreq.class);

    public static void main(String... args) {
        String dataFile = "data.txt";
        try {
            processDataFile(dataFile);
        } catch (FileNotFoundException e) {
            log.error("file {} not found" + dataFile);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("Unknown exception caught");
            e.printStackTrace();
        }
    }

    /**
     * @param dataFile
     * @throws FileNotFoundException Input data from file and insert into array for processing
     */
    static private void processDataFile(String dataFile) throws FileNotFoundException {
        File inputFile = new File(dataFile);
        final Scanner scanner = new Scanner(inputFile);
        // Collect values from data file and store in array
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            log.info("computing frequency counts for {}", nextLine);
            String[] arr = nextLine.split(",");

            Arrays.sort(arr);
            List<Comparable<? super String>> processed = FastList.newList();
            processed.clear();
            log.info("**** Starting recursive count computation ****");
            // uses the logic that length( repeating character in a sorted list) = lastIndex - firstIndex + 1)
            for (String val : arr) {
                if (!processed.contains(val)) {
                    Integer frequencyCount = OptimizedFreq.processBinarySearch(0, arr.length - 1, arr, val);
                    log.info("Frequency of {} is : {}", val, frequencyCount);
                    processed.add(val);
                }
            }
            processed.clear();
            log.info("**** Starting iterative count computation ****");
            for (String val : arr)
                if (!processed.contains(val)) {
                    log.info("Frequency of {} is : {}", val,
                            OptimizedFreq.processImperative(0, arr.length - 1, arr, val));
                    processed.add(val);
                }
        }
    }

    /**
     * @param startIndex
     * @param endIndex
     * @param arr
     * @param element
     * @return frequency count of element
     * <p>
     * This is an O(LogN) comparison time complexity. It uses the fact that each character presented to the array
     * to get its frequency count will have its length be its lastIndex - firstIndex + 1.
     */

    public static Integer processBinarySearch(Integer startIndex, Integer endIndex, String arr[], String element) {
        int lastIndex, firstIndex;

        // Optimization: If entire array segment is one element, we can just take the
        // difference of the indices to get the count.

        if (arr[startIndex].equals(element) && arr[endIndex].equals(element)) {
            lastIndex = endIndex;
            firstIndex = startIndex;
        } else {
            lastIndex = OptimizedFreq.findLastIndex(startIndex, endIndex, arr, element);
            firstIndex = OptimizedFreq.findFirstIndex(startIndex, endIndex, arr, element);
        }

        return lastIndex != -1 ? lastIndex - firstIndex + 1 : -1;
    }

    /**
     * @param startIndex
     * @param endIndex
     * @param arr
     * @param element
     * @return This is an O(n) function in terms of time complexity - worst case
     * is when array has all same element duplicated we have to do a full N comparisons.
     */

    public static Integer processImperative(int startIndex, int endIndex, String arr[], String element) {
        int count = 0;

        // Optimization: If entire array segment is one element, we can just take the
        // difference of the indices to get the count.

        if (arr[startIndex].equals(element) && arr[endIndex].equals(element)) {
            count = endIndex - startIndex + 1;
        } else
            for (Comparable<String> s1 : arr) {
                if (s1.equals(element)) count++;
                // if we are traversing elements that are lexicographically greater then the element
                // we are searching for, because the array is sorted, we will no longer encounter
                // that element, and we can stop iterating through loop.
                else if (s1.compareTo(element) > 0) break;
             }
        return count;
    }

    /**
     * @param startIndex
     * @param endIndex
     * @param arr
     * @param elem
     * @return first element index in array
     * <p>
     * Binary Search to find first element index in array; ~O(LogN) time complexity
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

    /**
     * @param startIndex
     * @param endIndex
     * @param arr
     * @param elem
     * @return first element index in array
     * <p>
     * Binary Search to find last element index in array; ~O(LogN) time complexity
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

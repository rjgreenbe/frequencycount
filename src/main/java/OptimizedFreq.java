import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.bag.sorted.MutableSortedBag;
import org.eclipse.collections.api.bimap.MutableBiMap;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function0;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.Function3;
import org.eclipse.collections.api.block.function.primitive.*;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.Procedure2;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.collection.primitive.*;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.MutableMapIterable;
import org.eclipse.collections.api.map.primitive.MutableObjectDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableObjectLongMap;
import org.eclipse.collections.api.map.sorted.MutableSortedMap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.partition.set.PartitionMutableSet;
import org.eclipse.collections.api.set.*;
import org.eclipse.collections.api.set.primitive.*;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.api.tuple.Twin;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;

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
     *
     * @param dataFile
     * @throws FileNotFoundException
     *
     * Input data from file and insert into array for processing
     */
    static private void processDataFile(String dataFile) throws FileNotFoundException {
        File inputFile = new File(dataFile);
        final Scanner scanner = new Scanner(inputFile);
        // Collect values from data file and store in array
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            log.info("computing frequency counts for {}", nextLine);
            String[] arr = nextLine.split(",");
            String[] newArray = new String[arr.length];
            System.arraycopy(arr, 0, newArray, 0, arr.length);
            Arrays.sort(newArray);
            List<Comparable<? super String>> processed = FastList.newList();
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

    /**
     *
     * @param startIndex
     * @param endIndex
     * @param arr
     * @param element
     * @return frequency count of element
     *
     * This is an O(LogN) comparison time complexity. It uses the fact that each character presented to the array
     * to get its frequency count will have its length be its lastIndex - firstIndex + 1.
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

        return lastIndex != -1 ? lastIndex - firstIndex + 1 : -1;
    }

    /**
     *
     * @param arr
     * @param element
     * @return
     *
     *
     * This is an O(n) function in terms of time complexity - worst case
     * is when array has all same element duplicated we have to do a full N comparisons.
     */
    public static Integer processImperative(String arr[], String element) {
        int count = 0;
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
     *
     * @param startIndex
     * @param endIndex
     * @param arr
     * @param elem
     * @return first element index in array
     *
     *
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
     *
     * @param startIndex
     * @param endIndex
     * @param arr
     * @param elem
     * @return first element index in array
     *
     *
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

package ixs180019;

import java.util.HashSet;
import java.util.Random;

/**
 * Testing for Java's HashSet and Cuckoo Hashing
 * 
 * @author Ayesha Gurnani      ang170003
 * @author Ishan Shah     ixs180019
 * @version 1.0
 * @since 2020-03-20
 */
public class CuckooDriver {
    /**
     * Main method to test add, remove and contains on millions of input in Hashset and Cuckoo hashing
     * @param args No of keys, load factor and no of trials
     */
    public static void main(String[] args){

        int keys = 1000000; // No of keys
        if (args.length > 1) {
            keys = Integer.parseInt(args[0]);
        }

        float lf = 0.5f; //Set load factor
        if (args.length > 2) {
            lf = Float.parseFloat(args[1]);
        }

        int trials = 3; //No of trials
        if (args.length > 0) {
            trials = Integer.parseInt(args[2]);
        }
        
        System.out.println("Trails: \n" + trials + " keys: \n" + keys + " Load Factor: " + lf);

        Random random = new Random(); // to generate random numbers
        Integer[] arr = new Integer[keys]; // Array to store numbers
        Cuckoo<Integer> cuckoo;
        HashSet<Integer> hashSet;

        for (int x = 0; x < trials; x++) {
            System.out.println("\nTRIAL: " + x);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = random.nextInt(keys);
            }

            // Test for Cuckoo Hash
            System.out.println("Cuckoo Hashing Performance:");
            Timer timer = new Timer();
            cuckoo = new Cuckoo<Integer>(lf);
            for(int i = 0; i < arr.length; i++){
                cuckoo.add(arr[i]);
                cuckoo.contains(arr[i]);
            }
            for(int i = 0; i < arr.length; i++){
                cuckoo.remove(arr[i]);
            }
            timer.end();
            System.out.println(timer);

            // Test for Java HashSet
            System.out.println("Java HashSet Performance:");
            timer = new Timer();
            hashSet = new HashSet<>(1024, lf);
            for(int i = 0; i < arr.length; i++){
                hashSet.add(arr[i]);
                hashSet.contains(arr[i]);
            }
            for(int i = 0; i < arr.length; i++){
                hashSet.remove(arr[i]);
            }
            timer.end();
            System.out.println(timer);
        }
    }
}

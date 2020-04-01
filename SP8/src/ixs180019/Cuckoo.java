package ixs180019; /**
 * Binary Search Tree
 * This program implements Binary Search Tree
 * It has different operations like contains(), add(), remove(), printTable(), rehash()
 * @author Ayesha Gurnani      ang170003
 * @author Ishan Shah     ixs180019
 * @version 1.0
 * @since 2020-03-30
 */

import java.util.HashSet;
import java.util.Iterator;


public class Cuckoo<T> {

    // To marks elements in the table
    enum Status {
        FILLED, DELETED;
    }

    /**
     * Class for entry to be added to table
     * */
    class Entry<E> {
        E element;
        Status status;

        /***
         * Default constructor for entry class
         */
        public Entry(E element) {
            this.element = element;
            this.status = Status.FILLED;
        }
    }

    int capacity; // capacity of tables
    int size;    //	Number elements in hashtable1
    int hashTables;    //	Number of hash tables
    Entry<T>[][] hashTable; // 2D array representing hashtable
    HashSet<Entry> secondaryTable; // Secondary HashTable
    float loadFactor;
    int threshold;

    /**
     * Default Constructor
     */
    public Cuckoo(float lf) {
        size = 0;
        hashTables = 2;
        capacity = 10;
        loadFactor = (float) lf;
        hashTable = new Entry[capacity][hashTables];
        secondaryTable = new HashSet();
        threshold = (int) Math.log(capacity*2);
    }


    /**
     * Code extracted from Java’s HashMap:
     * @return updated hashcode
     * @param h
     */
    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns index for the key
     * @return index
     * @param h keys to be indexed
     * @param length length of the table
     */
    static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    /**
     * Calculates the location at which key needs to be placed
     * @return calulcated location
     * @param i the table in which you want to add the key
     * @param x the key you want to add
     */
    private int hashFunction(int i, T x) {
        switch (i) {
            case 0:
                return indexFor(hash(x.hashCode()), capacity);        //	Hash Function 1
            default:
                return x.hashCode() % capacity ;    //	Hash Function 2
        }
    }

    /**
     * Adds the key to the table
     * @return boolean, true if key is added else false
     * @param x key to be added
     */
    public boolean add(T x) {

        if (contains(x))
            return false;

        if ( loadFactor < (size)/ (double)(capacity*2))  {
            rehash();
        }

        for (int i = 0; i < hashTables; i++) {
            int loc = hashFunction(i, x);
            if (hashTable[loc][i]== null) {
                hashTable[loc][i] = new Entry<T>(x);
                size++;
                return true;
            } else if (hashTable[loc][i].status == Status.DELETED) {
                hashTable[loc][i] = new Entry<T>(x);
                size++;
                return true;
            }
        }

        int i = 0, count = 0;
        while (count++ <= threshold) {
            int loc = hashFunction(i, x);
            if (hashTable[loc][i] != null) {
                if (hashTable[loc][i].status == Status.DELETED) {
                    hashTable[loc][i].element = (T) new Entry<T>(x);
                    size++;
                    return true;
                } else {
                    T old = (T) hashTable[loc][i].element;
                    hashTable[loc][i].element = x;
                    x = old;
                }
                i = i == hashTables-1 ? 0 : (i + 1);
            }
        }

        secondaryTable.add(new Entry<T>(x));
        return true;
    }

    /**
     * Checks if the table contains the given key
     * @return boolean, true if table contains the key else false
     * @param x key to be searched
     */
    public boolean contains(T x) {
        for (int i = 0; i < hashTables; i++) {
            int location = hashFunction(i, x);

            if (hashTable[location][i] == null)
                return false;
            if (hashTable[location][i].status == Status.DELETED)
                return false;
            if(secondaryTable.contains(x))
                return true;
            if (hashTable[location][i].element == x)
                return true;

        }
        return false;
    }

    /**
     * Removes key form the table
     * @return T, key that is removed
     * @param x key to be removed
     */
    public T remove(T x) {

        if (contains(x)) {
            T e = null;
            for (int i = 0; i < hashTables; i++) {
                int location = hashFunction(i, x);
                if (hashTable[location][i] != null) {
                    if (hashTable[location][i].element == x) {
                        e = (T) hashTable[location][i].element;
                        hashTable[location][i].status = Status.DELETED;
                        size--;
                        return e;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Rehashes the table by doubling the size and adding all elements again
     * @return
     * @param
     */
    private void rehash() {
        Entry[] temp_arr = new Entry[size];
        for (int j = 0, k = 0; j < hashTables; j++) {
            for (int i = 0; i < capacity; i++) {
                if (hashTable[i][j] != null && hashTable[i][j].status == Status.FILLED) {
                    temp_arr[k] = hashTable[i][j];
                    hashTable[i][j] = null;
                    k++;
                }
            }
        }

        capacity = 2 * capacity;
        size = 0;
        hashTable = new Entry[capacity][hashTables];
        threshold = (int) Math.log(capacity);

        for (int i = 0; i < temp_arr.length; i++) {
            add((T) temp_arr[i].element);
        }
    }

    /**
     * Prints the contents of table
     * @return
     * @param
     */
    public void printTable() {
        System.out.println("\n\nHash Table: " + "[" + size + "] " );
        for (int i = 0; i < capacity; i++) {
            for (int j = 0; j < hashTables; j++) {

                if (hashTable[i][j] == null || hashTable[i][j].status == Status.DELETED)
                    System.out.println("" + i + " -> " + "null");
                else
                    System.out.println("" + i + " -> " + hashTable[i][j].element);
            }
            System.out.println();
        }

        System.out.println("\n\nSecondary Table: " + "[" + secondaryTable.size() + "] ");
        Iterator<Entry> iter = secondaryTable.iterator();
        while (iter.hasNext())
            System.out.println(iter.next().element);
    }
}
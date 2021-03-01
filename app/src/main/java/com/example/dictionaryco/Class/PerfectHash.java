package com.example.dictionaryco.Class;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import java.util.Collections;
import java.util.Comparator;

public class PerfectHash {

    private ArrayList<ArrayList<String>> buckets;
    private HashMap<Integer, Integer> gValues;
    private HashMap<Integer, Integer> gValuesv2;
    private HashMap<Integer, String> values;
    private HashMap<Integer, String> valuesv2;
    private Map<String, String> dictionary;
    private Integer d_size;

    public PerfectHash() {

    }

    public PerfectHash(Map<String, String> dictionary) {
        this.dictionary = dictionary;
        this.gValues = new HashMap<Integer, Integer>(dictionary.size() + 1);
        this.gValuesv2 = new HashMap<Integer, Integer>(dictionary.size() + 1);
        this.values = new HashMap<Integer, String>(dictionary.size() * 4 + 1);
        this.valuesv2 = new HashMap<Integer, String>(dictionary.size() * 4 + 1);
        this.buckets = new ArrayList<ArrayList<String>>(dictionary.size() + 1);
        this.d_size = dictionary.size() + 1;
    }

    private Integer Hash(Integer d, String key, Integer M) {

        if (d == 0) {
            d = 0x01000193;
        }
        for (char c : key.toCharArray()) {
            d = (((((d * 0x01000193) % M) ^ Character.getNumericValue(c)) % M) & 0xffffffff) % M;
        }

        return Math.abs(d);
    }

    private void initiateBuckets() {

        Integer sz = d_size;

        for (Integer i = 0; i < sz; i++) {
            ArrayList<String> a = new ArrayList<String>();
            buckets.add(a);
        }
        for (Map.Entry<String, String> pair : dictionary.entrySet()) {
            buckets.get(Hash(0, pair.getKey(), sz)).add(pair.getKey());
        }
        sz = d_size * 3;
        for (Integer i = 0; i < sz; i++) {
            values.put(i, null);
        }
    }

    class ListComparator implements Comparator<ArrayList<String>> {

        // Function to compare
        public int compare(ArrayList<String> c1, ArrayList<String> c2) {

            if (c1.size() < c2.size()) {
                return 1;
            } else if (c1.size() > c2.size()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public void CreateMinimalPerfectHash() {
        Integer sz = d_size;
        HashMap<Integer, Boolean> slotsselected = new HashMap<Integer, Boolean>();
        initiateBuckets();

        Collections.sort(buckets, new ListComparator());

        Integer lengthOne = 0;

        for (Integer i = 0; i < sz; i++) {
            ArrayList<String> bucket = buckets.get(i);
            if (bucket.size() <= 1) {
                break;
            }
            Integer d = 1;
            Integer items = 0;
            ArrayList<Integer> slots = new ArrayList<Integer>();

            while (items < bucket.size()) {
                Integer slot = Hash(d, bucket.get(items), (sz * 3));

                if (values.get(slot) != null || slots.contains(slot)) {
                    items = 0;
                    slots.clear();
                    d++;
                } else {
                    items++;
                    slots.add(slot);
                }
            }

            gValues.put(Hash(0, bucket.get(0), sz), d);

            for (Integer j = 0; j < bucket.size(); j++) {
                values.put(slots.get(j), dictionary.get(bucket.get(j)));
                slotsselected.put(slots.get(j), true);
            }
            lengthOne = i;
        }

        // buckets.forEach(k ->{ System.out.println(k.size());});

        ArrayList<Integer> freeslot = new ArrayList<Integer>();
        for (Integer i = 0; i < (sz * 3); i++) {
            if (values.get(i) == null) {
                freeslot.add(i);
            }
        }

        for (Integer i = lengthOne + 1; i < sz; i++) {
            ArrayList<String> bucket = buckets.get(i);
            if (bucket.size() == 0) {
                break;
            }
            gValues.put(Hash(0, bucket.get(0), sz), -freeslot.get(i) - 1);
            values.put(freeslot.get(i), dictionary.get(bucket.get(0)));
        }

    }

    public String Lookup(String key) {
        if(gValues.get(Hash(0, key, d_size)) == null) return "Sorry, no meaning found for "+key;;
        Integer d = (Integer) gValues.get(Hash(0, key, d_size));
        if (d < 0) {
            if(values.containsKey(-d-1))
            return values.get(-d - 1);
            else return "Sorry, no meaning found for "+key;
        }
        if(values.containsKey(Hash(d, key, (d_size * 3))))
            return values.get(Hash(d, key, (d_size * 3)));
        else return "Sorry, no meaning found for "+key;

    }



    public void toPrintDictionary() {
        System.out.println(dictionary);
    }

    public void toPrintGvalues() {
        System.out.println(gValues);
    }

    public void toPrintValues() throws IOException {
        System.out.println(values);
    }



}

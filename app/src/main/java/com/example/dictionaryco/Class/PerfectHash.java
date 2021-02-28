package com.example.dictionaryco.Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;


public class PerfectHash{

    private ArrayList<ArrayList<String>> buckets;
    private HashMap<Integer,Integer> gValues;
    private HashMap<Integer,String> values;
    private Map<String, String> dictionary ;
    private Integer d_size;

    public PerfectHash(){

    }

    public PerfectHash( Map<String, String> dictionary){
        this.dictionary = dictionary;
        this.gValues = new HashMap<Integer,Integer>(dictionary.size()+1);
        this.values = new HashMap<Integer,String>(dictionary.size()*dictionary.size() + 1);
        this.buckets = new ArrayList<ArrayList<String>>(dictionary.size()+ 1);
        this.d_size = dictionary.size()+1;
    }

    private Integer Hash( Integer d, String key){

        if(d ==0){
            d = 0x01000193;
        }
        for(char c: key.toCharArray()){
            d = ( (d * 0x01000193) ^ Character.getNumericValue(c) ) & 0xffffffff;
        }

        return d;
    }

    private void initiateBuckets(){

        Integer sz = d_size;
        for (Map.Entry<String, String> pair : dictionary.entrySet()) {
            buckets.get( Hash( 0 , pair.getKey()) % sz ).add(pair.getKey());
        }
        sz = d_size*d_size;
        for( Integer i = 0; i<sz; i++){
            values.put(i, null);
        }
    }

    public void CreateMinimalPerfectHash(){
        Integer sz = d_size;
        HashMap<Integer,Boolean> slotsselected = new HashMap<Integer, Boolean>();
        initiateBuckets();

        Collections.sort(buckets, Collections.reverseOrder());

        for( Integer i = 0; i < sz; i++){
            ArrayList<String> bucket = buckets.get(i);
            if(bucket.size() <= 1){
                break;
            }
            Integer d = 1;
            Integer items = 0;
            ArrayList<Integer> slots = new ArrayList<Integer>();

            while( items < bucket.size()){
                Integer slot = Hash( d , bucket.get(items)) % (sz*sz);
                if( values.containsKey(slot) || slots.contains(slot)){
                    items = 0;
                    slots.clear();
                    d++;
                }
                else{
                    items++;
                    slots.add(slot);
                }
            }

            gValues.put(Hash(0,bucket.get(0)) % sz, d);
            for(Integer j=0; j< bucket.size(); j++){
                values.put(slots.get(j),dictionary.get(bucket.get(j)));
                slotsselected.put(slots.get(j), true);
            }
        }

        ArrayList<Integer> freeslot = new ArrayList<Integer>();
        for(Integer i = 0; i< (sz*sz);i++){
            if(!values.containsKey(i)){
                freeslot.add(i);
            }
        }

        Integer free=0;
        for(Integer i = 0; i < sz; i++ ){
            ArrayList<String> bucket = buckets.get(i);
            if(bucket.size()==0){
                break;
            }
            gValues.put(Hash(0,bucket.get(0)) % sz, -freeslot.get(free)-1);
            values.put(freeslot.get(free),dictionary.get(bucket.get(i)));
            free++;
        }


    }

    public String Lookup(String key){
        Integer d = Hash(0,key)% d_size;
        if(d < 0){
            return values.get(-d-1);
        }
        return values.get(Hash(d,key)%(d_size*d_size));
    }


}


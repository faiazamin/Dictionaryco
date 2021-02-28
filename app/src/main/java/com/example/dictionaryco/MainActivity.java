package com.example.dictionaryco;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionaryco.Class.PerfectHash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    PerfectHash perfectHash ;
    HashMap<String,String> dictionary;
    File file ;
    TextView textView;
    String jString;
    JSONObject reader;
    JSONArray wordList;



    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //file = new File(this.getFilesDir(),FILE_NAME);
        textView =  (TextView) findViewById(R.id.main_text);
        jString = loadJSONFromAsset();

        try {
            reader = new JSONObject();
            wordList =  (JSONArray) reader.getJSONArray("dictionary");
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            for(int i=0; i < wordList.length(); i++){
                JSONObject jsonObject = wordList.getJSONObject(i);
                String English = jsonObject.get("en").toString().toUpperCase();
                String Bangla = jsonObject.get("bn").toString().toUpperCase();

                dictionary.put(English, Bangla);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView.setText(wordList.length());





    }

    public String loadJSONFromAsset() {
        String jString = "";
        try {
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(
                            getAssets().open("sample.json")
                    )
            );
            String aDataRow="";
            while ((aDataRow = myReader.readLine())!= null){
                jString += aDataRow + "\n";
            }


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jString;

    }
}
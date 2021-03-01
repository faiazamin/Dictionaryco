package com.example.dictionaryco;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    // declare variables
    PerfectHash perfectHash ;
    HashMap<String,String> dictionary;
    File file ;
    TextView textView;
    EditText editText;
    Button searchButton;
    String jString;
    JSONObject reader;
    JSONArray wordList;



    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize vaeiable
        dictionary = new HashMap<String, String>();
        textView =  (TextView) findViewById(R.id.meaning_display_text);
        editText = (EditText) findViewById(R.id.edit_text_english);
        searchButton = (Button) findViewById(R.id.button_search);

        // Load string from json file
        jString = loadJSONFromAsset();

        // Load dictionary from string
        loadDictionary(jString);
        // initialize hash function
        initHash();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String englishWord = editText.getText().toString().toUpperCase();

                // Look for the english word and then get the corresponding bangla word.
                String banglaWord = perfectHash.Lookup(englishWord.trim());
                //show bangla word
                textView.setText(banglaWord);
            }
        });





    }

    private void initHash(){
        perfectHash = new PerfectHash(dictionary);
        perfectHash.CreateMinimalPerfectHash();
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

    private void loadDictionary ( String jString){
        try {
            reader = new JSONObject(jString);
            wordList =  (JSONArray) reader.getJSONArray("dictionary");
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            for(int i=0; i < wordList.length(); i++){
                JSONObject jsonObject = wordList.getJSONObject(i);
                String English = jsonObject.get("en").toString().toUpperCase();
                String Bangla = jsonObject.get("bn").toString().toUpperCase();

                //Toast.makeText(this, English+" "+Bangla, Toast.LENGTH_SHORT).show();

                dictionary.put(English, Bangla);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
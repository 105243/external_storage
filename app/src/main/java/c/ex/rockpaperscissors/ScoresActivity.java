package c.ex.rockpaperscissors;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class ScoresActivity extends AppCompatActivity {


    public String nameScore[];
    TextView name1, name2, name3, score1, score2, score3;
    final String SD_DIR = "File";
    String nameAndScore, user, temp, data;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);

        nameScore = new String[3];
        nameAndScore = MainActivity.getUserName() + "_" + GameActivity.getUserScore();

        if (canReadAndWrite()) {

            if (scoreExists(1)) useData(1, false);
            if (scoreExists(2)) useData(2, false);
            if (scoreExists(3)) useData(3, false);


            if (GameActivity.getUserScore() > 0) {


                if (name1.getText().toString().matches("User name") || GameActivity.getUserScore() > Integer.parseInt(getSavedData(1, 1))) {


                    if (scoreExists(2)) {

                        saveData(3, getSavedData(2, 2));
                        useData(3, false);
                        saveData(2, getSavedData(1, 2));
                        useData(2, false);

                    } else if (scoreExists(1)) {
                        saveData(2, getSavedData(1, 2));
                        useData(2, false);
                    }

                    saveData(1, MainActivity.getUserName() + "_" + GameActivity.getUserScore());
                    useData(1, true);

                } else if (name2.getText().toString().matches("User name") || GameActivity.getUserScore() > Integer.parseInt(getSavedData(2, 1))) {

                    if (scoreExists(3)) {
                        saveData(3, getSavedData(2, 2));
                        useData(3, false);
                    }

                    saveData(2, MainActivity.getUserName() + "_" + GameActivity.getUserScore());
                    useData(2, false);

                } else if (name3.getText().toString().matches("User name") || GameActivity.getUserScore() > Integer.parseInt(getSavedData(3, 1))) {

                    saveData(3, MainActivity.getUserName() + "_" + GameActivity.getUserScore());
                    useData(3, false);
                }
            }
            } else {
                Toast.makeText(this, "Can't save any score", Toast.LENGTH_SHORT).show();
            }
        }

    public void saveData(int placeToSave, String data) {
        nameAndScore = MainActivity.getUserName() + "_" + GameActivity.getUserScore();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                File myFile = new File(Environment.getExternalStorageDirectory(), SD_DIR);
                FileOutputStream fOut = new FileOutputStream(myFile, true);
                String write = placeToSave + "_" + data + "\r\n";
                fOut.write(write.getBytes());
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSavedData(int place, int dataQty) {
        temp = "";
        try {
            File myFile = new File(Environment.getExternalStorageDirectory(), SD_DIR);
            FileInputStream fIn = new FileInputStream(myFile);
            InputStreamReader in = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(in);

            while ((user = br.readLine()) != null) {
                n = Integer.parseInt(user.substring(0,1));
                if (place == n) {
                    temp = user;
                    nameScore = temp.split("_");
                    if (dataQty == 1) temp = nameScore[2];
                    if (dataQty == 2) temp = nameScore[1] + "_" + nameScore[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public boolean scoreExists(int place) {
        boolean exists = false;

        try {
            File myFile = new File(Environment.getExternalStorageDirectory(), SD_DIR);
            FileInputStream fIn = new FileInputStream(myFile);
            InputStreamReader in = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(in);

            while ((temp = br.readLine()) != null) {
                n = Integer.parseInt(temp.substring(0,1));
                if (place == n) {
                    exists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }


    public void useData(int place, boolean useCurrentScore) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (!useCurrentScore) {
                temp = "";
                try {
                    File myFile = new File(Environment.getExternalStorageDirectory(), SD_DIR);
                    FileInputStream fIn = new FileInputStream(myFile);
                    InputStreamReader in = new InputStreamReader(fIn);
                    BufferedReader br = new BufferedReader(in);

                    while ((user = br.readLine()) != null) {
                        n = Integer.parseInt(user.substring(0,1));
                        if (place == n) {
                            temp = user;
                            nameScore = temp.split("_");
                        }
                    }
                    if (temp.equals("")) return;

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            } else if (useCurrentScore) {
                nameScore[1] = MainActivity.getUserName();
                nameScore[2] = String.valueOf(GameActivity.getUserScore());
            }

            switch (place) {
                case 1:
                    name1.setText(nameScore[1]);
                    score1.setText(nameScore[2]);
                    break;
                case 2:
                    name2.setText(nameScore[1]);
                    score2.setText(nameScore[2]);
                    break;
                case 3:
                    name3.setText(nameScore[1]);
                    score3.setText(nameScore[2]);
                    break;
            }
        }
    }

    public boolean canReadAndWrite() {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        }

    public void backToMenu(View v) {
        startActivity(new Intent(ScoresActivity.this, GameActivity.class));
    }
}


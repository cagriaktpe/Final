package com.sametcagriaktepe.afinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "Final.txt";
    ArrayList<String> list;
    ListView listView;
    int countPauses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        loadData();

        /* variables */
        Button addButton = findViewById(R.id.addButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button invertButton = findViewById(R.id.invertButton);
        Button clearButton = findViewById(R.id.clearButton);

        /* main code */
        listView = findViewById(R.id.myListView);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), list);
        listView.setAdapter(customBaseAdapter);

        /* button listeners */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editableArg1 = findViewById(R.id.editableArg1);
                String arg1 = editableArg1.getText().toString();
                EditText editableArg2 = findViewById(R.id.editableArg2);
                String arg2 = editableArg2.getText().toString();

                list.add(arg1 + "\n" + arg2);
                CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), list);
                listView.setAdapter(customBaseAdapter);

                saveData();

                closeKeyboard(view);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "";
                for (int i = 0; i < list.size(); i++) {
                    text += list.get(i).replaceAll("\\n",":");
                    text += "\n";
                }

                FileOutputStream fos = null;

                try {
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write(text.getBytes());
                    Toast.makeText(MainActivity.this, "Saved to: " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Error happened, not saved", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error happened, not saved", Toast.LENGTH_LONG).show();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        });

        invertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editableArg1 = findViewById(R.id.editableArg1);
                String arg1 = editableArg1.getText().toString();
                EditText editableArg2 = findViewById(R.id.editableArg2);
                String arg2 = editableArg2.getText().toString();

                StringBuilder input1 = new StringBuilder();
                input1.append(arg1 + " " + arg2);
                input1.reverse();

                String text = input1.toString();

                Intent intent = new Intent(MainActivity.this, RevertedTextActivity.class);
                intent.putExtra("keyname", text);
                startActivity(intent);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.removeAll(list);
                CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), list);
                listView.setAdapter(customBaseAdapter);
                saveData();
            }
        });

        /* handling click events in listview */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        list.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        countPauses++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
        builder.setContentTitle("Count of pauses");
        builder.setContentText(Integer.toString(countPauses));
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(1, builder.build());

    }

    void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("list", json);
        editor.apply();
    }

    void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        list = gson.fromJson(json, type);

        if (list == null) {
            list = new ArrayList<>();
        }
    }

    /* methods */
    void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
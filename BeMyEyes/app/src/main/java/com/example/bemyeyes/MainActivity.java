package com.example.bemyeyes;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
TextView textView = findViewById(R.id.textView);
    File file;
    String filename;
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    IntentFilter rec;
    BroadcastReceiver myrec;
    int temp = 0;
    static TextToSpeech tts;
    Vibrator v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                //pic = f;

                startActivityForResult(intent, 1);

            }
        });

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    Log.e("Say it","Setting Language in Main Activity after onResume();");
                    tts.speak("main screen", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
                filename = "Network.ser";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)  {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }

    }
    boolean scheduled = false;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e("Press.TAG","ACTION_DOWN");

                break;

            case MotionEvent.ACTION_UP:
                if(scheduled == false){
                    scheduled = true;
                    Log.e("Press.TAG","Finger lifted. Scheduled = True");
                    Log.e("Press.TAG","Just about to start Test Activity because finger lifted.");
                }
                break;
        }
        return true;
    }
    private void launchApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        // Vibrate for 250 milliseconds
        Log.e("VBR","Vibrating to go out of MainActivity");
        super.onPause();
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "BlindWalker"
        );
        file = new File(
                mediaStorageDir.getPath() + File.separator + filename
        );
        try {
            //Log.e("File Dest",file.getAbsolutePath());
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeInt(temp);
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        v.vibrate(200);
        //NeuralNet.getInstance().saveData(getApplicationContext());
    }
    @Override
    protected void onResume() {
        super.onResume();
        scheduled = false;
        Log.e("VBR","Vibrating in MainActivity");
        v.vibrate(200);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    Log.e("Say it","Setting Language in Main Activity after onResume();");
                    tts.speak("main screen", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        //NeuralNet.setContext(getApplicationContext());
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "BlindWalker"
        );
        file = new File(
                mediaStorageDir.getPath() + File.separator + filename
        );
        try {
            /*if(){
            }else{*/
            file.createNewFile();/*
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                temp = (int)ois.readInt();
                temp++;
                ois.close();*/
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

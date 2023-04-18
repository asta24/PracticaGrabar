package com.example.practicagrabar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder grabacion;
    private String archivoSalida = null;
    private Button btn_grabar;
    Button play_pause, btn_repetir;
    MediaPlayer mp;
    ImageView iv;
    int repetir = 1, posicion = 0;
    MediaPlayer vectormp [] = new MediaPlayer[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_grabar = (Button) findViewById(R.id.btn_grabar);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
        play_pause = (Button) findViewById(R.id.btn_play);
        btn_repetir = (Button) findViewById(R.id.btn_repetir);
        iv = (ImageView) findViewById(R.id.imageView);

        vectormp[0] = MediaPlayer.create(this, R.raw.musica);
        vectormp[1] = MediaPlayer.create(this, R.raw.goatsimulator);
        vectormp[2] = MediaPlayer.create(this, R.raw.shrek);
    }
    public void PlayerPause(View view) {
        if (vectormp[posicion].isPlaying()) {
            vectormp[posicion].pause();
            play_pause.setBackgroundResource(R.drawable.reproducir);
            Toast.makeText(this, "Pausar", Toast.LENGTH_SHORT).show();
        }else{

            vectormp[posicion].start();
            play_pause.setBackgroundResource(R.drawable.pausa);
            Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();

        }
    }
    public void Stop(View view){


        if(vectormp[posicion] !=null){
            vectormp[posicion].stop();
            vectormp[0] = MediaPlayer.create(this, R.raw.musica);
            vectormp[1] = MediaPlayer.create(this, R.raw.goatsimulator);
            vectormp[2] = MediaPlayer.create(this, R.raw.shrek);

            posicion = 0;
            play_pause.setBackgroundResource(R.drawable.reproducir);
            iv.setImageResource(R.drawable.portada1);
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();

        }
    }

    public void Repetir(View view){

        if(repetir >=1){
            btn_repetir.setBackgroundResource(R.drawable.repetir);
            Toast.makeText(this, "No repetir", Toast.LENGTH_SHORT).show();
            vectormp[posicion].setLooping(false);
            repetir = 2;
        }else{
            btn_repetir.setBackgroundResource(R.drawable.no_repetir);
            Toast.makeText(this, "repetir", Toast.LENGTH_SHORT).show();
            vectormp[posicion].setLooping(true);
            repetir = 1;
        }
    }
    public void Siguiente(View view) {
        if (posicion < vectormp.length - 1) {

            if (vectormp[posicion].isPlaying()) {
                vectormp[posicion].stop();
                vectormp[0] = MediaPlayer.create(this, R.raw.musica);
                vectormp[1] = MediaPlayer.create(this, R.raw.goatsimulator);
                vectormp[2] = MediaPlayer.create(this, R.raw.shrek);
                posicion++;
                vectormp[posicion].start();
                if (posicion == 0) {
                    iv.setImageResource(R.drawable.portada1);
                } else if (posicion == 1) {
                    iv.setImageResource(R.drawable.portada2);
                } else if (posicion == 2) {
                    iv.setImageResource(R.drawable.portada3);
                }
            } else {
                posicion++;
                if (posicion == 0) {
                    iv.setImageResource(R.drawable.portada1);
                } else if (posicion == 1) {
                    iv.setImageResource(R.drawable.portada2);
                } else if (posicion == 2) {
                    iv.setImageResource(R.drawable.portada3);
                }
            }

        } else {
            Toast.makeText(this, "No hay más canciones", Toast.LENGTH_SHORT).show();
        }
    }
    public void Anterior(View view){
        if (posicion >= 1){
            if(vectormp[posicion].isPlaying()){
                vectormp[posicion].stop();
                vectormp[0] = MediaPlayer.create(this, R.raw.musica);
                vectormp[1] = MediaPlayer.create(this, R.raw.goatsimulator);
                vectormp[2] = MediaPlayer.create(this, R.raw.shrek);
                posicion--;
                vectormp[posicion].start();
                if(posicion == 0){
                    iv.setImageResource(R.drawable.portada1);
                }else if(posicion == 1){
                    iv.setImageResource(R.drawable.portada2);
                } else if(posicion == 2){
                    iv.setImageResource(R.drawable.portada3);
                }
            }

            else {
                posicion--;
                if(posicion == 0){
                    iv.setImageResource(R.drawable.portada1);
                } else if (posicion == 1) {
                    iv.setImageResource(R.drawable.portada2);
                } else if (posicion == 2) {
                    iv.setImageResource(R.drawable.portada3);
                }


            }


        }else{
            Toast.makeText(this, "No hay más canciones", Toast.LENGTH_SHORT).show();


        }
    }
        public void Recorder(View view){
            if(grabacion == null) {

                File ruta = new File(Environment.getExternalStorageDirectory() + "/DCIM/Grabacion/audios/");
                if(!ruta.exists()){
                    ruta.mkdirs();
                }
                archivoSalida = ruta + "/grabacion.mp3";
                grabacion = new MediaRecorder();
                grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
                grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                grabacion.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                grabacion.setOutputFile(archivoSalida);
                try{
                    grabacion.prepare();
                    grabacion.start();
                }catch(IOException e){}
                btn_grabar.setBackgroundResource(R.drawable.rec);
                Toast.makeText(this, "Grabando....", Toast.LENGTH_SHORT).show();


            }else{
                grabacion.stop();
                grabacion.release();
                grabacion = null;
                btn_grabar.setBackgroundResource(R.drawable.stop_rec);
                Toast.makeText(this, "Grabación finalizada", Toast.LENGTH_SHORT).show();

            }
        }
        public void reproducir(View view){
            MediaPlayer r = new MediaPlayer();
            try{
                r.setDataSource(archivoSalida);
                r.prepare();
            }catch (IOException e){}
            r.start();
            Toast.makeText(this, "Reproduciendo Audio", Toast.LENGTH_SHORT).show();
        }
}

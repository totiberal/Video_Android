package com.example.alberto.video;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.VideoView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnGravar, btnReproducir;
    Spinner spinner;
    String[] nomes;
    static int numero;
    ArrayAdapter<String> adaptador;
    String timeStamp = DateFormat.getDateTimeInstance().format(
            new Date()).replaceAll(":", "").replaceAll("/", "_")
            .replaceAll(" ", "_");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controlVideo();
        btnGravar=(Button) findViewById(R.id.idGravar);
        btnReproducir=(Button) findViewById(R.id.idReroducir);
        spinner=(Spinner) findViewById(R.id.idSpinner);
        File ruta = new File(Environment.getExternalStorageDirectory()+"/VIDEOPROBA/");
        nomes=ruta.list();
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomes);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numero=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamp = DateFormat.getDateTimeInstance().format(
                        new Date()).replaceAll(":", "").replaceAll("/", "_")
                        .replaceAll(" ", "_");
                File ruta = new File(Environment.getExternalStorageDirectory() + "/VIDEOPROBA/");
                if (!ruta.exists()) ruta.mkdirs();
                File arquivo = new File(ruta, timeStamp+".mp4");

                Intent intento = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intento.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivo));

                startActivityForResult(intento, 1);
                adaptador.notifyDataSetChanged();
            }
        });

        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File ruta = new File(Environment.getExternalStorageDirectory()+"/VIDEOPROBA/");
                File arquivo = new File(ruta,nomes[numero]);
                if (!arquivo.exists()) return;

                VideoView videoview = (VideoView) findViewById(R.id.idVideo);
                videoview.setVideoURI(Uri.fromFile(arquivo));
                videoview.start();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                File ruta = new File(Environment.getExternalStorageDirectory()+"/VIDEOPROBA/");
                File arquivo = new File(ruta,timeStamp+".mp4");
                if (!arquivo.exists()) return;
                adaptador.notifyDataSetChanged();
                VideoView videoview = (VideoView) findViewById(R.id.idVideo);
                videoview.setVideoURI(Uri.fromFile(arquivo));
                videoview.start();
            }

        } else if (resultCode == RESULT_CANCELED) {
                // Video ou Foto cancelada
        } else {

        }
    }

    private void controlVideo(){
        MediaController controller = new MediaController(this);
        VideoView videoview = (VideoView)findViewById(R.id.idVideo);
        videoview.setMediaController(controller);

    }


}

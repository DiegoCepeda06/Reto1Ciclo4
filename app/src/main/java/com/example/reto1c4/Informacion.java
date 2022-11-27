package com.example.reto1c4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Informacion extends AppCompatActivity {
    private Button btninfo;
    private TextView textInfoTitle;
    private ImageView imgInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
    btninfo = (Button) findViewById(R.id.btnInfo);
    textInfoTitle = (TextView) findViewById(R.id.textInfoTitle);
    imgInfo = (ImageView) findViewById(R.id.imgInfo);




    Intent intentIn = getIntent();
    textInfoTitle.setText(intentIn.getStringExtra("title"));
    int codeImage = intentIn.getIntExtra("codeImage", 0);
    imgInfo.setImageResource(codeImage);

    btninfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Catalogo.class);
            startActivity(intent);
        }
    });

    }
}
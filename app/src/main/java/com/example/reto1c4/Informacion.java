package com.example.reto1c4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reto1c4.BaseDatos.DBFirebase;
import com.example.reto1c4.BaseDatos.DBHelper;
import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Service.ProductServices;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Informacion extends AppCompatActivity {

    private DBHelper dbHelper;
    private ProductServices productServices;
    private Button btninfo;
    private TextView textInfoTitle;
    private ImageView imgInfo;
    private TextView textInfoDescripcion;
    private TextView textInfoPrecio;
    private Button btnInfoHome;
    private DBFirebase dbFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        btninfo = (Button) findViewById(R.id.btnInfo);
        textInfoTitle = (TextView) findViewById(R.id.textInfoTitle);
        textInfoDescripcion = (TextView) findViewById(R.id.textInfoDescripcion);
        textInfoPrecio = (TextView) findViewById(R.id.textInfoPrecio);
        imgInfo = (ImageView) findViewById(R.id.imgInfo);
        dbHelper = new DBHelper(this);
        dbFirebase = new DBFirebase();

        productServices = new ProductServices();
        btnInfoHome = (Button) findViewById(R.id.btnInfoHome);


        btnInfoHome.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }));

        Intent intentIn = getIntent();


        textInfoTitle.setText(intentIn.getStringExtra("name"));
        textInfoDescripcion.setText(intentIn.getStringExtra("description"));
        textInfoPrecio.setText(String.valueOf(intentIn.getStringExtra("price")));
        productServices.insertUriToImageView(intentIn.getStringExtra("image"),imgInfo,Informacion.this);

        //imgInfo.setImageBitmap(productServices.byteToBitmap(producto.getImage()));


        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Catalogo.class);
                startActivity(intent);
            }
        });

    }
}
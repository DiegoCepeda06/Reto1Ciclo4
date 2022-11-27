package com.example.reto1c4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Catalogo extends AppCompatActivity {
    private Button btnProducto1, btnProducto2, btnProducto3, btnProductoHome;
    private TextView textProducto1, textProducto2, textProducto3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

    btnProducto1 = (Button) findViewById(R.id.btnProducto1);
    btnProducto2 = (Button) findViewById(R.id.btnProducto2);
    btnProducto3 = (Button) findViewById(R.id.btnProducto3);
    btnProductoHome = (Button) findViewById(R.id.btnProductoHome);

    textProducto1 =(TextView) findViewById(R.id.textProducto1);
    textProducto2 =(TextView) findViewById(R.id.textProducto2);
    textProducto3 =(TextView) findViewById(R.id.textProducto3);

    btnProducto1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Informacion.class);
            intent.putExtra("title", textProducto1.getText().toString());
            intent.putExtra("codeImage", R.drawable.producto1);
            startActivity(intent);
        }
    });

    btnProducto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Informacion.class);
                intent.putExtra("title", textProducto2.getText().toString());
                intent.putExtra("codeImage", R.drawable.producto2);
                startActivity(intent);
            }
    });

    btnProducto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Informacion.class);
                intent.putExtra("title", textProducto3.getText().toString());
                intent.putExtra("codeImage", R.drawable.producto3);
                startActivity(intent);
            }
    });
    btnProductoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
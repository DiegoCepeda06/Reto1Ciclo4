package com.example.reto1c4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto1c4.Adapters.ProductoAdapter;
import com.example.reto1c4.BaseDatos.DBFirebase;
import com.example.reto1c4.BaseDatos.DBHelper;
import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Service.ProductServices;

import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {
    private ListView listViewCatalogo;
    private ProductServices productServices;
    private ArrayList<Producto> arrayListProductos;
    private ProductoAdapter productoAdapter;
    private DBHelper dbHelper;
    private DBFirebase dbFirebase;
    private Button btnProductoHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        arrayListProductos = new ArrayList<Producto>();

        btnProductoHome = (Button) findViewById(R.id.btnProductoHome);


        try {
            //dbHelper = new DBHelper(this);
            dbFirebase = new DBFirebase();
            productServices = new ProductServices();
            Cursor cursor = dbHelper.getData();
            arrayListProductos = productServices.cursorToArray(cursor);
            //Toast.makeText(this, "InsertOK", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bienvenido al Catalogo", Toast.LENGTH_SHORT).show();

        }

        productoAdapter = new ProductoAdapter(this, arrayListProductos);
        listViewCatalogo = (ListView) findViewById(R.id.listViewCatalogo);
        listViewCatalogo.setAdapter(productoAdapter);

        dbFirebase.getData(productoAdapter, arrayListProductos);

        btnProductoHome.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAdd:
                Intent intent = new Intent(getApplicationContext(), ProductForm.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
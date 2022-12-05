package com.example.reto1c4.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Informacion;
import com.example.reto1c4.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Producto> arrayListProductos;

    public ProductoAdapter(Context context, ArrayList<Producto> arrayListProductos) {
        this.context = context;
        this.arrayListProductos = arrayListProductos;
    }

    @Override
    public int getCount() {
        return this.arrayListProductos.size();
    }

    @Override
    public Object getItem(int i) {
        return this.arrayListProductos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vista = view;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        vista = layoutInflater.inflate(R.layout.product_template, null);


        Button btnTemplateProducto = (Button) vista.findViewById(R.id.btnTemplateProducto);
        ImageView imgTemplateProducto = (ImageView) vista.findViewById(R.id.imgTemplateProducto);
        TextView textTemplateProducto = (TextView) vista.findViewById(R.id.textTemplateProducto);
        TextView textDescripcionTemplateProducto = (TextView) vista.findViewById(R.id.textDescripcionTemplateProducto);
        TextView textPrecioTemplateProducto = (TextView) vista.findViewById(R.id.textPrecioTemplateProducto);

        Producto producto = arrayListProductos.get(i);
        byte[] image = producto.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        imgTemplateProducto.setImageBitmap(bitmap);
        textTemplateProducto.setText(producto.getName());
        textDescripcionTemplateProducto.setText(producto.getDescription());
        textPrecioTemplateProducto.setText(String.valueOf(producto.getPrice()));

        btnTemplateProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), Informacion.class);

                intent.putExtra("id", String.valueOf(producto.getId()));


                context.startActivity(intent);


            }
        });


        return vista;
    }
}

/*
.setOnKeyListener(new View.OnKeyListener() {
public boolean onKey(View v, int keyCode, KeyEvent event) {
        // If the event is a key-down event on the "enter" button
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
        (keyCode == KeyEvent.KEYCODE_ENTER)) {
        // Perform action on key press
        Toast.makeText(getApplicationContext(), "Hello Enter", Toast.LENGTH_SHORT).show();
        return true;
        }

        return false;
        }
        });
        
 */
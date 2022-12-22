package com.example.reto1c4.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto1c4.BaseDatos.DBFirebase;
import com.example.reto1c4.Catalogo;
import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Informacion;
import com.example.reto1c4.ProductForm;
import com.example.reto1c4.R;
import com.example.reto1c4.Service.ProductServices;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Producto> arrayListProductos;
    private ProductServices productServices;

    public ProductoAdapter(Context context, ArrayList<Producto> arrayListProductos) {
        this.context = context;
        this.arrayListProductos = arrayListProductos;
        this.productServices = new ProductServices();
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

        Button btnEditTemplate = (Button) vista.findViewById(R.id.btnEditTemplate);
        Button btnDeleteTemplate = (Button) vista.findViewById(R.id.btnDeleteTemplate);
        Button btnTemplateProducto = (Button) vista.findViewById(R.id.btnTemplateProducto);
        ImageView imgTemplateProducto = (ImageView) vista.findViewById(R.id.imgTemplateProducto);
        TextView textTemplateProducto = (TextView) vista.findViewById(R.id.textTemplateProducto);
        TextView textDescripcionTemplateProducto = (TextView) vista.findViewById(R.id.textDescripcionTemplateProducto);
        TextView textPrecioTemplateProducto = (TextView) vista.findViewById(R.id.textPrecioTemplateProducto);

        Producto producto = arrayListProductos.get(i);
        //byte[] image = producto.getImage();
        //Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        //imgTemplateProducto.setImageBitmap(bitmap);
        textTemplateProducto.setText(producto.getName());
        textDescripcionTemplateProducto.setText(producto.getDescription());
        textPrecioTemplateProducto.setText(String.valueOf(producto.getPrice()));

        productServices.insertUriToImageView(producto.getImage(), imgTemplateProducto, context );

        btnTemplateProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), Informacion.class);


                intent.putExtra("id", String.valueOf(producto.getId()));
                intent.putExtra("id", String.valueOf(producto.getId()));
                intent.putExtra("name", String.valueOf(producto.getName()));
                intent.putExtra("description", String.valueOf(producto.getDescription()));
                intent.putExtra("price", String.valueOf(producto.getPrice()));
                intent.putExtra("image", String.valueOf(producto.getImage()));


                context.startActivity(intent);


            }
        });

        btnEditTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), ProductForm.class);
                intent.putExtra("edit", true);
                intent.putExtra("id", producto.getId());
                intent.putExtra("name", producto.getName());
                intent.putExtra("description", producto.getDescription());
                intent.putExtra("price", producto.getPrice());
                intent.putExtra("image", producto.getImage());
                intent.putExtra("latitud", producto.getLatitud());
                intent.putExtra("longitud", producto.getLongitud());

                context.startActivity(intent);
            }
        });


        btnDeleteTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Estas seguro que deseas eliminar el producto?")
                        .setTitle("Confirmación")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DBFirebase dbFirebase = new DBFirebase();
                                dbFirebase.deleteData(producto.getId());
                                Intent intent = new Intent(context.getApplicationContext(), Catalogo.class);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }


                        });
                AlertDialog dialog = builder.create();
                dialog.show();
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
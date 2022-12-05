package com.example.reto1c4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto1c4.BaseDatos.DBHelper;
import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Service.ProductServices;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ProductForm extends AppCompatActivity {


    ActivityResultLauncher<String> content;


    private DBHelper dbHelper;
    private Button btnFormProduct, btnGet, btnDelete, btnUpdate;
    private TextView textNameFormProduct, textDescriptionFormProduct, textPriceFormProduct;
    private EditText editNameFormProduct, editDescriptionFormProduct, editPriceFormProduct, editIdFormProduct;
    private ImageView imgFormProduct;
    private ProductServices productServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);


        btnFormProduct = (Button) findViewById(R.id.btnFormProduct);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        textNameFormProduct = (TextView) findViewById(R.id.textNameFormProduct);
        textDescriptionFormProduct = (TextView) findViewById(R.id.textDescriptionFormProduct);
        textPriceFormProduct = (TextView) findViewById(R.id.textPriceFormProduct);

        imgFormProduct = (ImageView) findViewById(R.id.imgFormProduct);

        editNameFormProduct = (EditText) findViewById(R.id.editNameFormProduct);
        editDescriptionFormProduct = (EditText) findViewById(R.id.editDescriptionFormProduct);
        editPriceFormProduct = (EditText) findViewById(R.id.editPriceFormProduct);
        editIdFormProduct = (EditText) findViewById(R.id.editIdFormProduct);

        byte[] img = "".getBytes(StandardCharsets.UTF_8);
        try {
            productServices = new ProductServices();
            dbHelper = new DBHelper(this);
        } catch (Exception e) {
            Log.e("DB", e.toString());
        }


        content = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(result);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imgFormProduct.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );

        imgFormProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.launch("image/*");
            }
        });

        btnFormProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    dbHelper.insertData(
                            editNameFormProduct.getText().toString(),
                            editDescriptionFormProduct.getText().toString(),
                            editPriceFormProduct.getText().toString(),
                            productServices.imageViewToByte(imgFormProduct)
                    );
                } catch (Exception e) {
                    Log.e("DB Insert", e.toString());
                }

                Intent intent = new Intent(getApplicationContext(), Catalogo.class);
                startActivity(intent);

            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if (id.compareTo("") != 0) {


                    ArrayList<Producto> list = productServices.cursorToArray(dbHelper.getDataById(id));
                    if (list.size() != 0) {
                        Producto producto = list.get(0);

                        imgFormProduct.setImageBitmap(productServices.byteToBitmap(producto.getImage()));
                        editNameFormProduct.setText(producto.getName());
                        editDescriptionFormProduct.setText(producto.getDescription());
                        editPriceFormProduct.setText(String.valueOf(producto.getPrice()));
                    } else {
                        Toast.makeText(getApplicationContext(), "No existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese id", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if (id.compareTo("") != 0) {
                    Log.d("DB", id);
                    dbHelper.deleteDataById(id);
                    clean();

                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese id a eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if (id.compareTo("") != 0) {
                    dbHelper.updtadeDataById(
                            id,
                            editNameFormProduct.getText().toString(),
                            editDescriptionFormProduct.getText().toString(),
                            editPriceFormProduct.getText().toString(),
                            productServices.imageViewToByte(imgFormProduct)
                    );
                    clean();
                }

            }
        });

    }

    public void clean() {
        editNameFormProduct.setText("");
        editDescriptionFormProduct.setText("");
        editPriceFormProduct.setText("");
        imgFormProduct.setImageResource(R.drawable.agregrarimg);
    }


}
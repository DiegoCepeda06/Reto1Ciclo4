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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto1c4.BaseDatos.DBFirebase;
import com.example.reto1c4.BaseDatos.DBHelper;
import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Service.ProductServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class ProductForm extends AppCompatActivity {


    ActivityResultLauncher<String> content;


    private DBHelper dbHelper;
    private DBFirebase dbFirebase;
    private StorageReference storageReference;
    private Button btnFormProduct, btnGet, btnDelete, btnUpdate;
    private TextView textNameFormProduct, textDescriptionFormProduct, textPriceFormProduct, textLatitudFormProduct, textLongitudFormProduct;
    private EditText editNameFormProduct, editDescriptionFormProduct, editPriceFormProduct, editIdFormProduct, editLatitudFormProduct, editLongitudFormProduct;
    private ImageView imgFormProduct;
    private ProductServices productServices;
    private MapView map;
    private MapController mapController;
    private String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        productServices = new ProductServices();

        btnFormProduct = (Button) findViewById(R.id.btnFormProduct);


        textNameFormProduct = (TextView) findViewById(R.id.textNameFormProduct);
        textDescriptionFormProduct = (TextView) findViewById(R.id.textDescriptionFormProduct);
        textPriceFormProduct = (TextView) findViewById(R.id.textPriceFormProduct);
        textLatitudFormProduct = (TextView) findViewById(R.id.textLatitudFormProduct);
        textLongitudFormProduct = (TextView) findViewById(R.id.textLongitudFormProduct);

        imgFormProduct = (ImageView) findViewById(R.id.imgFormProduct);
        storageReference = FirebaseStorage.getInstance().getReference();

        editNameFormProduct = (EditText) findViewById(R.id.editNameFormProduct);
        editDescriptionFormProduct = (EditText) findViewById(R.id.editDescriptionFormProduct);
        editPriceFormProduct = (EditText) findViewById(R.id.editPriceFormProduct);
        editLatitudFormProduct = (EditText) findViewById(R.id.editLatitudFormProduct);
        editLongitudFormProduct = (EditText) findViewById(R.id.editLongitudFormProduct);

        Intent intentIN = getIntent();
        Boolean edit = intentIN.getBooleanExtra("edit", false);

        map = (MapView) findViewById(R.id.mapForm);
        map.setBuiltInZoomControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = (MapController) map.getController();
        GeoPoint colombia = new GeoPoint(4.570868, -74.297333);
        mapController.setCenter(colombia);
        mapController.setZoom(12);
        map.setMultiTouchControls(true);

        if (edit) {

            btnFormProduct.setText("Actualizar");
            productServices.insertUriToImageView(intentIN.getStringExtra("image"),imgFormProduct,this);
            editNameFormProduct.setText(intentIN.getStringExtra("name"));
            editDescriptionFormProduct.setText(intentIN.getStringExtra("description"));
            editPriceFormProduct.setText(String.valueOf(intentIN.getIntExtra("price", 0)));
            editLatitudFormProduct.setText(String.valueOf(intentIN.getDoubleExtra("latitud", 0.0)));
            editLongitudFormProduct.setText(String.valueOf(intentIN.getDoubleExtra("longitud", 0.0)));
            GeoPoint geoPoint = new GeoPoint(intentIN.getDoubleExtra("latitud", 0.0), intentIN.getDoubleExtra("longitud", 0.0));
            Marker marker = new Marker(map);
            marker.setPosition(geoPoint);
            map.getOverlays().add(marker);
        }


        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                editLatitudFormProduct.setText(String.valueOf(p.getLatitude()));
                editLongitudFormProduct.setText(String.valueOf(p.getLongitude()));
                Marker marker = new Marker(map);
                marker.setPosition(p);
                marker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(marker);

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, mapEventsReceiver);
        map.getOverlays().add(mapEventsOverlay);


        byte[] img = "".getBytes(StandardCharsets.UTF_8);
        try {
            productServices = new ProductServices();
            dbHelper = new DBHelper(this);
            dbFirebase = new DBFirebase();
        } catch (Exception e) {
            Log.e("DB", e.toString());
        }


        content = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Uri uri = result;
                        StorageReference filePath = storageReference.child("images").child(uri.getLastPathSegment());
                        filePath.putFile(uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(getApplicationContext(), "Imagen Cargada", Toast.LENGTH_SHORT).show();
                                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Uri downloadUrl = uri;
                                                urlImage = downloadUrl.toString();
                                                productServices.insertUriToImageView(urlImage, imgFormProduct, ProductForm.this);
                                            }
                                        });

                                    }
                                });
                    }
                }

        );

        imgFormProduct.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick (View view){
                        content.launch("image/*");
                    }
                    });

        btnFormProduct.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick (View view){
                        try {
                            Producto producto = new Producto(

                                    editNameFormProduct.getText().toString(),
                                    editDescriptionFormProduct.getText().toString(),
                                    Integer.parseInt(editPriceFormProduct.getText().toString()),
                                    urlImage,
                                    Double.parseDouble(editLatitudFormProduct.getText().toString().trim()),
                                    Double.parseDouble(editLongitudFormProduct.getText().toString().trim())


                                    //productServices.imageViewToByte(imgFormProduct)
                            );

                            if (edit) {
                                producto.setId(intentIN.getStringExtra("id"));
                                dbFirebase.updateData(producto);
                            } else {
                                //dbHelper.insertData(producto);
                                dbFirebase.insertData(producto);
                            }


                        } catch (Exception e) {
                            Log.e("DB Insert", e.toString());
                        }

                        Intent intent = new Intent(getApplicationContext(), Catalogo.class);
                        startActivity(intent);

                    }
                    });


/*
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if (id.compareTo("") != 0) {


                    ArrayList<Producto> list = productServices.cursorToArray(dbHelper.getDataById(id));
                    list.add(dbFirebase.getDataById(id));

                    if (list.size() != 0) {
                        Producto producto = list.get(0);

                        //imgFormProduct.setImageBitmap(productServices.byteToBitmap(producto.getImage()));
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

*/
        /*
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
*/

/*
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
*/
                }

        public void clean () {
            editNameFormProduct.setText("");
            editDescriptionFormProduct.setText("");
            editPriceFormProduct.setText("");
            imgFormProduct.setImageResource(R.drawable.agregrarimg);
        }


    }
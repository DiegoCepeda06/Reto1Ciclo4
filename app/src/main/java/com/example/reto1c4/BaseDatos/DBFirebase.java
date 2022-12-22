package com.example.reto1c4.BaseDatos;

import static android.content.ContentValues.TAG;

import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.reto1c4.Adapters.ProductoAdapter;
import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Service.ProductServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBFirebase {
    private FirebaseFirestore db;
    private ProductServices productServices;

    public DBFirebase() {
        this.db = FirebaseFirestore.getInstance();
        this.productServices = new ProductServices();

    }

    public void insertData(Producto producto) {

        Map<String, Object> product = new HashMap<>();
        product.put("id", producto.getId());
        product.put("name", producto.getName());
        product.put("description", producto.getDescription());
        product.put("price", producto.getPrice());
        product.put("image", producto.getImage());
        product.put("deleted", producto.isDeleted());
        product.put("createdAt", producto.getCreatedAt());
        product.put("updatedAt", producto.getUpdateAt());
        product.put("latitud", producto.getLatitud());
        product.put("longitud", producto.getLongitud());


        db.collection("products")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("OkCrud", "DocumentSnapshot added with ID; " + documentReference.getId());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("ErrorCrud", "Error adding document", e);
            }
        });
    }

    public void getData(ProductoAdapter productoAdapter, ArrayList<Producto> list) {
        db.collection("products")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Producto product = null;
                        if(!Boolean.valueOf(document.getData().get("deleted").toString())) {

                        product = new Producto(

                                document.getData().get("id").toString(),
                                document.getData().get("name").toString(),
                                document.getData().get("description").toString(),
                                Integer.parseInt(document.getData().get("price").toString()),
                                document.getData().get("image").toString(),
                                Boolean.valueOf(document.getData().get("deleted").toString()),
                                productServices.stringToDate(document.getData().get("createdAt").toString()),
                                productServices.stringToDate(document.getData().get("updatedAt").toString()),
                                Double.parseDouble(document.getData().get("latitud").toString()),
                                Double.parseDouble(document.getData().get("longitud").toString())

                        );
                        list.add(product);
                        }

                    }
                    productoAdapter.notifyDataSetChanged();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });


    }

    public void deleteData(String id) {
        db.collection("products").whereEqualTo("id", id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        documentSnapshot.getReference().delete();
                    }
                }
            }
        });
    }


    public void updateData(Producto producto) {
        db.collection("products").whereEqualTo("id", producto.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        documentSnapshot.getReference().update(
                                "name", producto.getName(),
                                "description", producto.getDescription(),
                                "price", producto.getPrice(),
                                "image", producto.getImage(),
                                "latitud", producto.getLatitud(),
                                "longitud", producto.getLongitud()
                        );
                    }
                }
            }
        });

    }

    public void updateDataById(String id, String name, String description, String price,
                               byte[] image) {
        db.collection("products").document(id).update("name", name, "description", description, "price", price).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //
            }
        });

    }

    public void deleteDataById(String id) {
        db.collection("products").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //
            }
        });
    }


}

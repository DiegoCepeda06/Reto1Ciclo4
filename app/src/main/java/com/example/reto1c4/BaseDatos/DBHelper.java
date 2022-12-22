package com.example.reto1c4.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.example.reto1c4.Entities.Producto;
import com.example.reto1c4.Service.ProductServices;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;
    private ProductServices productServices;

    public DBHelper(Context context) {
        super(context, "G104.db", null, 1);
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PRODUCTOS (" +
                        "id TEXT PRIMARY KEY," +
                        "name VARCHAR," +
                        "description TEXT," +
                        "price VARCHAR," +
                        "image TEXT," +
                        "deleted TEXT," +
                        "createdAt TEXT," +
                        "updateAt TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCTOS");
    }

    public void insertData(Producto producto) {
        String sql = "INSERT INTO PRODUCTOS VALUES (?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.bindString(1, producto.getId());
        statement.bindString(2, producto.getName());
        statement.bindString(3, producto.getDescription());
        statement.bindString(4, String.valueOf(producto.getPrice()));
        statement.bindString(5, producto.getImage());
        statement.bindString(6, String.valueOf(producto.isDeleted()));
        statement.bindString(7, productServices.dateToString(producto.getCreatedAt()));
        statement.bindString(8, productServices.dateToString(producto.getUpdateAt()));

        statement.executeInsert();

    }

    public Cursor getData() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PRODUCTOS", null);

        return cursor;
    }

    public Cursor getDataById(String id) {


        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PRODUCTOS WHERE id = " + id, null);


        return cursor;


    }

    public void deleteDataById(String id) {
        sqLiteDatabase.execSQL("DELETE FROM PRODUCTOS WHERE id = " + id);

    }

    public void updtadeDataById(String id, String name, String description, String price, byte[] image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("price", price);
        contentValues.put("image", image);

        sqLiteDatabase.update("PRODUCTOS", contentValues, "id = ?", new String[]{id});

    }
}

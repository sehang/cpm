package com.example.cpttm.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumList extends AppCompatActivity {
    List data;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        data = new ArrayList();

        AlbumSQLiteOpenHelper mapSQLiteOpenHelper = new AlbumSQLiteOpenHelper(this);
        SQLiteDatabase db = mapSQLiteOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id,title,cover FROM list", null);
        while (c.moveToNext() ){
            Integer id = c.getInt(c.getColumnIndex("id"));
            String title = c.getString( c.getColumnIndex( "title" ) );
            byte[] byteArray = c.getBlob(c.getColumnIndex("cover"));
            Bitmap img = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Map dataItem = new HashMap();
            dataItem.put("Id", id);
            dataItem.put("Title", title);
            dataItem.put("Cover", img);

            data.add(dataItem);
        }
        ListView listView1 = findViewById(R.id.listView1);
        String[] from = {"Title", "Cover"};
        int[] to = {R.id.Title, R.id.Cover};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_view_item, from, to);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder(){

            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if( (view instanceof ImageView ) & (data instanceof Bitmap) ) {
                    ImageView iv = (ImageView) view;
                    Bitmap bm = (Bitmap) data;
                    iv.setImageBitmap(bm);
                    return true;
                }
                return false;

            }

        });

        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l) {
                Intent playerActivityIntent = new Intent(AlbumList.this, PlayerActivity.class);
                Log.d( "AlbumList", "listId: " + l );
                playerActivityIntent.putExtra("listId", ( int )( l + 1 ) );
                startActivity(playerActivityIntent);
            }
        });
    }
}

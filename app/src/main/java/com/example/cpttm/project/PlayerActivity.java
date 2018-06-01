package com.example.cpttm.project;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private Button btn, mPlayButton, mPauseButton, mResetButton;

    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;

    Integer Id;
    String Title;
    String Artist;
    String Url;
    ArrayList<Bitmap> ImgArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Log.d("PlayerActivity", "PlayerActivity > onCreate");

        //int listId = this.getIntent().getIntExtra("listId", 1);
        int listId = 0;
        initializeUrl(listId);
        initializeUI();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void initializeUrl(int listId) {
        Url = "http://ioio-xr.16mb.com/song/1.mp3";

//        AlbumSQLiteOpenHelper albumSQLiteOpenHelper = new AlbumSQLiteOpenHelper(this);
//        SQLiteDatabase db = albumSQLiteOpenHelper.getReadableDatabase();
//
//        Cursor c = db.rawQuery("SELECT id, title, artist, url FROM list WHERE id = ? LIMIT 1", new String[]{String.valueOf(listId)});
//        c.moveToPosition(0);
//        Id = c.getInt(c.getColumnIndex("id"));
//        Title = c.getString(c.getColumnIndex("title"));
//        Artist = c.getString(c.getColumnIndex("artist"));
//        Url = c.getString(c.getColumnIndex("url"));
//
//
//        c = db.rawQuery("SELECT id, img FROM booklet WHERE id = ? LIMIT 1", new String[]{String.valueOf(listId)});
//        ImgArray = new ArrayList<Bitmap>();
//        while (c.moveToNext()) {
//            byte[] byteArray = c.getBlob(c.getColumnIndex("img"));
//            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//            ImgArray.add(bitmap);
//        }
    }

    private void initializeUI() {
        mPlayButton = (Button) findViewById(R.id.button_play);
        mPauseButton = (Button) findViewById(R.id.button_pause);
        mResetButton = (Button) findViewById(R.id.button_reset);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playPause) {
                    Log.d("PlayerActivity > onCreate > mPlayButton.onClick", "Play Streaming");

                    if (initialStage) {
                        new Player().execute(Url);
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }

                    playPause = true;
                }
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playPause) {
                    Log.d("PlayerActivity > onCreate > mPauseButton.onClick", "Pause Streaming");

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialStage = true;
                playPause = false;

                //btn.setText("Launch Streaming");
                Log.d("PlayerActivity > onCreate > mResetButton.onClick", "Reset Streaming");

                mediaPlayer.stop();
                mediaPlayer.reset();

                if (initialStage) {
                    new Player().execute(Url);
                } else {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                }

                playPause = true;
            }
        });
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;

                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("PlayerActivity > Player", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }
}
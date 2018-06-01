package com.example.cpttm.project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * Allows playback of a single MP3 file via the UI. It contains a {@link MediaPlayerHolder}
 * which implements the {@link PlayerAdapter} interface that the activity uses to control
 * audio playback.
 */

public class PlayerBak extends AppCompatActivity {

    public static final String TAG = "Player";
    //public static final int MEDIA_RES_ID = R.raw.jazz_in_paris;

    private TextView mTextDebug;
    private SeekBar mSeekbarAudio;
    private ScrollView mScrollContainer;
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;

    Integer Id;
    String Title;
    String Artist;
    String Url;
    ArrayList<Bitmap> ImgArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //int listId = this.getIntent().getIntExtra("listId", 1);
        int listId = 0;
        initializeDataSource(listId);

        initializeUI();
        initializeSeekbar();
        initializePlaybackController();
        Log.d(TAG, "onCreate: finished");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayerAdapter.loadMedia(Url);
        Log.d(TAG, "onStart: create MediaPlayer");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mPlayerAdapter.isPlaying()) {
            Log.d(TAG, "onStop: don't release MediaPlayer as screen is rotating & playing");
        } else {
            mPlayerAdapter.release();
            Log.d(TAG, "onStop: release MediaPlayer");
        }
    }

    private void initializeDataSource(int listId) {
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
        mTextDebug = (TextView) findViewById(R.id.text_debug);
        Button mPlayButton = (Button) findViewById(R.id.button_play);
        Button mPauseButton = (Button) findViewById(R.id.button_pause);
        Button mResetButton = (Button) findViewById(R.id.button_reset);
        //mSeekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        mScrollContainer = (ScrollView) findViewById(R.id.scroll_container);

        mPauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.pause();
                    }
                });
        mPlayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.play();
                    }
                });
        mResetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.reset();
                    }
                });
    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(this);
        Log.d(TAG, "initializePlaybackController: created MediaPlayerHolder");
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
        Log.d(TAG, "initializePlaybackController: MediaPlayerHolder progress callback set");
    }

    private void initializeSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            mSeekbarAudio.setMax(duration);
            Log.d(TAG, String.format("setPlaybackDuration: setMax(%d)", duration));
        }

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                mSeekbarAudio.setProgress(position, true);
                Log.d(TAG, String.format("setPlaybackPosition: setProgress(%d)", position));
            }
        }

        @Override
        public void onStateChanged(@State int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
        }

        @Override
        public void onLogUpdated(String message) {
            if (mTextDebug != null) {
                mTextDebug.append(message);
                mTextDebug.append("\n");
                // Moves the scrollContainer focus to the end.
                mScrollContainer.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
            }
        }
    }
}

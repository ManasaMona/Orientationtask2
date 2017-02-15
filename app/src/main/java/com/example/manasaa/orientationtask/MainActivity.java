package com.example.manasaa.orientationtask;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mStartButton, mStopButton, mActivity2;
    private static int progressStatus ;
    private TextView mTextView;
    //private ProgressDialog mProgressBar;
    private ProgressBar mProgressBar;
    private static final int MAX_VALUE = 50;
    private static boolean isCancelled;
    private static boolean startClicked;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"called ONCREATE");
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) { //getting the values from last instance
            int progress = savedInstanceState.getInt("progress_value");
            Log.d(TAG,progressStatus+"--progress_value");
        }
        else {
            progressStatus=0;
        }

        setContentView(R.layout.activity_main);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStopButton = (Button) findViewById(R.id.stop_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
       // mProgressBar=new ProgressDialog(findViewById(R.id.progressBar).getContext());
        mProgressBar.setMax(MAX_VALUE);
        mTextView = (TextView) findViewById(R.id.textView);
        mStopButton.setVisibility(View.INVISIBLE);

        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {//restoring the values from last instance
        int progress = savedInstanceState.getInt("progress_value");
        boolean start=savedInstanceState.getBoolean("isStartClicked");
        boolean rotate=savedInstanceState.getBoolean("isRotated");

        Log.d(TAG,"called RESTORE INSTANCE"+progress+"--"+start);


        if(start==true && rotate) {
            mStopButton.setVisibility(View.VISIBLE);
            progressStatus = progress;
            mTextView.setText(progress + "%");
            mProgressBar.setProgress(progress);

            progressBarUpdater();
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start_button:
                Log.d(TAG,"Start Click");
                isCancelled=false;
                startClicked=true;
                mStartButton.setVisibility(View.INVISIBLE);
                mStopButton.setVisibility(View.VISIBLE);
                progressBarUpdater();
                break;
            case R.id.stop_button:
                Log.d(TAG,"Stop Click");
                mStopButton.setVisibility(View.INVISIBLE);
                mStartButton.setVisibility(View.VISIBLE);
                isCancelled=true;
                progressStatus=0;
                mProgressBar.setProgress(progressStatus);//current value in the text view
               mTextView.setText("0");
//                handler=null
                break;


        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG,"called ON SAVE INSTANCESTATE"+"--value--"+progressStatus);
        outState.putInt("progress_value", progressStatus);
        outState.putBoolean("isStartClicked",startClicked);
        outState.putBoolean("isRotated",true);
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"*****BACKBUTTON PRESSED*****");
       progressStatus=0;
        return super.onKeyDown(keyCode, event);
    }


    //thread to update progress bar
    protected void progressBarUpdater() {
        Log.d(TAG,progressStatus+"--progressBarUpdater Method");
       Thread thread= new Thread(new Runnable() {
            public void run() {
                while (progressStatus < MAX_VALUE && !isCancelled) {
                    progressStatus += 1;
                    try {
                        Thread.sleep(200);   // Sleep for 200 milliseconds.
                    } catch (InterruptedException e) { //Just to display the progress slowly
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {// Update the progress bar and display the
                        public void run() {
                            try {
                                Thread.sleep(100);   // Sleep for 200 milliseconds.
                            } catch (InterruptedException e) { //Just to display the progress slowly
                                e.printStackTrace();
                            }
                            mProgressBar.setProgress(progressStatus);//current value in the text view
                            mTextView.setText(progressStatus + "/" + mProgressBar.getMax());
                        }
                    });

                }
//                if (progressStatus==MAX_VALUE){
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    mProgressBar.dismiss();
//
//                }

            }
        });

        thread.start();


    }

    @Override
    protected void onStart() {
        Log.d(TAG,"called ONSTART");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"called ONPAUSE");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"called ONPAUSE");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"called ON DESTROy");
        super.onDestroy();
    }
}

package com.example.android.simpleasynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsyncTask extends AsyncTask<Void, Integer, String> {
    /*
    If you pass a TextView into the AsyncTask constructor and then store it in a member variable,
    that reference to the TextView means the Activity cannot ever be garbage collected
    and thus leaks memory, even if the Activity is destroyed and recreated
    as in a device configuration change

    The weak reference prevents the memory leak by allowing the object
    held by that reference to be garbage collected if necessary.
     */
    private WeakReference<TextView> mTextView;
    private WeakReference<ProgressBar> mProgressBar;

    // Because the AsyncTask needs to update the TextView in the Activity
    // once it has completely slept (in the onPostExecute() method), we
    // need a reference to the TextView via the constructor
    SimpleAsyncTask(TextView tv, ProgressBar pb){
        mTextView = new WeakReference<>(tv);
        mProgressBar = new WeakReference<>(pb);
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Random r = new Random();
        int n = r.nextInt(10)+1;
        int sleepCount = n * 500;
        try{
            int totalCount = 0;
            int oneHundredth = sleepCount/100;
            while(totalCount < 100){
                Thread.sleep(oneHundredth);
                totalCount++;
                publishProgress(totalCount);

            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "Awake at last after sleeping for "+sleepCount+" milliseconds!";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //super.onProgressUpdate(values[0]);
        mProgressBar.get().setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        //Notice the 'get()' method prior to 'setText'
        //This is because mTextView is a weak reference,
        //therefore you need to dereference it with 'get'
        mTextView.get().setText(s);
        mProgressBar.get().setVisibility(View.INVISIBLE);
    }


}



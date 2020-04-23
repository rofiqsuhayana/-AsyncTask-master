package com.example.android.simpleasynctask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    //Key for saving the state of the TextView
    private static final String TEXT_STATE = "currentText";
    private ProgressBar mProgressBar;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textView1);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);
        // Restore TextView if there is a savedInstanceState
        if(savedInstanceState!=null){
            mTextView.setText(savedInstanceState.getString(TEXT_STATE));
        }
    }

    public void startTask(View view) {
        mTextView.setText(R.string.napping);
        new SimpleAsyncTask(mTextView, mProgressBar).execute();
    }

    /**
     * Saves the contents of the TextView to restore on configuration change.
     * @param outState The bundle in which the state of the activity is saved
     * when it is spontaneously destroyed.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of the TextView
        outState.putString(TEXT_STATE, mTextView.getText().toString());
        //The progress bar cannot use the Bundle because it's visibility is invisible when initialized
        //and all the progress is lost with the original AsyncTask
    }
}

/*
Note: You'll notice that when the device is rotated,
the TextView resets to its initial content, and the AsyncTask does not seem able to
update the TextView.

There are several things going on here:

*  When you rotate the device, the system restarts the app,
   calling onDestroy() and then onCreate(). The AsyncTask will continue running
   even if the activity is destroyed, but it will lose the ability
   to report back to the activity's UI. It will never be able to update
   the TextView that was passed to it, because that particular TextView has also been destroyed.

*  Once the activity is destroyed the AsyncTask will continue running
   to completion in the background, consuming system resources.
   Eventually, the system will run out of resources, and the AsyncTask will fail.

*  Even without the AsyncTask, the rotation of the device
   resets all of the UI elements to their default state,
   which for the TextView is the default string that you set in the layout file.

In order to prevent the TextView from resetting to the initial string,
you need to save its state. You will now implement onSaveInstanceState()
to preserve the content of your TextView when the activity is destroyed
in response to a configuration change such as device rotation.

 */

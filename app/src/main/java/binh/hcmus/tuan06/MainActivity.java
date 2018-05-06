package binh.hcmus.tuan06;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar myBar;

    TextView lblPersent;
    EditText txtMax;
    Button btnStart;

    int MAX_PROGRESS = 0;

    int globalVar = 0;

    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblPersent = (TextView) findViewById(R.id.lblPersent);
        myBar = (ProgressBar) findViewById(R.id.myBar);
        txtMax = (EditText) findViewById(R.id.txtMax);
        txtMax.setHint("Enter max data");
        btnStart = (Button) findViewById(R.id.btnStart);

        txtMax.setText("100");
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MAX_PROGRESS = Integer.parseInt(txtMax.getText().toString());
                onStart();
            }// onClick
        });// setOnClickListener
    }

    @Override
    protected void onStart() {
        super.onStart();
        // prepare UI components
        myBar.setMax(MAX_PROGRESS);
        myBar.setProgress(0);
        myBar.setVisibility(View.VISIBLE);
        // create-start background thread were the busy work will be done
        Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
        myBackgroundThread.start();
    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                // update UI, observe globalVar is changed in back thread
                myBar.setProgress(globalVar);

                //tinh phan tram
                int pt=globalVar*100/MAX_PROGRESS;
                lblPersent.setText(pt+"%");
            } catch (Exception e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
        }
    }; // foregroundTask

    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            // busy work goes here...
            try {
                for (int n = 0; n <= MAX_PROGRESS; n++) {
                    // this simulates 1 sec. of busy activity
                    Thread.sleep(3);
                    // change a global variable here...
                    globalVar=n;
                    // try: next two UI operations should NOT work
                    // Toast.makeText(getApplication(), "Hi ", 1).show();
                    // txtDataBox.setText("Hi ");
                    // wake up foregroundRunnable delegate to speak for you
                    myHandler.post(foregroundRunnable);
                }
            } catch (InterruptedException e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
        }// run
    };// backgroundTask
}

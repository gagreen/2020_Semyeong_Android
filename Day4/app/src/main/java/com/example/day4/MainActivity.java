package com.example.day4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.day4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);

        b.progress.setMax(10);
        b.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                doCalc();
                new MyTask().execute();
            }
        });
    }

/***********************************************/
    class MyTask extends AsyncTask<Long, Integer, Long> {
        // Working In Main Thread  : onPreExecute, onPostExecute, onProgressUpdate
        // Working In Sub Thread   : doInBackground

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            b.textview.setText("연산중..");

        }

        @Override
        protected Long doInBackground(Long... longs) {
            Log.d("HYCCC", "start");

            long sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
                Log.d("HYCCC", "" + i);

                publishProgress(i+1);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("HYCCC", "done~~");

            return sum;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            b.progress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            b.textview.setText("Result: "+aLong);
        }
    }

    void doCalc() {
/***********************************************/
//        // Runnable: 실행 가능한 코드 집합을 가지는 객체
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                Log.d("HYCCC", "start");
//
//                for (int i = 0; i < 10; i++) {
//                    Log.d("HYCCC", "" + i);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.d("HYCCC", "done~~");
//            }
//        };
//
//        // Thread 생성 시 Runnable 객체 전달
//        Thread t = new Thread(run);
//        t.start();

/***********************************************/
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                int sum = 0;
//                while (sum < Integer.MAX_VALUE) {
//                    sum ++;
//                }
//            }
//        };
    }
}
package com.example.day6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.day6.databinding.ActivityMainBinding;
import com.example.day6.db.MyDataBase;
import com.example.day6.db.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);

        b.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertItem();
            }
        });

        b.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    void insertItem() {
        String name = b.editText.getText().toString();
//        Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(MainActivity.this, "입력된 이름이 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        new DBAsync<String, Void, Void>("Wait", "데이터를 입력하는 중...") {

            @Override
            protected Void doInBackground(String... strings) {
                for (String s : strings) {
                    User user = new User(s);
                    db.userDao().insert(user);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                b.editText.setText("");
            }
        }.execute(name);


        /*본래 아래와 같은 형태*/
//        new AsyncTask<String, Void, Void>() {
//
//            ProgressDialog progressDlg;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDlg = ProgressDialog.show(MainActivity.this, "Wait", "데이터 저장중...");
//            }
//
//            @Override
//            protected Void doInBackground(String... strings) {
//                MyDataBase db = Room.databaseBuilder(MainActivity.this, MyDataBase.class, "mydb.db").build();
//                for (String s : strings) {
//                    User user = new User(s);
//                    db.userDao().insert(user);
//                }
//                db.close();
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                b.editText.setText("");
//                progressDlg.dismiss();
//            }
//        }.execute(name /*b.editText.getText().toString()*/);
    }

    @SuppressLint("StaticFieldLeak")
    void loadData() {
        new DBAsync<Void, Void, List<User>>("Wait", "데이터를 불러오는 중...") {

            // In Sub Thread
            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> users = db.userDao().getAll();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return users;
            }

            // in Main Thread (UI 작업 가능)
            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                b.textView.setText("");
                for (User e : users) {
                    b.textView.append(e.name + "\n");
                }
            }
        }.execute();

    }


    /* 중복을 없애기 위해 AsyncTask를 상속받아 동일한 메서드를 작성 */
    abstract class DBAsync<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

        private static final String DB_NAME = "mydb.db";

        private ProgressDialog progressDlg;
        protected MyDataBase db;
        private String dlgTitle;
        private String dlgMessage;

        public DBAsync(String dlgTitle, String dlgMessage) {
            this.dlgTitle = dlgTitle;
            this.dlgMessage = dlgMessage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(MainActivity.this, dlgTitle, dlgMessage);
            db = Room.databaseBuilder(MainActivity.this, MyDataBase.class, DB_NAME).build();
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            db.close();
            progressDlg.dismiss();
        }
    }
}
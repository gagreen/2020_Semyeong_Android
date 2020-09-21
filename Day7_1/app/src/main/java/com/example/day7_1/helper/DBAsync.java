package com.example.day7_1.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.example.day7_1.db.SearchDB;

public abstract class DBAsync<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    ProgressDialog dlg;
    protected Context mCtx;
    protected SearchDB mDB;

    // Constructor
    public DBAsync(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dlg = ProgressDialog.show(mCtx, null, "Wait..");
        mDB = Room.databaseBuilder(mCtx, SearchDB.class, "search.db").build();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        dlg.dismiss();
        mDB.close();
    }
}

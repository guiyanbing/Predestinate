package com.juxin.predestinate.bean.db;

import android.content.Context;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import rx.functions.Action1;

/**
 * Created by siow on 2017/5/19.
 */

public class OldDBModule {
    @Singleton
    private BriteDatabase provideDB(Context context) {
        final SqlBrite.Builder builder = new SqlBrite.Builder();
        OldDBHelper oldDBHelper = new OldDBHelper(context);
        BriteDatabase db = builder.build().wrapDatabaseHelper(oldDBHelper, rx.schedulers.Schedulers.io());
        return db;
    }

    public void getMsgData() {
        BriteDatabase db = provideDB();
        QueryObservable query = db.createQuery(DBHelper.USERTABLE, "SELECT * FROM " + DBHelper.USERTABLE, null);
        query.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
//                List<String> list = new ArrayList<String>();
//                while (cursor.moveToNext()) {
//                    String username = cursor.getString(cursor.getColumnIndex("USERNAME"));
//                    String id = cursor.getString(cursor.getColumnIndex("_id"));
//                    String nickname = cursor.getString(cursor.getColumnIndex("NICKNAME"));
//                    list.add(id + "--" + username + "--" + nickname);
//                }
//                cursor.close();
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
//                lv.setAdapter(adapter);
            }
        });
    }

//    public List<Message_List> query_Message_List(String login_id, String other_id, int offset, int maxResult) {
//        List<Message_List> message_Lists = new ArrayList<Message_List>();
//        Cursor cursor = mSQLiteDatabase.query(MESSAGE_LIST_TABLE, null, "login_id = ? and other_id = ?", new String[]{login_id, other_id},
//                null, null, "_id desc", offset + "," + maxResult);
//        try {
//            while (cursor.moveToNext()) {
//                Message_List message = new Message_List();
//                message.setLogin_id(cursor.getString(INDEX_COLUMN_LOGIN_ID));
//                message.setMsg_id(cursor.getLong(INDEX_COLUMN_MSG_ID));
//                message.setOther_id(cursor.getString(INDEX_COLUMN_OTHER_ID));
//                message.setContent(cursor.getString(INDEX_COLUMN_CONTENT));
//                message.setTime(cursor.getString(INDEX_COLUMN_TIME));
//                message.setReceive_send_status(cursor.getInt(INDEX_COLUMN_RECEIVE_SEND_STATUS));
//                message.setMsg_status(cursor.getInt(INDEX_COLUMN_STATUS));
//                message.setMsg_type(cursor.getInt(INDEX_COLUMN_MSG_TYPE));
//                message_Lists.add(message);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return message_Lists;
//    }
}

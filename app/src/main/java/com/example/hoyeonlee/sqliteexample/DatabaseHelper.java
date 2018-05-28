package com.example.hoyeonlee.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoyeonlee on 2018. 5. 17..
 */

//DatabaseHelper클래스는 SQLiteOpenHelper를 상속받은 클래스로 Sqlite 데이터베이스에 쉽게 접근하도록 도와주는 클래스입니다.
public class DatabaseHelper extends SQLiteOpenHelper {

    //Database 안에 Table을 여러개 넣을 수 있습니다. 우리는 DATABASE를 하나 만들고 여러개의 테이블을 만들 수 있습니다
    //엑셀을 예로 들면 하나의 파일이 데이터베이스이고 Sheet가 Table이 됩니다
    private static final String DATABASE_NAME = "TestDatabase";
    public static final String TABLE_NAME = "student";
    private static final int DATABASE_VERSION = 2;

    //DatabaseHelper 클래스가 인스턴스화 될 때 다음과 같이 DATABASE 이름과 버전이 들어가야 합ㄴ디ㅏ
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //여기서 student라는 테이블을 만들 것이며 첫번째 칼럼에 name이라는 이름에 text형을 넣을 수 있으며 두번째 칼럼에 age라는 이름에 text형을 넣을 수 있습니다.
    //이렇게 테이블을 설계하는 query명령어를 만들고 db.execSQL로 실행시킵니다 그렇게 되면 테이블이 만들어집니다.
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table "+ TABLE_NAME + "(name TEXT,age TEXT);";
        db.execSQL(query);
    }

    //만일 버전이 업데이트되게 될 시 테이블을 없애고 새로 똑같은 테이블을 만드는 메소드입니다.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists "+TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }
    //Database에 데이터를 넣는 작업을 메소드화 시켰습니다.
    public void add(String name, int age){
        //데이터를 넣기 위해서는 this.getWritableDatabase()를 통해 SQliteDatabase 객체를 불러와야 합니다.
        SQLiteDatabase db = this.getWritableDatabase();
        //db객체에 넣을 데이터는 ContentValues객체로 객체화시킨후 put을 통해 각 Column에 데이터를 넣어줍니다
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("age",age);
        //그리고 넣은 데이터를 db.insert메소드를 통해 insert 해줍니다.
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    //Database에 들어있는 데이터를 불러오는 작업을 메소드화 시켰습니다.
    public ArrayList<ContentValues> getAll(){
        //우리가 데이터를 넣을때 ContentValues로 넣어주었습니다(add메소드 참고). 그렇기에 받을 때도 ContentValues로 받겠습니다.
        ArrayList<ContentValues> list = new ArrayList<>();
        //데이터를 전부가져오는 명령어는 SELECT * FROM Table이름입니다.
        String query = "SELECT * FROM "+ TABLE_NAME;
        //db.getReadableDatabase()를 통해 데이터베이스 객체를 불러옵니다.
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor 객체를 통해 데이터를 조회하고 가져올 수 있습니다.
        Cursor cursor = db.rawQuery(query,null);
        //순차적으로 한 행씩 데이터를 가져와서 ContentValues에 값을 넣고 list에 그 ContentValues를 넣는 작업을 진행합니다. 이는 모든 행까지 전부 접근합니다
        if(cursor.moveToFirst()){
            do{
                ContentValues value = new ContentValues();
                //아까 테이블을 만들때 1번째로 넣은게 name이라는 column이며 이때 ColumnIndex는 0, age는 1입니다.
                value.put("name",cursor.getString(0));
                value.put("age",cursor.getInt(1));
                list.add(value);
            }while(cursor.moveToNext());
        }
        db.close();
        //마지막으로 List를 리턴합니다.
        return list;
    }
}

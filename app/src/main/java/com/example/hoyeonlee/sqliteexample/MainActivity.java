package com.example.hoyeonlee.sqliteexample;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private EditText nameInput;
    private EditText ageInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameInput = findViewById(R.id.input_name);
        ageInput = findViewById(R.id.input_age);
        ListView listView = findViewById(R.id.listView);
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        //DatabaseHepler 클래스를 인스턴스화 합니다.
        databaseHelper = new DatabaseHelper(this);

        //만일 처음 화면을 켰을 때(onCreate)에서 이미 데이터베이스에값이 들어가있다면 listView에 띄워줘야겠죠?
        //이는 getAll()을 통해 받은 List에 size()가 0보다 큰지 확인해서 알 수 있습니다.
        if(databaseHelper.getAll().size() > 0){
            ArrayList<ContentValues> list = databaseHelper.getAll();
            //가져온 데이터들은 COntentValues로 get("name")으로 각 행의 name값만 가져올 것입니다.
            for(int i =0; i<list.size();i++){
                adapter.add(list.get(i).get("name"));
            }
        }

        //버튼을 클릭하면 DatabaseHelper클래스의 add메소드를 실행시켜 데이터를 테이블에 넣어주는 작업이 진행됩니다.
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                int age = Integer.valueOf(ageInput.getText().toString());
                //1. 데이터베이스헬퍼에 데이터를 넣어줍니다.
                databaseHelper.add(name,age);
                //2. 데이터베이스헬퍼를 통해 테이블에 저장된 데이터를 전부 가져옵니다.
                ArrayList<ContentValues> list = databaseHelper.getAll();

                //가져온 데이터들은 COntentValues로 get("name")으로 각 행의 name값만 가져올 것입니다.
                //adapter.clear()를 통해 listView를 다 지워주고 처음부터 새로운 값을 넣어주면서 listView를 채워줄것입니다.
                //만일 clear을 안하면 중복해서 데이터가 쌓일 것 입니다.
                adapter.clear();
                for(int i =0; i<list.size();i++){
                    adapter.add(list.get(i).get("name"));
                }
                nameInput.setText("");
                ageInput.setText("");

            }
        });
        //해당 리스트뷰 아이템을 클릭했을 때 각 아이템에 대해 반응하게 됩니다. setOnClickListener와는 다릅니다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}

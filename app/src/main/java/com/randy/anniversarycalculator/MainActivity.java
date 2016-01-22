package com.randy.anniversarycalculator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MYD - MainActivity";

    private ListView listView;
    private Toolbar toolBar;

    private AlertDialog mDialog = null;

    DBHandler db = new DBHandler(this);

    private ArrayList<Item> arItem = new ArrayList<Item>();
    private ListAdapterWithButton listAdapter;

    private int mId;

    private Common mCommon = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivityForResult(intent, 1);
            }
        });



        // table이 존재하지 않으면 table 생성
        db.onCreate(db.getWritableDatabase());

        getAllItem();

        // ListView와 Adapter 연결
        listView = (ListView)this.findViewById(R.id.main_list);
        listAdapter = new ListAdapterWithButton(getApplicationContext(), R.layout.list_layout, arItem);
        listView.setAdapter(listAdapter);
        listView.getBackground().setAlpha(150);      // 투명도 효과

        // 삭제 버튼 [클릭]시의 이벤트 리스너를 등록
        listAdapter.setOnButtonClickListener(new ListAdapterWithButton.onButtonClickListener() {
            public void onButtonDelClick(Item item) {
                mId = item.getId();
                mDialog = createDialog();
                mDialog.show();
            }
        });

        // 항목 [클릭]시의 이벤트 리스너를 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, arItem.get(position).getDate(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);

                intent.putExtra("id", arItem.get(position).getId());
                intent.putExtra("date", arItem.get(position).getDate());
                intent.putExtra("sentence", arItem.get(position).getSentence());
                intent.putExtra("noti_flag", arItem.get(position).getNotiFlag());
                intent.putExtra("noti_interva", arItem.get(position).getiNotiInterval());
                intent.putExtra("hour", arItem.get(position).getHour());
                intent.putExtra("minute", arItem.get(position).getMin());
                intent.putExtra("list_text", arItem.get(position).getsListText());

                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        arItem.clear();
        getAllItem();

        listAdapter.setArrayList(arItem);
        listAdapter.notifyDataSetChanged();
    }


    // get all items
    public void getAllItem() {
        String date = "";

        arItem = db.getAllItems();

        for (int i = 0; i < arItem.size(); i++) {
            /*
            Log.d(TAG, "ID : " + arItem.get(i).getId()
                    + ", DATE : " + arItem.get(i).getDate()
                    + ", SENTENCE : " + arItem.get(i).getSentence()
                    + ", FLAG : " + arItem.get(i).getNotiFlag()
                    + ", INTERVAL : " + arItem.get(i).getiNotiInterval()
                    + ", HOUR : " + arItem.get(i).getHour()
                    + ", MINUTE : " + arItem.get(i).getMin()
            );
            */

            date = arItem.get(i).getDate();
            arItem.get(i).setsListText(mCommon.setTextSentence(arItem.get(i).getSentence(), date, getString(R.string.surfix)));
        }
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);

        ab.setTitle(R.string.dialog_del_title);
        ab.setMessage(R.string.dialog_del_content);
        ab.setCancelable(false);
        //ab.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        ab.setPositiveButton(R.string.dialog_del_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                db.deleteItem(mId);

                Toast.makeText(MainActivity.this, R.string.delete_ok, Toast.LENGTH_SHORT).show();
                setDismiss(mDialog);

                arItem.clear();
                getAllItem();

                listAdapter.setArrayList(arItem);
                listAdapter.notifyDataSetChanged();

            }
        });

        ab.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setDismiss(mDialog);
            }
        });

        return ab.create();
    }


    /**
     * 다이얼로그 종료
     * @param dialog
     */
    private void setDismiss(Dialog dialog){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

}

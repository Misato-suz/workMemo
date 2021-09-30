package com.example.workMemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListWorkActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonBack;
    private ListView listView;
    private ArrayList<Record> listItems = new ArrayList<>();
    private DateArrayAdapter arrayAdapter;
    private Context context;
    private String fileName;
    //TODO CSVファイルを取得して中身をメールで転送できるようにする。
    //TODO アーカイブ機能
    //TODO 業務内容別ソート機能

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            fileName = bundle.getString("fileName");
        }


        listView = findViewById(R.id.listView);
        buttonBack = findViewById(R.id.buttonBackList);

        buttonBack.setOnClickListener(this);

        context = getApplicationContext();

        /**ファイルが存在：　ファイルの中身を取得してリストViewに表示
         * ファイルがない：　記録がない旨を表示 */
        if (FileProcessor.checkFile(context)) {

            StringBuilder stringBuilder = FileProcessor.readFile(context); //ファイル読み込み
            String[] arrayListAll = stringBuilder.toString().split("\n");
            for (String s : arrayListAll) {
                String[] arrayElementHolder = s.split(",", -1);
                /*
                もしmemo内に改行があった場合、splitで分割されてarrayListSmallの中身は一つになる。
                ListItemsの最終要素を取得し、recordに代入する。
                appendMemoでmemoに書き加える。
                 */
                if (arrayElementHolder.length == 1){
                    Record record = listItems.get(listItems.size()-1);
                    record.appendMemo(arrayElementHolder[0]);
                    continue;
                }
                //もしarrayListSmallがdate,work,memoを含んでいた場合、recordのインスタンスとしてlistItemsに代入
                Record record = new Record(arrayElementHolder[0], arrayElementHolder[1], arrayElementHolder[2]);
                listItems.add(record);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listItems.sort(Comparator.comparing(Record::getDate));
            }

            //set adapters to listView
            // date_list : customized list view
            arrayAdapter = new DateArrayAdapter(this, R.layout.date_list);
            arrayAdapter.addAll(listItems);
            arrayAdapter.setItems(listItems); //arrayAdapterにlistItemsを渡している
            listView.setAdapter(arrayAdapter);
        } else {
            listNoFile();
        }
    }


    /**
     * リストビューにファイルがないことを表示する
     */
    private void listNoFile() {
        List<String> noFileList = new ArrayList<>();
        noFileList.add("記録はありません");
        ArrayAdapter<String> noFileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noFileList);
        listView.setAdapter(noFileAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonBack){
            Intent intent = new Intent(ListWorkActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    //メニュー画面を設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //XML リソースをプログラム可能なオブジェクトに変換する
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    //メニューのアイテムをクリックしたときの処理
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        switch (item) {
            case R.id.menu_mail:
                //send mail
                break;
            case R.id.menu_archive:
                //archive
                String fileContents = FileProcessor.readFile(context).toString();
                String newFileName = "1";
                if (!FileProcessor.checkFile(newFileName, context)){
                    FileProcessor.writeFile(fileContents, newFileName, context);
                }
                //myFile を消去する。
                System.out.println("DeleteFile: " + FileProcessor.deleteFile(context));
                listNoFile();
                break;

            case R.id.menu_delete:
                /*
                ファイルが無ければTOASTを表示
                ファイルがあればAlertDialogを表示、Okボタンが押されたらファイルを削除
                 */
                if(FileProcessor.checkFile(context)) {
                    System.out.print(1);
                    new AlertDialog.Builder(this)
                            .setTitle("本当に削除しますか？")
                            .setMessage("無くなっちゃうよ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK button pressed
                                    boolean delete = FileProcessor.deleteFile(context);     //delete file
                                    //show toast if file couldn't be deleted
                                    if (!delete) {
                                        Toast.makeText(getApplicationContext(), R.string.FileCouldNotBeDeleted, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //削除に成功した場合に、listNoFile()を実行
                                        listNoFile();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                    System.out.print(2);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.fileDoesNotExist, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_button:
                //button
                //逆順にする
                Collections.reverse(listItems); //listItemsの要素が逆順になる。戻り値→listItems
                arrayAdapter.clear();//remove all elements from the list
                arrayAdapter.addAll(listItems);// Adds the specified collection at the end of the array.
                arrayAdapter.notifyDataSetChanged();
                //↑　Notifies the attached observers that the underlying data has been changed and
                // any View reflecting the data set should refresh itself. On refresh, convertView is nonNull
                break;
        }
        return false;
    }
}
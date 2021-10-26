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
import java.util.Calendar;
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

        /*ファイルが存在：　ファイルの中身を取得してリストViewに表示
         * ファイルがない：　記録がない旨を表示　
         */
        if (FileProcessor.checkFile(context)) {
            /* stringBuilder: ファイルの中身を読み込んで保存
             * arrayListTemp: 改行で区切って、配列に挿入する。
             * 例： ["日付,業務内容,メモ" "メモ" "日付,業務内容,メモ"...]
             * arrayElementHolder: arrayListTempの各要素を","で区切る
             * 例：　"日付,業務内容,メモ"　-> "日付" "業務内容" "メモ"に区切られる。それぞれrecordのインスタンスとしてlistItemsに代入
             * 例：　"メモ" -> 前のメモに追記
             */
            StringBuilder stringBuilder = FileProcessor.readFile(context); //ファイル読み込み
            String[] arrayListTemp = stringBuilder.toString().split("\n");//改行ごとにarrayListAllに入れる。
            for (String s : arrayListTemp) {
                String[] arrayElementHolder = s.split(",", -1);
                if (arrayElementHolder.length == 1){
                    /*
                    「メモ」内に改行があった場合の処理
                    arrayListTempは改行で区切られるので、arrayElementHolderの要素数は1になる。
                    ListItemsの最終要素を取得し、recordに代入する。（コンストラクタを呼び出す、インスタンスを作る）
                    appendMemoでmemoに書き加える。
                     */
                    Record record = listItems.get(listItems.size()-1);
                    record.appendMemo(arrayElementHolder[0]); //listItemsはRecord型なので、改めてlistItemsに代入する必要はない
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
        } else {//ファイルがない場合
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
                /*
                ファイルが無ければTOASTを表示
                ファイルがあればAlertDialogを表示、Okボタンが押されたらファイルをアーカイブする
                 */
                if(FileProcessor.checkFile(context)) {
                    //アーカイブするファイルがある場合
                    new AlertDialog.Builder(this)
                            .setTitle("アーカイブしますか？")
                            .setPositiveButton("OK", (dialogInterface, i) -> {
                                //archive
                                String fileContents = FileProcessor.readFile(context).toString();
                                //アーカイブするファイルにアーカイブする日付の名前を付ける
                                //Year-Month-Date
                                String newFileName = (Calendar.getInstance().get(Calendar.YEAR)) +
                                        "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +
                                        "-" + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                                int j = 0;
                                String tempName = newFileName;
                                while (FileProcessor.checkFile(tempName, context)) {
                                    j++;
                                    tempName = newFileName + "-" + j;
                                }
                                newFileName = tempName;
                                FileProcessor.writeFile(fileContents, newFileName, context);

                                //myFile を消去する。
                                System.out.println("DeleteFile: " + FileProcessor.deleteFile(context));
                                //todo: updateにする?
                                arrayAdapter.clear();//arrayAdapterからも消去しとこ！
                                listNoFile();
                                //arrayAdapter.notifyDataSetChanged(); ->ファイルを削除してもarrayAdapterは変わっていない
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }else{//アーカイブするファイルがない場合
                    Toast.makeText(getApplicationContext(), R.string.fileDoesNotExist,Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu_delete:
                /*
                ファイルが無ければTOASTを表示
                ファイルがあればAlertDialogを表示、Okボタンが押されたらファイルを削除
                 */
                if(FileProcessor.checkFile(context)) {
                    new AlertDialog.Builder(this)
                            .setTitle("本当に削除しますか？")
                            .setMessage("無くなっちゃうよ")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // OK button pressed
                                boolean delete = FileProcessor.deleteFile(context);//delete file
                                if (!delete) {
                                    //削除に失敗した場合,toastを表示
                                    Toast.makeText(getApplicationContext(), R.string.FileCouldNotBeDeleted, Toast.LENGTH_SHORT).show();
                                } else {
                                    //削除に成功した場合に、listNoFile()を実行
                                    //todo: updateにした方がいい？
                                    arrayAdapter.clear();//listNoFileとは関係ないけどやっとく～
                                    listNoFile();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
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
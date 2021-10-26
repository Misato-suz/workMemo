package com.example.workMemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.Calendar;


/**入力した内容の確認作業を行う画面 */
public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonBack;
    private Button buttonNext;

    private Date date = new Date();
    private String work;
    private String memo;
    private final Calendar c = Calendar.getInstance();  //calendar クラスは必ず初期化！！！！！

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        //Intentを取得
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;

        String dateString = String.valueOf(bundle.getString("date"));//simpleDateFormat: YYYY-MM-dd
        date = java.sql.Date.valueOf(dateString);//Date型に変換
        assert date != null;
        c.setTime(date);//setTextするときにstring.formatできるように（多分）
        work = String.valueOf(bundle.getString("workType"));
        memo = String.valueOf(bundle.getString("memo"));


        TextView textViewDate = findViewById(R.id.textViewDateConfirm);
        TextView textViewWork = findViewById(R.id.textViewWorkConfirm);
        TextView textViewMemo = findViewById(R.id.textViewMemoConfirm);
        buttonBack = findViewById(R.id.buttonBackConfirm);
        buttonNext = findViewById(R.id.buttonNextConfirm);

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        //textViewに取得した文字を入力
        // ↓2021年10月25日
        textViewDate.setText(String.format("%d年%d月%d日", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));
        //textViewDate.setText(String.valueOf(date)); -> 2021-10-25
        textViewWork.setText(work);
        textViewMemo.setText(memo);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if(view == buttonBack){
            finish();
//            Intent intentBackToInput = new Intent(ConfirmActivity.this, InputActivity.class);
//            intentBackToInput.putStringArrayListExtra("hello", arrayList);
//            startActivity(intentBackToInput);
        }else if (view == buttonNext){

            String COMMA = ",";
            String fileContents = date + COMMA + work + COMMA + memo + "\n";

            //ファイルに書き込み
            FileProcessor.writeFile(fileContents, getApplicationContext());

            Intent intentNextMain = new Intent(ConfirmActivity.this, MainActivity.class);
            startActivity(intentNextMain);
        }
    }
}
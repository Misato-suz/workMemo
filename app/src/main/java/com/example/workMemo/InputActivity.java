package com.example.workMemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class InputActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private Spinner spinnerWork;
    private EditText editTextMemo;
    private Button buttonNext;
    private Button buttonBack;
    private TextView textViewDate;

    public final String MONEY_COLLECTION = "集金";
    public final String BANK = "銀行";
    public final String OTHERS = "その他";

    private Calendar c = Calendar.getInstance();
    private Date dateToday = new Date(); //selectedDateより前の時に作る

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        spinnerWork = findViewById(R.id.spinnerWork);
        editTextMemo = findViewById(R.id.editTextTextMultiLine);
        buttonBack = findViewById(R.id.buttonBackInput);
        buttonNext = findViewById(R.id.buttonNextInput);
        textViewDate = findViewById(R.id.textViewDateInput);

        // set spinner
        // TODO: spinner フォントサイズ
        ArrayAdapter<String> arrayAdapterWork = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        arrayAdapterWork.addAll(MONEY_COLLECTION, BANK, OTHERS);
        spinnerWork.setAdapter(arrayAdapterWork);

        //set currentDate to textViewDate
        textViewDate.setText(String.format("%d年%d月%d日", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));

        //set OnClickListener to buttonNext, buttonBack, and textViewDate
        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        textViewDate.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        //ボタンとtextViewDateが押された時の処理
        if (buttonBack.equals(view)) {
            // 戻るボタンでMainActivityに戻る
            Intent intentBack = new Intent(InputActivity.this, MainActivity.class);
            startActivity(intentBack);
        } else if (buttonNext.equals(view)) {
            /* 確定ボタンを押したとき */
            //設定した日付、業務内容、メモをarrayListで取得
            String memo = String.valueOf(editTextMemo.getText());
            String workType = (String)spinnerWork.getSelectedItem();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(c.getTime());
            //もし不適切な入力でなかったら、次のアクティビティへ
            if (checkError()) {
                Intent intentNext = new Intent(InputActivity.this, ConfirmActivity.class);
                intentNext.putExtra("date", date);
                intentNext.putExtra("workType", workType);
                intentNext.putExtra("memo", memo);
                startActivity(intentNext);
            }

        } else if (textViewDate.equals(view)){
            //when textViewDate　is selected, show DatePickerDialog
            DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
            datePicker.show(getSupportFragmentManager(), "datePicker");
        } else{
            throw new IllegalStateException("Unexpected value: " + view);
        }

    }

/**
checkError
戻り値：boolean(true = ダメな値なし、次の画面に遷移できる）
 */
    private boolean checkError() {
        //記入した内容を取得
        String memo = String.valueOf(editTextMemo.getText());

        //日付操作
        Date selectedDate = c.getTime();
        if (selectedDate.after(dateToday)) {
            //その日以降の日付の場合、不当なので次へ進めないようにする
            Toast.makeText(this,"今日以降の日付は入力できません。", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //DatePickerFragment に日付がセットされたときにtextViewに取得した日付を書く
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        c.set(year, month, dayOfMonth);
        textViewDate.setText(String.format("%d年%d月%d日", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));
    }
}
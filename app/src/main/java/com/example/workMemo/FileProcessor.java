package com.example.workMemo;

import android.content.Context;
import android.os.Build;


import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class FileProcessor {
    private static String FILE_NAME = "myFile";


    //Empty constructor
    public FileProcessor(){}

    public static boolean checkFile(String fileName, Context context){
        File file = context.getFileStreamPath(fileName);
        if (file.exists()){
            System.out.println("Checking file: " + fileName + " -> " + context.getString(R.string.FileExists)); //activityクラスを継承していないので"context."が必要になる。
            //context.が無いとint型で出力される。
            return true;
        } else {
            System.out.println("Checking file: " + fileName + " -> " + context.getString(R.string.fileDoesNotExist));
            return false;
        }
    }

    public static boolean checkFile(Context context){
        return checkFile(FILE_NAME, context);
    }

    public static boolean deleteFile(String fileName, Context context){
        File file = context.getFileStreamPath(fileName);
        System.out.println("Deleting file: " + fileName);
        if (file.exists()){
            System.out.println("file existed and it is now deleted.");
            return file.delete();
        } else {
            System.out.println("Could not delete file because file does not exist. ");
            return false;
        }
    }

    public static boolean deleteFile(Context context){
        return deleteFile(FILE_NAME, context);
    }

    //ファイルを読み込んで、システムに出力する。
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static StringBuilder readFile(String fileName, Context context) {
        System.out.println("Reading file: " + fileName);
        StringBuilder stringBuilder = new StringBuilder();  //読み込んだ行を保管する
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();  // 最初の行を読み込む. 読み込み用のString変数
            while (line != null) {
                //読み込んだ行を追加する
                stringBuilder.append(line);
                //次の行読み込み
                line = bufferedReader.readLine();
                if(line != null){
                    //改行コードを追加する
                    //次の行があるときだけ実行されるので、最後の行に改行コードは追加されない
                    stringBuilder.append("\n");
                }
            }
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("The file " + fileName + " was not found.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //読み込んだ行を全部システムに出力(改行なし)
        System.out.println("stringBuilder: " + stringBuilder);
        return stringBuilder;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static StringBuilder readFile(Context context){
        return readFile(FILE_NAME, context);
    }

    public static void writeFile(String fileContents, String fileName, Context context) {
        try {
            System.out.println("Write File");
            //check if the file exists
            FileProcessor.checkFile(fileName, context);

            //private|append mode でファイルを開く
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, MODE_PRIVATE|MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.append(fileContents);

            System.out.println("ファイル書き込み操作完了");

            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file not found is " + fileName);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String fileContents, Context context){
        writeFile(fileContents, FILE_NAME, context);
    }


}

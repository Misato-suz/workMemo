package com.example.workMemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileListFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //このクラス内でだけ参照する値のため、BundleのKEYの値をprivateにする
    private static final String KEY_FILE_NAME = "param1";
    private static final String ARG_PARAM2 = "param2";

    //値をOnCreate　で受け取るため、新規で変数を作成する
    //値がセットされなかった時のために、初期値もセットする
    private String mFileName = "";
    private String mParam2 = "";

    private List<String> items = new ArrayList<>();
    private Button button;

    public FileListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileListFragment.
     */
    //このメソッドからFragmentを作成することを強制する。
    //newInstance メソッドを使用することで、このクラスを作成する際にどのような値が必要になるか制約を設けることができる
    public static FileListFragment newInstance(String param1, String param2) {
        FileListFragment fragment = new FileListFragment();
        //Fragmentに渡す値はBundle型でやり取りする。
        Bundle args = new Bundle();
        //KEY/Pairの形で値をセットする。
        args.putString(KEY_FILE_NAME, param1);
        args.putString(ARG_PARAM2, param2);
        //fragmentに値をセットする。
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundleの値を受け取る際はonCreateメソッド内で行う
        Bundle args = getArguments();
        //Bundle に値がセットされていなかった場合はNullなのでNullCheckを行う。
        if (args != null) {
            mFileName = getArguments().getString(KEY_FILE_NAME);
            mParam2 = getArguments().getString(ARG_PARAM2);
            items.add(mFileName);
            items.add(mParam2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_list, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.listView1);
        button = view.findViewById(R.id.button);

        button.setOnClickListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, items);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onClick(View view) {
        if (view.equals(button)){
            getFragmentManager().popBackStack();
        }
    }
}
/*


 */
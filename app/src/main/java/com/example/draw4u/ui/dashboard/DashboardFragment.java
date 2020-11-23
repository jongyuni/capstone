package com.example.draw4u.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.draw4u.DiaryInfo;
import com.example.draw4u.R;
import com.example.draw4u.ResultDiary;
import com.example.draw4u.SelectImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Button btn_search;
    public EditText searchWord;
    private  MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ResultDiary> diaryInfos = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        searchWord = (EditText)view.findViewById(R.id.searchWord);
        btn_search = (Button)view.findViewById(R.id.btn_search);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new MyAdapter(diaryInfos);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        btn_search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                initDataset(searchWord.getText().toString());
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDataset();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initDataset() {//맨 처음 data set 설정

        db.collection(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {//data 가져오기
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DiaryInfo diaryInfo = document.toObject(DiaryInfo.class);
                                ResultDiary resultDiary = new ResultDiary(diaryInfo);
                                diaryInfos.add(resultDiary);
                            }
                            mAdapter.notifyDataSetChanged();//Dataset Update
                        } else {
                            Toast.makeText(getActivity(), "ERROR!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initDataset(String searchWord) {//검색해서 data set 초기화

        db.collection(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {//data 가져오기
                            diaryInfos.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("diary").toString().contains(searchWord)){
                                    DiaryInfo diaryInfo = document.toObject(DiaryInfo.class);
                                    ResultDiary resultDiary = new ResultDiary(diaryInfo);
                                    diaryInfos.add(resultDiary);
                                }
                                else {
                                    continue;
                                }
                            }
                            mAdapter.notifyDataSetChanged();//Dataset Update
                        } else {
                            Toast.makeText(getActivity(), "ERROR!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}


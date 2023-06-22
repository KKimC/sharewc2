package com.scsa.andr.sharewc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToiletListActivity extends AppCompatActivity {

    private static final String TAG = "scsatest";

    private ListView toiletListView;
    private ToiletAdapter toiletAdapter;
    private List<Toilet> toiletList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_list);

        toiletListView = findViewById(R.id.toiletListView);
        toiletList = new ArrayList<>();
        toiletAdapter = new ToiletAdapter(this, toiletList);
        toiletListView.setAdapter(toiletAdapter);

        toiletListView.setOnItemClickListener((parent, view, position, id) -> {
            // 클릭한 화장실 항목 처리
            Toilet selectedToilet = toiletList.get(position);
            Toast.makeText(ToiletListActivity.this, "선택한 화장실: " + selectedToilet, Toast.LENGTH_SHORT).show();
        });

        // Firebase 데이터베이스에서 화장실 목록 가져오기
        loadToiletList();
    }

    private void loadToiletList() {
        // Firebase 데이터베이스의 "toilets" 노드에서 화장실 목록 가져오기
        Query query = FirebaseDatabase.getInstance().getReference("toilets");

        Log.d(TAG, "loadToiletList: "+query);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toiletList.clear();

                // 데이터 스냅샷을 순회하며 화장실 객체로 변환 후 리스트에 추가
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String aa = snapshot.getKey();
                    Log.d(TAG, "onDataChange: "+aa);
                    snapshot.child("toilets").getKey();
                    Toilet toilet = snapshot.getValue(Toilet.class);
                    Log.d(TAG, "onDataChange: "+toilet);
                    if (toilet != null) {
                        toiletList.add(toilet);
                        Log.d(TAG, "onDataChange: "+toilet);
                    }
                }

                toiletAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ToiletListActivity.this, "데이터베이스에서 화장실 목록을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


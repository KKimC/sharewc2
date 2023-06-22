package com.scsa.andr.sharewc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "scsatest";

    private NfcAdapter nfcAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Places.initialize(getApplicationContext(), "AIzaSyB57oIjMR_KK2l2UPjJqCpBumMfXWQzdok");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show();
        }

        Button registerButton = findViewById(R.id.registerButton);
        Button viewButton = findViewById(R.id.viewButton);

        registerButton.setOnClickListener(v -> {
            // 화장실 등록창으로 이동
            Intent intent = new Intent(MainActivity.this, ToiletRegistrationActivity.class);
            startActivity(intent);
        });

        viewButton.setOnClickListener(v -> {
            // 화장실 조회창으로 이동
            Intent intent = new Intent(MainActivity.this, ToiletListActivity.class);
            startActivity(intent);
        });

        // Firebase 데이터베이스 레퍼런스
        databaseReference = FirebaseDatabase.getInstance().getReference("toilets");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // NFC 어댑터 활성화
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, NfcUtils.createPendingIntent(this), null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // NFC 어댑터 비활성화
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // NFC 태그 감지 시에 처리
        Log.d(TAG, "onNewIntent: tag");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage ndefMessage = NfcUtils.getNdefMessage(intent);
            if (ndefMessage != null) {
                String tagId = new String(ndefMessage.getRecords()[0].getPayload()).substring(3);
                Log.d(TAG, "onNewIntent: " + tagId);
                toggleAvailability(tagId);
            }
        }
    }

    private void toggleAvailability(String tagId) {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String toiletKey = snapshot.getKey();
                    Toilet toilet = snapshot.getValue(Toilet.class);
                    Log.d(TAG, toiletKey + "    " + toilet);
                    if (toilet != null) {
                        boolean newAvailability = !toilet.isAvailability();
                        toilet.setAvailability(newAvailability);
                        databaseReference.child(toiletKey).setValue(toilet);
                        Toast.makeText(MainActivity.this, "Availability toggled: " + newAvailability, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to toggle availability", Toast.LENGTH_SHORT).show();
            }
        });



//
//
//        databaseReference.orderByChild("id").equalTo(tagId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String toiletKey = snapshot.getKey();
//                    Toilet toilet = snapshot.getValue(Toilet.class);
//                    Log.d(TAG, toiletKey + "    " + toilet);
//                    if (toilet != null) {
//                        boolean newAvailability = !toilet.isAvailability();
//                        toilet.setAvailability(newAvailability);
//                        assert toiletKey != null;
//                        databaseReference.child(toiletKey).setValue(toilet);
//                        Toast.makeText(MainActivity.this, "Availability toggled: " + newAvailability, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "Failed to toggle availability", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}








package com.scsa.andr.sharewc;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class ToiletRegistrationActivity extends AppCompatActivity {
    private static final String TAG = "scsatest";

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    private EditText nameEditText;
    private TextView locationEditText;
    private EditText priceEditText;

    private EditText registrationNumberEditText;


    private String address;

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference;

    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_registration);

        nameEditText = findViewById(R.id.nameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        priceEditText = findViewById(R.id.priceEditText);
        Button selectLocationButton = findViewById(R.id.selectLocationButton);
        Button registerButton = findViewById(R.id.registerButton);
        registrationNumberEditText = findViewById(R.id.registrationNumberEditText);

        // Firebase 데이터베이스 레퍼런스
        databaseReference = db.getReference("toilets");

        selectLocationButton.setOnClickListener(v -> openPlacePicker());
        registerButton.setOnClickListener(v -> registerToilet());
    }

    private void openPlacePicker() {
        // Set the fields to be retrieved
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);

        // Set up the autocomplete intent
        Autocomplete.IntentBuilder intentBuilder = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields);
        Intent autocompleteIntent = intentBuilder.build(this);
        startActivityForResult(autocompleteIntent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d(TAG, "onActivityResult: " + place.getAddress());
                address = place.getAddress();
                selectedLocation = place.getLatLng();

                locationEditText.setText(address);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Place picking failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Place picking canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerToilet() {
        String name = nameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String registrationNumber = registrationNumberEditText.getText().toString();


        // 화장실 정보 유효성 검사
        if (name.isEmpty() || address.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceString);

        // 화장실 객체 생성
        Toilet toilet = new Toilet(name, selectedLocation.latitude, selectedLocation.longitude, address, price, registrationNumber);

        // Firebase에 화장실 정보 등록
        String toiletKey = databaseReference.push().getKey();
        assert toiletKey != null;
        databaseReference.child(toiletKey).setValue(toilet)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ToiletRegistrationActivity.this, "Toilet registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ToiletRegistrationActivity.this, "Failed to register toilet", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to register toilet", e);
                });
    }
}

package com.scsa.andr.sharewc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

public class NfcUtils {
    public static NdefMessage getNdefMessage(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null && rawMessages.length > 0) {
                return (NdefMessage) rawMessages[0];
            }
        }
        return null;
    }

    public static String getNfcTagId(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            return ByteArrayUtils.bytesToHex(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
        }
        return null;
    }

    public static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, context.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
    }



}


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private NdefMessage createNdefMessage(String url) {
        NdefRecord ndefRecord = NdefRecord.createUri(url);
        return new NdefMessage(new NdefRecord[]{ndefRecord});
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if (tag == null) {
                Log.e("writeNdefMessage", "Tag object cannot be null");
                return;
            }
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                Log.e("writeNdefMessage", "NDEF is not supported by this Tag.");
                return;
            }
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            ndef.close();
        } catch (Exception e) {
            Log.e("writeNdefMessage", "Write NDEF message failed", e);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage("https://example.com/audio.mp3");
            writeNdefMessage(tag, ndefMessage);
        }
    }
}

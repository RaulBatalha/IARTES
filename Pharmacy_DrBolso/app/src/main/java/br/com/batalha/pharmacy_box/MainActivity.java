package br.com.batalha.pharmacy_box;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button btnScanner, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScanner = findViewById(R.id.btnScanner);
        btnRegister = findViewById(R.id.btnRegister);
        final Activity activity = this;

        btnScanner.setOnClickListener(view -> {
            IntentIntegrator scanner = new IntentIntegrator(activity);
            scanner.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            scanner.setPrompt("Code Reader");
            scanner.setCameraId(0);
            scanner.initiateScan();
        });

        btnRegister.setOnClickListener(view -> {
            Intent nextPage = new Intent(this, RegisterMedicationActivity.class);
            startActivity(nextPage);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult barCode = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (barCode.getContents() != null) {
            String barCodeResult = barCode.getContents();
            showMessage("Barcode Result", barCodeResult);
        } else {
            alert("Barcode Reader Canceled");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    private void alert(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
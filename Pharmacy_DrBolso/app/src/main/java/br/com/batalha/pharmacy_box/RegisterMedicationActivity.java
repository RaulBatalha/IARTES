package br.com.batalha.pharmacy_box;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.EmptyStackException;

public class RegisterMedicationActivity extends AppCompatActivity {
    Button btnInsertData, btnDeleteData, btnUpdateData, btnReadData;
    EditText textBarcode, textDescription, textNameRemedy, textDateBuy, textDueDate;
    ImageButton btnImageBarcode;
    String barCodeResult;

    DatabaseMedication myDB;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_medication);

        myDB = new DatabaseMedication(this);  // created object of DatabaseMedication class
        myDB.getWritableDatabase(); // for checking db is created or not.

        btnInsertData = findViewById(R.id.btnInsertData);
        btnDeleteData = findViewById(R.id.btnDeleteData);
        btnUpdateData = findViewById(R.id.btnUpdateData);
        btnReadData = findViewById(R.id.btnReadData);
        btnImageBarcode = findViewById(R.id.btnImageBarcode);

        textBarcode = findViewById(R.id.textBarcode);
        textNameRemedy = findViewById(R.id.textNameRemedy);
        textDescription = findViewById(R.id.textDescription);
        textDateBuy = findViewById(R.id.textBuyDate);
        textDueDate = findViewById(R.id.textDueDate);

        final Activity registerMedication = this;

        btnImageBarcode.setOnClickListener(view -> {
            IntentIntegrator scanner = new IntentIntegrator(registerMedication);
            scanner.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            scanner.setPrompt("Code Reader");
            scanner.setCameraId(0);
            scanner.initiateScan();
        });

        btnInsertData.setOnClickListener(view -> {
            // Check emptiness of edit boxes
            if (textBarcode.getText().toString().isEmpty() ||
                    textNameRemedy.getText().toString().isEmpty() ||
                    textDescription.getText().toString().isEmpty() ||
                    textDateBuy.getText().toString().isEmpty() ||
                    textDueDate.getText().toString().isEmpty()) {
                alert ("Please fill all fields to register!");
                return;
            }

            // Insert data into database
            boolean isInserted = myDB.insertData(
                    textBarcode.getText().toString(),
                    textNameRemedy.getText().toString(),
                    textDescription.getText().toString(),
                    textDateBuy.getText().toString(),
                    textDueDate.getText().toString()
            );

            // Show toast when data inserted successfully
            if (isInserted) {
                Toast.makeText(RegisterMedicationActivity.this,
                        "Data inserted in the database!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterMedicationActivity.this,
                        "Data not inserted in the database!", Toast.LENGTH_SHORT).show();
            }

            // After inserting make edit box empty
            textBarcode.setText("");
            textNameRemedy.setText("");
            textDescription.setText("");
            textDateBuy.setText("");
            textDueDate.setText("");
        });

        btnReadData.setOnClickListener(view -> {
            Cursor cursor = myDB.getAllData();
            if (cursor.getCount() == 0) {
                showMessage("Error", "No data was found in the database!");
                textBarcode.setText("");
                textNameRemedy.setText("");
                textDescription.setText("");
                textDateBuy.setText("");
                textDueDate.setText("");
                return;
            }

            StringBuffer dataInformation = new StringBuffer();
            while (cursor.moveToNext()) {
                dataInformation.append("Barcode: " + cursor.getString(1) + "\n");
                dataInformation.append("Name Remedy: " + cursor.getString(2) + "\n");
                dataInformation.append("Description: " + cursor.getString(3) + "\n");
                dataInformation.append("Date Buy: " + cursor.getString(4) + "\n");
                dataInformation.append("Due Date: " + cursor.getString(5) + "\n\n");
            }
            showMessage("Information Remedy", dataInformation.toString());

        });

        btnUpdateData.setOnClickListener(view -> {
            // Check emptiness of edit boxes
            if (textBarcode.getText().toString().isEmpty() ||
                    textNameRemedy.getText().toString().isEmpty() ||
                    textDescription.getText().toString().isEmpty() ||
                    textDateBuy.getText().toString().isEmpty() ||
                    textDueDate.getText().toString().isEmpty()) {
                showMessage("Error", "Please fill the all fields to updating!");
                return;
            }
            boolean isUpdated = myDB.updateData(
                    textBarcode.getText().toString(),
                    textBarcode.getText().toString(),
                    textNameRemedy.getText().toString(),
                    textDescription.getText().toString(),
                    textDateBuy.getText().toString(),
                    textDueDate.getText().toString());
            if (isUpdated) {
                Toast.makeText(RegisterMedicationActivity.this, "Data updated in the database!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterMedicationActivity.this,
                        "Data not updated in the database!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteData.setOnClickListener(view -> {
            Integer isDeleted = myDB.deleteData(
                    textBarcode.getText().toString());
            if (isDeleted > 0) {
                Toast.makeText(RegisterMedicationActivity.this,
                        "Data deleted in the database!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterMedicationActivity.this,
                        "Data not deleted in the database!", Toast.LENGTH_SHORT).show();
            }
            textBarcode.setText("");
            textNameRemedy.setText("");
            textDescription.setText("");
            textDateBuy.setText("");
            textDueDate.setText("");
        });
    }

    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult barCode = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (barCode.getContents() != null) {
            barCodeResult = barCode.getContents();
            textBarcode.setText(barCodeResult);
        } else {
            alert("Barcode Reader Canceled");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert(String messageToast) {
        Toast.makeText(getApplicationContext(), messageToast, Toast.LENGTH_LONG).show();
    }
}
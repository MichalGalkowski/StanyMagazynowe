package pl.ara.stanymagazynowe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProduct extends AppCompatActivity {

    private EditText nameET, amountET, lowAmountET;
    private Button cancelBtn, addBtn;
    private String name;
    private int amount, lowAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

//        FIND VIEW

        nameET = findViewById(R.id.addProductName);
        amountET = findViewById(R.id.addAmount);
        lowAmountET = findViewById(R.id.addLowAmount);
        cancelBtn = findViewById(R.id.cancelAddBtn);
        addBtn = findViewById(R.id.addProductBtn);

//        ONCLICK

        cancelBtn.setOnClickListener(v -> {

            Toast.makeText(AddProduct.this, "Tworzenie produktu anulowane", Toast.LENGTH_SHORT).show();
            finish();

        });

        addBtn.setOnClickListener(v -> {

            if(nameET.getText().toString().isEmpty() || amountET.getText().toString().isEmpty() || lowAmountET.getText().toString().isEmpty()) {
                Toast.makeText(AddProduct.this, "Pola nie mogą być puste", Toast.LENGTH_SHORT).show();
            }
            else {
                name = nameET.getText().toString().trim();
                amount = Integer.parseInt(amountET.getText().toString());
                lowAmount = Integer.parseInt(lowAmountET.getText().toString());

                Intent intent = new Intent();
                intent.putExtra("productName", name);
                intent.putExtra("productAmount", amount);
                intent.putExtra("productLowAmount", lowAmount);
                setResult(RESULT_OK, intent);
                finish();
            }



        });
    }
}
package pl.ara.stanymagazynowe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProduct extends AppCompatActivity {

    private EditText nameET, amountET, lowAmountET;
    private Button cancelBtn, updateBtn;
    private String name;
    private int id, amount, lowAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

//        FINDVIEW

        nameET = findViewById(R.id.addProductionProductAmountET);
        amountET = findViewById(R.id.updatePPnameTV);
        lowAmountET = findViewById(R.id.updatePPamount);
        cancelBtn = findViewById(R.id.cancelUpdateProductionProductBtn);
        updateBtn = findViewById(R.id.updateProductionProductBtn);

        getData();

//        ONCLICK

        cancelBtn.setOnClickListener(v -> {
            Toast.makeText(UpdateProduct.this, "Brak aktualizacji", Toast.LENGTH_SHORT).show();
            finish();
        });

        updateBtn.setOnClickListener(v -> {
            updateProduct();
        });
    }

    public void updateProduct() {

        if(nameET.getText().toString().isEmpty() || amountET.getText().toString().isEmpty() || lowAmountET.getText().toString().isEmpty()) {
            Toast.makeText(UpdateProduct.this, "Pola nie mogą być puste", Toast.LENGTH_SHORT).show();
        }
        else {
            name = nameET.getText().toString().trim();
            amount = Integer.parseInt(amountET.getText().toString());
            lowAmount = Integer.parseInt(lowAmountET.getText().toString());

            Intent intent = new Intent();
            intent.putExtra("updateName", name);
            intent.putExtra("updateAmount", amount);
            intent.putExtra("updateLowAmount", lowAmount);

            if(id!=-1) {
                intent.putExtra("updateId", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }


    }

    public void getData() {
        Intent i = getIntent();
        id = i.getIntExtra("updateId", -1);
        name = i.getStringExtra("updateName");
        amount = i.getIntExtra("updateAmount", -1);
        lowAmount = i.getIntExtra("updateLowAmount", -1);

        nameET.setText(name);
        amountET.setText(String.valueOf(amount));
        lowAmountET.setText(String.valueOf(lowAmount));

    }
}
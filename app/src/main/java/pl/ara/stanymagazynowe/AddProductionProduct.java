package pl.ara.stanymagazynowe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddProductionProduct extends AppCompatActivity {

    private TextView nameTV;
    private EditText amountET;
    private Button cancel, add;
    private String name;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_production_product);

//        FINDVIEW

        nameTV = findViewById(R.id.productionProductNameTV);
        amountET = findViewById(R.id.addProductionProductAmountET);
        cancel = findViewById(R.id.cancelUpdateProductionProductBtn);
        add = findViewById(R.id.updateProductionProductBtn);

        getData();

//        ONCLICK

        cancel.setOnClickListener(v -> {
            Toast.makeText(AddProductionProduct.this, "Nie dodałeś produktu", Toast.LENGTH_SHORT).show();
            finish();
        });

        add.setOnClickListener(v -> {

            if(amountET.getText().toString().isEmpty()) {
                Toast.makeText(AddProductionProduct.this, "Podaj ilość", Toast.LENGTH_SHORT).show();
            }
            else {
                addProduct();
            }

        });
    }

    public void addProduct() {
        amount = Integer.parseInt(amountET.getText().toString());

        Intent i = new Intent();
        i.putExtra("productionProductName", name);
        i.putExtra("productionProductAmount", amount);
        setResult(RESULT_OK, i);
        finish();

    }

    public void getData() {

        Intent i = getIntent();
        name = i.getStringExtra("productionProductName");
        nameTV.setText(name);
    }
}
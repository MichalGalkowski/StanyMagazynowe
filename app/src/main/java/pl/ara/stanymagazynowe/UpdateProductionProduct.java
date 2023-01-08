package pl.ara.stanymagazynowe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProductionProduct extends AppCompatActivity {

    private TextView productName;
    private EditText productAmount;
    private Button cancelBtn, updateBtn;
    private String name;
    private int id, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_production_product);

//        FINDVIEW

        productName = findViewById(R.id.updatePPnameTV);
        productAmount = findViewById(R.id.updatePPamount);
        cancelBtn = findViewById(R.id.cancelUpdateProductionProductBtn);
        updateBtn = findViewById(R.id.updateProductionProductBtn);

        getPPdata();

//        ONCLICK

        cancelBtn.setOnClickListener(v -> {

            Toast.makeText(UpdateProductionProduct.this, "Brak aktualizacji", Toast.LENGTH_SHORT).show();
            finish();
        });

        updateBtn.setOnClickListener(v -> {

            updateProductionProduct();
        });
    }

    public void updateProductionProduct() {

        if(productAmount.getText().toString().isEmpty()) {
            Toast.makeText(UpdateProductionProduct.this, "Podaj ilość", Toast.LENGTH_SHORT).show();
        }

        else {
            amount = Integer.parseInt(productAmount.getText().toString());

            Intent i = new Intent();
            i.putExtra("updatePPname", name);
            i.putExtra("updatePPamount", amount);
            if(id!=-1) {
                i.putExtra("updatePPid", id);
                setResult(RESULT_OK, i);
                finish();
            }
        }

    }

    public void getPPdata() {
        Intent i = getIntent();
        id = i.getIntExtra("updatePPid", -1);
        name = i.getStringExtra("updatePPname");
        amount = i.getIntExtra("updatePPamount", -1);

        productName.setText(name);
        productAmount.setText(String.valueOf(amount));
    }
}
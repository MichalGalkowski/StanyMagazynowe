package pl.ara.stanymagazynowe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button productsBtn, productionsBtn, lowAmountBtn, ordersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FIND VIEW

        productsBtn = findViewById(R.id.products);
        productionsBtn = findViewById(R.id.productions);
        lowAmountBtn = findViewById(R.id.lowAmount);
        ordersBtn = findViewById(R.id.orders);

//        ONCLICK

        productsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Products.class);
            startActivity(intent);
        });

        productionsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProduction.class);
            startActivity(intent);
        });

        lowAmountBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LowAmount.class);
            startActivity(intent);
        });

        ordersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Orders.class);
            startActivity(intent);
        });

    }
}
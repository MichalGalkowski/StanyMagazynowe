package pl.ara.stanymagazynowe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddProduction extends AppCompatActivity {

    private RecyclerView productsRV;
    private Button cancelBtn, viewProductionBtn;
    private ProductVM productVM;
    private ProductionProductVM productionProductVM;
    private EditText search;
    private AddProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private List<ProductionProduct> productionProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_production);

//        FIND VIEW

        productsRV = findViewById(R.id.addProductsRV);
        cancelBtn = findViewById(R.id.cancelAddProductionBtn);
        viewProductionBtn = findViewById(R.id.viewProductionBtn);
        search = findViewById(R.id.searchProductionProductET);

//        SEARCH

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

//        RECYCLERVIEW

        productsRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddProductAdapter();
        productsRV.setAdapter(adapter);

//        VIEWMODEL

        productVM = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ProductVM.class);
        productVM.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
                productList = products;
            }
        });

        productionProductVM = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ProductionProductVM.class);
        productionProductVM.getAllProductionProducts().observe(this, new Observer<List<ProductionProduct>>() {
            @Override
            public void onChanged(List<ProductionProduct> productionProducts) {
                productionProductList = productionProducts;
            }
        });

        adapter.setOnItemClickListener(new AddProductAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Product product) {

                Intent i = new Intent(AddProduction.this, AddProductionProduct.class);
                i.putExtra("productionProductName", product.getName());

                startActivityForResult(i, 4);
            }
        });

        cancelBtn.setOnClickListener(v -> {

            Intent i = new Intent(AddProduction.this, MainActivity.class);
            Toast.makeText(AddProduction.this, "Tworzenie produkcji anulowane", Toast.LENGTH_SHORT).show();
            startActivity(i);

        });

        viewProductionBtn.setOnClickListener(v -> {

            Intent i = new Intent(AddProduction.this, ViewProduction.class);
            startActivity(i);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("productionProductName");
            int amount = data.getIntExtra("productionProductAmount", -1);

            ProductionProduct productionProduct = new ProductionProduct(name, amount);

            Boolean bool = false;
            for(int i=0; i<productionProductList.size();i++) {
                ProductionProduct currentProduct = productionProductList.get(i);
                String pPname = currentProduct.getName();
                int pPamount = currentProduct.getAmount();
                int pPid = currentProduct.getId();
                if(name.equals(pPname)) {
                    bool = true;
                    amount = amount + pPamount;
                    ProductionProduct product = new ProductionProduct(pPname, amount);
                    product.setId(pPid);
                    productionProductVM.update(product);
                }

            }
            if(!bool) {
                productionProductVM.insert(productionProduct);
                Toast.makeText(AddProduction.this, "Produkt dodany do produkcji", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AddProduction.this, "Produkt zaktualizowany", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void filter(String text) {

        List<Product> filteredList = new ArrayList<>();

        for(Product product: productList) {
            if(product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        adapter.filterList(filteredList);
    }
}
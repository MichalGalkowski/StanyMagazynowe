package pl.ara.stanymagazynowe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity {

    private RecyclerView productsRV;
    private Button cancelBtn, addProductBtn;
    private ProductVM productVM;
    private AlertDialog.Builder builder;
    private EditText search;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private ImageView pdfBtn;
    private List<String> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

//        FIND VIEW

        productsRV = findViewById(R.id.productsRV);
        cancelBtn = findViewById(R.id.backProductsBtn);
        addProductBtn = findViewById(R.id.createProductBtn);
        pdfBtn = findViewById(R.id.productPdfBtn);
        search = findViewById(R.id.searchProductET);
        builder = new AlertDialog.Builder(this);

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
        adapter = new ProductAdapter();
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                builder.setTitle("Usuwanie produktu")
                        .setMessage("Usunąć produkt")
                        .setCancelable(true)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productVM.delete(adapter.getProducts(viewHolder.getAdapterPosition()));
                                Toast.makeText(Products.this, "Produkt usunięty", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();


            }
        }).attachToRecyclerView(productsRV);

        adapter.setOnItemClickListener(new ProductAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Product product) {

                Intent intent = new Intent(Products.this, UpdateProduct.class);
                intent.putExtra("updateId", product.getId());
                intent.putExtra("updateName", product.getName());
                intent.putExtra("updateAmount", product.getAmount());
                intent.putExtra("updateLowAmount", product.getLowAmount());

                startActivityForResult(intent, 2);
            }
        });

//        ONCLICK

        cancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Products.this, MainActivity.class);
            startActivity(intent);
        });

        addProductBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Products.this, AddProduct.class);
            startActivityForResult(intent, 1);
        });

//        PDF

        ActivityCompat.requestPermissions(Products.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        pdfBtn.setOnClickListener(v -> {

            for(int i=0; i<productList.size(); i++) {


                Product currentProduct = productList.get(i);
                String product = currentProduct.getName() + " - ilość: " + currentProduct.getAmount();
                allProducts.add(product);
            }
            createPDF(allProducts);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("productName");
            int amount = data.getIntExtra("productAmount", -1);
            int lowAmount = data.getIntExtra("productLowAmount", -1);

            Product product = new Product(name, amount, lowAmount);
            productVM.insert(product);
            Toast.makeText(Products.this, "Dodałeś nowy produkt", Toast.LENGTH_SHORT).show();
        }

        if(requestCode == 2 && resultCode == RESULT_OK) {
            int id = data.getIntExtra("updateId", -1);
            String name = data.getStringExtra("updateName");
            int amount = data.getIntExtra("updateAmount", -1);
            int lowAmount = data.getIntExtra("updateLowAmount", -1);

            Product product = new Product(name, amount, lowAmount);
            product.setId(id);
            productVM.update(product);
            Toast.makeText(Products.this, "Zaktualizowałeś produkt", Toast.LENGTH_SHORT).show();
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

    public void createPDF(List<String> s) {

        PdfDocument productsPdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = productsPdf.startPage(pageInfo);
        Paint paint = new Paint();
        paint.setTextSize(14);
        int x=32, y=32;

        for(String line: s) {
            page.getCanvas().drawText(line, x, y, paint);
            y+=paint.descent()-paint.ascent();
        }

        productsPdf.finishPage(page);

        String filePath = Environment.getExternalStorageDirectory().getPath() + "/allProductsFile.pdf";
        File file = new File(filePath);

        try {
            productsPdf.writeTo(new FileOutputStream(file));
            Toast.makeText(Products.this, "Utworzono listę wszystkich produktów", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Products.this, "ERROR", Toast.LENGTH_SHORT).show();
        }

        productsPdf.close();

    }
}
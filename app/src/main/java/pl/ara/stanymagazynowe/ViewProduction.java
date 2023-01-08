package pl.ara.stanymagazynowe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ViewProduction extends AppCompatActivity {

    private RecyclerView viewProductionRV;
    private Button cancel, save;
    private EditText productionName;
    private ProductionProductVM productionProductVM;
    private ProductVM productVM;
    private AlertDialog.Builder builder;
    private String productionNameS;
    private List<Product> products = new ArrayList<>();
    private List<ProductionProduct> productionProducts = new ArrayList<>();
    private List<String> allProducts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_production);

//        FINDVIEW

        productionName = findViewById(R.id.productionNameET);
        viewProductionRV = findViewById(R.id.viewProductionRV);
        cancel = findViewById(R.id.backProductionViewBtn);
        save = findViewById(R.id.saveProductionBtn);
        builder = new AlertDialog.Builder(this);

//        RECYCLERVIEW

        viewProductionRV.setLayoutManager(new LinearLayoutManager(this));
        ViewProductionAdapter adapter = new ViewProductionAdapter();
        viewProductionRV.setAdapter(adapter);

        //        VIEWMODEL

        productVM = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ProductVM.class);
        productVM.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productsVM) {
                products = productsVM;
            }
        });
        productionProductVM = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ProductionProductVM.class);
        productionProductVM.getAllProductionProducts().observe(this, new Observer<List<ProductionProduct>>() {
            @Override
            public void onChanged(List<ProductionProduct> products) {
                productionProducts = products;
                adapter.setProducts(products);
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
                        .setMessage("Usunąć produkt z produkcji?")
                        .setCancelable(true)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productionProductVM.delete(adapter.getProducts(viewHolder.getAdapterPosition()));
                                Toast.makeText(ViewProduction.this, "Produkt usunięty z produkcji", Toast.LENGTH_SHORT).show();
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
        }).attachToRecyclerView(viewProductionRV);

        adapter.setOnItemClickListener(new ViewProductionAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ProductionProduct productionProduct) {

                Intent i = new Intent(ViewProduction.this, UpdateProductionProduct.class);
                i.putExtra("updatePPid", productionProduct.getId());
                i.putExtra("updatePPname", productionProduct.getName());
                i.putExtra("updatePPamount", productionProduct.getAmount());

                startActivityForResult(i, 5);
            }
        });

//        ONCLICK

        cancel.setOnClickListener(v -> {
            Intent i = new Intent(ViewProduction.this, AddProduction.class);
            startActivity(i);
        });

        save.setOnClickListener(v -> {

            if(productionName.getText().toString().isEmpty()) {
                Toast.makeText(ViewProduction.this, "Wpisz nazwę produkcji", Toast.LENGTH_SHORT).show();
            }
            else {
                saveProduction();
            }
        });
    }

    public void saveProduction() {

        int pPsize = productionProducts.size();
        int pSize = products.size();

        for(int i = 0; i < pPsize; i++) {

            ProductionProduct currentpP = productionProducts.get(i);
            String pPname = currentpP.getName();
            int pPamount = currentpP.getAmount();

            String currentProduct = pPname + " - ilość: " + pPamount;
            allProducts.add(currentProduct);


            for(int y=0; y < pSize; y++) {

                Product currentP = products.get(y);
                String pName = currentP.getName();
                int pAmount = currentP.getAmount();
                int pLowAmount = currentP.getLowAmount();
                int pID = currentP.getId();

                if(pName.equals(pPname)) {
                    pAmount = pAmount - pPamount;

                    Product product = new Product(pName, pAmount, pLowAmount);
                    product.setId(pID);
                    productVM.update(product);
                    productionProductVM.delete(currentpP);
                }
            }

            createProductionPDF(allProducts);
        }
    }

    public void createProductionPDF(List<String> s) {

        productionNameS = productionName.getText().toString();
        String emptyLine = "";
        PdfDocument productsPdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = productsPdf.startPage(pageInfo);
        Paint paint = new Paint();
        paint.setTextSize(14);
        int x=32, y=32;
        Paint paintBigBold = new Paint();
        paintBigBold.setTextSize(18);
        paintBigBold.setFakeBoldText(true);

        page.getCanvas().drawText(productionNameS, 8, y, paintBigBold);
        y+=paint.descent()-paint.ascent();
        page.getCanvas().drawText(emptyLine, x, y, paint);
        y+=paint.descent()-paint.ascent();

        for(String line: s) {
            page.getCanvas().drawText(line, x, y, paint);
            y+=paint.descent()-paint.ascent();
            page.getCanvas().drawText(emptyLine, x, y, paint);
            y+=paint.descent()-paint.ascent();
        }

        productsPdf.finishPage(page);

        String filePath = Environment.getExternalStorageDirectory().getPath() + "/PRODUCTIONS/" + productionNameS + ".pdf";
        File file = new File(filePath);

        try {
            productsPdf.writeTo(new FileOutputStream(file));
            Toast.makeText(ViewProduction.this, "Utworzono listę produktów na produkcji", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ViewProduction.this, "ERROR", Toast.LENGTH_SHORT).show();
        }

        productsPdf.close();

        Intent i = new Intent(ViewProduction.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 5 && resultCode == RESULT_OK) {
            int id = data.getIntExtra("updatePPid", -1);
            String name = data.getStringExtra("updatePPname");
            int amount = data.getIntExtra("updatePPamount", -1);

            ProductionProduct productionProduct = new ProductionProduct(name, amount);
            productionProduct.setId(id);
            productionProductVM.update(productionProduct);
            Toast.makeText(ViewProduction.this, "Zaktualizowałeś produkt", Toast.LENGTH_SHORT).show();
        }
    }
}
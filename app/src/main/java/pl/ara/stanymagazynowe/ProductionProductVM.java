package pl.ara.stanymagazynowe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductionProductVM extends AndroidViewModel {

    private ProductionProductRepository repository;
    private LiveData<List<ProductionProduct>> productionProducts;

    public ProductionProductVM(@NonNull Application application) {
        super(application);

        repository = new ProductionProductRepository(application);
        productionProducts = repository.getAllProductionProducts();
    }

    public void insert(ProductionProduct productionProduct) {repository.insert(productionProduct);}
    public void update(ProductionProduct productionProduct) {repository.update(productionProduct);}
    public void delete(ProductionProduct productionProduct) {repository.delete(productionProduct);}

    public LiveData<List<ProductionProduct>> getAllProductionProducts() {return productionProducts;}
}

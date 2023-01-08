package pl.ara.stanymagazynowe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductVM extends AndroidViewModel {

    private ProductRepository repository;
    private LiveData<List<Product>> products;

    public ProductVM(@NonNull Application application) {
        super(application);

        repository = new ProductRepository(application);
        products = repository.getAllProducts();
    }

    public void insert(Product product) {repository.insert(product);}
    public void update(Product product) {repository.update(product);}
    public void delete(Product product) {repository.delete(product);}

    public LiveData<List<Product>> getAllProducts() {return products;}

}

package pl.ara.stanymagazynowe;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> products;

    public ProductRepository(Application app) {
        ProductDB db = ProductDB.getInstance(app);
        productDao = db.productDao();
        products = productDao.GetAllProducts();
    }

    public void insert(Product product) {
        new InsertProductAsyncTask(productDao).execute(product);

    }

    public void update(Product product) {
        new UpdateProductAsyncTask(productDao).execute(product);

    }

    public void delete(Product product) {
        new DeleteProductAsyncTask(productDao).execute(product);

    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao) {this.productDao = productDao;}

        @Override
        protected Void doInBackground(Product... products) {
            productDao.Insert(products[0]);
            return null;
        }
    }

    private static class UpdateProductAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao productDao;

        private UpdateProductAsyncTask(ProductDao productDao) {this.productDao = productDao;}

        @Override
        protected Void doInBackground(Product... products) {
            productDao.Update(products[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao productDao;

        private DeleteProductAsyncTask(ProductDao productDao) {this.productDao = productDao;}

        @Override
        protected Void doInBackground(Product... products) {
            productDao.Delete(products[0]);
            return null;
        }
    }
}

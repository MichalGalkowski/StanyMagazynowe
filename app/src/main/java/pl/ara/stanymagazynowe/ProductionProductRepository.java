package pl.ara.stanymagazynowe;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductionProductRepository {

    private ProductionProductDao productionProductDao;
    private LiveData<List<ProductionProduct>> productionProducts;

    public ProductionProductRepository(Application app) {
        ProductionProductDB db = ProductionProductDB.getInstance(app);
        productionProductDao = db.productionProductDao();
        productionProducts = productionProductDao.GetAllProductionProducts();
    }

    public void insert(ProductionProduct productionProduct) {
        new ProductionProductRepository.InsertProductionProductAsyncTask(productionProductDao).execute(productionProduct);

    }

    public void update(ProductionProduct productionProduct) {
        new ProductionProductRepository.UpdateProductionProductAsyncTask(productionProductDao).execute(productionProduct);

    }

    public void delete(ProductionProduct productionProduct) {
        new ProductionProductRepository.DeleteProductionProductAsyncTask(productionProductDao).execute(productionProduct);

    }

    public LiveData<List<ProductionProduct>> getAllProductionProducts() {
        return productionProducts;
    }

    private static class InsertProductionProductAsyncTask extends AsyncTask<ProductionProduct, Void, Void> {

        private ProductionProductDao productionProductDao;

        private InsertProductionProductAsyncTask(ProductionProductDao productionProductDao) {this.productionProductDao = productionProductDao;}

        @Override
        protected Void doInBackground(ProductionProduct... productionProducts) {
            productionProductDao.Insert(productionProducts[0]);
            return null;
        }
    }

    private static class UpdateProductionProductAsyncTask extends AsyncTask<ProductionProduct, Void, Void> {

        private ProductionProductDao productionProductDao;

        private UpdateProductionProductAsyncTask(ProductionProductDao productionProductDao) {this.productionProductDao = productionProductDao;}

        @Override
        protected Void doInBackground(ProductionProduct... productionProducts) {
            productionProductDao.Update(productionProducts[0]);
            return null;
        }
    }

    private static class DeleteProductionProductAsyncTask extends AsyncTask<ProductionProduct, Void, Void> {

        private ProductionProductDao productionProductDao;

        private DeleteProductionProductAsyncTask(ProductionProductDao productionProductDao) {this.productionProductDao = productionProductDao;}

        @Override
        protected Void doInBackground(ProductionProduct... productionProducts) {
            productionProductDao.Delete(productionProducts[0]);
            return null;
        }
    }
}

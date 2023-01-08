package pl.ara.stanymagazynowe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductionProductDao {

    @Insert
    void Insert(ProductionProduct productionProduct);
    @Update
    void Update(ProductionProduct productionProduct);
    @Delete
    void Delete(ProductionProduct productionProduct);

    @Query("SELECT * FROM productionProductTable ORDER BY id ASC ")
    LiveData<List<ProductionProduct>> GetAllProductionProducts();
}

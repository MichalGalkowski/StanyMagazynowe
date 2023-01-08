package pl.ara.stanymagazynowe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void Insert(Product product);
    @Update
    void Update(Product product);
    @Delete
    void Delete(Product product);

    @Query("SELECT * FROM productTable ORDER BY id ASC ")
    LiveData<List<Product>> GetAllProducts();
}

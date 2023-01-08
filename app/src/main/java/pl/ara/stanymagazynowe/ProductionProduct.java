package pl.ara.stanymagazynowe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productionProductTable")
public class ProductionProduct {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int amount;

    public ProductionProduct(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }
}

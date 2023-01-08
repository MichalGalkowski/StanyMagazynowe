package pl.ara.stanymagazynowe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productTable")
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int amount;
    public int lowAmount;

    public Product(String name, int amount, int lowAmount) {
        this.name = name;
        this.amount = amount;
        this.lowAmount = lowAmount;
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

    public int getLowAmount() {
        return lowAmount;
    }

    public void setId(int id) {
        this.id = id;
    }
}

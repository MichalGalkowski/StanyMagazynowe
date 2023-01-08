package pl.ara.stanymagazynowe;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = ProductionProduct.class, version = 1)
public abstract class ProductionProductDB extends RoomDatabase {

    private static ProductionProductDB instance;
    public abstract ProductionProductDao productionProductDao();
    public static synchronized ProductionProductDB getInstance(Context ctx) {
        if(instance == null) {
            instance = Room.databaseBuilder(ctx.getApplicationContext(), ProductionProductDB.class, "productionProductDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new ProductionProductDB.PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductionProductDao productionProductDao;

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        private PopulateDbAsyncTask(ProductionProductDB db) {
            productionProductDao = db.productionProductDao();
        }
    }
}

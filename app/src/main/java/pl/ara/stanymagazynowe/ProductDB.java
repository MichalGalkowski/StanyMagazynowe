package pl.ara.stanymagazynowe;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Product.class, version=1)
public abstract class ProductDB extends RoomDatabase {

    private static ProductDB instance;
    public abstract ProductDao productDao();
    public static synchronized ProductDB getInstance(Context ctx) {
        if(instance == null) {
            instance = Room.databaseBuilder(ctx.getApplicationContext(), ProductDB.class, "productDB")
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

            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductDao productDao;

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        private PopulateDbAsyncTask(ProductDB db) {
            productDao = db.productDao();
        }
    }
}

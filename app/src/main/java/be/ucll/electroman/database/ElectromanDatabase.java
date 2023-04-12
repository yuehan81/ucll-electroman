package be.ucll.electroman.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.ucll.electroman.bootstrap.Bootstrap;
import be.ucll.electroman.models.User;
import be.ucll.electroman.models.WorkOrder;


@Database(entities = {User.class, WorkOrder.class}, version = 1, exportSchema = false)
public abstract class ElectromanDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WorkOrderDao workOrderDao();

    private static volatile ElectromanDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ElectromanDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ElectromanDatabase.class) {
                if (INSTANCE == null) {
                    // first delete database
                    deleteDatabase(context);
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ElectromanDatabase.class, "electroman_database.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            getDatabase(context).userDao().insertAll(Bootstrap.populateUsers());
                                            getDatabase(context).workOrderDao().insertAll(Bootstrap.populateWorkOrdersUser1());
                                            getDatabase(context).workOrderDao().insertAll(Bootstrap.populateWorkOrdersUser2());

                                        }
                                    });
                                }
                            })
                            .build();



                }
            }
        }

        return INSTANCE;
    }

    public static void deleteDatabase(final Context context) {
        context.deleteDatabase("electroman_database.db");
    }

}


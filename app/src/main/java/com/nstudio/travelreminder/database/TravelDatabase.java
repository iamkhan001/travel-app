package com.nstudio.travelreminder.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.nstudio.travelreminder.database.dao.LuggageDao;
import com.nstudio.travelreminder.database.dao.TravelDao;
import com.nstudio.travelreminder.database.entitiy.Luggage;
import com.nstudio.travelreminder.database.entitiy.Travel;

/**
 * Created by ravi on 05/02/18.
 */

@Database(entities = {Travel.class, Luggage.class}, version = 1, exportSchema = false)
public abstract class TravelDatabase extends RoomDatabase {

    public  abstract TravelDao travelDao();
    public  abstract LuggageDao luggageDao();

    private static TravelDatabase INSTANCE;

    public static TravelDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TravelDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TravelDatabase.class, "travel_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
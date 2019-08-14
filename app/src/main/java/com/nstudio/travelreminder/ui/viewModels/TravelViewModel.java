package com.nstudio.travelreminder.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.nstudio.travelreminder.database.AppDatabase;
import com.nstudio.travelreminder.database.TravelRepository;
import com.nstudio.travelreminder.database.dao.TravelDao;
import com.nstudio.travelreminder.database.entitiy.Travel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TravelViewModel extends AndroidViewModel {

    private TravelRepository travelRepository;
    private LiveData<List<Travel>> travels;

    public TravelViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        TravelDao travelDao = db.travelDao();
        travels = travelDao.getAllTravels();
        travelRepository = new TravelRepository(db,travelDao,travels);
    }

    public LiveData<List<Travel>> getTravels() {
        return travels;
    }

    public void addTravel(Travel travel){
        travelRepository.insertTravel(travel);
    }

    public Travel getTravel(int id){
        try {
            return travelRepository.getTravel(id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateTravel(Travel travel){
        travelRepository.updateTravel(travel);
    }

    public void deleteTravel(Travel travel){
        travelRepository.deleteTravel(travel);
    }

}

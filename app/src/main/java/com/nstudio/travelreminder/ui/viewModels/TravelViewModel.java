package com.nstudio.travelreminder.ui.viewModels;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.nstudio.travelreminder.data.TravelData;
import com.nstudio.travelreminder.database.TravelRepository;
import com.nstudio.travelreminder.database.entitiy.Luggage;
import com.nstudio.travelreminder.database.entitiy.Travel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TravelViewModel extends AndroidViewModel {

    private TravelRepository travelRepository;
    private MutableLiveData<List<TravelData>> travels;

    TravelViewModel(@NonNull Application application) {
        super(application);
        travels = new MutableLiveData<>();
        OnDataChangeListener changeListener = new OnDataChangeListener() {
            @Override
            public void onTravelListAdd(List<TravelData> list) {

                Log.e("vm", "list size " + list.size());
                travels.setValue(list);

            }
        };
        travelRepository = new TravelRepository(application, changeListener);
        updateTravels();
    }

    private void updateTravels() {
        travelRepository.getmAllTravels();
    }

    public MutableLiveData<List<TravelData>> getTravels() {
        return travels;
    }

    public void setTravels(List<TravelData> list){
        travels.setValue(list);
    }

    public void addTravel(Travel travel, ArrayList<String> luggageImages){
        travelRepository.insertTravel(travel,luggageImages);
    }

    public TravelData getTravel(int id){
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

    public void removeLuggage(Luggage luggage) {
        travelRepository.removeLuggage(luggage);
    }


    public interface OnDataChangeListener{
        void onTravelListAdd(List<TravelData> list);
    }
}

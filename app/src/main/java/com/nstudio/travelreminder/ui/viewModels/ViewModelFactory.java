package com.nstudio.travelreminder.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.nstudio.travelreminder.data.TravelData;
import com.nstudio.travelreminder.database.entitiy.Luggage;
import com.nstudio.travelreminder.ui.adapters.ImageListAdapter;

import java.util.List;

public class ViewModelFactory  implements ViewModelProvider.Factory {

    private Application application;

    public ViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TravelViewModel.class)) {
            return (T) new TravelViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());



    }
}

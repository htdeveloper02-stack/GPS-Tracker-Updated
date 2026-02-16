package gps.trackerid.location.models.zonedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DataZoneViewModelFactory implements ViewModelProvider.Factory {

    private final DataZoneDao dao;

    public DataZoneViewModelFactory(DataZoneDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DataZoneViewModel.class)) {
            return (T) new DataZoneViewModel(dao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}



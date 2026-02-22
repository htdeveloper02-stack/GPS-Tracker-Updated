package gps.trackerid.location.models.users;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import gps.trackerid.location.database.DataUserDao;

public class DataUserViewModelFactory implements ViewModelProvider.Factory {

    private final DataUserDao dao;

    public DataUserViewModelFactory(DataUserDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(USerListViewModel.class)) {
            return (T) new USerListViewModel(dao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}



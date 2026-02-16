package gps.trackerid.location.models.zonedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DataZoneViewModel extends ViewModel {

    private final DataZoneDao zoneDao;
    private final MutableLiveData<List<DataZone>> allZones = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DataZoneViewModel(DataZoneDao zoneDao) {
        this.zoneDao = zoneDao;
        loadZones();
    }

    public LiveData<List<DataZone>> getAllZones() {
        return allZones;
    }

    public void insert(DataZone zone) {
        executor.execute(() -> {
            zoneDao.insertZone(zone);
            loadZones();
        });
    }

    public void update(DataZone zone) {
        executor.execute(() -> {
            zoneDao.updateZone(zone);
            loadZones();
        });
    }

    public void delete(DataZone zone) {
        executor.execute(() -> {
            zoneDao.deleteZone(zone);
            loadZones();
        });
    }

    public void deleteAll() {
        executor.execute(() -> {
            zoneDao.deleteAll();
            allZones.postValue(null);
        });
    }

    private void loadZones() {
        executor.execute(() ->
                allZones.postValue(zoneDao.getAllZones())
        );
    }
}


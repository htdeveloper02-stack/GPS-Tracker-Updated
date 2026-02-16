package gps.trackerid.location.models.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gps.trackerid.location.database.DataUserDao;

public class USerListViewModel extends ViewModel {

    private DataUserDao dataUserDao;
    private final MutableLiveData<List<DataUser>> AllUsers = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public USerListViewModel(DataUserDao zoneDao) {
        this.dataUserDao = zoneDao;
        loadUsers();
    }

    public LiveData<List<DataUser>> getAllUsers() {
        return AllUsers;
    }

    public void insert(DataUser zone) {
        executor.execute(() -> {
            dataUserDao.insertUser(zone);
            loadUsers();
        });
    }

    public void insert(List<DataUser> users) {
        // Insert users in background
        new Thread(() -> {
            for (DataUser user : users) {
                if (dataUserDao.exists(user.getCode()) == 0) {
                    dataUserDao.insertUser(user);
                }
//                dataUserDao.insertUser(user);
            }
            loadUsers();
        }).start();
    }

    public void update(DataUser zone) {
        executor.execute(() -> {
            dataUserDao.updateUserByCode(zone.getCode(), zone.isSelected());
//            loadUsers();
        });
    }

    public void delete(DataUser zone) {
        executor.execute(() -> {
            dataUserDao.deleteUser(zone);
            loadUsers();
        });
    }

    private void loadUsers() {
        executor.execute(() ->
                AllUsers.postValue(dataUserDao.getAllUsers())
        );
    }

    public void getAllUserss() {
        new Thread(new Runnable() {
            @Override
            public final void run() {
                AllUsers.postValue(dataUserDao.getAllUsers());
            }
        }).start();
    }
}

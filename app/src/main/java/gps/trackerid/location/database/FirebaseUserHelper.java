package gps.trackerid.location.database;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.models.users.DataUser;

public final class FirebaseUserHelper {

    private static final String TAG = "FirebaseUserHelper";

    public static FirebaseUserHelper INSTANCE = new FirebaseUserHelper();
    private static FirebaseDatabase database;
    private static DatabaseReference idsRef;
    private static DatabaseReference usersRef;

    private static MutableLiveData<List<String>> userIdsLiveData =
            new MutableLiveData<>();
    private static MutableLiveData<List<DataUser>> usersLiveData =
            new MutableLiveData<>();

    private static ValueEventListener idsListener;
    private static ValueEventListener usersListener;

    private FirebaseUserHelper() {

    }

    static {
        database = FirebaseDatabase.getInstance();
        idsRef = database.getReference("users_ids");
        usersRef = database.getReference("users_data");
        userIdsLiveData = new MutableLiveData<>();
        usersLiveData = new MutableLiveData<>();
    }
    // ----------------------------------
    // LiveData getters
    // ----------------------------------

    public MutableLiveData<List<String>> getUserIdsLiveData() {
        return userIdsLiveData;
    }

    public MutableLiveData<List<DataUser>> getUsersLiveData() {
        return usersLiveData;
    }

    // ----------------------------------
    // CRUD operations
    // ----------------------------------

    public void addUser(final DataUser user) {
        if (user == null || user.getCode() == null) return;

        idsRef.child(user.getCode()).setValue(true);
        usersRef.child(user.getCode())
                .setValue(user)
                .addOnSuccessListener(aVoid ->
                        Log.e(TAG, "User added successfully: " + user.getCode()))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Failed to add user: " + user.getCode(), e));
    }

    public void updateUser(final DataUser user) {
        if (user == null || user.getCode() == null) return;

        usersRef.child(user.getCode())
                .setValue(user)
                .addOnSuccessListener(aVoid ->
                        Log.e(TAG, "User updated successfully: " + user.getCode()))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Failed to update user: " + user.getCode(), e));
    }

    public void deleteUser(final String code) {
        if (code == null) return;

        idsRef.child(code).removeValue();
        usersRef.child(code)
                .removeValue()
                .addOnSuccessListener(aVoid ->
                        Log.e(TAG, "User deleted successfully: " + code))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Failed to delete user: " + code, e));
    }

    // ----------------------------------
    // Check user existence
    // ----------------------------------

    public interface UserResultCallback {
        void onResult(boolean exists, DataUser user);
    }

    public static void getUserIfExists(final String code,
                                       final UserResultCallback callback) {

        if (code == null || callback == null) {
            callback.onResult(false, null);
            return;
        }

        usersRef.child(code)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        DataUser user = snapshot.getValue(DataUser.class);
                        Log.e(TAG, "User exists [" + code + "]: " + user);
                        callback.onResult(true, user);
                    } else {
                        Log.e(TAG, "User does not exist: " + code);
                        callback.onResult(false, null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to check user: " + code, e);
                    callback.onResult(false, null);
                });
    }

    // ----------------------------------
    // Realtime listeners
    // ----------------------------------

    public void startListeningUserIds() {
        if (idsListener != null) return;

        idsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> ids = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey() != null) {
                        ids.add(child.getKey());
                    }
                }
                userIdsLiveData.postValue(ids);
                Log.e(TAG, "User IDs updated: " + ids);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "User IDs listener error: " + error.getMessage());
            }
        };

        idsRef.addValueEventListener(idsListener);
    }

    public void startListeningUsers() {
        startListeningUsers(null);
    }

    public void startListeningUsers(final String userCode) {
        if (usersListener != null) return;

        DatabaseReference ref = userCode == null
                ? usersRef
                : usersRef.child(userCode);

        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (userCode != null) {
                    DataUser user = snapshot.getValue(DataUser.class);
                    if (user != null) {
                        List<DataUser> list = new ArrayList<>();
                        list.add(user);
                        usersLiveData.postValue(list);
                        Log.e(TAG, "User updated: " + userCode);
                    }
                    return;
                }

                List<DataUser> users = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    DataUser user = child.getValue(DataUser.class);
                    if (user != null) {
                        users.add(user);
                    }
                }

                usersLiveData.postValue(users);
                Log.e(TAG, "Users updated: " + users.size());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Users listener error: " + error.getMessage());
            }
        };

        ref.addValueEventListener(usersListener);
    }

    // ----------------------------------
    // Stop listeners
    // ----------------------------------

    public void stopListening() {
        if (idsListener != null) {
            idsRef.removeEventListener(idsListener);
            idsListener = null;
        }

        if (usersListener != null) {
            usersRef.removeEventListener(usersListener);
            usersListener = null;
        }
    }
}


package gps.trackerid.location.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import gps.trackerid.location.models.users.DataUser;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class FirebaseRequestHelper {
    private static final String TAG = "FirebaseRequestHelper";
    public static final FirebaseRequestHelper INSTANCE;
    private static final FirebaseDatabase database;
    private static final DatabaseReference friendsRef;
    private static final DatabaseReference requestRef;

    static {
        INSTANCE = new FirebaseRequestHelper();
        database = FirebaseDatabase.getInstance();
        requestRef = database.getReference("requests");
        friendsRef = database.getReference("friends");
        INSTANCE.ensureNodesExist();
    }

    public void ensureNodesExist() {
        try {
            createNodeIfNotExists("requests");
            createNodeIfNotExists("friends"); // replace with NativeProtocol.AUDIENCE_FRIENDS if available
        } catch (DatabaseException e) {
            Log.e(TAG, "Failed to ensure nodes exist", e);
        }
    }

    /**
     * Checks if a node exists, and if not, creates it with an empty map.
     */
    private void createNodeIfNotExists(@NonNull final String nodeName) throws DatabaseException {
        final DatabaseReference reference = database.getReference(nodeName);

        Task<DataSnapshot> task = reference.get();
        task.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    reference.setValue(Collections.emptyMap())
                            .addOnSuccessListener(aVoid -> Log.e(TAG, "Created missing node: " + nodeName))
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to create node: " + nodeName, e));
                } else {
                    Log.e(TAG, "Node already exists: " + nodeName);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to check node: " + nodeName, e);
            }
        });
    }

    /**
     * Sends a friend/location request from senderCode to receiverCode.
     */
    public void sendRequest(@NonNull final String senderCode, @NonNull final String receiverCode) {
        // Create data for the request
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("status", "pending");
        requestData.put("timestamp", System.currentTimeMillis());

        // Write to Firebase: /requests/{receiverCode}/{senderCode}
        requestRef.child(receiverCode).child(senderCode).setValue(requestData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Request sent: " + senderCode + " -> " + receiverCode);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to send request", e);
                    }
                });
    }

    public interface OnCompleteListener {
        void onComplete(boolean success);
    }

    /**
     * Confirms a request: either accepted or rejected.
     *
     * @param senderCode   The sender's code.
     * @param receiverCode The receiver's code.
     * @param accepted     True if accepted, false if rejected.
     * @param onComplete   Callback invoked when the operation completes.
     */
    public void confirmRequest(@NonNull final String senderCode,
                               @NonNull final String receiverCode,
                               final boolean accepted,
                               @NonNull final OnCompleteListener onComplete) {

        final String status = accepted ? "accepted" : "rejected";

        // Update request status: /requests/{receiver}/{sender}/status
        requestRef.child(receiverCode).child(senderCode).child("status")
                .setValue(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Request " + status + ": " + senderCode + " -> " + receiverCode);

                        if (accepted) {
                            // If accepted, add both users to each other's friends list
                            addToFriends(senderCode, receiverCode, onComplete);
                        } else {
                            // If rejected, just call onComplete with true
                            onComplete.onComplete(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to update request", e);
                        onComplete.onComplete(false);
                    }
                });
    }

    /**
     * Adds two users to each other's friend list.
     */
    private void addToFriends(@NonNull final String user1,
                              @NonNull final String user2,
                              @NonNull final OnCompleteListener onComplete) {

        // Add user2 to user1's friend list
        friendsRef.child(user1).child(user2)
                .setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Add user1 to user2's friend list
                        friendsRef.child(user2).child(user1)
                                .setValue(true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        onComplete.onComplete(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Failed to add to friend list", e);
                                        onComplete.onComplete(false);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to add to friend list", e);
                        onComplete.onComplete(false);
                    }
                });
    }

    public interface CheckFriendCallback {
        void onResult(boolean areFriends, boolean requestPending);
    }

    /**
     * Checks if the other user is a friend or if there is a pending request.
     */
    public void checkFriendOrRequested(@NonNull final String currentUserCode,
                                       @NonNull final String otherUserCode,
                                       @NonNull final CheckFriendCallback callback) {

        // Step 1: Check if they are already friends
        friendsRef.child(currentUserCode).child(otherUserCode).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Already friends
                            callback.onResult(true, false);
                        } else {
                            // Not friends: check for pending requests
                            checkPendingRequests(currentUserCode, otherUserCode, callback);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to check friend/request", e);
                        callback.onResult(false, false);
                    }
                });
    }

    private void checkPendingRequests(@NonNull String user1,
                                      @NonNull String user2,
                                      @NonNull final CheckFriendCallback callback) {

        List<Task<DataSnapshot>> tasks = Arrays.asList(
                requestRef.child(user1).child(user2).get(),
                requestRef.child(user2).child(user1).get()
        );

        final boolean[] requestPending = {false};
        final int[] completedCount = {0};

        for (Task<DataSnapshot> task : tasks) {
            task.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String status = snapshot.child("status").getValue(String.class);
                        if ("pending".equals(status)) {
                            requestPending[0] = true;
                        }
                    }
                    completedCount[0]++;
                    if (completedCount[0] == tasks.size()) {
                        callback.onResult(false, requestPending[0]);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    completedCount[0]++;
                    if (completedCount[0] == tasks.size()) {
                        callback.onResult(false, requestPending[0]);
                    }
                }
            });
        }
    }

    public interface RequestsCallback {
        void onSuccess(List<DataUser> requests);
    }

    public interface RequestsErrorCallback {
        void onError(Exception e);
    }

    /**
     * Get all pending friend requests for a specific user.
     */
//    public void getAllRequestsForUser(String receiverCode,
//                                      final RequestsCallback onResult,
//                                      final RequestsErrorCallback onError) {
//        if (receiverCode == null || receiverCode.isEmpty()) {
//            onResult.onSuccess(new ArrayList<>());
//            return;
//        }
//
//        requestRef.child(receiverCode).get()
//                .addOnSuccessListener(snapshot -> {
//                    if (!snapshot.exists()) {
//                        onResult.onSuccess(new ArrayList<>());
//                        return;
//                    }
//
//                    List<String> pendingSenders = new ArrayList<>();
//                    for (DataSnapshot child : snapshot.getChildren()) {
//                        String status = child.child("status").getValue(String.class);
//                        if ("pending".equals(status)) {
//                            pendingSenders.add(child.getKey());
//                        }
//                    }
//
//                    Log.e("FirebaseRequestHelper", "Pending senders: " + pendingSenders);
//
//                    if (pendingSenders.isEmpty()) {
//                        onResult.onSuccess(new ArrayList<>());
//                        return;
//                    }
//
//                    List<DataUser> requests = new ArrayList<>();
//                    final int[] completed = {0};
//
//                    for (String senderCode : pendingSenders) {
//                        friendsRef.child(senderCode).get()
//                                .addOnSuccessListener(userSnapshot -> {
//                                    if (userSnapshot.exists()) {
//                                        DataUser user = userSnapshot.getValue(DataUser.class);
//                                        if (user != null) {
//                                            requests.add(user);
//                                        } else {
//                                            Log.e("FirebaseRequestHelper", "User snapshot is null for " + senderCode);
//                                        }
//                                    } else {
//                                        Log.e("FirebaseRequestHelper", "No user found for " + senderCode);
//                                    }
//
//                                    completed[0]++;
//                                    if (completed[0] == pendingSenders.size()) {
//                                        Log.e("FirebaseRequestHelper", "Final request list: " + requests);
//                                        onResult.onSuccess(requests);
//                                    }
//                                })
//                                .addOnFailureListener(e -> {
//                                    Log.e("FirebaseRequestHelper", "Failed to fetch user: " + senderCode, e);
//                                    completed[0]++;
//                                    if (completed[0] == pendingSenders.size()) {
//                                        onResult.onSuccess(requests);
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("FirebaseRequestHelper", "Failed to fetch requests", e);
//                    onError.onError(e);
//                });
//    }
    public interface OnResultListener<T> {
        void onResult(T result);
    }

    public interface OnErrorListener {
        void onError(Exception e);
    }

    public void getAllRequestsForUser(
            String receiverCode,
            final OnResultListener<List<DataUser>> onResult,
            final OnErrorListener onError
    ) {
        if (receiverCode == null || receiverCode.isEmpty()) {
            if (onError != null)
                onError.onError(new IllegalArgumentException("Receiver code is empty"));
            return;
        }

        DatabaseReference requestReference = FirebaseDatabase.getInstance()
                .getReference("requests") // replace with your actual node for requests
                .child(receiverCode);

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users_data");

        // Fetch all pending requests for this user
        requestReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> pendingUserCodes = new ArrayList<>();

                // Filter only pending requests
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    String status = requestSnapshot.child("status").getValue(String.class);
                    if ("pending".equals(status)) {
                        String senderCode = requestSnapshot.getKey();
                        if (senderCode != null) {
                            pendingUserCodes.add(senderCode);
                        }
                    }
                }

                if (pendingUserCodes.isEmpty()) {
                    if (onResult != null) onResult.onResult(Collections.emptyList());
                    return;
                }

                // Fetch DataUser objects for each pending sender
                List<DataUser> pendingUsers = new ArrayList<>();
                AtomicInteger completedCount = new AtomicInteger(0);

                for (String senderCode : pendingUserCodes) {
                    usersReference.child(senderCode).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            DataUser user = userSnapshot.getValue(DataUser.class);
                            if (user != null) {
                                pendingUsers.add(user);
                            } else {
                                Log.e("FirebaseRequestHelper", "No user found for code: " + senderCode);
                            }

                            // Check if all requests are processed
                            if (completedCount.incrementAndGet() == pendingUserCodes.size() && onResult != null) {
                                onResult.onResult(pendingUsers);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("FirebaseRequestHelper", "Failed to fetch user: " + senderCode, error.toException());
                            if (completedCount.incrementAndGet() == pendingUserCodes.size() && onResult != null) {
                                onResult.onResult(pendingUsers);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseRequestHelper", "Failed to fetch requests for " + receiverCode, error.toException());
                if (onError != null) onError.onError(error.toException());
            }
        });
    }

    public void getFriendsListWithDetails(final String userCode, final Function1<List<DataUser>, Unit> onResult) {
        if (userCode == null || onResult == null) {
            throw new IllegalArgumentException("userCode and onResult must not be null");
        }

        // Fetch the user's friends list from Firebase
        friendsRef.child(userCode).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.e("FirebaseRequestHelper", "No friends found for " + userCode);
                            onResult.invoke(Collections.emptyList());
                            return;
                        }

                        List<String> friendCodes = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            if (key != null) {
                                friendCodes.add(key);
                            }
                        }

                        Log.e("FirebaseRequestHelper", "Friends of " + userCode + ": " + friendCodes);

                        if (friendCodes.isEmpty()) {
                            onResult.invoke(Collections.emptyList());
                            return;
                        }

                        final List<DataUser> friendsList = new ArrayList<>();
                        final int[] completedCount = {0};

                        for (final String friendCode : friendCodes) {
                            FirebaseUserHelper.INSTANCE.getUserIfExists(friendCode, new FirebaseUserHelper.UserResultCallback() {
                                @Override
                                public void onResult(boolean exists, DataUser user) {
                                    completedCount[0]++;
                                    if (exists && user != null) {
                                        friendsList.add(user);
                                    }
                                    // Check if all async fetches are complete
                                    if (completedCount[0] == friendCodes.size()) {
                                        Log.e("FirebaseRequestHelper", "Fetched " + friendsList.size() + " friends for " + userCode);
                                        onResult.invoke(friendsList);
                                    }
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("FirebaseRequestHelper", "Failed to fetch friends for " + userCode, e);
                        onResult.invoke(Collections.emptyList());
                    }
                });
    }

    public void updateFriendInFirebase(DataUser user) {
        if (user == null || user.getCode() == null) return;

        friendsRef.child(user.getCode()).setValue(user)  // replaces the entire node
                .addOnSuccessListener(aVoid ->
                        Log.e("FirebaseUpdate", "Updated friend: " + user.getCode()))
                .addOnFailureListener(e ->
                        Log.e("FirebaseUpdate", "Update failed: " + e.getMessage()));
    }


}

/*
package gps.trackerid.location.ui.phonetracker;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gps.trackerid.location.R;
import gps.trackerid.location.databinding.MyContactlistBinding;
import gps.trackerid.location.ui.PhoneLocator;
import gps.trackerid.location.ui.baseui.BaseActivity;

public class MyContactsAct extends BaseActivity {
    MyContactlistBinding binding;
    MyContactsAct myContactList;
    ContactAdapter contactAdapter;
    private ProgressDialog progressDialog;
    static ArrayList<HashMap<String, Object>> hashMapArrayList = new ArrayList<>();
    private Cursor cursor, cursor1;
    String data1;
    String name;
    Boolean aBoolean = true;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyContactlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myContactList = this;

        contactAdapter = new ContactAdapter(myContactList);
        binding.rvContacts.setLayoutManager(new GridLayoutManager(this, 1));
        binding.rvContacts.setAdapter(contactAdapter);
        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadContacts();
        binding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.ivSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = binding.edtSearchName.getText().toString();
                if (str.isEmpty()) {
                    Toast.makeText(myContactList, "Enter contact Name.!", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.edtSearchName.getWindowToken(), 0);

                    contactAdapter.filter(str);

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myContactList = null;
    }

    private void loadContacts() {

        // 1. show progress
        progressDialog = new ProgressDialog(myContactList);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // 2. background task
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                // Background work (same as doInBackground)
                try {
                    hashMapArrayList.clear();
                    Loadcontact(); // this method loads contacts
                } catch (Exception ignored) {
                }

                // 3. UI update (same as onPostExecute)
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (hashMapArrayList.size() <= 0) {
                            binding.tvEmptyData.setVisibility(VISIBLE);
                            binding.rvContacts.setVisibility(GONE);
                            binding.mCvSearch.setVisibility(GONE);
                            return;
                        }

                        try {
                            binding.tvEmptyData.setVisibility(GONE);
                            binding.rvContacts.setVisibility(VISIBLE);
                            binding.mCvSearch.setVisibility(VISIBLE);
                            contactAdapter.setContact(hashMapArrayList);

                        } catch (Exception e) {
                            binding.tvEmptyData.setVisibility(VISIBLE);
                            binding.rvContacts.setVisibility(GONE);
                            binding.mCvSearch.setVisibility(GONE);
                            binding.tvEmptyData.setText("Exception occured while retriving your contacts.");
                        }
                    }
                });
            }
        });
    }

    public void Loadcontact() {
        try {
            this.cursor = getApplicationContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (this.cursor.moveToNext()) {
                String string = this.cursor.getString(this.cursor.getColumnIndexOrThrow("_id"));
                if (this.cursor.getString(this.cursor.getColumnIndex("has_phone_number")).equalsIgnoreCase("1")) {
                    this.cursor1 = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = " + string, null, null);
                    this.cursor1.moveToFirst();
                    try {
                        this.data1 = this.cursor1.getString(this.cursor1.getColumnIndex("data1"));
                        this.data1 = this.data1.replaceAll("[^0-9]", "");
                    } catch (Exception unused) {
                    }
                    try {
                        this.name = this.cursor.getString(this.cursor.getColumnIndex("display_name"));
                    } catch (Exception unused2) {
                    }
                    this.aBoolean = true;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    try {
                        if (this.data1.length() > 0 && this.data1.length() > 4) {
                            try {
                                if (this.data1.contains("+91")) {
                                    this.string = this.data1.substring(3, 7);
                                } else if (this.data1.contains("+1")) {
                                    this.string = this.data1.substring(2, 5);
                                } else if (this.data1.contains("+001")) {
                                    this.string = this.data1.substring(4, 7);
                                } else if (this.data1.substring(0, 2).contains("91")) {
                                    this.string = this.data1.substring(2, 6);
                                } else if (this.data1.substring(0, 1).contains("1")) {
                                    this.string = this.data1.substring(1, 4);
                                } else if (this.data1.substring(0, 3).contains("001")) {
                                    this.string = this.data1.substring(3, 6);
                                } else if (this.data1.substring(0, 1).contains("0")) {
                                    this.string = this.data1.substring(1, 5);
                                } else if (this.data1.substring(0, 1).contains("+")) {
                                    this.string = this.data1.substring(1, 5);
                                } else {
                                    this.string = this.data1.substring(0, 4);
                                }
                            } catch (Exception unused3) {
                                this.aBoolean = false;
                            }
                        } else {
                            this.aBoolean = false;
                        }
//                        if (this.aBoolean.booleanValue()) {
//                            try {
//                                this.stateList = new Sqlhandler(this.context).getMobileData(this.string);
//                                if (this.stateList.size() > 0) {
//                                    for (State c0820e : this.stateList) {
//                                        hashMap.put("operator", c0820e.getOpertator());
//                                        hashMap.put("state", c0820e.getState());
//                                        hashMap.put("icon", numArr[c0820e.getIcon()]);
//                                        Log.d("Name: ", "OperatorName: " + c0820e.getOpertator() + " ,State Name: " + c0820e.getState() + " ,Icon Val: " + c0820e.getIcon());
//                                    }
//                                } else {
//                                    hashMap.put("operator", "Number not found.");
//                                    hashMap.put("state", "");
//                                    hashMap.put("icon", numArr[0]);
//                                }
//                            } catch (Exception unused4) {
//                            }
//                        } else {
//                            hashMap.put("operator", "Number not found.");
//                            hashMap.put("state", "");
//                            hashMap.put("icon", numArr[0]);
//                        }
                        hashMap.put("name", this.name);
                        hashMap.put("no", this.data1);
                        hashMapArrayList.add(hashMap);
                    } catch (Exception unused5) {
                    }
                }
            }
            this.cursor.close();
        } catch (Exception unused6) {
        }
    }

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
        Context context;
        private ArrayList<HashMap<String, Object>> mData = new ArrayList<>();
        private ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

        public void setContact(ArrayList<HashMap<String, Object>> zones) {
            this.mData = zones;
            this.filteredList = new ArrayList<>(mData);
            notifyDataSetChanged();
        }

        public ContactAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_contact, parent, false);

            return new ContactAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {

            holder.mTxtName.setText(filteredList.get(position).get("name").toString());
            holder.mTxtNumber.setText(filteredList.get(position).get("no").toString());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchPhoneNumber(filteredList.get(position));
                }
            });


        }

        public void filter(String query) {
            filteredList.clear();

            if (query.isEmpty()) {
                filteredList.addAll(mData);
            } else {
                String search = query.toLowerCase();
                for (HashMap<String, Object> map : mData) {
                    String name = map.get("name").toString().toLowerCase();
                    if (name.contains(search)) {
                        filteredList.add(map);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTxtName, mTxtNumber;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTxtName = itemView.findViewById(R.id.mTxtName);
                mTxtNumber = itemView.findViewById(R.id.mTxtNumber);
            }
        }
    }

    private void searchPhoneNumber(HashMap<String, Object> data) {

        // For demonstration, we just show a static location & fake carrier info
        // Replace this with a real API lookup (like numverify.com, etc.)

        String fullPhoneNumber = data.get("no").toString();
        String fullPhoneName = data.get("name").toString();

        // Open details activity
        Intent intent = new Intent(this, PhoneLocator.class);
        intent.putExtra("phoneNumber", fullPhoneNumber);
        intent.putExtra("Name", fullPhoneName);
        startActivity(intent);
    }

}*/

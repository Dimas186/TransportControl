package com.example.transportcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportcontrol.handler.DataModelToTextHandler;
import com.example.transportcontrol.handler.OCRHandler;
import com.example.transportcontrol.model.DataModel;
import com.example.transportcontrol.model.UserModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import static com.example.transportcontrol.handler.PlateNumberHandler.handleText;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, RecyclerTouchListener.RecyclerTouchListenerHelper {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private DatabaseReference usersRef;
    private DatabaseReference itemsRef;
    private DatabaseReference logsRef;
    private ArrayList<UserModel> users = new ArrayList<>();
    private ArrayList<String> logs = new ArrayList<>();
    private static UserModel currentUser;
    private OnActivityTouchListener touchListener;
    private RecyclerView mRecyclerView;
    private RVAdapter mAdapter;
    private ArrayList<DataModel> items = new ArrayList<>();
    PrefManager prefManager;
    ProgressBar progressBar;
    FloatingActionButton fabAddItem;
    TextView tvUnconfirmedAccount;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        tvUnconfirmedAccount = findViewById(R.id.tvUnconfirmedAccount);
        searchView = findViewById(R.id.searchView);
        setOnQueryTextListener();
        fabAddItem = findViewById(R.id.fab);
        fabAddItem.hide();//to avoid a departure, you need to wait until the current user's model is found
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        itemsRef = database.getReference("items");
        logsRef = database.getReference("logs");
        setUsers();
        initRV();
        setData();
        setLogs();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        login();
    }

    private void setOnQueryTextListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                setRecyclerViewAdapter(getFilteredList(s.toUpperCase()));
                return false;
            }
        });
    }

    private ArrayList<DataModel> getFilteredList(String query) {
        ArrayList<DataModel> filteredList = new ArrayList<>();
        for (DataModel dataModel : items) {
            if (query.isEmpty()) {
                filteredList.add(dataModel);
            }
            else if (dataModel.getPlateNumber().contains(query)
                    || dataModel.getInventoryNumber().contains(query)) {
                filteredList.add(dataModel);
            }
        }
        return filteredList;
    }

    private void login() {
        if (mFirebaseUser == null) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            if (!prefManager.isUserRegistered()) {
                regUser();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        showChangeLogs();
        return super.onOptionsItemSelected(item);
    }

    private void showChangeLogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Логи")
                .setMessage(arrayListToString(logs))
                .setCancelable(true)
                .setPositiveButton("Ок", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String arrayListToString(ArrayList<String> list) {
        StringBuilder str = new StringBuilder();
        for (String s : list) {
            str.append(s).append("\n\n");
        }
        return str.toString();
    }

    private void initRV() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        RecyclerTouchListener onTouchListener = new RecyclerTouchListener(this, mRecyclerView);
        onTouchListener
                .setClickable((new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        showInformationDialog(position);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        //Toast.makeText(MainActivity.this, "Button in row " + (position + 1) + " clicked!", Toast.LENGTH_SHORT).show();
                    }
                })).setLongClickable(true, (new RecyclerTouchListener.OnRowLongClickListener() {
            public void onRowLongClicked(int position) {
                Toast.makeText(MainActivity.this, "Row " + (position + 1) + " long clicked!", Toast.LENGTH_SHORT).show();
            }
        })).setSwipeOptionViews(R.id.edit, R.id.delete).setSwipeable(R.id.rowFG, R.id.rowBG, (new RecyclerTouchListener.OnSwipeOptionsClickListener() {
            public void onSwipeOptionClicked(int viewID, int position) {
                if (viewID == R.id.edit) {
                    if (currentUser.getType().equals("moder0")) {
                        AddOrChangeActivityModer0.setForChanges(true);
                        AddOrChangeActivityModer0.setDataModel(mAdapter.getModelList().get(position));
                        startActivity(new Intent(MainActivity.this, AddOrChangeActivityModer0.class));
                    }
                    else if (currentUser.getType().equals("user")) {
                        offer(mAdapter.getModelList().get(position));
                    }
                    else {
                        AddOrChangeActivityModer123.setMotorcade(currentUser.getType()
                                .replace("moder", ""));
                        AddOrChangeActivityModer123.setForChanges(true);
                        AddOrChangeActivityModer123.setDataModel(mAdapter.getModelList().get(position));
                        startActivity(new Intent(MainActivity.this, AddOrChangeActivityModer123.class));
                    }
                } else if (viewID == R.id.delete) {
                    deleteItem(MainActivity.this, position);
                }
            }
        }));
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }

    public void deleteItem(Context context, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setMessage("Удалить?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        itemsRef.child(mAdapter.getModelList().get(position).getId()).removeValue();
                        addDeleteLog(position);
                        items.remove(position);
                        mAdapter.removeItem(position);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void addDeleteLog(int position) {
        String userName = getString(R.string.user_name_and_last_name,
                MainActivity.getCurrentUser().getName(), MainActivity.getCurrentUser().getLastName());
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
        logsRef.push().setValue(getString(R.string.deletedBy, userName, mAdapter.getModelList().get(position).getPlateNumber(), time));
    }

    private void showInformationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(mAdapter.getModelList().get(position).getBrand())
                .setMessage(DataModelToTextHandler.handle(mAdapter.getModelList().get(position)))
                .setCancelable(true)
                .setPositiveButton("Ок", null);
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    private void setRecyclerViewAdapter(ArrayList<DataModel> list) {
        mAdapter = new RVAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchListener != null) {
            touchListener.getTouchCoordinates(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setUsers() {
        users = new ArrayList();

        Query myQuery = usersRef;
        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                if (mFirebaseUser != null) {
                    if (userModel.getuId().equals(mFirebaseUser.getUid())) {
                        currentUser = userModel;
                        progressBar.setVisibility(View.GONE);
                        //user and undefined user can't add items
                        if (!currentUser.getType().equals("user") && !currentUser.getType().equals("undefined")) {
                            fabAddItem.show();
                        }
                        if (currentUser.getType().equals("undefined")) {
                            mRecyclerView.setVisibility(View.GONE);
                            tvUnconfirmedAccount.setVisibility(View.VISIBLE);
                        }
                        System.out.println(currentUser.getType());
                    }
                }
                users.add(userModel);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setData() {
        items = new ArrayList();

        Query myQuery = itemsRef;
        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataModel dataModel = dataSnapshot.getValue(DataModel.class);
                dataModel.setId(dataSnapshot.getKey());

                if (currentUser != null) {
                    //moder0 and user can see all motorcades
                    if (currentUser.getType().equals("moder0") || currentUser.getType().equals("user")) {
                        items.add(dataModel);
                    }
                    //others can only see their respective motorcades
                    if (currentUser.getType().equals("moder1") && dataModel.getMotorcade().equals("1")) {
                        items.add(dataModel);
                    }
                    if (currentUser.getType().equals("moder2") && dataModel.getMotorcade().equals("2")) {
                        items.add(dataModel);
                    }
                    if (currentUser.getType().equals("moder3") && dataModel.getMotorcade().equals("3")) {
                        items.add(dataModel);
                    }
                    setRecyclerViewAdapter(items);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setLogs() {
        logs = new ArrayList();

        Query myQuery = logsRef;
        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                logs.add(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AddOrChangeActivityModer0.isAddedOrChanged()) {
            setData();
            AddOrChangeActivityModer0.setIsAddedOrChanged(false);
        }
        if (AddOrChangeActivityModer123.isAddedOrChanged()) {
            setData();
            AddOrChangeActivityModer123.setIsAddedOrChanged(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void showImageImportDialog() {
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent, 1213);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 1213) {
                String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                CropImage.activity(Uri.fromFile(new File(filePath)))
                        .setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                searchView.setIconified(false);//open searchView
                searchView.setQuery(handleText(OCRHandler.handle(data, MainActivity.this).toString()), false);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            boolean userIsRegistered = false;
                            for (UserModel userModel : users) {
                                if (userModel.getuId().equals(mFirebaseAuth.getUid())) {
                                    System.out.println(userModel.getuId());
                                    userIsRegistered = true;
                                }
                            }
                            if (!userIsRegistered) {
                                regUser();
                            } else {
                                prefManager.setUserRegistered(true);
                            }
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            setUsers();
                            initRV();
                            setData();
                        }
                    }
                });
    }

    private void regUser() {
        final View view = getLayoutInflater().inflate(R.layout.reg_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Регистрация");
        alertDialog.setCancelable(false);
        final EditText etLastName = view.findViewById(R.id.etLastName);
        final EditText etName = view.findViewById(R.id.etName);
        final EditText etMiddleName = view.findViewById(R.id.etMiddleName);
        final EditText etPhone = view.findViewById(R.id.etPhone);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefManager.setUserRegistered(true);//if you put it at the end, it doesn't have time to complete
                UserModel userModel = new UserModel();
                userModel.setuId(mFirebaseAuth.getUid());
                userModel.setName(etName.getText().toString());
                userModel.setLastName(etLastName.getText().toString());
                userModel.setMiddleName(etMiddleName.getText().toString());
                userModel.setPhone(etPhone.getText().toString());
                userModel.setType("undefined");
                usersRef.push().setValue(userModel);
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void offer(final DataModel dataModel) {
        final View view = getLayoutInflater().inflate(R.layout.offers_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Предложение");
        final EditText etOffer = view.findViewById(R.id.etOffer);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Предложить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOfferLog(dataModel, etOffer.getText().toString());
                dataModel.setOffers(etOffer.getText().toString());
                itemsRef.child(dataModel.getId()).setValue(dataModel);
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void addOfferLog(DataModel dataModel, String offers) {
        DataModel originDataModel = null;
        try {
            originDataModel = dataModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        dataModel.setOffers(offers);

        String changes = DataModelChangeFinder.getChanges(this, originDataModel, dataModel);
        String userName = getString(R.string.user_name_and_last_name,
                MainActivity.getCurrentUser().getName(), MainActivity.getCurrentUser().getLastName());
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
        logsRef.push().setValue(getString(R.string.changedBy, userName, changes, dataModel.getPlateNumber(), time));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (currentUser.getType().equals("moder0")) {
                    startActivity(new Intent(this, AddOrChangeActivityModer0.class));
                }
                else {
                    AddOrChangeActivityModer123.setMotorcade(currentUser.getType()
                            .replace("moder", ""));
                    startActivity(new Intent(this, AddOrChangeActivityModer123.class));
                }
                break;
            case R.id.ivScan:
                showImageImportDialog();
                break;
        }
    }

    public static UserModel getCurrentUser() {
        return currentUser;
    }
}

package com.example.transportcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, RecyclerTouchListener.RecyclerTouchListenerHelper {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference itemsRef;
    private ArrayList<UserModel> users = new ArrayList<>();
    UserModel myModel;
    private OnActivityTouchListener touchListener;
    private RecyclerView mRecyclerView;
    private RVAdapter mAdapter;
    private RecyclerTouchListener onTouchListener;
    private ArrayList<DataModel> items = new ArrayList<>();
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        itemsRef = database.getReference("items");
        setUsers();
        initRV();
        setData();

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

    private void login() {
        if (mFirebaseUser == null) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            if (prefManager.isUserRegistered()) {
                Toast.makeText(MainActivity.this, mFirebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
            else {
                regUser();
            }
        }
    }

    private void initRV() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        onTouchListener = new RecyclerTouchListener(this, mRecyclerView);
        onTouchListener
                .setClickable((new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "edit " + position, Toast.LENGTH_SHORT).show();
                }
                else if (viewID == R.id.delete) {
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
                        itemsRef.child(items.get(position).getId()).removeValue();
                        items.remove(position);
                        mAdapter.removeItem(position);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    private void setRecyclerViewAdapter() {
        mAdapter = new RVAdapter(this, items);
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
                        myModel = userModel;
                        System.out.println(myModel.getType());
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
                items.add(dataModel);
                setRecyclerViewAdapter();
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
        if (AddingActivityModer0.isAdded()) {
            setData();
            AddingActivityModer0.setIsAdded(false);
        }
        if (AddingActivityModer12.isAdded()) {
            setData();
            AddingActivityModer12.setIsAdded(false);
        }
        if (AddingActivityModer3.isAdded()) {
            setData();
            AddingActivityModer3.setIsAdded(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                            boolean userIsRegistered = false;
                            for (UserModel userModel : users) {
                                if (userModel.getuId().equals(mFirebaseAuth.getUid())) {
                                    System.out.println(userModel.getuId());
                                    userIsRegistered = true;
                                }
                            }
                            if (!userIsRegistered) {
                                regUser();
                            }
                            else {
                                prefManager.setUserRegistered(true);
                            }
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            setUsers();
                        }
                    }
                });
    }

    private void regUser() {
        final View view = getLayoutInflater().inflate(R.layout.edit_text_dialog, null);
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

    public void onClick(View v) {
        if (myModel.getType().equals("moder0")) {
            startActivity(new Intent(this, AddingActivityModer0.class));
        }
        if (myModel.getType().equals("moder1")) {
            startActivity(new Intent(this, AddingActivityModer12.class));
        }
        if (myModel.getType().equals("moder2")) {
            startActivity(new Intent(this, AddingActivityModer12.class));
        }
        if (myModel.getType().equals("moder3")) {
            startActivity(new Intent(this, AddingActivityModer3.class));
        }
    }
}

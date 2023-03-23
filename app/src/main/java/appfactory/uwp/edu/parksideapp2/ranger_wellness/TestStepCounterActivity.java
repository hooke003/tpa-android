//package appfactory.uwp.edu.parksideapp2.ranger_wellness;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.fitness.Fitness;
//import com.google.android.gms.fitness.FitnessOptions;
//import com.google.android.gms.fitness.data.DataSet;
//import com.google.android.gms.fitness.data.DataType;
//import com.google.android.gms.fitness.data.Field;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//
//import appfactory.uwp.edu.parksideapp2.R;
//
//public class TestStepCounterActivity extends AppCompatActivity {
//
//    public static final String TAG = "StepCounter";
//    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
//    private Button updateSteps;
//    private TextView stepCount;
//    private Context context;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_step_counter);
//        context = this;
//
//        updateSteps = (Button) findViewById(R.id.button);
//        stepCount = (TextView) findViewById(R.id.step_textView);
//
//        updateSteps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                readData();
//            }
//        });
//
//        FitnessOptions fitnessOptions = FitnessOptions.builder()
//                .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
//                .build();
//
//
//        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
//            GoogleSignIn.requestPermissions(
//                    this,
//                    REQUEST_OAUTH_REQUEST_CODE,
//                    GoogleSignIn.getLastSignedInAccount(this),
//                    fitnessOptions);
//        } else {
//            subscribe();
//        }
//
//
//
////        readData();
//    }
//
//    public void subscribe() {
//        // To create a subscription, invoke the Recording API. As soon as the subscription is
//        // active, fitness data will start recording.
//        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
//                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .addOnCompleteListener(
//                        new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.i(TAG, "Successfully subscribed!");
//                                } else {
//                                    Log.w(TAG, "There was a problem subscribing.", task.getException());
//                                }
//                            }
//                        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
//                subscribe();
//            }
//        }
//    }
//
//    private void readData() {
//        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//            Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
//                    .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
//                    .addOnSuccessListener(
//                            new OnSuccessListener<DataSet>() {
//                                @Override
//                                public void onSuccess(DataSet dataSet) {
//                                    long total = dataSet.isEmpty() ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                                    Log.i(TAG, "Total steps: " + total);
//                                    Toast.makeText(context, "Total steps: " + total, Toast.LENGTH_LONG).show();
//                                    updateText((int) total);
//                                }
//                            })
//                    .addOnFailureListener(
//                            new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "There was a problem getting the step count.", e);
//                                }
//                            });
//        }
//    }
//
//    public void updateText(int steps) {
//        stepCount.setText(String.valueOf(steps));
//    }
//}

package ca.connect.dal.dalconnect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIError;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;


public class ChatActivity extends AppCompatActivity implements LocationListener {

    public static final String LOG_TAG = ChatActivity.class.getName();

    ListView conversationList;
    ImageButton sendKey;
    EditText inputText;
    AIConfiguration aiConfiguration;
    AIDataService aiDataService;

    private Location userLocation;
    LocationManager locationManager;

    public static ArrayList<MessageData> messageList;
    public static MessageAdapter messageAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        setTitle("Chat Bot");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 101);
        } else
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


        //Currently supported language is set to english language
        final AIConfiguration.SupportedLanguages lang =
                AIConfiguration.SupportedLanguages.fromLanguageTag("en");

        aiConfiguration = new AIConfiguration("0436e0e5611a4ddab3baee87314b2444", lang);
        aiDataService = new AIDataService(aiConfiguration);

        sendKey = findViewById(R.id.imageButton);
        inputText = findViewById(R.id.textMessage);

        sendKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = inputText.getText().toString();
                if (!text.trim().equals("")) {
                    addMessage(text, true);
                    inputText.setText("");
                    sendRequest(text);
                }
            }
        });


        conversationList = findViewById(R.id.ConversationList);
        conversationList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        conversationList.setStackFromBottom(true);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        conversationList.setAdapter(messageAdapter);


    }

    /**
     * This function retrieves the corresponding message(sent/received) from the user/server
     *
     * @param message
     * @param isUser
     */
    private void addMessage(String message, boolean isUser) {

        final MessageData chatMessage = new MessageData(isUser, "", "", "");
        chatMessage.messageBody = message;
        messageAdapter.add(chatMessage);
        messageAdapter.notifyDataSetChanged();
    }

    private void addMessage(String message, String extraLink, boolean isUser) {
        final MessageData chatMessage = new MessageData(isUser, "", "", "");
        chatMessage.messageBody = message;
        chatMessage.extraLink = extraLink;
        messageAdapter.add(chatMessage);
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * This function
     *
     * @param userText
     */
    private void sendRequest(String userText) {

        final String contextString = String.valueOf(userText);

        @SuppressLint("StaticFieldLeak") final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];

                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);

                try {
                    return aiDataService.request(request);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResult(response);
                } else {
                    onError(aiError);
                }
            }
        };

        task.execute(contextString);
    }

    /**
     * This function returns the AI query response
     *
     * @param response
     */
    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "onResult");
                Log.i(LOG_TAG, "Received success response");
                // logging response status on the console
                final Status status = response.getStatus();
                Log.i(LOG_TAG, "Status code: " + status.getCode());
                Log.i(LOG_TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                List<AIOutputContext> contextList = result.getContexts();
                ArrayList<String> contextNames = new ArrayList<>();
                for (AIOutputContext context : contextList) {
                    Log.i(LOG_TAG, "context name: " + context.getName());
                    contextNames.add(context.getName());
                }
                Log.i(LOG_TAG, "Resolved query: " + result.getResolvedQuery());
                Log.i(LOG_TAG, "Action: " + result.getAction());

                final String speech = result.getFulfillment().getSpeech();
                Log.i(LOG_TAG, "Speech: " + speech);

                if (!TextUtils.isEmpty(speech))
                    addMessage(speech, false);

                if (!contextNames.isEmpty() &&
                        contextNames.contains("going") && contextNames.contains("car")) {
                    findFastRout();
                }

                if (!contextNames.isEmpty() &&
                        contextNames.contains("library") && contextNames.contains("study_room")) {
                    openLink();
                }

                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(LOG_TAG, "quesry response not empty" + metadata);
                } else {
                    System.out.println(Log.d(LOG_TAG, "Query returned empty response" + metadata.toString()));

                }
            }

        });
    }

    private void openLink() {
        String uri = "https://roombooking.library.dal.ca/schedule.php";
        addMessage("", uri, false);
    }

    private void findFastRout() {
        if (userLocation == null) {
            Toast.makeText(ChatActivity.this, "No location", Toast.LENGTH_SHORT).show();
            return;
        }
//        <item>Kenneth c Rowe,44.637006,-63.5882477</item>
        String buildingInfo = "Kenneth c Rowe";
        //User location
        Location location = getLastKnownLocation();
        String latitude1 = String.valueOf(location.getLatitude());
        String longitude1 = String.valueOf(location.getLongitude());
//        <item>Killam Memorial library (KML),44.6374177,-63.5921144</item>
        String latitude2 = String.valueOf("44.6374177");
        String longitude2 = String.valueOf("-63.5921144");
//               Building Location
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitude1 + "," + longitude1 + "&daddr=" + latitude2 + "," + longitude2;
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//        startActivity(Intent.createChooser(intent, "Select an application"));
        addMessage("'", uri, false);
    }

    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addMessage(error.toString(), false);
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 101);
        } else
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, "asdasdasd", Toast.LENGTH_SHORT).show();
        userLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
/**
 * https://stackoverflow.com/questions/20438627/getlastknownlocation-returns-null
 */


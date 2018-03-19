package ca.connect.dal.dalconnect;

import android.annotation.SuppressLint;
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

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;


public class ChatActivity extends AppCompatActivity {

    public static final String LOG_TAG = ChatActivity.class.getName();

    ListView conversationList;
    ImageButton sendKey;
    EditText inputText;
    AIConfiguration aiConfiguration;
    AIDataService aiDataService;

    public static ArrayList<MessageData> messageList;
    public static MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Currently supported language; the set the language to english
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

    private void addMessage(String message, boolean isUser) {

        final MessageData chatMessage = new MessageData(isUser, "", "", "");
        chatMessage.messageBody = message;
        messageAdapter.add(chatMessage);
        messageAdapter.notifyDataSetChanged();
    }

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

    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Log.d(LOG_TAG, "onResult");

                Log.i(LOG_TAG, "Received success response");

                // print log messages...
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
                if(contextNames.contains("going")&&contextNames.contains("car")){
                    findFastRout();
                }


                Log.i(LOG_TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(LOG_TAG, "Action: " + result.getAction());

                final String speech = result.getFulfillment().getSpeech();
                Log.i(LOG_TAG, "Speech: " + speech);

                if (!TextUtils.isEmpty(speech))
                    addMessage(speech, false);

                final Metadata metadata = result.getMetadata();
                System.out.println(metadata + "metadata Found+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                if (metadata != null) {
                    Log.i(LOG_TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(LOG_TAG, "Intent name: " + metadata.getIntentName());
                    System.out.println("Intent id: " + metadata.getIntentId());
                    System.out.println("Intent name: " + metadata.getIntentName());
//                    Log.d(LOG_TAG,response.)
                } else {
                    System.out.println("metadata not Found+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                }
            }

        });
    }

    private void findFastRout() {
        Toast.makeText(this, "Find Fast Rout", Toast.LENGTH_SHORT).show();
    }

    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addMessage(error.toString(), false);
            }
        });
    }
}

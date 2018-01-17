package com.raywenderlich.facespotter;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HealthActivity extends ActionBarActivity implements AIListener {
    private Button listenButton;
    private TextView resultTextView;
    private TextView userInput;
    private TextView aiResponse;
    private AIService aiService;
    List<String> myItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = (Button) findViewById(R.id.listenButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        userInput = (TextView) findViewById(R.id.userInput);
        userInput.setEnabled(false);
        userInput.setKeyListener(null);
        aiResponse = (TextView) findViewById(R.id.aiResponse);
        aiResponse.setEnabled(false);
        aiResponse.setKeyListener(null);

        //populateListView();
        //Enabling scrolling on TextView.
        resultTextView.setMovementMethod(new ScrollingMovementMethod());
        final AIConfiguration config = new AIConfiguration("ab814241a51546d1b18248b08a54c031",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

    }

    //    private void populateListView()
//    {
//        String[] myItems = {"Blue", "Green", "Purple", "Red"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_main, myItems);
//        ListView list = (ListView) findViewById(R.id.listViewMain);
//        list.setAdapter(adapter);
//    }
    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }
    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        userInput.setText(result.getResolvedQuery());
        aiResponse.setText(result.getFulfillment().getSpeech());

        myItems.add(result.getResolvedQuery());
        myItems.add(result.getFulfillment().getSpeech());
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_main, R.id.resultTextView, myItems);
        //ListView list = (ListView) findViewById(R.id.listViewMain);
        //list.setAdapter(adapter);
    }
    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
    }
    @Override
    public void onListeningStarted() {}

    @Override
    public void onListeningCanceled() {}

    @Override
    public void onListeningFinished() {}

    @Override
    public void onAudioLevel(final float level) {}

    public TextView createTextView(String response, boolean user) {
        TextView newresponse = new EditText(HealthActivity.this);
        newresponse.setText(response);
        //RelativeLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, 0, 0, 10);
        //newresponse.setLayoutParams(lp);
        return newresponse;
    }

}

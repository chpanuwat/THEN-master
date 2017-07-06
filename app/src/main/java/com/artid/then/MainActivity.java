package com.artid.then;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyD6Rm3n8Ygq6wygh2515sqiRo60D1i24kc";
    String targetLanguage;
    Handler textViewHandler;
    TextView textView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        textViewHandler = new Handler();

        final Spinner spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguage);
        final String[] Language = getResources().getStringArray(R.array.Language_arrays);
        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Language);
        spinnerLanguage.setAdapter(adapterLanguage);

        /*ggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    targetLanguage = "th";
                } else {
                    targetLanguage = "en";
                }
            }
        });*/
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Language = parent.getItemAtPosition(position).toString();

                if (Language.equals("Thai")) {
                    targetLanguage = "th";
                }
                if (Language.equals("English")) {
                    targetLanguage = "en";
                }
                if (Language.equals("French")) {
                    targetLanguage = "fr";
                }
                if (Language.equals("Chinese")) {
                    targetLanguage = "zh-CN";
                }
                if (Language.equals("Korean")) {
                    targetLanguage = "ko";
                }
                if (Language.equals("Japanese")) {
                    targetLanguage = "ja";
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new  TranslateText().execute(editText.getText().toString());
            }
        });
    }

    private class TranslateText extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Translating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            TranslateOptions options = TranslateOptions.newBuilder()
                    .setApiKey(API_KEY)
                    .build();
            Translate translate = options.getService();
            final Translation translation =
                    translate.translate(params[0],
                            Translate.TranslateOption.targetLanguage(targetLanguage));
            textViewHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (textView != null) {
                        textView.setText(translation.getTranslatedText());
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);
            if(progressDialog.isShowing()) { progressDialog.dismiss(); }
        }
    }
}

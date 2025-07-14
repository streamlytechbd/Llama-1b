package com.llama1b;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_MODEL_FILE = 1;
    private String modelPath = null;
    private long modelHandle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pickModelBtn = findViewById(R.id.pickModelBtn);
        Button runInferenceBtn = findViewById(R.id.runInferenceBtn);
        EditText promptInput = findViewById(R.id.promptInput);
        TextView outputView = findViewById(R.id.outputView);

        pickModelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/octet-stream");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PICK_MODEL_FILE);
        });

        runInferenceBtn.setOnClickListener(v -> {
            if (modelPath != null) {
                if (modelHandle == 0) {
                    modelHandle = NativeLlama.loadModel(modelPath);
                }
                String prompt = promptInput.getText().toString();
                String result = NativeLlama.runInference(modelHandle, prompt);
                outputView.setText(result);
            } else {
                outputView.setText("Please pick a model file first.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MODEL_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                modelPath = FileUtils.getPath(this, uri);
            }
        }
    }
}

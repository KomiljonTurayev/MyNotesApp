package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {

    EditText et_title, et_note;
    ProgressDialog progressDialog;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        et_note = findViewById(R.id.note);
        et_title = findViewById(R.id.title);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mote_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                //Save
                String title = et_title.getText().toString().trim();
                String note = et_note.getText().toString().trim();
                int color = -2184710;

                if (title.isEmpty()) {
                    et_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    et_note.setError("Please enter  a note");
                } else {
                    saveNote(title, note, color);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveNote(final String title, final String note, final int color) {
        progressDialog.show();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.saveNote(title, note, color);

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {

                    Boolean success = response.body().getSuccess();
                    Toast.makeText(EditorActivity.this,
                            "$response.body().getMessage() saveNote true",
                            Toast.LENGTH_SHORT).show();
                    Log.d("TTT", "${response.body().getMessage()} saveNote true");
                    if (success) {
                        finish();//back to main activity

                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditorActivity.this,
                        t.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}

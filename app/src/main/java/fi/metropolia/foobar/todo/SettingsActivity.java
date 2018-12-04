package fi.metropolia.foobar.todo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends TransitionActivity {
    private static final String PREF = "Settings";
    private Boolean reopenLast;
    private int selection;
    Spinner colorSpinner;
    Switch reopenLastSwitch;

    /**
     * Get preferences and set saved values to Switch and Spinner.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings"); // Set Actionbar title
        colorSpinner = (Spinner)findViewById(R.id.colorSpinner);
        reopenLastSwitch = findViewById(R.id.settingsSwitch);
        SharedPreferences getPref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        reopenLast = getPref.getBoolean("reopenLast", false); // Get Switch value
        selection = getPref.getInt("selection", 0); // Get spinner value
        reopenLastSwitch.setChecked(reopenLast); // Set Switch value
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.color_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);
        colorSpinner.setSelection(selection); // Set Spinner value
    }

    /**
     * Saving preferences after user leaves the activity
     */
    @Override
    public void onPause() {
        SharedPreferences prefPut = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        prefEditor.putBoolean("reopenLast", reopenLastSwitch.isChecked());
        prefEditor.putInt("selection", colorSpinner.getSelectedItemPosition());
        Log.d("Settings", "Item pos:" + colorSpinner.getSelectedItemPosition());
        Log.d("Settings", "selection: " + selection);
        prefEditor.commit();

        super.onPause();
    }
}

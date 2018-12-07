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

/**
 * Activity class for settings
 */
public class SettingsActivity extends TransitionActivity {
    private static final String PREF = "Settings";
    private Boolean reopenLast;
    private int selection;
    Spinner colorSpinner;
    Switch reopenLastSwitch;

    /**
     * Get preferences and set saved values to widgets.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings"); // Set Actionbar title
        // Get widgets
        colorSpinner = findViewById(R.id.colorSpinner);
        reopenLastSwitch = findViewById(R.id.settingsSwitch);
        // Get Settings preferences
        SharedPreferences getPref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        reopenLast = getPref.getBoolean("reopenLast", false); // Get Switch value
        selection = getPref.getInt("selection", 0); // Get spinner value
        reopenLastSwitch.setChecked(reopenLast); // Set Switch value
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.color_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter); // Set adapter for the spinner
        colorSpinner.setSelection(selection); // Set Spinner value
    }

    /**
     * Override default onPause method to save current settings to preferences
     */
    @Override
    public void onPause() {
        SharedPreferences prefPut = getSharedPreferences(PREF, Activity.MODE_PRIVATE); // Get Settings Preferences
        SharedPreferences.Editor prefEditor = prefPut.edit(); // Preferences Editor
        prefEditor.putBoolean("reopenLast", reopenLastSwitch.isChecked()); // Save Switch value
        prefEditor.putInt("selection", colorSpinner.getSelectedItemPosition()); // Save selected color's index
        prefEditor.commit(); // Commit changes

        super.onPause();
    }
}

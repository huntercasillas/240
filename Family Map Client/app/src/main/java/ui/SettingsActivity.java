package ui;

import model.Model;
import model.Settings;
import net.ServerProxy;
import android.os.Bundle;
import android.view.View;
import android.os.AsyncTask;
import android.widget.Toast;
import android.widget.Switch;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.CompoundButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import casillas.familymapclient.R;

public class SettingsActivity extends AppCompatActivity {

    // Declare variables
    private Model model;
    private boolean settingsChanged;
    private Spinner mapTypeSpinner, lifeStorySpinner, familyTreeSpinner, spouseSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get current model
        model = Model.getModel();

        // Set settings changed to false as default
        settingsChanged = false;

        // Declare and assign variables to corresponding xml id's
        mapTypeSpinner = (Spinner) findViewById(R.id.mapTypeSpinner);
        lifeStorySpinner = (Spinner) findViewById(R.id.lifeStorySpinner);
        familyTreeSpinner = (Spinner) findViewById(R.id.familyTreeSpinner);
        spouseSpinner = (Spinner) findViewById(R.id.spouseSpinner);
        Switch lifeStorySwitch = (Switch) findViewById(R.id.lifeStorySwitch);
        Switch familyTreeSwitch = (Switch) findViewById(R.id.familyTreeSwitch);
        Switch spouseSwitch = (Switch) findViewById(R.id.spouseSwitch);
        LinearLayout reSyncButton = (LinearLayout) findViewById(R.id.reSyncDataButton);
        LinearLayout logoutButton = (LinearLayout) findViewById(R.id.logoutButton);

        // Create and add adapters for map type and line color
        ArrayAdapter<CharSequence> mapType = ArrayAdapter.createFromResource(this,
                R.array.mapType, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> lineColors = ArrayAdapter.createFromResource(this,
                R.array.lineColors, android.R.layout.simple_spinner_item);
        mapType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply adapter to spinners
        lifeStorySpinner.setAdapter(lineColors);
        familyTreeSpinner.setAdapter(lineColors);
        spouseSpinner.setAdapter(lineColors);
        mapTypeSpinner.setAdapter(mapType);

        if (model.getSettingsStatus()) {
            // Get settings from model and set switch settings
            Settings settings = model.getSettings();
            spouseSwitch.setChecked(settings.getSpouseStatus());
            familyTreeSwitch.setChecked(settings.getFamilyTreeStatus());
            lifeStorySwitch.setChecked(settings.getLifeStoryStatus());

            // Set map type options and default
            switch (settings.getMapType()) {
                case "Normal" :  mapTypeSpinner.setSelection(0);
                    break;
                case "Hybrid" :  mapTypeSpinner.setSelection(1);
                    break;
                case "Satellite" :  mapTypeSpinner.setSelection(2);
                    break;
                case "Terrain" :  mapTypeSpinner.setSelection(3);
                    break;
                default: mapTypeSpinner.setSelection(0);
            }
            // Set spouse color options and default
            switch (settings.getSpouseColor()) {
                case "Red" :  spouseSpinner.setSelection(0);
                    break;
                case "Green" :  spouseSpinner.setSelection(1);
                    break;
                case "Blue" :  spouseSpinner.setSelection(2);
                    break;
                default: spouseSpinner.setSelection(2);
            }
            // Set family tree color options and default
            switch (settings.getFamilyTreeColor()) {
                case "Red" :  familyTreeSpinner.setSelection(0);
                    break;
                case "Green" :  familyTreeSpinner.setSelection(1);
                    break;
                case "Blue" :  familyTreeSpinner.setSelection(2);
                    break;
                default: familyTreeSpinner.setSelection(1);
            }
            // Set life story color options and default
            switch (settings.getLifeStoryColor()) {
                case "Red" :  lifeStorySpinner.setSelection(0);
                    break;
                case "Green" :  lifeStorySpinner.setSelection(1);
                    break;
                case "Blue" :  lifeStorySpinner.setSelection(2);
                    break;
                default: lifeStorySpinner.setSelection(0);
            }
        } else {
            // Set default switch values
            lifeStorySwitch.setChecked(true);
            familyTreeSwitch.setChecked(true);
            spouseSwitch.setChecked(true);

            // Set default spinner selection values
            mapTypeSpinner.setSelection(0);
            lifeStorySpinner.setSelection(0);
            familyTreeSpinner.setSelection(1);
            spouseSpinner.setSelection(2);
        }

        // Add on click listener for reSync button
        reSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If reSync button is clicked, get updated family data from the server
                DataSyncTask getUpdatedData = new DataSyncTask();
                getUpdatedData.execute();
            }
        });

        // Add on click listener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If logout button is clicked, set the model authtoken to null
                model = Model.getModel();
                model.setAuthToken(null);

                // Reset default spinner selection values
                mapTypeSpinner.setSelection(0);
                lifeStorySpinner.setSelection(0);
                familyTreeSpinner.setSelection(1);
                spouseSpinner.setSelection(2);

                // Create and show new intent for clicking logout
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Add check changed listener for life story switch
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update settings and settings changed status
                settingsChanged();
                model.getSettings().setLifeStoryStatus(isChecked);
            }
        });

        // Add check changed listener for family tree switch
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update settings and settings changed status
                settingsChanged();
                model.getSettings().setFamilyTreeStatus(isChecked);
            }
        });

        // Add check changed listener for spouse switch
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update settings and settings changed status
                settingsChanged();
                model.getSettings().setSpouseStatus(isChecked);
            }
        });

        // Add item selected listener for life story spinner
        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Set default user interaction boolean to false
            boolean userInteraction = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check if the user has interacted with the spinner yet
                if (userInteraction) {
                    // Get the selected color and update the settings
                    settingsChanged();
                    String lifeStoryLinesColor = (String) parent.getItemAtPosition(position);
                    model.getSettings().setLifeStoryColor(lifeStoryLinesColor);
                }
                // Update user interaction to true
                userInteraction = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        // Add item selected listener for family tree spinner
        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Set default user interaction boolean to false
            boolean userInteraction = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check if the user has interacted with the spinner yet
                if (userInteraction) {
                    // Get the selected color and update the settings
                    settingsChanged();
                    String familyTreeLinesColor = (String) parent.getItemAtPosition(position);
                    model.getSettings().setFamilyTreeColor(familyTreeLinesColor);
                }
                // Update user interaction to true
                userInteraction = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }

        });

        // Add item selected listener for spouse spinner
        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Set default user interaction boolean to false
            boolean userInteraction = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check if the user has interacted with the spinner yet
                if (userInteraction) {
                    // Get the selected color and update the settings
                    settingsChanged();
                    String spouseLinesColor = (String) parent.getItemAtPosition(position);
                    model.getSettings().setSpouseColor(spouseLinesColor);
                }
                // Update user interaction to true
                userInteraction = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Add item selected listener for map type spinner
        mapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Set default user interaction boolean to false
            boolean userInteraction = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check if the user has interacted with the spinner yet
                if (userInteraction) {
                    // Get the selected map type and update the settings
                    settingsChanged();
                    String mapType = (String) parent.getItemAtPosition(position);
                    model.getSettings().setMapType(mapType);
                }
                // Update user interaction to true
                userInteraction = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Get family data from the server
    private class DataSyncTask extends AsyncTask<Void, String, Void> {

        protected Void doInBackground(Void... voids) {
            // Get the necessary server information
            model = Model.getModel();
            String serverHost = model.getServerHost();
            String serverPort = model.getServerPort();

            // Initialize the server proxy
            ServerProxy serverProxy = new ServerProxy();
            try {
                serverProxy.getPersons(serverHost, serverPort);
                serverProxy.getEvents(serverHost, serverPort);

            } catch (Exception e) {
                // Display error message
                publishProgress("Error. Could not reload data.");
            }
            return null;
        }

        protected void onProgressUpdate(String... updateMessage) {
            // Display message update
            Toast.makeText(getBaseContext(), updateMessage[0], Toast.LENGTH_SHORT).show();
        }

        protected void onPostExecute(Void voids) {
            // Reload the map fragment to apply changes
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void settingsChanged() {
        // Set the settingsChanged boolean and model changed settings status to true
        settingsChanged = true;
        model = Model.getModel();
        model.setSettingsStatus(true);
    }

    // Get settingsChanged boolean
    public boolean getChangedSettings () {
        return settingsChanged;
    }

    @Override
    public void onBackPressed() {
        // Get the settingsChanged boolean
        boolean changedSettings = getChangedSettings();

        // If any settings have changed, reload the map fragment to apply changes
        if (changedSettings) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            NavUtils.navigateUpTo(this, upIntent);
            settingsChanged = false;
        }
        // Otherwise, let the back button function normally
        else {
            super.onBackPressed();
        }
    }
}
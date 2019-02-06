package ui;

import android.os.Bundle;
import com.joanzapata.iconify.Iconify;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import casillas.familymapclient.R;

public class MapActivity extends AppCompatActivity {

    // Declare fragment manager
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Iconify.with(new FontAwesomeModule());
        displayMapFragment();
    }

    // Create and display the maps fragment
    public void displayMapFragment() {
        MapFragment mapsFragment = new MapFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.mapActivityLayout, mapsFragment)
                .commit();
        mapsFragment.setMainStatus(false);
    }
}
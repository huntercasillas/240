package ui;

import model.Model;
import android.os.Bundle;
import com.joanzapata.iconify.Iconify;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import casillas.familymapclient.R;

public class MainActivity extends AppCompatActivity {

    // Declare fragment manager
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Model model = Model.getModel();
        Iconify.with(new FontAwesomeModule());

        // If this is the first time opening the app, create and display a new login fragment
        if (savedInstanceState == null) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.mainActivityLayout, loginFragment)
                    .commit();
        }
        // If the user has successfully logged in, display the map fragment
        if (model.getAuthToken() != null) {
            displayMapFragment();
        }
    }

    // Create and display the maps fragment
    public void displayMapFragment() {
        MapFragment mapsFragment = new MapFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.mainActivityLayout, mapsFragment)
                .commit();
        mapsFragment.setMainStatus(true);
    }
}
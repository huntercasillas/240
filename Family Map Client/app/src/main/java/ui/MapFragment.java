package ui;

import model.Model;
import model.Event;
import model.Lines;
import model.Filter;
import model.Person;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import android.view.Menu;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.graphics.drawable.Drawable;
import com.joanzapata.iconify.IconDrawable;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_MAGENTA;
import casillas.familymapclient.R;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    // Declare variables
    private View mapView;
    private Model model;
    private ImageView iconView;
    private GoogleMap googleMap;
    private Event currentEvent;
    private boolean mainStatus;
    private HashMap<Marker, Event> eventMarker;
    private List<Polyline> linesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable options menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize variables
        linesList = new ArrayList<>();
        model = Model.getModel();

        // Set map, icon, and info views to corresponding xml id's
        mapView = inflater.inflate(R.layout.fragment_map, container, false);
        iconView = (ImageView) mapView.findViewById(R.id.genderIcon);
        LinearLayout infoView = (LinearLayout) mapView.findViewById(R.id.infoView);

        // Set default android color and icon to view
        Drawable androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android)
                .colorRes(R.color.colorAndroid).sizeDp(40);
        iconView.setImageDrawable(androidIcon);

        // Apply filters to model
        Filter filters = new Filter();
        filters.createFilters();
        filters.applyFilters();

        // Declare and set support map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapFragment.this.googleMap = googleMap;

                model = Model.getModel();

                // Assign map type options to corresponding google map types
                switch (model.getSettings().getMapType()) {
                    case "Hybrid" : MapFragment.this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case "Satellite" : MapFragment.this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case "Terrain" : MapFragment.this.googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case "Normal" : MapFragment.this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    default: MapFragment.this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                // Create map markers
                makeEventMarkers();

                // Check the main activity status and create/update necessary information
                if (!mainStatus) {
                    makeLines();
                    showSelectedEvent();
                    updateInfoView();
                }
            }
        });

        // Set on click listener for info view
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check to see if the info view is displaying an event or not
                if (currentEvent == null) {
                    return;
                }
                model = Model.getModel();

                // Create and show new intent for clicking on the information window
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("personSelected", model.getAllPersons().get(currentEvent.getPersonID()));
                startActivity(intent);
            }
        });
        return mapView;
    }

    public boolean getMainStatus() {
        return mainStatus;
    }

    public void setMainStatus(boolean mainStatus) {
        this.mainStatus = mainStatus;
    }

    private void makeEventMarkers() {
        // Add on click listener for markers
        googleMap.setOnMarkerClickListener(this);

        // Get filtered events
        model = Model.getModel();
        HashSet<Event> filteredEvents = Model.getModel().getFilteredEvents();
        eventMarker = model.getEventMapMarker();

        for (Event event : filteredEvents) {
            // Get the color of each event
            model = Model.getModel();
            String currentEventColor = model.getEventColors().get(event.getType());
            float finalEventColor;

            // Select event color
            switch (currentEventColor) {
                case "Red":
                    finalEventColor = HUE_RED;
                    break;
                case "Orange":
                    finalEventColor =  HUE_ORANGE;
                    break;
                case "Yellow":
                    finalEventColor =  HUE_YELLOW;
                    break;
                case "Green":
                    finalEventColor =  HUE_GREEN;
                    break;
                case "Blue":
                    finalEventColor =  HUE_BLUE;
                    break;
                case "Violet":
                    finalEventColor =  HUE_VIOLET;
                    break;
                case "Rose":
                    finalEventColor =  HUE_ROSE;
                    break;
                case "Azure":
                    finalEventColor =  HUE_AZURE;
                    break;
                case "Magenta":
                    finalEventColor =  HUE_MAGENTA;
                    break;
                default:
                    finalEventColor =  HUE_CYAN;
            }

            // Get the location of each event
            double latitude = Double.parseDouble(event.getLatitude());
            double longitude = Double.parseDouble(event.getLongitude());
            LatLng location = new LatLng(latitude, longitude);

            // Add the marker for each event to the map
            Marker marker = googleMap.addMarker(new MarkerOptions().position(location)
                    .icon(BitmapDescriptorFactory.defaultMarker(finalEventColor)));
            eventMarker.put(marker, event);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Get the current event clicked on from the marker selected
        model = Model.getModel();
        Event currentEvent = eventMarker.get(marker);
        model.setCurrentEvent(currentEvent);

        // Update necessary information
        makeLines();
        updateInfoView();

        // Return false to indicate we should center over the clicked marker
        return false;

    }

    public void makeLines() {

        // Get current model
        model = Model.getModel();

        // Get the current event from the model
        currentEvent = model.getCurrentEvent();

        // Clear all lines from the list
        for (Polyline line : linesList) {
            line.remove();
        }
        linesList.clear();

        // Declare new lines in relation to the current event
        Lines lines = new Lines(currentEvent);

        // If family tree lines are enabled add them to the map
        if (model.getSettings().getFamilyTreeStatus()) {
            addFatherSideLines(lines);
            addMotherSideLines(lines);
        }

        // If life story lines are enabled add them to the map
        if (model.getSettings().getLifeStoryStatus()) {
            addLifeStoryLines(lines);
        }

        // If spouse lines are enabled add them to the map
        if (model.getSettings().getSpouseStatus() && lines.getSpouseLines().size() > 0) {
            addSpouseLines(lines);
        }
    }

    private void addFatherSideLines(Lines lines) {
        // Declare list of locations
        List<LatLng> familyTreeLocations = new ArrayList<>();

        try {
            // Add each event's location to the list
            for (Event event : lines.getFatherSideLines()) {
                familyTreeLocations.add(new LatLng(Double.parseDouble(event.getLatitude()),
                        Double.parseDouble(event.getLongitude())));
            }

            // Get life story line color from settings
            String familyTreeColor = model.getSettings().getFamilyTreeColor();
            int lineColor = Color.BLACK;

            // Check to see what color the setting is set to and set color value accordingly
            switch (familyTreeColor) {
                case "Red":
                    lineColor = Color.RED;
                    break;
                case "Green":
                    lineColor = Color.GREEN;
                    break;
                case "Blue":
                    lineColor = Color.BLUE;
                    break;
            }

            // Declare width and increaseWidth values
            float width = 30;
            float increaseWidth = 1.3f;

            // Add life story lines to the map
            for (int i = 1; i < familyTreeLocations.size(); i++) {
                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.add(familyTreeLocations.get(i-1),
                        familyTreeLocations.get(i)).clickable(false)
                        .color(lineColor).width(width);
                linesList.add(googleMap.addPolyline(lineOptions));
                width = width * increaseWidth;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void addMotherSideLines(Lines lines) {
        // Declare list of locations
        List<LatLng> familyTreeLocations = new ArrayList<>();

        try {
            // Add each event's location to the list
            for (Event event : lines.getMotherSideLines()) {
                familyTreeLocations.add(new LatLng(Double.parseDouble(event.getLatitude()),
                        Double.parseDouble(event.getLongitude())));
            }

            // Get life story line color from settings
            String familyTreeColor = model.getSettings().getFamilyTreeColor();
            int lineColor = Color.BLACK;

            // Check to see what color the setting is set to and set color value accordingly
            switch (familyTreeColor) {
                case "Red":
                    lineColor = Color.RED;
                    break;
                case "Green":
                    lineColor = Color.GREEN;
                    break;
                case "Blue":
                    lineColor = Color.BLUE;
                    break;
            }

            // Declare width and percentage values
            float width = 30;
            float percentage = 1.3f;

            // Add life story lines to the map
            for (int i = 1; i < familyTreeLocations.size(); i++) {
                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.add(familyTreeLocations.get(i-1),
                        familyTreeLocations.get(i)).clickable(false)
                        .color(lineColor).width(width);
                linesList.add(googleMap.addPolyline(lineOptions));
                width = width * percentage;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void addLifeStoryLines(Lines lines) {
        // Declare list of locations and starting width
        List<LatLng> lifeStoryLocations = new ArrayList<>();
        float width = 20;

        try {
            // Add each event's location to the list
            for (Event event : lines.getLifeStoryLines()) {
                lifeStoryLocations.add(new LatLng(Double.parseDouble(event.getLatitude()),
                        Double.parseDouble(event.getLongitude())));
            }

            // Get life story line color from settings
            String lifeStoryColor = model.getSettings().getLifeStoryColor();
            int lineColor = Color.BLACK;

            // Check to see what color the setting is set to and set color value accordingly
            switch (lifeStoryColor) {
                case "Red":
                    lineColor = Color.RED;
                    break;
                case "Green":
                    lineColor = Color.GREEN;
                    break;
                case "Blue":
                    lineColor = Color.BLUE;
                    break;
            }

            // Add life story lines to the map
            for (int i = 1; i < lifeStoryLocations.size(); i++) {
                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.add(lifeStoryLocations.get(i - 1),
                        lifeStoryLocations.get(i)).clickable(false)
                        .color(lineColor).width(width);
                linesList.add(googleMap.addPolyline(lineOptions));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void addSpouseLines(Lines lines) {
        try {
            // Set width and get starting spouse event
            float width = 20;
            Event startingEvent = lines.getSpouseLines().first();

            // Get spouse line color from settings
            String spouseColor = model.getSettings().getSpouseColor();
            int lineColor = Color.BLACK;

            // Check to see what color the setting is set to and set color value accordingly
            switch (spouseColor) {
                case "Red":
                    lineColor = Color.RED;
                    break;
                case "Green":
                    lineColor = Color.GREEN;
                    break;
                case "Blue":
                    lineColor = Color.BLUE;
                    break;
            }

            // Add spouse lines to the map
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.add(new LatLng(Double.parseDouble(currentEvent.getLatitude()),
                            Double.parseDouble(currentEvent.getLongitude())),
                    new LatLng(Double.parseDouble(startingEvent.getLatitude()),
                            Double.parseDouble(startingEvent.getLongitude())))
                    .clickable(false).color(lineColor).width(width);
            linesList.add(googleMap.addPolyline(lineOptions));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void updateInfoView() {
        // Declare variables
        TextView infoTitle;
        TextView infoSubtitle;

        // Set variables to corresponding xml id's
        infoTitle = (TextView) mapView.findViewById(R.id.infoTitle);
        infoSubtitle = (TextView) mapView.findViewById(R.id.infoSubtitle);

        // Get the current person's full name and the current event information
        model = Model.getModel();
        Person currentPerson = model.getPerson(currentEvent.getPersonID());
        String fullName = currentPerson.getFirstName() + " " + currentPerson.getLastName();
        String eventInfo = currentEvent.getType() + ": " + currentEvent.getCity()
                + ", " + currentEvent.getCountry() + " (" + currentEvent.getYear() + ")";

        // Update the info view
        infoTitle.setText(fullName);
        infoSubtitle.setText(eventInfo);

        // Set up male, female, and android icons
        Drawable maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                .colorRes(R.color.colorMale).sizeDp(40);
        Drawable femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                .colorRes(R.color.colorFemale).sizeDp(40);
        Drawable androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android)
                .colorRes(R.color.colorAndroid).sizeDp(40);

        // Add the current person's gender to the info view
        switch (currentPerson.getGender()) {
            case "m":
                iconView.setImageDrawable(maleIcon);
                break;
            case "f":
                iconView.setImageDrawable(femaleIcon);
                break;
            default:
                iconView.setImageDrawable(androidIcon);
                break;
        }
    }

    private void showSelectedEvent() {
        // Get selected event location
        LatLng eventLocation = new LatLng(Double.parseDouble(currentEvent.getLatitude()),
                Double.parseDouble(currentEvent.getLongitude()));

        // Focus map on selected event
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 4));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Check if we are in the main activity
        if (mainStatus) {
            inflater.inflate(R.menu.fragment_map, menu);

            // Create menu bar options (settings, filter, search)
            menu.findItem(R.id.settingsMenuItem).setIcon( new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_gear).colorRes(R.color.colorToolbar).actionBarSize());
            menu.findItem(R.id.filterMenuItem).setIcon(new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_filter).colorRes(R.color.colorToolbar).actionBarSize());
            menu.findItem(R.id.searchMenuItem).setIcon(new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_search).colorRes(R.color.colorToolbar).actionBarSize());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Declare new intent
        Intent intent;

        // Set options for the intent based on settings, filter, or search menu
        switch (menuItem.getItemId()) {
            // Set and start settings menu intent
            case R.id.settingsMenuItem:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            // Set and start filter menu intent
            case R.id.filterMenuItem:
                intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
                return true;
            // Set and start search menu intent
            case R.id.searchMenuItem:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            // Set default
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
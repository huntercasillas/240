package ui;

import model.Model;
import model.Event;
import model.Person;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import casillas.familymapclient.R;

public class PersonActivity extends AppCompatActivity {

    // Declare variables
    private Model model;
    private Person personSelected;
    TextView firstNameTitle, lastNameTitle, genderTitle, lifeEventsTitle, familyTitle;
    EventsAdapter eventsAdapter;
    FamilyAdapter familyAdapter;
    LinearLayoutManager familyLayoutManager, eventsLayoutManager;
    RecyclerView familyRecyclerView, eventsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Get current model and set selected person
        model = Model.getModel();
        personSelected = (Person) getIntent().getSerializableExtra("personSelected");

        // Assign variables to corresponding xml id's
        eventsRecyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        familyRecyclerView = (RecyclerView) findViewById(R.id.familyRecyclerView);
        firstNameTitle = (TextView) findViewById(R.id.firstNameTitle);
        lastNameTitle = (TextView) findViewById(R.id.lastNameTitle);
        genderTitle = (TextView) findViewById(R.id.genderTitle);
        lifeEventsTitle = (TextView) findViewById(R.id.lifeEventsTitle);
        familyTitle = (TextView) findViewById(R.id.familyTitle);

        // Set selected person's first name, last name, and gender titles
        firstNameTitle.setText(personSelected.getFirstName());
        lastNameTitle.setText(personSelected.getLastName());
        if (personSelected.getGender().equals("m")) {
            String maleTitle = "Male";
            genderTitle.setText(maleTitle);
        } else if (personSelected.getGender().equals("f")) {
            String femaleTitle = "Female";
            genderTitle.setText(femaleTitle);
        }

        // Create and set layout managers for family and events
        familyLayoutManager = new LinearLayoutManager(getApplicationContext());
        eventsLayoutManager = new LinearLayoutManager(getApplicationContext());
        familyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eventsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        familyRecyclerView.setLayoutManager(familyLayoutManager);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);

        // Create and set adapters for family and events
        familyAdapter = new FamilyAdapter(getPersonFamily());
        eventsAdapter = new EventsAdapter(getPersonEvents());
        familyRecyclerView.setAdapter(familyAdapter);
        eventsRecyclerView.setAdapter(eventsAdapter);

        // Set on click listeners for life events and family headers
        lifeEventsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapseHeader(eventsRecyclerView);
            }
        });
        familyTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapseHeader(familyRecyclerView);
            }
        });
    }

    public void collapseHeader(RecyclerView collapseRecyclerView) {
        // If the view is already hidden, make the view visible again
        if (collapseRecyclerView.getVisibility() == View.GONE) {
            collapseRecyclerView.setVisibility(View.VISIBLE);
        }
        // Otherwise, hide the view once the header is clicked
        else {
            collapseRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private List<Person> getPersonFamily() {

        // Get current model
        model = Model.getModel();

        TreeMap<String, Person> allPersons = model.getAllPersons();
        String mainPersonID = personSelected.getPersonID();

        // Declare list of family members related to selected person
        List<Person> familyMembers = new ArrayList<>();

        // If the person selected has a spouse
        if (personSelected.getSpouse() != null) {
            Person spouse = model.getPerson(personSelected.getSpouse());
            // Set the relationship type and add them to the list of family members
            spouse.setFamilyRelationship("Spouse");
            familyMembers.add(spouse);
        }

        // If the person selected has a father
        if (personSelected.getFather() != null) {
            Person father = model.getPerson(personSelected.getFather());
            // Set the relationship type and add them to the list of family members
            father.setFamilyRelationship("Father");
            familyMembers.add(father);
        }

        // If the person selected has a mother
        if (personSelected.getMother() != null) {
            Person mother = model.getPerson(personSelected.getMother());
            // Set the relationship type and add them to the list of family members
            mother.setFamilyRelationship("Mother");
            familyMembers.add(mother);
        }

        // If the person selected has children
        for (Person person : allPersons.values()) {

            if (person.getFather() != null && person.getMother() != null) {

                if (person.getFather().equals(mainPersonID)) {
                    // Set the relationship type and add them to the list of family members
                    person.setFamilyRelationship("Child");
                    familyMembers.add(person);
                }

                if (person.getMother().equals(mainPersonID)) {
                    // Set the relationship type and add them to the list of family members
                    person.setFamilyRelationship("Child");
                    familyMembers.add(person);
                }
            }
        }

        return familyMembers;
    }

    private List<Event> getPersonEvents() {

        // Get current model
        model = Model.getModel();

        // Declare list of selected person's events and add all related events
        TreeSet<Event> personEvents = new TreeSet<>();
        for (Event event : model.getFilteredEvents()) {
            if (event.getPersonID().equals(personSelected.getPersonID())) {
                personEvents.add(event);
            }
        }

        // Put the person's events in order
        List<Event> sortedEvents = new ArrayList<>(personEvents);

        // Sort the events by year
        Collections.sort(sortedEvents);

        return sortedEvents;
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

        // Declare list of family members
        private List<Person> familyList;

        private FamilyAdapter(List<Person> familyList) {
            this.familyList = familyList;
        }

        // Get number of family members found
        @Override
        public int getItemCount() {
            return familyList.size();
        }

        @Override
        public FamilyViewHolder onCreateViewHolder(ViewGroup viewGroup, int currentView) {
            final View itemView = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.viewholder_person, viewGroup, false);
            return new FamilyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FamilyViewHolder viewHolder, int currentFamilyMember) {
            // Find the selected family member and get their full name
            Person familyMember = familyList.get(currentFamilyMember);
            String fullName = familyMember.getFirstName() + " " + familyMember.getLastName();

            // Set the family member's title text to the selected person's full name
            viewHolder.titleView.setText(fullName);

            // Set the family member's subtitle text to the relationship type
            viewHolder.subtitleView.setText(familyMember.getFamilyRelationship());

            // set the gender icon of the selected family member
            if (familyMember.getGender().equals("m")) {
                viewHolder.genderIcon.setImageResource(R.drawable.maleicon);
            }
            else if (familyMember.getGender().equals("f")) {
                viewHolder.genderIcon.setImageResource(R.drawable.femaleicon);
            }
        }

        public class FamilyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // Declare variables
            public ImageView genderIcon;
            public TextView titleView;
            public TextView subtitleView;

            private FamilyViewHolder(View familyView) {
                super(familyView);

                // Assign variables to corresponding xml id's
                genderIcon = (ImageView) familyView.findViewById(R.id.iconView);
                titleView = (TextView) familyView.findViewById(R.id.titleView);
                subtitleView = (TextView) familyView.findViewById(R.id.subtitleView);

                // Set on click listener for family members
                familyView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                // Find the family member clicked on
                Person selectedFamilyMember = familyList.get(getAdapterPosition());

                // Create and show new intent for clicking on a family member
                Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                intent.putExtra("personSelected", selectedFamilyMember);
                startActivity(intent);
            }
        }
    }

    private class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

        // Declare list of events
        private List<Event> eventsList;

        private EventsAdapter(List<Event> eventsList) {
            this.eventsList = eventsList;

        }

        // Get number of events found
        @Override
        public int getItemCount() {
            return eventsList.size();
        }

        @Override
        public EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            final View itemView = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.viewholder_person, viewGroup, false);
            return new EventsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventsViewHolder viewHolder, int currentEvent) {
            // Find the selected event and get its title
            Event event = eventsList.get(currentEvent);
            String eventText = event.getType() + ": " + event.getCity()
                    + ", " + event.getCountry() + " (" + event.getYear() + ")";

            // Set the event title text and eventIcon
            viewHolder.eventTitle.setText(eventText);
            viewHolder.eventIcon.setImageResource(R.drawable.locationpin);

            // Get the selected person's full name and set it to the subtitle
            String fullName = personSelected.getFirstName() + " " + personSelected.getLastName();
            viewHolder.eventSubtitle.setText(fullName);
        }

        public class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // Declare variables
            private TextView eventTitle, eventSubtitle;
            private ImageView eventIcon;

            private EventsViewHolder(View eventView) {
                super(eventView);

                // Assign variables to corresponding xml id's
                eventTitle = (TextView) eventView.findViewById(R.id.titleView);
                eventSubtitle = (TextView) eventView.findViewById(R.id.subtitleView);
                eventIcon = (ImageView) eventView.findViewById(R.id.iconView);

                // Set on click listener for events
                eventView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                // Get current model
                model = Model.getModel();

                // Find the event clicked on and set it to the model
                Event clickedEvent = eventsList.get(getAdapterPosition());
                model.setCurrentEvent(clickedEvent);

                // Create and show new intent for clicking on an event
                Intent intent = new Intent(PersonActivity.this, MapActivity.class);
                startActivity(intent);
            }
        }
    }
}
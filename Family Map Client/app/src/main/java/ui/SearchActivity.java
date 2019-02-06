package ui;

import model.Model;
import model.Event;
import model.Person;
import model.Search;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import casillas.familymapclient.R;

public class SearchActivity extends AppCompatActivity {

    // Declare variables
    private Model model;
    SearchView searchBarView;
    EventsAdapter eventsAdapter;
    PersonsAdapter personsAdapter;
    RecyclerView eventsRecyclerView, personsRecyclerView;
    LinearLayoutManager eventsLayoutManager, personsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get current model
        model = Model.getModel();

        // Assign variables to corresponding xml id's
        searchBarView = (SearchView) findViewById(R.id.searchBarView);
        eventsRecyclerView = (RecyclerView) findViewById(R.id.searchEventsRecyclerView);
        personsRecyclerView = (RecyclerView) findViewById(R.id.searchPersonsRecyclerView);

        // Create and set layout managers for events and persons
        eventsLayoutManager = new LinearLayoutManager(getApplicationContext());
        personsLayoutManager = new LinearLayoutManager(getApplicationContext());
        eventsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        personsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        // Set text listener for the search bar
        searchBarView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchRequest) {
                Search searchResult = new Search(searchRequest);
                reloadResults(searchResult);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchRequest) {
                return false;
            }
        });
    }

    private class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonsViewHolder> {

        // List of all the persons found from the search request
        private List<Person> foundPersons;

        PersonsAdapter(List<Person> foundPersons) {
            this.foundPersons = foundPersons;
        }

        // Get number of persons found
        @Override
        public int getItemCount() {
            return foundPersons.size();
        }

        public class PersonsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // Declare variables
            TextView personTitle;
            public ImageView genderIcon;

            PersonsViewHolder(View personView) {
                super(personView);

                // Assign variables to corresponding xml id's
                personTitle = (TextView) personView.findViewById(R.id.searchPersonTitle);
                genderIcon = (ImageView) personView.findViewById(R.id.searchPersonIcon);

                // Set on click listener for persons
                personView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                // Find the person clicked on
                Person selectedPerson = foundPersons.get(getAdapterPosition());

                // Create and show new intent for clicking on a person
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("personSelected", selectedPerson);
                startActivity(intent);
            }
        }

        @Override
        public PersonsViewHolder onCreateViewHolder(ViewGroup viewGroup, int currentView) {
            final View itemView = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.viewholder_search_person, viewGroup, false);
            return new PersonsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PersonsViewHolder viewHolder, int currentPerson) {
            // Find the selected person and get their full name
            Person selectedPerson = foundPersons.get(currentPerson);
            String fullName = selectedPerson.getFirstName() + " " + selectedPerson.getLastName();

            // Set the person title text to the selected person's full name
            viewHolder.personTitle.setText(fullName);

            // set the gender icon of the selected person
            if (selectedPerson.getGender().equals("m")) {
                viewHolder.genderIcon.setImageResource(R.drawable.maleicon);
            } else if (selectedPerson.getGender().equals("f")) {
                viewHolder.genderIcon.setImageResource(R.drawable.femaleicon);
            }
        }
    }

    private class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

        // List of all the events found from the search request
        private List<Event> foundEvents;

        private EventsAdapter(List<Event> foundEvents) {
            this.foundEvents = foundEvents;
        }

        // Get number of events found
        @Override
        public int getItemCount() {
            return foundEvents.size();
        }

        public class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // Declare variables
            private TextView eventTitle;
            private TextView eventSubtitle;
            private ImageView eventIcon;

            private EventsViewHolder(View eventView) {
                super(eventView);

                // Assign variables to corresponding xml id's
                eventTitle = (TextView) eventView.findViewById(R.id.searchEventTitle);
                eventSubtitle = (TextView) eventView.findViewById(R.id.searchEventSubtitle);
                eventIcon = (ImageView) eventView.findViewById(R.id.searchEventIcon);

                // Set on click listener for events
                eventView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                // Get current model
                model = Model.getModel();

                // Find the event clicked on and set it to the model
                Event clickedEvent = foundEvents.get(getAdapterPosition());
                model.setCurrentEvent(clickedEvent);

                // Create and show new intent for clicking on an event
                Intent intent = new Intent(SearchActivity.this, MapActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int currentEvent) {
            final View itemView = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.viewholder_search_event, viewGroup, false);
            return new EventsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventsViewHolder viewHolder, int currentEvent) {
            // Find the selected event and get its title
            Event selectedEvent = foundEvents.get(currentEvent);
            String eventTitle = selectedEvent.getType() + ": " + selectedEvent.getCity()
                    + ", " + selectedEvent.getCountry() + " (" + selectedEvent.getYear() + ")";

            // Set the event title text and event icon
            viewHolder.eventTitle.setText(eventTitle);
            viewHolder.eventIcon.setImageResource(R.drawable.locationpin);

            // Find the person related to the selected event and get their full name
            model = Model.getModel();
            Person selectedPerson = model.getAllPersons().get(selectedEvent.getPersonID());
            String fullName = selectedPerson.getFirstName() + " " + selectedPerson.getLastName();

            // set the event subtitle
            viewHolder.eventSubtitle.setText(fullName);
        }
    }

    // Reload the results by resetting the adapters
    private void reloadResults(Search searchResult) {
        eventsAdapter = new EventsAdapter(searchResult.getFoundEvents());
        personsAdapter = new PersonsAdapter(searchResult.getFoundPersons());
        eventsRecyclerView.setAdapter(eventsAdapter);
        personsRecyclerView.setAdapter(personsAdapter);
    }
}
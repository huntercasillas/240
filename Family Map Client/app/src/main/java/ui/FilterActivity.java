package ui;

import model.Model;
import model.Filter;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import android.widget.Switch;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import casillas.familymapclient.R;

public class FilterActivity extends AppCompatActivity {

    // Declare variables
    private Model model;
    private boolean filterChanged;
    private boolean userInteraction;
    LinearLayoutManager filtersLayoutManager;
    ItemAdapter filtersAdapter;
    RecyclerView filtersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        model = Model.getModel();

        // Set default boolean value to false
        filterChanged = false;

        // Assign variables to corresponding xml id's
        filtersRecyclerView = (RecyclerView) findViewById(R.id.filtersRecyclerView);

        // Create layout manager and assign it
        filtersLayoutManager = new LinearLayoutManager(getApplicationContext());
        filtersLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        filtersRecyclerView.setLayoutManager(filtersLayoutManager);

        // Create adapter and assign it
        filtersAdapter = new ItemAdapter(getAllFilters());
        filtersRecyclerView.setAdapter(filtersAdapter);
    }

    @Override
    public void onBackPressed() {
        // Get the filterChanged boolean
        boolean checkFilters = getFilterChanged();

        // If any filter status has changed, reload the map fragment to apply changes
        if (checkFilters) {
            filterChanged = false;
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            NavUtils.navigateUpTo(this, upIntent);
        }
        // Otherwise, let the back button function normally
        else {
            super.onBackPressed();
        }
    }

    // Get filterChanged boolean
    public boolean getFilterChanged() {
        return filterChanged;
    }

    // Get list of all filters
    private List<Filter> getAllFilters() {
        model = Model.getModel();
        List<Filter> filterList;
        filterList = new ArrayList<>(model.getFilters().values());
        return filterList;
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ListItemViewHolder> {

        // Declare list of filters
        private List<Filter> filterList;

        private ItemAdapter(List<Filter> filterList) {
            this.filterList = filterList;
        }

        // Get number of filters in the list
        @Override
        public int getItemCount() {
            return filterList.size();
        }

        @Override
        public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int currentView) {
            final View filterView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_filter, viewGroup, false);

            return new ListItemViewHolder(filterView);
        }

        @Override
        public void onBindViewHolder(ListItemViewHolder viewHolder, int selectedFilter) {
            // Get the current filter
            Filter currentFilter = filterList.get(selectedFilter);

            // Get and set the current filter title
            String filterTitle = currentFilter.getFilterType();
            viewHolder.filterTitle.setText(filterTitle);

            // Get and set the current filter subtitle
            String filterSubtitle = "Filter by " + currentFilter.getFilterType();
            viewHolder.filterSubtitle.setText(filterSubtitle);

            // Get and set the current filter status (on or off)
            model = Model.getModel();
            boolean filterStatus = model.getFilters()
                    .get(currentFilter.getFilterType()).getFilterStatus();
            viewHolder.filterSwitch.setChecked(filterStatus);
        }

        public class ListItemViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
            // Declare filter related variables
            public Switch filterSwitch;
            public TextView filterTitle;
            public TextView filterSubtitle;

            private ListItemViewHolder(View itemView) {
                super(itemView);
                // Set userInteraction to false, since the user has not touched anything yet
                userInteraction = false;

                // Assign variables to corresponding xml id's
                filterTitle = (TextView) itemView.findViewById(R.id.filterTitle);
                filterSubtitle = (TextView) itemView.findViewById(R.id.filterSubtitle);
                filterSwitch = (Switch) itemView.findViewById(R.id.filterSwitch);

                // Add check changed listener for the filter switch
                filterSwitch.setOnCheckedChangeListener(this);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean filterStatus) {
                // Check if this is the first time the user has changed anything
                if (userInteraction) {
                    model = Model.getModel();
                    // If the user has changed anything, set filterChanged to true
                    filterChanged = true;

                    // Find which filter the user enabled/disabled
                    Filter clickedFilter = filterList.get(getAdapterPosition());

                    // Set the filterStatus of the selected filter (on or off)
                    model.getFilters().get(clickedFilter.getFilterType()).setFilterStatus(filterStatus);
                }
                userInteraction = true;
            }
        }
    }
}
package be.ucll.electroman.activities.ui.home;

import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import be.ucll.electroman.R;
import be.ucll.electroman.activities.SharedDataViewModel;
import be.ucll.electroman.activities.ui.home.NewWorkOrderFragmentDirections;
import be.ucll.electroman.databinding.FragmentNewWorkOrderBinding;
import be.ucll.electroman.models.WorkOrder;

public class NewWorkOrderFragment extends Fragment {

    private static final int MENU_SAVE = 1;
    private static final int MENU_CANCEL = 2;

    private FragmentNewWorkOrderBinding binding;
    private NewWorkOrderViewModel mNewWorkOrderViewModel;
    private String loggedInUserName;
    private SharedDataViewModel sharedDataViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        mNewWorkOrderViewModel =
                new ViewModelProvider(this).get(NewWorkOrderViewModel.class);

        // Get logged in username
        // get the logged in username when passed via safe-args
        if (getArguments().getString("loggedInUserName") != null && !getArguments().getString("loggedInUserName").isBlank()) {
            loggedInUserName = RepairDetailFragmentArgs.fromBundle(getArguments()).getLoggedInUserName();
            mNewWorkOrderViewModel.setLoggedInUserName(loggedInUserName);
        }

        binding = FragmentNewWorkOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Hide the drawer
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        ActionBarDrawerToggle actionBarDrawerToggle = sharedDataViewModel.getActionBarDrawerToggle();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        ActionBar actionBar = sharedDataViewModel.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        DrawerLayout drawerLayout = sharedDataViewModel.getDrawerLayout();
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.add(0, MENU_SAVE, Menu.NONE, getString(R.string.menu_action_save)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, MENU_CANCEL, Menu.NONE, getString(R.string.menu_action_cancel)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        NewWorkOrderFragmentDirections.ActionNavNewWorkOrderToNavWorkOrderOverview action = NewWorkOrderFragmentDirections.actionNavNewWorkOrderToNavWorkOrderOverview();
        action.setLoggedInUserName(loggedInUserName);

        switch (item.getItemId()) {
            // Save
            case MENU_SAVE:
                // Save new work order
                Log.i("NewWorkOrderFragment", "Menu item Save was clicked!!!");
                WorkOrder newWorkOrder = new WorkOrder();
                newWorkOrder.setCity(binding.promptNewWorkOrderCity.getText().toString());
                newWorkOrder.setDevice(binding.promptNewWorkOrderDevice.getText().toString());
                newWorkOrder.setCustomerName(binding.promptNewWorkOrderCustomerName.getText().toString());
                newWorkOrder.setProblemCode(binding.promptNewWorkOrderProblemCode.getText().toString());
                newWorkOrder.setUserId(mNewWorkOrderViewModel.getUserId(loggedInUserName));
                // TODO Error handling. Check if Workorder already exists
                mNewWorkOrderViewModel.insertWorkOrder(newWorkOrder);
                Navigation.findNavController(getView()).navigate(action);

                return true;
                // Cancel
            case MENU_CANCEL:
                Log.i("NewWorkOrderFragment", "Menu item Cancel was clicked!!!");
                Navigation.findNavController(getView()).navigate(action);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

}

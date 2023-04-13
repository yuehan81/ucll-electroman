package be.ucll.electroman.activities.ui.home;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import be.ucll.electroman.R;
import be.ucll.electroman.activities.SharedDataViewModel;
import be.ucll.electroman.databinding.FragmentNewWorkOrderBinding;
import be.ucll.electroman.models.WorkOrder;

public class NewWorkOrderFragment extends Fragment {

    private static final int MENU_SAVE = 1;
    private static final int MENU_CANCEL = 2;

    private FragmentNewWorkOrderBinding binding;
    private NewWorkOrderViewModel mNewWorkOrderViewModel;
    private String loggedInUserName;
    private SharedDataViewModel sharedDataViewModel;
    private View root;


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
        root = binding.getRoot();

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

        // Hide the error message if no account creation issue was detected earlier
        if (!mNewWorkOrderViewModel.isCreateWorkOrderError()) {
            binding.newWorkOrderErrorMessage.setVisibility(View.GONE);
        }

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
                String city = binding.promptNewWorkOrderCity.getText().toString().trim();
                String device = binding.promptNewWorkOrderDevice.getText().toString().trim();
                String customerName = binding.promptNewWorkOrderCustomerName.getText().toString().trim();
                String problemCode = binding.promptNewWorkOrderProblemCode.getText().toString().trim();
                String problemDescription = binding.promptNewWorkOrderProblemDescription.getText().toString().trim();
                WorkOrder newWorkOrder = new WorkOrder();
                newWorkOrder.setCity(city);
                newWorkOrder.setDevice(device);
                newWorkOrder.setCustomerName(customerName);
                newWorkOrder.setProblemCode(problemCode);
                newWorkOrder.setDetailedProblemDescription(problemDescription);
                newWorkOrder.setUserId(mNewWorkOrderViewModel.getUserId(loggedInUserName));
                if (mNewWorkOrderViewModel.isWorkOrderExisting(newWorkOrder.getCity(), newWorkOrder.getDevice(), newWorkOrder.getCustomerName())) {
                    binding.newWorkOrderErrorMessage.setVisibility(View.VISIBLE);
                    binding.newWorkOrderErrorMessage.setText(String.format("Not saved: %s already added for %s", newWorkOrder.getDevice(), newWorkOrder.getCustomerName()));
                    binding.newWorkOrderErrorMessage.setFocusable(true);
                    mNewWorkOrderViewModel.setCreateWorkOrderError(true);
                    closeKeyboard();

                } else if (city.isEmpty() || device.isEmpty() || customerName.isEmpty() || problemCode.isEmpty() || problemDescription.isEmpty()) {
                    binding.newWorkOrderErrorMessage.setVisibility(View.VISIBLE);
                    binding.newWorkOrderErrorMessage.setText("Not saved: All fields have to be filled in!");
                    binding.newWorkOrderErrorMessage.setFocusable(true);
                    mNewWorkOrderViewModel.setCreateWorkOrderError(true);
                    closeKeyboard();
                } else {
                    mNewWorkOrderViewModel.insertWorkOrder(newWorkOrder);
                    mNewWorkOrderViewModel.setCreateWorkOrderError(false);
                    Navigation.findNavController(getView()).navigate(action);
                }


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

    private void closeKeyboard() {
        InputMethodManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            manager = (InputMethodManager) root.getContext().getSystemService(INPUT_METHOD_SERVICE);
        }
        manager.hideSoftInputFromWindow(root.getWindowToken(), 0);
    }

}

package be.ucll.electroman.activities.ui.home;


import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import be.ucll.electroman.R;
import be.ucll.electroman.activities.SharedDataViewModel;
import be.ucll.electroman.databinding.FragmentRepairDetailBinding;

public class RepairDetailFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MENU_SAVE = 1;
    private static final int MENU_CANCEL = 2;

    private static final int MENU_REOPEN = 3;
    private static final int DRAWER_MENU_WORK_ORDER_OVERVIEW = 1;

    private RepairDetailViewModel repairDetailViewModel;
    private FragmentRepairDetailBinding binding;

    private Boolean reOpened;
    private View root;
    private String loggedInUserName;
    private SharedDataViewModel sharedDataViewModel;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // get the workOrderId passed via safe-args when navigating from WorkOrderOverviewFragment or from itself to this fragment
        Long workOrderId = RepairDetailFragmentArgs.fromBundle(getArguments()).getWorkOrderId();
        // get the reOpened boolean passed via safe-args when navigating from RepairDetailFragement to itself after
        // reopening readonly repair detail
        reOpened = RepairDetailFragmentArgs.fromBundle(getArguments()).getReOpened();

        repairDetailViewModel =
                new ViewModelProvider(this).get(RepairDetailViewModel.class);

        // Get logged in username
        // get the logged in username when passed via safe-args
        if (getArguments().getString("loggedInUserName") != null && !getArguments().getString("loggedInUserName").isBlank()) {
            loggedInUserName = RepairDetailFragmentArgs.fromBundle(getArguments()).getLoggedInUserName();
            repairDetailViewModel.setLoggedInUserName(loggedInUserName);
        }


        repairDetailViewModel.setWorkOrderId(workOrderId);
        repairDetailViewModel.loadRepairDetails(workOrderId);

        binding = FragmentRepairDetailBinding.inflate(inflater, container, false);
        binding.detailedProblemDescription.setText(repairDetailViewModel.getDetailedProblemDescription());

        // if already processed the work order details should be readonly
        if (repairDetailViewModel.isReadonly()) {
            binding.repairInformation.setVisibility(View.INVISIBLE);
            binding.repairInformationReadonly.setVisibility(View.VISIBLE);
            binding.repairInformationReadonly.setText(repairDetailViewModel.getRepairInformation());
        } else {
            binding.repairInformation.setText(repairDetailViewModel.getRepairInformation());
        }


        root = binding.getRoot();
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set drawer, actionBar and actionBarDrawerToggle from shared ViewModel with Main activity as instance variables
        setDrawerAndActionBar();

        navigationView = sharedDataViewModel.getNavigationView();

        createDrawerMenu(view, navigationView);

        binding.repairInformation.setMovementMethod(new ScrollingMovementMethod());
        binding.repairInformationReadonly.setMovementMethod(new ScrollingMovementMethod());


    }

    private void createDrawerMenu(@NonNull View view, NavigationView navigationView) {
        if (repairDetailViewModel.isReadonly()) {
            // Create Drawer menu
            navigationView.getMenu().clear();
            navigationView.getMenu().add(0, DRAWER_MENU_WORK_ORDER_OVERVIEW, Menu.NONE, getString(R.string.menu_work_order_overview));
            navigationView.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_home_24dp));

            showDrawer();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case DRAWER_MENU_WORK_ORDER_OVERVIEW:
                            be.ucll.electroman.activities.ui.home.RepairDetailFragmentDirections.ActionNavRepairDetailToNavWorkOrderOverview action = be.ucll.electroman.activities.ui.home.RepairDetailFragmentDirections.actionNavRepairDetailToNavWorkOrderOverview();
                            action.setLoggedInUserName(loggedInUserName);
                            Navigation.findNavController(view).navigate(action);
                            break;

                    }

                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    return true;
                }
            });

        } else {
            hideDrawer();
        }
    }

    private void hideDrawer() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
    }

    private void showDrawer() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void setDrawerAndActionBar() {
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        actionBarDrawerToggle = sharedDataViewModel.getActionBarDrawerToggle();
        actionBar = sharedDataViewModel.getActionBar();
        drawerLayout = sharedDataViewModel.getDrawerLayout();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        menu.clear();
        if (repairDetailViewModel.isReadonly()) {
            menu.add(0, MENU_REOPEN, Menu.NONE, getString(R.string.menu_action_reopen)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        } else {
            menu.add(0, MENU_SAVE, Menu.NONE, getString(R.string.menu_action_save)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, MENU_CANCEL, Menu.NONE, getString(R.string.menu_action_cancel)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            // Save
            case MENU_SAVE:
                // Save new work order
                Log.i("RepairDetailFragment", "Menu item Save was clicked!!!");
                if (binding.repairInformation.getText().length() == 0) {
                    // When the user saves but no text was entered in the repair information
                    // an error text is shown and navigation is aborted (the user either needs to
                    // enter text or cancel)
                    binding.repairInformationSaveError.setVisibility(View.VISIBLE);
                    closeKeyboard();


                } else {
                    binding.repairInformationSaveError.setVisibility(View.INVISIBLE);
                    repairDetailViewModel.setRepairInformation(binding.repairInformation.getText().toString());
                    repairDetailViewModel.updateWorkOrder(true);
                    // check if this repair detail was reopened. If yes go back to repair detail in readonly mode
                    // If it was not reopened we go back to the WorkOrderOverview fragment
                    if (reOpened) {
                        RepairDetailFragmentDirections.NavRepairDetailActionSelf actionSelf = RepairDetailFragmentDirections.navRepairDetailActionSelf(loggedInUserName);
                        actionSelf.setReOpened(false);
                        actionSelf.setWorkOrderId(repairDetailViewModel.getWorkOrderId());
                        Navigation.findNavController(root).navigate(actionSelf);

                    } else {
                        be.ucll.electroman.activities.ui.home.RepairDetailFragmentDirections.ActionNavRepairDetailToNavWorkOrderOverview action = RepairDetailFragmentDirections.actionNavRepairDetailToNavWorkOrderOverview();
                        action.setLoggedInUserName(loggedInUserName);
                        Navigation.findNavController(root).navigate(action);
                    }
                }
                return true;
            // Cancel
            case MENU_CANCEL:
                Log.i("RepairDetailFragment", "Menu item Cancel was clicked!!!");
                binding.repairInformationSaveError.setVisibility(View.INVISIBLE);
                // check if this repair detail was reopened. If yes go back to repair detail in readonly mode
                // If it was not reopened we go back to the WorkOrderOverview fragment
                if (reOpened) {
                    repairDetailViewModel.updateWorkOrder(true);
                    RepairDetailFragmentDirections.NavRepairDetailActionSelf actionSelf = RepairDetailFragmentDirections.navRepairDetailActionSelf(loggedInUserName);
                    actionSelf.setReOpened(false);
                    actionSelf.setWorkOrderId(repairDetailViewModel.getWorkOrderId());
                    Navigation.findNavController(root).navigate(actionSelf);

                } else {
                    be.ucll.electroman.activities.ui.home.RepairDetailFragmentDirections.ActionNavRepairDetailToNavWorkOrderOverview action = RepairDetailFragmentDirections.actionNavRepairDetailToNavWorkOrderOverview();
                    action.setLoggedInUserName(loggedInUserName);
                    Navigation.findNavController(root).navigate(action);
                }
                return true;

            case MENU_REOPEN:
                Log.i("RepairDetailFragment", "Menu item Reopen was clicked!!!");
                repairDetailViewModel.updateWorkOrder(false);
                RepairDetailFragmentDirections.NavRepairDetailActionSelf actionSelf = RepairDetailFragmentDirections.navRepairDetailActionSelf(loggedInUserName);
                actionSelf.setReOpened(true);
                actionSelf.setWorkOrderId(repairDetailViewModel.getWorkOrderId());
                Navigation.findNavController(root).navigate(actionSelf);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_work_order_overview) {
            // navigate back to WorkOrderOverview fragment
            be.ucll.electroman.activities.ui.home.RepairDetailFragmentDirections.ActionNavRepairDetailToNavWorkOrderOverview action = RepairDetailFragmentDirections.actionNavRepairDetailToNavWorkOrderOverview();
            action.setLoggedInUserName(loggedInUserName);
            Navigation.findNavController(root).navigate(action);
        }

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        createDrawerMenu(root, navigationView);

    }

    private void closeKeyboard() {
        InputMethodManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            manager = (InputMethodManager) root.getContext().getSystemService(INPUT_METHOD_SERVICE);
        }
        manager.hideSoftInputFromWindow(root.getWindowToken(), 0);
    }
}
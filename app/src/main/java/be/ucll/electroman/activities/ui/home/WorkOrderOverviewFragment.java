package be.ucll.electroman.activities.ui.home;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import be.ucll.electroman.R;
import be.ucll.electroman.WorkOrderBaseAdapter;
import be.ucll.electroman.activities.SharedDataViewModel;
import be.ucll.electroman.databinding.FragmentWorkOrderOverviewBinding;
import be.ucll.electroman.models.WorkOrder;

public class WorkOrderOverviewFragment extends Fragment {

    private static final int ACTION_NEW = 1;
    private static final int DRAWER_MENU_LOGOUT = 1;
    private FragmentWorkOrderOverviewBinding binding;
    private WorkOrderOverviewViewModel workOrderOverviewViewModel;

    private List<WorkOrder> workOrders;
    private WorkOrderBaseAdapter workOrderBaseAdapter;
    private GridView workOrderGridView;
    private Context context;
    private String loggedInUserName;
    private NavController navController;
    private NavigationView navigationView;
    private View root;

    private SharedDataViewModel sharedDataViewModel;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        workOrderOverviewViewModel =
                new ViewModelProvider(this).get(WorkOrderOverviewViewModel.class);

        binding = FragmentWorkOrderOverviewBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        // Get logged in username
        loggedInUserName = workOrderOverviewViewModel.getUserName();
        Log.i("WorkOrderOverviewFragment", "logged in username: " + loggedInUserName);
        // get the logged in username when passed via safe-args
        if (getArguments().getString("loggedInUserName") != null && !getArguments().getString("loggedInUserName").isBlank()) {
            Log.i("WorkOrderOverviewFragment", "get logged in username from safe-args from LoginFragment");
            loggedInUserName = WorkOrderOverviewFragmentArgs.fromBundle(getArguments()).getLoggedInUserName();
            workOrderOverviewViewModel.setUserName(loggedInUserName);
        }



        // Set the welcome greeting
        if (loggedInUserName != null && !loggedInUserName.isBlank()) {
            Log.i("WorkOrderOverviewFragment", "logged in username : " + loggedInUserName);
            workOrderOverviewViewModel.setWelcomeGreeting(loggedInUserName);

        }

        final TextView textView = binding.textWelcome;
        workOrderOverviewViewModel.getWelcomeGreeting().observe(getViewLifecycleOwner(), textView::setText);

        // Fill the work order GridView
        workOrderGridView = root.findViewById(R.id.work_order_grid);
        List<WorkOrder> workOrders = workOrderOverviewViewModel.getUserWithWorkOrders(loggedInUserName).getWorkOrders();
        if (workOrderOverviewViewModel.getWorkOrders().isEmpty()) {
            workOrderOverviewViewModel.addWorkOrders(workOrders);
        }

        workOrderOverviewViewModel.getWorkOrders().forEach(workOrder -> {Log.i("WorkOrderOverviewFragment", "work order: " + workOrder);});
        if (isAdded()) {
            workOrderBaseAdapter = new WorkOrderBaseAdapter(getActivity(), workOrderOverviewViewModel.getWorkOrders());
            workOrderGridView.setAdapter(workOrderBaseAdapter);
        }

        workOrderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(root.getContext(), "WorkOrder ID: " + workOrderOverviewViewModel.getWorkOrder(position).getId() +
//                        " processed: " + workOrderOverviewViewModel.getWorkOrder(position).isProcessed(), Toast.LENGTH_SHORT).show();
                be.ucll.electroman.activities.ui.home.WorkOrderOverviewFragmentDirections.RepairDetailAction action =
                        be.ucll.electroman.activities.ui.home.WorkOrderOverviewFragmentDirections.repairDetailAction(workOrderOverviewViewModel.getUserName());
                action.setWorkOrderId(workOrderOverviewViewModel.getWorkOrder(position).getId());
                Navigation.findNavController(view).navigate(action);

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        closeKeyboard();

        // Set drawer, actionBar and actionBarDrawerToggle from shared ViewModel with Main activity as instance variables
        setDrawerAndActionBar();

        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        NavigationView navigationView = sharedDataViewModel.getNavigationView();

        // Create Drawer menu
        if (navigationView != null) {
            navigationView.getMenu().clear();
            navigationView.getMenu().add(0, DRAWER_MENU_LOGOUT, Menu.NONE, getString(R.string.menu_logout));
            navigationView.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_logout_24dp));

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case DRAWER_MENU_LOGOUT:
                            navController.navigate(R.id.nav_login);
                            break;

                    }

                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    return true;
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, ACTION_NEW, Menu.NONE, getString(R.string.action_new_work_order)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case ACTION_NEW:
                WorkOrderOverviewFragmentDirections.NewWorkOrderAction action = WorkOrderOverviewFragmentDirections.newWorkOrderAction();
                action.setLoggedInUserName(loggedInUserName);
                Navigation.findNavController(getView()).navigate(action);
                break;

        }
//        return true;
        return super.onOptionsItemSelected(item);
    }

    private void setDrawerAndActionBar() {
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        actionBarDrawerToggle = sharedDataViewModel.getActionBarDrawerToggle();
        actionBar = sharedDataViewModel.getActionBar();
        drawerLayout = sharedDataViewModel.getDrawerLayout();
    }

    private void closeKeyboard()
    {
        InputMethodManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            manager = (InputMethodManager) root.getContext().getSystemService(INPUT_METHOD_SERVICE);
        }
        manager.hideSoftInputFromWindow(root.getWindowToken(), 0);
    }



}
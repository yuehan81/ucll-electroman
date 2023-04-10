package be.ucll.electroman.activities.ui.home;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import be.ucll.electroman.R;
import be.ucll.electroman.WorkOrderBaseAdapter;
import be.ucll.electroman.activities.MainActivity;
import be.ucll.electroman.activities.ui.home.WorkOrderOverviewFragmentDirections;
import be.ucll.electroman.activities.ui.login.LoginFragmentDirections;
import be.ucll.electroman.databinding.FragmentWorkOrderOverviewBinding;
import be.ucll.electroman.models.WorkOrder;
import be.ucll.electroman.repository.UserRepository;
import be.ucll.electroman.repository.WorkOrderRepository;

public class WorkOrderOverviewFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private static final int ACTION_NEW = 1;
    private static final int ACTION_LOGOUT = 2;
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
        workOrderOverviewViewModel.addWorkOrders(workOrders);

        workOrderOverviewViewModel.getWorkOrders().forEach(workOrder -> {Log.i("WorkOrderOverviewFragment", "work order: " + workOrder);});
        if (isAdded()) {
            workOrderBaseAdapter = new WorkOrderBaseAdapter(getActivity(), workOrderOverviewViewModel.getWorkOrders());
            workOrderGridView.setAdapter(workOrderBaseAdapter);
        }

        workOrderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(root.getContext(), "WorkOrder ID: " + workOrderOverviewViewModel.getWorkOrder(position).getId() +
                        " processed: " + workOrderOverviewViewModel.getWorkOrder(position).isProcessed(), Toast.LENGTH_SHORT).show();
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
//        navigationView = root.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.bringToFront();
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
        menu.add(0, ACTION_LOGOUT, Menu.NONE, getString(R.string.action_logout)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
            case ACTION_LOGOUT:
                Log.i("WorkOrderOverviewFragment", "Logout clicked in toolbar menu");
                navController.navigate(R.id.nav_login);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_work_order_overview:
                navController.navigate(R.id.nav_work_order_overview);
                break;
            case R.id.nav_login:
                Log.i("MainActivity", "Logout clicked in drawer menu");
                navController.navigate(R.id.nav_login);
                break;
        }

        return true;
    }


}
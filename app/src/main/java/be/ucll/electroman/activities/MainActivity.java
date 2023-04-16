package be.ucll.electroman.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import be.ucll.electroman.R;


public class MainActivity extends AppCompatActivity {

    private static final int DRAWER_MENU_LOGOUT = 1;
    private AppBarConfiguration mAppBarConfiguration;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private be.ucll.electroman.databinding.ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private SharedDataViewModel sharedDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = be.ucll.electroman.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        drawerLayout = binding.drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // remove the titles on the action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = binding.navView;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_work_order_overview, R.id.nav_login)
                .setOpenableLayout(drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();

        // share data with fragments in order to use when creating another drawer menu and event handler
        sharedDataViewModel = new ViewModelProvider(this).get(SharedDataViewModel.class);
        sharedDataViewModel.setNavigationView(navigationView);
        sharedDataViewModel.setDrawerLayout(drawerLayout);
        sharedDataViewModel.setActionBar(getSupportActionBar());
        sharedDataViewModel.setActionBarDrawerToggle(actionBarDrawerToggle);

        // Create Drawer menu
        navigationView.getMenu().clear();
        navigationView.getMenu().add(0, DRAWER_MENU_LOGOUT, Menu.NONE, getString(R.string.menu_logout));
        navigationView.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_logout_24dp));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        // disable the Backup button
//        super.onBackPressed();
        return;

    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }




}
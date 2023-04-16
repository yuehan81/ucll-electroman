package be.ucll.electroman.activities.ui.account;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.sql.Date;
import java.time.LocalDate;

import be.ucll.electroman.R;
import be.ucll.electroman.activities.SharedDataViewModel;
import be.ucll.electroman.databinding.FragmentCreateAccountBinding;
import be.ucll.electroman.models.User;


public class CreateAccountFragment extends Fragment {

    private static final int DRAWER_MENU_WORK_ORDER_OVERVIEW = 1;
    private FragmentCreateAccountBinding binding;
    private CreateAccountViewModel mCreateAccountViewModel;

    private SharedDataViewModel sharedDataViewModel;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        setDrawerAndActionBar();
        hideDrawer();
        actionBar.setTitle("You can scroll this window");
        actionBar.setDisplayShowTitleEnabled(true);

        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        mCreateAccountViewModel =
                new ViewModelProvider(this).get(CreateAccountViewModel.class);

        // Hide the error message if no account creation issue was detected earlier
        if (!mCreateAccountViewModel.isCreateAccountError()) {
            binding.createAccountErrorMessage.setVisibility(View.GONE);
        }


        binding.createAccount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // IMPLEMENT creating account in sqlite db via room
                User user = new User();
                user.setFirstName(binding.firstName.getText().toString().trim());
                user.setLastName(binding.lastName.getText().toString().trim());
                int day = binding.dateOfBirth.getDayOfMonth();
                int month = binding.dateOfBirth.getMonth();
                int year = binding.dateOfBirth.getYear();
                user.setDateOfBirth(Date.valueOf(LocalDate.of(year, month, day).toString()));
                user.setMunicipality(binding.municipality.getText().toString().trim());
                user.setPostalCode(binding.postalCode.getText().toString().trim());
                user.setHouseNumber(binding.houseNumber.getText().toString().trim());
                user.setBox(binding.box.getText().toString().trim());
                user.setUsername(binding.registerUsername.getText().toString().trim());
                user.setPassword(binding.registerPassword.getText().toString());

                boolean isExistingUser = mCreateAccountViewModel.isExistingUserName(user.getUsername());

                if (isExistingUser) {
                    binding.createAccountErrorMessage.setText(getResources().getString(R.string.username_exists_already));
                    binding.createAccountErrorMessage.setVisibility(View.VISIBLE);
                    binding.createAccountErrorMessage.setFocusable(true);
                    closeKeyboard();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        binding.createAccountScrollView.scrollToDescendant(binding.createAccountErrorMessage);
                    }
                    mCreateAccountViewModel.setCreateAccountError(true);
                } else if (binding.registerUsername.getText().toString().trim().isEmpty() || binding.registerPassword.getText().toString().isEmpty()) {
                    // Username and password are required
                    binding.createAccountErrorMessage.setText(getResources().getString(R.string.username_and_password_cannot_be_blank));
                    binding.createAccountErrorMessage.setVisibility(View.VISIBLE);
                    binding.createAccountErrorMessage.setFocusable(true);
                    closeKeyboard();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        binding.createAccountScrollView.scrollToDescendant(binding.createAccountErrorMessage);
                    }
                    mCreateAccountViewModel.setCreateAccountError(true);
                } else if (!binding.termsAndConditions.isChecked()) {
                    binding.createAccountErrorMessage.setText(getResources().getString(R.string.terms_and_conditions_need_to_be_checked));
                    binding.createAccountErrorMessage.setVisibility(View.VISIBLE);
                    binding.createAccountErrorMessage.setFocusable(true);
                    closeKeyboard();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        binding.createAccountScrollView.scrollToDescendant(binding.createAccountErrorMessage);
                    }
                    mCreateAccountViewModel.setCreateAccountError(true);
                } else {
                    long result = mCreateAccountViewModel.createAccount(user);
                    Log.i("CreateAccountFragment", "Account creation status: " + String.valueOf(result));
                    Log.i("CreateAccountFragment", user.getDateOfBirth().toString());
                    Log.i("CreateAccountFragment", mCreateAccountViewModel.findByUsername(user.getUsername()).getDateOfBirth().toString());
                    mCreateAccountViewModel.setCreateAccountError(false);
                    actionBar.setDisplayShowTitleEnabled(false);
                    Navigation.findNavController(view).navigate(R.id.nav_login);
                }


            }
        });

        DatePicker datePicker = (DatePicker) root.findViewById(R.id.date_of_birth);
        binding.createAccountScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                requestDisallowParentInterceptTouchEvent(datePicker, true);
            }
        });

        // Set focus on First Name field
        binding.firstName.setFocusable(true);
        binding.firstName.requestFocus();

        return root;
    }


    private void requestDisallowParentInterceptTouchEvent(View v, Boolean disallowIntercept) {
        while (v.getParent() != null && v.getParent() instanceof View) {
            if (v.getParent() instanceof ScrollView) {
                v.getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
            }
            v = (View) v.getParent();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        actionBar.setDisplayShowTitleEnabled(false);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        hideDrawer();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void hideDrawer() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
    }

    private void setDrawerAndActionBar() {
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        actionBarDrawerToggle = sharedDataViewModel.getActionBarDrawerToggle();
        actionBar = sharedDataViewModel.getActionBar();
        drawerLayout = sharedDataViewModel.getDrawerLayout();
    }

    private void closeKeyboard() {
        InputMethodManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            manager = (InputMethodManager) root.getContext().getSystemService(INPUT_METHOD_SERVICE);
        }
        manager.hideSoftInputFromWindow(root.getWindowToken(), 0);
    }


}
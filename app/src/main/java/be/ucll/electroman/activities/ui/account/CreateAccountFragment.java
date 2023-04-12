package be.ucll.electroman.activities.ui.account;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

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

        setDrawerAndActionBar();
        hideDrawer();

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
                user.setDateOfBirth(binding.dateOfBirth.getText().toString().trim());
                user.setMunicipality(binding.municipality.getText().toString().trim());
                user.setPostalCode(binding.postalCode.getText().toString().trim());
                user.setHouseNumber(binding.houseNumber.getText().toString().trim());
                user.setBox(binding.box.getText().toString().trim());
                user.setUsername(binding.registerUsername.getText().toString().trim());
                user.setPassword(binding.registerPassword.getText().toString());

                boolean isExistingUser = mCreateAccountViewModel.isExistingUserName(user.getUsername());

                if (isExistingUser) {
                    binding.createAccountErrorMessage.setVisibility(View.VISIBLE);
                    binding.createAccountErrorMessage.setFocusable(true);
                    closeKeyboard();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        binding.createAccountScrollView.scrollToDescendant(binding.createAccountErrorMessage);
                    }
                    mCreateAccountViewModel.setCreateAccountError(true);
                } else {
                    mCreateAccountViewModel.createAccount(user);

                    Navigation.findNavController(view).navigate(R.id.nav_login);
                }



            }
        });


        return root;
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
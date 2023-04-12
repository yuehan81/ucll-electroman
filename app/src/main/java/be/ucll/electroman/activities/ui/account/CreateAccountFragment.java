package be.ucll.electroman.activities.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import be.ucll.electroman.R;
import be.ucll.electroman.databinding.FragmentCreateAccountBinding;
import be.ucll.electroman.models.User;


public class CreateAccountFragment extends Fragment {

    private FragmentCreateAccountBinding binding;
    private CreateAccountViewModel mCreateAccountViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCreateAccountViewModel =
                new ViewModelProvider(this).get(CreateAccountViewModel.class);

        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.createAccount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // IMPLEMENT creating account in sqlite db via room
                User user = new User();
                user.setFirstName(binding.firstName.getText().toString());
                user.setLastName(binding.lastName.getText().toString());
                user.setDateOfBirth(binding.dateOfBirth.getText().toString());
                user.setMunicipality(binding.municipality.getText().toString());
                user.setPostalCode(binding.postalCode.getText().toString());
                user.setHouseNumber(binding.houseNumber.getText().toString());
                user.setBox(binding.box.getText().toString());
                user.setUsername(binding.registerUsername.getText().toString());
                user.setPassword(binding.registerPassword.getText().toString());
                mCreateAccountViewModel.createAccount(user);

                Navigation.findNavController(view).navigate(R.id.nav_login);


            }
        });


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
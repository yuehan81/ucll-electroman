package be.ucll.electroman.activities.ui.login;


import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import be.ucll.electroman.R;
import be.ucll.electroman.activities.SharedDataViewModel;
import be.ucll.electroman.databinding.FragmentLoginBinding;
import be.ucll.electroman.models.User;


public class LoginFragment extends Fragment {

    private long lastClickTime = 0;
    private FragmentLoginBinding binding;
    private SharedDataViewModel sharedDataViewModel;
    private final int WAIT_TIME = 3000;
    private Handler uiHandler;
    public LoginViewModel loginViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        uiHandler = new Handler(); // anything posted to this handler will run on the UI Thread

        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        // Hide the error message if no login error was detected
        if (!loginViewModel.isLoginIssue()) {
            binding.loginErrorMessage.setVisibility(View.GONE);
        }
        // Hide the login successful message if no successful login was detected
        if (!loginViewModel.isLoginSuccessful()) {
            binding.loginSuccessfulMessage.setVisibility(View.GONE);
        }

        root = binding.getRoot();

        final TextView textView = binding.loginErrorMessage;
        loginViewModel.getErrorMessage().observe(getViewLifecycleOwner(), textView::setText);


        binding.loginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.loginButton.performClick();
                    return true;
                }
                return false;
            }
        });


        binding.createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_create_account);
            }
        });

        // Set focus on username field
        binding.loginUsername.setFocusable(true);
        binding.loginUsername.requestFocus();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener loginOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // preventing multiple clicks, using threshold of 4000 ms
                if (SystemClock.elapsedRealtime() - lastClickTime < 4000) {
                    return;
                }

                String userName = binding.loginUsername.getText().toString();
                User user = loginViewModel.findByUserName(userName);
                if (loginViewModel.isUserFound(user)) {
                    // check if password is correct
                    Log.i("LoginFragment", "Filled in password: " + binding.loginPassword.getText().toString());
                    Log.i("LoginFragment", "Stored password in DB : " + user.getPassword());
                    if (binding.loginPassword.getText().toString().equals(user.getPassword())) {
                        // If login successful redirect to the next Fragment
                        // But first show a message for 3 seconds that the login was successful
                        if (loginViewModel.isLoginIssue()) {
                            binding.loginErrorMessage.setVisibility(View.GONE);
                        }
                        binding.loginSuccessfulMessage.setVisibility(View.VISIBLE);
                        closeKeyboard();
                        loginViewModel.setLoginSuccessful(true);


                        Runnable onUi = () -> {
                            // this will run on the main UI thread
                            LoginFragmentDirections.LoginSuccessfulAction action = LoginFragmentDirections.loginSuccessfulAction();
                            action.setLoggedInUserName(userName);
                            Navigation.findNavController(view).navigate(action);
                        };

                        Runnable background = () -> {
                            // This is the delay
                            SystemClock.sleep(WAIT_TIME);
                            // This will run on a background thread
                            System.out.println("Going to WorkOrderOverviewFragment");
                            uiHandler.post(onUi);
                        };

                        new Thread(background).start();
                    } else {
                        // password was not correct. show error
                        loginViewModel.setLoginIssue(true);
                        binding.loginErrorMessage.setVisibility(View.VISIBLE);
                        closeKeyboard();
                    }
                } else {
                    // user was not found. show error
                    loginViewModel.setLoginIssue(true);
                    binding.loginErrorMessage.setVisibility(View.VISIBLE);
                    closeKeyboard();
                }


                lastClickTime = SystemClock.elapsedRealtime();

            }
        };

        binding.loginButton.setOnClickListener(loginOnClickListener);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void closeKeyboard() {
        InputMethodManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            manager = (InputMethodManager) root.getContext().getSystemService(INPUT_METHOD_SERVICE);
        }
        manager.hideSoftInputFromWindow(root.getWindowToken(), 0);
    }


}
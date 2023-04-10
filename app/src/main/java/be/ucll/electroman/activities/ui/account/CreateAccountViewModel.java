package be.ucll.electroman.activities.ui.account;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import be.ucll.electroman.models.User;
import be.ucll.electroman.repository.UserRepository;

public class CreateAccountViewModel extends AndroidViewModel {

    private final UserRepository mUserRepository;

    public CreateAccountViewModel(Application application) {
        super(application);
        mUserRepository = new UserRepository(application);

    }

    public void createAccount(User user) {
        mUserRepository.insert(user);
    }
}
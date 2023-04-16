package be.ucll.electroman.activities.ui.account;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import be.ucll.electroman.models.User;
import be.ucll.electroman.repository.UserRepository;

public class CreateAccountViewModel extends AndroidViewModel {

    private final UserRepository mUserRepository;
    private boolean createAccountError;

    public CreateAccountViewModel(Application application) {
        super(application);
        mUserRepository = new UserRepository(application);
        createAccountError = false;

    }

    public boolean isCreateAccountError() {
        return createAccountError;
    }

    public void setCreateAccountError(boolean createAccountError) {
        this.createAccountError = createAccountError;
    }

    public long createAccount(User user) {

        return mUserRepository.insert(user);

    }

    public User findByUsername(String userName) {
        return mUserRepository.findByUserName(userName);
    }

    public boolean isExistingUserName(String userName) {
        if (mUserRepository.findByUserName(userName) != null) {
            return true;
        }
        return false;
    }
}
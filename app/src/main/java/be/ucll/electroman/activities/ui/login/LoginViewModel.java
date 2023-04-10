package be.ucll.electroman.activities.ui.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import be.ucll.electroman.bootstrap.Bootstrap;
import be.ucll.electroman.database.WorkOrderDao;
import be.ucll.electroman.models.User;
import be.ucll.electroman.repository.UserRepository;
import be.ucll.electroman.repository.WorkOrderRepository;

public class LoginViewModel extends AndroidViewModel {

    private final UserRepository mUserRepository;
    private final WorkOrderRepository mWorkOrderRepository;
    private final MutableLiveData<String> mErrorMessage;

    private boolean loginIssue;

    public LoginViewModel(Application application) {
        super(application);
        loginIssue = false;
        mUserRepository = new UserRepository(application);
        mWorkOrderRepository = new WorkOrderRepository(application);
        mErrorMessage = new MutableLiveData<>();
        // Fill the database with 1 initial user and 5 workorders
//        Bootstrap.prepareDatabase(mUserRepository, mWorkOrderRepository);

    }

    public boolean isLoginIssue() {
        return loginIssue;
    }

    public void setLoginIssue(boolean loginIssue) {
        this.loginIssue = loginIssue;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public User findByUserName(String userName) {
        User user = mUserRepository.findByUserName(userName);
        return user;
    }

    public boolean isUserFound(User user) {
        return user != null;
    }





}
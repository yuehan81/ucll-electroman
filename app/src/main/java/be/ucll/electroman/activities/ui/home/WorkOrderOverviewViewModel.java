package be.ucll.electroman.activities.ui.home;

import android.app.Application;
import android.widget.GridView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import be.ucll.electroman.WorkOrderBaseAdapter;
import be.ucll.electroman.models.User;
import be.ucll.electroman.models.UserWithWorkOrders;
import be.ucll.electroman.models.WorkOrder;
import be.ucll.electroman.repository.UserRepository;
import be.ucll.electroman.repository.WorkOrderRepository;

public class WorkOrderOverviewViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mWelcomeGreeting;
    private final UserRepository mUserRepository;
    private final WorkOrderRepository mWorkOrderRepository;

    private final SavedStateHandle state;

    List<WorkOrder> workOrders;
    private String userName;



    public WorkOrderOverviewViewModel(Application application, SavedStateHandle savedStateHandle) {
        super(application);
        this.workOrders = new ArrayList<WorkOrder>();
        state = savedStateHandle;
        mUserRepository = new UserRepository(application);
        mWorkOrderRepository = new WorkOrderRepository(application);

        mWelcomeGreeting = new MutableLiveData<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public LiveData<String> getWelcomeGreeting() {
        return mWelcomeGreeting;
    }

    public void setWelcomeGreeting(String userName) {
        User user = mUserRepository.findByUserName(userName);
        mWelcomeGreeting.setValue("Welcome " + user.getFirstName() + " " + user.getLastName());
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void addWorkOrder(WorkOrder workOrder) {
        this.workOrders.add(workOrder);
    }

    public void addWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders.addAll(workOrders);
    }

    public WorkOrder getWorkOrder(int index) {
        return this.workOrders.get(index);
    }

    public UserWithWorkOrders getUserWithWorkOrders(String userName) {
        return mUserRepository.getUserWithWorkOrders(userName);
    }

}
package be.ucll.electroman.activities.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import be.ucll.electroman.models.WorkOrder;
import be.ucll.electroman.repository.UserRepository;
import be.ucll.electroman.repository.WorkOrderRepository;

public class NewWorkOrderViewModel extends AndroidViewModel {

    private final WorkOrderRepository workOrderRepository;
    private final UserRepository userRepository;
    private String loggedInUserName;

    private boolean createWorkOrderError;

    public NewWorkOrderViewModel(@NonNull Application application) {
        super(application);
        createWorkOrderError = false;
        workOrderRepository = new WorkOrderRepository(application);
        userRepository = new UserRepository(application);
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
    }

    public boolean isWorkOrderExisting(String city, String device, String customerName) {
        if (workOrderRepository.isWorkOrderExisting(city, device, customerName)) {
            return true;
        }
        return false;
    }
    public void insertWorkOrder(WorkOrder workOrder) {
        workOrderRepository.insert(workOrder);
    }

    public long getUserId(String userName) {
        return userRepository.findByUserName(userName).getUid();
    }

    public boolean isCreateWorkOrderError() {
        return createWorkOrderError;
    }

    public void setCreateWorkOrderError(boolean createWorkOrderError) {
        this.createWorkOrderError = createWorkOrderError;
    }
}

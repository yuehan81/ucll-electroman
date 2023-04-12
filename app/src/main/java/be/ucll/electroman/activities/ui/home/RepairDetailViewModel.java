package be.ucll.electroman.activities.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import be.ucll.electroman.models.WorkOrder;
import be.ucll.electroman.repository.WorkOrderRepository;

public class RepairDetailViewModel extends AndroidViewModel {

    private Long workOrderId;
    private WorkOrder workOrder;

    private boolean readonly;
    private String detailedProblemDescription;
    private String repairInformation;
    private String loggedInUserName;

    private final WorkOrderRepository workOrderRepository;

    public RepairDetailViewModel(Application application) {
        super(application);
        this.workOrderRepository = new WorkOrderRepository(application);
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getDetailedProblemDescription() {
        return detailedProblemDescription;
    }

    public void setDetailedProblemDescription(String detailedProblemDescription) {
        this.detailedProblemDescription = detailedProblemDescription;
    }

    public String getRepairInformation() {
        return repairInformation;
    }

    public void setRepairInformation(String repairInformation) {
        this.repairInformation = repairInformation;
        this.workOrder.setRepairInformation(repairInformation);
    }

    public void loadRepairDetails(Long workOrderId) {
        this.workOrder = workOrderRepository.findByWorkOrderId(workOrderId);
        this.detailedProblemDescription = this.workOrder.getDetailedProblemDescription();
        this.repairInformation = this.workOrder.getRepairInformation();
        this.readonly = this.workOrder.isProcessed();
    }

    public void updateWorkOrder(Boolean processed) {
        this.workOrder.setProcessed(processed);
        this.workOrderRepository.update(this.workOrder);
        Log.i("RepairDetailViewModel", "The following Work Order was updated: " + workOrder);
    }
}
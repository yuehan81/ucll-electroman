package be.ucll.electroman.repository;

import android.app.Application;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;

import be.ucll.electroman.database.ElectromanDatabase;
import be.ucll.electroman.database.UserDao;
import be.ucll.electroman.database.WorkOrderDao;
import be.ucll.electroman.models.User;
import be.ucll.electroman.models.WorkOrder;

public class WorkOrderRepository {
    private final WorkOrderDao mWorkOrderDao;
    private final ElectromanDatabase db;

    public WorkOrderRepository(Application application) {
        db = ElectromanDatabase.getDatabase(application);
        mWorkOrderDao = db.workOrderDao();
    }

    public void insert(WorkOrder workOrder) {
            mWorkOrderDao.insert(workOrder);
    }

    public void insertAll(WorkOrder... workOrders) {
        ElectromanDatabase.databaseWriteExecutor.execute(() -> {
            mWorkOrderDao.insertAll(workOrders);
        });
    }

    public List<WorkOrder> getAll() {
        return mWorkOrderDao.getAll();

    }

    public WorkOrder findByWorkOrderId(Long workOrderId) {
        return mWorkOrderDao.findById(workOrderId);
    }

    public void update(WorkOrder workOrder) {
        mWorkOrderDao.update(workOrder);
    }
}

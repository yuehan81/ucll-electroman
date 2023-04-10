package be.ucll.electroman.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import be.ucll.electroman.models.User;
import be.ucll.electroman.models.WorkOrder;

@Dao
public interface WorkOrderDao {
    @Query("SELECT * FROM work_order")
    List<WorkOrder> getAll();

    @Query("SELECT * FROM work_order WHERE id IN (:workOrderIds)")
    List<WorkOrder> loadAllByIds(long[] workOrderIds);

    @Query("SELECT * FROM work_order WHERE id LIKE :id")
    WorkOrder findById(long id);

    @Insert
    void insertAll(WorkOrder... workOrders);

    @Insert
    void insert(WorkOrder workOrder);

    @Delete
    void delete(WorkOrder workOrder);

    @Update
    int update(WorkOrder workOrder);

}

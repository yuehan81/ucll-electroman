package be.ucll.electroman.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithWorkOrders {
    @Embedded public User user;
    @Relation(
            parentColumn = "uid",
            entityColumn = "userId"
    )
    public List<WorkOrder> workOrders;

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }
}

package be.ucll.electroman.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import be.ucll.electroman.database.ElectromanDatabase;

@Entity(tableName = "work_order")
public class WorkOrder {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String city;
    private String device;
    @ColumnInfo(name = "problem_code")
    private String problemCode;
    @ColumnInfo(name = "customer_name")
    private String customerName;
    private boolean processed;
    @ColumnInfo(name = "detailed_problem_description")
    private String detailedProblemDescription;
    @ColumnInfo(name = "repair_information")
    private String repairInformation;
    private long userId;

    public WorkOrder() {
    }

    public WorkOrder(String city, String device, String problemCode, String customerName, boolean processed, String detailedProblemDescription, long userId) {
        this.city = city;
        this.device = device;
        this.problemCode = problemCode;
        this.customerName = customerName;
        this.processed = processed;
        this.detailedProblemDescription = detailedProblemDescription;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
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
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "WorkOrder{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", device='" + device + '\'' +
                ", problemCode='" + problemCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", processed=" + processed +
                ", detailedProblemDescription='" + detailedProblemDescription + '\'' +
                ", repairInformation='" + repairInformation + '\'' +
                '}';
    }
}

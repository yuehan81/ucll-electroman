package be.ucll.electroman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import be.ucll.electroman.models.WorkOrder;

public class WorkOrderBaseAdapter extends BaseAdapter {
    Context context;
    private final List<WorkOrder> workOrders;

    LayoutInflater inflater;


    public WorkOrderBaseAdapter(@NonNull Context context, @NonNull List<WorkOrder> workOrders) {
        this.context = context;
        this.workOrders = workOrders;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public int getCount() {
        return this.workOrders.size();
    }

    @Override public Object getItem(int position) {
        return null;
    }

    @Override public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup viewGroup) {

        convertView = inflater.inflate(R.layout.work_order_grid_item, null);

        TextView city = convertView.findViewById(R.id.work_order_grid_item_city);
        city.setText(workOrders.get(position).getCity());

        TextView device = convertView.findViewById(R.id.work_order_grid_item_device);
        device.setText(workOrders.get(position).getDevice());

        TextView problemCode = convertView.findViewById(R.id.work_order_grid_item_problem_code);
        problemCode.setText(workOrders.get(position).getProblemCode());

        TextView customerName = convertView.findViewById(R.id.work_order_grid_item_customer_name);
        customerName.setText(workOrders.get(position).getCustomerName());

        CheckBox processed = convertView.findViewById(R.id.work_order_grid_item_processed);
        processed.setChecked(workOrders.get(position).isProcessed());
        processed.setClickable(false);

        return convertView;

    }
}

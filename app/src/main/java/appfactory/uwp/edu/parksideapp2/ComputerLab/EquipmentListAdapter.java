package appfactory.uwp.edu.parksideapp2.ComputerLab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.R;
import io.realm.RealmList;

/**
 * Created by mingxi on 8/4/18.
 */

public class EquipmentListAdapter extends RecyclerView.Adapter<EquipmentListAdapter.EquipmentListViewHolder> {

    private Context mContext;
    RealmList<String> equipmentList = new RealmList<>();

    public EquipmentListAdapter(Context context, RealmList<String> equipmentList){
        this.mContext = context;
        this.equipmentList = equipmentList;
    }

    @NonNull
    @Override
    public EquipmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_view,parent,false);
        return new EquipmentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentListViewHolder holder, int position) {
        holder.equipment.setText(equipmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    public class EquipmentListViewHolder extends RecyclerView.ViewHolder{
        // UI
        private TextView equipment;


        public EquipmentListViewHolder(View itemView){
            super(itemView);
            equipment = (TextView)itemView.findViewById(R.id.lab_equipment);
        }
    }
}

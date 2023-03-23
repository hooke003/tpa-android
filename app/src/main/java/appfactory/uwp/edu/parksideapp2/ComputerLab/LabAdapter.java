package appfactory.uwp.edu.parksideapp2.ComputerLab;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.LabObj;
import appfactory.uwp.edu.parksideapp2.R;

/**
 * Created by kyluong09 on 6/19/18.
 */

public class LabAdapter extends RecyclerView.Adapter<LabAdapter.LabViewHolder> {
    private Context context;
    private LinearLayoutManager lln;
    private EquipmentListAdapter mEquipmentListAdapter;
    private ArrayList<LabObj> labList;

    // testing
    private HashMap<String, List<String>> listDataChild = new HashMap<>();
    private  ExpandableListAdapter listAdapter;

    // height
    private int originalHeight;


    public LabAdapter(Context context, ArrayList<LabObj> labList) {
        this.context = context;
        this.labList = labList;
    }

    @NonNull
    @Override
    public LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_view,parent,false);
        LabViewHolder holder = new LabViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LabViewHolder holder, int position) {
        lln = new LinearLayoutManager(context);
        holder.labBuilding.setText(Html.fromHtml(labList.get(position).getBuilding()));
        holder.labBuildingLevel.setText(Html.fromHtml(labList.get(position).getBuildingLevel()));
        holder.labRoomNumber.setText(Html.fromHtml(labList.get(position).getRoomNumber()));
        holder.windowsComputerNum.setText(Html.fromHtml(Integer.toString(labList.get(position).getAmountWin())));
        holder.macComputerNum.setText(Html.fromHtml(Integer.toString(labList.get(position).getAmountMac())));
        // TODO Lab Equipment List
        mEquipmentListAdapter = new EquipmentListAdapter(context,labList.get(position).getEquipmentNewList());
        holder.capacity.setText(Html.fromHtml(Integer.toString(labList.get(position).getCapacity())));
        holder.classRoomType.setText(Html.fromHtml(labList.get(position).getClassRoomType()));

        prepareListData(position);
        holder.expListView.setAdapter(listAdapter);
        holder.expListView.setOnGroupExpandListener(groupPosition -> {
           holder.labCardView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int childCount = holder.expListView.getCount() - 1;
            int totalChildrenHeight = childCount * (int)(holder.expListView.getMeasuredHeight() / 1.4);
            originalHeight = holder.labCardView.getMeasuredHeight();
            int totalCardViewHeight = totalChildrenHeight + originalHeight;
            holder.labCardView.getLayoutParams().height = (int)(totalCardViewHeight * 1.1);
        });


        holder.expListView.setOnGroupCollapseListener(groupPosition -> holder.labCardView.getLayoutParams().height = originalHeight);



    }

    @Override
    public int getItemCount() {
        return labList.size();
    }


    private void prepareListData(int position) {
        List<String> listDataHeader = new ArrayList<>();
        if (labList.get(position).getInstructorComputerType() == null) {
            listDataHeader.add("null");
        }
        else {
            listDataHeader.add(labList.get(position).getInstructorComputerType());
        }

        List<String> top250 = new ArrayList<String>();
        for (String obj : labList.get(position).getEquipmentNewList()) {
            top250.add(obj);
        }

        listDataChild.put(listDataHeader.get(0), top250);

        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);



    }


    public class LabViewHolder extends RecyclerView.ViewHolder{
        // UI
        private CardView labCardView;
        private TextView labBuilding;
        private TextView labBuildingLevel;
        private TextView labRoomNumber;
        private TextView windowsComputerNum;
        private TextView macComputerNum;
        private TextView capacity;
        private TextView classRoomType;

        // testing
        private ExpandableListView expListView;

        public LabViewHolder(View itemView) {
            super(itemView);
            labCardView = itemView.findViewById(R.id.lab_card_view);
            labBuilding = itemView.findViewById(R.id.lab_building);
            labBuildingLevel = itemView.findViewById(R.id.lab_building_level);
            labRoomNumber = itemView.findViewById(R.id.lab_roomnumber);
            windowsComputerNum = itemView.findViewById(R.id.windows_computer_num);
            macComputerNum = itemView.findViewById(R.id.mac_computer_num);
            capacity = itemView.findViewById(R.id.capacity);
            classRoomType = itemView.findViewById(R.id.class_room_type);
            expListView = itemView.findViewById(R.id.lvExp);
        }
    }
}

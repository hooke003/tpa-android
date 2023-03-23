package appfactory.uwp.edu.parksideapp2.ComputerLab;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.LabBuildingObj;
import appfactory.uwp.edu.parksideapp2.R;

/**
 * Created by mingxi on 7/25/18.
 */

public class LabBuildingAdapter extends RecyclerView.Adapter<LabBuildingAdapter.LabBuildingViewHolder>{
    private Context mContext;
    private ArrayList<LabBuildingObj> labBuildingList;

    public LabBuildingAdapter(Context context, ArrayList<LabBuildingObj> labBuildingList){
        this.mContext = context;
        this.labBuildingList = labBuildingList;
    }

    @NonNull
    @Override
    public LabBuildingAdapter.LabBuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_building_view, parent,false);
        return new LabBuildingViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LabBuildingAdapter.LabBuildingViewHolder holder, final int position) {

        if(labBuildingList.get(position).getBuilding().equals("Molinaro Hall")){
            Picasso.get().load(R.drawable.image_molinaro).fit().into(holder.buildingImage);
            //holder.buildingImage.setImageResource(R.drawable.image_molinaro);
        } else if(labBuildingList.get(position).getBuilding().equals("Greenquist Hall")){
            Picasso.get().load(R.drawable.image_greenquist).fit().into(holder.buildingImage);
        } else if(labBuildingList.get(position).getBuilding().equals("Wyllie Hall")){
            Picasso.get().load(R.drawable.image_wyllie).fit().into(holder.buildingImage);
        } else if(labBuildingList.get(position).getBuilding().equals("Rita")){
            Picasso.get().load(R.drawable.image_rita).fit().into(holder.buildingImage);
        }
        holder.buildingName.setText(Html.fromHtml(labBuildingList.get(position).getBuilding()));
        holder.number.setText(Html.fromHtml(Integer.toString(labBuildingList.get(position).getLabsNumber())));
        holder.buildingCardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,LabDetailActivity.class);
            intent.putExtra("buildingName", labBuildingList.get(position).getBuilding());
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return labBuildingList.size();
    }

    public class LabBuildingViewHolder extends RecyclerView.ViewHolder{
        private CardView buildingCardView;
        private TextView buildingName;
        private TextView number;
        private ImageView buildingImage;

        public LabBuildingViewHolder(View itemView){
            super(itemView);
            buildingImage = (ImageView)itemView.findViewById(R.id.building_image);
            buildingCardView = (CardView)itemView.findViewById(R.id.building_card_view);
            buildingName = (TextView)itemView.findViewById(R.id.building_name);
            number = (TextView)itemView.findViewById(R.id.building_number_room);

        }
    }
}

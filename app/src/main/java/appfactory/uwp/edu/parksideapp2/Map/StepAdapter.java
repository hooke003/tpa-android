package appfactory.uwp.edu.parksideapp2.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.StepObj;
import appfactory.uwp.edu.parksideapp2.R;

/**
 * Created by kyluong09 on 6/6/18.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private ArrayList<StepObj> stepList;

    public StepAdapter(ArrayList<StepObj> stepList) {
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_view,parent,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder holder, int position) {

        holder.stepText.setText(stepList.get(position).getInstruction());
        holder.stepDistance.setText(stepList.get(position).getDistance() + "m");
        // Set icon for each steps
        if((stepList.size() - 1) == position){
            holder.stepImage.setImageResource(R.drawable.arrive_destination);
        }else if(stepList.get(position).getInstruction().contains("Head")){
            holder.stepImage.setImageResource(R.drawable.go_straight);
        }else if(stepList.get(position).getInstruction().contains("Turn left")){
            holder.stepImage.setImageResource(R.drawable.turn_left);
        }else if(stepList.get(position).getInstruction().contains("Turn right")){
            holder.stepImage.setImageResource(R.drawable.turn_right);
        }else if(stepList.get(position).getInstruction().contains("Turn slight left")) {
            holder.stepImage.setImageResource(R.drawable.turn_left);
        }else if(stepList.get(position).getInstruction().contains("Turn slight right")) {
            holder.stepImage.setImageResource(R.drawable.turn_right);
        }else if(stepList.get(position).getInstruction().contains("Turn sharp left")) {
            holder.stepImage.setImageResource(R.drawable.turn_left);
        }else if(stepList.get(position).getInstruction().contains("Turn sharp right")) {
            holder.stepImage.setImageResource(R.drawable.turn_right);
        }else if(stepList.get(position).getInstruction().contains("roundabout")) {
            holder.stepImage.setImageResource(R.drawable.roundabout);
        }else if(stepList.get(position).getInstruction().contains("Continue")) {
            holder.stepImage.setImageResource(R.drawable.go_straight);}
        else {
            holder.stepImage.setImageResource(R.drawable.go_straight);}

    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView stepImage;
        TextView  stepText;
        TextView stepDistance;
        public ViewHolder(View itemView) {
            super(itemView);

            stepImage = itemView.findViewById(R.id.step_image);
            stepText = itemView.findViewById(R.id.step_text);
            stepDistance = itemView.findViewById(R.id.step_distance);
        }
    }
}

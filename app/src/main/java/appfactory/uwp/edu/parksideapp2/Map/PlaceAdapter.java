package appfactory.uwp.edu.parksideapp2.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.PlaceObj;
import appfactory.uwp.edu.parksideapp2.R;

/**
 * Created by kyluong09 on 5/28/18.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private ArrayList<PlaceObj> placeList;
    private ArrayList<org.osmdroid.views.overlay.Marker> markerList;
    private MapView mapView;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    public PlaceAdapter(ArrayList<PlaceObj> placeList, ArrayList<org.osmdroid.views.overlay.Marker> markerList, MapView mapView, SlidingUpPanelLayout slidingUpPanelLayout) {
        this.placeList = placeList;
        this.markerList = markerList;
        this.mapView = mapView;
        this.slidingUpPanelLayout = slidingUpPanelLayout;
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, final int position) {
        holder.locationImage.setImageResource(placeList.get(position).getImageId());
        holder.name.setText(placeList.get(position).getName());
        holder.address.setText(placeList.get(position).getAddress());
        holder.viewLayout.setOnClickListener(v -> {
            GeoPoint point = new GeoPoint(placeList.get(position).getLongitude(),placeList.get(position).getLatude());
            mapView.getController().setZoom(17f);
            mapView.getController().setCenter(point);

            for(Overlay item:mapView.getOverlays()){
                if(item instanceof org.osmdroid.views.overlay.Marker){
                    if(markerList.get(position).equals(item)){
                        ((org.osmdroid.views.overlay.Marker) item).showInfoWindow();
                    }
                }
            }
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        ImageView locationImage;
        RelativeLayout viewLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.place_name);
            address = itemView.findViewById(R.id.place_address);
            locationImage = itemView.findViewById(R.id.place_image);
            viewLayout = itemView.findViewById(R.id.place_view_layout);
        }
    }
}

package appfactory.uwp.edu.parksideapp2.News;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Detail.NewsDetailActivity;
import appfactory.uwp.edu.parksideapp2.Models.NewsObj;
import appfactory.uwp.edu.parksideapp2.R;

/**
 * Created by kyluong09 on 4/13/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    // Context
    private Context context;
    //News List
    private ArrayList<NewsObj> newsList;
    // ArrayList to store url
    private ArrayList<String> urlList = new ArrayList<>();

    public NewsAdapter(ArrayList<NewsObj> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final  int position) {
        holder.newsTitle.setText(Html.fromHtml(newsList.get(position).getTitle()));
        holder.newsDescription.setText(Html.fromHtml(newsList.get(position).getSummary()));
//TODO this loop suppose to populate a view of news detail but keep crashing because of null pointer

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context,NewsDetailActivity.class);
            intent.putExtra("news_title",newsList.get(position).getTitle());
            intent.putExtra("pub_date", newsList.get(position).getPubDate());
            intent.putExtra("news_article",newsList.get(position).getArticleText());
            intent.putExtra("audio_file",newsList.get(position).getAudioFile());
            intent.putExtra("banner_image",newsList.get(position).getBannerImage());
            for(NewsObj obj : newsList){
                urlList.add(obj.getUrl());
            }
            intent.putExtra("url",urlList);
            intent.putExtra("position",position);
            context.startActivity(intent);
        });

        // Convert string to date object and change it to a right format
        try {
            @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd").parse(newsList.get(position).getPubDate());
            String dateString = new SimpleDateFormat("MM-dd-yyyy").format(date);
            holder.newsDate.setText(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // UI
        private CardView cardView;
        private TextView newsTitle;
        private TextView newsDescription;
        private ImageView newsImage;
        private TextView newsDate;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.news_card_view);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            newsDate = itemView.findViewById(R.id.news_date);
        }
    }
}

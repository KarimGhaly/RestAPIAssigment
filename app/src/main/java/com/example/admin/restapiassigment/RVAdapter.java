package com.example.admin.restapiassigment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.restapiassigment.model.repos.ReposClass;

import java.util.List;

/**
 * Created by Admin on 9/17/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    Context context;
    List<ReposClass> reposClassList;

    public RVAdapter(List<ReposClass> reposClassList) {
        this.reposClassList = reposClassList;
    }

    public void FilterList(List<ReposClass> newReposClassList)
    {
        reposClassList=newReposClassList;
        notifyDataSetChanged();
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVAdapter.ViewHolder holder, int position) {

        ReposClass repo = reposClassList.get(position);
        holder.txtRepoName.setText(repo.getName());
        if(repo.getDescription() != null)
            holder.txtRepoDesc.setText(repo.getDescription().toString());
        holder.txtRepoLang.setText("Language: "+repo.getLanguage());
        holder.txtRepoCreate.setText("Created At: "+repo.getCreatedAt());
        holder.txtRepoUpdated.setText("Last Update: "+repo.getUpdatedAt());
    }

    @Override
    public int getItemCount() {
        return reposClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRepoName;
        TextView txtRepoDesc;
        TextView txtRepoCreate;
        TextView txtRepoUpdated;
        TextView txtRepoLang;
        public ViewHolder(View itemView) {
            super(itemView);
            txtRepoName = (TextView) itemView.findViewById(R.id.txtRepoName);
            txtRepoDesc = (TextView) itemView.findViewById(R.id.txtRepoDesc);
            txtRepoLang = (TextView) itemView.findViewById(R.id.txtRepoLang);
            txtRepoCreate = (TextView) itemView.findViewById(R.id.txtRepoCreated);
            txtRepoUpdated = (TextView) itemView.findViewById(R.id.txtRepoUpdated);
        }

    }

}

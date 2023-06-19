package com.firmantour.travelingsolution;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyViewHolder> {

    private List<ModelUser> userList;
    private Activity activity;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public AdapterUser(List<ModelUser>userList, Activity activity){
        this.userList = userList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.item_user,parent,false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelUser data = userList.get(position);
        holder.TextNama.setText(data.getNama());
        holder.TextNomor.setText(data.getNomor());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TextNama, TextNomor;
        CardView CardHasil;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TextNama = itemView.findViewById(R.id.textViewNama);
            TextNomor = itemView.findViewById(R.id.textViewNomor);
            CardHasil = itemView.findViewById(R.id.cardUser);
        }
    }
}

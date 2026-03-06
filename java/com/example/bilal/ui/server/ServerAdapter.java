package com.example.bilal.ui.server;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bilal.data.model.Server;
import com.example.bilal.databinding.ItemServerBinding;
import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ServerViewHolder> {

    private final List<Server> serverList;
    private final OnServerClickListener listener;

    public interface OnServerClickListener {
        void onServerClick(Server server);
    }

    public ServerAdapter(List<Server> serverList, OnServerClickListener listener) {
        this.serverList = serverList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServerBinding binding = ItemServerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ServerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServerViewHolder holder, int position) {
        holder.bind(serverList.get(position));
    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }

    class ServerViewHolder extends RecyclerView.ViewHolder {
        private final ItemServerBinding binding;

        public ServerViewHolder(ItemServerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Server server) {
            binding.tvCountry.setText(server.getCountry());
            binding.tvIp.setText(server.getIp());
            Glide.with(binding.ivFlag.getContext())
                    .load(server.getFlagUrl())
                    .into(binding.ivFlag);
            
            itemView.setOnClickListener(v -> listener.onServerClick(server));
        }
    }
}
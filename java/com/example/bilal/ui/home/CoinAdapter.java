package com.example.bilal.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bilal.data.remote.model.CoinResponse;
import com.example.bilal.databinding.ItemCoinHotBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {

    private List<CoinResponse> coins = new ArrayList<>();

    public void setCoins(List<CoinResponse> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCoinHotBinding binding = ItemCoinHotBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CoinViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        holder.bind(coins.get(position));
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    static class CoinViewHolder extends RecyclerView.ViewHolder {
        private final ItemCoinHotBinding binding;

        public CoinViewHolder(ItemCoinHotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CoinResponse coin) {
            binding.tvCoinSymbol.setText(coin.symbol.toUpperCase());
            binding.tvCoinName.setText(coin.name);
            binding.tvCoinPrice.setText(String.format(Locale.US, "$%,.2f", coin.currentPrice));
            
            double change = coin.priceChangePercentage24h;
            binding.tvCoinChange.setText(String.format(Locale.US, "%.2f%%", change));
            
            if (change >= 0) {
                binding.tvCoinChange.setTextColor(Color.parseColor("#0ECB81")); // binanceGreen
                binding.tvCoinChange.setText("+" + binding.tvCoinChange.getText());
            } else {
                binding.tvCoinChange.setTextColor(Color.parseColor("#F6465D")); // binanceRed
            }

            Glide.with(binding.ivCoinLogo.getContext())
                    .load(coin.image)
                    .into(binding.ivCoinLogo);
        }
    }
}
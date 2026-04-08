package com.example.baitapfirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapfirebase.R;
import com.example.baitapfirebase.models.Ticket;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private Context context;
    private List<Ticket> ticketList;

    public TicketAdapter(Context context, List<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.tvMovieTitle.setText(ticket.getMovieTitle());
        holder.tvTheater.setText("Rạp: " + ticket.getTheaterName());
        holder.tvShowtime.setText("Ngày: " + ticket.getShowDate() + " | Giờ: " + ticket.getShowTime());
        holder.tvQuantity.setText("Số vé: " + ticket.getQuantity());
        holder.tvTotalPrice.setText(String.format("Tổng: %.0f VNĐ", ticket.getTotalPrice()));
        holder.tvBookedAt.setText("Đặt lúc: " + ticket.getBookedAt());
    }

    @Override
    public int getItemCount() {
        return ticketList == null ? 0 : ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tvTheater, tvShowtime, tvQuantity, tvTotalPrice, tvBookedAt;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvTheater = itemView.findViewById(R.id.tvTheater);
            tvShowtime = itemView.findViewById(R.id.tvShowtime);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvBookedAt = itemView.findViewById(R.id.tvBookedAt);
        }
    }
}

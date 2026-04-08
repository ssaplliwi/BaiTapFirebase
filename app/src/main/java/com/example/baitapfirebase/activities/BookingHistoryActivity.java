package com.example.baitapfirebase.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapfirebase.R;
import com.example.baitapfirebase.adapters.TicketAdapter;
import com.example.baitapfirebase.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {

    private RecyclerView rvTickets;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvTickets = findViewById(R.id.rvTickets);
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter = new TicketAdapter(this, ticketList);
        rvTickets.setAdapter(ticketAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lịch sử đặt vé");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadTickets();
    }

    private void loadTickets() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("tickets")
                .whereEqualTo("userId", userId)
                .orderBy("bookedAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ticketList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Ticket ticket = new Ticket();
                            ticket.setId(doc.getId());
                            ticket.setUserId(doc.getString("userId"));
                            ticket.setUserEmail(doc.getString("userEmail"));
                            ticket.setMovieId(doc.getString("movieId"));
                            ticket.setMovieTitle(doc.getString("movieTitle"));
                            ticket.setTheaterId(doc.getString("theaterId"));
                            ticket.setTheaterName(doc.getString("theaterName"));
                            ticket.setShowtimeId(doc.getString("showtimeId"));
                            ticket.setShowDate(doc.getString("showDate"));
                            ticket.setShowTime(doc.getString("showTime"));
                            ticket.setQuantity(doc.getLong("quantity") != null ?
                                    doc.getLong("quantity").intValue() : 1);
                            ticket.setTotalPrice(doc.getDouble("totalPrice") != null ?
                                    doc.getDouble("totalPrice") : 0);
                            ticket.setBookedAt(doc.getString("bookedAt"));
                            ticketList.add(ticket);
                        }
                        ticketAdapter.notifyDataSetChanged();

                        if (ticketList.isEmpty()) {
                            Toast.makeText(this, "Bạn chưa đặt vé nào", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Lỗi tải vé: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

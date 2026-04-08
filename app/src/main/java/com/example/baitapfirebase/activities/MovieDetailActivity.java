package com.example.baitapfirebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.baitapfirebase.R;
import com.example.baitapfirebase.models.Showtime;
import com.example.baitapfirebase.models.Theater;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private TextView tvTitle, tvDescription, tvGenre, tvDuration, tvRating, tvPrice;
    private Spinner spTheater, spShowtime;
    private Button btnBook;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private List<Theater> theaterList = new ArrayList<>();
    private List<Showtime> showtimeList = new ArrayList<>();

    private Theater selectedTheater;
    private Showtime selectedShowtime;
    private String movieId, movieTitle, movieImageName;
    private double moviePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Lấy thông tin từ Intent
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        moviePrice = getIntent().getDoubleExtra("moviePrice", 75000);
        movieImageName = getIntent().getStringExtra("movieImageName");

        // Ánh xạ view
        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvGenre = findViewById(R.id.tvGenre);
        tvDuration = findViewById(R.id.tvDuration);
        tvRating = findViewById(R.id.tvRating);
        tvPrice = findViewById(R.id.tvPrice);
        spTheater = findViewById(R.id.spTheater);
        spShowtime = findViewById(R.id.spShowtime);
        btnBook = findViewById(R.id.btnBook);

        tvTitle.setText(movieTitle);
        tvPrice.setText(String.format("Giá: %.0f VNĐ", moviePrice));

        // Load ảnh poster
        if (movieImageName != null && !movieImageName.isEmpty()) {
            int resId = getResources().getIdentifier(movieImageName, "drawable", getPackageName());
            if (resId != 0) {
                ivPoster.setImageResource(resId);
            }
        }

        // Load danh sách rạp từ Firebase
        loadTheaters();

        // Xử lý chọn rạp
        spTheater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position <= theaterList.size()) {
                    selectedTheater = theaterList.get(position - 1);
                    loadShowtimes(selectedTheater.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý chọn suất chiếu
        spShowtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position <= showtimeList.size()) {
                    selectedShowtime = showtimeList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý đặt vé
        btnBook.setOnClickListener(v -> bookTicket());
    }

    private void loadTheaters() {
        db.collection("theaters").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                theaterList.clear();
                for (com.google.firebase.firestore.DocumentSnapshot doc : task.getResult()) {
                    Theater theater = doc.toObject(Theater.class);
                    theater.setId(doc.getId());
                    theaterList.add(theater);
                }
                setupTheaterSpinner();

                // Nếu chưa có rạp nào, thêm dữ liệu mẫu
                if (theaterList.isEmpty()) {
                    addSampleTheaters();
                }
            }
        });
    }

    private void addSampleTheaters() {
        List<Theater> sampleTheaters = new ArrayList<>();
        sampleTheaters.add(new Theater("1", "CGV Vincom Center", "Quận 1, TP.HCM", "theater_cgv"));
        sampleTheaters.add(new Theater("2", "Lotte Cinema", "Quận 7, TP.HCM", "theater_lotte"));
        sampleTheaters.add(new Theater("3", "Galaxy Nguyễn Du", "Quận 1, TP.HCM", "theater_galaxy"));

        for (Theater t : sampleTheaters) {
            final String id = t.getId();
            Map<String, Object> map = new HashMap<>();
            map.put("name", t.getName());
            map.put("address", t.getAddress());
            map.put("imageName", t.getImageName());

            db.collection("theaters").document(id).set(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Theater newT = new Theater(id, t.getName(), t.getAddress(), t.getImageName());
                    theaterList.add(newT);
                    setupTheaterSpinner();
                }
            });
        }
    }

    private void setupTheaterSpinner() {
        List<String> theaterNames = new ArrayList<>();
        theaterNames.add("-- Chọn rạp chiếu --");
        for (Theater t : theaterList) {
            theaterNames.add(t.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, theaterNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheater.setAdapter(adapter);
    }

    private void loadShowtimes(String theaterId) {
        db.collection("showtimes")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("theaterId", theaterId)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showtimeList.clear();
                List<String> showtimeDisplayList = new ArrayList<>();
                showtimeDisplayList.add("-- Chọn suất chiếu --");

                for (com.google.firebase.firestore.DocumentSnapshot doc : task.getResult()) {
                    Showtime showtime = doc.toObject(Showtime.class);
                    showtime.setId(doc.getId());
                    showtimeList.add(showtime);
                    showtimeDisplayList.add(showtime.getDate() + " | " + showtime.getTime());
                }

                // Nếu chưa có suất chiếu, thêm mẫu
                if (showtimeList.isEmpty()) {
                    addSampleShowtimes(theaterId, showtimeDisplayList);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, showtimeDisplayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spShowtime.setAdapter(adapter);
            }
        });
    }

    private void addSampleShowtimes(String theaterId, List<String> showtimeDisplayList) {
        String[] times = {"09:00", "11:30", "14:00", "16:30", "19:00", "21:30"};
        String[] dates = {"2026-04-09", "2026-04-10", "2026-04-11"};

        for (int i = 0; i < dates.length; i++) {
            for (int j = 0; j < times.length; j++) {
                String showId = movieId + "_" + theaterId + "_" + i + "_" + j;
                Map<String, Object> map = new HashMap<>();
                map.put("movieId", movieId);
                map.put("theaterId", theaterId);
                map.put("date", dates[i]);
                map.put("time", times[j]);
                map.put("availableSeats", 50);

                final int idx = showtimeList.size();
                db.collection("showtimes").document(showId).set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Showtime s = new Showtime(showId, movieId, theaterId,
                                times[idx % times.length], dates[idx / times.length], 50);
                        showtimeList.add(s);
                        showtimeDisplayList.add(s.getDate() + " | " + s.getTime());
                    }
                });
            }
        }
    }

    private void bookTicket() {
        if (selectedTheater == null) {
            Toast.makeText(this, "Vui lòng chọn rạp chiếu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedShowtime == null) {
            Toast.makeText(this, "Vui lòng chọn suất chiếu", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        String userEmail = mAuth.getCurrentUser().getEmail();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String bookedAt = sdf.format(new Date());

        Map<String, Object> ticket = new HashMap<>();
        ticket.put("userId", userId);
        ticket.put("userEmail", userEmail);
        ticket.put("movieId", movieId);
        ticket.put("movieTitle", movieTitle);
        ticket.put("theaterId", selectedTheater.getId());
        ticket.put("theaterName", selectedTheater.getName());
        ticket.put("showtimeId", selectedShowtime.getId());
        ticket.put("showDate", selectedShowtime.getDate());
        ticket.put("showTime", selectedShowtime.getTime());
        ticket.put("quantity", 1);
        ticket.put("totalPrice", moviePrice);
        ticket.put("bookedAt", bookedAt);

        db.collection("tickets").add(ticket)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MovieDetailActivity.this,
                                "Đặt vé thành công!\n" +
                                "Phim: " + movieTitle + "\n" +
                                "Rạp: " + selectedTheater.getName() + "\n" +
                                "Giờ chiếu: " + selectedShowtime.getTime(),
                                Toast.LENGTH_LONG).show();

                        // Chuyển sang màn hình lịch sử đặt vé
                        startActivity(new Intent(MovieDetailActivity.this,
                                BookingHistoryActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MovieDetailActivity.this,
                                "Lỗi đặt vé: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}

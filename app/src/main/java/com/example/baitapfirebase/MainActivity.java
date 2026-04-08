package com.example.baitapfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapfirebase.activities.BookingHistoryActivity;
import com.example.baitapfirebase.activities.LoginActivity;
import com.example.baitapfirebase.activities.MovieDetailActivity;
import com.example.baitapfirebase.activities.RegisterActivity;
import com.example.baitapfirebase.adapters.MovieAdapter;
import com.example.baitapfirebase.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // KIỂM TRA ĐĂNG NHẬP
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(this, movieList);
        rvMovies.setAdapter(movieAdapter);

        // Load danh sách phim từ Firebase
        loadMovies();

        // Đăng ký FCM token
        registerFCMToken();
    }

    private void loadMovies() {
        db.collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        movieList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Movie movie = doc.toObject(Movie.class);
                            movie.setId(doc.getId());
                            movieList.add(movie);
                        }
                        movieAdapter.notifyDataSetChanged();

                        // Nếu Firebase chưa có phim nào, thêm dữ liệu mẫu
                        if (movieList.isEmpty()) {
                            addSampleMovies();
                        }
                    } else {
                        Toast.makeText(this, "Lỗi tải phim: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSampleMovies() {
        List<Movie> sampleMovies = new ArrayList<>();

        sampleMovies.add(new Movie("1", "Avatar: The Way of Water",
                "Phần tiếp theo của Avatar kể về cuộc sống mới của Jake Sully và Neytiri trên Pandora.",
                "", 8.5, "Khoa học viễn tưởng", "192 phút", 80000, "movie_avatar"));
        sampleMovies.add(new Movie("2", "Oppenheimer",
                "Câu chuyện về J. Robert Oppenheimer và vai trò của ông trong việc phát triển bom nguyên tử.",
                "", 9.0, "Tiểu sử, Drama", "180 phút", 75000, "movie_oppenheimer"));
        sampleMovies.add(new Movie("3", "Barbie",
                "Barbie và Ken đang tận hưởng cuộc sống tuyệt vời trong thế giới đầy màu sắc của Barbie Land.",
                "", 7.8, "Hài, Phiêu lưu", "114 phút", 70000, "movie_barbie"));
        sampleMovies.add(new Movie("4", "Spider-Man: Across the Spider-Verse",
                "Miles Morales trở lại để phiêu lưu cùng Spider-Man nhóm của Gwen Stacy.",
                "", 9.2, "Hoạt hình, Hành động", "140 phút", 75000, "movie_spiderman"));
        sampleMovies.add(new Movie("5", "Dune: Part Two",
                "Paul Atreides kết hợp với Fremen để trả thù những kẻ đã phá hủy gia đình mình.",
                "", 9.1, "Khoa học viễn tưởng", "166 phút", 85000, "movie_dune"));

        for (Movie m : sampleMovies) {
            db.collection("movies").add(m.toMap());
        }

        movieList.addAll(sampleMovies);
        movieAdapter.notifyDataSetChanged();
    }

    private void registerFCMToken() {
        // Token sẽ được gửi lên Firebase trong MyFirebaseMessagingService
        // Phần này chỉ là placeholder
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_history) {
            startActivity(new Intent(this, BookingHistoryActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movieId", movie.getId());
        intent.putExtra("movieTitle", movie.getTitle());
        intent.putExtra("moviePrice", movie.getPrice());
        intent.putExtra("movieImageName", movie.getImageName());
        startActivity(intent);
    }
}

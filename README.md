# Movie Ticket App

## Mô tả
Ứng dụng đặt vé xem phim sử dụng Firebase (Android Java).

## Các chức năng đã triển khai

### 1. Authentication (Đăng nhập / Đăng ký)
- Đăng ký với Email/Password
- Đăng nhập với Email/Password
- Quản lý phiên đăng nhập qua FirebaseAuth

### 2. Xem danh sách phim
- Hiển thị danh sách phim đang chiếu từ Firebase Firestore
- RecyclerView với MovieAdapter
- Hỗ trợ ảnh từ drawable resources hoặc URL

### 3. Đặt vé
- Chọn rạp chiếu từ danh sách (Spinner)
- Chọn suất chiếu (ngày + giờ)
- Lưu thông tin vé lên Firebase Firestore

### 4. Lịch sử đặt vé
- Xem danh sách vé đã đặt
- Thông tin: phim, rạp, giờ chiếu, giá, thời gian đặt

### 5. Push Notification (FCM)
- Nhận thông báo nhắc giờ chiếu phim
- MyFirebaseMessagingService xử lý notification

## Cấu trúc project

```
app/
├── src/main/
│   ├── java/com/example/baitapfirebase/
│   │   ├── activities/
│   │   │   ├── LoginActivity.java       # Đăng nhập
│   │   │   ├── RegisterActivity.java   # Đăng ký
│   │   │   ├── MovieDetailActivity.java # Chi tiết phim & đặt vé
│   │   │   └── BookingHistoryActivity.java # Lịch sử đặt vé
│   │   ├── adapters/
│   │   │   ├── MovieAdapter.java       # Adapter danh sách phim
│   │   │   └── TicketAdapter.java      # Adapter danh sách vé
│   │   ├── models/
│   │   │   ├── Movie.java              # Model phim
│   │   │   ├── Theater.java            # Model rạp chiếu
│   │   │   ├── Showtime.java           # Model suất chiếu
│   │   │   └── Ticket.java             # Model vé đặt
│   │   ├── services/
│   │   │   └── MyFirebaseMessagingService.java # Push notification
│   │   └── MainActivity.java           # Màn hình chính
│   └── res/
│       ├── layout/                     # Các file layout XML
│       ├── menu/                      # Menu options
│       └── values/                    # Colors, strings, themes
```

## Firebase Collections
- `users` - Thông tin người dùng
- `movies` - Danh sách phim
- `theaters` - Danh sách rạp chiếu
- `showtimes` - Suất chiếu
- `tickets` - Vé đã đặt
- `fcm_tokens` - FCM tokens cho push notification

## Cách chạy
1. Mở project trong Android Studio
2. Thêm file `google-services.json` vào thư mục `app/`
   (Tải từ Firebase Console > Project Settings)
3. Build và chạy ứng dụng

## Yêu cầu
- Android Studio (Java/Kotlin)
- Firebase Project (Authentication, Firestore, Cloud Messaging)
- Android SDK 29+
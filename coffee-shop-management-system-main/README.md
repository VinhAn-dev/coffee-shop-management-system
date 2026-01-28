# ☕ Coffee Hub - Hệ thống Quản lý Quán Cà Phê

## 📝 Giới thiệu
Coffee Hub là một ứng dụng web Full-stack giúp quản lý quy trình bán hàng tại quầy (POS) và thống kê doanh thu cho quán cà phê. Hệ thống hỗ trợ quy trình khép kín: Gọi món -> In hóa đơn -> Thanh toán -> Thống kê doanh thu.

## 🚀 Tính năng chính
- **Bán hàng (POS):** Giao diện chọn món nhanh, giỏ hàng, tính tổng tiền tự động.
- **Thanh toán:** Xử lý đơn hàng, in hóa đơn (Bill) trực tiếp từ trình duyệt.
- **Quản lý đơn hàng:** Xem lại lịch sử đơn hàng, trạng thái (Pending/Paid).
- **Dashboard Admin:** Thống kê tổng doanh thu, số lượng đơn bán ra theo thời gian thực.
- **Phân quyền:** Cơ chế đăng nhập/đăng xuất cho Nhân viên và Quản lý.

## 🛠 Công nghệ sử dụng
- **Backend:** Java 21, Spring Boot 3.x, Hibernate/JPA.
- **Database:** H2 Database (In-memory) - Dễ dàng demo không cần cài đặt SQL Server/MySQL.
- **Frontend:** HTML5, CSS3, JavaScript (Fetch API) - Không dùng Framework JS nặng.
- **Tools:** Maven, VS Code/IntelliJ IDEA.

## ⚙️ Cài đặt & Chạy
1. Clone repo này về máy.
2. Mở project bằng VS Code hoặc IntelliJ.
3. Chạy file `QuanlysanphamApplication.java`.
4. Truy cập:
   - Trang bán hàng: `http://localhost:8080`
   - Tài khoản demo: `admin` / `123`
   - Tài khoảng staff: `staff` / `123`
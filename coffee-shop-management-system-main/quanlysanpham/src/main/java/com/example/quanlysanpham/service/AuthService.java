package com.example.quanlysanpham.service;

import com.example.quanlysanpham.dto.LoginResponse;
import com.example.quanlysanpham.entity.Staff;
import com.example.quanlysanpham.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AuthService {

    @Autowired
    private StaffRepository staffRepository;

    // --- 1. HÀM ĐĂNG NHẬP (GIỮ NGUYÊN) ---
    public LoginResponse loginStaff(String username, String password) {
        
        // Cửa sau cho Admin
        if ("admin".equals(username) && "123".equals(password)) {
            return new LoginResponse("0", "ADMIN", "Quản Trị Viên");
        }

        Staff staff = staffRepository.findByUsername(username);

        if (staff == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại!");
        }

        if (!staff.getPassword().trim().equals(password.trim())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai mật khẩu!");
        }

        return new LoginResponse(
            String.valueOf(staff.getId()), 
            "STAFF", 
            staff.getFullName() 
        );
    }

    // --- 2. HÀM TẠO NHÂN VIÊN MỚI (THÊM MỚI VÀO ĐÂY) ---
    // Hàm này sẽ được Controller gọi khi Admin bấm nút "Lưu"
    public Staff createStaff(String username, String password, String fullName) {
        // Kiểm tra xem tên đăng nhập đã có người dùng chưa
        if (staffRepository.findByUsername(username) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập này đã tồn tại!");
        }

        // Tạo nhân viên mới
        Staff newStaff = new Staff();
        newStaff.setUsername(username);
        newStaff.setPassword(password); // Lưu password (đang để lộ thiên, sau này nên mã hóa)
        newStaff.setFullName(fullName);
        newStaff.setRole("STAFF");      // Mặc định quyền là Nhân viên

        // Lưu vào Database
        return staffRepository.save(newStaff);
    }
    
    public void logout(String token) {}
}
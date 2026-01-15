package com.example.quanlysanpham;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class QuanlysanphamApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuanlysanphamApplication.class, args);
    }

    // Sự kiện chạy ngay sau khi Server khởi động xong
    @EventListener(ApplicationReadyEvent.class)
    public void printLinksAndOpenBrowser() {
        String homeUrl = "http://localhost:8080/";
        String adminUrl = "http://localhost:8080/admin/staff";
        String dbUrl = "http://localhost:8080/h2-console";

        // 1. In danh sách link ra Terminal để bấm cho nhanh
        System.out.println("\n----------------------------------------------------------");
        System.out.println("🚀  DỰ ÁN ĐÃ KHỞI ĐỘNG THÀNH CÔNG!  🚀");
        System.out.println("----------------------------------------------------------");
        System.out.println("🏠  Trang chủ:      " + homeUrl);
        System.out.println("👨‍💼  Quản lý Staff:  " + adminUrl);
        System.out.println("🗄️   Database (H2):  " + dbUrl);
        System.out.println("----------------------------------------------------------\n");

        // 2. Tự động mở trình duyệt vào trang chủ (như cũ)
        System.out.println(">>> Đang tự động mở trình duyệt...");
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(homeUrl));
            } catch (IOException | URISyntaxException e) {
                System.err.println(">>> Không thể mở trình duyệt: " + e.getMessage());
            }
        } else {
            System.out.println(">>> Máy không hỗ trợ tự mở, vui lòng bấm vào link trên.");
        }
    }
}
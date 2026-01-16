package com.example.quanlysanpham; // Giữ nguyên package của mày

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

    // --- THÊM ĐOẠN NÀY ĐỂ TỰ MỞ TRÌNH DUYỆT ---
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        System.out.println(">>> Đang cố gắng mở trình duyệt...");
        String url = "http://localhost:8080/"; // Cổng dang dung

        // Kiểm tra xem máy có hỗ trợ Desktop không (Windows/Mac thường có)
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
                System.out.println(">>> Đã mở trình duyệt thành công!");
            } catch (IOException | URISyntaxException e) {
                System.err.println(">>> Không thể mở trình duyệt: " + e.getMessage());
            }
        } else {
            System.out.println(">>> Máy này không hỗ trợ tự mở trình duyệt. Vui lòng mở thủ công: " + url);
        }
    }
}
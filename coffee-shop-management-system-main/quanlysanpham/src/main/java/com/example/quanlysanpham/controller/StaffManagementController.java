package com.example.quanlysanpham.controller; // <--- Chuẩn package

import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/staff")
public class StaffManagementController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listStaff(Model model) {
        List<User> list = userRepository.findByRole("STAFF");
        if (list == null) list = new ArrayList<>();
        model.addAttribute("staffList", list);
        return "staff-management"; 
    }

    @PostMapping("/add")
    public String addStaff(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String fullName,
                           Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Username đã tồn tại!");
            model.addAttribute("staffList", userRepository.findByRole("STAFF"));
            return "staff-management";
        }
        User newStaff = new User();
        newStaff.setUsername(username);
        newStaff.setPassword(password);
        newStaff.setFullName(fullName);
        newStaff.setRole("STAFF");
        userRepository.save(newStaff);
        return "redirect:/admin/staff";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Long id) {
        if(userRepository.existsById(id)) userRepository.deleteById(id);
        return "redirect:/admin/staff";
    }
}


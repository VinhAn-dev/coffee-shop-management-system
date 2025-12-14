package com.example.quanlysanpham;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index", "/index.html"})
    public String index() { return "index"; }

    @GetMapping({"/shop", "/shop.html"})
    public String shop() { return "shop"; }

    @GetMapping({"/services", "/services.html"})
    public String services() { return "services"; }

    @GetMapping({"/menu", "/menu.html"})
    public String menu() { return "menu"; }

    @GetMapping({"/about", "/about.html"})
    public String about() { return "about"; }

    @GetMapping({"/blog", "/blog.html"})
    public String blog() { return "blog"; }

    @GetMapping({"/blog-single", "/blog-single.html"})
    public String blogSingle() { return "blog-single"; }

    @GetMapping({"/contact", "/contact.html"})
    public String contact() { return "contact"; }

    @GetMapping({"/cart", "/cart.html"})
    public String cart() { return "cart"; }

    @GetMapping({"/checkout", "/checkout.html"})
    public String checkout() { return "checkout"; }

    @GetMapping({"/product-single", "/product-single.html"})
    public String productSingle() { return "product-single"; }
}

package com.example.crudapp.controller;

import com.example.crudapp.model.Product;
import com.example.crudapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ResourceLoader resourceLoader;

    private final String uploadDir = "uploads/";

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @GetMapping("/showNewProductForm")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "new_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadDir + file.getOriginalFilename());
                Files.write(path, bytes);
                product.setFileName(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productService.saveProduct(product);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
        } else {
            // Handle case where product is not found
            return "redirect:/";
        }
        return "update_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute("product") Product product,
                                @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadDir + file.getOriginalFilename());
                Files.write(path, bytes);
                product.setFileName(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productService.saveProduct(product);
        return "redirect:/";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/";
    }

    @GetMapping("/file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = resourceLoader.getResource("file:" + uploadDir + filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/pyq")
    public String viewPyqPage() {
        return "pyq"; // This will look for pyq.html in the templates folder
    }

    @GetMapping("/roadmap")
    public String viewRoadmapPage() {
        return "roadmap"; // This will look for roadmap.html in the templates folder
    }

    @GetMapping("/revision")
    public String viewRevisionPage() {
        return "revision"; // This will look for revision.html in the templates folder
    }

    @GetMapping("/notes")
    public String viewNotesPage() {
        return "notes"; // This will look for notes.html in the templates folder
    }

    @GetMapping("/cheat")
    public String viewCheatPage() {
        return "cheat"; // This will look for cheat.html in the templates folder
    }

    @GetMapping("/syllabus")
    public String viewSyllabusPage() {
        return "syllabus"; // This will look for syllabus.html in the templates folder
    }

    @GetMapping("/profile")
    public String viewProfilePage() {
        return "profile"; // This will look for profile.html in the templates folder
    }

    @GetMapping("/check")
    public String viewCheckPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "check"; // This will look for check.html in the templates folder
    }

}

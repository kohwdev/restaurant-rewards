package org.woonkoh.capstonedraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.woonkoh.capstonedraft.model.Order;
import org.woonkoh.capstonedraft.model.Product;
import org.woonkoh.capstonedraft.model.ProductDto;
import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.repository.ProductsRepository;
import org.woonkoh.capstonedraft.service.OrderService;
import org.woonkoh.capstonedraft.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.security.Principal;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping({"","/"})
    public String showProducts(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        User user = userService.findByEmail(userEmail);

        // Check if user is null or not found
        if (user == null) {
            // Invalidate session to clear the security context
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Redirect to the login page
            return "redirect:/login";
        }

        Order activeOrder = orderService.findOrCreateActiveOrder(user.getId());
        model.addAttribute("order", activeOrder);

        List<Product> products = productsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping({"/create"})
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping({"/create"})
    public String createProduct (
        @Valid @ModelAttribute ProductDto productDto,
                BindingResult result) {

        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "The image file is mandatory"));
        }

        if (result.hasErrors()) {
            return "products/CreateProduct";
        }

        //save image file
        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Exception ex) {
                System.out.println("Exception:" + ex.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        productsRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam("id") Long id) {
        try {
            Product product = productsRepository.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());

            model.addAttribute("productDto", productDto);

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "redirect:/products";
        }
    return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam Long id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {

       try {
           Product product = productsRepository.findById(id).get();
           model.addAttribute("product", product);

           if (result.hasErrors()) {
               return "products/EditProduct";
           }

           if (!productDto.getImageFile().isEmpty()) {
               //delete old image
               String uploadDir = "public/images/";
               Path oldImagePath = Paths.get(uploadDir + productDto.getImageFile());

               try {
                   Files.deleteIfExists(oldImagePath);
               } catch (Exception ex) {
                   System.out.println("Exception: " + ex.getMessage());
               }

               //save new image file
               MultipartFile image = productDto.getImageFile();
               Date createdAt = new Date();
               String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

               try (InputStream inputStream = image.getInputStream()) {
                   Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
               }

               product.setImageFileName(storageFileName);

           }

           product.setName(productDto.getName());
           product.setDescription(productDto.getDescription());
           product.setCategory(productDto.getCategory());
           product.setPrice(productDto.getPrice());

           productsRepository.save(product);

       } catch (Exception ex) {
           System.out.println("Exception:" + ex.getMessage());
       }
       return "redirect:/products";

    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product: " + ex.getMessage());
        }
        return "redirect:/products";
    }



}

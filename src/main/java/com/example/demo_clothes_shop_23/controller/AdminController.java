package com.example.demo_clothes_shop_23.controller;

import com.example.demo_clothes_shop_23.entities.*;
import com.example.demo_clothes_shop_23.model.enums.ImageType;
import com.example.demo_clothes_shop_23.model.enums.OrdersStatus;
import com.example.demo_clothes_shop_23.model.enums.SizeType;
import com.example.demo_clothes_shop_23.model.enums.UserRole;
import com.example.demo_clothes_shop_23.service.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final BlogService blogService;
    private final TagService tagService;
    private final ProductService productService;
    private final SizeService sizeService;
    private final ColorService colorService;
    private final CategoryService categoryService;
    private final QuantityService quantityService;
    private final UserService userService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final ImageService imageService;
    private final OrderDetailService orderDetailService;
    private final CouponService couponService;
    private final DiscountService discountService;
    private final BannerService bannerService;
    private final CostService costService;

    //dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("currentMonth", LocalDateTime.now().getMonthValue());
        model.addAttribute("totalFinal", orderService.getTotalFinalTotalCreatedThisMonth());
        model.addAttribute("orders", orderService.getOrdersCreatedThisMonth());
        model.addAttribute("users", userService.getUsersCreatedThisMonth());
        model.addAttribute("costs", costService.getCostsCreatedThisMonth());
        model.addAttribute("blogs", blogService.getBlogsCreatedThisMonth());
        return "admin/dashboard/dashboard";
    }

    //BLOG
    @GetMapping("/blogs")
    public String getBlogIndexPage(Model model) {
        model.addAttribute("blogs", blogService.getAll());
        return "admin/blog/blog-index";
    }
    @GetMapping("/blogs/own-blogs")
    public String getBlogOwnBlogPage(Model model) {
        model.addAttribute("blogs", blogService.getAllByUserIdOrderByCreatedAtDesc());
        return "admin/blog/blog-yourself";
    }
    @GetMapping("/blogs/create")
    public String getBlogCreatePage(Model model) {
        model.addAttribute("tags", tagService.getAll());
        return "admin/blog/blog-create";
    }

    @GetMapping("/blogs/{id}")
    public String getBlogDetailPage(@PathVariable int id, Model model) {
        model.addAttribute("blog",blogService.getBlogById(id));
        model.addAttribute("tags", tagService.getAll());
        return "admin/blog/blog-detail";
    }

    @GetMapping("/blogs/tags")
    public String getBlogTagPage(Model model) {
        List<Tag> tags = tagService.getAll();

        Map<Integer, List<Blog>> blogsByTagName = tags.stream()
            .collect(Collectors.toMap(
                Tag::getId,
                tag -> blogService.getByTagId(tag.getId())
            ));

        model.addAttribute("tags", tags);
        model.addAttribute("blogsByTagName", blogsByTagName);

        return "admin/blog/blog-tag";
    }

    //PRODUCT
    @GetMapping("/products")
    public String getProductIndexPage(Model model) {
        model.addAttribute("products",productService.getAll());
        return "admin/product/product-index";
    }

    @GetMapping("/products/{id}")
    public String getProductDetailPage(@PathVariable int id, Model model) {
        model.addAttribute("product",productService.getById(id));

        //Sắp xếp màu
        Set<Color> colors = colorService.getAll();
        Set<Color> sortedColor = new TreeSet<>(Comparator.comparingInt(Color::getId));
        sortedColor.addAll(colors);

        List<Quantity> quantities = quantityService.getByProductId(id);

        Map<String, Integer> stockMap = quantities.stream()
            .filter(q -> q.getValue() > 0)
            .collect(Collectors.toMap(
                q -> q.getColor().getId() + "-" + q.getSize().getId(),
                Quantity::getValue
            ));
        List<Image> images = imageService.getByProductId(id);

        Map<Integer, List<Image>> imageMap = images.stream()
            .sorted(Comparator.comparing(img -> img.getType() == ImageType.MAIN ? 0 : 1))
            .collect(Collectors.groupingBy(
                img -> img.getColor().getId(),
                Collectors.toList()
            ));

        model.addAttribute("imageMap", imageMap);
        model.addAttribute("stockMap", stockMap);
        model.addAttribute("sizeTypes", SizeType.values());
        model.addAttribute("colors",sortedColor);
        model.addAttribute("categoryParents",categoryService.getCategoriesWithNullParentId());
        model.addAttribute("reviews",reviewService.findByProduct_IdOrderByCreatedAtDesc(id));
        return "admin/product/product-detail";
    }

    @GetMapping("/products/create")
    public String getProductCreatePage(Model model) {
        //Sắp xếp màu
        Set<Color> colors = colorService.getAll();
        Set<Color> sortedColor = new TreeSet<>(Comparator.comparingInt(Color::getId));
        sortedColor.addAll(colors);

        model.addAttribute("sizeTypes", SizeType.values());
        model.addAttribute("colors",sortedColor);
        model.addAttribute("categoryParents",categoryService.getCategoriesWithNullParentId());

        return "admin/product/product-create";
    }

    //USER
    @GetMapping("/users")
    public String getUserIndexPage(Model model) {
        model.addAttribute("users",userService.getAll());
        return "admin/user/user-index";
    }

    @GetMapping("/users/{id}")
    public String getUserDetailPage(@PathVariable int id, Model model) {
        model.addAttribute("user",userService.getById(id));
        model.addAttribute("ordersByUserId", orderService.getByUser_IdOrderByCreatedAtDesc(id));
        return "admin/user/user-detail";
    }

    @GetMapping("/users/create")
    public String getUserCreatePage(Model model) {
        return "admin/user/user-create";
    }

    //ORDER
    @GetMapping("/orders")
    public String getOrderIndexPage(Model model) {
        model.addAttribute("orders",orderService.getAll());
        return "admin/order/order-index";
    }

    @GetMapping("/orders/{codeOrder}")
    public String getOrderDetailPage(@PathVariable String codeOrder, Model model) {
        Orders order = orderService.getByCodeOrder(codeOrder);
        model.addAttribute("order",order);
        model.addAttribute("orderDetails",orderDetailService.getByOrderId(order.getId()));
        model.addAttribute("orderStatus", OrdersStatus.values());
        return "admin/order/order-detail";
    }

    @GetMapping("/orders/create")
    public String getOrderCreatePage(Model model) {
        model.addAttribute("users",userService.getAll());
        model.addAttribute("coupons",couponService.getByActiveTrue());
        model.addAttribute("products",productService.getAllByStatus(true));
        return "admin/order/order-create";
    }

    //DISCOUNT & COUPON
    @GetMapping("/discounts")
    public String getDiscountIndexPage(Model model) {
        List<Discount> discounts = discountService.getAll();
        Map<Integer, List<Product>> productsByDiscountId = discounts.stream()
            .collect(Collectors.toMap(
                Discount::getId,
                discount -> {
                    List<Product> products = productService.getByDiscount_Id(discount.getId());
                    return products != null ? products : Collections.emptyList();
                }
            ));

        model.addAttribute("discounts",discounts);
        model.addAttribute("productsByDiscountId", productsByDiscountId);
        return "admin/discount/discount-index";
    }

    @GetMapping("/discounts/{id}")
    public String getDiscountDetailPage(@PathVariable Integer id, Model model) {
        Discount discount = discountService.getDiscountById(id);
        model.addAttribute("discount",discount);
        model.addAttribute("productsByDiscount",productService.getByDiscount_Id(discount.getId()));
        model.addAttribute("products",productService.getAll());
       List<Category> categories = categoryService.getCategoriesWithParentId();
        Map<Integer, List<Product>> productsByCategory = categories.stream()
            .collect(Collectors.toMap(
                Category::getId,
                category -> {
                    List<Product> products = productService.getByCategoryId(category.getId());
                    return products != null ? products : Collections.emptyList();
                }
            ));
        model.addAttribute("categories", categories);
        model.addAttribute("productsByCategory", productsByCategory);
        return "admin/discount/discount-detail";
    }

    @GetMapping("/discounts/create")
    public String getDiscountCreatePage(Model model) {
        return "admin/discount/discount-create";
    }

    @GetMapping("/coupons")
    public String getCouponIndexPage(Model model) {
        model.addAttribute("coupons",couponService.getAll());

        return "admin/discount/coupon-index";
    }

    //BANNER
    @GetMapping("/banners")
    public String getBannerIndexPage(Model model) {
        model.addAttribute("banners",bannerService.getAll());
        List<Integer> selectedBannerId = bannerService.getBannerByStatus(true).stream()
                .map(Banner::getId)
                    .toList();
        model.addAttribute("selectedBannerId", selectedBannerId);
        return "admin/banner/banner-index";
    }

    @GetMapping("/banners/{id}")
    public String getBannerDetailPage(@PathVariable int id, Model model) {
        model.addAttribute("banner",bannerService.getById(id));
        return "admin/banner/banner-detail";
    }

    @GetMapping("/banners/create")
    public String getBannerCreatePage(Model model) {
        return "admin/banner/banner-create";
    }

    //COST
    @GetMapping("/costs")
    public String getCostIndexPage(Model model) {
        model.addAttribute("costs",costService.getAll());
        return "admin/cost/cost-index";
    }

    @GetMapping("/costs/{id}")
    public String getCostDetailPage(@PathVariable int id, Model model) {
        model.addAttribute("cost",costService.getById(id));
        model.addAttribute("users",userService.getByRole(UserRole.ADMIN));
        return "admin/cost/cost-detail";
    }

    @GetMapping("/costs/create")
    public String getCostCreatePage(Model model) {
        model.addAttribute("users",userService.getByRole(UserRole.ADMIN));
        return "admin/cost/cost-create";
    }
}

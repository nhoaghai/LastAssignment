package com.example.demo_clothes_shop_23.controller;

import com.example.demo_clothes_shop_23.entities.*;
import com.example.demo_clothes_shop_23.model.enums.SizeType;
import com.example.demo_clothes_shop_23.model.model.ImageProductDetailModel;
import com.example.demo_clothes_shop_23.model.response.VerifyResponse;
import com.example.demo_clothes_shop_23.service.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class WebController {
    private final ProductService productService;
    private final ReviewService reviewService;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final BlogService blogService;
    private final CommentService commentService;
    private final TagService tagService;
    private final DiscountService discountService;
    private final BannerService bannerService;
    private final FavoriteService favoriteService;
    private final CartService cartService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final QuantityService quantityService;
    private final AuthService authService;

    /*Trang chủ*/
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("allDiscounts", discountService.getDiscountByActive(true));
        model.addAttribute("allBanners", bannerService.getBannerByStatus(true));
        model.addAttribute("hotSales", productService.getAllByPriceDifferenceAsc(true));
        model.addAttribute("newArrivals", productService.getByStatusOrderByCreatedAtDesc(true));
        model.addAttribute("clothesBanner", productService.getOneProductByCategoryId(5).getPoster());
        model.addAttribute("shoesBanner", productService.getOneProductByCategoryId(12).getPoster());
        model.addAttribute("pantsBanner", productService.getOneProductByCategoryId(10).getPoster());
        model.addAttribute("latestBlog", blogService.getByTagIdAndStatusOrderByCreatedAtDesc(2,true));
        List<Integer> favoriteProductIds = favoriteService.getByUser_IdOrderByCreatedAtDesc().stream().map(f->f.getProduct().getId()).toList();
        if (!favoriteProductIds.isEmpty()) {
            model.addAttribute("favoriteProductIds",favoriteProductIds);
        }else model.addAttribute("favoriteProductIds",new ArrayList<>());
        return "web/index";
    }

    /*Trang đăng nhập*/
    @GetMapping("/login")
    public String login(Model model) {
        return "web/login";
    }

    /*Trang đăng ký*/
    @GetMapping("/register")
    public String register(Model model) {
        return "web/register";
    }
    /*Trang quên mât khẩu*/
    @GetMapping("/forgetPassword")
    public String forgetPassword(Model model) {
        return "web/forgetPassword";
    }

    /*Trang thay đổi mật khẩu*/
    @GetMapping("/changePassword")
    public String changePassword(@RequestParam String token, Model model) {
        VerifyResponse verifyResponse = authService.confirmChangePassword(token);
        model.addAttribute("verifyResponse", verifyResponse);
        model.addAttribute("tokenString", token);
        return "web/changePassword";
    }

    /*Trang thông báo xác thưc tài khoản*/
    @GetMapping("/verifyAccount")
    public String verifyAccount(@RequestParam String token, Model model) {
        VerifyResponse verifyResponse = authService.confirmRegistration(token);
        model.addAttribute("verifyResponse", verifyResponse);
        return "web/verifyAccount";
    }

    /*Trang thông tin cá nhân*/
    @GetMapping("/user-info")
    public String userInfo(Model model) {
        model.addAttribute("addressesByUserId", addressService.getByCurrentUser_Id());
        return "web/user-info";
    }

    /*Trang danh sách sản phẩm được giảm giá theo chương trình*/
    @GetMapping("/discount/{id}")
    public String discount(
        @PathVariable int id,
        @RequestParam(required = false,defaultValue = "1") int page,
        @RequestParam(required = false,defaultValue = "24") int pageSize,
        Model model
    ) {
        model.addAttribute("discount", discountService.getDiscountById(id));
        Page<Product> pageData = productService.getByDiscount_IdAndStatus(id, true, page, pageSize);
        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage",page);

        //Lấy danh sách favoriteProductIds để kiểm tra xem product nàd đã được yêu thích
        List<Integer> favoriteProductIds = favoriteService.getByUser_IdOrderByCreatedAtDesc().stream().map(f->f.getProduct().getId()).toList();
        if (!favoriteProductIds.isEmpty()) {
            model.addAttribute("favoriteProductIds",favoriteProductIds);
        }else model.addAttribute("favoriteProductIds",new ArrayList<>());
        return "web/discount";
    }
    /*Trang danh sách yêu thích*/
    @GetMapping("/favorite")
    public String favorite(
        Model model
    ) {
        //Lấy danh sách favoriteProductIds để kiểm tra xem product nàd đã được yêu thích
        model.addAttribute("favorites", favoriteService.getByUser_IdOrderByCreatedAtDesc());
        List<Integer> favoriteProductIds = favoriteService.getByUser_IdOrderByCreatedAtDesc().stream().map(f->f.getProduct().getId()).toList();
        if (!favoriteProductIds.isEmpty()) {
            model.addAttribute("favoriteProductIds",favoriteProductIds);
        }else model.addAttribute("favoriteProductIds",new ArrayList<>());
        return "web/favorite";
    }

    /*Trang chi tiết sản phẩm*/
    @GetMapping("/product/{id}/{slug}")
    public String product(@PathVariable int id, @PathVariable String slug, Model model) {
        Product product = productService.findProductByIdAndSlugAndStatus(id,slug,true);
        model.addAttribute("product",product);
        model.addAttribute("reviews",reviewService.findByProduct_IdOrderByCreatedAtDesc(id));

        //Sắp xếp size
        Set<Size> sizes = product.getSizes();
        Set<Size> sortedSizes = new TreeSet<>(Comparator.comparingInt(Size::getOrders));
        sortedSizes.addAll(sizes);
        model.addAttribute("sizes",sortedSizes);

        // Kiểm tra nếu có size với type
        boolean hasSizeType1 = product.getSizes().stream().anyMatch(size -> size.getType().toString().equals("CLOTHES_SIZE"));
        model.addAttribute("hasSizeType1", hasSizeType1);
        boolean hasSizeType2 = product.getSizes().stream().anyMatch(size -> size.getType().toString().equals("SHOES_SIZE"));
        model.addAttribute("hasSizeType2", hasSizeType2);


        //Sắp xếp màu
        Set<Color> colors = product.getColors();
        Set<Color> sortedColor = new TreeSet<>(Comparator.comparingInt(Color::getId));
        sortedColor.addAll(colors);
        model.addAttribute("colors",sortedColor);

        //Kiểm tra lượng hàng trong kho
        List<Quantity> quantities = quantityService.getByProductId(id);
        Map<String, Integer> stockMap = quantities.stream()
            .filter(q -> q.getValue() > 0)
            .collect(Collectors.toMap(
                q -> q.getColor().getId() + "_" + q.getSize().getId(),
                Quantity::getValue
            ));

        model.addAttribute("stockMap", stockMap);

        //Lấy thông tin màu để lấy hình ảnh
        List<ImageProductDetailModel> images = imageService.getAllByColor_IdAndProduct_Id(sortedColor.iterator().next().getId(),product.getId());
        model.addAttribute("images",images);

        //Danh sách gợi ý sản phẩm
        model.addAttribute("ListProductDeCu",productService.findByCategoryIdOrderByCreatedAtDescExcludingProductId(product.getCategory().getId(), product.getId()));

        //Lấy danh sách yêu thích và kểm tra xem sản phẩm này có trong danh sách không
        model.addAttribute("favorites", favoriteService.getByUser_IdOrderByCreatedAtDesc());
        model.addAttribute("favorite", favoriteService.getByProduct_Id(id));
        List<Integer> favoriteProductIds = favoriteService.getByUser_IdOrderByCreatedAtDesc().stream().map(f->f.getProduct().getId()).toList();
        if (!favoriteProductIds.isEmpty()) {
            model.addAttribute("favoriteProductIds",favoriteProductIds);
        }else model.addAttribute("favoriteProductIds",new ArrayList<>());

        return "web/shop-details";
    }

    /*Trang danh sách và lọc sản phẩm*/
    @GetMapping("/product-shop")
    public String productShop(
        Model model,
        @RequestParam(required = false,defaultValue = "1") int page,
        @RequestParam(required = false,defaultValue = "9") int pageSize,
        @RequestParam(required = false) Integer sizeId,
        @RequestParam(required = false) Integer colorId,
        @RequestParam(required = false) String nameKeyword,
        @RequestParam(required = false) Integer categoryParentId,
        @RequestParam(required = false) Integer categoryChildId,
        @RequestParam(required = false, defaultValue = "asc") String sortProduct,
        @RequestParam(required = false) Double startPrice,
        @RequestParam(required = false) Double endPrice
    ) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("colors",colorService.findAllColors());
        model.addAttribute("shoesSizes",sizeService.findSizeByTypeOrderByOrdersAsc(SizeType.SHOES_SIZE));
        model.addAttribute("clothesSize",sizeService.findSizeByTypeOrderByOrdersAsc(SizeType.CLOTHES_SIZE));
        Page<Product> pageData = productService.findAllProductsWithSpec(page,pageSize,sizeId,colorId,true,nameKeyword,categoryParentId,categoryChildId,sortProduct,startPrice,endPrice);
        model.addAttribute("pageData",pageData);
        model.addAttribute("currentPage",page);
        //Lấy danh sách yêu thích và kểm tra xem sản phẩm này có trong danh sách không
        model.addAttribute("favorites", favoriteService.getByUser_IdOrderByCreatedAtDesc());
        List<Integer> favoriteProductIds = favoriteService.getByUser_IdOrderByCreatedAtDesc().stream().map(f->f.getProduct().getId()).toList();
        if (!favoriteProductIds.isEmpty()) {
            model.addAttribute("favoriteProductIds",favoriteProductIds);
        }else model.addAttribute("favoriteProductIds",new ArrayList<>());

        return "web/shop";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        model.addAttribute("cartsByUserId", cartService.getByUser_IdOrderByCreatedAt());
        return "web/shopping-cart";
    }

    @GetMapping("/checkout")
    public String checkOut(Model model) {
        model.addAttribute("cartsByUserId", cartService.getByUser_IdOrderByCreatedAt());
        model.addAttribute("addressesByUserId", addressService.getByCurrentUser_Id());
        model.addAttribute("addressChosen", addressService.getByCurrentUser_IdAndChosen());
        model.addAttribute("cartsByUserId", cartService.getByUser_IdOrderByCreatedAt());
        return "web/checkout";
    }

    @GetMapping("/cod-Return")
    public String codReturn(Model model,@RequestParam(required = true) String codeOrder) {
        Orders order = orderService.getByCodeOrder(codeOrder);
        model.addAttribute("order",order);
        model.addAttribute("orderDetails", orderDetailService.getByOrderId(order.getId()));
        return "web/cod-Return";
    }

    @GetMapping("/order-history")
    public String orderHistory(Model model) {
        model.addAttribute("ordersByUserId", orderService.getByCurrentUser_IdOrderByCreatedAtDesc());
        return "web/order-history";
    }

    /*Trang danh sách blog*/
    @GetMapping("/blog")
    public String blog(
        Model model,
        @RequestParam(required = false,defaultValue = "1") int page,
        @RequestParam(required = false,defaultValue = "6") int pageSize
    ) {
        Page<Blog> pageData = blogService.getByStatusOrderByCreatedAt(true,page,pageSize);
        model.addAttribute("pageData",pageData);
        model.addAttribute("currentPage",page);
        return "web/blog";
    }

    /*Trang chi tiết blog*/
    @GetMapping("/blog/{id}/{slug}")
    public String blogDetail(@PathVariable int id, @PathVariable String slug, Model model) {
        Blog blog = blogService.getByIdAndSlugAndStatus(id,slug,true);
        model.addAttribute("blog", blog);
        model.addAttribute("comments", commentService.getByBlog_IdOrderByCreatedAtDesc(id));
        return "web/blog-details";
    }

    /*Trang danh sách blog theo tag*/
    @GetMapping("/blog/tag/{id}")
    public String blogTag(
        Model model,
        @PathVariable int id,
        @RequestParam(required = false,defaultValue = "1") int page,
        @RequestParam(required = false,defaultValue = "9") int pageSize
    ) {
        Page<Blog> pageData = blogService.getByTagIdAndStatus(id,true,page,pageSize);
        model.addAttribute("tag", tagService.getTagById(id));
        model.addAttribute("pageData",pageData);
        model.addAttribute("currentPage",page);
        return "web/blog-tag";
    }

    @GetMapping("/about")
    public String aboutUs(Model model) {
        return "web/about";
    }

    @GetMapping("/contact")
    public String contactUs(Model model) {
        return "web/contact";
    }
}

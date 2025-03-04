package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.*;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.model.enums.ImageType;
import com.example.demo_clothes_shop_23.model.response.ImageResponse;
import com.example.demo_clothes_shop_23.repository.*;
import com.example.demo_clothes_shop_23.request.CreateSubImageRequest;
import com.example.demo_clothes_shop_23.request.UpsertProductRequest;
import com.example.demo_clothes_shop_23.specifications.ProductSpecifications;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ProductSpecifications productSpecifications;
    public final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final QuantityRepository quantityRepository;
    private final ImageRepository imageRepository;
    private final FileServerService fileServerService;
    private  final ImageService imageService;

    public List<Product> getAllByStatus(Boolean status) {
        return productRepository.findAllByStatus(status);
    }

    public Product findProductByIdAndSlugAndStatus(Integer id, String slug, Boolean status) {
        return productRepository.findProductByIdAndSlugAndStatus(id,slug,status);
    }

    //Tìm các sản phẩm liên quan nhưng không phải sản phẩm đang hiển thị
    public List<Product> findByCategoryIdOrderByCreatedAtDescExcludingProductId(Integer categoryId, Integer excludedMovieId) {
        return productRepository.findByCategoryIdOrderByCreatedAtDescExcludingProductId(categoryId,excludedMovieId).stream().limit(4).toList();
    }

    public void updatePostersFakeData() {
        String sqlUpdatePoster = "UPDATE products p " +
                "SET p.poster = (" +
                "    SELECT i.img_url " +
                "    FROM images i " +
                "    WHERE i.product_id = p.id " +
                "    ORDER BY i.id ASC " +
                "    LIMIT 1" +
                ") ";

        jdbcTemplate.execute(sqlUpdatePoster);

        String sqlDefaultPoster = "UPDATE products SET poster = 'https://placehold.co/400x700?text=NULL' WHERE poster IS NULL";
        jdbcTemplate.execute(sqlDefaultPoster);
    }

    public Page<Product> findAllProductsWithSpec(
        int page,
        int pageSize,
        Integer sizeId,
        Integer colorId,
        Boolean status,
        String nameKeyword,
        Integer categoryParentId,
        Integer categoryChildId,
        String sortProduct,
        Double startPrice,
        Double endPrice
    ) {
        Specification<Product> spec = ProductSpecifications
            .findProducts(
                sizeId,
                colorId,
                status,
                nameKeyword,
                categoryParentId,
                categoryChildId,
                sortProduct,
                startPrice,
                endPrice
            );
        PageRequest pageRequest = PageRequest.of(page-1, pageSize);
        return productRepository.findAll(spec,pageRequest);
    }

    public List<Product> getAllByPriceDifferenceAsc(Boolean status){
        return productRepository.findAllByPriceDifferenceAsc(status)
            .stream()
            .limit(4)
            .toList();
    }

    public List<Product> getByStatusOrderByCreatedAtDesc(Boolean status){
        return productRepository.findByStatusOrderByCreatedAtDesc(status)
            .stream()
            .limit(4)
            .toList();
    }

    public Product getOneProductByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryIdAndStatusTrue(categoryId)
            .stream()
            .findFirst()
            .orElse(null);
    }

    public Page<Product> getByDiscount_IdAndStatus(Integer discountId, Boolean status, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending());
        return productRepository.findByDiscount_IdAndStatus(discountId, status, pageRequest);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }


    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public void createProduct(UpsertProductRequest upsertProductRequest) {
        Slugify slugify = Slugify.builder().build();
        Category category = categoryRepository.findById(upsertProductRequest.getCategoryId()).orElseThrow(
            () -> new ResourceNotFoundException("Không thấy thể loại")
        );
        Set<Color> colors = upsertProductRequest.getColorIds().stream()
            .map(i->colorRepository.findById(i).orElseThrow(() -> new ResourceNotFoundException("Không thấy color")))
            .collect(Collectors.toSet());

        Set<Size> sizes = upsertProductRequest.getSizeIds().stream()
            .map(i->sizeRepository.findById(i).orElseThrow(() -> new ResourceNotFoundException("Không thấy size")))
            .collect(Collectors.toSet());
        Product product = Product.builder()
            .name(upsertProductRequest.getName())
            .slug(slugify.slugify(upsertProductRequest.getName()))
            .description(upsertProductRequest.getDescription())
            .price(upsertProductRequest.getPrice())
            .newPrice(upsertProductRequest.getPrice())
            .rating(0.0)
            .status(upsertProductRequest.getStatus())
            .category(category)
            .colors(colors)
            .sizes(sizes)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        productRepository.save(product);

       colors.forEach(color->{
           for (int i = 0; i < 3; i++) {
               Image image =Image.builder()
                   .imgUrl("https://placehold.co/400x700?text=" +String.valueOf(product.getName().charAt(0)).toUpperCase()+color.getId()+i)
                   .product(product)
                   .color(color)
                   .type(i==0 ? ImageType.MAIN : ImageType.SUP)
                   .createdAt(LocalDateTime.now())
                   .updatedAt(LocalDateTime.now())
                   .build();
               imageRepository.save(image);
            }
       });


        Map<String, Integer> sortedQuantityData = new HashMap<>();
        colors.forEach(color -> {
            sizes.forEach(size -> {
                // Tạo key cho kết hợp màu sắc và kích thước
                String key = color.getId() + "-" + size.getId();
                if (upsertProductRequest.getQuantityData().containsKey(key)) {
                    // Lấy số lượng và thêm vào Map
                    Integer quantity = upsertProductRequest.getQuantityData().get(key);
                    sortedQuantityData.put(key, quantity);
                }
            });
        });

        sortedQuantityData.forEach((key, value) -> {
            String[] parts = key.split("-");
            Integer colorId = Integer.parseInt(parts[0]);
            Integer sizeId = Integer.parseInt(parts[1]);

            // Tìm thực thể Color, Size, và Product (giả định có Product ID)
            Color colorEntity = colorRepository.findById(colorId).orElseThrow(() -> new RuntimeException("Không thấy color"));
            Size sizeEntity = sizeRepository.findById(sizeId).orElseThrow(() -> new RuntimeException("Không thấy size"));

            // Tạo và lưu thực thể Quantity
            Quantity quantityEntity = Quantity.builder()
                .color(colorEntity)
                .size(sizeEntity)
                .product(product)
                .value(value)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            quantityRepository.save(quantityEntity);
        });
    }


    public Map<Integer, List<Image>> updateProduct(Integer productId, UpsertProductRequest upsertProductRequest) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ResourceNotFoundException("Không tìm thấy sản phẩm")
        );
        Slugify slugify = Slugify.builder().build();
        Category category = categoryRepository.findById(upsertProductRequest.getCategoryId()).orElseThrow(
            () -> new ResourceNotFoundException("Không thấy thể loại")
        );
        Set<Color> colors = upsertProductRequest.getColorIds().stream()
            .map(i->colorRepository.findById(i).orElseThrow(() -> new ResourceNotFoundException("Không thấy color")))
            .collect(Collectors.toSet());

        Set<Size> sizes = upsertProductRequest.getSizeIds().stream()
            .map(i->sizeRepository.findById(i).orElseThrow(() -> new ResourceNotFoundException("Không thấy size")))
            .collect(Collectors.toSet());

        product.getColors().forEach(oldColor->{
            if (!colors.contains(oldColor)){
                List<Image> images = imageRepository.findAllByColor_IdAndProduct_Id(oldColor.getId(), productId);
                imageRepository.deleteAll(images);
            }
        });

        colors.forEach(color->{
            if (imageRepository.findAllByColor_IdAndProduct_Id(color.getId(), productId).isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    Image image =Image.builder()
                        .imgUrl("https://placehold.co/400x700?text=" +String.valueOf(product.getName().charAt(0)).toUpperCase()+color.getId()+i)
                        .product(product)
                        .color(color)
                        .type(i==0 ? ImageType.MAIN : ImageType.SUP)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                    imageRepository.save(image);
                }
            }
        });

        List<Quantity> quantities = quantityRepository.findByProductId(productId);
        quantityRepository.deleteAll(quantities);

        Map<String, Integer> sortedQuantityData = new HashMap<>();
        colors.forEach(color -> {
            sizes.forEach(size -> {
                // Tạo key cho kết hợp màu sắc và kích thước
                String key = color.getId() + "-" + size.getId();
                if (upsertProductRequest.getQuantityData().containsKey(key)) {
                    // Lấy số lượng và thêm vào Map
                    Integer quantity = upsertProductRequest.getQuantityData().get(key);
                    sortedQuantityData.put(key, quantity);
                }
            });
        });

        sortedQuantityData.forEach((key, value) -> {
            String[] parts = key.split("-");
            Integer colorId = Integer.parseInt(parts[0]);
            Integer sizeId = Integer.parseInt(parts[1]);

            // Tìm thực thể Color, Size, và Product (giả định có Product ID)
            Color colorEntity = colorRepository.findById(colorId).orElseThrow(() -> new RuntimeException("Không thấy color"));
            Size sizeEntity = sizeRepository.findById(sizeId).orElseThrow(() -> new RuntimeException("Không thấy size"));

            // Tạo và lưu thực thể Quantity
            Quantity quantityEntity = Quantity.builder()
                .color(colorEntity)
                .size(sizeEntity)
                .product(product)
                .value(value)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            quantityRepository.save(quantityEntity);
        });

        product.setPrice(upsertProductRequest.getPrice());
        productRepository.save(product);

        if (product.getDiscount()!=null){
            if (Objects.equals(product.getDiscount().getType().toString(), "PERCENT")){
                // Lấy giá sản phẩm và tính toán giá mới sau khi giảm giá
                Long price = product.getPrice();
                Long discountAmount = product.getDiscount().getAmount();
                double discountPercent = discountAmount / 100.0;
                Long newPrice = Math.round(price * (1 - discountPercent));
                product.setNewPrice(newPrice);

            }else if (Objects.equals(product.getDiscount().getType().toString(), "SAME_PRICE")){
                // Lấy giá sản phẩm và tính toán giá mới sau khi giảm giá
                Long discountAmount = product.getDiscount().getAmount();

                product.setNewPrice(discountAmount);
            }else {
                // Lấy giá sản phẩm và tính toán giá mới sau khi giảm giá
                Long price = product.getPrice();
                Long discountAmount = product.getDiscount().getAmount();
                Long newPrice =  price - discountAmount;

                product.setNewPrice(newPrice);
            }
        } else{
            product.setNewPrice(product.getPrice());
        }

        product.setName(upsertProductRequest.getName());
        product.setSlug(slugify.slugify(upsertProductRequest.getName()));
        product.setDescription(upsertProductRequest.getDescription());
        product.setStatus(upsertProductRequest.getStatus());
        product.setCategory(category);
        product.setColors(colors);
        product.setSizes(sizes);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        List<Image> images = imageService.getByProductId(productId);

        Map<Integer, List<Image>> imageMap = images.stream()
            .sorted(Comparator.comparing(img -> img.getType() == ImageType.MAIN ? 0 : 1))
            .collect(Collectors.groupingBy(
                img -> img.getColor().getId(),
                Collectors.toList()
            ));
        return imageMap;
    }

    public List<Product> getByDiscount_Id(Integer id) {
        return productRepository.findByDiscount_Id(id);
    }

    public List<Product> getByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public String updatePoster(Integer productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
            .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        ImageResponse imageResponse = fileServerService.uploadFile(file);
        product.setPoster(imageResponse.getUrl());
        productRepository.save(product);
        return product.getPoster();
    }

    public Image updateMainImage(Integer mainImageId, MultipartFile file) {
        Image image = imageRepository.findById(mainImageId).orElseThrow(
            ()-> new ResourceNotFoundException("Không tìm thấy ảnh")
        );
        ImageResponse imageResponse = fileServerService.uploadFile(file);
        image.setImgUrl(imageResponse.getUrl());
        image.setUpdatedAt(LocalDateTime.now());
        imageRepository.save(image);
        return image;
    }

    public Image createSubImage(CreateSubImageRequest createSubImageRequest) {
        ImageResponse imageResponse = fileServerService.uploadFile(createSubImageRequest.getFormData());

        Color color = colorRepository.findById(createSubImageRequest.getColorId()).orElseThrow(
            ()->new ResourceNotFoundException("Không tìm thấy màu")
        );

        Product product = productRepository.findById(createSubImageRequest.getProductId())
            .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        Image image = Image.builder()
            .imgUrl(imageResponse.getUrl())
            .type(ImageType.SUP)
            .color(color)
            .product(product)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        imageRepository.save(image);
        return image;
    }

    public void deleteImage(Integer imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(
            ()->new ResourceNotFoundException("Không tìm thấy ảnh")
        );
        imageRepository.delete(image);
    }
}

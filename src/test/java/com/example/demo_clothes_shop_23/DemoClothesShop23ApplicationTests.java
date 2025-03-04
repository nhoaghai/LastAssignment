package com.example.demo_clothes_shop_23;

import com.example.demo_clothes_shop_23.entities.*;
import com.example.demo_clothes_shop_23.model.enums.*;
import com.example.demo_clothes_shop_23.repository.*;
import com.example.demo_clothes_shop_23.service.ProductService;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;


@SpringBootTest
class DemoClothesShop23ApplicationTests {
	private static final Logger logger = Logger.getLogger(DemoClothesShop23ApplicationTests.class.getName());
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	BlogRepository blogRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ColorRepository colorRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private DiscountRepository discountRepository;
	@Autowired
	private FavoriteRepository favoriteRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private OrdersDetailRepository ordersDetailRepository;
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private QuantityRepository quantityRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private SizeRepository sizeRepository;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BannerRepository bannerRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private PasswordEncoder passwordEncoder;


	@Test
	void save_categories(){
		Slugify slugify = Slugify.builder().build();
		Category categoryParent1 = Category.builder()
				.name("Áo")
				.slug(slugify.slugify("Áo"))
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category categoryParent2 = Category.builder()
				.name("Quần")
				.slug(slugify.slugify("Quần"))
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category categoryParent3 = Category.builder()
				.name("Giày")
				.slug(slugify.slugify("Giày"))
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category categoryParent4 = Category.builder()
				.name("Đồ Dùng Học Tập")
				.slug(slugify.slugify("Đồ Dùng Học Tập"))
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category categoryParent5 = Category.builder()
				.name("BaLo")
				.slug(slugify.slugify("Balo"))
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category1 = Category.builder()
				.name("Áo Thun")
				.slug(slugify.slugify("Áo Thun"))
				.parentId(1)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category2 = Category.builder()
				.name("Áo Polo")
				.slug(slugify.slugify("Áo Polo"))
				.parentId(1)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category3 = Category.builder()
				.name("Áo Nỉ & Hoodie")
				.slug(slugify.slugify("Áo Nỉ & Hoodie"))
				.parentId(1)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category4 = Category.builder()
				.name("Áo Sơ Mi")
				.slug(slugify.slugify("Áo Sơ Mi"))
				.parentId(1)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category5 = Category.builder()
				.name("Áo Khoác")
				.slug(slugify.slugify("Áo Khoác"))
				.parentId(1)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category6 = Category.builder()
				.name("Quần Short")
				.slug(slugify.slugify("Quần Short"))
				.parentId(2)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category7 = Category.builder()
				.name("Quần Dài")
				.slug(slugify.slugify("Quần Dài"))
				.parentId(2)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category8 = Category.builder()
				.name("Giày Thời Trang")
				.slug(slugify.slugify("Giày Thời Trang"))
				.parentId(3)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category9 = Category.builder()
				.name("Giày Thể Thao")
				.slug(slugify.slugify("Giày Thể Thao"))
				.parentId(3)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category10 = Category.builder()
				.name("Bút Bi")
				.slug(slugify.slugify("Bút Bi"))
				.parentId(4)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Category category11 = Category.builder()
				.name("Phấn")
				.slug(slugify.slugify("Phấn"))
				.parentId(4)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();

		categoryRepository.save(categoryParent1);
		categoryRepository.save(categoryParent2);
		categoryRepository.save(categoryParent3);
		categoryRepository.save(category1);
		categoryRepository.save(category2);
		categoryRepository.save(category3);
		categoryRepository.save(category4);
		categoryRepository.save(category5);
		categoryRepository.save(category6);
		categoryRepository.save(category7);
		categoryRepository.save(category8);
		categoryRepository.save(category9);
	}

	@Test
	void save_colors(){
		Color color1 = Color.builder()
				.colorName("Đen")
				.colorCode("#000000")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Color color2 = Color.builder()
				.colorName("Trắng")
				.colorCode("#FFFFFF")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Color color3 = Color.builder()
				.colorName("Đỏ")
				.colorCode("#FF0000")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Color color4 = Color.builder()
				.colorName("Vàng")
				.colorCode("#FFFF00")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Color color5 = Color.builder()
				.colorName("Xanh lá cây")
				.colorCode("#008000")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Color color6 = Color.builder()
				.colorName("Xanh Nước Bển")
				.colorCode("#0000FF")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		colorRepository.save(color1);
		colorRepository.save(color2);
		colorRepository.save(color3);
		colorRepository.save(color4);
		colorRepository.save(color5);
		colorRepository.save(color6);
	}

	@Test
	void save_sizes(){
		Size size1 = Size.builder()
				.sizeName("S")
				.orders(1)
				.type(SizeType.CLOTHES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size2 = Size.builder()
				.sizeName("M")
				.orders(2)
				.type(SizeType.CLOTHES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size3 = Size.builder()
				.sizeName("L")
				.orders(3)
				.type(SizeType.CLOTHES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size4 = Size.builder()
				.sizeName("XL")
				.orders(4)
				.type(SizeType.CLOTHES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size5 = Size.builder()
				.sizeName("XXL")
				.orders(5)
				.type(SizeType.CLOTHES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size6 = Size.builder()
				.sizeName("36")
				.orders(1)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size7 = Size.builder()
				.sizeName("37")
				.orders(2)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size8 = Size.builder()
				.sizeName("38")
				.orders(3)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size9 = Size.builder()
				.sizeName("39")
				.orders(4)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size10 = Size.builder()
				.sizeName("40")
				.orders(5)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size11 = Size.builder()
				.sizeName("41")
				.orders(6)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size12 = Size.builder()
				.sizeName("42")
				.orders(7)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Size size13 = Size.builder()
				.sizeName("43")
				.orders(8)
				.type(SizeType.SHOES_SIZE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		sizeRepository.save(size1);
		sizeRepository.save(size2);
		sizeRepository.save(size3);
		sizeRepository.save(size4);
		sizeRepository.save(size5);
		sizeRepository.save(size6);
		sizeRepository.save(size7);
		sizeRepository.save(size8);
		sizeRepository.save(size9);
		sizeRepository.save(size10);
		sizeRepository.save(size11);
		sizeRepository.save(size12);
		sizeRepository.save(size13);
	}

	@Test
	void save_discounts(){
		Faker faker = new Faker();
		for (int i=0;i<5;i++){
			DiscountType type = (i < 2) ? DiscountType.AMOUNT : (i == 2) ? DiscountType.PERCENT : DiscountType.SAME_PRICE;
			long amount = (i < 2) ? 100000 : (i == 2) ? 50 : 200000;
			String name = faker.funnyName().name();
			Discount discount = Discount.builder()
					.name(name)
					.description(faker.lorem().paragraph())
					.type(type)
					.amount(amount)
					.startDate(LocalDateTime.now())
					.endDate(LocalDateTime.now().plusYears(1))
					.active(i != 0)
					.imageUrl("https://placehold.co/1920x800?text=" +String.valueOf(name.charAt(0)).toUpperCase())
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			discountRepository.save(discount);
		}
	}

	@Test
	void save_coupons(){
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();
		for (int i=0;i<5;i++){
			Coupon coupon = Coupon.builder()
					.code(slugify.slugify(faker.name().name()))
					.amount(50)
					.startDate(LocalDateTime.now())
					.endDate(LocalDateTime.now().plusYears(1))
					.active(i != 0)
					.quantity(100)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			couponRepository.save(coupon);
		}
	}

	@Test
	void save_tags(){
		Tag tag1 =Tag.builder()
				.name("Chương trình khuyến mại")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Tag tag2 =Tag.builder()
				.name("Sự kiện thời trang")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Tag tag3 =Tag.builder()
				.name("Thông tin thời trang")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		tagRepository.save(tag1);
		tagRepository.save(tag2);
		tagRepository.save(tag3);
	}

	@Test
	void save_users(){
		Faker faker = new Faker();
		Random random = new Random();
		List<Coupon> coupons = couponRepository.findAll();
		Coupon rdCoupon = coupons.get(random.nextInt(coupons.size()));
		for (int i = 0; i < 50; i++) {
			String name = faker.name().fullName();
			User user =User.builder()
					.name(name)
					.email(faker.internet().emailAddress())
					.avatar("https://placehold.co/600x600?text=" +String.valueOf(name.charAt(0)).toUpperCase())
					.password("123")
					.role(i==0 || i==1 ? UserRole.ADMIN : UserRole.USER)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			userRepository.save(user);
		}
	}

	@Test
	void save_addresses(){
		Faker faker = new Faker();
		Random random = new Random();
		List<User> users = userRepository.findAll();
		for (User user : users) {
			Address address = Address.builder()
					.receiverName(faker.name().fullName())
					.phone(faker.phoneNumber().phoneNumber())
					.province(faker.address().state())
					.district(faker.address().city())
					.ward(faker.address().streetName())
					.addressDetail(faker.address().streetAddressNumber())
					.chosen(false)
					.user(user)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			addressRepository.save(address);
		}
	}

	@Test
	void save_products(){
		Faker faker = new Faker();
		Random random = new Random();
		Slugify slugify = Slugify.builder().build();
		List<Discount> discounts = discountRepository.findAll();
		List<Category> categories = categoryRepository.findAll().stream().skip(4).toList();
		List<Color> colors = colorRepository.findAll().stream().filter(c->c.getId()!=1).toList();
		Color blackColor = colorRepository.findById(1).orElseThrow(() -> new RuntimeException("Color with ID 1 not found"));
		SizeType[] sizeTypes = SizeType.values();

		for (int i = 0; i < 50; i++) {
			String name = faker.book().title();
			double price = 300000 + (random.nextDouble() * (1000000 - 300000));
			double roundedPrice = Math.round(price / 100.0) * 100.0;
			Discount rdDiscount = random.nextBoolean() ? discounts.get(random.nextInt(discounts.size())) : null;
			Category rdCategory = categories.get(random.nextInt(categories.size()));
			Set<Color> rdColors = new HashSet<>();
			rdColors.add(blackColor);
			for (int j = 0; j < random.nextInt(3)+1; j++) {
				Color rdColor = colors.get(random.nextInt(colors.size()));
                rdColors.add(rdColor);
			}
			Set<Size> rdSizes = new HashSet<>();
			if (Arrays.asList(4,5,6,7,8,9,10).contains(rdCategory.getId())) {
				Set<Size> rdSizes1 = sizeRepository.findSizeByTypeOrderByOrdersAsc(SizeType.CLOTHES_SIZE);
				rdSizes.addAll(rdSizes1);
			}else if (Arrays.asList(11,12).contains(rdCategory.getId())){
				Set<Size> rdSizes2 = sizeRepository.findSizeByTypeOrderByOrdersAsc(SizeType.SHOES_SIZE);
				rdSizes.addAll(rdSizes2);
			}

			Product product = Product.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.description(faker.lorem().paragraph())
					.price((long) roundedPrice)
					.status(i < 40)
					.discount(rdDiscount)
					.category(rdCategory)
					.colors(rdColors)
					.sizes(rdSizes)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			productRepository.save(product);
		}
	}

	@Test
	void save_images(){
		List<Product> products = productRepository.findAllWithColors();
		for (Product product : products) {
			for (Color color : product.getColors()) {
				for (int i = 0; i < 3; i++) {
					Image image =Image.builder()
							.imgUrl("https://placehold.co/400x700?text=" +String.valueOf(product.getName().charAt(0)).toUpperCase())
							.product(product)
							.color(color)
							.type(i==0 ? ImageType.MAIN : ImageType.SUP)
							.createdAt(LocalDateTime.now())
							.updatedAt(LocalDateTime.now())
							.build();
					imageRepository.save(image);
				}
			}
		}
	}

	@Test
	void save_quantities() {
		List<Product> products = productRepository.findAllWithColorsAndSizes();
		for (Product product : products) {
			for (Color color : product.getColors()) {
				for (Size size : product.getSizes()) {
					Quantity quantity = Quantity.builder()
							.value(12)
							.product(product)
							.color(color)
							.size(size)
							.createdAt(LocalDateTime.now())
							.updatedAt(LocalDateTime.now())
							.build();
					quantityRepository.save(quantity);
					logger.info("Saved quantity: " + quantity);
				}
			}
		}
	}

	@Test
	void save_banners(){
		Banner banner1 = Banner.builder()
				.name("Banner 1")
				.status(true)
				.thumbnail("https://placehold.co/1920x800?text=B1")
				.id(1)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Banner banner2 = Banner.builder()
				.name("Banner 2")
				.status(true)
				.thumbnail("https://placehold.co/1920x800?text=B2")
				.id(2)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		Banner banner3 = Banner.builder()
				.name("Banner 3")
				.status(false)
				.thumbnail("https://placehold.co/1920x800?text=B3")
				.id(3)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		bannerRepository.save(banner1);
		bannerRepository.save(banner2);
		bannerRepository.save(banner3);
	}

	@Test
	void save_blogs(){
		List<User> users = userRepository.findByRole(UserRole.ADMIN);
		List<Tag> tags = tagRepository.findAll();
		Random random = new Random();
		Faker faker = new Faker();
		Slugify slugify= Slugify.builder().build();
		for (int i = 0; i < 30; i++) {
			String title = faker.book().title();
			Blog blog = Blog.builder()
					.title(title)
					.slug(slugify.slugify(title))
					.description(faker.lorem().paragraph())
					.content(faker.lorem().paragraph(50))
					.thumbnail("https://placehold.co/600x400?text=" +String.valueOf(title.charAt(0)).toUpperCase())
					.status(faker.bool().bool())
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.user(users.get(random.nextInt(users.size())))
					.tags(tags.subList(0,random.nextInt(tags.size()+1)))
					.build();
			blogRepository.save(blog);
		}
	}

	@Test
	void save_comments(){
		Faker faker = new Faker();
		Random random = new Random();
		List<User> users = userRepository.findByRole(UserRole.USER);
		List<Blog> blogs =blogRepository.findAll();
		for (Blog blog : blogs) {
			for (int i = 0; i < random.nextInt(6)+5; i++) {
				Comment comment =Comment.builder()
						.content(faker.lorem().paragraph())
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.user(users.get(random.nextInt(users.size())))
						.blog(blog)
						.build();
				commentRepository.save(comment);
			}

		}
	}

	@Test
	void save_reviews(){
		Faker faker = new Faker();
		Random random = new Random();
		List<User> users = userRepository.findByRole(UserRole.USER);
		List<Product> products =productRepository.findAll();
		for (Product product : products) {
			for (int i = 0; i < random.nextInt(6) + 5; i++) {
				Review review = Review.builder()
						.content(faker.lorem().paragraph())
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.user(users.get(random.nextInt(users.size())))
						.rating(random.nextInt(5) + 1)
						.product(product)
						.build();
				reviewRepository.save(review);
			}
		}
	}

	@Test
	void update_rating(){
		List<Product> products = productRepository.findAll();
		for (Product product : products) {
			product.setRating(reviewRepository.findAverageRatingByProductId(product.getId()));
			productRepository.save(product);
		}
	}

	@Test
	void save_carts(){
		Random random = new Random();
		List<User> users = userRepository.findAll();
		List<Product> products =productRepository.findAll();
		for (User user : users) {
			for (int i = 0; i < random.nextInt(2) + 1; i++) {
				Product product = products.get(random.nextInt(products.size()));
				Integer quantity = random.nextInt(1)+1;
				Cart cart = Cart.builder()
						.user(user)
						.product(product)
						.quantity(quantity)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.build();
				cartRepository.save(cart);
			}
		}
	}

	@Test
	void save_favorites(){
		List<User> users = userRepository.findByRole(UserRole.USER);
		List<Product> products = productRepository.findByStatus(true);

		for (User user : users) {
			// 1 -> 4 favorite movies. Each favorite has a unique movie
			List<Product> favoriteProducts = new ArrayList<>();
			for (int i = 0; i < new Random().nextInt(4) + 1; i++) {
				Product product = products.get(new Random().nextInt(products.size()));
				if (!favoriteProducts.contains(product)) {
					favoriteProducts.add(product);
				}
			}

			for (Product product : favoriteProducts) {
				Favorite favorite = Favorite.builder()
						.user(user)
						.product(product)
						.createdAt(LocalDateTime.now())
						.build();
				favoriteRepository.save(favorite);
			}
		}
	}

	@Test
	void save_orders(){
		Faker faker = new Faker();
		Random random = new Random();
		OrdersStatus[] ordersStatuses = OrdersStatus.values();
		DeliveryType[] deliveryTypes = DeliveryType.values();
		PaymentType[] paymentTypes = PaymentType.values();
		List<User> users = userRepository.findAll();
		for (int i = 0; i < 30; i++) {
			Orders orders = Orders.builder()
					.user(users.get(random.nextInt(users.size())))
					.email(faker.internet().emailAddress())
					.receiverName(faker.name().fullName())
					.phone(faker.phoneNumber().phoneNumber())
					.province(faker.address().state())
					.district(faker.address().city())
					.ward(faker.address().streetName())
					.addressDetail(faker.address().streetAddressNumber())
					.notes(faker.lorem().paragraph())
					.totalPrice((long) faker.number().numberBetween(100, 300))
					.finalTotal(faker.number().randomNumber())
					.status(ordersStatuses[random.nextInt(ordersStatuses.length)])
					.delivery(deliveryTypes[random.nextInt(deliveryTypes.length)])
					.payment(paymentTypes[random.nextInt(paymentTypes.length)])
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			ordersRepository.save(orders);
		}
	}

	@Test
	void save_ordersDetails(){
		Random random = new Random();
		Faker faker = new Faker();
		List<Orders> orders = ordersRepository.findAll();
		List<Product> products = productRepository.findAll();
		for (Orders order : orders) {
			for (int i = 0; i < random.nextInt(3)+1; i++) {
				Product product = products.get(random.nextInt(products.size()));
				Integer quantity = random.nextInt(1)+1;
				OrdersDetail ordersDetail = OrdersDetail.builder()
						.orders(order)
						.product(product)
						.quantity(quantity)
						.price(product.getPrice()*quantity)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.build();
				ordersDetailRepository.save(ordersDetail);
			}
		}
	}

	@Test
	void update_totalPrice(){
		List<Orders> orders = ordersRepository.findAll();
		for (Orders order : orders) {
			order.setTotalPrice(ordersDetailRepository.findTotalPriceByOrderId(order.getId()));
			ordersRepository.save(order);
		}
	}

	@Test
	void update_poster(){
		List<Product> products = productRepository.findAll();
		for (Product product : products) {
			if (product.getPoster() == null) {
				// Đặt poster thành URL mặc định nếu poster hiện tại là null
				product.setPoster("https://placehold.co/400x700?text=NULL");
			} else {
				// Kiểm tra và xử lý nếu `colors` không null và không rỗng
				Set<Color> colors = product.getColors();
				if (colors != null && !colors.isEmpty()) {
					// Lấy danh sách màu của sản phẩm và sắp xếp theo ID
					Set<Color> sortedColor = new TreeSet<>(Comparator.comparingInt(Color::getId));
					sortedColor.addAll(colors);

					// Lấy hình ảnh đầu tiên của màu đầu tiên trong danh sách đã sắp xếp
					Color firstColor = sortedColor.iterator().next();
					List<Image> images = imageRepository.findAllByColor_IdAndProduct_Id(firstColor.getId(), product.getId());
					if (!images.isEmpty()) {
						product.setPoster(images.get(0).getImgUrl());
					}
				}
			}
			productRepository.save(product);
		}
		productService.updatePostersFakeData();
	}

	@Test
	void update_newPrice(){
		List<Product> products = productRepository.findAll();
		for (Product product : products) {
			if (product.getDiscount()!=null){
				if (Objects.equals(product.getDiscount().getType().toString(), "PERCENT")){
					// Lấy giá sản phẩm và tính toán giá mới sau khi giảm giá
					Long price = product.getPrice();
					Long discountAmount = product.getDiscount().getAmount();
					double discountPercent = discountAmount / 100.0;
					Long newPrice = Math.round(price * (1 - discountPercent));
					product.setNewPrice(newPrice);

					product.setNewPrice(newPrice);
					productRepository.save(product);
				}else if (Objects.equals(product.getDiscount().getType().toString(), "SAME_PRICE")){
					// Lấy giá sản phẩm và tính toán giá mới sau khi giảm giá
					double discountAmount = product.getDiscount().getAmount();

					product.setNewPrice((long) discountAmount);
					productRepository.save(product);
				}else {
					// Lấy giá sản phẩm và tính toán giá mới sau khi giảm giá
					double price = product.getPrice();
					double discountAmount = product.getDiscount().getAmount();
					long newPrice = (long) (price - discountAmount);

					product.setNewPrice(newPrice);
					productRepository.save(product);
				}
			} else{
				product.setNewPrice(product.getPrice());
				productRepository.save(product);
			}
		}
	}

	@Test
	void updatePrivatePassword(){
		List<User> users = userRepository.findAll();
		for (User user : users) {
			user.setPassword(passwordEncoder.encode("123"));
			userRepository.save(user);
		}
	}

	@Test
	void updateColorIdAndSizeIdForCart(){
		// Lấy tất cả giỏ hàng từ cơ sở dữ liệu
		List<Cart> carts = cartRepository.findAll();

		for (Cart cart : carts) {
			try {
				// Lấy ID của màu đầu tiên trong danh sách màu sắc của sản phẩm
				Integer colorId = cart.getProduct().getColors()
					.stream()
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("No colors found for product"))
					.getId();

				// Lấy ID của kích thước đầu tiên trong danh sách kích thước của sản phẩm
				Integer sizeId = cart.getProduct().getSizes()
					.stream()
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("No sizes found for product"))
					.getId();

				// Tìm màu sắc tương ứng trong cơ sở dữ liệu và cập nhật vào giỏ hàng
				Color color = colorRepository.findById(colorId)
					.orElseThrow(() -> new IllegalStateException("Color not found with ID: " + colorId));
				cart.setColor(color);

				// Tìm kích thước tương ứng trong cơ sở dữ liệu và cập nhật vào giỏ hàng
				Size size = sizeRepository.findById(sizeId)
					.orElseThrow(() -> new IllegalStateException("Size not found with ID: " + sizeId));
				cart.setSize(size);

				// Lưu giỏ hàng đã được cập nhật
				cartRepository.save(cart);

			} catch (Exception e) {
				// Ghi lại lỗi hoặc xử lý theo yêu cầu
				System.err.println("Error updating cart: " + e.getMessage());
			}
		}
	}

	@Test
	void updateColorIdAndSizeIdForOrderDetail(){
		// Lấy tất cả giỏ hàng từ cơ sở dữ liệu
		List<OrdersDetail> ordersDetails = ordersDetailRepository.findAll();

		for (OrdersDetail ordersDetail : ordersDetails) {
			try {
				// Lấy ID của màu đầu tiên trong danh sách màu sắc của sản phẩm
				Integer colorId = ordersDetail.getProduct().getColors()
					.stream()
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("No colors found for product"))
					.getId();

				// Lấy ID của kích thước đầu tiên trong danh sách kích thước của sản phẩm
				Integer sizeId = ordersDetail.getProduct().getSizes()
					.stream()
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("No sizes found for product"))
					.getId();

				// Tìm màu sắc tương ứng trong cơ sở dữ liệu và cập nhật vào giỏ hàng
				Color color = colorRepository.findById(colorId)
					.orElseThrow(() -> new IllegalStateException("Color not found with ID: " + colorId));
				ordersDetail.setColor(color);

				// Tìm kích thước tương ứng trong cơ sở dữ liệu và cập nhật vào giỏ hàng
				Size size = sizeRepository.findById(sizeId)
					.orElseThrow(() -> new IllegalStateException("Size not found with ID: " + sizeId));
				ordersDetail.setSize(size);

				// Lưu giỏ hàng đã được cập nhật
				ordersDetailRepository.save(ordersDetail);

			} catch (Exception e) {
				// Ghi lại lỗi hoặc xử lý theo yêu cầu
				System.err.println("Error updating ordersDetail: " + e.getMessage());
			}
		}
	}

	@Test
	void updatePriceForOrderDetail(){
		List<OrdersDetail> ordersDetails = ordersDetailRepository.findAll();
		ordersDetails.forEach(ordersDetail -> {
			ordersDetail.setPrice(ordersDetail.getProduct().getPrice()*ordersDetail.getQuantity());
			ordersDetailRepository.save(ordersDetail);
		});
	}
}

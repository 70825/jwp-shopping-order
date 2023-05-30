package cart.ui;

import cart.application.CartItemService;
import cart.application.ProductService;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.HomePagingRequest;
import cart.dto.HomePagingResponse;
import cart.dto.ProductCartItemResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;
    private final CartItemService cartItemService;

    public ProductApiController(final ProductService productService,
                                final CartItemService cartItemService) {
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        final Product product = productService.getProductById(id);
        return ResponseEntity.ok(ProductResponse.of(product));
    }

    @GetMapping("/cart-items")
    public ResponseEntity<HomePagingResponse> getHomePagingProduct(@RequestBody HomePagingRequest homePagingRequest) {
        final Long lastIdInPrevPage = homePagingRequest.getLastId();
        final int pageItemCount = homePagingRequest.getPageItemCount();

        final List<Product> products = productService.getProductsInPaging(lastIdInPrevPage, pageItemCount);
        final boolean isLast = productService.hasLastProduct(lastIdInPrevPage, pageItemCount);

        return ResponseEntity.ok(HomePagingResponse.of(products, isLast));
    }

    @GetMapping("/{productId}/cart-items")
    public ResponseEntity<ProductCartItemResponse> getProductCartItemByProductId(@PathVariable Long productId,
                                                                                 Member member) {
        final Product product = productService.getProductById(productId);
        final CartItem cartItem = cartItemService.findByMemberAndProduct(member, product);

        final ProductCartItemResponse productCartItemResponse = ProductCartItemResponse.of(product, cartItem);

        return ResponseEntity.ok(productCartItemResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

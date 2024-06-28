package gift.application;

import gift.dao.ProductRepository;
import gift.domain.Product;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponse> responseList = new ArrayList<>();
        for (Product product : productList) {
            responseList.add(new ProductResponse(product));
        }
        return responseList;
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 상품은 존재하지 않습니다")
        );
        return new ProductResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        try {
            Optional<Product> product = productRepository.findById(request.id());
        } catch (Exception exception) {
            return new ProductResponse(productRepository.save(new Product(request)));
        }
        throw new IllegalArgumentException("해당 상품은 이미 존재하고 있습니다.");
    }

    public Long deleteProductById(Long id) {
        productRepository.deleteById(id);
        return id;
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public Long updateProduct(Long id, ProductRequest request) {
        if (!id.equals(request.id())) {
            deleteProductById(id);
            createProduct(request);
            return request.id();
        }
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 상품은 존재하지 않습니다")
        );
        product.update(request);
        productRepository.update(id, product);
        return id;
    }

}
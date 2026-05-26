package com.returnradar.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.returnradar.dto.response.HighReturnProductResponse;
import com.returnradar.entity.Product;
import com.returnradar.enums.ProductCategory;
import com.returnradar.repository.AnalyticsSnapshotRepository;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.repository.DataQualityIssueRepository;
import com.returnradar.repository.ProductRepository;
import com.returnradar.repository.ReturnRequestRepository;
import com.returnradar.service.impl.AnalyticsServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighReturnProductTest {

    @Mock
    private ReturnRequestRepository returnRequestRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DataQualityIssueRepository dataQualityIssueRepository;

    @Mock
    private AnalyticsSnapshotRepository analyticsSnapshotRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    void test_getHighReturnProducts_returnsOnlyProductsAboveThreshold() {
        when(productRepository.findByReturnRateGreaterThan(10.0))
                .thenReturn(List.of(
                        highReturnProduct(1L, "Nike Air Max", 15.0),
                        highReturnProduct(2L, "Zara Floral Dress", 21.5)
                ));

        List<HighReturnProductResponse> result = analyticsService.getHighReturnProducts();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(product -> product.getReturnRate() > 10.0));
    }

    private Product highReturnProduct(Long productId, String name, Double returnRate) {
        return Product.builder()
                .productId(productId)
                .name(name)
                .brand("Brand")
                .category(ProductCategory.SHOES)
                .totalSold(100)
                .totalReturned(returnRate.intValue())
                .returnRate(returnRate)
                .build();
    }
}

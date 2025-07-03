package com.thanhnt.productservice.domain.repository.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.thanhnt.productservice.api.request.ProductCriteria;
import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductElasticsearchRepository {
  private final ElasticsearchClient elasticsearchClient;
  private static final String INDEX_NAME = "products";

  public String createOrUpdateProduct(ProductElasticsearchDTO product) {
    try {
      log.info("Creating or updating product in Elasticsearch: {}", product.getId());
      IndexResponse response =
          elasticsearchClient.index(
              i -> i.index(INDEX_NAME).id(String.valueOf(product.getId())).document(product));
      Map<String, String> responseMap =
          Map.of("Create", "Document has been create", "Update", "Document has been update");
      log.info("Product indexed successfully: {}", product.getId());
      return responseMap.get(response.result().name());
    } catch (Exception e) {
      log.error("Failed to create or update product in Elasticsearch: {}", product.getId(), e);
      throw new RuntimeException("Failed to create or update product in Elasticsearch", e);
    }
  }

  public ProductElasticsearchDTO findById(String productId) {
    try {
      log.info("Finding product by ID in Elasticsearch: {}", productId);
      return elasticsearchClient
          .get(g -> g.index(INDEX_NAME).id(productId), ProductElasticsearchDTO.class)
          .source();
    } catch (ElasticsearchException e) {
      return null;
    } catch (Exception e) {
      log.error("Failed to find product by ID in Elasticsearch: {}", productId, e);
      throw new RuntimeException("Unexpected error occurred", e);
    }
  }

  public String deleteById(String productId) {
    try {
      log.info("Deleting product by ID in Elasticsearch: {}", productId);
      DeleteRequest request = DeleteRequest.of(d -> d.index(INDEX_NAME).id(productId));
      DeleteResponse response = elasticsearchClient.delete(request);
      return new StringBuffer(
              response.result().name().equalsIgnoreCase("NOT_FOUND")
                  ? "Document not found " + productId
                  : "Document deleted")
          .toString();
    } catch (Exception e) {
      log.error("Failed to delete product in Elasticsearch: {}", productId, e);
      throw new RuntimeException("Failed to delete product in Elasticsearch", e);
    }
  }

  public List<ProductElasticsearchDTO> searchProducts(ProductCriteria criteria, boolean isAdmin) {
    List<Query> mustQueries = new ArrayList<>();
    List<Query> filterQueries = new ArrayList<>();

    log.info("Searching products with criteria: {}", criteria);
    if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
      mustQueries.add(
          Query.of(q -> q.match(m -> m.field("productName").query(criteria.getSearch()))));
      log.info("Added search query for product name: {}", criteria.getSearch());
    }

    if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
      log.info(
          "Adding price range filter: minPrice={}, maxPrice={}",
          criteria.getMinPrice(),
          criteria.getMaxPrice());
      RangeQuery.Builder rangeBuilder = new RangeQuery.Builder().field("variants.resalePrice");
      if (criteria.getMinPrice() != null) {
        rangeBuilder.gte(JsonData.of(criteria.getMinPrice()));
      }
      if (criteria.getMaxPrice() != null) {
        rangeBuilder.lte(JsonData.of(criteria.getMaxPrice()));
      }

      filterQueries.add(
          Query.of(
              q -> q.nested(n -> n.path("variants").query(nq -> nq.range(rangeBuilder.build())))));
    }

    if (criteria.getBrandId() != null) {
      log.info("Adding filter for brandId: {}", criteria.getBrandId());
      filterQueries.add(
          Query.of(q -> q.term(t -> t.field("brandId").value(criteria.getBrandId()))));
    }

    if (criteria.getShopId() != null) {
      log.info("Adding filter for shopId: {}", criteria.getShopId());
      filterQueries.add(Query.of(q -> q.term(t -> t.field("shopId").value(criteria.getShopId()))));
    }

    if (criteria.getSubCategory() != null) {
      log.info("Adding filter for subCategoryId: {}", criteria.getSubCategory());
      filterQueries.add(
          Query.of(q -> q.term(t -> t.field("subCategoryId").value(criteria.getSubCategory()))));
    }
    if (isAdmin) {
      log.info("Admin search, adding additional filters");
      if (criteria.getApprovalStatus() != null) {
        filterQueries.add(
            Query.of(
                q ->
                    q.term(
                        t ->
                            t.field("productApprovalStatus.keyword")
                                .value(criteria.getApprovalStatus().name()))));
      }
    } else {
      filterQueries.add(Query.of(q -> q.term(t -> t.field("isActive").value(true))));
      filterQueries.add(
          Query.of(q -> q.term(t -> t.field("productApprovalStatus.keyword").value("APPROVED"))));
    }
    Query finalQuery = Query.of(q -> q.bool(b -> b.must(mustQueries).filter(filterQueries)));

    int page =
        (criteria.getCurrentPage() != null && criteria.getCurrentPage() > 0)
            ? criteria.getCurrentPage()
            : 1;
    int size =
        (criteria.getPageSize() != null && criteria.getPageSize() > 0)
            ? criteria.getPageSize()
            : 20;
    int from = (page - 1) * size;
    log.info("Executing search with pagination: page={}, size={}, from={}", page, size, from);

    SearchResponse<ProductElasticsearchDTO> response;
    try {
      response =
          elasticsearchClient.search(
              s ->
                  s.index(INDEX_NAME)
                      .query(finalQuery)
                      .from(from)
                      .size(size)
                      .sort(sort -> sort.field(f -> f.field("createdAt").order(SortOrder.Desc))),
              ProductElasticsearchDTO.class);
    } catch (Exception e) {
      log.error("Elasticsearch search error", e);
      return Collections.emptyList();
    }
    log.info("Search completed. Total hits: {}", response.hits().total().value());

    return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
  }
}

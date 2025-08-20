package com.raved.content.service.impl;

import com.raved.content.model.elasticsearch.PostSearchDocument;
import com.raved.content.repository.elasticsearch.PostSearchRepository;
import com.raved.content.service.AdvancedSearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Advanced search service implementation using Elasticsearch
 */
@Service
public class AdvancedSearchServiceImpl implements AdvancedSearchService {

    private static final Logger logger = LoggerFactory.getLogger(AdvancedSearchServiceImpl.class);

    @Autowired
    private PostSearchRepository postSearchRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<PostSearchDocument> searchWithFilters(String query, List<String> tags, 
                                                      String category, String status, 
                                                      LocalDateTime fromDate, LocalDateTime toDate,
                                                      int page, int size) {
        logger.info("Performing advanced search with query: {}, tags: {}, category: {}", query, tags, category);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // Add text search if query is provided
        if (query != null && !query.trim().isEmpty()) {
            boolQuery.must(QueryBuilders.multiMatchQuery(query)
                    .field("title", 3.0f)
                    .field("content", 2.0f)
                    .field("tags", 1.0f)
                    .fuzziness("AUTO"));
        }

        // Add tag filters
        if (tags != null && !tags.isEmpty()) {
            boolQuery.filter(QueryBuilders.termsQuery("tags", tags));
        }

        // Add category filter
        if (category != null && !category.trim().isEmpty()) {
            boolQuery.filter(QueryBuilders.termQuery("category", category));
        }

        // Add status filter
        if (status != null && !status.trim().isEmpty()) {
            boolQuery.filter(QueryBuilders.termQuery("status", status));
        }

        // Add date range filter
        if (fromDate != null || toDate != null) {
            var rangeQuery = QueryBuilders.rangeQuery("createdAt");
            if (fromDate != null) {
                rangeQuery.gte(fromDate);
            }
            if (toDate != null) {
                rangeQuery.lte(toDate);
            }
            boolQuery.filter(rangeQuery);
        }

        // Build the search query
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
                .build();

        SearchHits<PostSearchDocument> searchHits = elasticsearchOperations.search(searchQuery, PostSearchDocument.class);
        
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostSearchDocument> searchSimilarPosts(String postId, int limit) {
        logger.info("Finding similar posts for post ID: {}", postId);

        // First, get the original post to extract its characteristics
        var originalPost = postSearchRepository.findById(postId);
        if (originalPost.isEmpty()) {
            logger.warn("Post not found for similarity search: {}", postId);
            return List.of();
        }

        PostSearchDocument post = originalPost.get();
        
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        // Exclude the original post
        boolQuery.mustNot(QueryBuilders.termQuery("_id", postId));
        
        // Add similarity based on tags
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            boolQuery.should(QueryBuilders.termsQuery("tags", post.getTags()).boost(2.0f));
        }
        
        // Add similarity based on category
        if (post.getCategory() != null) {
            boolQuery.should(QueryBuilders.termQuery("category", post.getCategory()).boost(1.5f));
        }
        
        // Add similarity based on author
        if (post.getAuthorId() != null) {
            boolQuery.should(QueryBuilders.termQuery("authorId", post.getAuthorId()).boost(1.0f));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "_score")))
                .build();

        SearchHits<PostSearchDocument> searchHits = elasticsearchOperations.search(searchQuery, PostSearchDocument.class);
        
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostSearchDocument> searchTrendingPosts(int days, int limit) {
        logger.info("Finding trending posts from last {} days", days);

        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
        
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        // Filter by date range
        boolQuery.filter(QueryBuilders.rangeQuery("createdAt").gte(fromDate));
        
        // Filter by status (only published posts)
        boolQuery.filter(QueryBuilders.termQuery("status", "PUBLISHED"));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "likeCount", "commentCount", "createdAt")))
                .build();

        SearchHits<PostSearchDocument> searchHits = elasticsearchOperations.search(searchQuery, PostSearchDocument.class);
        
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPopularTags(int limit) {
        logger.info("Getting popular tags with limit: {}", limit);

        // This would typically use aggregations, but for simplicity, we'll use a basic approach
        // In a real implementation, you'd use Elasticsearch aggregations
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withPageable(PageRequest.of(0, 1000)) // Get a large sample
                .build();

        SearchHits<PostSearchDocument> searchHits = elasticsearchOperations.search(searchQuery, PostSearchDocument.class);
        
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .filter(post -> post.getTags() != null)
                .flatMap(post -> post.getTags().stream())
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public List<PostSearchDocument> searchByAuthor(String authorId, int page, int size) {
        logger.info("Searching posts by author: {}", authorId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostSearchDocument> posts = postSearchRepository.findAll(pageable);
        
        return posts.getContent().stream()
                .filter(post -> authorId.equals(post.getAuthorId()))
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalPostCount() {
        return postSearchRepository.count();
    }

    @Override
    public long getPostCountByStatus(String status) {
        return postSearchRepository.findByStatus(status).size();
    }
}

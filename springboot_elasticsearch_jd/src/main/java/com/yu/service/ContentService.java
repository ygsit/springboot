package com.yu.service;

import com.alibaba.fastjson.JSON;
import com.yu.entity.Content;
import com.yu.utils.CommonConstant;
import com.yu.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ContentService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    //爬取京东数据到es
    public boolean parseData(String keyword) throws IOException {
        //爬取京东数据
        List<Content> contents = HtmlParseUtil.parseJD(keyword);
        //将数据批量存到es中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1m");
        for (Content content : contents) {
            IndexRequest indexRequest = new IndexRequest(CommonConstant.INDEX);
            indexRequest.id(UUID.randomUUID().toString());
            indexRequest.source(JSON.toJSONString(content), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !response.hasFailures();
    }

    //搜索数据
    public List<Map<String, Object>> searchData(String keyword, Integer page, Integer size) throws IOException {
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = 10;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        //查询数据
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.from((page - 1) * size); //分页
        searchSourceBuilder.size(size);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.requireFieldMatch(false); //如果要多个字段高亮,这项要为false
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits()) {
            //处理高亮
            //获取高亮的值
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            //原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //将原来的字段替换为高亮的字段
            if(title != null){
                Text[] fragments = title.fragments();
                String n_title = "";
                for (Text fragment : fragments) {
                    n_title += fragment;
                }
                sourceAsMap.put("title", n_title); //替换
            }

            list.add(hit.getSourceAsMap());
        }
        return list;
    }
}

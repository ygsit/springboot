package com.yu;

import com.alibaba.fastjson.JSON;
import com.yu.entity.User;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * es文档操作
 */
@SpringBootTest
public class DocumentTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //创建文档 PUT java_indices/_doc/1
    @Test
    void createDocument() throws IOException {
        //创建请求并指定索引
        IndexRequest request = new IndexRequest("java_indices");
        request.id("1"); // 设置ID
        request.timeout("1s"); // 设置超时时间
        request.source(JSON.toJSONString(new User("张三", 20)), XContentType.JSON); //设置数据
        //客户端发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    //根据ID获取指定索引的文档信息，判断是否存在
    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("java_indices", "1");
        //request.id("1");
        //判断指定的索引和id是否存在
        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
        //获取指定的id信息
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        //System.out.println(response.isExists());
    }

    //更新文档(Update)，重新插入同一个id的数据也是更新(PUT操作)
    @Test
    void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("java_indices", "1");
        request.timeout("1s");
        //封装需要更新的文档信息
        request.doc(JSON.toJSONString(new User("小张", 10)), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    //删除文档
    @Test
    void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("java_indices", "1");
        request.timeout("1s");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    //批量插入文档
    @Test
    void batchCreateDocument() throws IOException {
        //创建批量的请求
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("张三" + i, 20 + i);
            list.add(user);
        }
        //遍历list，把多条数据放到BulkRequest中
        for (int i = 0; i < list.size(); i++) {
            IndexRequest indexRequest = new IndexRequest("java_indices");
            indexRequest.id("" + i);
            indexRequest.source(JSON.toJSONString(list.get(i)), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        //客户端执行批量操作
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(responses.hasFailures()); //是否失败，如果false则表示全部成功
    }

    //文档复杂查询
    @Test
    void searchDocument() throws IOException {
        //创建批量搜索请求
        SearchRequest request = new SearchRequest("java_indices");

        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //查询条件，可以通过 QueryBuilders 工具来实现
        //当中文查询时，如果使用ik分词器会查询不到数据，属性需要使用xxx.keyword才能查询到数据
        //TermQueryBuilder termQuery = QueryBuilders.termQuery("name.keyword", "张三1");//精确查询
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("name", "张三"); //模糊查询
        //BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name", "张三")); //多条件查询

        searchSourceBuilder.query(matchQuery); //查询
        searchSourceBuilder.sort("age", SortOrder.ASC); //排序
        searchSourceBuilder.from(0); //分页起始
        searchSourceBuilder.size(5); //分页一页显示的数量
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(1)); //设置超时时间

        //创建高亮生成器
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<p style='color:red'>");
        highlightBuilder.postTags("</p>");
        highlightBuilder.requireFieldMatch(false);//如果要多个字段高亮,这项要为false
        //设置高亮
        searchSourceBuilder.highlighter(highlightBuilder);

        //将查询条件放入搜索请求request中
        request.source(searchSourceBuilder);

        //客户端执行请求
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response.getHits()));
        //遍历Hits
        for (SearchHit hit : response.getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

}

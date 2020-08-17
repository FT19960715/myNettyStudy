package com.service.client;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HighLevelRestClient {
//    public static String HOST_1 = "10.1.24.223";
    public static String HOST_1 = "127.0.0.1";
    public static int PORT = 9200;
    public static String SCHEME = "HTTP";
    public static RestHighLevelClient client = null;
    static {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST_1,PORT,SCHEME)));

    }

    public void queryDocument() throws IOException {
        GetRequest getRequest = new GetRequest("tb_medical_dictionary","21");
        getRequest.fetchSourceContext(FetchSourceContext.FETCH_SOURCE);
        String[] excludes = Strings.EMPTY_ARRAY;
        String[] includes = new String[]{"name","comment"};
        FetchSourceContext fetchSourceContext = new FetchSourceContext(
                true,
                includes,
                excludes
        );
        getRequest.realtime(false);//实时
        getRequest.refresh(true);
//        System.out.println(getRequest.version());
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse);

    }

    public void queryPageTest() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("tb_medical_dictionary");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request,RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        for (SearchHit hit :
                hits) {
            System.out.println(hit.getSourceAsMap());
        }
    }
    public void queryKeyWordsTest() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder b = QueryBuilders.wildcardQuery("name","*h*");
        boolQueryBuilder.must(b);
        searchSourceBuilder.query(boolQueryBuilder);

        SearchRequest request = new SearchRequest();
        request.indices("tb_medical_dictionary");
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request,RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        for (SearchHit hit :
                hits) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    public void addtest()throws  Exception{
        PutStoredScriptRequest request = new PutStoredScriptRequest();
        client.putScript(request,RequestOptions.DEFAULT);
    }

    public void test2() throws IOException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("172.17.3.122"), 9200));
        //将数据转换成文档的格式（后期可以使用java对象，将数据转换成json对象就可以了）
        XContentBuilder doContentBuilder= XContentFactory.jsonBuilder()
                .startObject()
                .field("id", "1") //字段名 ： 值
                .field("title", "java设计模式之装饰模式")
                .field("content", "在不必改变原类文件和使用继承的情况下，动态地扩展一个对象的功能")
                .field("postdate", "2018-05-20")
                .field("url", "https://www.cnblogs.com/chenyuanbo/")
                .endObject();
        //添加文档  index1:索引名 blog:类型 10:id
//.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) 代表插入成功后立即刷新，因为ES中插入数据默认分片要1秒钟后再刷新


        IndexResponse response = client.prepareIndex("index1","blog","10")
                .setSource(doContentBuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();
        System.out.println(response.status());
        //打印出CREATED 表示添加成功
    }

    // 添加数据
    public void addData (){
        IndexRequest request = new IndexRequest("user");
        Map map = new HashMap();
        map.put("name","张三");
        map.put("age",8);
        map.put("sex","男");
        map.put("phone",1234567);
        request.source(JSON.toJSONString(map), XContentType.JSON);
        try {
            IndexResponse response = client.index(request,RequestOptions.DEFAULT);
            System.out.println("status");
            System.out.println(response.status());
        }catch (Exception e){e.printStackTrace();}
    }

    // 查询数据
    public void queryData(){
        SearchRequest request = new SearchRequest();
        request.indices("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        try {
            SearchResponse response = client.search(request,RequestOptions.DEFAULT);
            System.out.println("status");
            System.out.println(response.status());
            SearchHits hits = response.getHits();
            Iterator it = hits.iterator();
            while (it.hasNext()){
                System.out.println(it.next().toString());
            }
//            client.bulk()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HighLevelRestClient client = new HighLevelRestClient();
//        client.queryKeyWordsTest();
//        client.queryPageTest();
//        client.queryDocument();
//        client.addData();
        client.queryData();
    }
}

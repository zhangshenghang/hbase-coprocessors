# 类说明

#####  协处理器类

- hbase协助处理保存es Demo

```
/es-coprocessor/src/main/java/wiki/hadoop/coprocessor/HbaseDataSyncEsObserver.java
```

- wechat用户保存ES

```
/es-coprocessor/src/main/java/wiki/hadoop/coprocessor/WechatUserSyncEsObserver.java
```

  ##### es工具类

```
/es-coprocessor/src/main/java/wiki/hadoop/es/ElasticSearchBulkOperator.java
/es-coprocessor/src/main/java/wiki/hadoop/es/ESClient.java
```









# 索引表

#### 索引表 ：crawl:self_media_account_index_prd

- ES 创建索引

```json
PUT self_media_account
{
  "settings": {
    "number_of_replicas": 1
    , "number_of_shards": 5
  }
}


PUT self_media_account/_mapping/self_media_account_type
{
  
  "self_media_account_type":{
    "properties":{
      "name":{"type":"text"},
      "city":{"type":"text"},
      "province":{"type":"text"},
      "followers_count":{"type":"long"},
	  "friends_count":{"type":"long"},
	  "statuses_count":{"type":"long"}
    }
  }
}
```



####  索引表 ：crawl:wechat_account_index_prd

```json
PUT wechat_account
{
  "settings": {
    "number_of_replicas": 1
    , "number_of_shards": 5
  }
}


PUT wechat_account/_mapping/wechat_account_type
{
  
  "wechat_account_type":{
    "properties":{
      "name":{"type":"text"},
      "city":{"type":"text"},
      "province":{"type":"text"},
      "followers_count":{"type":"long"},
	  "friends_count":{"type":"long"},
	  "statuses_count":{"type":"long"}
    }
  }
}
```



#### 索引表 ：crawl:weibo_user_index_prd

```json
PUT weibo_user
{
  "settings": {
    "number_of_replicas": 1
    , "number_of_shards": 5
  }
}


PUT weibo_user/_mapping/weibo_user_type
{
  
  "weibo_user_type":{
    "properties":{
      "name":{"type":"text"},
      "city":{"type":"text"},
      "province":{"type":"text"},
      "followers_count":{"type":"long"},
	  "friends_count":{"type":"long"},
	  "statuses_count":{"type":"long"}
    }
  }
}
```



#### 索引表 ：infoManager:local_user_warehouse

```json
PUT local_user_warehouse
{
  "settings": {
    "number_of_replicas": 1
    , "number_of_shards": 5
  }
}


PUT local_user_warehouse/_mapping/local_user_warehouse_type
{
  
  "local_user_warehouse_type":{
    "properties":{
      "name":{"type":"text"},
      "city":{"type":"text"},
      "province":{"type":"text"},
      "followers_count":{"type":"long"},
	  "friends_count":{"type":"long"},
	  "statuses_count":{"type":"long"}
    }
  }
}
```







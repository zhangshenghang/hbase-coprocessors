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
PUT tt_user
{
  "settings": {
    "number_of_replicas": 1
    , "number_of_shards": 5
  }
}


PUT tt_user/_mapping/tt_user
{
  
  "tt_user":{
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

#### 安装协处理器
```
disable 'zh_ams_ns:tt_user'
alter 'zh_ams_ns:tt_user' , METHOD =>'table_att','coprocessor'=>'/es-coprocessor-0.0.7-jar-with-dependencies.jar|wiki.hadoop.coprocessor.SelfMediaAccountSyncEsObserver|1001'
enable 'zh_ams_ns:tt_user'
desc 'zh_ams_ns:tt_user'


disable 'infoManager:local_user_warehouse'
alter 'infoManager:local_user_warehouse' , METHOD =>'table_att','coprocessor'=>'/es-coprocessor-0.0.7-jar-with-dependencies.jar|wiki.hadoop.coprocessor.LocalUserWarehouseSyncEsObserver|1001'
enable 'infoManager:local_user_warehouse'
desc 'infoManager:local_user_warehouse'


disable 'crawl:wechat_account'
alter 'crawl:wechat_account' , METHOD =>'table_att','coprocessor'=>'/es-coprocessor-0.0.7-jar-with-dependencies.jar|wiki.hadoop.coprocessor.WechatUserSyncEsObserver|1001'
enable 'crawl:wechat_account'
desc 'crawl:wechat_account'


disable 'crawl:weibo_user'
alter 'crawl:weibo_user' , METHOD =>'table_att','coprocessor'=>'/es-coprocessor-0.0.7-jar-with-dependencies.jar|wiki.hadoop.coprocessor.WeiboUserSyncEsObserver|1001'
enable 'crawl:weibo_user'
desc 'crawl:weibo_user'

```





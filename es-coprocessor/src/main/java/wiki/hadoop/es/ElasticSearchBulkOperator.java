package wiki.hadoop.es;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ElasticSearchBulkOperator {

    private static final Log LOG = LogFactory.getLog(ElasticSearchBulkOperator.class);

    private static final int MAX_BULK_COUNT = 10000;

    private static BulkRequestBuilder bulkRequestBuilder = null;

    private static final Lock commitLock = new ReentrantLock();

    private static ScheduledExecutorService scheduledExecutorService = null;
    static {
        // 初始化  bulkRequestBuilder
        bulkRequestBuilder = ESClient.client.prepareBulk();
        bulkRequestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        // 初始化线程池大小为1
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

       //创建一个Runnable对象，提交待写入的数据，并使用commitLock锁保证线程安全
        final Runnable beeper = () -> {
            commitLock.lock();
            try {
            	LOG.info("Before submission bulkRequest size : " +bulkRequestBuilder.numberOfActions());
            	//提交数据至es
                bulkRequest(0);
                LOG.info("After submission bulkRequest size : " +bulkRequestBuilder.numberOfActions());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                commitLock.unlock();
            }
        };

        //初始化延迟10s执行 runnable方法，后期每隔30s执行一次
        scheduledExecutorService.scheduleAtFixedRate(beeper, 10, 30, TimeUnit.SECONDS);

    }
    public static void shutdownScheduEx() {
        if (null != scheduledExecutorService && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }
    private static void bulkRequest(int threshold) {
        if (bulkRequestBuilder.numberOfActions() > threshold) {
            BulkResponse bulkItemResponse = bulkRequestBuilder.execute().actionGet();
            if (!bulkItemResponse.hasFailures()) {
                bulkRequestBuilder = ESClient.client.prepareBulk();
            }
        }
    }

    /**
     * add update builder to bulk
     * use commitLock to protected bulk as thread-save
     * @param builder
     */
    public static void addUpdateBuilderToBulk(UpdateRequestBuilder builder) {
        commitLock.lock();
        try {
            bulkRequestBuilder.add(builder);
            bulkRequest(MAX_BULK_COUNT);
        } catch (Exception ex) {
            LOG.error(" update Bulk " + "gejx_test" + " index error : " + ex.getMessage());
        } finally {
            commitLock.unlock();
        }
    }

    /**
     * add delete builder to bulk
     * use commitLock to protected bulk as thread-save
     *
     * @param builder
     */
    public static void addDeleteBuilderToBulk(DeleteRequestBuilder builder) {
        commitLock.lock();
        try {
            bulkRequestBuilder.add(builder);
            bulkRequest(MAX_BULK_COUNT);
        } catch (Exception ex) {
            LOG.error(" delete Bulk " + "gejx_test" + " index error : " + ex.getMessage());
        } finally {
            commitLock.unlock();
        }
    }
}
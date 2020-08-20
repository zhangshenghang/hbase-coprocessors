package wiki.hadoop.coprocessor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.wal.WALEdit;
import org.apache.log4j.Logger;

import wiki.hadoop.es.ESClient;
import wiki.hadoop.es.ElasticSearchBulkOperator;

import java.io.IOException;
import java.util.*;

/**
 * 本地用户库数据同步es,孙伟使用
 * @author jast
 * @date 2020年8月12日 下午3:15:23
 */
public class LocalUserWarehouseSyncEsObserver implements RegionObserver , RegionCoprocessor {

    private static final Logger LOG = Logger.getLogger(LocalUserWarehouseSyncEsObserver.class);

    private String index = "local_user_warehouse";
    private String type = index + "_type";
    public Optional<RegionObserver> getRegionObserver() {
        return Optional.of(this);
    }

    @Override
    public void start(CoprocessorEnvironment env) throws IOException {
        // init ES client
        ESClient.initEsClient();
        
        ElasticSearchBulkOperator.init();
        LOG.info("****init start*****");
    }

    @Override
    public void stop(CoprocessorEnvironment env) throws IOException {
        ESClient.closeEsClient();
        // shutdown time task
        ElasticSearchBulkOperator.shutdownScheduEx();
        LOG.info("****end*****");
    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        String indexId = new String(put.getRow());
        try {
            NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
            Map<String, Object> infoJson = new HashMap<>();
            Map<String, Object> json = new HashMap<>();
            for (Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()) {
                for (Cell cell : entry.getValue()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    json.put(key, value);
                }
            }
            // set hbase family to es
            infoJson.put("info", json);
//            LOG.info(json.toString());
            ElasticSearchBulkOperator.addUpdateBuilderToBulk(ESClient.client.prepareUpdate(index,type, indexId).setDocAsUpsert(true).setDoc(json));
            if(Bytes.toString(put.getRow()).contains("test")) {
				LOG.info(json.toString());
				LOG.info("**** postPut success*****");
			}
        } catch (Exception ex) {
            LOG.error("observer put  a doc, index [ " + "user_test" + " ]" + "indexId [" + indexId + "] error : " + ex.getMessage());
        }
    }
    @Override
    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit, Durability durability) throws IOException {
        String indexId = new String(delete.getRow());
        try {
            ElasticSearchBulkOperator.addDeleteBuilderToBulk(ESClient.client.prepareDelete(index,type, indexId));
            LOG.info("**** postDelete success*****");
        } catch (Exception ex) {
            LOG.error(ex);
            LOG.error("observer delete  a doc, index [ " + "user_test" + " ]" + "indexId [" + indexId + "] error : " + ex.getMessage());

        }
    }
}
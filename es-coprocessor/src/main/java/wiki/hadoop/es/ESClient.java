package wiki.hadoop.es;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ES Cleint class
 */
public class ESClient {

	public static Client client;
	
	private static final Log log = LogFactory.getLog(ESClient.class);
	/**
	 * init ES client
	 */
	public static void initEsClient() throws UnknownHostException {
		log.info("初始化es连接开始");
		
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		
		Settings esSettings = Settings.builder()
				.put("cluster.name", "log_cluster")//设置ES实例的名称
				.put("client.transport.sniff", true)
				.build();
		
		client = new  PreBuiltTransportClient(esSettings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("10.248.161.16"), 9300))
				.addTransportAddress(new TransportAddress(InetAddress.getByName("10.248.161.17"), 9300))
				.addTransportAddress(new TransportAddress(InetAddress.getByName("10.248.161.18"), 9300));
		
		log.info("初始化es连接完成");

	}

	/**
	 * Close ES client
	 */
	public static void closeEsClient() {
		client.close();
		log.info("es连接关闭");
	}
}

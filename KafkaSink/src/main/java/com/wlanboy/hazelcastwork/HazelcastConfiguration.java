package com.wlanboy.hazelcastwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.wlanboy.hazelcastwork.config.HazelcastConfigurationStatics;

@Configuration
public class HazelcastConfiguration {

	private static final Logger log = LoggerFactory.getLogger(HazelcastConfiguration.class);

	@Value("${server.ip:127.0.0.1}")
	private String ip;

	@Value("${server.port:5703}")
	private int port;

	@Value("${groupname:hazelcastworker}")
	private String groupname;

	@Value("${publicaddress:127.0.0.1:5703}")
	private String publicaddress;

	@Bean
	@Primary
	public HazelcastInstance hazelcastInstance() {

		String jobInstanceID = HazelcastConfigurationStatics.WORK_INSTANCE + "-" + port;

		Config config = new Config();
		config.setInstanceName(jobInstanceID)
				.addMapConfig(new MapConfig().setName(HazelcastConfigurationStatics.WORK_MAP)
						.setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
						.setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(-1))
				.addMapConfig(new MapConfig().setName(HazelcastConfigurationStatics.WORK_RESULT_MAP)
						.setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
						.setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(-1))
				.addExecutorConfig(new ExecutorConfig().setName(HazelcastConfigurationStatics.WORK_SERVICE).setPoolSize(5).setQueueCapacity(50)
						.setStatisticsEnabled(true));

		config.getNetworkConfig().setPortAutoIncrement(true);
		config.getGroupConfig().setName(groupname);
		config.getNetworkConfig().setPublicAddress(publicaddress);

		log.info("Hazelcast InstanceID is: {}", jobInstanceID);

		return Hazelcast.getOrCreateHazelcastInstance(config);
	}

}
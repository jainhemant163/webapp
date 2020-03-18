package com.cloud.csye6225.assignment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NoOpStatsDClient;

/**
 * Metric collection client class with StatsDClient
 * 
 * @author hrjain
 *
 */
@Configuration
public class MetricsClientConfig {

	@Value("true")
	private boolean publishMessage;

	@Value("localhost")
	private String metricHost;

	@Value("8125")
	private int portNumber;

	@Value("csye6225")
	private String prefix;

	@Bean
	public StatsDClient metricClient() {
		if (publishMessage)
			return new NonBlockingStatsDClient(prefix, metricHost, portNumber);

		return new NoOpStatsDClient();
	}
}
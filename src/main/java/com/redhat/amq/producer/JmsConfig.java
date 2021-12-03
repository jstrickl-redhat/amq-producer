package com.redhat.amq.producer;


import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class JmsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JmsConfig.class);

    @Value("${broker.scheme}")
    private String brokerScheme;

    @Value("${broker.port}")
    private String brokerPort;

    @Value("${broker.host}")
    private String brokerHost;

    @Value("${broker.user}")
    private String brokerUser;

    @Value("${broker.password}")
    private String brokerPass;

    @Value("${broker.maxConnections}")
    private String maxConnections;

    @Value("${broker.tsPath}")
    private String trustStorePath;

    @Value("${broker.tsPassword}")
    private String trustStorePass;

    @Value("${broker.verifyHostName}")
    private String verifyHostName;

    @Bean
    public JmsTemplate jmsTemplate(CachingConnectionFactory connectionFactory) {

        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);

        jmsTemplate.afterPropertiesSet();

        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(CachingConnectionFactory connectionFactory) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("3-10");
        return factory;
    }

    @Bean
    public ActiveMQSslConnectionFactory cachingConnectionFactory(ActiveMQSslConnectionFactory connectionFactory) throws Exception {

        ActiveMQSslConnectionFactory factory = new ActiveMQSslConnectionFactory();
        factory.setBrokerURL(remoteUri());
        factory.setTrustStore(trustStorePath);
        factory.setKeyStorePassword(trustStorePass);
        factory.setUserName(brokerUser);
        factory.setPassword(brokerPass);

        return factory;
    }

    private String remoteUri() {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(brokerScheme)
                .host(brokerHost)
                .port(brokerPort)
                .queryParam("verifyHostName", verifyHostName)
                .build();

        LOG.debug(uriComponents.toString());

        return String.format("failover:(%s)", uriComponents.toString());
    }
}

package main;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat").withSockJS();
		registry.addEndpoint("/lobby").withSockJS();
		registry.addEndpoint("/login").withSockJS();
		registry.addEndpoint("/createRoom").withSockJS();
		registry.addEndpoint("/editRoom").withSockJS();
		registry.addEndpoint("/deleteRoom").withSockJS();
		registry.addEndpoint("/createUser").withSockJS();
		registry.addEndpoint("/deleteUser").withSockJS();
		registry.addEndpoint("/users").withSockJS();
	}

}
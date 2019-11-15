package com.knowably.notificationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * Indicates this as Configuration class
 */
@Configuration
/**Enables WebSocket message handling, backed by a message broker.*/

@EnableWebSocketMessageBroker

/**Implement WebsocketMessageBrokerConfigurer to overide methods**/
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	/**
	 * Register Stomp endpoints of the url to open the WebSocket connection.
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {

		/**Register the "/web-socket" endpoint, enabling the SockJS protocol.
		 SockJS is used (both client and server side) to allow alternative messaging options if WebSocket is not available.*/
		stompEndpointRegistry.addEndpoint("/socket")

				/**To allow server to receive requests from any origin*/
				.setAllowedOrigins("*")
				.withSockJS();
	}



	/**Configure the message broker**/
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		/** Enable a simple memory-based message broker to send messages to the client on destinations prefixed with "/app".
		 Simple message broker handles subscription requests from clients, stores
		 them in memory, and broadcasts messages to connected clients with
		 matching destinations.*/
		registry.enableSimpleBroker("/topic","/queue");
		registry.setApplicationDestinationPrefixes("/app");
	}
}

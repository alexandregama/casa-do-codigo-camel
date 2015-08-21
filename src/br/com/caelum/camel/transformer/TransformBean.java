package br.com.caelum.camel.transformer;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

public class TransformBean {

	public void transform(Exchange exchange) {
		Message message = exchange.getIn();
		
		String messageId = message.getMessageId();
		System.out.println("Message id: " + messageId);
		
		String mensagem = message.getBody(String.class);
		System.out.println(mensagem);
	}
}

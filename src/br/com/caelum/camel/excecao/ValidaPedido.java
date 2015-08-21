package br.com.caelum.camel.excecao;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

public class ValidaPedido {

	public void valida(Exchange exchange) {
		Message message = exchange.getIn();
		
		String body = message.getBody(String.class);
		
		System.out.println(body);
		
		throw new RuntimeException("Ocorreu um erro a transformar a mensagem");
	}
}

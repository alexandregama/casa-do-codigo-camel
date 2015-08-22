package br.com.caelum.camel.excecao;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ValidaPedidoComRedelivery {

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando a validação do XSD");
		
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				errorHandler(
						deadLetterChannel("file:falha").
							useOriginalMessage()
								.redeliveryDelay(5 * 1000)
									.maximumRedeliveries(2)
				);
				
				from("file:entrada").
					log(LoggingLevel.INFO, "Validando mensagem com exceção").
						bean(ValidaPedido.class, "valida").
				to("file:saida");
			}
		});
		
		context.start();
		Thread.sleep(3 * 1000);
		context.stop();
		
		System.out.println("Validação finalizada!");
	}
	
}

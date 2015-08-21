package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class TransformaBodyDaMensagem {

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando a transformação dos Arquivos..");
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("file:entrada-transform?delay=5s").
					log(LoggingLevel.INFO, "Transformando arquivos").
						to("file:saida-transform");
			}
		});
		
		context.start();
		Thread.sleep(3 * 1000);
		context.stop();
		
		System.out.println("Transformação finalizada!");
	}
	
}

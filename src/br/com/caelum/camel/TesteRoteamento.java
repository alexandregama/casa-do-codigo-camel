package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class TesteRoteamento {

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando o envio do arquivo");
		CamelContext context = new DefaultCamelContext();
		
		context.start();
		Thread.sleep(10* 1000);
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("file:entrada?delay=5s").to("file:saida");
			}
		});
		context.stop();
		System.out.println("Finalizando o envio do arquivo");
	}
	
}

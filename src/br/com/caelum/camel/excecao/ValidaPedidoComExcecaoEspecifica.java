package br.com.caelum.camel.excecao;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ValidaPedidoComExcecaoEspecifica {

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando a validacao dos arquivos");
		
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() {
				
				onException(RuntimeException.class).
					redeliveryDelay(2 * 1000).
						maximumRedeliveries(2).
							useOriginalMessage().
				to("file:falha");
				
				from("file:entrada?delay=5s").
					log(LoggingLevel.INFO, "Roteando arquivo..").
						transform(body(String.class).regexReplaceAll("nomeAutor", "autor")).
							bean(ValidaPedidoDandoExcecao.class, "valida").
				to("file:saida");
			}
		});
		
		context.start();
		Thread.sleep(3 * 1000);
		context.stop();
		
		System.out.println("Validação finalizada!");
	}
	
}

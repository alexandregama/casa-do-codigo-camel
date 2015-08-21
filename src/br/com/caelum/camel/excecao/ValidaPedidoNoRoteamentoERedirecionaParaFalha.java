package br.com.caelum.camel.excecao;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ValidaPedidoNoRoteamentoERedirecionaParaFalha {

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando a transformação e validação da mensagem");
		
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				errorHandler(
						deadLetterChannel("file:falha")
				);
				
				from("file:entrada-transform").
					log(LoggingLevel.INFO, "Transformando a mensagem e validando").
						transform(body(String.class).regexReplaceAll("nomeAutor", "autor")).
							bean(ValidaPedido.class, "valida").
								to("file:saida-transform");
			}
		});
		
		context.start();
		Thread.sleep(3 * 1000);
		context.stop();
		
		System.out.println("Transformação finalizada!");
	}
	
}

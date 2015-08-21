package br.com.caelum.camel.polling;

import java.util.List;
import java.util.Scanner;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import br.com.caelum.livraria.modelo.Livro;

public class PollingBancoLivrosMaisVendidos {

	@SuppressWarnings({"unchecked", "resource"})
	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando o polling de livros mais vendidos para enviar para o Banco de Dados");
		
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("http://localhost:8088/casa-do-codigo/loja/livros/mais-vendidos").
					delay(10 * 1000).
						log(LoggingLevel.INFO, "Retornando livros mais vendidos..").
							unmarshal().
								json().
									split(body()).
				process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
						Message message = exchange.getIn();
						
						List<Livro> livro = (List<Livro>) message.getBody();
						
						System.out.println(livro);
					}
				});
			}
		});
		
		context.start();
		new Scanner(System.in).next();
		context.stop();
		
		System.out.println("Polling finalizado!");
	}
	
}

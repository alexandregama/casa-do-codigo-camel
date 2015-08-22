package br.com.caelum.camel.polling;

import java.util.ArrayList;
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

public class PollingLivrosMaisBaratos {

	@SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando o polling dos livros mais baratos");
		
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("http://localhost:8088/casa-do-codigo/loja/livros/mais-baratos").
					log(LoggingLevel.INFO, "Fazendo o polling dos livros mais baratos").
						delay(10 * 1000).
				unmarshal().
					json().
				process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
						Message message = exchange.getIn();
						
						List<?> livrosMaisBaratos = (List<?>) message.getBody();
						
						ArrayList<Livro> livros = (ArrayList<Livro>) livrosMaisBaratos.get(0);
						
						message.setBody(livros);
						System.out.println("Livros mais baratos: " + livros);
					}
				}).
				to("mock:livros");
			}
		});
		
		context.start();
		new Scanner(System.in).next();
		context.stop();
		
		System.out.println("Polling finalizado!");
	}
	
}

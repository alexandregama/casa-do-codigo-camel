package br.com.caelum.camel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import br.com.caelum.livraria.modelo.Livro;

public class TestePolling {

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				System.out.println("Iniciando polling");
				from("http://localhost:8088/casa-do-codigo/loja/livros/mais-vendidos")
					.delay(1000)
						.unmarshal()
							.json()
					.process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							System.out.println("Iniciando processo..");
							List<?> listaDeLivros = (List<?>) exchange.getIn().getBody();
							
							ArrayList<Livro> livros = (ArrayList<Livro>) listaDeLivros.get(0);
							
							System.out.println("Livros: " + livros);
							Message message = exchange.getIn();
							message.setBody(livros);
						}
					})
						.to("mock:livros");		
				System.out.println("Finalizando polling");
			}
		});
		
		context.start();
		
		new Scanner(System.in).next();
		
		context.stop();
		System.out.println("Finalizando teste");
	}
	
}

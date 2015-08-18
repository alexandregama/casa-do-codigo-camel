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
import org.apache.camel.util.jndi.JndiContext;

import br.com.caelum.livraria.modelo.Livro;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class TestePollingInserindoNoBanco {

	public static void main(String[] args) throws Exception {
		MysqlConnectionPoolDataSource mysqlDs = new MysqlConnectionPoolDataSource();
		mysqlDs.setDatabaseName("camel");
		mysqlDs.setServerName("localhost");
		mysqlDs.setPort(3306);
		mysqlDs.setUser("root");
		mysqlDs.setPassword("");
		
		JndiContext jndi = new JndiContext();
		jndi.rebind("mysqlDataSource", mysqlDs);
		
		CamelContext context = new DefaultCamelContext(jndi);
		
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
						.to("direct:livros");
				
				from("direct:livros")
					.split(body())
						.process(new Processor() {
							
							@Override
							public void process(Exchange exchange) throws Exception {
								Message message = exchange.getIn();
								
								Livro livro = (Livro) message.getBody();
								
								String nomeAutor = livro.getNomeAutor();
								System.out.println("Autor: " + nomeAutor);
								message.setHeader("nomeAutor", nomeAutor);
							}
						})
						.setBody(simple("insert into Livros (nomeAutor) values ('${header[nomeAutor]}')"))
						.to("jdbc:mysqlDataSource");
				
				System.out.println("Finalizando polling");
			}
		});
		
		context.start();
		
		new Scanner(System.in).next();
		
		context.stop();
		System.out.println("Finalizando teste");
	}
	
}

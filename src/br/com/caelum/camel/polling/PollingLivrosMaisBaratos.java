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
import org.apache.camel.util.jndi.JndiContext;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import br.com.caelum.livraria.modelo.Livro;

public class PollingLivrosMaisBaratos {

	@SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando o polling dos livros mais baratos");
		
		MysqlConnectionPoolDataSource mysqlDs = new MysqlConnectionPoolDataSource();
		mysqlDs.setServerName("localhost");
		mysqlDs.setPort(3306);
		mysqlDs.setDatabaseName("camel");
		mysqlDs.setUser("root");
		mysqlDs.setPassword("");
		
		JndiContext jndi = new JndiContext();
		jndi.rebind("mysqlDataSource", mysqlDs);
		
		CamelContext context = new DefaultCamelContext(jndi);
		
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
				to("direct:livros");
				
				from("direct:livros").
					log(LoggingLevel.INFO, "Enviando para o banco de dados").
						split(body()).
				process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
						Message message = exchange.getIn();
						
						Livro livro = (Livro) message.getBody();
						
						message.setHeader("autor", livro.getNomeAutor());
					}
				}).
				setBody(simple("insert into Livros (nomeAutor) values ('${header[autor]}')")).
				to("jdbc:mysqlDataSource");
			}
		});
		
		context.start();
		new Scanner(System.in).next();
		context.stop();
		
		System.out.println("Polling finalizado!");
	}
	
}

package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import br.com.caelum.camel.transformer.TransformBean;

public class TransformacaoMensagemViaBean {

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando a transformação da mensagem via Bean");
		
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("file:entrada-transform?delay=5s").
					log(LoggingLevel.INFO, "Transformando mensagem através do bean").
						transform(body(String.class).regexReplaceAll("nomeAutor", "autor")).
							bean(TransformBean.class, "transform").
								to("file:saida-transform");
			}
		});
		
		context.start();
		Thread.sleep(3 * 1000);
		context.stop();
		
		System.out.println("Transformação finalizada!");
	}
	
}

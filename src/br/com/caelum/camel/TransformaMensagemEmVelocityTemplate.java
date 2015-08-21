package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import br.com.caelum.camel.transformer.TransformVelocity;

public class TransformaMensagemEmVelocityTemplate {

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("file:entrada-transform?delay=5s").
					log(LoggingLevel.INFO, "Transformando mensagem em velocity template").
						transform(body(String.class).regexReplaceAll("nomeAutor", "autor")).
							bean(TransformVelocity.class, "transform").
								to("velocity:br/com/caelum/camel/transformer/NotaFiscal.vm").
									to("file:saida-transform");
			}
		});
		
		context.start();
		Thread.sleep(3 * 1000);
		context.stop();
	}
	
}

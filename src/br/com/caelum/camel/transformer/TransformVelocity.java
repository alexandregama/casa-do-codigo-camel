package br.com.caelum.camel.transformer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

public class TransformVelocity {

	public void transform(Exchange exchange) {
		Message message = exchange.getIn();
		message.setHeader("dataAtual", new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
	}
	
}

package br.com.caelum.camel.excecao;

import org.apache.camel.Exchange;

public class ValidaPedidoDandoExcecao {

	public void valida(Exchange exchange) {
		throw new RuntimeException("Deu ruim!");
	}
	
}

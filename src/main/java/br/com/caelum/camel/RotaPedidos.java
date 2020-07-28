package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("file:pedidos?delay=5s&noop=true")
						.split()
							.xpath("/pedido/itens/item")
						.marshal().xmljson()
						.filter().xpath("/item/formato[text()='EBOOK']")
						.log("${id}")
						.log("${body}")
						.setHeader("CamelFileName", simple("${file:name.noext}.xml"))
				.to("file:saida");
			}
		});

		context.start();
		context.stop();
	}	
}

package com.bolsadeideas.springboot.app.view.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {

	@Override//se eliminan los datos del modelo innecesarios que se pusieron en el controlador 
	protected Object filterModel(Map<String, Object> model) {
		model.remove("titulo");
		model.remove("page");
		@SuppressWarnings("unchecked")//se obtiene la lista de clientes
		Page <Cliente> clientes = (Page<Cliente>) model.get("clientes");
		model.remove("clientes"); //se quita todo lo que no es necesario
		model.put("clientes",clientes.getContent());
		return super.filterModel(model);
	}
	

}

package com.bolsadeideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@RestController
@RequestMapping("/api")
public class ClienteRestController {
	@Autowired
	IClienteService clienteService;
	 
	@RequestMapping(value = "/listar-rest", method = RequestMethod.GET)
	public ClienteList listarRest(){
		
		
		
		return new ClienteList(clienteService.findAll());
	}

}

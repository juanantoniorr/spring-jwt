package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>  { //El long es por el tipo de dato
	//Busca en automatico el username solo hay que seguir nomenclatura findBy
	public Usuario findByUsername(String username);
	

}

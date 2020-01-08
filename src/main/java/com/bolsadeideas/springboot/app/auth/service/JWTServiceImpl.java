package com.bolsadeideas.springboot.app.auth.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.bolsadeideas.springboot.app.auth.SimpleGrantedAuthoritiesMixin;
import com.bolsadeideas.springboot.app.auth.filter.JWTAuthenticationFilter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JWTServiceImpl implements JWTService {

	@Override
	public String create(Authentication auth) throws JsonProcessingException {
		String username = ((User)auth.getPrincipal()).getUsername();
		//Obtenemos roles
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		
		Claims claims = Jwts.claims();
		///le pasamos los roles en formato string json
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		
		
	/////Se crea el token con todas sus propiedades
		String token = Jwts.builder()
				.setClaims(claims)
	            .setIssuedAt(new Date())
	            .setSubject(username)
	            .signWith(JWTAuthenticationFilter.SECRET_KEY)
	            .setExpiration(new Date(System.currentTimeMillis() + 3600000*4))
	            .compact();
		
		return token;
	}

	@Override
	public boolean validate(String token) {
		boolean validoToken;
		try {
			getClaims(token);
		validoToken= true;
		} catch (JwtException | IllegalArgumentException e){
			e.getStackTrace();
			validoToken= false;
			
		}
		
		return validoToken;
	}

	@Override
	public Claims getClaims(String token) {
		//Parsea la llave secreta que se creo en la primera clase filtro 
				Claims claims =Jwts.parser().setSigningKey(JWTAuthenticationFilter.SECRET_KEY)
				///le pasamos el token del cliente que contiene la palabra bearer entonces se lo quitamos 
				.parseClaimsJws(resolve(token)).getBody();
		
		return claims;
	}

	@Override
	public String getUsername(String token) {
		
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");
		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthoritiesMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
		return authorities;
	}

	@Override
	public String resolve(String token) {
		if(token!= null && token.startsWith("Bearer ")) {
			return token.replace("Bearer ", "");
		}
		return null;
		
		
	}

}

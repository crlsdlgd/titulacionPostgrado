/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ConexionLdap.java      
 DESCRIPCION: Clase encargada de proveer los persmisos al usuario de acuerdo a su rol. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018 		  Daniel Albuja						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity;

/**
 * Clase ConexionLdap.
 * Clase  Clase encargada de proveer los persmisos al usuario de acuerdo a su rol. 
 * @author dalbuja
 * @version 1.0
 */
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;

public class DetalleUsuario implements UserDetails{
	private static final long serialVersionUID = -7604616693479737220L;
	private Usuario usuario;
	private List<ProveedorPermisos> permisos;
	
	public DetalleUsuario(Usuario usuario, List<ProveedorPermisos> permisos) {
		this.usuario = usuario;
		this.permisos = permisos;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return permisos;
	}

	@Override
	public String getPassword()	{
		return usuario.getUsrPassword();
	}

	@Override
	public String getUsername(){
		return usuario.getUsrNick();
	}

	@Override
	public boolean isAccountNonExpired(){
		return true;
	}

	@Override
	public boolean isAccountNonLocked(){
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired(){
		return true;
	}

	@Override
	public boolean isEnabled(){
		return true;
	}

	public Usuario getUsuario() {
		return usuario;
	}
}

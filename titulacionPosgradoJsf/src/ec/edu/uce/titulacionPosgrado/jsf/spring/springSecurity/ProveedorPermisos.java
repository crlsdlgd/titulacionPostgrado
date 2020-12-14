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
   
 ARCHIVO:     ProveedorPermisos.java      
 DESCRIPCION: Clase encargada de proveer el rol asignado al usuario. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 08-04-2017            	 Daniel Albuja						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity;

/**
 * Clase ProveedorPermisos.
 * Clase encargada de proveer el rol asignado al usuario. 
 * @author dalbuja
 * @version 1.0
 */
import org.springframework.security.core.GrantedAuthority;

public class ProveedorPermisos implements GrantedAuthority{

	private static final long serialVersionUID = -6835653812274402660L;
	
	private String rol;

	public ProveedorPermisos(String rol) {
		this.rol = rol;
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + rol;
	}
}
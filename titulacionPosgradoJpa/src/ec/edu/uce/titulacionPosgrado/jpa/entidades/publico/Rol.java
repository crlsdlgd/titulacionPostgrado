/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     Rol.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Rol de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) Rol.
 * Entity Bean que representa a la tabla Rol de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "rol", schema = "titulacion_posgrado")
public class Rol implements  Serializable {

	private static final long serialVersionUID = 2380784860490491954L;
	private int rolId;
	private String rolDescripcion;
	private Integer rolTipo;
	private List<UsuarioRol> rolUsuarioRoles;

	public Rol() {
	}

	public Rol(int rolId) {
		this.rolId = rolId;
	}


	public Rol(int rolId, String rolDescripcion, Integer rolTipo,
			List<UsuarioRol> rolUsuarioRoles) {
		this.rolId = rolId;
		this.rolDescripcion = rolDescripcion;
		this.rolTipo = rolTipo;
		this.rolUsuarioRoles = rolUsuarioRoles;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "rol_id", unique = true, nullable = false)
	public int getRolId() {
		return this.rolId;
	}

	public void setRolId(int rolId) {
		this.rolId = rolId;
	}

	@Column(name = "rol_descripcion", length = 20)
	@Length(max = 20)
	public String getRolDescripcion() {
		return this.rolDescripcion;
	}

	public void setRolDescripcion(String rolDescripcion) {
		this.rolDescripcion = rolDescripcion;
	}

	@Column(name = "rol_tipo")
	public Integer getRolTipo() {
		return this.rolTipo;
	}

	public void setRolTipo(Integer rolTipo) {
		this.rolTipo = rolTipo;
	}

	@OneToMany(mappedBy = "usroRol")
	public List<UsuarioRol> getRolUsuarioRoles() {
		return rolUsuarioRoles;
	}

	public void setRolUsuarioRoles(List<UsuarioRol> rolUsuarioRoles) {
		this.rolUsuarioRoles = rolUsuarioRoles;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("rolId: " + rolId);
    	sb.append(tabulador);
    	sb.append("rolDescripcion: " + (rolDescripcion==null? "NULL":rolDescripcion));  
    	sb.append(tabulador);
    	sb.append("rolTipo: " + (rolTipo==null? "NULL":rolTipo.intValue()));  
        return sb.toString();
	}
}

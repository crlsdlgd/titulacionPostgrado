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
   
 ARCHIVO:     UsuarioRol.java	  
 DESCRIPCION: Entity Bean que representa a la tabla UsuarioRol de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Clase (Entity Bean) UsuarioRol.
 * Entity Bean que representa a la tabla UsuarioRol de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "usuario_rol", schema = "titulacion_posgrado")
public class UsuarioRol implements  Serializable {

	private static final long serialVersionUID = -2021487870956170949L;
	private int usroId;
	private Integer usroEstado;
	private Rol usroRol;
	private Usuario usroUsuario;
	
//	private List<RolFlujoCarrera> usroRolFlujoCarreras;

	public UsuarioRol() {
	}

	public UsuarioRol(int usroId) {
		this.usroId = usroId;
	}

//	public UsuarioRol(int usroId, Integer usroEstado, Rol usroRol,
//			Usuario usroUsuario, List<RolFlujoCarrera> usroRolFlujoCarreras) {
//		this.usroId = usroId;
//		this.usroEstado = usroEstado;
//		this.usroRol = usroRol;
//		this.usroUsuario = usroUsuario;
//		this.usroRolFlujoCarreras = usroRolFlujoCarreras;
//	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "usro_id", unique = true, nullable = false)
	public int getUsroId() {
		return this.usroId;
	}

	public void setUsroId(int usroId) {
		this.usroId = usroId;
	}

	@JoinColumn(name = "rol_id", referencedColumnName = "rol_id")
	@ManyToOne
	public Rol getUsroRol() {
		return usroRol;
	}

	public void setUsroRol(Rol usroRol) {
		this.usroRol = usroRol;
	}

	@JoinColumn(name = "usr_id", referencedColumnName = "usr_id")
	@ManyToOne
	public Usuario getUsroUsuario() {
		return usroUsuario;
	}

	public void setUsroUsuario(Usuario usroUsuario) {
		this.usroUsuario = usroUsuario;
	}

	@Column(name = "usro_estado")
	public Integer getUsroEstado() {
		return this.usroEstado;
	}

	public void setUsroEstado(Integer usroEstado) {
		this.usroEstado = usroEstado;
	}

//	@OneToMany(mappedBy = "roflcrUsuarioRol")
//	public List<RolFlujoCarrera> getUsroRolFlujoCarreras() {
//		return usroRolFlujoCarreras;
//	}
//
//	public void setUsroRolFlujoCarreras(List<RolFlujoCarrera> usroRolFlujoCarreras) {
//		this.usroRolFlujoCarreras = usroRolFlujoCarreras;
//	}
	
	
	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("usroId: " + usroId);
    	sb.append(tabulador);
    	sb.append("usroEstado: " + (usroEstado==null? "NULL":usroEstado.intValue()));  
    	sb.append(tabulador);
    	sb.append("usroRol: " + (usroRol==null? "NULL":usroRol.getRolId()));  
    	sb.append(tabulador);
    	sb.append("usroUsuario: " + (usroUsuario==null? "NULL":usroUsuario.getUsrId()));  
        return sb.toString();
	}

}

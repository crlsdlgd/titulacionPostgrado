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
   
 ARCHIVO:     RolConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Rol 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		 	Daniel Albuja			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) RolConstantes.
 * Clase que maneja las constantes de la entidad Rol.
 * @author dalbuja.
 * @version 1.0
 */
public class RolConstantes {
	//constantes de la entidad roles en la base de datos
	public static final String ROL_BD_ADMINISTRADOR = "ADMINISTRADOR";
	public static final int ROL_BD_ADMINISTRADOR_VALUE = Integer.valueOf(1);
	public static final String ROL_BD_POSTULANTE = "POSTULANTE";
	public static final int ROL_BD_POSTULANTE_VALUE = Integer.valueOf(2);
	public static final String ROL_BD_VALIDADOR = "VALIDADOR";
	public static final int ROL_BD_VALIDADOR_VALUE = Integer.valueOf(3);
	public static final String ROL_BD_EVALUADOR = "EVALUADOR";
	public static final int ROL_BD_EVALUADOR_VALUE = Integer.valueOf(4);
	public static final String ROL_BD_CONSULTOR = "CONSULTOR";
	public static final int ROL_BD_CONSULTOR_VALUE = Integer.valueOf(5);
	public static final String ROL_BD_EDITOR_DGIP= "EDITORDGIP";
	public static final int ROL_BD_EDITOR_DGIP_VALUE = Integer.valueOf(6);
	public static final String ROL_BD_EDITOR_ACTA= "EDITOR_ACTA";
	public static final int ROL_BD_EDITOR_ACTA_VALUE = Integer.valueOf(7);
	public static final String ROL_BD_DIRECTOR_DGPP= "DIRECTOR_DGPP";
	public static final int ROL_BD_DIRECTOR_DGPP_VALUE = Integer.valueOf(8);
	
	
	//constantes de la entidad roles en el sistema
	public static final String ROL_ADMINISTRADOR = "ROLE_ADMINISTRADOR";
	public static final String ROL_POSTULANTE = "ROLE_POSTULANTE";
	public static final String ROL_VALIDADOR = "ROLE_VALIDADOR";
	public static final String ROL_EVALUADOR = "ROLE_EVALUADOR";
	public static final String ROL_CONSULTOR = "ROLE_CONSULTOR";
	public static final String ROL_EDITOR_DGIP= "ROLE_EDITORDGIP";
	public static final String ROL_EDITOR_ACTA= "ROLE_EDITOR_ACTA";
	public static final String ROL_DIRECTOR_DGPP= "ROLE_DIRECTOR_DGPP";

}

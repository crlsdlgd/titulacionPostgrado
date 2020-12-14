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
   
 ARCHIVO:     UsuarioConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Usuario 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) UsuarioConstantes.
 * Clase que maneja las constantes de la entidad Usuario.
 * @author dalbuja.
 * @version 1.0
 */
public class UsuarioConstantes {
	
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVA";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVA";
	public static Integer LARGO_CLAVE = Integer.valueOf(8);
	public static final String REGEX_CLAVE = "[A-Za-z0-9.]*";
	
	//constantes para el tipo de recuperación de contraseña	
	public static final int TIPO_USUARIO_VALUE = Integer.valueOf(0);
	public static final String TIPO_USUARIO_LABEL = "POR USUARIO";
	public static final int TIPO_CORREO_VALUE = Integer.valueOf(1);	
	public static final String TIPO_CORREO_LABEL = "POR CORREO ELECTRÓNICO";
	
	//constantes para el tipo de usuario	
	public static final int ACTIVE_DIRECTORY_SI_VALUE = Integer.valueOf(0);
	public static final String ACTIVE_DIRECTORY_SI_LABEL = "SI";
	public static final int ACTIVE_DIRECTORY_NO_VALUE = Integer.valueOf(1);	
	public static final String ACTIVE_DIRECTORY_NO_LABEL = "NO";
	
	//constantes para el usuario estado session
	public static final int ESTADO_SESSION_SI_LOGGEADO_VALUE = Integer.valueOf(0);
	public static final int ESTADO_SESSION_NO_LOGGEADO_VALUE = Integer.valueOf(1);	
}

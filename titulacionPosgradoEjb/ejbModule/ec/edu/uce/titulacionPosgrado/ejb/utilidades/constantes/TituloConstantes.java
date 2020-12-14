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
   
 ARCHIVO:     TituloConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Titulo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		    Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;


/**
 * Clase (constantes) TituloConstantes.
 * Clase que maneja las constantes de la entidad Titulo.
 * @author dcollaguazo.
 * @version 1.0
 */
public class TituloConstantes {
	
	//Constantes para tipo de identificacion UCE
	public static final int GENERO_MASCULINO_VALUE = Integer.valueOf(0);
	public static final String GENERO_MASCULINO_LABEL = "MASCULINO";
	public static final int GENERO_FEMENINO_VALUE = Integer.valueOf(1);
	public static final String GENERO_FEMENINO_LABEL = "FEMENINO";
	public static final int GENERO_ESTANDAR_VALUE = Integer.valueOf(2);
	public static final String GENERO_ESTANDAR_LABEL = "ESTANDAR";
	
	//constantes para el tipo de titulo
	public static final int TIPO_TITULO_UNIVERSIDAD_VALUE = Integer.valueOf(0);
	public static final String TIPO_TITULO_UNIVERSIDAD_LABEL = "TIPO TERCER NIVEL";
	public static final int TIPO_TITULO_COLEGIO_VALUE = Integer.valueOf(1);
	public static final String TIPO_TITULO_COLEGIO_LABEL = "TIPO BACHILLERATO";
	public static final int TIPO_TITULO_CUARTO_NIVEL_VALUE = Integer.valueOf(2);
	public static final String TIPO_TITULO_CUARTO_NIVEL_LABEL = "TIPO CUARTO NIVEL";
}

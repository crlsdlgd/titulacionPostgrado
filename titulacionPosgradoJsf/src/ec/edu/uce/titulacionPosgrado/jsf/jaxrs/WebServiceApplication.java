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
   
 ARCHIVO:     WebServiceApplication.java	  
 DESCRIPCION: Clase encargada de presentar el WebService para enviar los datos a EmisionTitulo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-JUN-2017		 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;



/**
 * Clase WebServiceApplication.
 * Clase encargada de presentar el WebService para enviar los datos a EmisionTitulo. 
 * @author dalbuja.
 * @version 1.0
 */
@ApplicationPath("/datos")
public class WebServiceApplication extends Application {
	
	
}

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
   
 ARCHIVO:     PersonaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Persona. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * Clase (constantes) UsuarioConstantes.
 * Clase que maneja las constantes de la entidad Persona.
 * @author dalbuja.
 * @version 1.0
 */
public class PersonaConstantes {
	
	//Constantes para tipo de identificacion UCE
	public static final int TIPO_IDENTIFICACION_CEDULA_VALUE = Integer.valueOf(0);
	public static final String TIPO_IDENTIFICACION_CEDULA_LABEL = "CÉDULA";
	public static final int TIPO_IDENTIFICACION_PASAPORTE_VALUE = Integer.valueOf(1);
	public static final String TIPO_IDENTIFICACION_PASAPORTE_LABEL = "PASAPORTE/OTROS";
	
	//Constantes para tipo de identificacion SNIESE
	public static final int TIPO_IDENTIFICACION_CEDULA_SNIESE_VALUE = Integer.valueOf(1);
	public static final String TIPO_IDENTIFICACION_CEDULA_SNIESE_LABEL = "CÉDULA";
	public static final int TIPO_IDENTIFICACION_PASAPORTE_SNIESE_VALUE = Integer.valueOf(2);
	public static final String TIPO_IDENTIFICACION_PASAPORTE_SNIESE_LABEL = "PASAPORTE/OTROS";
	
	//Constantes para tipo de identificacion UCE
	public static final int SEXO_HOMBRE_VALUE = Integer.valueOf(0);
	public static final String SEXO_HOMBRE_LABEL = "HOMBRE";
	public static final int SEXO_MUJER_VALUE = Integer.valueOf(1);
	public static final String SEXO_MUJER_LABEL = "MUJER";
	public static final int SEXO_GENERICO_VALUE = Integer.valueOf(2);
	public static final String SEXO_GENERICO_LABEL = "GENERICO";
	
	//Constantes para tipo de identificacion SNIESE
	public static final int SEXO_HOMBRE_SNIESE_VALUE = Integer.valueOf(1);
	public static final String SEXO_HOMBRE_SNIESE_LABEL = "HOMBRE";
	public static final int SEXO_MUJER_SNIESE_VALUE = Integer.valueOf(2);
	public static final String SEXO_MUJER_SNIESE_LABEL = "MUJER";	
	
	public static int traerTipoIdEsnieseXTipoIdUce(int tipoIdSniese) throws PersonaValidacionException{
		int retorno = -99;
		if(tipoIdSniese == TIPO_IDENTIFICACION_CEDULA_VALUE){
			retorno = TIPO_IDENTIFICACION_CEDULA_SNIESE_VALUE;
		}else if(tipoIdSniese == TIPO_IDENTIFICACION_PASAPORTE_VALUE){
			retorno = TIPO_IDENTIFICACION_PASAPORTE_SNIESE_VALUE;
		}else{
			throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaConstantes.traer.tipo.id.sniese.id.tipoUce.exception")));
		}
		
		return retorno;
	}
	
	public static int traerSexoEsnieseXSexoUce(int tipoSexoSniese) throws PersonaValidacionException{
		int retorno = -99;
		if(tipoSexoSniese == SEXO_HOMBRE_VALUE){
			retorno = SEXO_HOMBRE_SNIESE_VALUE;
		}else if(tipoSexoSniese == SEXO_MUJER_VALUE){
			retorno = SEXO_MUJER_SNIESE_VALUE;
		}else{
			throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaConstantes.traer.sexo.sniese.sexo.uce.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo de un tipo de identificacion UCE
	 * @param tipoIdentUce - valor de un tipo de identificacion UCE
	 * @return label del valor representativo de un tipo de identificacion UCE
	 */
	public static String valueToLabelTipoIdentificacionUce(int tipoIdentUce) {
		String retorno = "";
		if(tipoIdentUce == TIPO_IDENTIFICACION_CEDULA_VALUE){
			retorno = TIPO_IDENTIFICACION_CEDULA_LABEL;
		}else if(tipoIdentUce == TIPO_IDENTIFICACION_PASAPORTE_VALUE){
			retorno = TIPO_IDENTIFICACION_PASAPORTE_LABEL;
		}
		return retorno;
	}
	
	
	/**
	 * Retorna el label del valor representativo de un tipo de sexo UCE
	 * @param tipoSexoUce - valor de un tipo de sexo UCE
	 * @return label del valor representativo de un tipo de sexo UCE
	 */
	public static String valueToLabelTipoSexoUce(int tipoSexoUce) {
		String retorno = "";
		if(tipoSexoUce == SEXO_HOMBRE_SNIESE_VALUE){
			retorno = SEXO_HOMBRE_SNIESE_LABEL;
		}else if(tipoSexoUce == SEXO_MUJER_SNIESE_VALUE){
			retorno = SEXO_MUJER_SNIESE_LABEL;
		}
		return retorno;
	}
	
	
	
}

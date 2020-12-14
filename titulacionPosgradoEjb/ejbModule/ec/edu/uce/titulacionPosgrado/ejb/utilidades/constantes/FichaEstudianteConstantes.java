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
 DESCRIPCION: Clase que maneja las constantes de la entidad Persona 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
20/02/2018            Daniel Albuja                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * Clase (constantes) PersonaConstantes.
 * Clase que maneja las constantes de la entidad Persona.
 * @author dalbuja.
 * @version 1.0
 */
public class FichaEstudianteConstantes {
	
	//constantes que indican si tiene o no reconocimiento de estudios previos de la UCE
	public static final int FCES_ESTADO_MIGRADO_VALUE = Integer.valueOf(0);
	public static final String FCES_ESTADO_MIGRADO_LABEL = "SI";
	public static final int FCES_ESTADO_MIGRADO_DESACTIVADO_VALUE = Integer.valueOf(1);	
	public static final String FCES_ESTADO_MIGRADO_DESACTIVADO_LABEL = "DESACTIVADO";
	
	//constantes que indican si tiene o no reconocimiento de estudios previos de la UCE
	public static final int RECON_ESTUD_PREVIOS_SI_VALUE = Integer.valueOf(0);
	public static final String RECON_ESTUD_PREVIOS_SI_LABEL = "SI";
	public static final int RECON_ESTUD_PREVIOS_NO_VALUE = Integer.valueOf(1);	
	public static final String RECON_ESTUD_PREVIOS_NO_LABEL = "NO";
	
	//constantes que indican si tiene o no reconocimiento de estudios previos de la SNIESE
	public static final int RECON_ESTUD_PREVIOS_SNIESE_SI_VALUE = Integer.valueOf(1);
	public static final String RECON_ESTUD_PREVIOS_SNIESE_SI_LABEL = "SI";
	public static final int RECON_ESTUD_PREVIOS_SNIESE_NO_VALUE = Integer.valueOf(2);	
	public static final String RECON_ESTUD_PREVIOS_SNIESE_NO_LABEL = "NO";	
	
	//constantes que indican el tipo de duracion reconocimiento
	public static final int TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE = Integer.valueOf(0);
	public static final String TIPO_DURACION_RECONOCIMIENTO_ANIOS_LABEL = "AÑOS";
	public static final int TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE = Integer.valueOf(1);
	public static final String TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_LABEL = "SEMESTRES";
	public static final int TIPO_DURACION_RECONOCIMIENTO_CREDITOS_VALUE = Integer.valueOf(2);
	public static final String TIPO_DURACION_RECONOCIMIENTO_CREDITOS_LABEL = "CRÉDITOS";
	
	/**
	 * Retorna si tiene o no reconocimiento de estu¿dios previos de SNIES a partir del tipo de UEC
	 * @param recoPrevUce - recnocimiento de estudioas previos tipo UCE
	 * @return si tiene o no reconocimiento de estu¿dios previos de SNIES a partir del tipo de UEC
	 */
	public static int traerTipoReconocimientoEsnieseXUce(int recoPrevUce) throws FichaEstudianteValidacionException{
		int retorno = -99;
		if(recoPrevUce == RECON_ESTUD_PREVIOS_SI_VALUE){
			retorno = RECON_ESTUD_PREVIOS_SNIESE_SI_VALUE;
		}else if(recoPrevUce == RECON_ESTUD_PREVIOS_NO_VALUE){
			retorno = RECON_ESTUD_PREVIOS_SNIESE_NO_VALUE;
		}else{
			throw new FichaEstudianteValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteConstantes.traer.tipo.reconocimiento.estudios.previos.sniese.exception")));
		}
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo de un tipo de reconocimiento UCE
	 * @param tipoRecUce - valor de un tipo de reconocimiento UCE
	 * @return label del valor representativo de un tipo de reconocimiento UCE
	 */
	public static String valueToLabelTipoRecUce(int tipoRecUce) {
		String retorno = "";
		if(tipoRecUce == RECON_ESTUD_PREVIOS_SI_VALUE){
			retorno = RECON_ESTUD_PREVIOS_SI_LABEL;
		}else if(tipoRecUce == RECON_ESTUD_PREVIOS_NO_VALUE){
			retorno = RECON_ESTUD_PREVIOS_NO_LABEL;
		}
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo de un tipo de duracion UCE
	 * @param tipoDuracUce - valor de un tipo de duracion UCE
	 * @return label del valor representativo de un tipo de duracion UCE
	 */
	public static String valueToLabelTipoDuracUce(int tipoDuracUce) {
		String retorno = "";
		if(tipoDuracUce == TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE){
			retorno = TIPO_DURACION_RECONOCIMIENTO_ANIOS_LABEL;
		}else if(tipoDuracUce == TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE){
			retorno = TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_LABEL;
		}else if(tipoDuracUce == TIPO_DURACION_RECONOCIMIENTO_CREDITOS_VALUE){
			retorno = TIPO_DURACION_RECONOCIMIENTO_CREDITOS_LABEL;
		}
		return retorno;
	}
	
}

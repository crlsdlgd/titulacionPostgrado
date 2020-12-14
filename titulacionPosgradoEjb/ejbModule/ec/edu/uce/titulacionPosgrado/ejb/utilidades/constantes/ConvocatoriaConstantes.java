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
   
 ARCHIVO:     ConvocatoriaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Convocatoria 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) ConvocatoriaConstantes. Clase que maneja las constantes de
 * la entidad Convocatoria.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class ConvocatoriaConstantes {

	// constantes para definir combos de busqueda por año
	public static final String ANO_2016_2017_LABEL = "2016-2017";
	public static final String ANO_2017_2017_LABEL = "2017-2017";
	
	public static final int ANO_2016_2017_VALUE = Integer.valueOf(2016);
	public static final int ANO_2017_2017_VALUE = Integer.valueOf(2017);

	// constantes para definir combos de busqueda por mes
	public static final String MES_ENERO_LABEL = "ENERO";
	public static final String MES_FEBRERO_LABEL = "FEBRERO";
	public static final String MES_MARZO_LABEL = "MARZO";
	public static final String MES_ABRIL_LABEL = "ABRIL";
	public static final String MES_MAYO_LABEL = "MAYO";
	public static final String MES_JUNIO_LABEL = "JUNIO";
	public static final String MES_JULIO_LABEL = "JULIO";
	public static final String MES_AGOSTO_LABEL = "AGOSTO";
	public static final String MES_SEPTIEMBRE_LABEL = "SEPTIEMBRE";
	public static final String MES_OCTUBRE_LABEL = "OCTUBRE";
	public static final String MES_NOVIEMBRE_LABEL = "NOVIEMBRE";
	public static final String MES_DICIEMBRE_LABEL = "DICIEMBRE";

	public static final int MES_ENERO_VALUE = Integer.valueOf(1);
	public static final int MES_FEBRERO_VALUE = Integer.valueOf(2);
	public static final int MES_MARZO_VALUE = Integer.valueOf(3);
	public static final int MES_ABRIL_VALUE = Integer.valueOf(4);
	public static final int MES_MAYO_VALUE = Integer.valueOf(5);
	public static final int MES_JUNIO_VALUE = Integer.valueOf(6);
	public static final int MES_JULIO_VALUE = Integer.valueOf(7);
	public static final int MES_AGOSTO_VALUE = Integer.valueOf(8);
	public static final int MES_SEPTIEMBRE_VALUE = Integer.valueOf(9);
	public static final int MES_OCTUBRE_VALUE = Integer.valueOf(10);
	public static final int MES_NOVIEMBRE_VALUE = Integer.valueOf(11);
	public static final int MES_DICIEMBRE_VALUE = Integer.valueOf(12);

	// constantes para el campo de cnv_estado
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVA";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVA";
	public static final int ESTADO_PENDIENTE_VALUE = Integer.valueOf(2);
	public static final String ESTADO_PENDIENTE_LABEL = "PENDIENTE CIERRE DE ESTADO";

	// constantes para el campo de cnv_estado_fase
	public static final int ESTADO_FASE_REGISTRO_POSTULACION_VALUE = Integer
			.valueOf(0);
	public static final String ESTADO_FASE_REGISTRO_POSTULACION_LABEL = "FASE DE REGISTRO Y POSTULACIÓN";
	public static final int ESTADO_FASE_TITULACION_VALUE = Integer.valueOf(1);
	public static final String ESTADO_FASE_TITULACION_LABEL = "FASE DE TITULACIÓN";

	// constantes para definir la primera convocatoria
	public static final int CONVOCATORIA_INICIAL_VALUE = Integer.valueOf(1);
	public static final String CONVOCATORIA_INICIAL_LABEL = "PRIMERA CONVOCATORIA";
	// constantes para definir la segunda convocatoria
	public static final int CONVOCATORIA_SEGUNDA_VALUE = Integer.valueOf(2);
	public static final String CONVOCATORIA_SEGUNDA_LABEL = "SEGUNDA CONVOCATORIA";
	
}

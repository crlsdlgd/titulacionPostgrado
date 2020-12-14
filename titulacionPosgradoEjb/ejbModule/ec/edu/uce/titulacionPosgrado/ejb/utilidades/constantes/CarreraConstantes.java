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
   
 ARCHIVO:     CarreraConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Carrera. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) CarreraConstantes.
 * Clase que maneja las constantes de la entidad Carrera.
 * @author dalbuja.
 * @version 1.0
 */
public class CarreraConstantes {
	
	//Constantes para tipo de duracion de la carrera
	public static final int TIPO_DURACION_CARRERA_ANIOS_VALUE = Integer.valueOf(0);
	public static final String TIPO_DURACION_CARRERA_ANIOS_LABEL = "ANIOS";
	public static final int TIPO_DURACION_CARRERA_SEMESTRE_VALUE = Integer.valueOf(1);
	public static final String TIPO_DURACION_CARRERA_SEMESTRE_LABEL = "SEMESTRES";
	public static final int TIPO_DURACION_CARRERA_CREDITOS_VALUE = Integer.valueOf(2);
	public static final String TIPO_DURACION_CARRERA_CREDITOS_LABEL = "CREDITOS";
	public static final String TIPO_NIVEL_CARRERA_TERCER_NIVEL_LABEL="TERCER NIVEL";
	public static final String TIPO_NIVEL_CARRERA_CUARTO_NIVEL_LABEL="CUARTO NIVEL";
	public static final int TIPO_NIVEL_CARRERA_TERCER_NIVEL_VALUE=Integer.valueOf(0);
	public static final int TIPO_NIVEL_CARRERA_CUARTO_NIVEL_VALUE=Integer.valueOf(1);
	
	public static final String SELECCION_TODAS_LAS_CARRERAS="TODAS LAS CARRERAS";
	public static final String SELECCION_TODAS_LAS_FACULTADES="TODAS LAS FACULTADES";
	
	public static final int CARRERA_ECONOMIA_VALUE = Integer.valueOf(12);
	public static final String CARRERA_ECONOMIA_LABEL = "ECONOMIA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	public static final int CARRERA_ECONOMIA_DISTANCIA_VALUE = Integer.valueOf(15);
	public static final String CARRERA_ECONOMIA_DISTANCIA_LABEL = "ECONOMIA (NO VIGENTE HABILITADO PARA REGISTRO DE TÍTULOS - DISTANCIA - Tercer Nivel o Pregrado)";
	
	public static final int CARRERA_DERECHO_VALUE = Integer.valueOf(80);
	public static final String CARRERA_DERECHO_LABEL = "DERECHO (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	
	public static final int CARRERA_COMUNICACION_SOCIAL_VALUE = Integer.valueOf(42);
	public static final String CARRERA_COMUNICACION_SOCIAL_LABEL = "COMUNICACION SOCIAL (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	public static final int CARRERA_TURISMO_HISTORICO_CULTURAL_NO_VIGENTE_VALUE = Integer.valueOf(43);
	public static final String CARRERA_TURISMO_HISTORICO_CULTURAL_NO_VIGENTE_LABEL = "TURISMO HISTORICO CULTURAL (NO VIGENTE HABILITADO PARA REGISTRO DE TÍTULOS - PRESENCIAL - Nivel Técnico Superior)";
	public static final int CARRERA_TURISMO_HISTORICO_CULTURAL_VIGENTE_VALUE = Integer.valueOf(44);
	public static final String CARRERA_TURISMO_HISTORICO_CULTURAL_VIGENTE_LABEL = "TURISMO HISTORICO CULTURAL (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	public static final int CARRERA_ODONTOLOGIA_VALUE = Integer.valueOf(84);
	public static final String CARRERA_ODONTOLOGIA_LABEL = "ODONTOLOGIA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";

	public static final int CARRERA_INGENIERIA_INFORMATICA_VALUE = Integer.valueOf(72);
	public static final String CARRERA_INGENIERIA_INFORMATICA_LABEL = "INGENIERIA EN INFORMATICA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	public static final int CARRERA_INGENIERIA_MATEMATICA_VALUE = Integer.valueOf(74);
	public static final String CARRERA_INGENIERIA_MATEMATICA_LABEL = "INGENIERIA MATEMATICA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	public static final int CARRERA_INGENIERIA_DISEÑO_INDUSTRIAL_VALUE = Integer.valueOf(76);
	public static final String CARRERA_INGENIERIA_DISEÑO_INDUSTRIAL_LABEL = "INGENIERIA EN DISEÑO INDUSTRIAL (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	public static final int CARRERA_INGENIERIA_COMPUTACION_GRAFICA_VALUE = Integer.valueOf(77);
	public static final String CARRERA_INGENIERIA_COMPUTACION_GRAFICA_LABEL = "INGENIERIA EN COMPUTACION GRAFICA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	
	public static final int CARRERA_OBSTETRICIA_VALUE = Integer.valueOf(18);
	public static final String CARRERA_OBSTETRICIA_LABEL = "OBSTETRICIA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";	
	
	public static final int CARRERA_MEDICINA_VALUE = Integer.valueOf(31);
	public static final String CARRERA_MEDICINA_LABEL = "MEDICINA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";	
	
	public static final int CARRERA_ENFERMERIA_VALUE = Integer.valueOf(17);
	public static final String CARRERA_ENFERMERIA_LABEL = "ENFERMERIA (VIGENTE - PRESENCIAL - Tercer Nivel o Pregrado)";
	public static final int CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_VALUE = Integer.valueOf(28);
	public static final String CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_LABEL = "ENFERMERIA (NO VIGENTE HABILITADO PARA REGISTRO DE TÍTULOS - PRESENCIAL - Nivel Técnico Superior)";
	public static final int CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_VALUE = Integer.valueOf(88);
	public static final String CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_LABEL = "ENFERMERIA (NO VIGENTE HABILITADO PARA REGISTRO DE TÍTULOS - PRESENCIAL - Tercer Nivel o Pregrado)";
	
	public static final int CARRERA_EVALUACION_DEFENSA_ORAL_VALUE = Integer.valueOf(0);
	public static final String CARRERA_EVALUACION_DEFENSA_ORAL_LABEL = "DEFENSA ORAL UNICAMENTE";
	public static final int CARRERA_EVALUACION_DEFENSA_ESCRITO_ORAL_VALUE = Integer.valueOf(1);
	public static final String CARRERA_EVALUACION_DEFENSA_ESCRITO_ORAL_LABEL = "DEFENSA ESCRITA Y ORAL";
	public static final int CARRERA_EVALUACION_DEFENSA_DIVISION_TRES_VALUE = Integer.valueOf(2);
	public static final String CARRERA_EVALUACION_DEFENSA_DIVISION_TRES_LABEL = "TRES VALORES ACTA DE GRADO";
	
}

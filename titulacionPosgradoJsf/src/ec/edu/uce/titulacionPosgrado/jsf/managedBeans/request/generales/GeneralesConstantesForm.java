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
   
 ARCHIVO:     GeneralesConstantesForm.java	  
 DESCRIPCION: Bean de peticion que maneja las constantes generales. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 18-04-2017 			Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.request.generales;



import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ConvocatoriaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UbicacionConstantes;


/**
 * Clase (request bean) GeneralesConstantesForm.
 * Bean de peticion que maneja las constantes generales.
 * @author dalbuja.
 * @version 1.0
 */
@ManagedBean
@RequestScoped
public class GeneralesConstantesForm {
	
	
	/**
	 * Retorna el valor de la constante de campo requerido
	 * @return el valor de la constante de campo requerido
	 */
	public String getMsgCampoRequerido() {
		return GeneralesConstantes.APP_MSG_CAMP_REQUERIDO;
	}
	
	/**
	 * Retorna la constante que representa a la etiqueta de la opcion de seleccionar
	 * @return la constante que representa a la etiqueta de la opcion de seleccionar
	 */
	public String getAppSeleccione() {
		return GeneralesConstantes.APP_SELECCIONE;
	}
	
	/**
	 * Retorna la constante que representa a la etiqueta de la opcion de seleccionar todos
	 * @return la constante que representa a la etiqueta de la opcion de seleccionar todos
	 */
	public String getAppSeleccioneTodos() {
		return GeneralesConstantes.APP_SELECCIONE_TODOS;
	}
	
	/**
	 * Retorna el valor de la de APP_ID_BASE de la entidad
	 * @return el valor de la de APP_ID_BASE de la entidad
	 */
	public Integer getAppIdBase() {
		return GeneralesConstantes.APP_ID_BASE;
	}
	/**
	 * Retorna el valor de la de APP_EDITAR
	 * @return el valor de la de APP_EDITAR
	 */
	public Integer getAppIdEditar() {
		return GeneralesConstantes.APP_EDITAR;
	}
	/**
	 * Retorna el valor de la de APP_NUEVO
	 * @return el valor de la de APP_NUEVO
	 */
	public Integer getAppIdNuevo() {
		return GeneralesConstantes.APP_NUEVO;
	}
	/**
	 * Retorna el formato fecha y hora
	 * @return el formato fecha y hora
	 */
	public String getAppFormatoFechaHora() {
		return GeneralesConstantes.APP_FORMATO_FECHA_HORA;
	}
	
	/**
	 * Retorna el formato fecha
	 * @return el formato fecha
	 */
	public String getAppFormatoFecha() {
		return GeneralesConstantes.APP_FORMATO_FECHA;
	}

	/**
	 * Retorna la fecha actual del sistema
	 * @return
	 */
	public Date getFechaDelSistema() {
		Date fecha = new Date();
		return fecha;
    }
	/**
	 * Retorna el valor de si para el reonocimiento de estudios previos
	 * @return el valor de si para el reonocimiento de estudios previos
	 */
	public Integer getSiRecEstudPrevios() {
		return FichaEstudianteConstantes.RECON_ESTUD_PREVIOS_SI_VALUE;
	}
	
	/**
	 * Retorna el valor de no para el reonocimiento de estudios previos
	 * @return el valor de no para el reonocimiento de estudios previos
	 */
	public Integer getNoRecEstudPrevios() {
		return FichaEstudianteConstantes.RECON_ESTUD_PREVIOS_NO_VALUE;
	}
	
	/**
	 * Retorna el valor para la ubicacion Ecuador 
	 * @return el valor para la ubicacion Ecuador
	 */
	public Integer getEcuadorValue() {
		return UbicacionConstantes.ECUADOR_VALUE;
	}
	
	

	/**
	 * Retorn el valor de la constantes ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_LABEL
	 * @return el valor de la constantes ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_LABEL de la entidad ConvocatoriaConstantes
	 */
	public String traerFaseConvocatoria(int tipoBD){
		if(tipoBD == ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_VALUE){
			return ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_LABEL;
		}
		
		if(tipoBD == ConvocatoriaConstantes.ESTADO_FASE_TITULACION_VALUE){
			return ConvocatoriaConstantes.ESTADO_FASE_TITULACION_LABEL;
		}
		return null;
	}
	
	/**
	 * Retorn el valor de la constantes ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_LABEL
	 * @return el valor de la constantes ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_LABEL de la entidad ConvocatoriaConstantes
	 */
	public String traerEstadoConvocatoria(int tipoBD){
		if(tipoBD == ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE){
			return ConvocatoriaConstantes.ESTADO_ACTIVO_LABEL;
		}
		
		if(tipoBD == ConvocatoriaConstantes.ESTADO_INACTIVO_VALUE){
			return ConvocatoriaConstantes.ESTADO_INACTIVO_LABEL;
		}
		
		if(tipoBD == ConvocatoriaConstantes.ESTADO_PENDIENTE_VALUE){
			return ConvocatoriaConstantes.ESTADO_PENDIENTE_LABEL;
		}
		return null;
	}
	
	
	/**
	 * Retorn el valor de la constantes LABEL de tipo de identificacion de la persona
	 * @return el valor de la constantes PersonaConstantes: 0.-CEDULA 1.-PASAPORTE/OTROS
	 */
	public String traerTipoIdentificacion(int tipoBD){
		if(tipoBD == PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
			return PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_LABEL;
		}
		
		if(tipoBD == PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE){
			return PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_LABEL;
		}
		return null;
	}
	
	/**
	 * Retorn el valor de la constantes LABEL de Sexo de la persona
	 * @return el valor de la constantes de Sexo: 0.- HOMBRE 1.-MUJER
	 */
	public String traerTipoSexo(int tipoBD){
		if(tipoBD == PersonaConstantes.SEXO_HOMBRE_VALUE){
			return PersonaConstantes.SEXO_HOMBRE_LABEL;
		}
		
		if(tipoBD == PersonaConstantes.SEXO_MUJER_VALUE){
			return PersonaConstantes.SEXO_MUJER_LABEL;
		}
		return null;
	}
	
	/**
	 * Retorn el valor de la constantes LABEL de Tipo de duracion reconocimiento de la persona
	 * @return el valor de la constantes de Tipo duracion reconocimiento: 0.- años 1.-semestres 2.- creditos
	 */
	public String traerTipoDuracionReconocimiento(int tipoBD){
		if(tipoBD == FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE){
			return FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_ANIOS_LABEL;
		}
		
		if(tipoBD == FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE){
			return FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_LABEL;
		}
		
		if(tipoBD == FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_CREDITOS_VALUE){
			return FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_CREDITOS_LABEL;
		}
		return null;
	}
	
	/**
	 * Retorn el valor de la constantes LABEL de Sexo de la persona
	 * @return el valor de la constantes de Sexo: 0.- HOMBRE 1.-MUJER
	 */
	public String traerEstadoRolFlujoCarrera(int tipoBD){
		if(tipoBD == RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE){
			return RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_LABEL;
		}
		
		if(tipoBD == RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_INACTIVO_VALUE){
			return RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_INACTIVO_LABEL;
		}
		return null;
	}
	
	/**
	 * Genera la lista de items para el combo estado_tramite
	 * @return lista de items para el combo examen complexivo
	 */
	public String getEstadoEstudiante(int estado)	{
		if(estado == TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE){
			return TramiteTituloConstantes.EST_PROC_VALIDADO_LABEL;
		}
		if(estado == TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE){
			return TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_APTO_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_LABEL;
		}
		if(estado == TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE){
			return TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_LABEL;
		}
		
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_LABEL;
		}
		
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_LABEL;
		}
		
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_LABEL;
		}
		if(estado == TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE){
			return TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_LABEL;
		}
		return null;
	}
	
	public Integer getSiValue() {
		return GeneralesConstantes.APP_SI_VALUE;
	}
	public Integer getNoValue() {
		return GeneralesConstantes.APP_NO_VALUE;
	}	
	
}

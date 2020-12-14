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
   
 ARCHIVO:     ListasCombosForm.java	  
 DESCRIPCION: Bean de peticion que maneja las listas para los combos en la aplicacion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-SEP-2018		Daniel Albuja			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.request.generales;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AptitudConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AsentamientoNotaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AutoridadConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ConvocatoriaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;


/**
 * Clase (request bean) ListasCombosForm.
 * Bean de peticion que maneja las listas para los combos en la aplicacion.
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean(name="listasCombosForm")
@RequestScoped
public class ListasCombosForm implements Serializable {
	private static final long serialVersionUID = -873375668506819062L;
	
	/**
	 * Genera la lista de items para el combo tipo identificación
	 * @return lista de items para el combo tipo identificación
	 */
	public List<SelectItem> getListaTipoIdentificacion()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE,PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE,PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo sexo
	 * @return lista de items para el combo sexo
	 */
	public List<SelectItem> getListaSexo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(PersonaConstantes.SEXO_HOMBRE_VALUE,PersonaConstantes.SEXO_HOMBRE_LABEL));
		retorno.add(new SelectItem(PersonaConstantes.SEXO_MUJER_VALUE,PersonaConstantes.SEXO_MUJER_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo Autoridades Tipo
	 * @return lista de items para el combo Autoridades Tipo
	 */
	public List<SelectItem> getListaTipoAutoridad()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(AutoridadConstantes.DECANO_FACULTAD_VALUE,AutoridadConstantes.DECANO_FACULTAD_LABEL));
		retorno.add(new SelectItem(AutoridadConstantes.SUBDECANO_FACULTAD_VALUE,AutoridadConstantes.SUBDECANO_FACULTAD_LABEL));
		retorno.add(new SelectItem(AutoridadConstantes.SECRETARIO_FACULTAD_VALUE,AutoridadConstantes.SECRETARIO_FACULTAD_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo Autoridades Estado
	 * @return lista de items para el combo Autoridades Estado
	 */
	public List<SelectItem> getListaEstadoAutoridad()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(AutoridadConstantes.ESTADO_ACTIVO_VALUE,AutoridadConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(AutoridadConstantes.ESTADO_INACTIVO_VALUE,AutoridadConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo sexo para los titulos
	 * @return lista de items para el combo sexo para los titulos
	 */
	public List<SelectItem> getListaGeneroTitulo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(TituloConstantes.GENERO_MASCULINO_VALUE,TituloConstantes.GENERO_MASCULINO_LABEL));
		retorno.add(new SelectItem(TituloConstantes.GENERO_FEMENINO_VALUE,TituloConstantes.GENERO_FEMENINO_LABEL));
		retorno.add(new SelectItem(TituloConstantes.GENERO_ESTANDAR_VALUE,TituloConstantes.GENERO_ESTANDAR_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo Tipo duracion reconocimiento
	 * @return lista de items para el combo Tipo duracion reconocimiento
	 */
	public List<SelectItem> getListaTipoDuracionReconocimiento()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE,FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_ANIOS_LABEL));
		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE,FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_LABEL));
		retorno.add(new SelectItem(FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_CREDITOS_VALUE,FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_CREDITOS_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo Tipo colegio
	 * @return lista de items para el combo Tipo colegio
	 */
	public List<SelectItem> getListaTipoColegio()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_FISCAL_VALUE ,InstitucionAcademicaConstantes.TIPO_FISCAL_LABEL));
		retorno.add(new SelectItem(InstitucionAcademicaConstantes.TIPO_PARTICULAR_VALUE,InstitucionAcademicaConstantes.TIPO_PARTICULAR_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo Modadlidad Estadistico
	 * @return lista de items para el combo Modadlidad Estadistico
	 */
	public List<SelectItem> getListaModalidadTitulacionEsta()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE ,TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL));
		retorno.add(new SelectItem(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_VALUE ,TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo de opciones para las opciones de restaurar la contraseña
	 * @return lista de items para el combo de opciones para las opciones de restaurar la contraseña
	 */
	public List<SelectItem> getListaComboTipoRestaurar()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(UsuarioConstantes.TIPO_USUARIO_VALUE,UsuarioConstantes.TIPO_USUARIO_LABEL));
		retorno.add(new SelectItem(UsuarioConstantes.TIPO_CORREO_VALUE,UsuarioConstantes.TIPO_CORREO_LABEL));
		return retorno;
	}
	
	/**
	 * Obtiene el valor de la variable tipo colegio
	 * @param estado - id del tipo de colegio para obtener el label del tipo de colegio
	 * @return el label de la variable id colegio.
	 */
	public String getLabelTipoColegio(int idTipoColegio){
		String retorno = null;
		for (SelectItem item : getListaTipoColegio()) {
			if(((Integer)item.getValue()).intValue() == idTipoColegio){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo con opciones de SI y NO 
	 * @return lista de items para el combo con opciones de SI y NO 
	 */
	public List<SelectItem> getListaGeneralSiNo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(GeneralesConstantes.APP_SI_VALUE,GeneralesConstantes.APP_SI_LABEL));
		retorno.add(new SelectItem(GeneralesConstantes.APP_NO_VALUE,GeneralesConstantes.APP_NO_LABEL));
		return retorno;
	}
	
	
	
	
	/**
	 * Genera la lista de items para el combo con opciones de SI y NO 
	 * @return lista de items para el combo con opciones de SI y NO 
	 */
	public List<SelectItem> getListaGeneralSiNoReproboComplexivo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2014_VALUE,"SI REPROBO"));
		retorno.add(new SelectItem(ValidacionConstantes.NO_REPROBO_COMPLEXIVO_VALUE,"NO REPROBO"));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo con opciones de SI y NO 
	 * @return lista de items para el combo con opciones de SI y NO 
	 */
	public List<SelectItem> getListaGeneralSiNoUltimoSemestre()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ValidacionConstantes.SI_ULTIMO_SEMESTRE_VALUE,ValidacionConstantes.SI_ULTIMO_SEMESTRE_LABEL));
		retorno.add(new SelectItem(ValidacionConstantes.NO_ULTIMO_SEMESTRE_VALUE,ValidacionConstantes.NO_ULTIMO_SEMESTRE_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo con opciones de SI y NO 
	 * @return lista de items para el combo con opciones de SI y NO 
	 */
	public List<SelectItem> getListaGeneralSiNoAsignacionTutor()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ValidacionConstantes.SI_ASIGNADO_TUTOR_VALUE,"ASIGNADO TUTOR"));
		retorno.add(new SelectItem(ValidacionConstantes.NO_ASIGNADO_TUTOR_VALUE,"NO ASIGNADO TUTOR"));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo con opciones de SI 2014 SI 2015 y NO 
	 * @return lista de items para el combo con opciones de SI 2014 SI 2015 y NO
	 */
	public List<SelectItem> getListaGeneralReproboComplexivo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2014_VALUE,ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2014_LABEL));
		retorno.add(new SelectItem(ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2015_VALUE,ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2015_LABEL));
		retorno.add(new SelectItem(ValidacionConstantes.NO_REPROBO_COMPLEXIVO_VALUE,ValidacionConstantes.NO_REPROBO_COMPLEXIVO_LABEL));
		return retorno;
	}
	
	/**
	 * Genera la lista de items para el combo con de fases de convocatoria 
	 * @return lista de items para el combo
	 */
	public List<SelectItem> getListaConvocatoriaFase()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_VALUE,ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_LABEL));
		retorno.add(new SelectItem(ConvocatoriaConstantes.ESTADO_FASE_TITULACION_VALUE,ConvocatoriaConstantes.ESTADO_FASE_TITULACION_LABEL));
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo con de fases de convocatoria 
	 * @return lista de items para el combo
	 */
	public String getListaConvocatoriaEstado(int estado)	{
		String retorno = null;
		if(estado==ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE){
			retorno=ConvocatoriaConstantes.ESTADO_ACTIVO_LABEL;
		}
		if(estado==ConvocatoriaConstantes.ESTADO_INACTIVO_VALUE){
			retorno=ConvocatoriaConstantes.ESTADO_INACTIVO_LABEL;
		}
		if(estado==ConvocatoriaConstantes.ESTADO_PENDIENTE_VALUE){
			retorno=ConvocatoriaConstantes.ESTADO_PENDIENTE_LABEL;
		}
		return retorno;
	}
	
	
	/**
	 * Obtiente el label del sexo del usuario
	 * @param sexo - sexo del usuario a obtener
	 * @return label del valor del usuario
	 */
	public String getLabelSexoUsuario(int sexo){
		String retorno = null;
		for (SelectItem item : getListaSexo()) {
			if(((Integer)item.getValue()).intValue() == sexo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	/**
	 * Obtiente el label del resultado de aptitud
	 * @param aptitud - aptitud del usuario a obtener
	 * @return label del valor del usuario
	 */
	public String getLabelAptoNoApto(int aptitud){
		String retorno = null;
		if(aptitud==TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_LABEL;
		}else{
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APTO_LABEL;
		}
		return retorno;
	}
	
	/**
	 * Obtiente la lista del combo de porcentaje de parte teórica del examen complexivo
	 * 
	 * @return lista de selectitems
	 */
	public List<SelectItem> getListaPorcentajesExComplexivo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(GeneralesConstantes.APP_SELECCIONE));
		retorno.add(new SelectItem("50%"));
		retorno.add(new SelectItem("60%"));
		retorno.add(new SelectItem("70%"));
		retorno.add(new SelectItem("80%"));
		retorno.add(new SelectItem("90%"));
		retorno.add(new SelectItem("100%"));
		return retorno;
	}
	
	/**
	 * Obtiente el label del resultado de aptitud
	 * @param aptitud - aptitud del usuario a obtener
	 * @return label del valor del usuario
	 */
	public String getRespuestaSiNO(int aptitud){
		String retorno = null;
		if(aptitud==AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE){
			retorno=AptitudConstantes.SI_APROBO_ACTUALIZAR_LABEL;
		}else if(aptitud==AptitudConstantes.NO_APROBO_ACTUALIZAR_VALUE){
			retorno=AptitudConstantes.NO_APROBO_ACTUALIZAR_LABEL;
		}else if (aptitud==GeneralesConstantes.APP_ID_BASE){
			retorno=null;
		}
		return retorno;
	}
	
	
	/*
	 * Genera la lista de items para el combo estado usuario
	 * @return lista de items para el combo estado usuario
	 */
	public List<SelectItem> getListaEstadoUsuario()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(UsuarioConstantes.ESTADO_ACTIVO_VALUE,UsuarioConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(UsuarioConstantes.ESTADO_INACTIVO_VALUE,UsuarioConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	/**
	 * Genera la lista de items para el combo examen complexivo
	 * @return lista de items para el combo examen complexivo
	 */
	public List<SelectItem> getListaTipoComplexivo()	{
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(AsentamientoNotaConstantes.EXAMEN_COMPLEXIVO_NORMAL_VALUE,AsentamientoNotaConstantes.EXAMEN_COMPLEXIVO_NORMAL_LABEL));
		retorno.add(new SelectItem(AsentamientoNotaConstantes.EXAMEN_COMPLEXIVO_GRACIA_VALUE,AsentamientoNotaConstantes.EXAMEN_COMPLEXIVO_GRACIA_LABEL));
		return retorno;
	}
	/**
	 * Genera la lista de items para el combo estado_tramite
	 * @return lista de items para el combo examen complexivo
	 */
	public String getEstadoTramite(int estado)	{
		String retorno = null;
		if(estado==TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_LABEL;
		}
		return retorno;
	}
	/**
	 * Genera la etiqueta correspondiente al estado
	 * @return valor correspondiente a estado
	 */
	public String getRespuestaSiNoReproboComplexivo(int estado)	{
		String retorno = null;
		if(estado==ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2014_VALUE){
			retorno="SI REPROBO";
		}else if(estado==ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2015_VALUE){
			retorno="NO REPROBO";
		}
		return retorno;
	}
	
	/**
	 * Genera la etiqueta correspondiente al estado
	 * @return valor correspondiente a estado
	 */
	public String getRespuestaSiNoAsignacionTutor(int estado)	{
		String retorno = null;
		if(estado==ValidacionConstantes.SI_ASIGNADO_TUTOR_VALUE){
			retorno="ASIGNADO TUTOR";
		}else if(estado==ValidacionConstantes.SI_ASIGNADO_TUTOR_VALUE){
			retorno="NO ASIGNADO TUTOR";
		}
		return retorno;
	}
	
	
	/**
	 * Genera la lista de items para el combo estado_tramite
	 * @return lista de items para el combo examen complexivo
	 */
	public String getEstadoEstudiante(int estado)	{
		String retorno = null;
		if(estado==TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_LABEL;
		}else if(estado==TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.EST_PROC_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE){
			retorno=TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APTO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_EDICION_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_EDICION_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBACION_TUTOR_TRIBUNAL_LECTOR_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_NO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_NO_APROBACION_TUTOR_TRIBUNAL_LECTOR_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_GENERADA_MANUALMENTE_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_ACTA_GENERADA_MANUALMENTE_DEFENSA_LABEL;
		}
		
		
		return retorno;
	}
	
	/**
	 * Obtiente el label de la Autoridad
	 * @param tipo - tipo de la autoridad a obtener
	 * @return label del valor del tipo de autoridad
	 */
	public String getLabelTipoAutoridad(int tipo){
		String retorno = null;
		for (SelectItem item : getListaTipoAutoridad()) {
			if(((Integer)item.getValue()).intValue() == tipo){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	/**
	 * Obtiente el label de la Autoridad
	 * @param estado - estado de la autoridad a obtener
	 * @return label del valor del estado de laautoridad
	 */
	public String getLabelEstadoAutoridad(int estado){
		String retorno = null;
		for (SelectItem item : getListaEstadoAutoridad()) {
			if(((Integer)item.getValue()).intValue() == estado){
				retorno = item.getLabel();
			}
		}
		return retorno;
	}
	
	
	
	 /**
	 * Genera la lista de items para el combo meses
	 * @return lista de items para el combo meses
	 */
	public List<SelectItem> getListaMes()	{
	List<SelectItem> retorno = new ArrayList<SelectItem>();
	retorno = new ArrayList<SelectItem>();
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_ENERO_VALUE, ConvocatoriaConstantes.MES_ENERO_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_FEBRERO_VALUE, ConvocatoriaConstantes.MES_FEBRERO_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_MARZO_VALUE, ConvocatoriaConstantes.MES_MARZO_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_ABRIL_VALUE, ConvocatoriaConstantes.MES_ABRIL_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_MAYO_VALUE, ConvocatoriaConstantes.MES_MAYO_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_JUNIO_VALUE, ConvocatoriaConstantes.MES_JUNIO_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_JULIO_VALUE, ConvocatoriaConstantes.MES_JULIO_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_AGOSTO_VALUE, ConvocatoriaConstantes.MES_AGOSTO_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_SEPTIEMBRE_VALUE, ConvocatoriaConstantes.MES_SEPTIEMBRE_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_OCTUBRE_VALUE, ConvocatoriaConstantes.MES_OCTUBRE_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_NOVIEMBRE_VALUE, ConvocatoriaConstantes.MES_NOVIEMBRE_LABEL));
	retorno.add(new SelectItem(ConvocatoriaConstantes.MES_DICIEMBRE_VALUE, ConvocatoriaConstantes.MES_DICIEMBRE_LABEL));
	return retorno;

	}
	
	
}

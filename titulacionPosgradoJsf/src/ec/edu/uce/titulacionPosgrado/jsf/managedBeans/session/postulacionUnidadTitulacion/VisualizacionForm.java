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
   
 ARCHIVO:     VisualizacionForm.java	  
 DESCRIPCION: Bean de sesion que maneja la vista de las postulaciones de estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.postulacionUnidadTitulacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

//import ec.edu.uce.titulacion.ejb.dtos.EstudianteAptitudJdbcDto;
//import ec.edu.uce.titulacion.ejb.dtos.EstudianteDetalleComplexivoDto;
//import ec.edu.uce.titulacion.ejb.dtos.EstudianteEvaluacionJdbcDto;
//import ec.edu.uce.titulacion.ejb.servicios.jdbc.interfaces.EstudianteDetalleComplexivoDtoServicioJdbc;
//import ec.edu.uce.titulacion.ejb.servicios.jdbc.interfaces.EstudianteEvaluacionDtoServicioJdbc;
//import ec.edu.uce.titulacion.ejb.utilidades.constantes.DetalleComplexivoConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudiantePostuladoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.FacultadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ConvocatoriaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) VisualizacionForm.
 * Bean de sesion que maneja la vista de las postulaciones de estudiantes.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="visualizacionForm")
@SessionScoped
public class VisualizacionForm implements Serializable {
	
	private static final long serialVersionUID = -6154363931113111345L;
	
	private FacultadDto vfFacultadDto;
	private CarreraDto vfCarreraDto;
	private List<FacultadDto> vfListFacultades;
	private List<CarreraDto> vfListCarreras;
	private List<EstudiantePostuladoJdbcDto> vfListEstudiantePostuladoJdbcDto;
	private Convocatoria vfCnvActiva;
	private Persona vfPersonaPostulante;
	private EstudiantePostuladoJdbcDto vfEstudiantePostuladoJdbcDto;
	private UbicacionDto vfPaisResidencia;
	private UbicacionDto vfProvinciaResidencia;
	private UbicacionDto vfCantonResidencia;
	
	private EstudianteValidacionJdbcDto vfEstudianteValidacionJdbcDto;
//	private EstudianteEvaluacionJdbcDto vfEstudianteEvaluacionJdbcDto;
//	private EstudianteAptitudJdbcDto vfEstudianteAptitudJdbcDto;
//	private EstudianteDetalleComplexivoDto vfEstudianteComplexivoDto;
//	private EstudianteDetalleComplexivoDto vfEstudianteComplexivoPracticoDto;
	private String culmino_malla;
	private String reprobo_complexivo;
	private String asignado_tutor;
	private String ultimo_semestre;
	private boolean vfVisualizarTemaTutor;
	private boolean vfVisualizarComplexivo;
	private boolean vfVisualizarComplexivoGracia;
	private String vfFechaCarga;
	private String vfFechaPractico;
	private String vfResultadoComplexivo;
	private String vfFechaCargaGracia;
	private String vfFechaPracticoGracia;
	private String vfResultadoComplexivoGracia;
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	FacultadDtoServicioJdbc servVfFacultadDtoJdbc;
	@EJB
	CarreraDtoServicioJdbc servVfCarreraDtoJdbc;
	@EJB
	ConvocatoriaServicio servVfConvocatoriaServicio;
	@EJB
	PersonaServicio servVfPersonaServicio;
	@EJB
	EstudiantePostuladoDtoServicioJdbc servVfEstudiantePostuladoDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servVfUbicacionJdbc;
	@EJB
	private EstudianteValidacionDtoServicioJdbc servVfEstudianteValidacionDtoServicioJdbc;
//	@EJB
//	private EstudianteEvaluacionDtoServicioJdbc servVfEstudianteEvaluacionDtoServicioJdbc;
//	@EJB
//	private EstudianteDetalleComplexivoDtoServicioJdbc servEstudianteDetalleComplexivoDtoServicioJdbc;

	//****************************************************************/
	//******** Metodo general de inicializacion de variables *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	
	/**
	 * Navegación a la página para listar las postulaciones del estudiante
	 * @param usuario - usuario que se va a visualizar
	 * @return navegacion a la pagina de irListarPostulacion
	 */
	public String irListarPostulacion(Usuario usuario){
		try {
			//busqueda de convocatoria activa
			//asigno la persona por el usuario ingresado
			vfPersonaPostulante = servVfPersonaServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());				
			//Instancio el objeto de postulante
			vfEstudiantePostuladoJdbcDto = new EstudiantePostuladoJdbcDto();			
			//Instancio el objeto facultad
			vfFacultadDto = new FacultadDto(GeneralesConstantes.APP_ID_BASE);
			//Instancio el objeto carrera
			vfCarreraDto = new CarreraDto(GeneralesConstantes.APP_ID_BASE);			
			//Busco las facultades en bd
			vfListFacultades = servVfFacultadDtoJdbc.listarTodos();
			
		} catch (FacultadDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.postulacion.facultad.excepcion")));
		} catch (FacultadDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.postulacion.facultad.no.encontrada.excepcion")));
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.postulacion.persona.no.encontrada.excepcion")));
		} catch (PersonaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.postulacion.persona.excepcion")));
		}
		vfVisualizarTemaTutor=false;
		return "irListarPostulacion";
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		vfEstudiantePostuladoJdbcDto = new EstudiantePostuladoJdbcDto();			
		vfFacultadDto = new FacultadDto(GeneralesConstantes.APP_ID_BASE);
		vfCarreraDto = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
		vfListCarreras = null;
		vfListEstudiantePostuladoJdbcDto = null;
		vfVisualizarTemaTutor=true;
		vfVisualizarComplexivo=true;
		vfVisualizarComplexivoGracia=true;
		vfFechaCargaGracia=null;
		vfFechaPracticoGracia=null;
		vfResultadoComplexivoGracia=null;
		return "irInicio";
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de listar postulaciones
	 * @return  Navegacion a la pagina de listar postulaciones.
	 */
	public String regresarAListaPostulaciones(){
		vfFacultadDto = new FacultadDto(GeneralesConstantes.APP_ID_BASE);
		vfCarreraDto = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
		vfListCarreras = null;
		vfListEstudiantePostuladoJdbcDto = null;
		vfVisualizarTemaTutor=true;
		return "regresarAListaPostulaciones";
	}
	
	/**
	 * Limpia los parámetro ingresados en el panel de busqueda 
	 */
	public void limpiarBusquedaVisualizacion(){
		vfFacultadDto = new FacultadDto(GeneralesConstantes.APP_ID_BASE);
		vfCarreraDto = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
		vfListCarreras = null;
		vfListEstudiantePostuladoJdbcDto = null;
		vfVisualizarTemaTutor=true;
	}
	
	/**
	 * Navegación a la página para visualizar la postulación del estudiante
	 * @param estudiantePostulado - estudiantePostulado que se va a visualizar
	 * @return navegacion a la pagina de irVisualizarPostulacion
	 */
	public String irVisualizarPostulacion(EstudiantePostuladoJdbcDto estudiantePostulado){
		vfFechaCargaGracia=null;
		vfFechaPracticoGracia=null;
		vfResultadoComplexivoGracia=null;
		this.vfEstudiantePostuladoJdbcDto = estudiantePostulado;
		try {
			vfCantonResidencia = servVfUbicacionJdbc.buscarXId(vfEstudiantePostuladoJdbcDto.getUbcCantonId());
			UbicacionDto ubicacionAux =  null;
			try {
				ubicacionAux =  servVfUbicacionJdbc.buscarPadreXId(vfEstudiantePostuladoJdbcDto.getUbcCantonId());
			} catch (UbicacionDtoJdbcException e) {
				ubicacionAux =  null;
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				ubicacionAux =  null;
			}
			//verfifica si es pais o canton
			if(ubicacionAux == null){//No es ecuador
				vfPaisResidencia = vfCantonResidencia;
				vfProvinciaResidencia = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
				vfProvinciaResidencia.setUbcDescripcion("N/A");
				vfCantonResidencia = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
				vfCantonResidencia.setUbcDescripcion("N/A");
			}else{
				vfProvinciaResidencia = servVfUbicacionJdbc.buscarPadreXId(vfCantonResidencia.getUbcId());
				vfPaisResidencia = servVfUbicacionJdbc.buscarPadreXId(vfProvinciaResidencia.getUbcId());
			}
		} catch (UbicacionDtoJdbcException 
				| UbicacionDtoJdbcNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.ir.detalle.postulacion.ubicacion.excepcion")));
		}
		
		try {
			//Busco la información de la validación del estudiante, si ha sido validado
			vfEstudianteValidacionJdbcDto=servVfEstudianteValidacionDtoServicioJdbc.buscarValidacionXEstudiante(estudiantePostulado);
//			// Asigno a las variables los resultados de acuerdo a los valores obtenidos
//			if(vfEstudianteValidacionJdbcDto.getVldCulminoMalla()==ValidacionConstantes.NO_CULMINO_MALLA_VALUE){
//				culmino_malla=ValidacionConstantes.NO_CULMINO_MALLA_LABEL;
//			}else if(vfEstudianteValidacionJdbcDto.getVldCulminoMalla()==ValidacionConstantes.SI_CULMINO_MALLA_VALUE){
//				culmino_malla=ValidacionConstantes.SI_CULMINO_MALLA_LABEL;
//			}
//			if(vfEstudianteValidacionJdbcDto.getVldReproboComplexivo()==ValidacionConstantes.NO_REPROBO_COMPLEXIVO_VALUE){
//				reprobo_complexivo=ValidacionConstantes.NO_REPROBO_COMPLEXIVO_LABEL;
//			}else if(vfEstudianteValidacionJdbcDto.getVldReproboComplexivo()==ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2014_VALUE){
//				reprobo_complexivo=ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2014_LABEL;
//			}else if (vfEstudianteValidacionJdbcDto.getVldReproboComplexivo()==ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2015_VALUE){
//				reprobo_complexivo=ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2015_LABEL;
//			}else{
//				reprobo_complexivo=null;
//			}
//			if(vfEstudianteValidacionJdbcDto.getVldAsignadoTutor()==ValidacionConstantes.SI_ASIGNADO_TUTOR_VALUE){
//				asignado_tutor=ValidacionConstantes.SI_ASIGNADO_TUTOR_LABEL;
//			}else if(vfEstudianteValidacionJdbcDto.getVldAsignadoTutor()==ValidacionConstantes.NO_ASIGNADO_TUTOR_VALUE){
//				asignado_tutor=ValidacionConstantes.NO_ASIGNADO_TUTOR_LABEL;
//			}else{
//				asignado_tutor=null;
//			}
//			if(vfEstudianteValidacionJdbcDto.getVldUltimoSemestre()==ValidacionConstantes.SI_ULTIMO_SEMESTRE_VALUE){
//				ultimo_semestre=ValidacionConstantes.SI_ULTIMO_SEMESTRE_LABEL;
//			}else if (vfEstudianteValidacionJdbcDto.getVldUltimoSemestre()==ValidacionConstantes.NO_ULTIMO_SEMESTRE_VALUE){
//				ultimo_semestre=ValidacionConstantes.NO_ULTIMO_SEMESTRE_LABEL;
//			}else{
//				ultimo_semestre=null;
//			}
//			
//			vfEstudianteEvaluacionJdbcDto=servVfEstudianteEvaluacionDtoServicioJdbc.buscarEvaluacionXEstudiante(estudiantePostulado);
//			if(vfEstudianteEvaluacionJdbcDto.getAsttTemaTrabajo()==null){
//				vfVisualizarTemaTutor=false;
//			}else{
//				vfVisualizarTemaTutor=true;
//			}
//			vfEstudianteAptitudJdbcDto=servVfEstudianteEvaluacionDtoServicioJdbc.buscarAptitudXEstudiante(estudiantePostulado);
//			if(vfEstudianteAptitudJdbcDto.getAptId()==null){
//				vfEstudianteAptitudJdbcDto=new EstudianteAptitudJdbcDto();
//				vfEstudianteAptitudJdbcDto.setAptAproboActualizacion(GeneralesConstantes.APP_ID_BASE);
//				vfEstudianteAptitudJdbcDto.setAptReproboCreditos(GeneralesConstantes.APP_ID_BASE);
//				vfEstudianteAptitudJdbcDto.setAptRequisitos(GeneralesConstantes.APP_ID_BASE);
//				vfEstudianteAptitudJdbcDto.setAptSegundaCarrera(GeneralesConstantes.APP_ID_BASE);
//			}
		} catch (EstudiantePostuladoJdbcDtoException
				| EstudiantePostuladoJdbcDtoNoEncontradoException e) {
		}catch(Exception e){
//			// En el caso de que no haya ningún resultado de validación, cuando solo se ha postulado sin validar
//			vfEstudianteAptitudJdbcDto=new EstudianteAptitudJdbcDto();
//			vfEstudianteAptitudJdbcDto.setAptAproboActualizacion(GeneralesConstantes.APP_ID_BASE);
//			vfEstudianteAptitudJdbcDto.setAptReproboCreditos(GeneralesConstantes.APP_ID_BASE);
//			vfEstudianteAptitudJdbcDto.setAptRequisitos(GeneralesConstantes.APP_ID_BASE);
//			vfEstudianteAptitudJdbcDto.setAptSegundaCarrera(GeneralesConstantes.APP_ID_BASE);
//			culmino_malla=null;
//			reprobo_complexivo=null;
//			asignado_tutor=null;
//			ultimo_semestre=null;
			vfEstudianteValidacionJdbcDto=new EstudianteValidacionJdbcDto();
		}
//		vfEstudianteComplexivoDto=null;
//		vfVisualizarComplexivo=false;
//		vfVisualizarComplexivoGracia=false;
//		try {
//			vfEstudianteComplexivoDto=servEstudianteDetalleComplexivoDtoServicioJdbc.buscarAsentamientoNotaDetalleComplexivoXIdentificacionXCarrera(vfEstudiantePostuladoJdbcDto.getPrsIdentificacion(), vfEstudiantePostuladoJdbcDto.getTrttCarrera(), estudiantePostulado.getCnvId());
//			vfFechaCarga=null;
//			if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_CARGADO_VALUE){
//				vfVisualizarComplexivo=true;
//				vfResultadoComplexivo=DetalleComplexivoConstantes.FALTA_COMPONENTE_PRACTICO_LABEL;
//				vfEstudianteComplexivoDto.setAsnoComplexivoFinal(null);
//				vfEstudianteComplexivoDto.setAsnoComplexivoPractico(null);
//				vfFechaPractico=null;
//				vfVisualizarComplexivoGracia=false;
//				vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaTeorico(null);
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaFinal(null);
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaPractico(null);
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_VALUE){
//				vfVisualizarComplexivo=true;
//				vfFechaPractico=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//				vfVisualizarComplexivoGracia=false;
//				vfEstudianteComplexivoDto.setAsnoComplexivoFinal(null);
//				vfEstudianteComplexivoDto.setAsnoComplexivoTeorico(null);
//				vfResultadoComplexivo=DetalleComplexivoConstantes.FALTA_COMPONENTE_TEORICO_LABEL;
//				vfFechaCarga=null;
//				vfVisualizarComplexivoGracia=false;
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaTeorico(null);
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaFinal(null);
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaPractico(null);
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE){
//				vfVisualizarComplexivo=true;	
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaPractico(null);
//					vfEstudianteComplexivoDto.setAsnoCmpGraciaFinal(null);
//					vfEstudianteComplexivoDto.setAsnoCmpGraciaTeorico(null);
//					vfVisualizarComplexivoGracia=false;
//					vfFechaCargaGracia=null;
//					vfFechaPracticoGracia=null;
//					vfResultadoComplexivo=DetalleComplexivoConstantes.NO_APRUEBA_PRESENTARSE_AL_EXAMEN_DE_GRACIA_LABEL;
//					vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//					vfFechaPractico=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//					if(vfEstudianteComplexivoDto.getMcttcrPorcentaje()==MecanismoTitulacionCarreraConstantes.PORCENTAJE_CIEN_POR_CIENTO_MECANISMO_TITULACION){
//						vfFechaPractico=null;
//						vfEstudianteComplexivoDto.setAsnoComplexivoPractico(null);
//					}
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_VALUE){
//				vfVisualizarComplexivo=true;
//				vfVisualizarComplexivoGracia=true;
//				vfFechaPracticoGracia=null;
//				vfResultadoComplexivoGracia=DetalleComplexivoConstantes.FALTA_COMPONENTE_PRACTICO_LABEL;
//				vfFechaCargaGracia=vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaTeorico().toString().substring(0, 10) ;
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaFinal(null);
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaPractico(null);
//				vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//				vfFechaPractico=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_VALUE){
//				vfVisualizarComplexivo=true;
//				vfVisualizarComplexivoGracia=true;
//				vfFechaCargaGracia=null;
//				vfResultadoComplexivoGracia=DetalleComplexivoConstantes.FALTA_COMPONENTE_TEORICO_LABEL;
//				vfFechaPracticoGracia=vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaPractico().toString().substring(0, 10) ;
//				vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//				vfFechaPracticoGracia=vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaPractico().toString().substring(0, 10) ;
//				vfFechaPractico=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaTeorico(null);
//				vfEstudianteComplexivoDto.setAsnoCmpGraciaFinal(null);
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE){
//				vfVisualizarComplexivo=true;
//				vfVisualizarComplexivoGracia=true;
//				vfResultadoComplexivoGracia=DetalleComplexivoConstantes.NO_APRUEBA_EXAMEN_DE_GRACIA_LABEL;
//				vfFechaCargaGracia= vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaTeorico().toString().substring(0, 10) ;
//				vfResultadoComplexivo=DetalleComplexivoConstantes.NO_APRUEBA_PRESENTARSE_AL_EXAMEN_DE_GRACIA_LABEL;
//				vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//				if(vfEstudianteComplexivoDto.getMcttcrPorcentaje()==MecanismoTitulacionCarreraConstantes.PORCENTAJE_CIEN_POR_CIENTO_MECANISMO_TITULACION){
//					vfFechaPracticoGracia=null;
//					vfEstudianteComplexivoDto.setAsnoCmpGraciaPractico(null);
//					vfFechaPractico=null;
//					vfEstudianteComplexivoDto.setAsnoComplexivoPractico(null);
//				}else{
//					vfFechaPracticoGracia=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//				}
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE
//					|| vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE
//							|| vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE){
//				vfVisualizarComplexivo=true;
//				vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//				vfResultadoComplexivo=DetalleComplexivoConstantes.APROBADO_PROCESO_DE_TITULACION_LABEL;
//				if(vfEstudianteComplexivoDto.getMcttcrPorcentaje()==MecanismoTitulacionCarreraConstantes.PORCENTAJE_CIEN_POR_CIENTO_MECANISMO_TITULACION){
//					vfEstudianteComplexivoDto.setAsnoComplexivoPractico(null);
//					vfFechaPractico=null;
//				}else{
//					vfFechaPractico=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//				}
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE
//					|| vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE
//							|| vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE){
//				vfVisualizarComplexivo=true;
//				vfVisualizarComplexivoGracia=true;
//				vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//				vfResultadoComplexivo=DetalleComplexivoConstantes.NO_APRUEBA_PRESENTARSE_AL_EXAMEN_DE_GRACIA_LABEL;
//				vfResultadoComplexivoGracia=DetalleComplexivoConstantes.APROBADO_PROCESO_DE_TITULACION_LABEL;
//				vfFechaCargaGracia= vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaTeorico().toString().substring(0, 10) ;
//				if(vfEstudianteComplexivoDto.getMcttcrPorcentaje()==MecanismoTitulacionCarreraConstantes.PORCENTAJE_CIEN_POR_CIENTO_MECANISMO_TITULACION){
//					vfFechaPracticoGracia=null;
//					vfEstudianteComplexivoDto.setAsnoComplexivoPractico(null);
//					vfEstudianteComplexivoDto.setAsnoCmpGraciaPractico(null);
//					vfFechaPractico=null;
//				}else{
//					vfFechaPracticoGracia=vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaPractico().toString().substring(0, 10) ;
//					vfFechaPractico=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//				}
//			}else if(vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE
//					|| vfEstudianteComplexivoDto.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE){
//				vfVisualizarComplexivo=true;
//				vfVisualizarComplexivoGracia=true;
//				vfFechaCarga= vfEstudianteComplexivoDto.getAsnoFechaTeorico().toString().substring(0, 10) ;
//				int res=vfEstudianteComplexivoDto.getAsnoCmpGraciaFinal().compareTo(new BigDecimal(GeneralesConstantes.APP_ID_BASE));
//				if(res==0){
//					vfVisualizarComplexivoGracia=false;
//					vfResultadoComplexivo=DetalleComplexivoConstantes.GENERADA_ACTA_DE_GRADO_LABEL;
//				}else{
//					vfResultadoComplexivo=DetalleComplexivoConstantes.NO_APRUEBA_PRESENTARSE_AL_EXAMEN_DE_GRACIA_LABEL;
//					vfResultadoComplexivoGracia=DetalleComplexivoConstantes.GENERADA_ACTA_DE_GRADO_LABEL;
//				}
//				res=vfEstudianteComplexivoDto.getAsnoComplexivoPractico().compareTo(new BigDecimal(GeneralesConstantes.APP_ID_BASE));
//				if(res==0){
//					vfEstudianteComplexivoDto.setAsnoComplexivoPractico(null);
//				}
//				vfFechaCargaGracia= vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaTeorico().toString().substring(0, 10) ;
//				if(vfEstudianteComplexivoDto.getMcttcrPorcentaje()==MecanismoTitulacionCarreraConstantes.PORCENTAJE_CIEN_POR_CIENTO_MECANISMO_TITULACION){
//					vfFechaPracticoGracia=null;
//					vfEstudianteComplexivoDto.setAsnoComplexivoPractico(null);
//					vfEstudianteComplexivoDto.setAsnoCmpGraciaPractico(null);
//					vfFechaPractico=null;
//				}else{
//					vfFechaPracticoGracia=vfEstudianteComplexivoDto.getAsnoFechaCmpGraciaPractico().toString().substring(0, 10) ;
//					vfFechaPractico=vfEstudianteComplexivoDto.getPrtrFechaEjecucion().toString().substring(0, 10) ;
//				}
//				
//			}
//			
//		} catch (Exception e) {
//			vfVisualizarComplexivoGracia=false;
//			vfVisualizarComplexivo=false;
//		}
//		try {
//			if(vfEstudianteComplexivoDto.getMcttcrPorcentaje()==GeneralesConstantes.APP_ID_BASE){
//				vfVisualizarComplexivoGracia=false;
//				vfVisualizarComplexivo=false;	
//			}	
//		} catch (Exception e) {
//			vfVisualizarComplexivoGracia=false;
//			vfVisualizarComplexivo=false;	
//		}
		
		return "irVisualizarPostulacion";
	}

	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/

	/**
	 * permite llenar las carreras por la facultad seleccionada
	 */
	public void llenarCarrerasXfacultad(int facultadId){
		try {
			if(facultadId == GeneralesConstantes.APP_ID_BASE){
				vfListCarreras = new ArrayList<CarreraDto>();
				vfFacultadDto = new FacultadDto(GeneralesConstantes.APP_ID_BASE);
				vfCarreraDto = new CarreraDto(GeneralesConstantes.APP_ID_BASE);
			}else{
				vfListCarreras = servVfCarreraDtoJdbc.buscarXfacultad(facultadId);
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.llenar.carreras.por.facultad.exception")));
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			vfListCarreras = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.llenar.carreras.por.facultad.no.encontrado.exception")));
		}
	}
	
	//**************************************************************//
	//************** METODOS ACCESORES Y MUTADORES *****************//
	//**************************************************************//
	/**
	 * Lista las postulaciones del estudiante segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarPostulacion(){
		vfListEstudiantePostuladoJdbcDto = null;
		try {
			vfListEstudiantePostuladoJdbcDto = servVfEstudiantePostuladoDtoServicioJdbc.buscarPostulacionesVistaEstudiante(vfPersonaPostulante.getPrsIdentificacion(), vfFacultadDto.getFclId(), vfCarreraDto.getCrrCodSniese(), ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
		} catch (EstudiantePostuladoJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.listar.postulacion.facultad.carrera.identificacion.estado.exception")));
		} catch (EstudiantePostuladoJdbcDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.listar.postulacion.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		}
	}
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	public FacultadDto getVfFacultadDto() {
		return vfFacultadDto;
	}
	public void setVfFacultadDto(FacultadDto vfFacultadDto) {
		this.vfFacultadDto = vfFacultadDto;
	}
	public CarreraDto getVfCarreraDto() {
		return vfCarreraDto;
	}
	public void setVfCarreraDto(CarreraDto vfCarreraDto) {
		this.vfCarreraDto = vfCarreraDto;
	}
	public List<FacultadDto> getVfListFacultades() {
		vfListFacultades = vfListFacultades==null?(new ArrayList<FacultadDto>()):vfListFacultades;
		return vfListFacultades;
	}
	public void setVfListFacultades(List<FacultadDto> vfListFacultades) {
		this.vfListFacultades = vfListFacultades;
	}
	public List<CarreraDto> getVfListCarreras() {
		vfListCarreras = vfListCarreras==null?(new ArrayList<CarreraDto>()):vfListCarreras;
		return vfListCarreras;
	}
	public void setVfListCarreras(List<CarreraDto> vfListCarreras) {
		this.vfListCarreras = vfListCarreras;
	}
	public List<EstudiantePostuladoJdbcDto> getVfListEstudiantePostuladoJdbcDto() {
		vfListEstudiantePostuladoJdbcDto = vfListEstudiantePostuladoJdbcDto==null?(new ArrayList<EstudiantePostuladoJdbcDto>()):vfListEstudiantePostuladoJdbcDto;
		return vfListEstudiantePostuladoJdbcDto;
	}
	public void setVfListEstudiantePostuladoJdbcDto(
			List<EstudiantePostuladoJdbcDto> vfListEstudiantePostuladoJdbcDto) {
		this.vfListEstudiantePostuladoJdbcDto = vfListEstudiantePostuladoJdbcDto;
	}
	public Convocatoria getVfCnvActiva() {
		return vfCnvActiva;
	}
	public void setVfCnvActiva(Convocatoria vfCnvActiva) {
		this.vfCnvActiva = vfCnvActiva;
	}
	public Persona getVfPersonaPostulante() {
		return vfPersonaPostulante;
	}
	public void setVfPersonaPostulante(Persona vfPersonaPostulante) {
		this.vfPersonaPostulante = vfPersonaPostulante;
	}
	public EstudiantePostuladoJdbcDto getVfEstudiantePostuladoJdbcDto() {
		return vfEstudiantePostuladoJdbcDto;
	}
	public void setVfEstudiantePostuladoJdbcDto(
			EstudiantePostuladoJdbcDto vfEstudiantePostuladoJdbcDto) {
		this.vfEstudiantePostuladoJdbcDto = vfEstudiantePostuladoJdbcDto;
	}
	public UbicacionDto getVfPaisResidencia() {
		return vfPaisResidencia;
	}
	public void setVfPaisResidencia(UbicacionDto vfPaisResidencia) {
		this.vfPaisResidencia = vfPaisResidencia;
	}
	public UbicacionDto getVfProvinciaResidencia() {
		return vfProvinciaResidencia;
	}
	public void setVfProvinciaResidencia(UbicacionDto vfProvinciaResidencia) {
		this.vfProvinciaResidencia = vfProvinciaResidencia;
	}
	public UbicacionDto getVfCantonResidencia() {
		return vfCantonResidencia;
	}
	public void setVfCantonResidencia(UbicacionDto vfCantonResidencia) {
		this.vfCantonResidencia = vfCantonResidencia;
	}

	public EstudianteValidacionJdbcDto getVfEstudianteValidacionJdbcDto() {
		return vfEstudianteValidacionJdbcDto;
	}

	public void setVfEstudianteValidacionJdbcDto(
			EstudianteValidacionJdbcDto vfEstudianteValidacionJdbcDto) {
		this.vfEstudianteValidacionJdbcDto = vfEstudianteValidacionJdbcDto;
	}

	public String getCulmino_malla() {
		return culmino_malla;
	}

	public void setCulmino_malla(String culmino_malla) {
		this.culmino_malla = culmino_malla;
	}

	public String getReprobo_complexivo() {
		return reprobo_complexivo;
	}

	public void setReprobo_complexivo(String reprobo_complexivo) {
		this.reprobo_complexivo = reprobo_complexivo;
	}

	public String getAsignado_tutor() {
		return asignado_tutor;
	}

	public void setAsignado_tutor(String asignado_tutor) {
		this.asignado_tutor = asignado_tutor;
	}

	public String getUltimo_semestre() {
		return ultimo_semestre;
	}

	public void setUltimo_semestre(String ultimo_semestre) {
		this.ultimo_semestre = ultimo_semestre;
	}

//	public EstudianteEvaluacionJdbcDto getVfEstudianteEvaluacionJdbcDto() {
//		return vfEstudianteEvaluacionJdbcDto;
//	}
//
//	public void setVfEstudianteEvaluacionJdbcDto(
//			EstudianteEvaluacionJdbcDto vfEstudianteEvaluacionJdbcDto) {
//		this.vfEstudianteEvaluacionJdbcDto = vfEstudianteEvaluacionJdbcDto;
//	}

	public boolean isVfVisualizarTemaTutor() {
		return vfVisualizarTemaTutor;
	}

	public void setVfVisualizarTemaTutor(boolean vfVisualizarTemaTutor) {
		this.vfVisualizarTemaTutor = vfVisualizarTemaTutor;
	}

//	public EstudianteAptitudJdbcDto getVfEstudianteAptitudJdbcDto() {
//		return vfEstudianteAptitudJdbcDto;
//	}
//
//	public void setVfEstudianteAptitudJdbcDto(
//			EstudianteAptitudJdbcDto vfEstudianteAptitudJdbcDto) {
//		this.vfEstudianteAptitudJdbcDto = vfEstudianteAptitudJdbcDto;
//	}
//
//	public EstudianteDetalleComplexivoDto getVfEstudianteComplexivoDto() {
//		return vfEstudianteComplexivoDto;
//	}

//	public void setVfEstudianteComplexivoDto(
//			EstudianteDetalleComplexivoDto vfEstudianteComplexivoDto) {
//		this.vfEstudianteComplexivoDto = vfEstudianteComplexivoDto;
//	}

	public boolean isVfVisualizarComplexivo() {
		return vfVisualizarComplexivo;
	}

	public void setVfVisualizarComplexivo(boolean vfVisualizarComplexivo) {
		this.vfVisualizarComplexivo = vfVisualizarComplexivo;
	}

	public String getVfFechaCarga() {
		return vfFechaCarga;
	}

	public void setVfFechaCarga(String vfFechaCarga) {
		this.vfFechaCarga = vfFechaCarga;
	}

//	public EstudianteDetalleComplexivoDto getVfEstudianteComplexivoPracticoDto() {
//		return vfEstudianteComplexivoPracticoDto;
//	}
//
//	public void setVfEstudianteComplexivoPracticoDto(
//			EstudianteDetalleComplexivoDto vfEstudianteComplexivoPracticoDto) {
//		this.vfEstudianteComplexivoPracticoDto = vfEstudianteComplexivoPracticoDto;
//	}

	public String getVfFechaPractico() {
		return vfFechaPractico;
	}

	public void setVfFechaPractico(String vfFechaPractico) {
		this.vfFechaPractico = vfFechaPractico;
	}

	public String getVfResultadoComplexivo() {
		return vfResultadoComplexivo;
	}

	public void setVfResultadoComplexivo(String vfResultadoComplexivo) {
		this.vfResultadoComplexivo = vfResultadoComplexivo;
	}

	public boolean isVfVisualizarComplexivoGracia() {
		return vfVisualizarComplexivoGracia;
	}

	public void setVfVisualizarComplexivoGracia(boolean vfVisualizarComplexivoGracia) {
		this.vfVisualizarComplexivoGracia = vfVisualizarComplexivoGracia;
	}

	public String getVfFechaCargaGracia() {
		return vfFechaCargaGracia;
	}

	public void setVfFechaCargaGracia(String vfFechaCargaGracia) {
		this.vfFechaCargaGracia = vfFechaCargaGracia;
	}

	public String getVfFechaPracticoGracia() {
		return vfFechaPracticoGracia;
	}

	public void setVfFechaPracticoGracia(String vfFechaPracticoGracia) {
		this.vfFechaPracticoGracia = vfFechaPracticoGracia;
	}

	public String getVfResultadoComplexivoGracia() {
		return vfResultadoComplexivoGracia;
	}

	public void setVfResultadoComplexivoGracia(String vfResultadoComplexivoGracia) {
		this.vfResultadoComplexivoGracia = vfResultadoComplexivoGracia;
	}
	
	
	
}

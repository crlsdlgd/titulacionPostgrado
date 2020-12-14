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
   
 ARCHIVO:     PostulacionForm.java	  
 DESCRIPCION: Bean de sesion que maneja la postulación de los usuarios. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.postulacionUnidadTitulacion;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PostulacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PostulacionDtoValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PostulacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.FacultadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.MecanismoTitulacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TramiteTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ConvocatoriaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (session bean) RegistroUsuarioForm.
 * Bean de sesion que maneja la postulación de los usuarios.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="postulacionForm")
@SessionScoped
public class PostulacionForm implements Serializable {
	private static final long serialVersionUID = -7885786472838841602L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	
	private PostulacionDto pfPostulante;
	private FacultadDto pfFacultad;
	private CarreraDto pfCarrera;
	private List<FacultadDto> pfListFacultades;
	private List<CarreraDto> pfListCarreras;
	private List<UbicacionDto> pfListPaises;
	private List<UbicacionDto> pfListProvincias;
	private List<UbicacionDto> pfListCantones;
	private List<InstitucionAcademicaDto> pfListUniversidades;
	private List<TituloDto> pfListTitulosBachilleres;
	private Persona pfPersonaPostulante;
	private Integer pfPaisResidenciaId;
	private Integer pfProvinciaResidenciaId;
	private Convocatoria pfCnvActiva;
	private boolean pfHabilitadorGuardar;
	private EstudiantePostuladoJdbcDto pfInfoPostulante;
	private boolean pfHabilitadorFechaEgresamiento;
	private Integer pfSeleccionEgresamiento;
	private Integer pfValidadorClic;
	private Integer pfHabilitadorNombreuniversidad;
	private UploadedFile uploadedPicture;
	private String rutaArchivo;
	private DefaultStreamedContent image;
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){

	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	FacultadDtoServicioJdbc servPfFacultadDtoJdbc;
	@EJB
	CarreraDtoServicioJdbc servPfCarreraDtoJdbc;
	@EJB
	PersonaServicio servPfPersona;
	@EJB
	InstitucionAcademicaDtoServicioJdbc servPfIntitucionAcademicaDtoJdbc;
	@EJB
	UbicacionDtoServicioJdbc servPfUbicacionDtoJdbc;
	@EJB
	MecanismoTitulacionDtoServicioJdbc  servPfMecanismoTitulacionDtoJdbc;
	@EJB
	ConvocatoriaServicio servPfConvocatoria;
	@EJB
	PostulacionDtoServicio servPfPostulacionDtoJdbc;
	@EJB
	UsuarioServicio servPfUsuario;
	@EJB
	TituloDtoServicioJdbc servPfTituloDtoJdbc;
	@EJB
	TramiteTituloDtoServicioJdbc servTramiteTituloDtoServicioJdbc;
	@EJB 
	EstudianteValidacionDtoServicioJdbc servEstudianteValidacionDtoServicioJdbc;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	/**
	 * Maneja de inicio del bean
	 * @return - cadena de navegacion a la pagina de postulación
	 */
	public String iniciarPostulacion(Usuario usuario){
		pfValidadorClic=1;
		pfHabilitadorNombreuniversidad = 0;
		try {
			//busqueda de convocatoria activa
			pfCnvActiva = servPfConvocatoria.buscarPorIdPorEstadoFaseRegistro(ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
			
			if(pfCnvActiva.getCnvEstadoFase().intValue() == ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_VALUE){
				
				//asigno la persona por el usuario ingresado
				pfPersonaPostulante = servPfPersona.buscarPorIdentificacion(usuario.getUsrIdentificacion());	
				
				//ELIMINO LAS ASIGNACIONES A PAIS Y PROVINCIA
				pfPaisResidenciaId = null;
				pfProvinciaResidenciaId = null;
				
				//Instancio el objeto de postulante
				pfPostulante = new PostulacionDto();
				
				//Instancio el objeto facultad
				pfFacultad = new FacultadDto();
				//Instancio el objeto carrera
				pfCarrera = new CarreraDto();
				
				//Busco las facultades en bd
				pfListFacultades = servPfFacultadDtoJdbc.listarTodos();
				
				//Busco las universidades en bd
				pfListUniversidades = servPfIntitucionAcademicaDtoJdbc.listarXNivel(InstitucionAcademicaConstantes.NIVEL_UNIVERSIDAD_VALUE);
//				for (InstitucionAcademicaDto item: pfListUniversidades) {//quito la central de la lista
//					if(item.getInacId() == InstitucionAcademicaConstantes.UNIVERSIDAD_CENTRAL_DEL_ECUADOR_VALUE){
//						pfListUniversidades.remove(item);
//						break;
//					}
//				}
//				//elimina la instancia de pais
				pfPaisResidenciaId = null;
				pfProvinciaResidenciaId = null;
				//Busco los paises en bd
				pfListPaises = servPfUbicacionDtoJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
				
				//Busco los titulos de bachillerato
//				pfListTitulosBachilleres = servPfTituloDtoJdbc.listarXtipo(TituloConstantes.TIPO_TITULO_UNIVERSIDAD_VALUE);
				
			}else{
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.no.fase.postulacion")));
				return null;
			}
		
		
		} catch (FacultadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.facultad.dto.exception")));
			return null;
		} catch (FacultadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.facultad.dto.no.encontrado.exception")));
			return null;
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.persona.no.encontrado.exception")));
			return null;
		} catch (PersonaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.persona.exception")));
			return null;
		} catch (InstitucionAcademicaDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.inac.dto.exception")));
			return null;
		} catch (InstitucionAcademicaDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.inac.dto.no.encontrado.exception")));
			return null;
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.ubicacion.dto.exception")));
			return null;
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.ubicacion.dto.no.encontrado.exception")));
			return null;
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.convocatoria.no.encontrado.exception")));
			return null;
		} catch (ConvocatoriaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.convocatoria.exception")));
			return null;
//		} catch (TituloDtoJdbcException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.titulo.exception")));
//			return null;
//		} catch (TituloDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.iniciar.postulacion.titulo.no.encontrado.exception")));
//			return null;
		}
//		} catch (TituloDtoJdbcException e) {
//			FacesUtil.mensajeError("errrrooor titulo exception");
//			return null;
//		} catch (TituloDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError("errrrooor titulo no encontrado exception");
//			return null;
//		}
		pfSeleccionEgresamiento=null;
		pfHabilitadorFechaEgresamiento=true;
		pfHabilitadorGuardar=true;
		return "iniciarPostulacion";
	}
	


	/**
	 * Maneja de cancelacion de registro
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarPostulacion(){
		pfHabilitadorGuardar=true;
		pfHabilitadorGuardar = true;
		pfPostulante = null;
		pfListFacultades = null;
		pfFacultad = null;
		pfPersonaPostulante = null;
		pfCarrera = null;
		pfListCarreras = null;
		pfListPaises = null;
		pfListTitulosBachilleres = null;
		return "cancelarPostulacion";
		
	}
	
	/**
	 * Maneja la funcionalidad para la postulación del usuario
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String postular(){
		String retorno = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		Date fechaInicioPostulaciones= null;
//		try {
//			fechaInicioPostulaciones = sdf.parse(GeneralesConstantes.APP_FECHA_MAXIMA_POSTULACION);
//		} catch (ParseException e1) {
//		}
		if(pfPostulante.getPrsFoto()!=null){
			
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor debe cargar el archivo de la fotografía.");
			return null;
		}
			
			try {
				
			
				//si el país de residencia no es ecuador se debe guardar en cantón residencia, el país del que reside, caso contrario el cantón que seleccione
				if(pfPaisResidenciaId != UbicacionConstantes.ECUADOR_VALUE){
					pfPostulante.setFcesUbicacionResidencia(pfPaisResidenciaId);
				}
					
				
				// seteo el estado del tramite titulo que se esta generando
				pfPostulante.setTrttEstadoTramite(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
				// seteo el estado del proceso 
				pfPostulante.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_VALUE);
				// seteo la convocatoria al tramite titulo
				pfPostulante.setTrttCnvId(pfCnvActiva.getCnvId());
				
				//seteo el tipo de proceso para la table proceso tramite
				pfPostulante.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_POSTULACION_VALUE);
				//seteo la fecha de ejecucion de la postulacion
				pfPostulante.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
							//seteo la fecha creacion de la ficha estudiante
					pfPostulante.setFcesFechaCreacion(new Timestamp((new Date()).getTime()));
					//guardo la persona en la ficha estudiante dto
					pfPostulante.setFcesPrsId(pfPersonaPostulante.getPrsId());
					
					//busco el codigo de la carrera por el codigo sniese
					CarreraDto carrera =  servPfCarreraDtoJdbc.buscarXId(pfCarrera.getCrrId());
					pfPostulante.setTrttCrrCarreraId(carrera.getCrrId());
					
					
					//inserto los datos en la bd
					servPfPostulacionDtoJdbc.anadir(pfPostulante, carrera, pfPersonaPostulante);
					
					//busco la facultad a la que se postuló
					String facultad= new String();
					for (FacultadDto item : pfListFacultades) {
						if(item.getFclId() == pfFacultad.getFclId()){
							facultad = GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
							break;
						}
					}
					
					Convocatoria cnvAux = servPfConvocatoria.buscarConvocatoriaActivaFaseRegistro();
					
					
					//******************************************************************************
					//************************* ACA INICIA EL ENVIO DE MAIL ************************
					//******************************************************************************
					//defino los datos para la plantilla
					Map<String, Object> parametros = new HashMap<String, Object>();
					
					parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(pfPersonaPostulante.getPrsNombres().toUpperCase()));
					StringBuilder sb = new StringBuilder();
					sb.append(GeneralesUtilidades.generaStringParaCorreo(pfPersonaPostulante.getPrsPrimerApellido().toUpperCase()));
					sb.append(" ");
					try {
						sb.append(GeneralesUtilidades.generaStringParaCorreo(pfPersonaPostulante.getPrsSegundoApellido().toUpperCase()));
						sb.append(" ");
					} catch (Exception e) {
						// TODO: handle exception
					}
					parametros.put("apellidos", sb.toString());
					sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
					parametros.put("fechaHora", sdf.format(new Date()));
					parametros.put("facultad", facultad);
					parametros.put("carrera", GeneralesUtilidades.generaStringParaCorreo(carrera.getCrrDescripcion()));
					parametros.put("convocatoria", cnvAux.getCnvDescripcion());
					//lista de correos a los que se enviara el mail
					List<String> correosTo = new ArrayList<>();
					
					correosTo.add(pfPersonaPostulante.getPrsMailPersonal());
					
					//path de la plantilla del mail
					String pathTemplate = "/ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-postulacion.vm";
					
					//llamo al generador de mails
					GeneradorMails genMail = new GeneradorMails();
					String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_POSTULACION, 
							GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
					//****envio el mail a la cola
					//cliente web service
					Client client = ClientBuilder.newClient();
					WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
					MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
					postForm.add("mail", mailjsonSt);
		//			String responseData = target.request().post(Entity.form(postForm),String.class);
					target.request().post(Entity.form(postForm),String.class);
					
					//******************************************************************************
					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
					//******************************************************************************
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.postular.exitoso")));
					retorno = "irInicio";
				} catch (PostulacionDtoValidacionException e) {
					e.printStackTrace();
					FacesUtil.mensajeError("Excepción de validación al guardar la postulación, intente más tarde");
					return null;
				} catch (Exception e){
					e.printStackTrace();
					FacesUtil.mensajeError("Excepción al guardar la postulación, intente más tarde");
					return null;
				}
		return retorno;
	}


			
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	/**
	 * permite llenar las carreras por la facultad seleccionada
	 */
	public void llenarCarrerasXfacultad(int facultadId){
		try {
			pfListCarreras = servPfCarreraDtoJdbc.buscarXfacultadId(facultadId);
			
			
			pfHabilitadorGuardar=false;
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.carreras.por.facultad.exception")));
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			pfListCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.carreras.por.facultad.no.encontrado.exception")));
		}
	
	}
	
	/**
	 * permite verificar si el estudiante ya ha egresado y se requiere la fecha de egresamiento
	 */
	public void responderEgresamiento(){
		if(pfSeleccionEgresamiento==0){
			pfHabilitadorFechaEgresamiento=false;	
		}else{
			pfHabilitadorFechaEgresamiento=true;
		}
	}
	
	/**
	 * permite verificar si el estudiante ya se ha postulado a la carrera seleccionada y aún está activa su postulación
	 */
	public void buscarPostulacionActiva(){
		try {
			pfInfoPostulante = null;
			pfInfoPostulante = servPfCarreraDtoJdbc.buscarCarrerasPostulacionesXCedula(pfPersonaPostulante.getPrsIdentificacion(),pfCarrera.getCrrId());
			if(pfInfoPostulante!=null){
				pfHabilitadorGuardar=true;
				try {
					switch(pfInfoPostulante.getTrttEstadoProceso()){
					case 1:
						EstudianteValidacionJdbcDto estudianteValidacionJdbcDtoAux = new EstudianteValidacionJdbcDto();
						EstudiantePostuladoJdbcDto postulanteAux = new EstudiantePostuladoJdbcDto();
						postulanteAux.setTrttId(pfInfoPostulante.getTrttId());
						try {
							estudianteValidacionJdbcDtoAux=servEstudianteValidacionDtoServicioJdbc.buscarValidacionXEstudianteIdoneidad(postulanteAux);
						} catch (
								EstudiantePostuladoJdbcDtoException
								| EstudiantePostuladoJdbcDtoNoEncontradoException e) {
						}
						if(estudianteValidacionJdbcDtoAux.getVldRslIdoneidad()==0){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.estudiante.declarado.idoneo.exception")));	
							break;
						}else{
							pfHabilitadorGuardar=false;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.estudiante.declarado.no.idoneo.exception")));	
							break;
						}
					case 5:
					case 7:
						pfHabilitadorGuardar=false;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.estudiante.declarado.no.apto.exception")));	
						break;
					// Reprobado complexivo de gracia
					case 14:
						pfHabilitadorGuardar=false;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.estudiante.reprobado.examen.complexivo.exception")));	
						break;
					// Aprobados proceso de titulación	
					case 100:
					case 101:
					case 102:
					case 103:
					case 104:
					case 105:
					case 106:
					case 107:
					case 108:
					case 109:
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.estudiante.titulado.exception")));	
						break;
						
					default:
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.estudiante.ya.postulado.en.carrera.exception")));	
						break;
					}
				} catch (NullPointerException e) {
					pfHabilitadorGuardar=false;
				}
			}else{
				pfHabilitadorGuardar=false;
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.carreras.por.facultad.exception")));
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			pfHabilitadorGuardar=false;
		}
	}
	
	public String verificarClickAceptarTramiteNo(){
		pfValidadorClic = 0;
		return null;
	}
	
	
	/**Llena las provincias por el el id de pais
	 * @param idPais - id del pais 
	 */
	public void llenarProvinciasXPais(int idPais){
		try {
			pfListProvincias = servPfUbicacionDtoJdbc.listaProvinciaXPais(idPais);
		} catch (UbicacionDtoJdbcException e) {
			pfListProvincias = null;
			pfListCantones = null;
			if(idPais == UbicacionConstantes.ECUADOR_VALUE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.provincias.por.pais.exception")));	
			}
			
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			pfListProvincias = null;
			pfListCantones = null;
			if(idPais == UbicacionConstantes.ECUADOR_VALUE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.provincias.por.pais.no.encontrado.exception")));	
			}
			
		}
	}
	
	/**Carga cantones por provincia
	 * @param idPais - id del pais 
	 */
	public void llenarCantonesXProvincias(int idProvincias){
		try {
			pfListCantones = servPfUbicacionDtoJdbc.listaCatonXProvincia(idProvincias);
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.cantones.por.provincias.exception")));
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.cantones.por.provincias.no.encontrado.exception")));
		}
	}

	/** Verifica la universidadId que se ha seleccionado
	 * @param idPais - id del pais 
	 */
	public void verificarIES(){
		pfPostulante.setPstNombreUniversidad(null);
		if(pfPostulante.getPstUniversidadId()==InstitucionAcademicaConstantes.IES_VALUE){
			pfHabilitadorNombreuniversidad=1;
		}else{
			pfHabilitadorNombreuniversidad=0;
		}
	}
	
	
	
	public void cargarImagen(FileUploadEvent event){
		
		byte[] file = event.getFile().getContents();
		String contentType = event.getFile().getContentType();
		String extension =  event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf('.'), event.getFile().getFileName().length()); ;
		pfPostulante.setPrsFoto(file);
		pfPostulante.setPrsUbicacionFoto(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+pfPersonaPostulante.getPrsIdentificacion()+extension) ;
		image = new DefaultStreamedContent(new ByteArrayInputStream(file), contentType);
		try (FileOutputStream fos = new FileOutputStream(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+pfPersonaPostulante.getPrsIdentificacion()+extension)) {
			   fos.write(file);
			   fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
	}
	  
	
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	
	public PostulacionDto getPfPostulante() {
		return pfPostulante;
	}

	public void setPfPostulante(PostulacionDto pfPostulante) {
		this.pfPostulante = pfPostulante;
	}
	
	public List<FacultadDto> getPfListFacultades() {
		pfListFacultades = pfListFacultades==null?(new ArrayList<FacultadDto>()):pfListFacultades;
		return pfListFacultades;
	}

	public void setPfListFacultades(List<FacultadDto> pfListFacultades) {
		this.pfListFacultades = pfListFacultades;
	}

	public List<CarreraDto> getPfListCarreras() {
		pfListCarreras = pfListCarreras==null?(new ArrayList<CarreraDto>()):pfListCarreras;
		return pfListCarreras;
	}

	public void setPfListCarreras(List<CarreraDto> pfListCarreras) {
		this.pfListCarreras = pfListCarreras;
	}

	public List<UbicacionDto> getPfListPaises() {
		pfListPaises = pfListPaises==null?(new ArrayList<UbicacionDto>()):pfListPaises;
		return pfListPaises;
	}

	public void setPfListPaises(List<UbicacionDto> pfListPaises) {
		this.pfListPaises = pfListPaises;
	}

	public FacultadDto getPfFacultad() {
		return pfFacultad;
	}

	public void setPfFacultad(FacultadDto pfFacultad) {
		this.pfFacultad = pfFacultad;
	}

	public CarreraDto getPfCarrera() {
		return pfCarrera;
	}

	public void setPfCarrera(CarreraDto pfCarrera) {
		this.pfCarrera = pfCarrera;
	}

	public Persona getPfPersonaPostulante() {
		return pfPersonaPostulante;
	}

	public void setPfPersonaPostulante(Persona pfPersonaPostulante) {
		this.pfPersonaPostulante = pfPersonaPostulante;
	}

	public List<InstitucionAcademicaDto> getPfListUniversidades() {
		pfListUniversidades = pfListUniversidades==null?(new ArrayList<InstitucionAcademicaDto>()):pfListUniversidades;
		return pfListUniversidades;
	}

	public void setPfListUniversidades(
			List<InstitucionAcademicaDto> pfListUniversidades) {
		this.pfListUniversidades = pfListUniversidades;
	}

	public Integer getPfPaisResidenciaId() {
		return pfPaisResidenciaId;
	}

	public void setPfPaisResidenciaId(Integer pfPaisResidenciaId) {
		this.pfPaisResidenciaId = pfPaisResidenciaId;
	}

	public Integer getPfProvinciaResidenciaId() {
		return pfProvinciaResidenciaId;
	}

	public void setPfProvinciaResidenciaId(Integer pfProvinciaResidenciaId) {
		this.pfProvinciaResidenciaId = pfProvinciaResidenciaId;
	}

	public List<UbicacionDto> getPfListProvincias() {
		pfListProvincias = pfListProvincias==null?(new ArrayList<UbicacionDto>()):pfListProvincias;
		return pfListProvincias;
	}

	public void setPfListProvincias(List<UbicacionDto> pfListProvincias) {
		this.pfListProvincias = pfListProvincias;
	}

	public List<UbicacionDto> getPfListCantones() {
		pfListCantones = pfListCantones==null?(new ArrayList<UbicacionDto>()):pfListCantones;
		return pfListCantones;
	}

	public void setPfListCantones(List<UbicacionDto> pfListCantones) {
		this.pfListCantones = pfListCantones;
	}

	public List<TituloDto> getPfListTitulosBachilleres() {
		return pfListTitulosBachilleres;
	}

	public void setPfListTitulosBachilleres(List<TituloDto> pfListTitulosBachilleres) {
		pfListTitulosBachilleres = pfListTitulosBachilleres==null?(new ArrayList<TituloDto>()):pfListTitulosBachilleres;
		this.pfListTitulosBachilleres = pfListTitulosBachilleres;
	}

	public boolean isPfHabilitadorGuardar() {
		return pfHabilitadorGuardar;
	}

	public void setPfHabilitadorGuardar(boolean pfHabilitadorGuardar) {
		this.pfHabilitadorGuardar = pfHabilitadorGuardar;
	}

	public EstudiantePostuladoJdbcDto getPfInfoPostulante() {
		return pfInfoPostulante;
	}

	public void setPfInfoPostulante(EstudiantePostuladoJdbcDto pfInfoPostulante) {
		this.pfInfoPostulante = pfInfoPostulante;
	}

	public boolean isPfHabilitadorFechaEgresamiento() {
		return pfHabilitadorFechaEgresamiento;
	}

	public void setPfHabilitadorFechaEgresamiento(
			boolean pfHabilitadorFechaEgresamiento) {
		this.pfHabilitadorFechaEgresamiento = pfHabilitadorFechaEgresamiento;
	}

	public Integer getPfSeleccionEgresamiento() {
		return pfSeleccionEgresamiento;
	}

	public void setPfSeleccionEgresamiento(Integer pfSeleccionEgresamiento) {
		this.pfSeleccionEgresamiento = pfSeleccionEgresamiento;
	}

	public Integer getPfValidadorClic() {
		return pfValidadorClic;
	}

	public void setPfValidadorClic(Integer pfValidadorClic) {
		this.pfValidadorClic = pfValidadorClic;
	}



	public Integer getPfHabilitadorNombreuniversidad() {
		return pfHabilitadorNombreuniversidad;
	}



	public void setPfHabilitadorNombreuniversidad(Integer pfHabilitadorNombreuniversidad) {
		this.pfHabilitadorNombreuniversidad = pfHabilitadorNombreuniversidad;
	}



	public UploadedFile getUploadedPicture() {
		return uploadedPicture;
	}



	public void setUploadedPicture(UploadedFile uploadedPicture) {
		this.uploadedPicture = uploadedPicture;
	}



	public String getRutaArchivo() {
		return rutaArchivo;
	}



	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}



	public DefaultStreamedContent getImage() {
		return image;
	}



	public void setImage(DefaultStreamedContent image) {
		this.image = image;
	}
	
	
	
}

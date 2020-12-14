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
   
 ARCHIVO:     AsignacionMecanismoForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de asignación de mecanismo de evaluador.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
24-04-2018			Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.evaluacion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import ec.edu.uce.envioMail.excepciones.ValidacionMailException;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAsignacionMecanismoDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EstudianteValidacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.MecanismoTitulacionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteAsignacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ReportesDtoJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (managed bean) AsignacionMecanismoForm.
 * Managed Bean que maneja las peticiones de asignación de mecanismo de evaluador.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="asignacionMecanismoForm")
@SessionScoped
public class AsignacionMecanismoForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
		
	// *****************************************************************/
	// ******************* Variables de AsignacionMecanismoForm ********/
	// *****************************************************************/
	
	private EstudianteAsignacionMecanismoDto amfEstudianteAsignacionMecanismoDtoBuscar;
	private EstudianteAsignacionMecanismoDto amfEstudianteAsignacionMecanismoDtoEditar;
	private CarreraDto amfCarrera;
	private List<CarreraDto> amfListCarreras;
	private Convocatoria amfConvocatoria;
	private List<Convocatoria> amfListConvocatorias;
	private Usuario amfUsuarioQueCambia;
	private List<EstudianteAsignacionMecanismoDto> amfListEstudianteAsignacionMecanismoDto;
	private Persona amfPersonaPostulante;
	private List<MecanismoCarrera> amfListMecanismoTitulacionCarrera;

	private MecanismoCarrera amfMecanismoTitulacionCarrera;
	private List<DocenteTutorTribunalLectorJdbcDto> amfListDocentesDto;
	private String amfMensajeTutor;
	private String cedula;
	private String amfApellidoDocenteBuscar;
	private Boolean amfHabilitadorAsignar;
	
	private int fechaRegimenPostulacion;
	private int habilitarCampos;
	private Integer amfValidadorClic;
	private Integer amfValidadorClicTutor;
	private Integer amfValidadorClicDirector;
	private Boolean amfHabilitadorGuardar;
	private String cedulaTutor;
	private String cedulaDirector;
	private Integer seleccion;
	private Boolean amfDeshabilitadoTutor;
	private InputStream atfIs;
	private String atfNombreArchivo;
	private Integer amfExamenComplexivoValue;
	
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private ConvocatoriaServicio srvConvocatoriaServicio;
		
	@EJB
	private ReportesDtoJdbc srvReportesDtoJdbc;
	
	@EJB
	private UsuarioRolServicio srvUsuarioRolServicio;

	@EJB
	private FacultadServicio srvFacultadServicio;
		
	@EJB
	private CarreraServicio srvCarreraServicio;
	
	@EJB
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
	
	@EJB
	EstudianteAsignacionDtoServicioJdbc servAmfEstudianteAsignacionDtoServicioJdbc;
	
	@EJB
	EstudianteValidacionDtoServicio servAmfEstudianteValidacionDtoServicio;
	
	@EJB
	private MecanismoTitulacionCarreraServicio servAmfMecanismoTitulacionCarreraServicio;
	
	@EJB
	private DocenteDtoServicioJdbc srvDocenteDtoServicioJdbc;

	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	//****************************************************************/
	//******** METODOS DE NAVEGACION ********** **********************/
	//****************************************************************/
	
	/**
	 * Método que dirige a la página de Validación
	 * @param usuario
	 * @return
	 */
	public String irListarAsignacionMecanismos(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacionEvaluador(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
				return null;
			}
			iniciarParametros();
			amfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			amfListCarreras=servAmfEstudianteAsignacionDtoServicioJdbc.buscarCarrerasXEvaluador(usuario.getUsrIdentificacion());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (SQLException e) {
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No se encontró ninguna convocatoria ");
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		amfUsuarioQueCambia = usuario;
		amfValidadorClic = 0;
		return "irListarAsignacionMecanismos";
	}
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarValidacion(){
		amfPersonaPostulante = null;
		return "irInicio";
	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarMecanismoTitulacion(){
		amfListEstudianteAsignacionMecanismoDto=null;
		amfEstudianteAsignacionMecanismoDtoEditar.setAssttTutor(null);
		amfEstudianteAsignacionMecanismoDtoEditar.setAssttTemaTrabajo(null);
		try {
			MecanismoCarrera mcttcrAux;
			mcttcrAux = servAmfMecanismoTitulacionCarreraServicio.buscarPorId(amfEstudianteAsignacionMecanismoDtoEditar.getFcesMecanismoTitulacionCarrera());
			if(	mcttcrAux.getMccrMecanismoTitulacion().getMcttId() != MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE){
				amfHabilitadorAsignar=false;
				amfHabilitadorGuardar=true;
			}else{
				amfHabilitadorAsignar=true;
				amfHabilitadorGuardar=false;
			}
		} catch (Exception e) {
		}
	}
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		amfListEstudianteAsignacionMecanismoDto=null;
		amfHabilitadorAsignar=true;
//		amfValidadorClic = 0;
		amfHabilitadorAsignar=true;
		amfEstudianteAsignacionMecanismoDtoBuscar = null;
		iniciarParametros();
	}
	
	public void limpiarModal(){
		amfListDocentesDto=null;
		amfApellidoDocenteBuscar=null;
		cedula=null;
	}
	
	/**
	 * Lista los estudiantes validados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarValidados(Usuario usuario){
		try {
			amfListEstudianteAsignacionMecanismoDto = servAmfEstudianteAsignacionDtoServicioJdbc.buscarEstudiantesXAsignarXIndetificacionXCarreraXConvocatoria(amfEstudianteAsignacionMecanismoDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion(),amfCarrera , amfConvocatoria.getCnvId());
		} catch (EstudianteValidacionDtoException e) {
			FacesUtil.mensajeInfo(e.getMessage());
		} catch (EstudianteValidacionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public String irCancelarPostulacion(){
		limpiar();
		return "irListarAsignacionMecanismos";
	}

	public String irAsignacionMecanismo(EstudianteAsignacionMecanismoDto estudiante, Usuario usuario){
		this.amfEstudianteAsignacionMecanismoDtoEditar = estudiante;
		amfUsuarioQueCambia=usuario;
		amfValidadorClic = 0;
		amfHabilitadorAsignar=true;
		amfEstudianteAsignacionMecanismoDtoEditar.setFcesMecanismoTitulacionCarrera(GeneralesConstantes.APP_ID_BASE);
		try {
			amfListMecanismoTitulacionCarrera = servAmfMecanismoTitulacionCarreraServicio.listaMccttXcarrera(estudiante.getTrttCarreraId());
			
			// Recorro la lista para encontrar el id del Examen Complexivo para esa carrera
			for (MecanismoCarrera item : amfListMecanismoTitulacionCarrera) {
				if(item.getMccrMecanismoTitulacion().getMcttDescripcion().equals(ValidacionConstantes.EXAMEN_COMPLEXIVO_LABEL)){
					// Guardo el valor del id del mecanismo de examen complexivo
					amfExamenComplexivoValue=item.getMccrId();
				}
			}
			if(estudiante.getTrttObsValSecSplit().equals(ValidacionConstantes.EXAMEN_COMPLEXIVO_IDONEO_LABEL)){
				Iterator<MecanismoCarrera> listaIterator = amfListMecanismoTitulacionCarrera.iterator();
				listaIterator = amfListMecanismoTitulacionCarrera.iterator();
				while(listaIterator.hasNext()){
					MecanismoCarrera elemento = listaIterator.next();
					if(!elemento.getMccrMecanismoTitulacion().getMcttDescripcion().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)){
						listaIterator.remove();
//					}else{
//						
//					}
//					if(elemento.getMcttcrMecanismoTitulacion().getMcttDescripcion().toString() != MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL){
						
					}
				}	
			}
			if(estudiante.getTrttObsValSecSplit().equals(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL)){
				Iterator<MecanismoCarrera> listaIterator = amfListMecanismoTitulacionCarrera.iterator();
				listaIterator = amfListMecanismoTitulacionCarrera.iterator();
				while(listaIterator.hasNext()){
					MecanismoCarrera elemento = listaIterator.next();
					if(elemento.getMccrMecanismoTitulacion().getMcttDescripcion().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)){
						listaIterator.remove();
					}
				}	
			}
			if(estudiante.getTrttObsValSecSplit().equals(ValidacionConstantes.OTROS_MECANISMOS_IDONEO_LABEL)){
				Iterator<MecanismoCarrera> listaIterator = amfListMecanismoTitulacionCarrera.iterator();
				listaIterator = amfListMecanismoTitulacionCarrera.iterator();
				while(listaIterator.hasNext()){
					MecanismoCarrera elemento = listaIterator.next();
					if(elemento.getMccrMecanismoTitulacion().getMcttDescripcion().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)){
						listaIterator.remove();
					}else{
						
					}
				}	
			}
//			for (MecanismoTitulacionCarrera item : amfListMecanismoTitulacionCarrera) {
//				if(item.getMcttcrMecanismoTitulacion().getMcttDescripcion() == MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL ){
//					amfDeshabilitadoTutor=true;
//				}else{
//					amfDeshabilitadoTutor=false;
//				}
//			}
			
			
		} catch (MecanismoTitulacionCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.todos.no.result.exception")));
		} catch (MecanismoTitulacionCarreraException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.por.id.non.unique.result.exception")));
		}
		amfDeshabilitadoTutor=true;
		return "irAsignacionMecanismo";
	}
	
	public String guardar(EstudianteAsignacionMecanismoDto estudiante) {
		if(atfIs!=null){
			OutputStream output=null;
			try {
//				java.util.Date utilDate = new java.util.Date(); //fecha actual
//				long lnMilisegundos = utilDate.getTime();
//				java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
				output = new FileOutputStream(new File(GeneralesConstantes.APP_PATH_ARCHIVOS+"tutor/", estudiante.getPrsIdentificacion()+"TemaTutor"+".pdf"));
				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = atfIs.read(bytes)) != -1) {
					output.write(bytes, 0, read);
				}
			} catch (IOException e) {
			} finally {
				if (atfIs != null) {
					try {
						atfIs.close();
					} catch (IOException e) {
					}
				}
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
					}
				}
			}
			
			try {
				if(estudiante!= null && amfEstudianteAsignacionMecanismoDtoEditar.getFcesMecanismoTitulacionCarrera() != GeneralesConstantes.APP_ID_BASE ){
				amfMecanismoTitulacionCarrera = servAmfMecanismoTitulacionCarreraServicio.buscarPorId(estudiante.getFcesMecanismoTitulacionCarrera());
				
				RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(estudiante.getTrttCarreraId(), amfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_EVALUADOR_VALUE);
				estudiante.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				
//				System.out.println("estudiante : "+ estudiante.getFcesMecanismoTitulacionCarrera());
//				System.out.println("usuario : "+ amfUsuarioQueCambia.getUsrIdentificacion());
//				System.out.println("rol_flujo_carrera : "+ roflcrr.getRoflcrId());
//				System.out.println("rol_flujo_carrera : "+ amfMecanismoTitulacionCarrera.getMcttcrMecanismoTitulacion().getMcttDescripcion());
				
				servAmfEstudianteAsignacionDtoServicioJdbc.editar(estudiante, amfUsuarioQueCambia.getUsrIdentificacion() , roflcrr , amfMecanismoTitulacionCarrera);
				
				
//				if(estudiante.getAssttTemaTrabajo()==null){
//					//******************************************
//					//**** MAIL EXAMEN COMPLEXIVO***************
//					//******************************************
//					//******************************************************************************
//					//************************* ACA INICIA EL ENVIO DE MAIL ************************
//					//******************************************************************************
//					//defino los datos para la plantilla
//					Map<String, Object> parametros = new HashMap<String, Object>();
//					
//					parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsNombres().toUpperCase()));
//					parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsPrimerApellido().toUpperCase()+" "
//									+(estudiante.getPrsSegundoApellido().toUpperCase() == null?"":estudiante.getPrsSegundoApellido().toUpperCase())));
//					SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
//					parametros.put("fechaHora", sdf.format(new Date()));
//					String facultadMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getFclDescripcion());
//					String carreraMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getCrrDescripcion());
//					parametros.put("facultad", facultadMail);
//					parametros.put("carrera", carreraMail);
//					
//					//lista de correos a los que se enviara el mail
//					List<String> correosTo = new ArrayList<>();
//					
//					correosTo.add(estudiante.getPrsMailPersonal());
//					
//					//path de la plantilla del mail
//					String pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-asignacion-complexivo.vm";
//					
//					//llamo al generador de mails
//					GeneradorMails genMail = new GeneradorMails();
//					String mailjsonSt;
//					try {
//						mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_POSTULACION, 
//								GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
//						//****envio el mail a la cola
//						//cliente web service
//						Client client = ClientBuilder.newClient();
//						WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
//						MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
//						postForm.add("mail", mailjsonSt);
//		//				String responseData = target.request().post(Entity.form(postForm),String.class);
//						target.request().post(Entity.form(postForm),String.class);
//					} catch (ValidacionMailException e) {
//						
//					}
//					//******************************************************************************
//					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//					//******************************************************************************
//				}else{
					//******************************************
					//**** MAIL OTROS MECANISMOS ***************
					//******************************************
					//******************************************************************************
					//************************* ACA INICIA EL ENVIO DE MAIL ************************
					//******************************************************************************
					//defino los datos para la plantilla
					Map<String, Object> parametros = new HashMap<String, Object>();
					
					parametros.put("nombres", estudiante.getPrsNombres().toUpperCase());
					try {
						parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsPrimerApellido().toUpperCase()+" "
								+(estudiante.getPrsSegundoApellido().toUpperCase() == null?"":estudiante.getPrsSegundoApellido().toUpperCase())));	
					} catch (Exception e) {
						parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsPrimerApellido().toUpperCase()));
					}
					
					SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
					parametros.put("fechaHora", sdf.format(new Date()));
					String facultadMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getFclDescripcion());
					String carreraMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getCrrDescripcion());
					String mecanismoMail=GeneralesUtilidades.generaStringParaCorreo(amfMecanismoTitulacionCarrera.getMccrMecanismoTitulacion().getMcttDescripcion());
					String temaMail=GeneralesUtilidades.generaStringParaCorreo(estudiante.getAssttTemaTrabajo().toUpperCase().replaceAll(" +", " ").trim());
					String tutorMail=GeneralesUtilidades.generaStringParaCorreo(estudiante.getAssttTutor().toUpperCase().replaceAll(" +", " ").trim());
//					String directorMail=GeneralesUtilidades.generaStringParaCorreo(estudiante.getAsttDirectorCientifico().toUpperCase().replaceAll(" +", " ").trim());
					parametros.put("facultad", facultadMail);
					parametros.put("carrera", carreraMail);
					parametros.put("mecanismo", mecanismoMail);
					parametros.put("tema", temaMail);
					parametros.put("docenteCientifico", tutorMail);
					//lista de correos a los que se enviará el mail
					List<String> correosTo = new ArrayList<>();
					
					correosTo.add(estudiante.getPrsMailPersonal());
					
					//path de la plantilla del mail
					String pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-asignacion-otrosMecanismos.vm";
					
					//llamo al generador de mails
					GeneradorMails genMail = new GeneradorMails();
					String mailjsonSt;
					try {
						mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_POSTULACION, 
								GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
						//****envio el mail a la cola
						//cliente web service
						Client client = ClientBuilder.newClient();
						WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
						MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
						postForm.add("mail", mailjsonSt);
		//				String responseData = target.request().post(Entity.form(postForm),String.class);
						target.request().post(Entity.form(postForm),String.class);
					} catch (ValidacionMailException e) {
					}
					//******************************************************************************
					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
					//******************************************************************************
//				}
				iniciarParametros();
				amfValidadorClic = 0;
				amfEstudianteAsignacionMecanismoDtoEditar.setFcesMecanismoTitulacionCarrera(GeneralesConstantes.APP_ID_BASE);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.guardar.exitoso")));
				return "irListarAsignacionMecanismos";
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.guardar.no.exitoso")));
					amfValidadorClic = 0;
					return null;
				}
				
			} catch (RolFlujoCarreraNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (RolFlujoCarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EstudianteAsignacionDtoNoEncontradoException
					| EstudianteAsignacionDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MecanismoTitulacionCarreraNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MecanismoTitulacionCarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			return null;
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor, debe cargar el archivo del informe de Aprobación de Tutoría y Tema.");
			return null;
		}
		
	}
	
	
	public void handleFileUpload(FileUploadEvent event){
		try {
			this.atfIs =event.getFile().getInputstream();
			atfNombreArchivo = event.getFile().getFileName();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Archivo "+atfNombreArchivo+" del informe de aprobación cargado con éxito.");
		} catch (IOException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al cargar el archivo, por favor intente más tarde.");
		}
	}
	
	/**
	 * Lista los docentes segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarDocentes(Usuario usuario, String cedula){
		amfHabilitadorGuardar=true;
		try {
			amfListDocentesDto=srvDocenteDtoServicioJdbc.buscarDocenteXIdentificacion(cedula,amfApellidoDocenteBuscar);
			if(amfListDocentesDto.size()>0){
				amfMensajeTutor="No hay resultados con los parámetros seleccionados";
			}
			cedula=null;
			amfApellidoDocenteBuscar=null;
		} catch (DocenteTutorTribunalLectorJdbcDtoException e) {
			amfListDocentesDto = null;
			amfMensajeTutor=(e.getMessage());
		} catch (DocenteTutorTribunalLectorJdbcDtoNoEncontradoException e) {
			amfListDocentesDto = null;
			amfMensajeTutor=(e.getMessage());
		}
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		amfCarrera=new CarreraDto();
		amfConvocatoria = new Convocatoria();
		amfHabilitadorAsignar=true;
		amfValidadorClicTutor = 0;
		amfValidadorClicDirector = 0;
		amfHabilitadorGuardar=true;
		amfEstudianteAsignacionMecanismoDtoBuscar = new EstudianteAsignacionMecanismoDto();
		amfListEstudianteAsignacionMecanismoDto = null;
		amfListEstudianteAsignacionMecanismoDto= new ArrayList<EstudianteAsignacionMecanismoDto>();
		amfEstudianteAsignacionMecanismoDtoEditar = new EstudianteAsignacionMecanismoDto();
		cedulaDirector=null;
		cedulaTutor=null;
		seleccion = 0;
	}

	public String verificarClickAceptarTramite(){
		if(atfIs!=null){
			amfValidadorClic = 1;
			return null;	
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor, debe cargar el archivo del informe de Aprobación de Tutoría y Tema.");
			return null;
		}
		
	}
	
	public String verificarClickAceptarTramiteNo(){
		amfValidadorClic = 0;
		return null;
	}
	
	public String verificarClickAsignarTutor(Integer op){
		seleccion=op;
		if(seleccion==0){
			amfEstudianteAsignacionMecanismoDtoEditar.setAssttTutor(null);	
		}else if (seleccion==1){
			amfEstudianteAsignacionMecanismoDtoEditar.setAsttDirectorCientifico(null);
		}
		
		amfValidadorClicTutor = 1;
		return null;
	}
	
	public String verificarClickAsignarTutorNo(){
		amfValidadorClicTutor = 0;
		return null;
	}
	
	
	public String verificarClickAsignarDirector(){
		amfEstudianteAsignacionMecanismoDtoEditar.setAsttDirectorCientifico(null);
		amfValidadorClicDirector = 1;
		return null;
	}
	
	public String verificarClickAsignarDirectorNo(){
		amfValidadorClicDirector = 0;
		return null;
	}
	
	public void cambiarTema(){
		if(amfEstudianteAsignacionMecanismoDtoEditar.getAssttTemaTrabajo()!=null){
			if(amfEstudianteAsignacionMecanismoDtoEditar.getAssttTutor()!=null){
				amfHabilitadorGuardar=false;
			}
		}
	}
	
	/**
	 * asigna el docente a la variable que se mostrará en el JSF 
	 */
	public void asignarDocente(DocenteTutorTribunalLectorJdbcDto item, Usuario usuario){
		amfListDocentesDto = null;
		amfHabilitadorGuardar=false;
		if(seleccion==0){
			amfEstudianteAsignacionMecanismoDtoEditar.setAssttTutor(item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
			verificarClickAsignarTutorNo();
			cedulaTutor=item.getPrsIdentificacion();	
		}else if(seleccion==1){
			amfEstudianteAsignacionMecanismoDtoEditar.setAsttDirectorCientifico(item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
			verificarClickAsignarTutorNo();
			cedulaDirector=item.getPrsIdentificacion();
		}
		
	}
	
	/**
	 * asigna el docente a la variable que se mostrará en el JSF 
	 */
	public void asignarDocenteDirector(DocenteTutorTribunalLectorJdbcDto item, Usuario usuario){
		amfListDocentesDto = null;
		amfHabilitadorGuardar=false;
		amfEstudianteAsignacionMecanismoDtoEditar.setAsttDirectorCientifico(item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
		verificarClickAsignarTutorNo();
		cedulaDirector=item.getPrsIdentificacion();
	}
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/

	public EstudianteAsignacionMecanismoDto getAmfEstudianteAsignacionMecanismoDtoBuscar() {
		return amfEstudianteAsignacionMecanismoDtoBuscar;
	}


	public void setAmfEstudianteAsignacionMecanismoDtoBuscar(
			EstudianteAsignacionMecanismoDto amfEstudianteAsignacionMecanismoDtoBuscar) {
		this.amfEstudianteAsignacionMecanismoDtoBuscar = amfEstudianteAsignacionMecanismoDtoBuscar;
	}

	public EstudianteAsignacionMecanismoDto getAmfEstudianteAsignacionMecanismoDtoEditar() {
		return amfEstudianteAsignacionMecanismoDtoEditar;
	}

	public void setAmfEstudianteAsignacionMecanismoDtoEditar(
			EstudianteAsignacionMecanismoDto amfEstudianteAsignacionMecanismoDtoEditar) {
		this.amfEstudianteAsignacionMecanismoDtoEditar = amfEstudianteAsignacionMecanismoDtoEditar;
	}

	public CarreraDto getAmfCarrera() {
		return amfCarrera;
	}

	public void setAmfCarrera(CarreraDto amfCarrera) {
		this.amfCarrera = amfCarrera;
	}

	public List<CarreraDto> getAmfListCarreras() {
		amfListCarreras = amfListCarreras==null?(new ArrayList<CarreraDto>()):amfListCarreras;
		return amfListCarreras;
	}

	public void setAmfListCarreras(List<CarreraDto> amfListCarreras) {
		this.amfListCarreras = amfListCarreras;
	}

	public Convocatoria getAmfConvocatoria() {
		return amfConvocatoria;
	}

	public void setAmfConvocatoria(Convocatoria amfConvocatoria) {
		this.amfConvocatoria = amfConvocatoria;
	}

	public List<Convocatoria> getAmfListConvocatorias() {
		amfListConvocatorias = amfListConvocatorias==null?(new ArrayList<Convocatoria>()):amfListConvocatorias;
		return amfListConvocatorias;
	}

	public void setAmfListConvocatorias(List<Convocatoria> amfListConvocatorias) {
		this.amfListConvocatorias = amfListConvocatorias;
	}

	public Usuario getAmfUsuarioQueCambia() {
		return amfUsuarioQueCambia;
	}

	public void setAmfUsuarioQueCambia(Usuario amfUsuarioQueCambia) {
		this.amfUsuarioQueCambia = amfUsuarioQueCambia;
	}

	public List<EstudianteAsignacionMecanismoDto> getAmfListEstudianteAsignacionMecanismoDto() {
		amfListEstudianteAsignacionMecanismoDto = amfListEstudianteAsignacionMecanismoDto==null?(new ArrayList<EstudianteAsignacionMecanismoDto>()):amfListEstudianteAsignacionMecanismoDto;
		return amfListEstudianteAsignacionMecanismoDto;
	}

	public void setAmfListEstudianteAsignacionMecanismoDto(
			List<EstudianteAsignacionMecanismoDto> amfListEstudianteAsignacionMecanismoDto) {
		this.amfListEstudianteAsignacionMecanismoDto = amfListEstudianteAsignacionMecanismoDto;
	}

	public Persona getAmfPersonaPostulante() {
		return amfPersonaPostulante;
	}

	public void setAmfPersonaPostulante(Persona amfPersonaPostulante) {
		this.amfPersonaPostulante = amfPersonaPostulante;
	}

	public int getFechaRegimenPostulacion() {
		return fechaRegimenPostulacion;
	}

	public void setFechaRegimenPostulacion(int fechaRegimenPostulacion) {
		this.fechaRegimenPostulacion = fechaRegimenPostulacion;
	}

	public int getHabilitarCampos() {
		return habilitarCampos;
	}

	public void setHabilitarCampos(int habilitarCampos) {
		this.habilitarCampos = habilitarCampos;
	}

	public Integer getAmfValidadorClic() {
		return amfValidadorClic;
	}

	public void setAmfValidadorClic(Integer amfValidadorClic) {
		this.amfValidadorClic = amfValidadorClic;
	}

	public Boolean getAmfDeshabilitadoTutor() {
		return amfDeshabilitadoTutor;
	}

	public void setAmfDeshabilitadoTutor(Boolean amfDeshabilitadoTutor) {
		this.amfDeshabilitadoTutor = amfDeshabilitadoTutor;
	}

	public List<MecanismoCarrera> getAmfListMecanismoTitulacionCarrera() {
		amfListMecanismoTitulacionCarrera = amfListMecanismoTitulacionCarrera==null?(new ArrayList<MecanismoCarrera>()):amfListMecanismoTitulacionCarrera;
		return amfListMecanismoTitulacionCarrera;
	}

	public void setAmfListMecanismoTitulacionCarrera(
			List<MecanismoCarrera> amfListMecanismoTitulacionCarrera) {
		this.amfListMecanismoTitulacionCarrera = amfListMecanismoTitulacionCarrera;
	}

	public MecanismoCarrera getAmfMecanismoTitulacionCarrera() {
		return amfMecanismoTitulacionCarrera;
	}

	public void setAmfMecanismoTitulacionCarrera(
			MecanismoCarrera amfMecanismoTitulacionCarrera) {
		this.amfMecanismoTitulacionCarrera = amfMecanismoTitulacionCarrera;
	}

	public Integer getAmfExamenComplexivoValue() {
		return amfExamenComplexivoValue;
	}

	public void setAmfExamenComplexivoValue(Integer amfExamenComplexivoValue) {
		this.amfExamenComplexivoValue = amfExamenComplexivoValue;
	}

	public Boolean getAmfHabilitadorGuardar() {
		return amfHabilitadorGuardar;
	}

	public void setAmfHabilitadorGuardar(Boolean amfHabilitadorGuardar) {
		this.amfHabilitadorGuardar = amfHabilitadorGuardar;
	}

	public Integer getAmfValidadorClicTutor() {
		return amfValidadorClicTutor;
	}

	public void setAmfValidadorClicTutor(Integer amfValidadorClicTutor) {
		this.amfValidadorClicTutor = amfValidadorClicTutor;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getAmfListDocentesDto() {
		amfListDocentesDto = amfListDocentesDto==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):amfListDocentesDto;
		return amfListDocentesDto;
	}

	public void setAmfListDocentesDto(
			List<DocenteTutorTribunalLectorJdbcDto> amfListDocentesDto) {
		this.amfListDocentesDto = amfListDocentesDto;
	}

	public String getAmfMensajeTutor() {
		return amfMensajeTutor;
	}

	public void setAmfMensajeTutor(String amfMensajeTutor) {
		this.amfMensajeTutor = amfMensajeTutor;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getAmfApellidoDocenteBuscar() {
		return amfApellidoDocenteBuscar;
	}

	public void setAmfApellidoDocenteBuscar(String amfApellidoDocenteBuscar) {
		this.amfApellidoDocenteBuscar = amfApellidoDocenteBuscar;
	}
	
	public Boolean getAmfhabilitadorAsignar() {
		return amfHabilitadorAsignar;
	}

	public void setAmfhabilitadorAsignar(Boolean amfHabilitadorAsignar) {
		this.amfHabilitadorAsignar = amfHabilitadorAsignar;
	}

	public Integer getAmfValidadorClicDirector() {
		return amfValidadorClicDirector;
	}

	public void setAmfValidadorClicDirector(Integer amfValidadorClicDirector) {
		this.amfValidadorClicDirector = amfValidadorClicDirector;
	}

	public String getCedulaTutor() {
		return cedulaTutor;
	}

	public void setCedulaTutor(String cedulaTutor) {
		this.cedulaTutor = cedulaTutor;
	}

	public String getCedulaDirector() {
		return cedulaDirector;
	}

	public void setCedulaDirector(String cedulaDirector) {
		this.cedulaDirector = cedulaDirector;
	}

	public InputStream getAtfIs() {
		return atfIs;
	}

	public void setAtfIs(InputStream atfIs) {
		this.atfIs = atfIs;
	}

	public String getAtfNombreArchivo() {
		return atfNombreArchivo;
	}

	public void setAtfNombreArchivo(String atfNombreArchivo) {
		this.atfNombreArchivo = atfNombreArchivo;
	}
	
	
	
	
}

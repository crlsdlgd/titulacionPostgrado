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
   
 ARCHIVO:     DesactivarPostulacionForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones de desactivar postulaciones.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
24-04-2018			Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.administracion.dgpp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import ec.edu.uce.envioMail.excepciones.ValidacionMailException;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudiantePostuladoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.FacultadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (managed bean) DesactivarPostulacionForm.
 * Managed Bean que maneja las peticiones de desactivar postulaciones.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="desactivarPostulacionForm")
@SessionScoped
public class DesactivarPostulacionForm {
	// *****************************************************************/
	// ******************* Variables de DesactivarPostulacionForm **********/
	// *****************************************************************/
	private EstudiantePostuladoJdbcDto dpfEstudiantePostuladoDtoBuscar;
	private EstudiantePostuladoJdbcDto dpfEstudiantePostuladoDtoEditar;
	private FacultadDto dpfFacultad;
	private CarreraDto dpfCarrera;
	private List<CarreraDto> dpfListCarreras;
	private List<FacultadDto> dpfListFacultades;
	private List<CarreraDto> dpfListCarrera;
	private Convocatoria dpfConvocatoria;
	private List<Convocatoria> dpfListConvocatorias;
	private Usuario dpfUsuarioQueCambia;
	private List<EstudiantePostuladoJdbcDto> dpfListEstudiantePostuladoDto;
	private Integer dpfValidadorClic;
	
	@EJB
	private ConvocatoriaServicio srvConvocatoriaServicio;
	
	@EJB
	private UsuarioRolServicio srvUsuarioRolServicio;
	
	@EJB
	private FacultadDtoServicioJdbc srvFacultadServicio;
	
	@EJB
	private EstudianteValidacionDtoServicioJdbc servAsfEstudianteValidacionDtoServicioJdbc;
	
	@EJB
	private EstudiantePostuladoDtoServicioJdbc servDpfEstudiantePostulacionDtoServicioJdbc;
	
	@EJB
	private TramiteTituloServicio servDpfTramiteTituloServicio;
	
	
	@EJB
	private CarreraDtoServicioJdbc servCarreraDtoServicioJdbc;
	//****************************************************************/
	//******** METODOS DE NAVEGACION ********** **********************/
	//****************************************************************/
	
	/**
	 * Método que dige a la página de Aptitud de examen complexivo
	 * @param usuario
	 * @return
	 */
	public String ListarEvaluadosAptitud(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacionDga(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
				return null;
			}
			iniciarParametros();
			dpfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			dpfListFacultades = srvFacultadServicio.listarTodos();
			dpfUsuarioQueCambia = usuario;
			return "irListarDesactivarPostulacion";
		}  catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró ninguna convocatoria ");
		} catch (UsuarioRolNoEncontradoException e) {
			try {
				UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacionEvaluador(usuario.getUsrIdentificacion());
				if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
					FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
					return null;
				}
				iniciarParametros();
				dpfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
				dpfListCarrera = servCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFl(usuario.getUsrId(), RolConstantes.ROL_BD_EVALUADOR, RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
				dpfUsuarioQueCambia = usuario;
				return "irListarDesactivarPostulacionDirector";
			} catch (Exception e2) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			}
			
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (FacultadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (FacultadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		return null;
	}
	
	/**
	 * Maneja de cancelacion de la desactivación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String irInicio(){
		return "irInicio";
	}
	
	/**
	 * Maneja de cancelacion de la desactivación del postulante
	 * @return - cadena de navegacion a la pagina de listar postulantes
	 */
	public String irCancelar(){
		iniciarParametros();
		return "irCancelarDesactivar";
	}
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String irDesactivar(EstudiantePostuladoJdbcDto estudiante,Usuario usuario){
		dpfValidadorClic = 0;
		this.dpfEstudiantePostuladoDtoEditar=estudiante;
		if(estudiante.getTrttEstadoTramite()==TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE){
			return "irDesactivar";
		}else if (estudiante.getTrttEstadoTramite()==TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE){
			return "irActivar";
		}
		return null;
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	public String guardarActivacion(){
		try {
			servDpfTramiteTituloServicio.desactivarPorId(dpfEstudiantePostuladoDtoEditar);
			dpfValidadorClic = 0;
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Activar.estado.postulante.exitoso",dpfEstudiantePostuladoDtoEditar.getPrsIdentificacion())));
			//******************************************
			//**** MAIL EXAMEN COMPLEXIVO***************
			//******************************************
			//******************************************************************************
			//************************* ACA INICIA EL ENVIO DE MAIL ************************
			//******************************************************************************
			//defino los datos para la plantilla
			Map<String, Object> parametros = new HashMap<String, Object>();
			
			parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsNombres().toUpperCase()));
			parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsPrimerApellido().toUpperCase())+" "
							+(GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsSegundoApellido().toUpperCase()) == null?"":
								GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsSegundoApellido().toUpperCase())));
			SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
			parametros.put("fechaHora", sdf.format(new Date()));
			String facultadMail = GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getFclDescripcion());
			String carreraMail = GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getCrrDescripcion());
			parametros.put("facultad", facultadMail);
			parametros.put("carrera", carreraMail);
			parametros.put("fechaConvocatoria", dpfEstudiantePostuladoDtoEditar.getCnvDescripcion());

			//lista de correos a los que se enviara el mail
			List<String> correosTo = new ArrayList<>();
			
			correosTo.add(dpfEstudiantePostuladoDtoEditar.getPrsMailPersonal());
			
			//path de la plantilla del mail
			String pathTemplate;
			pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-activacion.vm";
			//llamo al generador de mails
			GeneradorMails genMail = new GeneradorMails();
			String mailjsonSt;
			try {
				mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_ACTIVACION_POSTULANTE+dpfEstudiantePostuladoDtoEditar.getCnvDescripcion(), 
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
		} catch (Exception e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Activar.estado.postulante.error")));
		}
		iniciarParametros();
		return "irCancelarDesactivar";
	}
	
	public String guardarDesactivacion(){
		try {
			servDpfTramiteTituloServicio.desactivarPorId(dpfEstudiantePostuladoDtoEditar);
			dpfValidadorClic = 0;
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Desactivar.estado.postulante.exitoso",dpfEstudiantePostuladoDtoEditar.getPrsIdentificacion())));
			//******************************************
			//**** MAIL EXAMEN COMPLEXIVO***************
			//******************************************
			//******************************************************************************
			//************************* ACA INICIA EL ENVIO DE MAIL ************************
			//******************************************************************************
			//defino los datos para la plantilla
			Map<String, Object> parametros = new HashMap<String, Object>();
			
			parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsNombres().toUpperCase()));
			parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsPrimerApellido().toUpperCase())+" "
							+(GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsSegundoApellido().toUpperCase()) == null?"":
								GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getPrsSegundoApellido().toUpperCase())));
			SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
			parametros.put("fechaHora", sdf.format(new Date()));
			String facultadMail = GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getFclDescripcion());
			String carreraMail = GeneralesUtilidades.generaStringParaCorreo(dpfEstudiantePostuladoDtoEditar.getCrrDescripcion());
			parametros.put("facultad", facultadMail);
			parametros.put("carrera", carreraMail);
			parametros.put("fechaConvocatoria", dpfEstudiantePostuladoDtoEditar.getCnvDescripcion());
			//lista de correos a los que se enviara el mail
			List<String> correosTo = new ArrayList<>();
			
			correosTo.add(dpfEstudiantePostuladoDtoEditar.getPrsMailPersonal());
			
			//path de la plantilla del mail
			String pathTemplate;
			pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-desactivacion.vm";
			//llamo al generador de mails
			GeneradorMails genMail = new GeneradorMails();
			String mailjsonSt;
			try {
				mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_DESACTIVACION_POSTULANTE+dpfEstudiantePostuladoDtoEditar.getCnvDescripcion(), 
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
		} catch (Exception e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Desactivar.estado.postulante.error")));
		}
		iniciarParametros();
		return "irCancelarDesactivar";
	}
	
	//iniciar parametros de busqueda
	@SuppressWarnings("unused")
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		dpfCarrera=new CarreraDto();
		dpfConvocatoria = new Convocatoria();
		dpfFacultad=new FacultadDto();
		dpfEstudiantePostuladoDtoBuscar = new EstudiantePostuladoJdbcDto();
		dpfListEstudiantePostuladoDto=null;
		List<CarreraDto> dpfListCarrera = new ArrayList<CarreraDto>();
	}
	
	/** Método que limpia la lista de carreras
	* 
	*/
	public void cambiarCarrera(){
		iniciarParametros();
	}
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		dpfListEstudiantePostuladoDto=null;
		dpfEstudiantePostuladoDtoBuscar = null;
		iniciarParametros();
	}
	
	/**
	 * Lista los estudiantes evaluados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarAsignadosTotalesParaEditar(Usuario usuario){
		try {
			dpfListEstudiantePostuladoDto = servDpfEstudiantePostulacionDtoServicioJdbc.buscarPostulantesParaDesactivar(dpfEstudiantePostuladoDtoBuscar.getPrsIdentificacion(),dpfFacultad.getFclId(),dpfConvocatoria.getCnvId());
		
			for (EstudiantePostuladoJdbcDto item : dpfListEstudiantePostuladoDto) {
				if(item.getTrttEstadoTramite()==TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE){
					item.setEstadoTramite("Desactivar trámite");
				}else if (item.getTrttEstadoTramite()==TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE){
					item.setEstadoTramite("Activar trámite");
				}
			}
		} catch (EstudiantePostuladoJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(e.getMessage());
			dpfListEstudiantePostuladoDto = null;
		} catch (EstudiantePostuladoJdbcDtoNoEncontradoException e) {
			try {
				dpfListEstudiantePostuladoDto = servDpfEstudiantePostulacionDtoServicioJdbc.buscarPostulantesParaDesactivarPorCarrera(dpfEstudiantePostuladoDtoBuscar.getPrsIdentificacion(),dpfCarrera.getCrrId(),dpfConvocatoria.getCnvId(),dpfUsuarioQueCambia.getUsrIdentificacion());
				
				for (EstudiantePostuladoJdbcDto item : dpfListEstudiantePostuladoDto) {
					if(item.getTrttEstadoTramite()==TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE){
						item.setEstadoTramite("Desactivar trámite");
					}else if (item.getTrttEstadoTramite()==TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE){
						item.setEstadoTramite("Activar trámite");
					}
				}
			} catch (Exception e2) {
				dpfListEstudiantePostuladoDto = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}
	}
	
	public String verificarClickAceptarTramite(){
		dpfValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		dpfValidadorClic = 0;
		return null;
	}
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/
	
	
	public FacultadDto getDpfFacultad() {
		return dpfFacultad;
	}
	public void setDpfFacultad(FacultadDto dpfFacultad) {
		this.dpfFacultad = dpfFacultad;
	}
	public CarreraDto getDpfCarrera() {
		return dpfCarrera;
	}
	public void setDpfCarrera(CarreraDto dpfCarrera) {
		this.dpfCarrera = dpfCarrera;
	}
	public List<CarreraDto> getDpfListCarreras() {
		dpfListCarreras = dpfListCarreras==null?(new ArrayList<CarreraDto>()):dpfListCarreras;
		return dpfListCarreras;
	}
	public void setDpfListCarreras(List<CarreraDto> dpfListCarreras) {
		this.dpfListCarreras = dpfListCarreras;
	}
	public List<FacultadDto> getDpfListFacultades() {
		dpfListFacultades = dpfListFacultades==null?(new ArrayList<FacultadDto>()):dpfListFacultades;
		return dpfListFacultades;
	}
	public void setDpfListFacultades(List<FacultadDto> dpfListFacultades) {
		this.dpfListFacultades = dpfListFacultades;
	}
	public Convocatoria getDpfConvocatoria() {
		return dpfConvocatoria;
	}
	public void setDpfConvocatoria(Convocatoria dpfConvocatoria) {
		this.dpfConvocatoria = dpfConvocatoria;
	}
	public List<Convocatoria> getDpfListConvocatorias() {
		dpfListConvocatorias = dpfListConvocatorias==null?(new ArrayList<Convocatoria>()):dpfListConvocatorias;
		return dpfListConvocatorias;
	}
	public void setDpfListConvocatorias(List<Convocatoria> dpfListConvocatorias) {
		this.dpfListConvocatorias = dpfListConvocatorias;
	}
	public Usuario getDpfUsuarioQueCambia() {
		return dpfUsuarioQueCambia;
	}
	public void setDpfUsuarioQueCambia(Usuario dpfUsuarioQueCambia) {
		this.dpfUsuarioQueCambia = dpfUsuarioQueCambia;
	}
	
	public EstudiantePostuladoJdbcDto getDpfEstudiantePostuladoDtoBuscar() {
		return dpfEstudiantePostuladoDtoBuscar;
	}

	public void setDpfEstudiantePostuladoDtoBuscar(
			EstudiantePostuladoJdbcDto dpfEstudiantePostuladoDtoBuscar) {
		this.dpfEstudiantePostuladoDtoBuscar = dpfEstudiantePostuladoDtoBuscar;
	}

	public EstudiantePostuladoJdbcDto getDpfEstudiantePostuladoDtoEditar() {
		return dpfEstudiantePostuladoDtoEditar;
	}

	public void setDpfEstudiantePostuladoDtoEditar(
			EstudiantePostuladoJdbcDto dpfEstudiantePostuladoDtoEditar) {
		this.dpfEstudiantePostuladoDtoEditar = dpfEstudiantePostuladoDtoEditar;
	}

	public List<EstudiantePostuladoJdbcDto> getDpfListEstudiantePostuladoDto() {
		dpfListEstudiantePostuladoDto = dpfListEstudiantePostuladoDto==null?(new ArrayList<EstudiantePostuladoJdbcDto>()):dpfListEstudiantePostuladoDto;
		return dpfListEstudiantePostuladoDto;
	}

	public void setDpfListEstudiantePostuladoDto(
			List<EstudiantePostuladoJdbcDto> dpfListEstudiantePostuladoDto) {
		this.dpfListEstudiantePostuladoDto = dpfListEstudiantePostuladoDto;
	}

	public Integer getDpfValidadorClic() {
		return dpfValidadorClic;
	}

	public void setDpfValidadorClic(Integer dpfValidadorClic) {
		this.dpfValidadorClic = dpfValidadorClic;
	}

	public List<CarreraDto> getDpfListCarrera() {
		return dpfListCarrera;
	}

	public void setDpfListCarrera(List<CarreraDto> dpfListCarrera) {
		this.dpfListCarrera = dpfListCarrera;
	}

	

	
	

	
}

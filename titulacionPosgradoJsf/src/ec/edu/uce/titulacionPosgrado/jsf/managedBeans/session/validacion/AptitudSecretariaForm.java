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
   
 ARCHIVO:     AptitudSecretariaForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de declaración de aptitud de los postulantes.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
25 - Julio - 2018		Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
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
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ValidacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.AsignacionTitulacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaDocenteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ValidacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AsentamientoNotaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteAsignacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudiantePostuladoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.FacultadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.MecanismoTitulacionCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TramiteTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AptitudConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AsentamientoNotaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Validacion;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (managed bean) AptitudSecretariaForm.
 * Bean que maneja las peticiones de declaración de aptitud de los postulantes.
 * @author dalbuja.
 * @version 1.0
 */



@ManagedBean(name="aptitudSecretariaForm")
@SessionScoped
public class AptitudSecretariaForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
		
	// *****************************************************************/
	// ******************* Variables de AptitudSecretariaForm **********/
	// *****************************************************************/
	
	private EstudianteAptitudJdbcDto asfEstudianteAptitudDtoBuscar;
	private EstudianteAptitudJdbcDto asfEstudianteAptitudDtoEditar;
	private EstudianteAptitudOtrosMecanismosJdbcDto asfEstudianteAptitudOtrosMecanismosDtoEditar;
	private FacultadDto asfFacultad;
	private CarreraDto asfCarrera;
	private List<CarreraDto> asfListCarreras;
	private List<FacultadDto> asfListFacultades;
	private Convocatoria asfConvocatoria;
	private List<Convocatoria> asfListConvocatorias;
	private Usuario asfUsuarioQueCambia;
	private List<EstudianteAptitudJdbcDto> asfListEstudianteAptitudDto;
	private List<EstudianteAptitudOtrosMecanismosJdbcDto> asfListEstudianteAptitudOtrosMecanismosDto;
	private Persona asfPersonaPostulante;
	private List<MecanismoCarrera> asfListMecanismoTitulacionCarrera;
	private MecanismoCarrera asfMecanismoTitulacionCarrera;
	private Integer asfRequisitos;
	private Integer asfSuficienciaIngles;
	private Integer asfFinMalla;
	private Integer asfAproboActualizar;
	private BigDecimal asfAptNotaDirector;
	private Integer asfSegundaCarrera;
	private int habilitarCampos;
	private Integer asfValidadorClic;
	private Integer asfTutorClic;
	private Integer asfLectoresClic;
	private Boolean asfDeshabilitadoTutor;
	private Integer asfActualizaConocimientos;
	private boolean asfHabilitadorActualizarConocimientos;
	private boolean asfHabilitadorPanelAptitud;
	private String asfCedulaDocenteBuscar;
	private String asfApellidoDocenteBuscar;
	private String asfCedulaDocenteLectorBuscar;
	private String asfMensajeTutor;
	private String asfNota1Cadena;
	private String asfNota2Cadena;
	private String asfNota3Cadena;
	
	private BigDecimal asfNota1;
	private BigDecimal asfNota2;
	private BigDecimal asfNota3;
	private BigDecimal sumaNotas;
	private BigDecimal promedioNotas;

	private String asfPrimerLector;
	private String asfSegundoLector;
	private String asfTercerLector;
	private String asfCedulaPrimerLector;
	private String asfCedulaSegundoLector;
	private String asfCedulaTercerLector;
	private List<DocenteTutorTribunalLectorJdbcDto> asfListDocentesDto;
	private List<DocenteTutorTribunalLectorJdbcDto> asfListDocentesDtoVisualizar;
	private List<DocenteTutorTribunalLectorJdbcDto>  asfListDocentesLectoresDto;
	private boolean asfHabilitadorAsignar;
	private boolean asfHabilitadorTribunalMayorATres;
	private String asfMensajeLectores;
	private boolean asfHabilitadorNotasTribunal;
	private boolean asfHabilitadorNotasTribunal3;
	private boolean asfHabilitadorNotasTribunal2;
	private boolean asfHabilitadorTribunal3;
	private boolean asfHabilitadorGuardarModalTribunal;
	private boolean asfHabilitadorBtnAsignar;
	private boolean asfHabilitadorBtnAsignarLectores;
	private boolean asfHabilitadorRegistrarNotas;
	private boolean asfHabilitadorRequisitos;

	private boolean asfHabilitadorApruebaTutor;
	private Integer asfAproboTemaTutor;
	private boolean asfHabilitadorPanelTribunal;
	private boolean asfHabilitadorGuardar;
	private Integer faseAptitud;
	private boolean asfHabilitadorFecha;;
	private EstudiantePostuladoJdbcDto estudianteParametrosAux;
	private boolean asfHabilitador;
	private	Validacion vldAux;
	private Integer asfAranceles;
	private InputStream atfIs;
	private String atfNombreArchivo;
	
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private ConvocatoriaServicio srvConvocatoriaServicio;
	
	@EJB
	private UsuarioRolServicio srvUsuarioRolServicio;
	
	@EJB
	private FacultadDtoServicioJdbc srvFacultadServicio;
	
	@EJB
	private EstudianteAsignacionDtoServicioJdbc servAmfEstudianteAsignacionDtoServicioJdbc;
	
	@EJB
	private EstudianteValidacionDtoServicioJdbc servAsfEstudianteValidacionDtoServicioJdbc;
	
	@EJB
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
	
	@EJB
	private DocenteDtoServicioJdbc srvDocenteDtoServicioJdbc;
	
	@EJB
	private AsignacionTitulacionServicio srvAsignacionTitulacionServicio;
	
	@EJB
	private FichaDocenteServicio srvFichaDocenteServicio;
	
	@EJB
	private AsentamientoNotaDtoServicioJdbc srvAsentamientoNotaDtoServicioJdbc;

	@EJB
	private PersonaDtoServicioJdbc srvPersonaDtoServicioJdbc;
	
	@EJB
	private ValidacionServicio srvValidacionServicio;
	
	@EJB
	private TramiteTituloServicio srvTramiteTituloServicio;
	
	@EJB
	private TramiteTituloDtoServicioJdbc srvTramiteTituloDtoServicioJdbc;
	
	@EJB
	private EstudiantePostuladoDtoServicioJdbc srvEstudiantePostuladoDtoServicioJdbc;
	
	@EJB
	private FichaEstudianteServicio srvFichaEstudianteServicio;
	
	@EJB
	private MecanismoTitulacionCarreraDtoServicioJdbc srvMecanismoTitulacionCarreraDtoServicioJdbc;
	
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
	 * Método que dige a la página de Aptitud de examen complexivo
	 * @param usuario
	 * @return
	 */
	public String ListarEvaluadosAptitud(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active dectory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
			asfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			asfListCarreras=servAsfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(asfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (SQLException e) {
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró ninguna convocatoria ");
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		asfUsuarioQueCambia = usuario;
		asfValidadorClic = 0;
		asfLectoresClic=0;
		return "irListarEvaluadosAptitud";
	}
	
	/**
	 * Método que dige a la página de Aptitud de Otros mecanismos
	 * @param usuario
	 * @return
	 */
	public String ListarEvaluadosAptitudOtrosMecanismos(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active dectory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
			asfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			asfListCarreras=servAsfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(asfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
			asfHabilitadorActualizarConocimientos=false;
			asfUsuarioQueCambia = usuario;
			asfValidadorClic = 0;
			asfLectoresClic=0;
			asfListDocentesLectoresDto= new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
			asfAproboTemaTutor=1;
			return "irListarEvaluadosAptitudOtrosMecanismos";
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Método que dige a la página de Aptitud de la DGA
	 * @param usuario
	 * @return
	 */
	public String ListarEvaluadosAptitudDGA(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active dectory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacionDga(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
				return null;
			}
			iniciarParametros();
			asfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			asfListFacultades = srvFacultadServicio.listarTodos();
		}  catch (ConvocatoriaNoEncontradoException e) {
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
		} catch (FacultadDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (FacultadDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		asfHabilitadorActualizarConocimientos=false;
		asfUsuarioQueCambia = usuario;
		asfValidadorClic = 0;
		return "irListarAptitudDga";
	}
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String irInicio(){
		limpiar();
		iniciarParametros();
		asfHabilitadorActualizarConocimientos=false;
		asfActualizaConocimientos=null;
		asfRequisitos=null;
		asfSuficienciaIngles=null;
		asfFinMalla=null;
		asfAproboActualizar=null;
		asfAptNotaDirector=null;
		asfSegundaCarrera=null;
		asfSegundaCarrera=null;
		asfPersonaPostulante = null;
		asfCedulaDocenteBuscar=null;
		return "irInicio";
	}
	
	public String irCancelarAptitud(){
		asfListDocentesLectoresDto= null;
		asfPrimerLector=null;
		asfSegundoLector=null;
		asfTercerLector=null;
		asfNota1Cadena=null;
		asfNota2Cadena=null;
		asfNota3Cadena=null;
		iniciarParametros();
		limpiar();
		return "irCancelarAptitud";
	}

	public String irAptitudEvaluado(EstudianteAptitudJdbcDto estudiante, Usuario usuario){
		this.asfEstudianteAptitudDtoEditar = estudiante;
		asfUsuarioQueCambia=usuario;
		asfValidadorClic = 0;
		asfLectoresClic=0;
		asfDeshabilitadoTutor=true;
		asfHabilitadorApruebaTutor=true;
		asfHabilitadorPanelTribunal=false;
		if (estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL)
				||estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_DOS_PERIODOS_LABEL)){
			asfHabilitadorActualizarConocimientos=true;
		}
		if (estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_LABEL)
				||estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_DOS_PERIODOS_LABEL)){
			asfHabilitadorActualizarConocimientos=true;
		}
		Validacion vldAux;
		try {
			vldAux = srvValidacionServicio.buscarXtrttId(asfEstudianteAptitudDtoEditar.getTrttId());
			if(vldAux.getVldRslActConocimiento()!=null
					&& vldAux.getVldRslActConocimiento()==ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El postulante aún no ha cancelado el arancel de actualización de conocimientos, no puede continuar");
				limpiar();
				return null;
			}else if (vldAux.getVldRslActConocimiento()!=null
					&& vldAux.getVldRslActConocimiento()==ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El postulante no se ha registrado en actualización de conocimientos, no puede continuar");
				limpiar();
				return null;
			}
		} catch (ValidacionNoEncontradoException e) {
		}
		return "irAptitudEvaluado";
	}
	
	public String irAptitudEvaluadoOtrosMecanismos(EstudianteAptitudOtrosMecanismosJdbcDto estudiante, Usuario usuario){
		iniciarParametros();
		limpiar();
		this.asfEstudianteAptitudOtrosMecanismosDtoEditar = estudiante;
//		asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttTutor(null);
		asfUsuarioQueCambia=usuario;
		asfValidadorClic = 0;
		asfTutorClic = 0;
		asfLectoresClic=0;
		asfListDocentesLectoresDto= new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
		if (estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL)){
			asfHabilitadorActualizarConocimientos=true;
		}
		if (estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_LABEL)){
			asfHabilitadorActualizarConocimientos=true;
		}
		asfDeshabilitadoTutor=true;
		asfMensajeTutor="NO SE ENCONTRARON DATOS CON LOS PARÁMETROS DE BÚSQUEDA INGRESADOS";
		asfHabilitadorGuardar=true;
		asfHabilitadorRegistrarNotas=true;
		asfHabilitadorNotasTribunal=true;
		try {
			if(srvPersonaDtoServicioJdbc.buscarPersonaXApellidosNombres(estudiante.getAsttTutor())){
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttTutor(null);
				asfHabilitadorApruebaTutor=true;
			}
		} catch (PersonaDtoJdbcException | PersonaDtoJdbcNoEncontradoException e) {
			asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttTutor(null);
			asfHabilitadorApruebaTutor=true;
		}
		try {
			vldAux = srvValidacionServicio.buscarXtrttId(asfEstudianteAptitudOtrosMecanismosDtoEditar.getTrttId());
			if(vldAux.getVldRslActConocimiento()!=null
					&& vldAux.getVldRslActConocimiento()==ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El postulante aún no ha cancelado el arancel de actualización de conocimientos, no puede continuar");
				limpiar();
				return null;
			}else if (vldAux.getVldRslActConocimiento()!=null
					&& vldAux.getVldRslActConocimiento()==ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El postulante no se ha registrado en actualización de conocimientos, no puede continuar");
				limpiar();
				return null;
			}
		}catch (Exception e) {
		}
			return "irAptitudEvaluadootrosMecanismos";	
			
	}
	
	public String irAptitudEvaluadoDGA(EstudianteAptitudJdbcDto estudiante, Usuario usuario){
		this.asfEstudianteAptitudDtoEditar = estudiante;
		asfUsuarioQueCambia=usuario;
		asfValidadorClic = 0;
		if (estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL)){
			asfHabilitadorActualizarConocimientos=true;
		}
		if (estudiante.getTrttObsValSec().equals(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_LABEL)){
			asfHabilitadorActualizarConocimientos=true;
		}
		asfDeshabilitadoTutor=true;
		asfHabilitadorAsignar=true;
		asfHabilitadorApruebaTutor=true;
		asfHabilitadorPanelTribunal=false;
		asfHabilitadorGuardarModalTribunal=true;
		return "irAptitudEvaluadoDGA";
	}
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	
	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarCarrera(){
		limpiar();
	}
	
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		asfListEstudianteAptitudDto=null;
		asfListEstudianteAptitudOtrosMecanismosDto=null;
		asfHabilitadorActualizarConocimientos=false;
		asfActualizaConocimientos=null;
		asfRequisitos=null;
		asfRequisitos=null;
		asfSuficienciaIngles=null;
		asfFinMalla=null;
		asfAproboActualizar=null;
		asfAptNotaDirector=null;
		asfSegundaCarrera=null;
		asfPersonaPostulante = null;
		asfEstudianteAptitudDtoBuscar = null;
		asfHabilitadorPanelAptitud=false;
		asfCedulaDocenteBuscar=null;
		asfHabilitadorAsignar=true;
		asfListDocentesDto=null;
		asfListDocentesDtoVisualizar=null;
		asfHabilitadorApruebaTutor=false;
		asfHabilitadorTribunalMayorATres=false;
		asfHabilitadorGuardar=true;
		asfNota1 = null;
		asfNota2 = null;
		asfNota3 = null;
		asfHabilitadorRegistrarNotas=true;
		asfPrimerLector=null;
		asfSegundoLector=null;
		asfTercerLector=null;
		asfListEstudianteAptitudDto=null;
		iniciarParametros();
	}
	
	public void limpiarTablaDocentes(){
		asfListDocentesDto=null;
		asfListDocentesDtoVisualizar=null;
	}
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	

	//iniciar parametros de busqueda
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		asfAranceles=1;
		atfNombreArchivo=null;
		atfIs=null;
		asfCarrera=new CarreraDto();
		asfConvocatoria = new Convocatoria();
		asfHabilitadorActualizarConocimientos=false;
		asfFacultad=new FacultadDto();
//		amfValidadorClic = 0;
		asfActualizaConocimientos=null;
		asfEstudianteAptitudDtoBuscar = new EstudianteAptitudJdbcDto();
		asfListEstudianteAptitudDto = null;
		asfListEstudianteAptitudDto= new ArrayList<EstudianteAptitudJdbcDto>();
		asfEstudianteAptitudDtoEditar = new EstudianteAptitudJdbcDto();
		asfHabilitadorPanelAptitud=false;
		asfCedulaDocenteBuscar=null;
		asfHabilitadorAsignar=true;
		asfApellidoDocenteBuscar=null;
		asfHabilitadorApruebaTutor=false;
		asfHabilitadorPanelTribunal=false;
		asfHabilitadorGuardar=true;
		asfHabilitadorTribunalMayorATres=false;
		asfHabilitadorNotasTribunal=true;
		asfHabilitadorNotasTribunal3=true;
		asfHabilitadorNotasTribunal2=true;
		asfHabilitadorGuardarModalTribunal=true;
		asfHabilitadorRegistrarNotas=true;
		asfHabilitadorBtnAsignar=false;
		asfHabilitadorBtnAsignarLectores=false;
		asfAproboTemaTutor=1;
		asfHabilitadorTribunal3=true;
		asfHabilitadorFecha=false;
		asfHabilitador=false;
	}
	
	/**
	 * Lista los estudiantes evaluados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarAsignadosMecanismos(Usuario usuario){
		try {
			asfListEstudianteAptitudDto = servAmfEstudianteAsignacionDtoServicioJdbc.buscarEstudiantesAsignadoMecanismosXEstudianteXValidadorXCarreraXconvocatoria(asfEstudianteAptitudDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion(),asfCarrera , asfConvocatoria.getCnvId());
		} catch (EvaluadosDtoJdbcException e) {
			asfListEstudianteAptitudDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(e.getMessage());
		} catch (EvaluadosDtoJdbcNoEncontradoException e) {
			asfListEstudianteAptitudDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Lista los estudiantes evaluados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarAsignadosOtrosMecanismos(Usuario usuario){
		try {
			asfListEstudianteAptitudOtrosMecanismosDto = servAmfEstudianteAsignacionDtoServicioJdbc.buscarEstudiantesAsignadoMecanismosOtrosMecanismos(asfEstudianteAptitudDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion(),asfCarrera , asfConvocatoria.getCnvId());
		} catch (EvaluadosDtoJdbcException e) {
			asfListEstudianteAptitudOtrosMecanismosDto = new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
			asfListEstudianteAptitudDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(e.getMessage());
			
		} catch (EvaluadosDtoJdbcNoEncontradoException e) {
			asfListEstudianteAptitudOtrosMecanismosDto = new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
			asfListEstudianteAptitudDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Lista los estudiantes evaluados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarAsignadosTotalesParaEditar(Usuario usuario){
		try {
			asfListEstudianteAptitudDto = servAmfEstudianteAsignacionDtoServicioJdbc.buscarEstudiantesAsignadoMecanismosDGA(asfEstudianteAptitudDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion(),asfFacultad , asfConvocatoria.getCnvId());
		} catch (EvaluadosDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(e.getMessage());
			asfListEstudianteAptitudDto = null;
		} catch (EvaluadosDtoJdbcNoEncontradoException e) {
			asfListEstudianteAptitudDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Lista los docentes segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarDocentes(Usuario usuario, String cedula){
		asfHabilitadorGuardar=true;
		asfCedulaDocenteLectorBuscar=null;
		asfHabilitadorTribunalMayorATres=false;
		asfMensajeLectores="No se puede asignar más lectores, máximo tres integrantes del tribunal.";
		asfListDocentesDtoVisualizar=null;
		asfListDocentesDtoVisualizar=new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
		try {
			asfListDocentesDto=srvDocenteDtoServicioJdbc.buscarDocenteXIdentificacionTitulacion(cedula,asfApellidoDocenteBuscar);
			
			if(asfListDocentesDto.size()>0){
				asfHabilitadorAsignar=false;
				asfMensajeTutor="No hay resultados con los parámetros seleccionados";
				
				for (int i=0;i<asfListDocentesDto.size();i++) {
					boolean op=true;
					for (DocenteTutorTribunalLectorJdbcDto item : asfListDocentesLectoresDto) {
						if(item.getPrsIdentificacion().equals(asfListDocentesDto.get(i).getPrsIdentificacion())) op=false;
					
					}
					if(op){
						asfListDocentesDtoVisualizar.add(asfListDocentesDto.get(i));
					}
				}
			}
			asfCedulaDocenteBuscar=null;
			asfApellidoDocenteBuscar=null;
		} catch (DocenteTutorTribunalLectorJdbcDtoException e) {
			asfListDocentesDto = null;
			asfListDocentesDtoVisualizar=null;
			asfMensajeTutor=(e.getMessage());
		} catch (DocenteTutorTribunalLectorJdbcDtoNoEncontradoException e) {
			asfListDocentesDto = null;
			asfListDocentesDtoVisualizar=null;
			asfMensajeTutor=(e.getMessage());
		}
	}
	
	/**
	 * asigna el docente a la variable que se mostrará en el JSF 
	 */
	public void asignarDocente(DocenteTutorTribunalLectorJdbcDto item, Usuario usuario){
		faseAptitud=1;
		asfListDocentesDto = null;
		asfCedulaDocenteBuscar=item.getPrsIdentificacion();
		asfApellidoDocenteBuscar=null;
		verificarClickAsignarTutorNo();
		asfHabilitadorGuardar=false;
		asfHabilitadorApruebaTutor=false;
		asfHabilitadorPanelTribunal=false;
		asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttTutor(item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
	}
	
	public String guardar(EstudianteAptitudJdbcDto estudiante) {
		estudiante.setAptRequisitos(asfRequisitos);
		estudiante.setAptAproboTutor(asfAproboTemaTutor);
		estudiante.setAptFinMalla(asfFinMalla);
		estudiante.setAptNotaDirector(asfAptNotaDirector);
		estudiante.setAptSuficienciaIngles(asfSuficienciaIngles);
		// Verificamos si aprobó actualizar conocimientos
		if(asfHabilitadorActualizarConocimientos){
			estudiante.setAptAproboActualizar(asfActualizaConocimientos);
		}else{
			estudiante.setAptAproboActualizar(null);
		}
		try {
			if(estudiante!= null){
				RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(estudiante.getTrttCarreraId(), asfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_VALIDADOR_VALUE);
				estudiante.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				@SuppressWarnings("unused")
				Integer aptitudValidador;
				aptitudValidador=servAsfEstudianteValidacionDtoServicioJdbc.guardarAptitud(estudiante, asfUsuarioQueCambia.getUsrIdentificacion() , roflcrr );
				limpiar();
				iniciarParametros();
				asfHabilitadorActualizarConocimientos=false;
				asfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.guardar.exitoso")));
				return "irListarEvaluadosAptitud";
			}
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluadosDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluadosDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e){
		}
		limpiar();
		iniciarParametros();
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.guardar.no.exitoso")));
		asfValidadorClic = 0;
		return null;
	}
	
	public String guardarEdicionAptitud() {
		try {
			if(asfEstudianteAptitudDtoEditar!= null){
				asfEstudianteAptitudDtoEditar.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				Integer aptitudValidador;
				aptitudValidador=servAsfEstudianteValidacionDtoServicioJdbc.guardarEdicionAptitud(asfEstudianteAptitudDtoEditar, asfUsuarioQueCambia.getUsrIdentificacion()  );
				if(aptitudValidador==ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE){
					//******************************************
					//**** MAIL EXAMEN COMPLEXIVO***************
					//******************************************
					//******************************************************************************
					//************************* ACA INICIA EL ENVIO DE MAIL ************************
					//******************************************************************************
					//defino los datos para la plantilla
					Map<String, Object> parametros = new HashMap<String, Object>();
					
					parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsNombres().toUpperCase()));
					try {
						parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsPrimerApellido().toUpperCase())+" "
								+(GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsSegundoApellido().toUpperCase()) == null?"":
									GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsSegundoApellido().toUpperCase())));	
					} catch (Exception e) {
						parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsPrimerApellido().toUpperCase()));
					}
					
					SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
					parametros.put("fechaHora", sdf.format(new Date()));
					String facultadMail = GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getFclDescripcion());
					String carreraMail = GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getCrrDescripcion());
					parametros.put("facultad", facultadMail);
					parametros.put("carrera", carreraMail);
					
					//lista de correos a los que se enviara el mail
					List<String> correosTo = new ArrayList<>();
					
					correosTo.add(asfEstudianteAptitudDtoEditar.getPrsMailPersonal());
					
					//path de la plantilla del mail
					String pathTemplate;
					if(asfEstudianteAptitudDtoEditar.getMcttId()==MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE){
						pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-reconsideracion-apto-complexivo.vm";
					}else{
						pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-reconsideracion-apto-otros-mecanismos.vm";
					}
					//llamo al generador de mails
					GeneradorMails genMail = new GeneradorMails();
					String mailjsonSt;
					try {
						mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_RECONSIDERACION_APTITUD, 
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
			}else{
				//******************************************
				//**** MAIL OTROS MECANISMOS ***************
				//******************************************
				//******************************************************************************
				//************************* ACA INICIA EL ENVIO DE MAIL ************************
				//******************************************************************************
				//defino los datos para la plantilla
				Map<String, Object> parametros = new HashMap<String, Object>();
				
				parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsNombres().toUpperCase()));
				try {
					parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsPrimerApellido().toUpperCase())+" "
							+(GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsSegundoApellido().toUpperCase()) == null?"":
								GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsSegundoApellido().toUpperCase())));	
				} catch (Exception e) {
					parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getPrsPrimerApellido().toUpperCase()));
				}
				SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
				parametros.put("fechaHora", sdf.format(new Date()));
				String facultadMail = GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getFclDescripcion());
				String carreraMail = GeneralesUtilidades.generaStringParaCorreo(asfEstudianteAptitudDtoEditar.getCrrDescripcion());
				parametros.put("facultad", facultadMail);
				parametros.put("carrera", carreraMail);
				//lista de correos a los que se enviará el mail
				List<String> correosTo = new ArrayList<>();
				
				correosTo.add(asfEstudianteAptitudDtoEditar.getPrsMailPersonal());
				
				//path de la plantilla del mail
				String pathTemplate;
				if(asfEstudianteAptitudDtoEditar.getMcttId()==MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE){
					pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-reconsideracion-no-apto-complexivo.vm";
				}else{
					pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-reconsideracion-no-apto-otros-mecanismos.vm";
				}
				//llamo al generador de mails
				GeneradorMails genMail = new GeneradorMails();
				String mailjsonSt;
				try {
					mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_RECONSIDERACION_NO_APTITUD, 
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
				}
				limpiar();
				iniciarParametros();
				asfHabilitadorActualizarConocimientos=false;
				asfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.guardar.exitoso")));
				return "irListarAptitudDga";
			}
		}  catch (EvaluadosDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluadosDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e){
		}
		limpiar();
		iniciarParametros();
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.guardar.no.exitoso")));
		asfValidadorClic = 0;
		return null;
	}

	public String guardarAprobacionTutor(EstudianteAptitudOtrosMecanismosJdbcDto estudiante){
//			try {
//				estudianteParametrosAux = new EstudiantePostuladoJdbcDto();
//				
//				estudianteParametrosAux = srvEstudiantePostuladoDtoServicioJdbc.buscarCulminoMalla(estudiante.getPrsIdentificacion(),
//						estudiante.getCnvId(), estudiante.getCrrId());
//				if(estudianteParametrosAux.getVldCulminoMalla()==ValidacionConstantes.SI_CULMINO_MALLA_VALUE){
//					if(asfAproboTemaTutor==GeneralesConstantes.APP_NO_VALUE){
//						estudianteParametrosAux = srvEstudiantePostuladoDtoServicioJdbc.buscarFechaEgresamiento(estudiante.getPrsIdentificacion(),
//								estudiante.getCnvId(), estudiante.getCrrId());
//							Date fechaEgresamiento = estudianteParametrosAux.getFcesFechaEgresamiento();
//							estudianteParametrosAux = srvEstudiantePostuladoDtoServicioJdbc.buscarFechaAsignacionMecanismo(estudiante.getPrsIdentificacion(),
//									estudiante.getCnvId(), estudiante.getCrrId());
//							Date fechaAsignacionMecanismo = new Date(estudianteParametrosAux.getPrtrFechaEjecucion().getTime());
//							Date fechaActual = new Date();
//							if(fechaEgresamiento.before(fechaAsignacionMecanismo)){
////								System.out.println("ASignación mecanismo");
//								long diffAsignacion = Math.abs(fechaAsignacionMecanismo.getTime() - fechaActual.getTime());
//								long diffDiasAsignacion = diffAsignacion / (24 * 60 * 60 * 1000);
////								System.out.println("-fechaActual"+fechaActual);
////								System.out.println("fechaAsignacionMecanismo"+fechaAsignacionMecanismo);
////								System.out.println("fechaEgresamiento"+fechaEgresamiento);
////								System.out.println(diffDiasAsignacion);
//								if(diffDiasAsignacion>364){
////									
//									/////////////////////ACTIVAR PARA CUANDO SE DESEE LO DE ACT CON
////									srvTramiteTituloDtoServicioJdbc.actualizacionConocimientosDosPeriodos(estudiante.getTrttId());
//									Validacion vldAux = srvValidacionServicio.buscarXtrttId(estudiante.getTrttId());
//									srvValidacionServicio.modificarActualizarConocimientos(vldAux.getVldId());
//									//******************************************************************************
//									//************************* ACA INICIA EL ENVIO DE MAIL ************************
//									//******************************************************************************
//									//defino los datos para la plantilla
//									Map<String, Object> parametros = new HashMap<String, Object>();
//									
//									parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsNombres().toUpperCase()));
//									parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsPrimerApellido().toUpperCase())+" "
//													+(GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsSegundoApellido().toUpperCase()) == null?"":
//														GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsSegundoApellido().toUpperCase())));
//									SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
//									parametros.put("fechaHora", sdf.format(new Date()));
//									String facultadMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getFclDescripcion());
//									String carreraMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getCrrDescripcion());
//									parametros.put("facultad", facultadMail);
//									parametros.put("carrera", carreraMail);
//									
//									//lista de correos a los que se enviara el mail
//									List<String> correosTo = new ArrayList<>();
//									
//									correosTo.add(estudiante.getPrsMailPersonal());
//									
//									//path de la plantilla del mail
//									String pathTemplate;
//									pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-superado-dos-periodos.vm";
//									//llamo al generador de mails
//									GeneradorMails genMail = new GeneradorMails();
//									String mailjsonSt;
//									try {
//										mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_SUPERACION_DOS_PERIODOS, 
//												GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
//										//****envio el mail a la cola
//										//cliente web service
//										Client client = ClientBuilder.newClient();
//										WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
//										MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
//										postForm.add("mail", mailjsonSt);
//						//				String responseData = target.request().post(Entity.form(postForm),String.class);
//										target.request().post(Entity.form(postForm),String.class);
//									} catch (ValidacionMailException e) {
//									}
//									//******************************************************************************
//									//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//									//******************************************************************************
//								}
//							}else if (fechaAsignacionMecanismo.before(fechaEgresamiento)){
////								System.out.println("Fecha de egresamiento");
//								long diffEgresamiento = Math.abs(fechaEgresamiento.getTime() - fechaActual.getTime());
//								long diffDiasEgresamiento = diffEgresamiento / (24 * 60 * 60 * 1000);
////								System.out.println("fechaEgresamiento"+fechaEgresamiento);
////								System.out.println("fechaAsignacionMecanismo"+fechaAsignacionMecanismo);
////								System.out.println("-fechaActual"+fechaActual);
////								System.out.println(diffDiasEgresamiento+" dias");
//								if(diffDiasEgresamiento>365){
//									
//									//////////////////ACTIVAR LUEGO
////									srvTramiteTituloDtoServicioJdbc.actualizacionConocimientosDosPeriodos(estudiante.getTrttId());
//									Validacion vldAux = srvValidacionServicio.buscarXtrttId(estudiante.getTrttId());
//									srvValidacionServicio.modificarActualizarConocimientos(vldAux.getVldId());
//									//******************************************
//									//**** MAIL EXAMEN COMPLEXIVO***************
//									//******************************************
//									//******************************************************************************
//									//************************* ACA INICIA EL ENVIO DE MAIL ************************
//									//******************************************************************************
//									//defino los datos para la plantilla
//									Map<String, Object> parametros = new HashMap<String, Object>();
//									
//									parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsNombres().toUpperCase()));
//									parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsPrimerApellido().toUpperCase())+" "
//													+(GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsSegundoApellido().toUpperCase()) == null?"":
//														GeneralesUtilidades.generaStringParaCorreo(estudiante.getPrsSegundoApellido().toUpperCase())));
//									SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
//									parametros.put("fechaHora", sdf.format(new Date()));
//									String facultadMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getFclDescripcion());
//									String carreraMail = GeneralesUtilidades.generaStringParaCorreo(estudiante.getCrrDescripcion());
//									parametros.put("facultad", facultadMail);
//									parametros.put("carrera", carreraMail);
//									
//									//lista de correos a los que se enviara el mail
//									List<String> correosTo = new ArrayList<>();
//									
//									correosTo.add(estudiante.getPrsMailPersonal());
//									
//									//path de la plantilla del mail
//									String pathTemplate;
//									pathTemplate = "ec/edu/uce/titulacion/jsf/velocity/plantillas/template-superado-dos-periodos.vm";
//									//llamo al generador de mails
//									GeneradorMails genMail = new GeneradorMails();
//									String mailjsonSt;
//									try {
//										mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_SUPERACION_DOS_PERIODOS, 
//												GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
//										//****envio el mail a la cola
//										//cliente web service
//										Client client = ClientBuilder.newClient();
//										WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
//										MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
//										postForm.add("mail", mailjsonSt);
//						//				String responseData = target.request().post(Entity.form(postForm),String.class);
//										target.request().post(Entity.form(postForm),String.class);
//									} catch (ValidacionMailException e) {
//									}
//									//******************************************************************************
//									//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//									//******************************************************************************	
//								}
//							}
//						
//					}
//				}else{
//					Convocatoria cnvAux = srvConvocatoriaServicio.buscarConvocatoriaActiva();
//					if((cnvAux.getCnvId()-estudiante.getCnvId())>1){
//						FacesUtil.limpiarMensaje();
//						FacesUtil.mensajeWarn("El postulante ya cuenta con dos períodos académicos, por favor ingrese la fecha de egresamiento.");
//						asfHabilitadorFecha=true;
//						asfValidadorClic = 0;
//						asfHabilitador=true;
//						return null;
//					}
//				}
//			
				
////				
				try {
					srvAsignacionTitulacionServicio.actualizarPorId(estudiante.getAsttId(), estudiante.getAsttTutor(), estudiante.getAsttTemaTrabajo(),asfAproboTemaTutor);
					srvTramiteTituloServicio.cambiarEstadoAprobadoTutor(estudiante, asfAproboTemaTutor);
				} catch (Exception e) {
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Registro guardado correctamente");
//			} catch (Exception e) {
//				e.printStackTrace();
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("Error al guardar el registro, intente más tarde.");
//			}
		asfValidadorClic = 0;
		limpiar();
		return "irListarEvaluadosAptitudOtrosMecanismos";
	}
	
	public void registrarEgresamiento(){
		try {
			if(asfEstudianteAptitudDtoEditar.getFcesFechaEgresamiento()!=null){
				srvValidacionServicio.cambiarCulminoMalla(estudianteParametrosAux.getVldId());
				srvFichaEstudianteServicio.actualizarFichaEstudianteFechaEgresamiento(estudianteParametrosAux.getFcesId(), 
						asfEstudianteAptitudDtoEditar.getFcesFechaEgresamiento());
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Se ha registrado correctamente la fecha de egresamiento del postulante.");
				asfHabilitador=false;
			}else{
				asfHabilitador=true;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor, ingrese una fecha válida.");
			}
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al guardar la fecha de egresamiento.");
		}
	} 
	
	public String guardarOtrosMecanismos(EstudianteAptitudOtrosMecanismosJdbcDto estudiante) {
		
		if(atfIs!=null){
			OutputStream output=null;
			try {
//				java.util.Date utilDate = new java.util.Date(); //fecha actual
//				long lnMilisegundos = utilDate.getTime();
//				java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
				output = new FileOutputStream(new File(GeneralesConstantes.APP_PATH_ARCHIVOS+"aranceles/", estudiante.getPrsIdentificacion()+"Aranceles"+".pdf"));
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
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor es requisito el informe de no tener aranceles pendientes.");
			return null;
		}
		
		if(faseAptitud==1){
			// En caso de solo guardar la edición del tutor y el tema
			try {
				srvAsignacionTitulacionServicio.actualizarPorId(estudiante.getAsttId(), estudiante.getAsttTutor(), estudiante.getAsttTemaTrabajo(),asfAproboTemaTutor);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.guardardo.exitoso.tema.tutor")));
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.guardardo.no.exitoso.tema.tutor")));
			}
		}else if(faseAptitud==2){
			List<FichaDocente> listaCedulaDocentes = new ArrayList<FichaDocente>();
			try {
				srvAsignacionTitulacionServicio.actualizarPorId(estudiante.getAsttId(), estudiante.getAsttTutor(), estudiante.getAsttTemaTrabajo(),asfAproboTemaTutor);
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(asfCedulaPrimerLector));
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(asfCedulaSegundoLector));
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttAprobaciontutor(AsentamientoNotaConstantes.SI_APROBO_TEMA_TUTOR_VALUE);
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector1(asfNota1.floatValue());
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector2(asfNota2.floatValue());
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector3(null);
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoPrmTrbEscrito(promedioNotas.floatValue());
				if(asfHabilitadorTribunal3){
					listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(asfCedulaTercerLector));
					asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector3(asfNota3.floatValue());
				}
				RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(estudiante.getTrttCarreraId(), asfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_VALIDADOR_VALUE);
				estudiante.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				srvAsentamientoNotaDtoServicioJdbc.guardarAsentamientoNotaPendienteAptitud(asfEstudianteAptitudOtrosMecanismosDtoEditar, roflcrr, listaCedulaDocentes,TramiteTituloConstantes.ESTADO_PROCESO_TRIBUNAL_LECTOR_PENDIENTE_VALUE);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente")));
			} catch (FichaDocenteNoEncontradoException | FichaDocenteException  e1) {
			} catch (RolFlujoCarreraNoEncontradoException e) {
			} catch (RolFlujoCarreraException e) {
			} catch (AsentamientoNotaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
			}
		}else if(faseAptitud==3){
			// En caso de tener todos los paneles con datos completos
			List<FichaDocente> listaCedulaDocentes = new ArrayList<FichaDocente>();
			try {
				estudiante.setAptRequisitos(asfRequisitos);
				estudiante.setAptAproboTutor(asfAproboTemaTutor);
				estudiante.setAptFinMalla(asfFinMalla);
				estudiante.setAptNotaDirector(asfAptNotaDirector);
				estudiante.setAptSuficienciaIngles(asfSuficienciaIngles);
				// Verificamos si aprobó actualizar conocimientos
				if(asfHabilitadorActualizarConocimientos){
					estudiante.setAptAproboActualizar(asfActualizaConocimientos);
				}else{
					estudiante.setAptAproboActualizar(null);
				}
//				try {
					listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(asfCedulaPrimerLector));
//					srvFichaDocenteServicio.ingresarFichaDocente(asfListDocentesLectoresDto.get(0));
//					
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(asfCedulaSegundoLector));
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttAprobaciontutor(AsentamientoNotaConstantes.SI_APROBO_TEMA_TUTOR_VALUE);
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector1(asfNota1.floatValue());
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector2(asfNota2.floatValue());
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector3(null);
				asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoPrmTrbEscrito(promedioNotas.setScale(2,RoundingMode.DOWN).floatValue());
				if(asfHabilitadorTribunal3){
					listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(asfCedulaTercerLector));
					asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoTrbLector3(asfNota3.floatValue());
				}
				RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(estudiante.getTrttCarreraId(), asfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_VALIDADOR_VALUE);
				estudiante.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				if(asfRequisitos==AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE){
					srvAsentamientoNotaDtoServicioJdbc.guardarAsentamientoNotaAptitud(asfEstudianteAptitudOtrosMecanismosDtoEditar, roflcrr, listaCedulaDocentes,TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
				}else if(asfRequisitos==AptitudConstantes.NO_CUMPLE_REQUISITOS_VALUE) {
					srvAsentamientoNotaDtoServicioJdbc.guardarAsentamientoNotaAptitud(asfEstudianteAptitudOtrosMecanismosDtoEditar, roflcrr, listaCedulaDocentes,TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE);
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.exitoso")));
			} catch (FichaDocenteNoEncontradoException | FichaDocenteException e) {
			} catch (RolFlujoCarreraNoEncontradoException e) {
			} catch (RolFlujoCarreraException e) {
			} catch (AsentamientoNotaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
			} catch (AsentamientoNotaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
			}
		}else if(faseAptitud==4){
			// En caso de tener todos los paneles con datos completos
			try {
				estudiante.setAptRequisitos(asfRequisitos);
				estudiante.setAptAproboTutor(asfAproboTemaTutor);
				estudiante.setAptFinMalla(asfFinMalla);
				estudiante.setAptNotaDirector(asfAptNotaDirector);
				estudiante.setAptSuficienciaIngles(asfSuficienciaIngles);
				// Verificamos si aprobó actualizar conocimientos
				if (asfHabilitadorActualizarConocimientos) {
					estudiante
							.setAptAproboActualizar(asfActualizaConocimientos);
				} else {
					estudiante.setAptAproboActualizar(null);
				}
				RolFlujoCarrera roflcrr = new RolFlujoCarrera();
				try {
					roflcrr = srvRolFlujoCarreraServicio
							.buscarPorCarrera(estudiante.getTrttCarreraId(),
									asfUsuarioQueCambia.getUsrId(),
									RolConstantes.ROL_BD_VALIDADOR_VALUE);
					estudiante.setPrtrFechaEjecucion(new Timestamp((new Date())
							.getTime()));
				} catch (Exception e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("El usuario se encuentra deshabilitado en la carrera seleccionada.");
					return "irListarEvaluadosAptitudOtrosMecanismos";
				}
				
				
				if (asfRequisitos == AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE) {
					srvAsentamientoNotaDtoServicioJdbc.guardarAptitud2018(
							asfEstudianteAptitudOtrosMecanismosDtoEditar,
							roflcrr,
							TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
				} else if (asfRequisitos == AptitudConstantes.NO_CUMPLE_REQUISITOS_VALUE) {
					srvAsentamientoNotaDtoServicioJdbc
							.guardarAptitud2018(
									asfEstudianteAptitudOtrosMecanismosDtoEditar,
									roflcrr,
									TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE);
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades(
								"Aptitud.otros.mecanismos.guardar.exitoso")));
				
			} catch (AsentamientoNotaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil
						.mensajeError(MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
			} catch (AsentamientoNotaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil
						.mensajeError(MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
			} catch (Exception e) {
			}
			
			
		}
		asfValidadorClic = 0;
		limpiar();
		return "irListarEvaluadosAptitudOtrosMecanismos";
	}
	
	
	public String verificarClickAceptarTramite(){
		asfCedulaDocenteBuscar=null;
		asfValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		asfValidadorClic = 0;
		return null;
	}
	
	public String verificarClickAsignarTutor(){
		faseAptitud=0;
		asfListDocentesDtoVisualizar=new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
		asfCedulaDocenteBuscar=null;
		asfListDocentesDto=null;
		asfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttTutor(null);
		asfHabilitadorApruebaTutor=false;
		asfHabilitadorGuardar=true;
		asfHabilitadorPanelTribunal=false;
		asfTutorClic = 1;
		asfAproboTemaTutor=1;
		asfNota1Cadena=null;
		asfNota2Cadena=null;
		asfNota3Cadena=null;
		return null;
	}
	
	


	public void download() throws IOException
	{
	    File file = new File(GeneralesConstantes.APP_PATH_ARCHIVOS+"tutor/", asfEstudianteAptitudOtrosMecanismosDtoEditar.getPrsIdentificacion()+"TemaTutor"+".pdf");
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    HttpServletResponse response = 
	            (HttpServletResponse) facesContext.getExternalContext().getResponse();
	    response.reset();
	    response.setHeader("Content-Type", "application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment;filename="+asfEstudianteAptitudOtrosMecanismosDtoEditar.getPrsIdentificacion()+"TemaTutor"+".pdf");
	    OutputStream responseOutputStream = response.getOutputStream();
	    InputStream fileInputStream = new FileInputStream(file);
	    byte[] bytesBuffer = new byte[2048];
	    int bytesRead;
	    while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) 
	    {
	        responseOutputStream.write(bytesBuffer, 0, bytesRead);
	    }
	    responseOutputStream.flush();
	    fileInputStream.close();
	    responseOutputStream.close();
	    facesContext.responseComplete();
	}


	public String verificarClickAsignarTutorNo(){
		asfTutorClic = 0;
		return null;
	}
	
	public String verificarClickAsignarLectores(){
		asfNota1Cadena=null;
		asfNota2Cadena=null;
		asfNota3Cadena=null;
		asfListDocentesDtoVisualizar=new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
		if(asfAproboTemaTutor==0)asfHabilitadorPanelTribunal=true;
		asfLectoresClic = 1;
		asfHabilitadorNotasTribunal=true;
		asfHabilitadorNotasTribunal3=true;
		asfHabilitadorBtnAsignar=true;
		asfHabilitadorApruebaTutor=false;
		return null;
	}
	
	public String verificarClickAsignarLectoresNo(){
		asfListDocentesDto=null;
		asfLectoresClic = 0;
		if(asfListDocentesLectoresDto.size()>1){
			asfHabilitadorGuardarModalTribunal=false;	
		}else{
			asfHabilitadorGuardarModalTribunal=true;
		}
		if(asfPrimerLector!=null && asfSegundoLector!=null){
			asfHabilitadorGuardarModalTribunal=false;	
		}else{
			asfHabilitadorGuardarModalTribunal=true;
		}
		return null;
	}
	
	public void cambioAprobacion(){
		if(asfEstudianteAptitudOtrosMecanismosDtoEditar.getAsttTutor()!=null){
				asfHabilitadorGuardar=true;
				asfHabilitadorRequisitos=true;
				try {
					if(asfAproboTemaTutor==AsentamientoNotaConstantes.SI_APROBO_TEMA_TUTOR_VALUE){
						asfHabilitadorPanelTribunal=true;
						asfHabilitadorGuardar=true;
					}
					else if (asfAproboTemaTutor==AsentamientoNotaConstantes.NO_APROBO_TEMA_TUTOR_VALUE){
						asfHabilitadorPanelTribunal=false;
						asfHabilitadorGuardar=false;
					}
				} catch (Exception e) {
					asfHabilitadorGuardar=true;
					asfHabilitadorPanelTribunal=false;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.seleccione.aprobacion.tutor")));
				}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor seleccione el tutor con el botón ASIGNAR.");
		}
	}
	
	public void asignarDocenteLector(DocenteTutorTribunalLectorJdbcDto item){
		asfNota1 = null;
		asfNota2 = null;
		asfNota3 = null;
		asfHabilitadorTribunalMayorATres=false;
		if(asfListDocentesLectoresDto.size()>2){
			asfMensajeLectores="No se puede asignar más lectores, máximo tres integrantes del tribunal.";
			  asfHabilitadorTribunalMayorATres=true;
		}else{
			boolean op=false;
			for (DocenteTutorTribunalLectorJdbcDto elemento : asfListDocentesLectoresDto) {
				if(item.getPrsIdentificacion().equals(elemento.getPrsIdentificacion())){
					op=true;
				}
			}
			if(op){
				asfHabilitadorTribunalMayorATres=true;
				asfMensajeLectores="El docente ya fue asignado como lector.";
			}else{
				 
				asfListDocentesLectoresDto.add(item);
//				
				Iterator<DocenteTutorTribunalLectorJdbcDto> itr = asfListDocentesDtoVisualizar.iterator();
		        while (itr.hasNext()) {
			       	Object o = itr.next();
			       	if(item.equals(o))
			       	itr.remove();
			    }
			}
		}
		if(asfListDocentesLectoresDto.size()>1){
			asfHabilitadorGuardarModalTribunal=false;	
		}
	}
	
	public void eliminarDocente(DocenteTutorTribunalLectorJdbcDto item){
		Iterator<DocenteTutorTribunalLectorJdbcDto> itr = asfListDocentesLectoresDto.iterator();
        while (itr.hasNext()) {
	       	Object o = itr.next();
	       	if(item.equals(o))
	       	itr.remove();
	    }
        asfHabilitadorTribunalMayorATres=false;
        if(asfListDocentesLectoresDto.size()>2){
			asfHabilitadorGuardarModalTribunal=false;	
		}else{
			asfHabilitadorGuardarModalTribunal=true;
		}
        asfHabilitadorApruebaTutor=false;
	}
	
	public void guardarTribunal(){
		if(asfListDocentesLectoresDto.size()==2){
			asfHabilitadorNotasTribunal=false;
			if(asfListDocentesLectoresDto.get(0).getPrsSegundoApellido()!=null){
				asfPrimerLector=asfListDocentesLectoresDto.get(0).getPrsPrimerApellido()+" "+asfListDocentesLectoresDto.get(0).getPrsSegundoApellido()+" "+asfListDocentesLectoresDto.get(0).getPrsNombres();
			}else{
				asfPrimerLector=asfListDocentesLectoresDto.get(0).getPrsPrimerApellido()+"  "+asfListDocentesLectoresDto.get(0).getPrsNombres();
			}
			if(asfListDocentesLectoresDto.get(1).getPrsSegundoApellido()!=null){
				asfSegundoLector=asfListDocentesLectoresDto.get(1).getPrsPrimerApellido()+" "+asfListDocentesLectoresDto.get(1).getPrsSegundoApellido()+" "+asfListDocentesLectoresDto.get(1).getPrsNombres();	
			}else{
				asfSegundoLector=asfListDocentesLectoresDto.get(1).getPrsPrimerApellido()+"  "+asfListDocentesLectoresDto.get(1).getPrsNombres();
			}
			
			asfCedulaPrimerLector=asfListDocentesLectoresDto.get(0).getPrsIdentificacion();
			asfCedulaSegundoLector=asfListDocentesLectoresDto.get(1).getPrsIdentificacion();
			asfCedulaTercerLector=null;
			asfTercerLector=null;
			asfHabilitadorBtnAsignarLectores=true;
			asfHabilitadorRegistrarNotas=false;
			verificarClickAsignarLectoresNo();
			asfHabilitadorNotasTribunal3=true;
			asfHabilitadorNotasTribunal2=false;
			asfHabilitadorTribunal3=false;
		}else if(asfListDocentesLectoresDto.size()==0 || asfListDocentesLectoresDto.size()==1 || asfListDocentesLectoresDto==null){
			asfHabilitadorTribunalMayorATres=true;
			asfMensajeLectores="El tribunal debe ser conformado por mínimo dos docentes y máximo tres";
			verificarClickAsignarLectores();
			asfHabilitadorRegistrarNotas=true;
		}else{
			asfHabilitadorBtnAsignarLectores=true;
			asfHabilitadorNotasTribunal=false;
			asfHabilitadorNotasTribunal3=false;
			asfHabilitadorNotasTribunal2=false;
			asfHabilitadorRegistrarNotas=false;
			asfHabilitadorTribunal3=true;
			if(asfListDocentesLectoresDto.get(0).getPrsSegundoApellido()!=null){
				asfPrimerLector=asfListDocentesLectoresDto.get(0).getPrsPrimerApellido()+" "+asfListDocentesLectoresDto.get(0).getPrsSegundoApellido()+" "+asfListDocentesLectoresDto.get(0).getPrsNombres();
			}else{
				asfPrimerLector=asfListDocentesLectoresDto.get(0).getPrsPrimerApellido()+"  "+asfListDocentesLectoresDto.get(0).getPrsNombres();
			}
			if(asfListDocentesLectoresDto.get(0).getPrsSegundoApellido()!=null){
				asfSegundoLector=asfListDocentesLectoresDto.get(1).getPrsPrimerApellido()+" "+asfListDocentesLectoresDto.get(1).getPrsSegundoApellido()+" "+asfListDocentesLectoresDto.get(1).getPrsNombres();	
			}else{
				asfSegundoLector=asfListDocentesLectoresDto.get(1).getPrsPrimerApellido()+"  "+asfListDocentesLectoresDto.get(1).getPrsNombres();
			}
			if(asfListDocentesLectoresDto.get(0).getPrsSegundoApellido()!=null){
				asfTercerLector=asfListDocentesLectoresDto.get(2).getPrsPrimerApellido()+" "+asfListDocentesLectoresDto.get(2).getPrsSegundoApellido()+" "+asfListDocentesLectoresDto.get(2).getPrsNombres();	
			}else{
				asfTercerLector=asfListDocentesLectoresDto.get(2).getPrsPrimerApellido()+"  "+asfListDocentesLectoresDto.get(2).getPrsNombres();
			}
			
			asfCedulaPrimerLector=asfListDocentesLectoresDto.get(0).getPrsIdentificacion();
			asfCedulaSegundoLector=asfListDocentesLectoresDto.get(1).getPrsIdentificacion();
			asfCedulaTercerLector=asfListDocentesLectoresDto.get(2).getPrsIdentificacion();
			verificarClickAsignarLectoresNo();	
		}
	}
	
	public void handleFileUpload(FileUploadEvent event){
		try {
			this.atfIs =event.getFile().getInputstream();
			atfNombreArchivo = event.getFile().getFileName();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Archivo "+atfNombreArchivo+" de aranceles cargado con éxito.");
		} catch (IOException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al cargar el archivo, por favor intente más tarde.");
		}
	}
	
	public void registrarNotas(){
		asfHabilitadorBtnAsignarLectores=true;
		asfHabilitadorRegistrarNotas=true;
		sumaNotas=BigDecimal.ZERO;
		if(asfHabilitadorTribunal3){
			sumaNotas=asfNota1.add(asfNota2.add(asfNota3));
			promedioNotas=sumaNotas.divide(new BigDecimal(3),2,RoundingMode.DOWN);
		}else{
			Integer contador=0;
			try {
				sumaNotas=asfNota1.add(asfNota2);
				contador++;
			} catch (Exception e) {
			}
			try {
				sumaNotas=asfNota2.add(asfNota3);
				contador++;
			} catch (Exception e) {
			}
			try {
				sumaNotas=asfNota1.add(asfNota3);
				contador++;
			} catch (Exception e) {
			}
			promedioNotas=sumaNotas.divide(new BigDecimal(2),2,RoundingMode.DOWN);
		}
		int res;
		res=promedioNotas.compareTo(new BigDecimal(7));
		if(res==1 || res==0){
			asfRequisitos=AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE;
			asfHabilitadorRequisitos=false;
			asfHabilitadorPanelAptitud=true;
			faseAptitud=3;
			asfHabilitadorGuardar=false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Las notas se han registrado correctamente, puede guardar la aptitud del postulante.");
		}else{
			asfHabilitadorGuardar=true;
			asfRequisitos=AptitudConstantes.NO_CUMPLE_REQUISITOS_VALUE;
			faseAptitud=2;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.no.aprueba.tribunal")));
		}
		asfHabilitadorNotasTribunal=true;
		asfHabilitadorNotasTribunal3=true;
		asfHabilitadorBtnAsignarLectores=true;
		asfHabilitadorRegistrarNotas=true;
	}
	
	public void cambioRequisitos(){
		asfHabilitadorGuardar=true;
		if(vldAux.getVldRslAprobacionIngles()==GeneralesConstantes.APP_NO_VALUE){
			try {
				if(asfHabilitadorActualizarConocimientos){
					if((asfActualizaConocimientos==AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE || asfActualizaConocimientos==AptitudConstantes.NO_APROBO_ACTUALIZAR_VALUE)
							&& (asfFinMalla==AptitudConstantes.SI_FIN_MALLA_VALUE || asfFinMalla==AptitudConstantes.NO_FIN_MALLA_VALUE)
							){
						asfHabilitadorPanelAptitud=true;
						asfRequisitos=AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE;
					}else{
						asfRequisitos=AptitudConstantes.NO_CUMPLE_REQUISITOS_VALUE;
						asfHabilitadorPanelAptitud=false;
					}
				}else{
					if(asfAranceles==0){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("El posgradista no puede continuar si tiene aranceles pendientes de pago.");
						asfHabilitadorPanelAptitud=false;	
					}else{
						asfHabilitadorPanelAptitud=true;
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				if(asfHabilitadorActualizarConocimientos){
					if((asfActualizaConocimientos==AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE || asfActualizaConocimientos==AptitudConstantes.NO_APROBO_ACTUALIZAR_VALUE)
							&& (asfFinMalla==AptitudConstantes.SI_FIN_MALLA_VALUE || asfFinMalla==AptitudConstantes.NO_FIN_MALLA_VALUE)
							&& (asfSuficienciaIngles==AptitudConstantes.SI_SUFICIENCIA_VALUE || asfSegundaCarrera==AptitudConstantes.NO_SUFICIENCIA_VALUE)){
						asfHabilitadorPanelAptitud=true;
						asfRequisitos=AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE;
					}else{
						asfRequisitos=AptitudConstantes.NO_CUMPLE_REQUISITOS_VALUE;
						asfHabilitadorPanelAptitud=false;
					}
				}else{
					if((asfSuficienciaIngles==GeneralesConstantes.APP_SI_VALUE )
							&& (asfFinMalla==GeneralesConstantes.APP_SI_VALUE || asfFinMalla==GeneralesConstantes.APP_NO_VALUE )){
						asfHabilitadorPanelAptitud=true;
					}else if(asfSuficienciaIngles==GeneralesConstantes.APP_NO_VALUE){
						asfHabilitadorPanelAptitud=false;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeWarn("El postulante no puede continuar en el proceso al no cumplir con los requisitos de aptitud.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
	}
	
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/

	public EstudianteAptitudJdbcDto getAsfEstudianteAptitudDtoBuscar() {
		return asfEstudianteAptitudDtoBuscar;
	}

	public void setAsfEstudianteAptitudDtoBuscar(
			EstudianteAptitudJdbcDto asfEstudianteAptitudDtoBuscar) {
		this.asfEstudianteAptitudDtoBuscar = asfEstudianteAptitudDtoBuscar;
	}

	public EstudianteAptitudJdbcDto getAsfEstudianteAptitudDtoEditar() {
		return asfEstudianteAptitudDtoEditar;
	}

	public void setAsfEstudianteAptitudDtoEditar(
			EstudianteAptitudJdbcDto asfEstudianteAptitudDtoEditar) {
		this.asfEstudianteAptitudDtoEditar = asfEstudianteAptitudDtoEditar;
	}

	public CarreraDto getAsfCarrera() {
		return asfCarrera;
	}

	public void setAsfCarrera(CarreraDto asfCarrera) {
		this.asfCarrera = asfCarrera;
	}

	public List<CarreraDto> getAsfListCarreras() {
		asfListCarreras = asfListCarreras==null?(new ArrayList<CarreraDto>()):asfListCarreras;
		return asfListCarreras;
	}

	public void setAsfListCarreras(List<CarreraDto> asfListCarreras) {
		this.asfListCarreras = asfListCarreras;
	}

	public List<FacultadDto> getAsfListFacultades() {
		asfListFacultades = asfListFacultades==null?(new ArrayList<FacultadDto>()):asfListFacultades;
		return asfListFacultades;
	}

	public void setAsfListFacultades(List<FacultadDto> asfListFacultades) {
		this.asfListFacultades = asfListFacultades;
	}
	
	public Convocatoria getAsfConvocatoria() {
		return asfConvocatoria;
	}

	public void setAsfConvocatoria(Convocatoria asfConvocatoria) {
		this.asfConvocatoria = asfConvocatoria;
	}

	public List<Convocatoria> getAsfListConvocatorias() {
		asfListConvocatorias = asfListConvocatorias==null?(new ArrayList<Convocatoria>()):asfListConvocatorias;
		return asfListConvocatorias;
	}

	public void setAsfListConvocatorias(List<Convocatoria> asfListConvocatorias) {
		this.asfListConvocatorias = asfListConvocatorias;
	}

	public Usuario getAsfUsuarioQueCambia() {
		return asfUsuarioQueCambia;
	}

	public void setAsfUsuarioQueCambia(Usuario asfUsuarioQueCambia) {
		this.asfUsuarioQueCambia = asfUsuarioQueCambia;
	}

	public List<EstudianteAptitudJdbcDto> getAsfListEstudianteAptitudDto() {
		asfListEstudianteAptitudDto = asfListEstudianteAptitudDto==null?(new ArrayList<EstudianteAptitudJdbcDto>()):asfListEstudianteAptitudDto;
		return asfListEstudianteAptitudDto;
	}

	public void setAsfListEstudianteAptitudDto(
			List<EstudianteAptitudJdbcDto> asfListEstudianteAptitudDto) {
		this.asfListEstudianteAptitudDto = asfListEstudianteAptitudDto;
	}

	public Persona getAsfPersonaPostulante() {
		return asfPersonaPostulante;
	}

	public void setAsfPersonaPostulante(Persona asfPersonaPostulante) {
		this.asfPersonaPostulante = asfPersonaPostulante;
	}


	public int getHabilitarCampos() {
		return habilitarCampos;
	}

	public void setHabilitarCampos(int habilitarCampos) {
		this.habilitarCampos = habilitarCampos;
	}

	public Integer getAsfValidadorClic() {
		return asfValidadorClic;
	}

	public void setAsfValidadorClic(Integer asfValidadorClic) {
		this.asfValidadorClic = asfValidadorClic;
	}

	public Boolean getAsfDeshabilitadoTutor() {
		return asfDeshabilitadoTutor;
	}

	public void setAsfDeshabilitadoTutor(Boolean asfDeshabilitadoTutor) {
		this.asfDeshabilitadoTutor = asfDeshabilitadoTutor;
	}

	public List<MecanismoCarrera> getAsfListMecanismoTitulacionCarrera() {
		asfListMecanismoTitulacionCarrera = asfListMecanismoTitulacionCarrera==null?(new ArrayList<MecanismoCarrera>()):asfListMecanismoTitulacionCarrera;
		return asfListMecanismoTitulacionCarrera;
	}

	public void setAsfListMecanismoTitulacionCarrera(
			List<MecanismoCarrera> asfListMecanismoTitulacionCarrera) {
		this.asfListMecanismoTitulacionCarrera = asfListMecanismoTitulacionCarrera;
	}

	public MecanismoCarrera getAsfMecanismoTitulacionCarrera() {
		return asfMecanismoTitulacionCarrera;
	}

	public void setAsfMecanismoTitulacionCarrera(
			MecanismoCarrera asfMecanismoTitulacionCarrera) {
		this.asfMecanismoTitulacionCarrera = asfMecanismoTitulacionCarrera;
	}

	public Integer getAsfRequisitos() {
		return asfRequisitos;
	}

	public void setAsfRequisitos(Integer asfRequisitos) {
		this.asfRequisitos = asfRequisitos;
	}


	public Integer getAsfSegundaCarrera() {
		return asfSegundaCarrera;
	}

	public void setAsfSegundaCarrera(Integer asfSegundaCarrera) {
		this.asfSegundaCarrera = asfSegundaCarrera;
	}

	public Integer getAsfActualizaConocimientos() {
		return asfActualizaConocimientos;
	}

	public void setAsfActualizaConocimientos(Integer asfActualizaConocimientos) {
		this.asfActualizaConocimientos = asfActualizaConocimientos;
	}

	public boolean isAsfHabilitadorActualizarConocimientos() {
		return asfHabilitadorActualizarConocimientos;
	}

	public void setAsfHabilitadorActualizarConocimientos(
			boolean asfHabilitadorActualizarConocimientos) {
		this.asfHabilitadorActualizarConocimientos = asfHabilitadorActualizarConocimientos;
	}

	public FacultadDto getAsfFacultad() {
		return asfFacultad;
	}

	public void setAsfFacultad(FacultadDto asfFacultad) {
		this.asfFacultad = asfFacultad;
	}

	public List<EstudianteAptitudOtrosMecanismosJdbcDto> getAsfListEstudianteAptitudOtrosMecanismosDto() {
		asfListEstudianteAptitudOtrosMecanismosDto = asfListEstudianteAptitudOtrosMecanismosDto==null?(new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>()):asfListEstudianteAptitudOtrosMecanismosDto;
		return asfListEstudianteAptitudOtrosMecanismosDto;
	}

	public void setAsfListEstudianteAptitudOtrosMecanismosDto(
			List<EstudianteAptitudOtrosMecanismosJdbcDto> asfListEstudianteAptitudOtrosMecanismosDto) {
		this.asfListEstudianteAptitudOtrosMecanismosDto = asfListEstudianteAptitudOtrosMecanismosDto;
	}

	public EstudianteAptitudOtrosMecanismosJdbcDto getAsfEstudianteAptitudOtrosMecanismosDtoEditar() {
		return asfEstudianteAptitudOtrosMecanismosDtoEditar;
	}

	public void setAsfEstudianteAptitudOtrosMecanismosDtoEditar(
			EstudianteAptitudOtrosMecanismosJdbcDto asfEstudianteAptitudOtrosMecanismosDtoEditar) {
		this.asfEstudianteAptitudOtrosMecanismosDtoEditar = asfEstudianteAptitudOtrosMecanismosDtoEditar;
	}

	public Integer getAsfTutorClic() {
		return asfTutorClic;
	}

	public void setAsfTutorClic(Integer asfTutorClic) {
		this.asfTutorClic = asfTutorClic;
	}
	public boolean isAsfHabilitadorPanelAptitud() {
		return asfHabilitadorPanelAptitud;
	}

	public void setAsfHabilitadorPanelAptitud(boolean asfHabilitadorPanelAptitud) {
		this.asfHabilitadorPanelAptitud = asfHabilitadorPanelAptitud;
	}

	public String getAsfCedulaDocenteBuscar() {
		return asfCedulaDocenteBuscar;
	}

	public void setAsfCedulaDocenteBuscar(String asfCedulaDocenteBuscar) {
		this.asfCedulaDocenteBuscar = asfCedulaDocenteBuscar;
	}

	public String getAsfApellidoDocenteBuscar() {
		return asfApellidoDocenteBuscar;
	}

	public void setAsfApellidoDocenteBuscar(String asfApellidoDocenteBuscar) {
		this.asfApellidoDocenteBuscar = asfApellidoDocenteBuscar;
	}

	public String getAsfMensajeTutor() {
		return asfMensajeTutor;
	}

	public void setAsfMensajeTutor(String asfMensajeTutor) {
		this.asfMensajeTutor = asfMensajeTutor;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getAsfListDocentesDto() {
		asfListDocentesDto = asfListDocentesDto==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):asfListDocentesDto;
		return asfListDocentesDto;
	}

	public void setAsfListDocentesDto(
			List<DocenteTutorTribunalLectorJdbcDto> asfListDocentesDto) {
		
		this.asfListDocentesDto = asfListDocentesDto;
	}

	public boolean isAsfHabilitadorAsignar() {
		return asfHabilitadorAsignar;
	}

	public void setAsfHabilitadorAsignar(boolean asfHabilitadorAsignar) {
		this.asfHabilitadorAsignar = asfHabilitadorAsignar;
	}

	public boolean isAsfHabilitadorApruebaTutor() {
		return asfHabilitadorApruebaTutor;
	}

	public void setAsfHabilitadorApruebaTutor(boolean asfHabilitadorApruebaTutor) {
		this.asfHabilitadorApruebaTutor = asfHabilitadorApruebaTutor;
	}

	public Integer getAsfAproboTemaTutor() {
		return asfAproboTemaTutor;
	}

	public void setAsfAproboTemaTutor(Integer asfAproboTemaTutor) {
		this.asfAproboTemaTutor = asfAproboTemaTutor;
	}
	
	public boolean isAsfHabilitadorPanelTribunal() {
		return asfHabilitadorPanelTribunal;
	}

	public void setAsfHabilitadorPanelTribunal(boolean asfHabilitadorPanelTribunal) {
		this.asfHabilitadorPanelTribunal = asfHabilitadorPanelTribunal;
	}

	public boolean isAsfHabilitadorGuardar() {
		return asfHabilitadorGuardar;
	}

	public void setAsfHabilitadorGuardar(boolean asfHabilitadorGuardar) {
		this.asfHabilitadorGuardar = asfHabilitadorGuardar;
	}

	public Integer getAsfLectoresClic() {
		return asfLectoresClic;
	}

	public void setAsfLectoresClic(Integer asfLectoresClic) {
		this.asfLectoresClic = asfLectoresClic;
	}
	
	public List<DocenteTutorTribunalLectorJdbcDto> getAsfListDocentesLectoresDto() {
		asfListDocentesLectoresDto = asfListDocentesLectoresDto==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):asfListDocentesLectoresDto;
		return asfListDocentesLectoresDto;
	}

	public void setAsfListDocentesLectoresDto(
			List<DocenteTutorTribunalLectorJdbcDto> asfListDocentesLectoresDto) {
		this.asfListDocentesLectoresDto = asfListDocentesLectoresDto;
	}

	public String getAsfPrimerLector() {
		return asfPrimerLector;
	}

	public void setAsfPrimerLector(String asfPrimerLector) {
		this.asfPrimerLector = asfPrimerLector;
	}

	public String getAsfSegundoLector() {
		return asfSegundoLector;
	}

	public void setAsfSegundoLector(String asfSegundoLector) {
		this.asfSegundoLector = asfSegundoLector;
	}

	public String getAsfTercerLector() {
		return asfTercerLector;
	}

	public void setAsfTercerLector(String asfTercerLector) {
		this.asfTercerLector = asfTercerLector;
	}

	public String getAsfCedulaDocenteLectorBuscar() {
		return asfCedulaDocenteLectorBuscar;
	}

	public void setAsfCedulaDocenteLectorBuscar(String asfCedulaDocenteLectorBuscar) {
		this.asfCedulaDocenteLectorBuscar = asfCedulaDocenteLectorBuscar;
	}

	public boolean isAsfHabilitadorTribunalMayorATres() {
		return asfHabilitadorTribunalMayorATres;
	}

	public void setAsfHabilitadorTribunalMayorATres(
			boolean asfHabilitadorTribunalMayorATres) {
		this.asfHabilitadorTribunalMayorATres = asfHabilitadorTribunalMayorATres;
	}
	
	public String getAsfMensajeLectores() {
		return asfMensajeLectores;
	}

	public void setAsfMensajeLectores(String asfMensajeLectores) {
		this.asfMensajeLectores = asfMensajeLectores;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getAsfListDocentesDtoVisualizar() {
		return asfListDocentesDtoVisualizar;
	}

	public void setAsfListDocentesDtoVisualizar(
			List<DocenteTutorTribunalLectorJdbcDto> asfListDocentesDtoVisualizar) {
		asfListDocentesDtoVisualizar = asfListDocentesDtoVisualizar==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):asfListDocentesDtoVisualizar;
		this.asfListDocentesDtoVisualizar = asfListDocentesDtoVisualizar;
	}
	public boolean isAsfHabilitadorNotasTribunal() {
		return asfHabilitadorNotasTribunal;
	}

	public void setAsfHabilitadorNotasTribunal(boolean asfHabilitadorNotasTribunal) {
		this.asfHabilitadorNotasTribunal = asfHabilitadorNotasTribunal;
	}

	public boolean isAsfHabilitadorNotasTribunal3() {
		return asfHabilitadorNotasTribunal3;
	}

	public void setAsfHabilitadorNotasTribunal3(boolean asfHabilitadorNotasTribunal3) {
		this.asfHabilitadorNotasTribunal3 = asfHabilitadorNotasTribunal3;
	}

	public BigDecimal getAsfNota1() {
		return asfNota1;
	}

	public void setAsfNota1(BigDecimal asfNota1) {
		this.asfNota1 = asfNota1;
	}

	public BigDecimal getAsfNota2() {
		return asfNota2;
	}

	public void setAsfNota2(BigDecimal asfNota2) {
		this.asfNota2 = asfNota2;
	}

	public BigDecimal getAsfNota3() {
		return asfNota3;
	}

	public void setAsfNota3(BigDecimal asfNota3) {
		this.asfNota3 = asfNota3;
	}
	

	public boolean isAsfHabilitadorGuardarModalTribunal() {
		return asfHabilitadorGuardarModalTribunal;
	}

	public void setAsfHabilitadorGuardarModalTribunal(
			boolean asfHabilitadorGuardarModalTribunal) {
		this.asfHabilitadorGuardarModalTribunal = asfHabilitadorGuardarModalTribunal;
	}

	public boolean isAsfHabilitadorRegistrarNotas() {
		return asfHabilitadorRegistrarNotas;
	}

	public void setAsfHabilitadorRegistrarNotas(boolean asfHabilitadorRegistrarNotas) {
		this.asfHabilitadorRegistrarNotas = asfHabilitadorRegistrarNotas;
	}

	public BigDecimal getSumaNotas() {
		return sumaNotas;
	}

	public void setSumaNotas(BigDecimal sumaNotas) {
		this.sumaNotas = sumaNotas;
	}

	public BigDecimal getPromedioNotas() {
		return promedioNotas;
	}

	public void setPromedioNotas(BigDecimal promedioNotas) {
		this.promedioNotas = promedioNotas;
	}

	public boolean isAsfHabilitadorBtnAsignar() {
		return asfHabilitadorBtnAsignar;
	}

	public void setAsfHabilitadorBtnAsignar(boolean asfHabilitadorBtnAsignar) {
		this.asfHabilitadorBtnAsignar = asfHabilitadorBtnAsignar;
	}

	public boolean isAsfHabilitadorBtnAsignarLectores() {
		return asfHabilitadorBtnAsignarLectores;
	}

	public void setAsfHabilitadorBtnAsignarLectores(
			boolean asfHabilitadorBtnAsignarLectores) {
		this.asfHabilitadorBtnAsignarLectores = asfHabilitadorBtnAsignarLectores;
	}

	public Integer getFaseAptitud() {
		return faseAptitud;
	}

	public void setFaseAptitud(Integer faseAptitud) {
		this.faseAptitud = faseAptitud;
	}

	public boolean isAsfHabilitadorRequisitos() {
		return asfHabilitadorRequisitos;
	}

	public void setAsfHabilitadorRequisitos(boolean asfHabilitadorRequisitos) {
		this.asfHabilitadorRequisitos = asfHabilitadorRequisitos;
	}

	public String getAsfNota1Cadena() {
		return asfNota1Cadena;
	}

	public void setAsfNota1Cadena(String asfNota1Cadena) {
		this.asfNota1Cadena = asfNota1Cadena;
	}

	public String getAsfNota2Cadena() {
		return asfNota2Cadena;
	}

	public void setAsfNota2Cadena(String asfNota2Cadena) {
		this.asfNota2Cadena = asfNota2Cadena;
	}

	public String getAsfNota3Cadena() {
		return asfNota3Cadena;
	}

	public void setAsfNota3Cadena(String asfNota3Cadena) {
		this.asfNota3Cadena = asfNota3Cadena;
	}

	public String getAsfCedulaPrimerLector() {
		return asfCedulaPrimerLector;
	}

	public void setAsfCedulaPrimerLector(String asfCedulaPrimerLector) {
		this.asfCedulaPrimerLector = asfCedulaPrimerLector;
	}

	public String getAsfCedulaSegundoLector() {
		return asfCedulaSegundoLector;
	}

	public void setAsfCedulaSegundoLector(String asfCedulaSegundoLector) {
		this.asfCedulaSegundoLector = asfCedulaSegundoLector;
	}

	public String getAsfCedulaTercerLector() {
		return asfCedulaTercerLector;
	}

	public void setAsfCedulaTercerLector(String asfCedulaTercerLector) {
		this.asfCedulaTercerLector = asfCedulaTercerLector;
	}

	public boolean isAsfHabilitadorFecha() {
		return asfHabilitadorFecha;
	}

	public void setAsfHabilitadorFecha(boolean asfHabilitadorFecha) {
		this.asfHabilitadorFecha = asfHabilitadorFecha;
	}

	public EstudiantePostuladoJdbcDto getEstudianteParametrosAux() {
		return estudianteParametrosAux;
	}

	public void setEstudianteParametrosAux(
			EstudiantePostuladoJdbcDto estudianteParametrosAux) {
		this.estudianteParametrosAux = estudianteParametrosAux;
	}

	public boolean isAsfHabilitador() {
		return asfHabilitador;
	}

	public void setAsfHabilitador(boolean asfHabilitador) {
		this.asfHabilitador = asfHabilitador;
	}

	public Integer getAsfSuficienciaIngles() {
		return asfSuficienciaIngles;
	}

	public void setAsfSuficienciaIngles(Integer asfSuficienciaIngles) {
		this.asfSuficienciaIngles = asfSuficienciaIngles;
	}

	public Integer getAsfFinMalla() {
		return asfFinMalla;
	}

	public void setAsfFinMalla(Integer asfFinMalla) {
		this.asfFinMalla = asfFinMalla;
	}

	public Integer getAsfAproboActualizar() {
		return asfAproboActualizar;
	}

	public void setAsfAproboActualizar(Integer asfAproboActualizar) {
		this.asfAproboActualizar = asfAproboActualizar;
	}

	public BigDecimal getAsfAptNotaDirector() {
		return asfAptNotaDirector;
	}

	public void setAsfAptNotaDirector(BigDecimal asfAptNotaDirector) {
		this.asfAptNotaDirector = asfAptNotaDirector;
	}

	public Validacion getVldAux() {
		return vldAux;
	}

	public void setVldAux(Validacion vldAux) {
		this.vldAux = vldAux;
	}

	public Integer getAsfAranceles() {
		return asfAranceles;
	}

	public void setAsfAranceles(Integer asfAranceles) {
		this.asfAranceles = asfAranceles;
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

	public boolean isAsfHabilitadorNotasTribunal2() {
		return asfHabilitadorNotasTribunal2;
	}

	public void setAsfHabilitadorNotasTribunal2(boolean asfHabilitadorNotasTribunal2) {
		this.asfHabilitadorNotasTribunal2 = asfHabilitadorNotasTribunal2;
	}
	
	
	
	
	
	
}

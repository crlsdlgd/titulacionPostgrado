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
   
 ARCHIVO:     ValidacionPosgradistaForm.java	  
 DESCRIPCION: Bean que maneja las peticiones del rol Secretaria.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-04-2018			Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import org.primefaces.model.DefaultStreamedContent;

import ec.edu.uce.clienteSenescytDataJXRS.dtos.Detalle;
import ec.edu.uce.clienteSenescytDataJXRS.principal.InteroperadorClienteSenescyt;
import ec.edu.uce.envioMail.excepciones.ValidacionMailException;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.RegistroSenescytDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EstudianteValidacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TramiteTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Titulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (managed bean) ValidacionPosgradistaForm.
 * Managed Bean que maneja las peticiones del rol Secretaria.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="validacionPosgradistaForm")
@SessionScoped
public class ValidacionPosgradistaForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
	// *****************************************************************/
	// ******************* DataSource de ValidacionSecretariaForm*******/
	// *****************************************************************/
//	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
//	DataSource ds;

//	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
//	private EntityManager em;
		
	// *****************************************************************/
	// ******************* Variables de ValidacionSecretariaForm********/
	// *****************************************************************/
	private CarreraDto vsfCarrera;

	private FacultadDto vsfFacultad;
	private List<CarreraDto> vsfListCarreras;
	private EstudianteValidacionJdbcDto vsfEstudianteValidacionJdbcDtoBuscar;
	private EstudianteValidacionJdbcDto vsfEstudianteValidacionJdbcDtoEditar;
	

	private Usuario vsfUsuarioQueCambia;
	private List<EstudianteValidacionJdbcDto> vsfListEstudianteValidacionJdbcDto;
	private Persona vsfPersonaPostulante;
	
	private Convocatoria vsfConvocatoria;
	private List<Convocatoria> vsfListConvocatorias;
	
	private List<CarreraDto> vsfListCarrerasAsignar;
	private Carrera vsfNuevaCarrera;
	private int fechaRegimenPostulacion;
	
	private int habilitarCampos;
	private Integer vsfValidadorClic;
	
	private boolean vsfHabilitadorComplex;
	private boolean vsfHabilitadorFechaCulmina;
	private boolean vsfHabilitadorAsignaTutor;
	private boolean vsfHabilitadorFechaAsignacion;
	private boolean vsfHabilitadorMalla;

	private Boolean vsfDeshabilitadoExportar;
	private Date fechaLimiteMaxima;
	private Date fechaLimiteMinima ;
	private Date fechaInicioConvocatoria ;
	
	private int vsfIdoneidad;
	private Boolean vsfResultadoConsulta;
	private List<RegistroSenescytDto> vsfListaTitulos;
	private DefaultStreamedContent image;
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private ConvocatoriaServicio srvConvocatoriaServicio;
		
	
	@EJB
	private UsuarioRolServicio srvUsuarioRolServicio;

	@EJB
	private FacultadServicio srvFacultadServicio;
		
	@EJB
	private CarreraServicio srvCarreraServicio;
	
	@EJB
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
	
	
	@EJB
	private EstudianteValidacionDtoServicioJdbc servVsfEstudianteValidacionDtoServicioJdbc;
	
	@EJB
	private EstudianteValidacionDtoServicio servVsfEstudianteValidacionDtoServicio;
	
	@EJB
	private	TramiteTituloDtoServicioJdbc servTramiteTituloDtoServicioJdbc;
	
	@EJB
	private TramiteTituloServicio servTramiteTituloServicio;
	
	@EJB
	private PersonaServicio servPersonaServicio;
	
	@EJB
	private TituloServicio servTituloServicio;
	
	
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
	public String irlistarPostuladosValidacion(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
		this.vsfUsuarioQueCambia=usuario;
		vsfDeshabilitadoExportar=true;
		try {
//			List<Convocatoria> cnvAux = srvConvocatoriaServicio.buscarConvocatoriasActivaFaseIdoneidad();
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacionValidador(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
				vsfListConvocatorias =  srvConvocatoriaServicio.listarTodosActivas();
			vsfListCarreras=servVsfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(vsfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
//			if(!(cnvAux!=null)){
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeInfo("Cronograma deshabilitado para la fase de idoneidad.");
//				return null;		
//			}
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (SQLException e) {
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Error al listar las convocatorias, por favor comuníquese con la Dirección de Tecnologías.");
			return null;
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		vsfUsuarioQueCambia = usuario;
		return "irListarPostuladosValidacion";
	}
	
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarValidacion(){
		vsfPersonaPostulante = null;
		return "irInicio";

	}
	/** 
	 * Método que muestra el mensaje que direciona a la página de Reportes de Validados
	 * @param usuario que realiza la petición de la página
	 * @return irReportesPostulados
	 */
	public String irReportesValidados(Usuario usuario){
		//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
				
				try {
					UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
					if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
						FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
						return null;
					}
				
				this.vsfUsuarioQueCambia=usuario;
				vsfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
				vsfListCarreras=servVsfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
				limpiar();
				iniciarParametros();
				}
				catch (CarreraDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (SQLException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				} catch (ConvocatoriaNoEncontradoException e) {
					FacesUtil.mensajeInfo("No se encontró ninguna convocatoria ");
					return null;
				} catch (UsuarioRolNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				} catch (UsuarioRolException e) {
					FacesUtil.mensajeError(e.getMessage());
					return null;
				}
				vsfUsuarioQueCambia = usuario;
			vsfDeshabilitadoExportar = true;
		return "irReportesValidados";
	}
	
	/** 
	 * Método que muestra el mensaje que direciona a la página de validación
	 * @param usuario que realiza la petición de la página
	 * @return irValidacionPostulado
	 */
	public String irValidacionPostulado(EstudianteValidacionJdbcDto estudiante, Usuario usuario) {
		vsfHabilitadorComplex = true;
		vsfHabilitadorFechaCulmina = true;
		vsfHabilitadorAsignaTutor = true;
		vsfHabilitadorFechaAsignacion = true;
		vsfHabilitadorMalla = true;
		this.vsfEstudianteValidacionJdbcDtoEditar = estudiante;
		vsfUsuarioQueCambia=usuario;
		vsfNuevaCarrera =  null;
		vsfNuevaCarrera=new Carrera();
		habilitarCampos = 0;
		vsfValidadorClic = 0;
		
		// Proceso de consulta a través de la Senescyt para validar el título y declarar idoneidad
		vsfResultadoConsulta = false;
		vsfListaTitulos= new ArrayList<RegistroSenescytDto>();
		try {
			InteroperadorClienteSenescyt inter = new InteroperadorClienteSenescyt();
			
			try {
				inter.consultar(estudiante.getPrsIdentificacion());
				for (Detalle item : inter.getJsonDinardapSenescyt().getFichaGeneral().getInstituciones().getDetalle()) {
					RegistroSenescytDto aux = new RegistroSenescytDto();
					int ubicacionCaracter = item.getRegistros().get(1).getValor().indexOf("T");
					String fechaRegistro = item.getRegistros().get(1).getValor().substring(0,ubicacionCaracter);
					aux.setFechaRegistroSenescyt(fechaRegistro);
					aux.setUniversidadSenescyt(item.getRegistros().get(2).getValor());
					aux.setDetalleSenescyt(item.getRegistros().get(3).getValor());
					aux.setRegistroSenescyt(item.getRegistros().get(4).getValor());
					vsfListaTitulos.add(aux);
					vsfResultadoConsulta=true;
				}
			} catch (Exception e) {
			}
			try {
				inter.consultarSimple(estudiante.getPrsIdentificacion());
				RegistroSenescytDto aux = new RegistroSenescytDto();
				int ubicacionCaracter = inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(0).getValor().indexOf("T");
				String fechaRegistro = inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(0).getValor().substring(0,ubicacionCaracter);
				aux.setFechaRegistroSenescyt(fechaRegistro);
				aux.setDetalleSenescyt(inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(3).getValor());
				aux.setRegistroSenescyt(inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(4).getValor());
				aux.setUniversidadSenescyt(inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(2).getValor());
				vsfListaTitulos.add(aux);
				vsfResultadoConsulta=true;
			} catch (Exception e) {
				inter.consultarSimple(estudiante.getPrsIdentificacion());
				RegistroSenescytDto aux = new RegistroSenescytDto();
				int ubicacionCaracter = inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(0).getValor().indexOf("T");
//				String fechaRegistro = inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(0).getValor().substring(0,ubicacionCaracter);
				aux.setFechaRegistroSenescyt(null);
				aux.setDetalleSenescyt(inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(3).getValor());
				aux.setRegistroSenescyt(inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(4).getValor());
				aux.setUniversidadSenescyt(inter.getJsonDinardapSenescytSimple().getFichaGeneral().getInstituciones().getDetalle().getItems().getRegistros().get(2).getValor());
				vsfListaTitulos.add(aux);
				vsfResultadoConsulta=true;
			}
		} catch (Exception e) {
		}
		if(vsfResultadoConsulta){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Por favor verifique la información obtenida del Senescyt.");
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se obtuvo resultados de la consulta al Senescyt, realice la verificación manual por favor.");
		}
		byte[] data;
		try {
			Persona ppfPersona = servPersonaServicio.buscarPorIdentificacion(estudiante.getPrsIdentificacion());
			
			String[] partesNotas =  ppfPersona.getPrsUbicacionFoto().split("\\.");
			Path path = Paths.get(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+ppfPersona.getPrsIdentificacion()+"."+partesNotas[1].toString());
//			Path path = Paths.get("d:/archivos/"+"Fotografia_1714991989.jpg");
			data = Files.readAllBytes(path);
			image = new DefaultStreamedContent(new ByteArrayInputStream(data), Files.probeContentType(path));
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return "irValidacionPostulado";
	}
	
	/** 
	 * Método que guarda la selección del Coordinador del programa para el registro de la Senescyt
	 * @param String registro Es la cadena que se almacenará
	 * @return irValidacionPostulado
	 */
	public void registrarSenescyt(String registro) {
		try {
			if(servVsfEstudianteValidacionDtoServicio.guardarRegistroSenescyt(vsfEstudianteValidacionJdbcDtoEditar.getFcesId(), registro)){
				vsfEstudianteValidacionJdbcDtoEditar.setPrtrTtlRegSenescyt(registro);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Se ha modificado el registro del Senescyt, por favor continue con la declaratoria de idoneidad");
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Error al guardar el registro del Senescyt, por favor intente más tarde");
			}
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Error al guardar el registro del Senescyt, por favor intente más tarde");
		}
	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarCarrera(){
		vsfDeshabilitadoExportar=true;
		vsfListEstudianteValidacionJdbcDto=null;
	}
	
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		vsfDeshabilitadoExportar=true;
		vsfListEstudianteValidacionJdbcDto=null;
		vsfEstudianteValidacionJdbcDtoBuscar = null;
		vsfDeshabilitadoExportar = true;
		
		iniciarParametros();
	}
	
	/**
	 * Lista las postulaciones del estudiante segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarPostulacion(Usuario usuario){
		try {
			vsfListEstudianteValidacionJdbcDto = servVsfEstudianteValidacionDtoServicioJdbc.buscarPostulacionesEstudianteXIndetificacionXCarreraXConvocatoria(vsfEstudianteValidacionJdbcDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion(),vsfCarrera , vsfConvocatoria.getCnvId());
		} catch (EstudiantePostuladoJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.listar.postulacion.facultad.carrera.identificacion.estado.exception")));
		} catch (EstudiantePostuladoJdbcDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.listar.postulacion.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		}
	}
	

	public void validarFechaFinCohorte() {
		try {
			Date fechaActual = new Date();
			int diasTranscurridos = GeneralesUtilidades.calcularDiferenciFechas(fechaActual,vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaFinCohorte());
			if(diasTranscurridos>3650){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn("El postulante será declarado NO IDÓNEO por haber sobrepasado el límite de 10 años.");
			}	
		} catch (Exception e) {
		}
	}
	/**
	 * Guarda los parámetros ingresados por la persona encargada de verificar la idoneidad del postulante 
	 */

	@SuppressWarnings("unused")
	public String guardar(EstudianteValidacionJdbcDto estudiante) {
		RegistroSenescytDto nuevoRegistro = new RegistroSenescytDto();
		boolean op=false;
//		for (RegistroSenescytDto item : vsfListaTitulos) {
//			if(vsfEstudianteValidacionJdbcDtoEditar.getPrtrTtlRegSenescyt().equals(item.getRegistroSenescyt())){
//				nuevoRegistro = item;
//				op=true;
//				nuevoRegistro.setDetalleSenescyt(estudiante.getFcesTituloBachiller());
//				break;
//			}
//		}
//		if(op){

			Integer porcentaje = 0;
			String pathTemplate = null;
			String pathTemplateCoordinador = null;
			Date fechaActual = new Date();
			Integer diasCohorte=GeneralesUtilidades.calcularDiferenciFechas(vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaInicioCohorte(), vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaFinCohorte());
			Integer diasTranscurridos = GeneralesUtilidades.calcularDiferenciFechas(vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaInicioCohorte(), fechaActual);
			if(vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaFinCohorte().after(fechaActual)){
				if(diasCohorte < 1096){ // Si el programa es menor a tres años
					if(diasTranscurridos>=(diasCohorte/4)){
						porcentaje = 25;
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-selecciona.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-selecciona-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.SI_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
					}else{
						porcentaje = 25;
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-no50malla.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-no50malla-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.NO_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.NO_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.NO_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
					}
				}else{//Si el programa es menor a 4 años
					if(diasTranscurridos>=(diasCohorte/2)){
						porcentaje = 50;
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-selecciona.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-selecciona-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.SI_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
					}else{
						porcentaje = 50;
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-no50malla.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-no50malla-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.NO_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.NO_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.NO_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
					}
				}
			}else{
				diasTranscurridos = GeneralesUtilidades.calcularDiferenciFechas(vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaFinCohorte(), fechaActual);
				while(true) {
					if(diasTranscurridos<183){// caso menor a seis meses
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-primera-prorroga.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-primera-prorroga-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.SI_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(0);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
						break;
					}else if(diasTranscurridos<365){// caso menor a un año
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-segunda-prorroga.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-segunda-prorroga-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.SI_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(0);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
						break;
					}else if(diasTranscurridos<1825){//Caso menor a 5 años
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-actualizacion.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-actualizacion-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.SI_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(0);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
						break;
					}else if(diasTranscurridos<3650){//Caso menor a 10 años
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-homologacion.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-idoneo-homologacion-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.SI_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.SI_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(0);
						break;
					}else{// Caso mayor a 10 años
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
						pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-mayor10anios.vm";
						pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-mayor10anios-coordinador.vm";
						vsfEstudianteValidacionJdbcDtoEditar.setTrttEstadoProceso(ValidacionConstantes.NO_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setTrttObsValSec(ValidacionConstantes.NO_ES_IDONEO_LABEL);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslIdoneidad(ValidacionConstantes.NO_ES_IDONEO_VALUE);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldSegundaProrroga(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslActConocimiento(1);
						vsfEstudianteValidacionJdbcDtoEditar.setVldRslHomologacion(1);
						break;
					}
				}
				
			}
//			if(vsfEstudianteValidacionJdbcDtoEditar.getVldRslAprobacionIngles()==GeneralesConstantes.APP_NO_VALUE){
//				pathTemplate = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-no-culmino-ingles.vm";
//				pathTemplateCoordinador = "ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-no-culmino-inglesCoordinador.vm";
	//
//			}
			try {
				Persona prsDirector = new Persona();
				
				try {
					RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarreraXUsuarioSecretaria(vsfEstudianteValidacionJdbcDtoEditar.getTrttCarreraId());
					prsDirector = servPersonaServicio.buscarPersonaXRolXCarrera(RolConstantes.ROL_BD_EVALUADOR_VALUE, vsfEstudianteValidacionJdbcDtoEditar.getTrttCarreraId());
					
//					Titulo ttlNuevo = null;
//					try {
//						ttlNuevo = servTituloServicio.buscarPorDescripcion(nuevoRegistro.getDetalleSenescyt());
//					} catch (Exception e) {
//						
//					}
//					if(ttlNuevo!=null){
//						vsfEstudianteValidacionJdbcDtoEditar.setFcesTituloBachillerId(ttlNuevo.getTtlId());
//						vsfEstudianteValidacionJdbcDtoEditar.setFcesTituloBachiller(ttlNuevo.getTtlDescripcion());
//					}else{
//						nuevoRegistro.setSexo(vsfEstudianteValidacionJdbcDtoEditar.getPrsSexo());
//						ttlNuevo = servTituloServicio.crearNuevoTitulo(nuevoRegistro);
//						vsfEstudianteValidacionJdbcDtoEditar.setFcesTituloBachillerId(ttlNuevo.getTtlId());
//						vsfEstudianteValidacionJdbcDtoEditar.setFcesTituloBachiller(ttlNuevo.getTtlDescripcion());
//					}
					servVsfEstudianteValidacionDtoServicio.editar(vsfEstudianteValidacionJdbcDtoEditar,vsfUsuarioQueCambia.getUsrIdentificacion(),
							vsfEstudianteValidacionJdbcDtoEditar.getTrttCarreraId(),roflcrr);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//********************************O**********************************************
				//************************* ACA INICIA EL ENVIO DE MAIL ************************
				//******************************************************************************
				//defino los datos para la plantilla
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("nombres", vsfEstudianteValidacionJdbcDtoEditar.getPrsNombres().toUpperCase());
				try {
					parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(vsfEstudianteValidacionJdbcDtoEditar.getPrsPrimerApellido().toUpperCase()+" "
							+(vsfEstudianteValidacionJdbcDtoEditar.getPrsSegundoApellido().toUpperCase() == null?"":vsfEstudianteValidacionJdbcDtoEditar.getPrsSegundoApellido().toUpperCase())));	
				} catch (Exception e) {
					 parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(vsfEstudianteValidacionJdbcDtoEditar.getPrsPrimerApellido().toUpperCase()+" "));
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
				parametros.put("fechaHora", sdf.format(new Date()));
				String facultadMail = GeneralesUtilidades.generaStringParaCorreo(vsfEstudianteValidacionJdbcDtoEditar.getFclDescripcion());
				String carreraMail = GeneralesUtilidades.generaStringParaCorreo(vsfEstudianteValidacionJdbcDtoEditar.getCrrDescripcion());
				parametros.put("facultad", facultadMail);
				parametros.put("carrera", carreraMail);
				parametros.put("carreraDetalle", vsfEstudianteValidacionJdbcDtoEditar.getCrrDetalle());
				SimpleDateFormat formato = 
						new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
				String fecha = formato.format(vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaInicioCohorte());
				String fecha2 = formato.format(vsfEstudianteValidacionJdbcDtoEditar.getFcesFechaFinCohorte());
				parametros.put("fechaInicio",fecha);
				parametros.put("fechaFin", fecha2);
				if(pathTemplate.equals("ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-noIdoneo-no50malla.vm")){
					parametros.put("porcentaje", porcentaje);
				}
				//lista de correos a los que se enviara el mail
				List<String> correosTo = new ArrayList<>();
				correosTo.add(vsfEstudianteValidacionJdbcDtoEditar.getPrsMailPersonal());
				List<String> correosCc = new ArrayList<>();
				try {
					correosCc.add(prsDirector.getPrsMailInstitucional());	
				} catch (Exception e) {
				}
				
				//path de la plantilla del mail
				
				//llamo al generador de mails
				GeneradorMails genMail = new GeneradorMails();
				String mailjsonSt = null;
				try {
					mailjsonSt = genMail.generarMailJson(correosTo, correosCc, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_VALIDACION, 
							GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
				} catch (ValidacionMailException e) {
				}
				//****envio el mail a la cola
				//cliente web service
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target("http://10.20.1.94:8080/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
				MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
				postForm.add("mail", mailjsonSt);
//				String responseData = target.request().post(Entity.form(postForm),String.class);
				target.request().post(Entity.form(postForm),String.class);
				//******************************************************************************
				//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
				//******************************************************************************
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Declaratoria de idoneidad realizada correctamente.");
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Excepción al guardar el Trámite del postulante.");
			} 
			
			limpiar();
			iniciarParametros();
			return "irListarPostulantesValidacion";
//		}else{
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError("El registro de la Senescyt no es el correcto, por favor revise la información ingresada.");
//			verificarClickAceptarTramiteNo();
//			return null;
//		}
		
	}
	
	public String irCancelarPostulacion(){
		limpiar();
		return "irCancelarPostulacion";
	}
	
	/** 
	 * Método que muestra el mensaje que direciona a la página de Reportes de Postulados para las secretarias
	 * @param usuario que realiza la petición de la página
	 * @return irReportesPostulados
	 */
	public String irReportesPostuladosSecretaria(Usuario usuario){
		vsfListCarreras=new ArrayList<CarreraDto>();
		return "irReportesPostuladosSecretaria";
	}
	
	/**
	 * Valida la fecha en el componente de primefaces que no sea mayor a la actual
	 * @return
	 */
	public Date getValidaFechaNoMayor() {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Date fecha = null;
		try {
			fecha = formato.parse("19-02-2016");
		} catch (ParseException e) {
			fecha = new Date();
		}
		return fecha;
    }
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		vsfCarrera=new CarreraDto();
		vsfConvocatoria = new Convocatoria();
		vsfEstudianteValidacionJdbcDtoBuscar = new EstudianteValidacionJdbcDto();
		vsfListEstudianteValidacionJdbcDto = null;
		vsfListEstudianteValidacionJdbcDto= new ArrayList<EstudianteValidacionJdbcDto>();
//		vsfListEstudianteValidacionJdbcDto=new ArrayList<EstudianteValidacionJdbcDto>();
		vsfDeshabilitadoExportar = true;
		vsfHabilitadorComplex = false;
		vsfHabilitadorFechaCulmina = true;
		vsfHabilitadorAsignaTutor = true;
		vsfHabilitadorMalla = true;
		Calendar c = Calendar.getInstance();
        try {
			c.setTime(GeneralesUtilidades.fechaCadenaToDate(GeneralesConstantes.APP_MAX_FECHA_CULMINACION_MALLA_CONVOCATORIA, "dd-mm-yyyy"));
		} catch (ParseException e1) {
		}
        fechaLimiteMaxima= c.getTime();
        try {
			c.setTime(GeneralesUtilidades.fechaCadenaToDate(GeneralesConstantes.APP_MIN_FECHA_CULMINACION_MALLA, "dd-mm-yyyy"));
		} catch (ParseException e1) {
		}
        fechaLimiteMinima = c.getTime();
        try {
			c.setTime(GeneralesUtilidades.fechaCadenaToDate(GeneralesConstantes.APP_MAX_FECHA_PERIODO_ACADEMICO, "dd-mm-yyyy"));
		} catch (ParseException e1) {
		}
        fechaInicioConvocatoria = c.getTime();
	}
	
	
	public String verificarClickAceptarTramite(int idoneidad){
			vsfValidadorClic = 1;
			return null;	
	}
	
	public String verificarClickAceptarTramiteNo(){
		vsfValidadorClic = 0;
		return null;
	}
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/

	public CarreraDto getVsfCarrera() {
		return vsfCarrera;
	}

	public void setVsfCarrera(CarreraDto vsfCarrera) {
		this.vsfCarrera = vsfCarrera;
	}

	public FacultadDto getVsfFacultad() {
		return vsfFacultad;
	}

	public void setVsfFacultad(FacultadDto vsfFacultad) {
		this.vsfFacultad = vsfFacultad;
	}

	public List<CarreraDto> getVsfListCarreras() {
		vsfListCarreras = vsfListCarreras==null?(new ArrayList<CarreraDto>()):vsfListCarreras;
		return vsfListCarreras;
	}

	public void setVsfListCarreras(List<CarreraDto> vsfListCarreras) {
		this.vsfListCarreras = vsfListCarreras;
	}

	public EstudianteValidacionJdbcDto getVsfEstudianteValidacionJdbcDtoBuscar() {
		return vsfEstudianteValidacionJdbcDtoBuscar;
	}

	public void setVsfEstudianteValidacionJdbcDtoBuscar(
			EstudianteValidacionJdbcDto vsfEstudianteValidacionJdbcDtoBuscar) {
		this.vsfEstudianteValidacionJdbcDtoBuscar = vsfEstudianteValidacionJdbcDtoBuscar;
	}

	public Usuario getVsfUsuarioQueCambia() {
		return vsfUsuarioQueCambia;
	}

	public void setVsfUsuarioQueCambia(Usuario vsfUsuarioQueCambia) {
		this.vsfUsuarioQueCambia = vsfUsuarioQueCambia;
	}

	public List<EstudianteValidacionJdbcDto> getVsfListEstudianteValidacionJdbcDto() {
		return vsfListEstudianteValidacionJdbcDto;
	}

	public void setVsfListEstudianteValidacionJdbcDto(
			List<EstudianteValidacionJdbcDto> vsfListEstudianteValidacionJdbcDto) {
		this.vsfListEstudianteValidacionJdbcDto = vsfListEstudianteValidacionJdbcDto;
	}

	public Persona getVsfPersonaPostulante() {
		return vsfPersonaPostulante;
	}

	public void setVsfPersonaPostulante(Persona vsfPersonaPostulante) {
		this.vsfPersonaPostulante = vsfPersonaPostulante;
	}


	public Convocatoria getVsfConvocatoria() {
		return vsfConvocatoria;
	}

	public void setVsfConvocatoria(Convocatoria vsfConvocatoria) {
		this.vsfConvocatoria = vsfConvocatoria;
	}

	public List<Convocatoria> getVsfListConvocatorias() {
		vsfListConvocatorias = vsfListConvocatorias==null?(new ArrayList<Convocatoria>()):vsfListConvocatorias;
		return vsfListConvocatorias;
	}

	public void setVsfListConvocatorias(List<Convocatoria> vsfListConvocatorias) {
		this.vsfListConvocatorias = vsfListConvocatorias;
	}
	
	public EstudianteValidacionJdbcDto getVsfEstudianteValidacionJdbcDtoEditar() {
		return vsfEstudianteValidacionJdbcDtoEditar;
	}

	public void setVsfEstudianteValidacionJdbcDtoEditar(
			EstudianteValidacionJdbcDto vsfEstudianteValidacionJdbcDtoEditar) {
		this.vsfEstudianteValidacionJdbcDtoEditar = vsfEstudianteValidacionJdbcDtoEditar;
	}


	public List<CarreraDto> getVsfListCarrerasAsignar() {
		vsfListCarrerasAsignar = vsfListCarrerasAsignar==null?(new ArrayList<CarreraDto>()):vsfListCarrerasAsignar;
		return vsfListCarrerasAsignar;
	}


	public void setVsfListCarrerasAsignar(List<CarreraDto> vsfListCarrerasAsignar) {
		this.vsfListCarrerasAsignar = vsfListCarrerasAsignar;
	}


	public Carrera getVsfNuevaCarrera() {
		return vsfNuevaCarrera;
	}


	public void setVsfNuevaCarrera(Carrera vsfNuevaCarrera) {
		this.vsfNuevaCarrera = vsfNuevaCarrera;
	}


	public int getHabilitarCampos() {
		return habilitarCampos;
	}


	public void setHabilitarCampos(int habilitarCampos) {
		this.habilitarCampos = habilitarCampos;
	}

	
	
	public int getFechaRegimenPostulacion() {
		return fechaRegimenPostulacion;
	}


	public void setFechaRegimenPostulacion(int fechaRegimenPostulacion) {
		this.fechaRegimenPostulacion = fechaRegimenPostulacion;
	}
	
	public Boolean getVsfDeshabilitadoExportar() {
		return vsfDeshabilitadoExportar;
	}


	public void setVsfDeshabilitadoExportar(Boolean vsfDeshabilitadoExportar) {
		this.vsfDeshabilitadoExportar = vsfDeshabilitadoExportar;
	}


	public Integer getVsfValidadorClic() {
		return vsfValidadorClic;
	}


	public void setVsfValidadorClic(Integer vsfValidadorClic) {
		this.vsfValidadorClic = vsfValidadorClic;
	}


	public boolean isVsfHabilitadorComplex() {
		return vsfHabilitadorComplex;
	}


	public void setVsfHabilitadorComplex(boolean vsfHabilitadorComplex) {
		this.vsfHabilitadorComplex = vsfHabilitadorComplex;
	}


	public boolean isVsfHabilitadorFechaCulmina() {
		return vsfHabilitadorFechaCulmina;
	}


	public void setVsfHabilitadorFechaCulmina(boolean vsfHabilitadorFechaCulmina) {
		this.vsfHabilitadorFechaCulmina = vsfHabilitadorFechaCulmina;
	}


	public boolean isVsfHabilitadorAsignaTutor() {
		return vsfHabilitadorAsignaTutor;
	}


	public void setVsfHabilitadorAsignaTutor(boolean vsfHabilitadorAsignaTutor) {
		this.vsfHabilitadorAsignaTutor = vsfHabilitadorAsignaTutor;
	}


	public boolean isVsfHabilitadorFechaAsignacion() {
		return vsfHabilitadorFechaAsignacion;
	}


	public void setVsfHabilitadorFechaAsignacion(
			boolean vsfHabilitadorFechaAsignacion) {
		this.vsfHabilitadorFechaAsignacion = vsfHabilitadorFechaAsignacion;
	}


	public boolean isVsfHabilitadorMalla() {
		return vsfHabilitadorMalla;
	}


	public void setVsfHabilitadorMalla(boolean vsfHabilitadorMalla) {
		this.vsfHabilitadorMalla = vsfHabilitadorMalla;
	}


	public Date getFechaLimiteMaxima() {
		return fechaLimiteMaxima;
	}


	public void setFechaLimiteMaxima(Date fechaLimiteMaxima) {
		this.fechaLimiteMaxima = fechaLimiteMaxima;
	}


	public Date getFechaLimiteMinima() {
		return fechaLimiteMinima;
	}


	public void setFechaLimiteMinima(Date fechaLimiteMinima) {
		this.fechaLimiteMinima = fechaLimiteMinima;
	}


	public Date getFechaInicioConvocatoria() {
		return fechaInicioConvocatoria;
	}


	public void setFechaInicioConvocatoria(Date fechaInicioConvocatoria) {
		this.fechaInicioConvocatoria = fechaInicioConvocatoria;
	}


	public int getVsfIdoneidad() {
		return vsfIdoneidad;
	}


	public void setVsfIdoneidad(int vsfIdoneidad) {
		this.vsfIdoneidad = vsfIdoneidad;
	}


	public Boolean getVsfResultadoConsulta() {
		return vsfResultadoConsulta;
	}


	public void setVsfResultadoConsulta(Boolean vsfResultadoConsulta) {
		this.vsfResultadoConsulta = vsfResultadoConsulta;
	}


	public List<RegistroSenescytDto> getVsfListaTitulos() {
		vsfListaTitulos = vsfListaTitulos==null?(new ArrayList<RegistroSenescytDto>()):vsfListaTitulos;
		return vsfListaTitulos;
	}


	public void setVsfListaTitulos(List<RegistroSenescytDto> vsfListaTitulos) {
		this.vsfListaTitulos = vsfListaTitulos;
	}


	public DefaultStreamedContent getImage() {
		return image;
	}


	public void setImage(DefaultStreamedContent image) {
		this.image = image;
	}
	
	
	
}

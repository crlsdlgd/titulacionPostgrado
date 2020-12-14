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
   
 ARCHIVO:     ActaGradoSecretariaForm.java	  
 DESCRIPCION: Bean que maneja las peticiones del rol Secretaria.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 09-09-2018			Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultStreamedContent;

import ec.edu.uce.clientePersonalDataJXRS.principal.InteroperadorCliente;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ConfiguracionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteActaGradoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ProcesoTramiteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ProcesoTramiteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EstudianteValidacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.InstitucionAcademicaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProcesoTramiteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UbicacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ConfiguracionCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteActaGradoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ReportesDtoJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.RolDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.InstitucionAcademica;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) ActaGradoSecretariaForm.
 * Managed Bean que maneja las peticiones del rol Secretaria.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="actaGradoSecretariaForm")
@SessionScoped
public class ActaGradoSecretariaForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
		
	// *****************************************************************/
	// ************** Variables de ActaGradoSecretariaForm**************/
	// *****************************************************************/
	
	//COMPONENTES DE BUSQUEDA
	private Convocatoria agfConvocatoria;
	private List<Convocatoria> agfListConvocatorias;
	private CarreraDto agfCarrera;
	private List<CarreraDto> agfListCarreras;
	private EstudianteActaGradoJdbcDto agfEstudianteActaGradoDtoServicioJdbcDtoBuscar;
	private List<EstudianteActaGradoJdbcDto> agfListEstudiantesActasGrado;
	private List<Etnia> agfListEtnia;
	private List<UbicacionDto> agfListPaises;
	
	//COMPONENTES DE EDITAR
	private EstudianteActaGradoJdbcDto agfEstudianteEstudianteActaGradoJdbcDtoEditar;
	private String fechaNacimientoLabel;
	private List<TituloDto> agfListTitulosBachilleres;
	private List<InstitucionAcademicaDto> agfListUniversidades;
	private Boolean agfEstadoValidado;
	private Usuario agfUsuarioQueCambia;
	private Persona agfPersonaPostulante;
	private ConfiguracionCarreraDto agfConfiguracionCarrera;
	private List<ConfiguracionCarreraDto> agfListConfiguracionesCarrera;
	private int habilitarCampos;
	private Integer agfValidadorClic;
	private Boolean agfDeshabilitadoExportar;
	private Boolean agfEstadoConsultado;
	
	private String agfNombreValidado;
	private String agfFechaValidada;
	private String agfMensajeDinardap;
	private String agfTituloValidado;
//	private Integer numeroActaGrado;
	private Boolean agfTresComponentes;
	private Integer cncrId;
	private DefaultStreamedContent image;
	
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB private ConvocatoriaServicio srvConvocatoriaServicio;
		
	@EJB private ReportesDtoJdbc srvReportesDtoJdbc;
		
	@EJB private UsuarioRolServicio srvUsuarioRolServicio;

	@EJB private FacultadServicio srvFacultadServicio;
		
	@EJB private CarreraServicio srvCarreraServicio;
	
	@EJB private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
		
	@EJB private EstudianteValidacionDtoServicioJdbc servAgfEstudianteValidacionDtoServicioJdbc;
	
	@EJB private EstudianteValidacionDtoServicio servAgfEstudianteValidacionDtoServicio;
	
	@EJB private EstudianteActaGradoDtoServicioJdbc servAgfEstudianteActaGradoDtoServicioJdbc;
	
	@EJB private TituloDtoServicioJdbc servAgfTituloDtoJdbc;
	@EJB private InstitucionAcademicaDtoServicioJdbc servAgfIntitucionAcademicaDtoJdbc;
	@EJB private ConfiguracionCarreraDtoServicioJdbc servAgfConfiguracionCarreraDtoJdbc;
	@EJB private EtniaServicio servAgfEtniaServicio;
	@EJB private UbicacionDtoServicioJdbc servAgfUbicacionDtoServicioJdbc;
	@EJB private ConfiguracionCarreraServicio servAgfConfiguracionCarreraServicio;
	@EJB private UbicacionServicio servAgfUbicacionServicio;
	@EJB private TituloServicio servAgfTituloServicio;
	@EJB private InstitucionAcademicaServicio servAgfInstitucionAcademicaServicio;
	@EJB private RolDtoServicioJdbc servRolDtoServicioJdbc;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarreraServicio;
	@EJB private ProcesoTramiteServicio servProcesoTramiteServicio;
	@EJB private UbicacionDtoServicioJdbc servUbicacionJdbc;
	@EJB private CarreraDtoServicioJdbc servCarreraDtoServicioJdbc;
	
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
	 * Método que dirige a la página de Acta Grado
	 * @param usuario
	 * @return
	 */
	public String irlistarActaGrado(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
		agfDeshabilitadoExportar=true;
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
			agfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			agfListCarreras=servAgfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(agfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
			agfListEtnia = servAgfEtniaServicio.listarTodos();
			//genero la lista de paises 
			agfListPaises = servUbicacionJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
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
		} catch (EtniaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UbicacionDtoJdbcException e) {
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
		}
		agfUsuarioQueCambia = usuario;
//		RolDto rolBuscar = new RolDto();
//		try {
//			rolBuscar= servRolDtoServicioJdbc.buscarRolXCedula(usuario.getUsrIdentificacion());
//		} catch (RolException | RolNoEncontradoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("Rol de usuario no encontrado");
//			return null;
//		}
//		boolean op=false;
//		List<RolFlujoCarrera> rolFlujoCarreraAux;
//		try {
//			rolFlujoCarreraAux = servRolFlujoCarreraServicio.buscarPorIdUsuario(usuario);
//			for (RolFlujoCarrera rolFlujoCarrera : rolFlujoCarreraAux) {
//				if(rolFlujoCarrera.getRoflcrCarrera().getCrrFacultad().getFclId()==FacultadConstantes.FACULTAD_INGENIERIA_CFM_VALUE){
//					op=true;
//				}
//			}
//		} catch (RolFlujoCarreraNoEncontradoException
//				| RolFlujoCarreraException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("No se encontraron carreras asignadas al usuario");
//			return null;
//		}
//		if(rolBuscar.getRolId()==RolConstantes.ROL_BD_EDITOR_ACTA_VALUE || !op){
			return "irlistarActaGrado";	
//		}else{
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("No puede acceder a este menú, esta tarea la ejecuta otro usuario");
//			return null;
//		}
	}
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelar(){
		agfCarrera =  null;
		agfConvocatoria = null;
		limpiar();
		return "irInicio";
	}
	
	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarCarrera(){
		agfListEstudiantesActasGrado=null;
	}
	
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		agfListEstudiantesActasGrado=null;
		agfEstudianteActaGradoDtoServicioJdbcDtoBuscar = null;
		iniciarParametros();
	}
	
	/**
	 * Lista las postulaciones del estudiante segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarPostulacion(Usuario usuario){
			try {
				agfListEstudiantesActasGrado = servAgfEstudianteActaGradoDtoServicioJdbc.buscarEstudianteXIndetificacionXCarreraXConvocatoria(agfEstudianteActaGradoDtoServicioJdbcDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion(),agfCarrera , agfConvocatoria.getCnvId());
			} catch (EstudianteActaGradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGradoSecretaria.listar.postulacion.facultad.carrera.identificacion.estado.exception")));
			} catch (EstudianteActaGradoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGradoSecretaria.listar.postulacion.facultad.carrera.identificacion.estado.no.encontrado.exception")));
			}
	}
	
	public String irActaGrado(EstudianteActaGradoJdbcDto estudiante, Usuario usuario) {
		
		
		this.agfEstudianteEstudianteActaGradoJdbcDtoEditar =estudiante;
		byte[] data;
		try {
			
			String[] partesNotas =  agfEstudianteEstudianteActaGradoJdbcDtoEditar.getPrsUbicacionFoto().split("\\.");
			Path path = Paths.get(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+agfEstudianteEstudianteActaGradoJdbcDtoEditar.getPrsIdentificacion()+"."+partesNotas[1]);
//			Path path = Paths.get("d:/archivos/"+"Fotografia_1714991989.jpg");
			data = Files.readAllBytes(path);
			image = new DefaultStreamedContent(new ByteArrayInputStream(data), Files.probeContentType(path));
		} catch (IOException e) {
		} catch (Exception e) {
		}
		this.agfUsuarioQueCambia=usuario;
		if(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getFcesRecEstuPrevios()==GeneralesConstantes.APP_ID_BASE){
			agfEstudianteEstudianteActaGradoJdbcDtoEditar.setFcesRecEstuPrevios(null);
		}
			if(estudiante.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE
					|| estudiante.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE
					|| estudiante.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE
					|| estudiante.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE){
				try {
					ProcesoTramite prtrAux = servProcesoTramiteServicio.buscarXTrttXTipoPorceso(estudiante.getTrttId(),ProcesoTramiteConstantes.TIPO_PROCESO_EMISION_ACTA_VALUE);
					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
					long diff = prtrAux.getPrtrFechaEjecucion().getTime() - fechaActual.getTime();
					int diasTranscurridos=(int)diff/(1000*60*60*24);
					if(diasTranscurridos>0){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGradoSecretaria.listar.postulacion.emision.titulos.tiempo.excedido.exception")));
						return null;
					}
					long diaSemana = fechaActual.getTime();
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(diaSemana);
					if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGradoSecretaria.listar.postulacion.emision.titulos.exception")));
						return null;
					}
				} catch (ProcesoTramiteNoEncontradoException
						| ProcesoTramiteException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGradoSecretaria.listar.postulacion.emision.titulos.exception")));
					return null;
				}catch(Exception e){
				}
			}
		
			try {
				agfValidadorClic = 0;
//				if(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE
//				||agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE
//				||agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE
//				||agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE
//				||agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE
//				||agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITADO_VALUE
//				||agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_VALUE
//						){
					agfEstadoValidado = false;
//				}else{
//					agfEstadoValidado = true;
//				}
				DateFormat fechaMMDDYYYY = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA);
				fechaNacimientoLabel = fechaMMDDYYYY.format(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getPrsFechaNacimiento());
				//Busco los titulos de bachillerato
			
				agfListTitulosBachilleres = servAgfTituloDtoJdbc.listarXtipo(TituloConstantes.TIPO_TITULO_UNIVERSIDAD_VALUE);
				
				Iterator<TituloDto> it = agfListTitulosBachilleres.iterator();
				while (it.hasNext()) {
					TituloDto tituloDto = it.next();
				    if (tituloDto.getTtlDescripcion().equals("OTRO")) {
				        it.remove();
				    }
				}
				//Busco las universidades en bd
				agfListUniversidades = servAgfIntitucionAcademicaDtoJdbc.listarXNivel(InstitucionAcademicaConstantes.NIVEL_UNIVERSIDAD_VALUE);
				for (InstitucionAcademicaDto item: agfListUniversidades) {//quito la central de la lista
					if(item.getInacId() == InstitucionAcademicaConstantes.UNIVERSIDAD_CENTRAL_DEL_ECUADOR_VALUE){
						agfListUniversidades.remove(item);
						break;
					}
				}
				agfListConfiguracionesCarrera = servAgfConfiguracionCarreraDtoJdbc.listarTitulosXCarreraXSexo(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttCarreraId(), agfEstudianteEstudianteActaGradoJdbcDtoEditar.getPrsSexo());
//				if(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getFcesNumActaGrado()!=null){
//					numeroActaGrado=-99;
//				}else{
//					Integer numActaGrado = srvCarreraServicio.buscarMaxNumActaXCarreraIdlistar(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getTrttCarreraId());
//					numActaGrado++;
//					numeroActaGrado=numActaGrado;
//					StringBuilder sb = new StringBuilder();
//					if(numActaGrado<10){
//						sb.append("00");
//						sb.append(numActaGrado);
//					}else if (numActaGrado<100){
//						sb.append("0");
//						sb.append(numActaGrado);
//					}else{
//						sb.append(numActaGrado);
//					}
//					agfEstudianteEstudianteActaGradoJdbcDtoEditar.setFcesNumActaGrado(sb.toString());
//				}
			} catch (Exception e1) {
				e1.printStackTrace();
				FacesUtil.mensajeInfo("Error al buscar los datos de estudiante, comuníquese con la Dirección de Tecnologías");
				return null;
			} 
			agfTresComponentes=false;
			CarreraDto aux = new CarreraDto();
			try {
				aux = servCarreraDtoServicioJdbc.buscarXId(estudiante.getCrrId());
			} catch (Exception e) {
			} 
		if(aux.getCrrTipoEvaluacion()==1){
			agfTresComponentes=true;
		}
//			else{
			return "irActaGrado";	
//		}
		
		
	}

	
	public String irRegresarActaGrado(){
		agfListTitulosBachilleres = null;
		agfEstadoValidado=true;
		limpiar();
		return "irRegresarActaGrado";
	}
	
	public String irRegresarActaGradoReImprimir(){
		agfListTitulosBachilleres = null;
		agfEstadoValidado=true;
		limpiar();
		return "irRegresarActaGradoReImprimir";
	}
	
	public void limpiarInacPrevios(){
		if(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getFcesRecEstuPrevios()==GeneralesConstantes.APP_NO_VALUE){
		agfEstudianteEstudianteActaGradoJdbcDtoEditar.setFcesTipoDuracionRec(null);
		agfEstudianteEstudianteActaGradoJdbcDtoEditar.setFcesCrrEstudPrevios(null);
		agfEstudianteEstudianteActaGradoJdbcDtoEditar.setFcesTiempoEstudRec(null);
		agfEstudianteEstudianteActaGradoJdbcDtoEditar.setInacId(null);
		}
		
	}
	
	public String guardar(EstudianteActaGradoJdbcDto estudiante ) {
		try {
			Etnia etnPersona = new Etnia();
			etnPersona = servAgfEtniaServicio.buscarPorId(estudiante.getPrsEtnId());
			Ubicacion ubcNacionalidad = new Ubicacion();
			ubcNacionalidad =  servAgfUbicacionServicio.buscarPorId(estudiante.getPrsUbcId());
			Ubicacion ubcResidencia = new Ubicacion();
			ubcResidencia =  servAgfUbicacionServicio.buscarPorId(estudiante.getFcesUbcCantonResidencia());
			InstitucionAcademica inacFces = new InstitucionAcademica();
			
			if(estudiante.getFcesInacIdInstEstPrevios()!=null || estudiante.getFcesInacIdInstEstPrevios() == GeneralesConstantes.APP_ID_BASE ){
				inacFces = servAgfInstitucionAcademicaServicio.buscarPorId(estudiante.getInacId());
			}else{
				estudiante.setFcesInacIdInstEstPrevios(null);
			}
			DateFormat fechaMMDDYYYY = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA);
			try {
				estudiante.setPrsFechaNacimiento(fechaMMDDYYYY.parse(fechaNacimientoLabel));
			} catch (ParseException e) {
			}
			
			ConfiguracionCarrera cncrFces = new ConfiguracionCarrera();
			cncrFces = 	servAgfConfiguracionCarreraServicio.buscarPorId(cncrId);
			
			RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(estudiante.getTrttCarreraId(), agfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_VALIDADOR_VALUE);
			estudiante.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
			
			if(servAgfEstudianteActaGradoDtoServicioJdbc.editarActaGrado(estudiante, etnPersona, ubcNacionalidad, ubcResidencia, inacFces, cncrFces, null, roflcrr,GeneralesConstantes.APP_ID_BASE)){
				limpiar();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.exitoso")));
				return "irlistarActaGrado";	
			}else{
				limpiar();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al generar el acta de posgrado, comuníquese con el administrador del sistema");
				verificarClickAceptarTramiteNo();
				return null;
			}
			
			
		} catch (EtniaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.etnia.no.encontrado.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (EtniaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.etnia.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (UbicacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.ubicacion.no.encontrado.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (UbicacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.ubicacion.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (InstitucionAcademicaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.institucion.academica.no.encontrado.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (InstitucionAcademicaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.institucion.academica.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (ConfiguracionCarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.configuracion.carrera.no.encontrado.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (ConfiguracionCarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.configuracion.carrera.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (EstudianteActaGradoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.editar.acta.grado.no.encontrado.exception")));
			limpiar();
			return "irlistarActaGrado";
		} catch (EstudianteActaGradoException e) {
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActaGrado.guardar.editar.acta.grado.exception")));
			limpiar();
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		limpiar();
		return "irlistarActaGrado";
	}
	

	public void consultarDinardap(){
		
		try {
			InteroperadorCliente inter = new InteroperadorCliente();
			inter.consultar(agfEstudianteEstudianteActaGradoJdbcDtoEditar.getPrsIdentificacion());
			agfEstadoConsultado = true;
			agfNombreValidado = inter.getJsonDinardap().getFichaGeneral().getInstituciones().getDatosPrincipales().get(1).getValor();
			agfFechaValidada =  inter.getJsonDinardap().getFichaGeneral().getInstituciones().getDatosPrincipales().get(3).getValor();
			agfMensajeDinardap = "Datos consultados correctamente, por favor revise la información";
			
		} catch (Exception e) {
			agfMensajeDinardap = "No se encontró información en el Registro Civil, por favor revise la documentación y continue con la edición";
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(agfMensajeDinardap);
			agfEstadoConsultado=false;
		}
	}
	
	/** 
	 * Método que muestra el mensaje que direciona a la página de Reportes de Postulados para las secretarias
	 * @param usuario que realiza la petición de la página
	 * @return irReportesPostulados
	 */
	public String irReportesPostuladosSecretaria(Usuario usuario){
		agfListCarreras=new ArrayList<CarreraDto>();
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
		agfCarrera=new CarreraDto();
		agfConvocatoria = new Convocatoria();
		agfEstudianteActaGradoDtoServicioJdbcDtoBuscar = new EstudianteActaGradoJdbcDto();
		agfListEstudiantesActasGrado = null;
		agfListEstudiantesActasGrado= new ArrayList<EstudianteActaGradoJdbcDto>();
		agfEstadoConsultado= false;
		agfNombreValidado = null;
		agfFechaValidada =  null;
		agfMensajeDinardap = null;
		agfTituloValidado = null;
//		agfListEtnia =  new ArrayList<Etnia>();
//		agfListEstudianteValidacionJdbcDto=new ArrayList<EstudianteValidacionJdbcDto>();
	}
	
	
	public String verificarClickAceptarTramite(EstudianteActaGradoJdbcDto estudiante){
		agfValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		agfValidadorClic = 0;
		return null;
	}
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/
	public Convocatoria getAgfConvocatoria() {
		return agfConvocatoria;
	}

	public void setAgfConvocatoria(Convocatoria agfConvocatoria) {
		this.agfConvocatoria = agfConvocatoria;
	}

	public List<Convocatoria> getAgfListConvocatorias() {
		agfListConvocatorias = agfListConvocatorias==null?(new ArrayList<Convocatoria>()):agfListConvocatorias;
		return agfListConvocatorias;
	}

	public void setAgfListConvocatorias(List<Convocatoria> agfListConvocatorias) {
		this.agfListConvocatorias = agfListConvocatorias;
	}
	
	public CarreraDto getAgfCarrera() {
		return agfCarrera;
	}
	
	public void setAgfCarrera(CarreraDto agfCarrera) {
		this.agfCarrera = agfCarrera;
	}

	public List<CarreraDto> getAgfListCarreras() {
		agfListCarreras = agfListCarreras==null?(new ArrayList<CarreraDto>()):agfListCarreras;
		return agfListCarreras;
	}

	public void setAgfListCarreras(List<CarreraDto> agfListCarreras) {
		this.agfListCarreras = agfListCarreras;
	}
	
	public EstudianteActaGradoJdbcDto getAgfEstudianteActaGradoDtoServicioJdbcDtoBuscar() {
		return agfEstudianteActaGradoDtoServicioJdbcDtoBuscar;
	}

	public void setAgfEstudianteActaGradoDtoServicioJdbcDtoBuscar(
			EstudianteActaGradoJdbcDto agfEstudianteActaGradoDtoServicioJdbcDtoBuscar) {
		this.agfEstudianteActaGradoDtoServicioJdbcDtoBuscar = agfEstudianteActaGradoDtoServicioJdbcDtoBuscar;
	}

	public List<EstudianteActaGradoJdbcDto> getAgfListEstudiantesActasGrado() {
		agfListEstudiantesActasGrado = agfListEstudiantesActasGrado==null?(new ArrayList<EstudianteActaGradoJdbcDto>()):agfListEstudiantesActasGrado;
		return agfListEstudiantesActasGrado;
	}

	public void setAgfListEstudiantesActasGrado(
			List<EstudianteActaGradoJdbcDto> agfListEstudiantesActasGrado) {
		this.agfListEstudiantesActasGrado = agfListEstudiantesActasGrado;
	}

	public EstudianteActaGradoJdbcDto getAgfEstudianteEstudianteActaGradoJdbcDtoEditar() {
		return agfEstudianteEstudianteActaGradoJdbcDtoEditar;
	}

	public void setAgfEstudianteEstudianteActaGradoJdbcDtoEditar(
			EstudianteActaGradoJdbcDto agfEstudianteEstudianteActaGradoJdbcDtoEditar) {
		this.agfEstudianteEstudianteActaGradoJdbcDtoEditar = agfEstudianteEstudianteActaGradoJdbcDtoEditar;
	}

	public String getFechaNacimientoLabel() {
		return fechaNacimientoLabel;
	}


	public void setFechaNacimientoLabel(String fechaNacimientoLabel) {
		this.fechaNacimientoLabel = fechaNacimientoLabel;
	}
	
	
	public List<ConfiguracionCarreraDto> getAgfListConfiguracionesCarrera() {
		agfListConfiguracionesCarrera = agfListConfiguracionesCarrera==null?(new ArrayList<ConfiguracionCarreraDto>()):agfListConfiguracionesCarrera;
		return agfListConfiguracionesCarrera;
	}


	public void setAgfListConfiguracionesCarrera(
			List<ConfiguracionCarreraDto> agfListConfiguracionesCarrera) {
		this.agfListConfiguracionesCarrera = agfListConfiguracionesCarrera;
	}
	
	public Usuario getAgfUsuarioQueCambia() {
		return agfUsuarioQueCambia;
	}

	public void setAgfUsuarioQueCambia(Usuario agfUsuarioQueCambia) {
		this.agfUsuarioQueCambia = agfUsuarioQueCambia;
	}


	public Persona getAgfPersonaPostulante() {
		return agfPersonaPostulante;
	}

	public void setAgfPersonaPostulante(Persona agfPersonaPostulante) {
		this.agfPersonaPostulante = agfPersonaPostulante;
	}

	public ConfiguracionCarreraDto getAgfConfiguracionCarrera() {
		return agfConfiguracionCarrera;
	}

	public void setAgfConfiguracionCarrera(
			ConfiguracionCarreraDto agfConfiguracionCarrera) {
		this.agfConfiguracionCarrera = agfConfiguracionCarrera;
	}
	
	public List<TituloDto> getAgfListTitulosBachilleres() {
		agfListTitulosBachilleres = agfListTitulosBachilleres==null?(new ArrayList<TituloDto>()):agfListTitulosBachilleres;
		return agfListTitulosBachilleres;
	}


	public void setAgfListTitulosBachilleres(
			List<TituloDto> agfListTitulosBachilleres) {
		this.agfListTitulosBachilleres = agfListTitulosBachilleres;
	}
	
	public List<InstitucionAcademicaDto> getAgfListUniversidades() {
		agfListUniversidades = agfListUniversidades==null?(new ArrayList<InstitucionAcademicaDto>()):agfListUniversidades;
		return agfListUniversidades;
	}


	public void setAgfListUniversidades(
			List<InstitucionAcademicaDto> agfListUniversidades) {
		this.agfListUniversidades = agfListUniversidades;
	}
	

	public Boolean getAgfEstadoValidado() {
		return agfEstadoValidado;
	}


	public void setAgfEstadoValidado(Boolean agfEstadoValidado) {
		this.agfEstadoValidado = agfEstadoValidado;
	}













	







	public int getHabilitarCampos() {
		return habilitarCampos;
	}


	public void setHabilitarCampos(int habilitarCampos) {
		this.habilitarCampos = habilitarCampos;
	}

	
	
	public Boolean getAgfDeshabilitadoExportar() {
		return agfDeshabilitadoExportar;
	}


	public void setAgfDeshabilitadoExportar(Boolean agfDeshabilitadoExportar) {
		this.agfDeshabilitadoExportar = agfDeshabilitadoExportar;
	}


	public Integer getAgfValidadorClic() {
		return agfValidadorClic;
	}


	public void setAgfValidadorClic(Integer agfValidadorClic) {
		this.agfValidadorClic = agfValidadorClic;
	}

	public List<Etnia> getAgfListEtnia() {
		agfListEtnia = agfListEtnia==null?(new ArrayList<Etnia>()):agfListEtnia;
		return agfListEtnia;
	}

	public void setAgfListEtnia(List<Etnia> agfListEtnia) {
		this.agfListEtnia = agfListEtnia;
	}

	public Boolean getAgfEstadoConsultado() {
		return agfEstadoConsultado;
	}

	public void setAgfEstadoConsultado(Boolean agfEstadoConsultado) {
		this.agfEstadoConsultado = agfEstadoConsultado;
	}

	public String getAgfNombreValidado() {
		return agfNombreValidado;
	}

	public void setAgfNombreValidado(String agfNombreValidado) {
		this.agfNombreValidado = agfNombreValidado;
	}

	public String getAgfFechaValidada() {
		return agfFechaValidada;
	}

	public void setAgfFechaValidada(String agfFechaValidada) {
		this.agfFechaValidada = agfFechaValidada;
	}

	public String getAgfMensajeDinardap() {
		return agfMensajeDinardap;
	}

	public void setAgfMensajeDinardap(String agfMensajeDinardap) {
		this.agfMensajeDinardap = agfMensajeDinardap;
	}

	public String getAgfTituloValidado() {
		return agfTituloValidado;
	}

	public void setAgfTituloValidado(String agfTituloValidado) {
		this.agfTituloValidado = agfTituloValidado;
	}

	public List<UbicacionDto> getAgfListPaises() {
		return agfListPaises;
	}

	public void setAgfListPaises(List<UbicacionDto> agfListPaises) {
		this.agfListPaises = agfListPaises;
	}

	public Boolean getAgfTresComponentes() {
		return agfTresComponentes;
	}

	public void setAgfTresComponentes(Boolean agfTresComponentes) {
		this.agfTresComponentes = agfTresComponentes;
	}

	public Integer getCncrId() {
		return cncrId;
	}

	public void setCncrId(Integer cncrId) {
		this.cncrId = cncrId;
	}

	public DefaultStreamedContent getImage() {
		return image;
	}

	public void setImage(DefaultStreamedContent image) {
		this.image = image;
	}


	
	
	
	
}

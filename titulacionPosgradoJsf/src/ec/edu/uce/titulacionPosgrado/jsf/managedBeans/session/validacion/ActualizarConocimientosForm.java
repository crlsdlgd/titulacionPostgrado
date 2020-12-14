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
   
 ARCHIVO:     ActualizarConocimientosForm.java	  
 DESCRIPCION: Bean que maneja las peticiones del rol Secretaria para actualizar conocimientos.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20-FEBRERO-2017		Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ConfiguracionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EstudianteValidacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ReportesDtoJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TramiteTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ActualizarConocimientosForm.
 * Managed Bean que maneja las peticiones del rol Secretaria para Actualizar conocimientos.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="actualizarConocimientosForm")
@SessionScoped
public class ActualizarConocimientosForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
	// *****************************************************************/
	// ******************* DataSource de ActualizarConocimientosForm*******/
	// *****************************************************************/

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
		
	// *********************************************************************/
	// ******************* Variables de ActualizarConocimientosForm ********/
	// *********************************************************************/
	private CarreraDto acfCarrera;

	private FacultadDto acfFacultad;
	private List<CarreraDto> acfListCarreras;
	private EstudianteValidacionJdbcDto acfEstudianteValidacionJdbcDtoBuscar;
	private List<EstudianteValidacionJdbcDto> acfListaEstudianteValidacionJdbcDtoEditar;
	
	private Usuario acfUsuarioQueCambia;
	private List<EstudianteValidacionJdbcDto> acfListEstudianteValidacionJdbcDto;
	private Persona acfPersonaPostulante;
	private ConfiguracionCarreraDto acfConfiguracionCarrera;
	
	private Convocatoria acfConvocatoria;
	private List<Convocatoria> acfListConvocatorias;
	private Boolean acfDeshabilitadoExportar;
	private Integer acfValidadorClic;
	private boolean acfHabilitadorBotonSi;
	private String acfComprobantePago;
	private UsuarioRol usro;
	
	
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
	private EstudianteValidacionDtoServicioJdbc servacfEstudianteValidacionDtoServicioJdbc;
	
	@EJB
	private EstudianteValidacionDtoServicio servacfEstudianteValidacionDtoServicio;
	
	@EJB
	private	TramiteTituloDtoServicioJdbc servTramiteTituloDtoServicioJdbc;
	
	
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
	 * Método que dirige a la página de Actualizar conocimientos
	 * @param usuario
	 * @return
	 */
	public String irlistarPostuladosActualizar(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
		this.acfUsuarioQueCambia=usuario;
		try {
			this.usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
			acfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			acfListCarreras=servacfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(acfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
			acfEstudianteValidacionJdbcDtoBuscar= new EstudianteValidacionJdbcDto();
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
		acfUsuarioQueCambia = usuario;
		return "irListarActualizarConocimientos";
	}
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarActualizar(){
		acfPersonaPostulante = null;
		acfListaEstudianteValidacionJdbcDtoEditar = null;
		acfEstudianteValidacionJdbcDtoBuscar=null;
		iniciarParametros();
		return "irInicio";

	}
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarRegistroActualizar(){
		acfPersonaPostulante = null;
		acfListaEstudianteValidacionJdbcDtoEditar = null;
		acfEstudianteValidacionJdbcDtoBuscar=null;
		iniciarParametros();
		return "irRegresarListarActualizar";

	}
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String irRegistrarActualizar(EstudianteValidacionJdbcDto estudiante){
		this.acfEstudianteValidacionJdbcDtoBuscar = estudiante;
		acfComprobantePago=null;
		return "irRegistrarActualizar";

	}
	
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		acfDeshabilitadoExportar=true;
		acfListEstudianteValidacionJdbcDto=null;
		acfListaEstudianteValidacionJdbcDtoEditar= null;
		
		acfEstudianteValidacionJdbcDtoBuscar = null;
		acfDeshabilitadoExportar = true;
		iniciarParametros();
	}
	
	/**
	 * Lista las postulaciones del estudiante segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarValidados(Usuario usuario){
		acfListEstudianteValidacionJdbcDto=null;
		try {
//			System.out.println(acfEstudianteValidacionJdbcDtoBuscar.getPrsIdentificacion());
			acfListaEstudianteValidacionJdbcDtoEditar = new ArrayList<EstudianteValidacionJdbcDto>();
			acfListaEstudianteValidacionJdbcDtoEditar = servacfEstudianteValidacionDtoServicioJdbc.buscarEstudiantesValidadosXIndetificacionXCarreraXConvocatoriaActualizar(acfEstudianteValidacionJdbcDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion());
			if(acfListaEstudianteValidacionJdbcDtoEditar.size()==0){
				acfListaEstudianteValidacionJdbcDtoEditar = new ArrayList<EstudianteValidacionJdbcDto>();
//				try {
//					acfListaEstudianteValidacionJdbcDtoEditar = servacfEstudianteValidacionDtoServicioJdbc.buscarEstudiantesAptitudXIndetificacionXCarreraXConvocatoriaActualizar(acfEstudianteValidacionJdbcDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion());
//					if(acfListaEstudianteValidacionJdbcDtoEditar.size()==0){
//						FacesUtil.limpiarMensaje();
//						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));	
//					}
//				} catch (EstudiantePostuladoJdbcDtoException
//						| EstudiantePostuladoJdbcDtoNoEncontradoException e1) {
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
//				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen resultados con la identificación ingresada.");
			}
		} catch (EstudiantePostuladoJdbcDtoException
				| EstudiantePostuladoJdbcDtoNoEncontradoException e) {
		}
	}


	public String registrarActualizar() {
		
		if(GeneralesUtilidades.eliminarEspaciosEnBlanco(acfComprobantePago).length()!=0){
			try {
				RolFlujoCarrera roflcr = srvRolFlujoCarreraServicio.buscarPorCarrera(acfEstudianteValidacionJdbcDtoBuscar.getTrttCarreraId(), acfUsuarioQueCambia.getUsrId(), RolConstantes.ROL_BD_VALIDADOR_VALUE);
				servacfEstudianteValidacionDtoServicio.registrarActualizarConocimientoPago(acfEstudianteValidacionJdbcDtoBuscar.getVldId(), acfEstudianteValidacionJdbcDtoBuscar.getTrttId(),acfComprobantePago,roflcr.getRoflcrId(),acfEstudianteValidacionJdbcDtoBuscar.getPrsIdentificacion());
				iniciarParametros();
				acfPersonaPostulante = null;
				acfListaEstudianteValidacionJdbcDtoEditar = null;
				acfEstudianteValidacionJdbcDtoBuscar=null;
				acfEstudianteValidacionJdbcDtoBuscar = new EstudianteValidacionJdbcDto();
				acfListaEstudianteValidacionJdbcDtoEditar = new ArrayList<EstudianteValidacionJdbcDto>();
				acfPersonaPostulante = new Persona();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionConocimientos.registro.comprobante.pago.correcto")));
				
			} catch (EstudianteValidacionDtoNoEncontradoException
					| EstudianteValidacionDtoException e) {
				
				
				
				iniciarParametros();
				acfPersonaPostulante = null;
				acfListaEstudianteValidacionJdbcDtoEditar = null;
				acfEstudianteValidacionJdbcDtoBuscar=null;
				acfEstudianteValidacionJdbcDtoBuscar = new EstudianteValidacionJdbcDto();
				acfListaEstudianteValidacionJdbcDtoEditar = new ArrayList<EstudianteValidacionJdbcDto>();
				acfPersonaPostulante = new Persona();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionConocimientos.registro.comprobante.pago.error")));
				
			} catch (RolFlujoCarreraNoEncontradoException e) {
				iniciarParametros();
				acfPersonaPostulante = null;
				acfListaEstudianteValidacionJdbcDtoEditar = null;
				acfEstudianteValidacionJdbcDtoBuscar=null;
				acfEstudianteValidacionJdbcDtoBuscar = new EstudianteValidacionJdbcDto();
				acfListaEstudianteValidacionJdbcDtoEditar = new ArrayList<EstudianteValidacionJdbcDto>();
				acfPersonaPostulante = new Persona();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionConocimientos.registro.comprobante.pago.error")));
			} catch (RolFlujoCarreraException e) {
				iniciarParametros();
				acfPersonaPostulante = null;
				acfListaEstudianteValidacionJdbcDtoEditar = null;
				acfEstudianteValidacionJdbcDtoBuscar=null;
				acfEstudianteValidacionJdbcDtoBuscar = new EstudianteValidacionJdbcDto();
				acfListaEstudianteValidacionJdbcDtoEditar = new ArrayList<EstudianteValidacionJdbcDto>();
				acfPersonaPostulante = new Persona();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionConocimientos.registro.comprobante.pago.error")));
			}
			iniciarParametros();
			return "irRegresarListarActualizar";
		}else{
			acfValidadorClic=0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ActualizacionConocimientos.comprobante.pago.en.blanco")));
			return null;
		}

	}
	
	public Integer validarReproboComplexivo(EstudianteValidacionJdbcDto estudiante){
		Integer retorno=GeneralesConstantes.APP_ID_BASE;
		try {
			retorno=servTramiteTituloDtoServicioJdbc.burcarTramiteTituloDesactivadoReproboComplex(estudiante.getPrsIdentificacion());
			
		} catch (Exception e) {
			retorno=GeneralesConstantes.APP_ID_BASE;
		}
		if(retorno==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE){
			return ValidacionConstantes.SI_REPROBO_COMPLEXIVO_2014_VALUE;
		}
		
		return retorno;
	}	
	
	
	public String irCancelarActualizar(){
		limpiar();
		acfPersonaPostulante = null;
		acfListaEstudianteValidacionJdbcDtoEditar = null;
		acfEstudianteValidacionJdbcDtoBuscar=null;
		return "irCancelarActualizar";
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		acfCarrera=new CarreraDto();
		acfConvocatoria = new Convocatoria();
		acfEstudianteValidacionJdbcDtoBuscar = new EstudianteValidacionJdbcDto();
		acfListEstudianteValidacionJdbcDto = null;
		acfListEstudianteValidacionJdbcDto= new ArrayList<EstudianteValidacionJdbcDto>();
		acfDeshabilitadoExportar = true;
		acfValidadorClic=0;
	}
	
	
	public String verificarClickAceptarTramite(){
		acfValidadorClic = 1;
		acfHabilitadorBotonSi=true;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		acfValidadorClic = 0;
		return null;
	}
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/

	public CarreraDto getAcfCarrera() {
		return acfCarrera;
	}

	public void setAcfCarrera(CarreraDto acfCarrera) {
		this.acfCarrera = acfCarrera;
	}

	public FacultadDto getAcfFacultad() {
		return acfFacultad;
	}

	public void setAcfFacultad(FacultadDto acfFacultad) {
		this.acfFacultad = acfFacultad;
	}

	public List<CarreraDto> getAcfListCarreras() {
		acfListCarreras = acfListCarreras==null?(new ArrayList<CarreraDto>()):acfListCarreras;
		return acfListCarreras;
	}

	public void setAcfListCarreras(List<CarreraDto> acfListCarreras) {
		this.acfListCarreras = acfListCarreras;
	}

	public EstudianteValidacionJdbcDto getAcfEstudianteValidacionJdbcDtoBuscar() {
		return acfEstudianteValidacionJdbcDtoBuscar;
	}

	public void setAcfEstudianteValidacionJdbcDtoBuscar(
			EstudianteValidacionJdbcDto acfEstudianteValidacionJdbcDtoBuscar) {
		this.acfEstudianteValidacionJdbcDtoBuscar = acfEstudianteValidacionJdbcDtoBuscar;
	}

	public Usuario getAcfUsuarioQueCambia() {
		return acfUsuarioQueCambia;
	}

	public void setAcfUsuarioQueCambia(Usuario acfUsuarioQueCambia) {
		this.acfUsuarioQueCambia = acfUsuarioQueCambia;
	}

	public List<EstudianteValidacionJdbcDto> getAcfListEstudianteValidacionJdbcDto() {
		return acfListEstudianteValidacionJdbcDto;
	}

	public void setAcfListEstudianteValidacionJdbcDto(
			List<EstudianteValidacionJdbcDto> acfListEstudianteValidacionJdbcDto) {
		this.acfListEstudianteValidacionJdbcDto = acfListEstudianteValidacionJdbcDto;
	}

	public Persona getAcfPersonaPostulante() {
		return acfPersonaPostulante;
	}

	public void setAcfPersonaPostulante(Persona acfPersonaPostulante) {
		this.acfPersonaPostulante = acfPersonaPostulante;
	}

	public ConfiguracionCarreraDto getacfConfiguracionCarrera() {
		return acfConfiguracionCarrera;
	}

	public void setacfConfiguracionCarrera(
			ConfiguracionCarreraDto acfConfiguracionCarrera) {
		this.acfConfiguracionCarrera = acfConfiguracionCarrera;
	}

	public Convocatoria getacfConvocatoria() {
		return acfConvocatoria;
	}

	public void setAcfConvocatoria(Convocatoria acfConvocatoria) {
		this.acfConvocatoria = acfConvocatoria;
	}

	public List<Convocatoria> getAcfListConvocatorias() {
		acfListConvocatorias = acfListConvocatorias==null?(new ArrayList<Convocatoria>()):acfListConvocatorias;
		return acfListConvocatorias;
	}

	public void setAcfListConvocatorias(List<Convocatoria> acfListConvocatorias) {
		this.acfListConvocatorias = acfListConvocatorias;
	}
	

	public Boolean getAcfDeshabilitadoExportar() {
		return acfDeshabilitadoExportar;
	}

	public void setAcfDeshabilitadoExportar(Boolean acfDeshabilitadoExportar) {
		this.acfDeshabilitadoExportar = acfDeshabilitadoExportar;
	}

	public Integer getAcfValidadorClic() {
		return acfValidadorClic;
	}

	public void setAcfValidadorClic(Integer acfValidadorClic) {
		this.acfValidadorClic = acfValidadorClic;
	}

	public Convocatoria getAcfConvocatoria() {
		return acfConvocatoria;
	}

	public boolean isAcfHabilitadorBotonSi() {
		return acfHabilitadorBotonSi;
	}

	public void setAcfHabilitadorBotonSi(boolean acfHabilitadorBotonSi) {
		this.acfHabilitadorBotonSi = acfHabilitadorBotonSi;
	}

	public List<EstudianteValidacionJdbcDto> getAcfListaEstudianteValidacionJdbcDtoEditar() {
		acfListaEstudianteValidacionJdbcDtoEditar = acfListaEstudianteValidacionJdbcDtoEditar==null?(new ArrayList<EstudianteValidacionJdbcDto>()):acfListaEstudianteValidacionJdbcDtoEditar;
		return acfListaEstudianteValidacionJdbcDtoEditar;
	}

	public void setAcfListaEstudianteValidacionJdbcDtoEditar(
			List<EstudianteValidacionJdbcDto> acfListaEstudianteValidacionJdbcDtoEditar) {
		this.acfListaEstudianteValidacionJdbcDtoEditar = acfListaEstudianteValidacionJdbcDtoEditar;
	}

	public String getAcfComprobantePago() {
		return acfComprobantePago;
	}

	public void setAcfComprobantePago(String acfComprobantePago) {
		this.acfComprobantePago = acfComprobantePago;
	}

	public UsuarioRol getUsro() {
		return usro;
	}

	public void setUsro(UsuarioRol usro) {
		this.usro = usro;
	}
	
	
	
	
}

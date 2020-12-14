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
   
 ARCHIVO:     ReportesForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de reportes de los usuarios. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.reportes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEstadoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistradosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistradosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ReportesDtoJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.managedBeans.request.generales.GeneralesConstantesForm;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReportesForm.
 * Managed Bean que maneja las peticiones de reportes de los usuarios.
 * @author dalbuja.
 * @version 1.0
 */
@ManagedBean(name="reportesForm")
@SessionScoped
public class ReportesForm implements Serializable{
	private static final long serialVersionUID = 5869128123295204551L;
	// *****************************************************************/
	// ******************* DataSource de ReportesForm ******************/
	// *****************************************************************/
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	// *****************************************************************/
	// ******************* Variables de ReportesForm *******************/
	// *****************************************************************/
	List<SelectItem> listaConvocatoria=new ArrayList<SelectItem>();
	List<SelectItem> listaFacultad=new ArrayList<SelectItem>();
	List<SelectItem> listaCarrera=new ArrayList<SelectItem>();
	private String convocatoriaSeleccionada;
	private String facultadSeleccionada;
	private String carreraSeleccionada;
	private String facultadSecretaria;
	private Boolean refDeshabilitado;
	private Boolean refDeshabilitadoExportar;
	private String frmRfCedulaPostulante;
	private List<EstudianteEstadoJdbcDto> frmRfListaEstadosPostulantes;
	private List<EstudianteEstadoJdbcDto> frmRfListaEstadosPostulantesVisualizar;
	private List<EstudiantePostuladoJdbcDto> frmRfListaCalificacionPostulantes;
	private List<EstudiantePostuladoJdbcDto> frmRfListaCalificacionPostulantesOrdenada;
	private Usuario usuarioQueConsulta;
	private Facultad facultadReporteEstado;
	private Carrera carreraReporteEstado;
	private List<CarreraDto> rfListCarreras;
	
	private Convocatoria convocatoriaReporteEstado;
	private List<Convocatoria> rfListConvocatorias;
	private List<Facultad> rfListFacultades;
	private List<Carrera> rfListCarrera;
	private List<EstudiantePostuladoJdbcDto> rfListMeritos;
	private Integer rfTipoComplexivo;
	private boolean rfSeleccionarConvocatoria;
	private boolean rfSeleccionarFacultad;
	private boolean rfSeleccionarCarrera;
	private boolean rfBuscar;
	private boolean rfComplexivo;
	private Usuario rfUsuarioQueCambia;
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private ConvocatoriaServicio srvConvocatoriaServicio;
	

	@EJB
	private FacultadServicio srvFacultadServicio;
	
	@EJB
	private CarreraServicio srvCarreraServicio;
	
	@EJB
	private UsuarioRolServicio srvUsuarioRolServicio;

	@EJB
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB
	private CarreraServicio servCarreraServicio;
	
	@EJB
	private PersonaServicio servPersonaServicio;
	
	@EJB
	private ReportesDtoJdbc srvReportesDtoJdbc;
	@EJB
	private CarreraDtoServicioJdbc srvCarreraDtoServicioJdbc;
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	/**
	 * Método que llena la lista de convocatorias para los combobox
	 * @return List<SelectItem>
	 */
	public List<SelectItem> rellenarConvocatorias() {
		try {
			List<Convocatoria> listaCnv=new ArrayList<>();
			listaConvocatoria=new ArrayList<SelectItem>();
			listaCnv=srvConvocatoriaServicio.listarTodosActivas();
			for (Convocatoria convocatoria : listaCnv) {
				listaConvocatoria.add(new SelectItem(convocatoria.getCnvDescripcion()));
			}
			return listaConvocatoria;
		} catch (ConvocatoriaNoEncontradoException e) {
		}
		return null;
	}

	
	public void iniciarParametros(){
		try {
			rfTipoComplexivo=null;
			rfListConvocatorias =  srvConvocatoriaServicio.listarTodosActivas();
//			Convocatoria convocatoriaTodos = new Convocatoria();
//			convocatoriaTodos.setCnvDescripcion(GeneralesConstantes.APP_SELECCIONE_TODOS);
//			convocatoriaTodos.setCnvId(GeneralesConstantes.APP_ID_BASE);
//			rfListConvocatorias.add(convocatoriaTodos);
			rfListFacultades=srvFacultadServicio.listarTodos();
//			Facultad facultad = new Facultad();
//			facultad.setFclDescripcion(GeneralesConstantes.APP_SELECCIONE_TODOS);
//			facultad.setFclId(GeneralesConstantes.APP_ID_BASE);
//			rfListFacultades.add(facultad);
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.convocatoria.irlistarconvocatoria.Convocatoria.No.Encontrado.Exception")));
		} catch (FacultadNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.todos.no.result.exception")));
		}
		rfSeleccionarConvocatoria=true;
		rfSeleccionarFacultad= true;
		rfSeleccionarCarrera= true;
		rfBuscar=true;	
		rfComplexivo=false;
		carreraReporteEstado = new Carrera();
		convocatoriaReporteEstado = new Convocatoria();
	}
	/**
	 * Método que llena la lista de facultades para los combobox
	 * @return List<SelectItem>
	 */
	public List<SelectItem> rellenarFacultades() {
		
		try {
			List<Facultad> listaFcl=new ArrayList<>();
			listaFacultad=new ArrayList<SelectItem>();
			listaFcl=srvFacultadServicio.listarTodos();
			for (Facultad facultad : listaFcl) {
				listaFacultad.add(new SelectItem(facultad.getFclDescripcion()));
			}
			return listaFacultad;
		} catch ( FacultadNoEncontradoException e) {
		}
		return null;
	}
	
	/**
	 * Método que llena la lista de carreras para los combobox
	 * @return List<SelectItem>
	 */
	public List<SelectItem> rellenarCarreras(Usuario usuario) {
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		sbSql.append(" DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" AS ");
		sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
		sbSql.append(" AS ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
		sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr,");
		sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
		sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr,");
		sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl");
		sbSql.append(" WHERE usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USR_ID);
		sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = roflcr.");sbSql.append(JdbcConstantes.USRO_ID);
		sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.FCL_ID);
		sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
		sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" = ?");
		sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, usuario.getUsrIdentificacion());
			List<SelectItem> retorno = new ArrayList<SelectItem>();
			rs = pstmt.executeQuery();
			while(rs.next()){
				facultadSeleccionada=rs.getString("facultad");
				retorno.add(new SelectItem(transformarResultSet(rs)));
			}
			rs.close();
			pstmt.close();
			con.close();
			return retorno;
		} catch (SQLException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.carreras.no.encontradas.por.usuario")));
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
					}
				} catch (SQLException e) {
				}
		}
		return null;
	}
	
//	//****************************************************************/
//	//******** METODOS DE NAVEGACION ENTRE PAGINAS *******************/
//	//****************************************************************/
//	
	/** 
	 * Método que muestra el mensaje que direciona a la página de Reportes de Estados de los postulantes
	 * @param usuario que realiza la petición de la página
	 * @return irReportesEstadoPostulantes
	 */
	public String irReportesEstadoPostulantes(Usuario usuario){
		try {
			UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionActivos(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
		} catch (UsuarioRolNoEncontradoException e) {
		} catch (UsuarioRolException e) {
		} 
		convocatoriaSeleccionada=GeneralesConstantes.APP_SELECCIONE;
		listaConvocatoria=rellenarConvocatorias();
		listaFacultad=rellenarFacultades();
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
		frmRfCedulaPostulante=null;
		usuarioQueConsulta=usuario;
		facultadReporteEstado= new Facultad();
		convocatoriaReporteEstado = new Convocatoria();
		iniciarParametros();
		return "irReporteEstados";
	}
	
	/** 
	 * Método que muestra el mensaje que direciona a la página de Reportes de Estados de los postulantes
	 * @param usuario que realiza la petición de la página
	 * @return irReportesEstadoPostulantes
	 */
	public String irReportesValidacionPostulantes(Usuario usuario){
		try {
			UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionActivos(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
		} catch (UsuarioRolNoEncontradoException e) {
		} catch (UsuarioRolException e) {
		} 
		convocatoriaSeleccionada=GeneralesConstantes.APP_SELECCIONE;
		listaConvocatoria=rellenarConvocatorias();
		listaFacultad=rellenarFacultades();
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
		frmRfCedulaPostulante=null;
		usuarioQueConsulta=usuario;
		facultadReporteEstado= new Facultad();
		convocatoriaReporteEstado = new Convocatoria();
		iniciarParametros();
		rfListCarreras = new ArrayList<CarreraDto>();
		try {
			rfListCarreras = srvCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFl(usuario.getUsrId(),RolConstantes.ROL_BD_EVALUADOR,RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
		} catch (CarreraDtoJdbcException | CarreraDtoJdbcNoEncontradoException e) {
		}
		return "irReporteValidacion";
	}
//	
//	
//	public String irReportesMeritosApelaciones(Usuario usuario){
//		try {
//			UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionActivos(usuario.getUsrIdentificacion());
//			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
//				FacesUtil.mensajeInfo("Su usuario se encuentra desactivado.");
//				return null;
//			}
//			iniciarParametros();
//			if(usro.getUsroRol().getRolId()!= RolConstantes.ROL_BD_EDITOR_DGIP_VALUE ||
//					usro.getUsroRol().getRolId()!=RolConstantes.ROL_BD_EVALUADOR_VALUE	){
//				rfListCarreras = new ArrayList<CarreraDto>();
//				try {
//					rfListCarreras=servEstudianteValidacionDtoServicioJdbc.buscarCarrerasXEvaluador(usuario.getUsrIdentificacion());
//					if(rfListCarreras.size()==0){
//						try {
//							rfListCarreras=servEstudianteValidacionDtoServicioJdbc.buscarCarrerasXDGIP();
//							convocatoriaSeleccionada=GeneralesConstantes.APP_SELECCIONE;
//							listaConvocatoria=rellenarConvocatorias();
//							refDeshabilitado = false;
//							refDeshabilitadoExportar = true;
//							frmRfCedulaPostulante=null;
//							usuarioQueConsulta=usuario;
//							facultadReporteEstado= new Facultad();
//							convocatoriaReporteEstado = new Convocatoria();
//							carreraReporteEstado = new Carrera();
//							rfListMeritos = new ArrayList<EstudiantePostuladoJdbcDto>();
//							return "irReportesMeritosApelaciones";
//						} catch (CarreraDtoJdbcNoEncontradoException e1) {
//						} catch (SQLException e1) {
//						}
//						if(rfListCarreras.size()==0){
//							FacesUtil.limpiarMensaje();
//							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
//							return null;	
//						}
//						
//					}else{
//						for (CarreraDto item : rfListCarreras) {
//							if(item.getCrrFacultad()==FacultadConstantes.FACULTAD_MEDICINA_ID){
//								convocatoriaSeleccionada=GeneralesConstantes.APP_SELECCIONE;
//								listaConvocatoria=rellenarConvocatorias();
//								refDeshabilitado = false;
//								refDeshabilitadoExportar = true;
//								frmRfCedulaPostulante=null;
//								usuarioQueConsulta=usuario;
//								facultadReporteEstado= new Facultad();
//								convocatoriaReporteEstado = new Convocatoria();
//								carreraReporteEstado = new Carrera();
//								rfListMeritos = new ArrayList<EstudiantePostuladoJdbcDto>();
//								return "irReportesMeritosApelaciones";						
//							}
//						}
//					}
//				} catch (CarreraDtoJdbcNoEncontradoException | SQLException e) {
//					e.printStackTrace();
//					try {
//						rfListCarreras=servEstudianteValidacionDtoServicioJdbc.buscarCarrerasXDGIP();
//						return "irReportesMeritosApelaciones";
//					} catch (CarreraDtoJdbcNoEncontradoException e1) {
//					} catch (SQLException e1) {
//					}
//				}	
//			}
//			
//		} catch (UsuarioRolNoEncontradoException e) {
//		} catch (UsuarioRolException e) {
//		} 
//		return null;
//	}
//	
//	
//	
//	
	/** 
	 * Método que muestra el mensaje que direciona a la página de Inicio
	 * 
	 * @return irInicio
	 */
	public String irInicio(){
		FacesUtil.limpiarMensaje();
		carreraSeleccionada=null;
		convocatoriaSeleccionada=null;
		cambiarCarrera();
		cambiarConvocatoria();
		cambiarFacultad();
		facultadSeleccionada=null;
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
		frmRfCedulaPostulante=null;
		// CAMBIAR CUANDO EST EL EXPORTAR
//		frmRfListaEstadosPostulantesVisualizar=null;
		facultadReporteEstado= new Facultad();
		rfComplexivo=false;
		FacesUtil.limpiarMensaje();
		return "irInicio";
	}

	/** 
	 * Método que limpia los componenentes del formulario
	 * 
	 */
	public void limpiar(){
		carreraSeleccionada=null;
		convocatoriaSeleccionada=null;
		cambiarCarrera();
		cambiarConvocatoria();
		cambiarFacultad();
		facultadSeleccionada=null;
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
		frmRfCedulaPostulante=null;
		// CAMBIAR CUANDO EST EL EXPORTAR
		frmRfListaEstadosPostulantesVisualizar=null;
		frmRfListaEstadosPostulantes= null;
		frmRfCedulaPostulante=null;
		facultadReporteEstado= new Facultad();
		convocatoriaReporteEstado = new Convocatoria();
		frmRfListaCalificacionPostulantes=null;
		rfListMeritos = new ArrayList<EstudiantePostuladoJdbcDto>();
		carreraReporteEstado = new Carrera();
//		try {
//			rfListCarreras=servEstudianteValidacionDtoServicioJdbc.buscarCarrerasXEvaluador(usuarioQueConsulta.getUsrIdentificacion());
//		} catch (CarreraDtoJdbcNoEncontradoException | SQLException e) {
//		}
		FacesUtil.limpiarMensaje();
		
	}
//	
//	//****************************************************************/
//	//******** METODOS de GENERACION DE DATOS PARA LOS REPORTES*******/
//	//****************************************************************/
//	
//	
	/**Método que genera los datos para el reporte de postulantes
	 * 
	 * @param convocatoria -- cadena que contiene la convocatoria
	 * @param facultad cadena que contine la facultad
	 */
	public void generarDatosReporteEstados(Integer convocatoria, Integer facultad){
		try {
			frmRfListaEstadosPostulantes  = srvReportesDtoJdbc.listaPersonaEstado(convocatoria,facultad, frmRfCedulaPostulante);
			if(frmRfListaEstadosPostulantes.size() > 0){
				generarReporteEstados(frmRfListaEstadosPostulantes);
				refDeshabilitado = true;
				refDeshabilitadoExportar = false;
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
			}
			
		} catch (RegistradosDtoJdbcException e) {
			frmRfListaEstadosPostulantesVisualizar=null;
			cambiarCarrera();
			cambiarConvocatoria();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (RegistradosDtoJdbcNoEncontradoException e) {
			frmRfListaEstadosPostulantesVisualizar=null;
			cambiarCarrera();
			cambiarConvocatoria();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**Método que genera los datos para el reporte de postulantes
	 * 
	 * @param convocatoria -- cadena que contiene la convocatoria
	 * @param facultad cadena que contine la facultad
	 */
	public void generarDatosReporteValidados(){
//		try {
////			frmRfListaEstadosPostulantes  = srvReportesDtoJdbc.listaPersonaEstado(frmRfCedulaPostulante);
//			if(frmRfListaEstadosPostulantes.size() > 0){
//				generarReporteEstados(frmRfListaEstadosPostulantes);
//				refDeshabilitado = true;
//				refDeshabilitadoExportar = false;
//			}else{
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
//			}
//			
//		} catch (RegistradosDtoJdbcException e) {
//			frmRfListaEstadosPostulantesVisualizar=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (RegistradosDtoJdbcNoEncontradoException e) {
//			frmRfListaEstadosPostulantesVisualizar=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}
	}
	
//	
//	/**Método que genera los datos para el reporte de postulantes
//	 * 
//	 * @param convocatoria -- cadena que contiene la convocatoria
//	 * @param facultad cadena que contine la facultad
//	 */
//	public void generarDatosReporteMigracionDatos(Integer convocatoria, Integer facultad){
//		try {
//			frmRfListaEstadosPostulantes  = srvReportesDtoJdbc.listarDatosMigracionAcademico(convocatoria,facultad);
//			if(frmRfListaEstadosPostulantes.size() > 0){
//				generarReporteMigracion(frmRfListaEstadosPostulantes);
//				refDeshabilitado = true;
//				refDeshabilitadoExportar = false;
//			}else{
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
//			}
//		}  catch (PersonaDtoJdbcException e) {
//			frmRfListaEstadosPostulantesVisualizar=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (PersonaNoEncontradoException e) {
//			frmRfListaEstadosPostulantesVisualizar=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}
//	}
//	
//	
//	/**Método que genera los datos para el reporte de calificación para los coordinadores
//	 * 
//	 * @param convocatoria -- cadena que contiene la convocatoria
//	 * @param facultad cadena que contine la facultad
//	 */
//	public void generarDatosCalificaciones(){
//		try {
//			frmRfListaCalificacionPostulantes  = srvReportesDtoJdbc.buscarCalificaciones(frmRfCedulaPostulante,carreraReporteEstado.getCrrId() , convocatoriaReporteEstado.getCnvId(), rfUsuarioQueCambia.getUsrIdentificacion());
//			if(frmRfListaCalificacionPostulantes.size() > 0){
//				frmRfListaCalificacionPostulantesOrdenada = new ArrayList<EstudiantePostuladoJdbcDto>();
//				frmRfListaCalificacionPostulantesOrdenada= ordenarDescendente(frmRfListaCalificacionPostulantes);
//				Persona persona = new Persona();
//				try {
//					persona= servPersonaServicio.buscarPorIdentificacion(rfUsuarioQueCambia.getUsrIdentificacion());
//				} catch (PersonaNoEncontradoException e) {
//				} catch (PersonaException e) {
//				}
//				generarReporteCalificaciones(frmRfListaCalificacionPostulantes,
//						persona.getPrsNombres()+" "+persona.getPrsPrimerApellido()+" "+persona.getPrsSegundoApellido());
//				refDeshabilitado = true;
//				refDeshabilitadoExportar = false;
//			}else{
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
//			}
//		}   catch (EstudiantePostuladoJdbcDtoException e) {
//			frmRfListaCalificacionPostulantes=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (EstudiantePostuladoJdbcDtoNoEncontradoException e) {
//			frmRfListaCalificacionPostulantes=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}
//	}
//	
//    static List<EstudiantePostuladoJdbcDto> ordenarDescendente(List<EstudiantePostuladoJdbcDto> lista)
//    {
//    	int i, j;
//        for(i=0;i<lista.size()-1;i++)
//             for(j=0;j<lista.size()-i-1;j++){
//            	int comparador = lista.get(j+1).getOpsNotaFinal().compareTo(lista.get(j).getOpsNotaFinal());
//        		if (comparador==1 ){
//        			EstudiantePostuladoJdbcDto aux = new EstudiantePostuladoJdbcDto();
//                     aux=lista.get(j+1);
//                     lista.set(j+1,lista.get(j));
//                     lista.set(j,aux);
//                  }
//        }
//        return lista;
//     }
//	
//    public void generarDatosMeritos(){
//		try {
//    	
//			rfListMeritos  = srvReportesDtoJdbc.buscarMeritos(convocatoriaReporteEstado,carreraReporteEstado, frmRfCedulaPostulante,usuarioQueConsulta.getUsrIdentificacion());
//			if(rfListMeritos.size() > 0){
//				generarReporteMeritos(rfListMeritos);
//				refDeshabilitado = true;
//				refDeshabilitadoExportar = false;
//			}else{
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
//			}
//		} catch (EstudiantePostuladoJdbcDtoException e) {
//			rfListMeritos=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (EstudiantePostuladoJdbcDtoNoEncontradoException e) {
//			rfListMeritos=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}
//	}
//	
//
//    public void generarDatosMeritosApelaciones(){
//		try {
//    	
//			rfListMeritos  = srvReportesDtoJdbc.buscarMeritosApelaciones(convocatoriaReporteEstado,carreraReporteEstado, frmRfCedulaPostulante,usuarioQueConsulta.getUsrIdentificacion());
//			if(rfListMeritos.size() > 0){
//				generarReporteMeritos(rfListMeritos);
//				refDeshabilitado = true;
//				refDeshabilitadoExportar = false;
//			}else{
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
//			}
//		} catch (EstudiantePostuladoJdbcDtoException e) {
//			rfListMeritos=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (EstudiantePostuladoJdbcDtoNoEncontradoException e) {
//			rfListMeritos=null;
//			cambiarCarrera();
//			cambiarConvocatoria();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}
//	}
//
//    
//    /**
//	 * Método que habilta el botón de cargar archivo
//	 * 
//	 */
//	public void cambiarTipoComplexivo() {
//		try {
//			rfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
//			rfListFacultades=srvFacultadServicio.listarTodos();
//		} catch (ConvocatoriaNoEncontradoException e) {
//		} catch (FacultadNoEncontradoException e) {
//		}
//		rfSeleccionarConvocatoria = true;
//		if(rfTipoComplexivo==GeneralesConstantes.APP_ID_BASE){
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cargar.notas.error.seleccionar.tipo.complexivo")));
//			
//			rfSeleccionarFacultad = true;
//			rfSeleccionarCarrera = true;
//			facultadReporteEstado= new Facultad();
//			carreraReporteEstado = new Carrera();
//			convocatoriaReporteEstado = new Convocatoria();
//		}else {
//			rfSeleccionarConvocatoria=false;
//		}
//	}
//	
//	
	//****************************************************************/
	//******** METODOS de GENERACION DE REPORTES *********************/
	//****************************************************************/
	
	
	/**
	* Genera el reporte de registrados y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return void
	*/
	public static void generarReporteEstados(List<EstudianteEstadoJdbcDto> listaEstados){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
		// ****************************************************************//
		// ****************************************************************//
		frmCrpNombreReporte = "ReporteEstadosPostulantes";
		java.util.Date date= new java.util.Date();
		frmCrpParametros = new HashMap<String, Object>();
		String fecha = new Timestamp(date.getTime()).toString();
		frmCrpParametros.put("fecha",fecha);
		
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		GeneralesConstantesForm auxEstados = new GeneralesConstantesForm();
		for (EstudianteEstadoJdbcDto item : listaEstados) {
			dato = new HashMap<String, Object>();
			dato.put("convocatoria",item.getCnvDescripcion());
			dato.put("cedula", item.getPrsIdentificacion());
			dato.put("apellido1", item.getPrsPrimerApellido());
			dato.put("apellido2", item.getPrsSegundoApellido());
			dato.put("nombres", item.getPrsNombres());
			dato.put("facultad", item.getFclDescripcion());
			dato.put("carrera", item.getCrrDescripcion());
			dato.put("celular", item.getPrsCelular());
			dato.put("telefono", item.getPrsTelefono());
			dato.put("correo", item.getPrsMailPersonal());
			dato.put("registroSenescyt", item.getFcesRegTtlSenecyt());
			dato.put("estado", auxEstados.getEstadoEstudiante(item.getTrttEstadoProceso()));
//			if(item.getMrtCalificacion()!=null){
//				dato.put("meritos", item.getMrtCalificacion().toString());
//			}else{
//				dato.put("meritos", "N/A");	
//			}
//			if(item.getOpsEntrevista()!=null){
//				dato.put("entrevista", item.getOpsEntrevista().toString());
//			}else{
//				dato.put("entrevista", "N/A");	
//			}
//			if(item.getOpsNotaOposicion()!=null){
//				dato.put("oposicion", item.getOpsNotaOposicion().toString());
//			}else{
//				dato.put("oposicion", "N/A");	
//			}
//			if(item.getOpsNotaFinal()!=null){
//				dato.put("calificacion", item.getOpsNotaFinal().toString());
//			}else{
//				dato.put("calificacion", "N/A");	
//			}
			frmCrpCampos.add(dato);
		}
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		// ******************FIN DE GENERACION DE REPORTE ************//	
	} 
	
	
//	/**
//	* Genera el reporte de registrados y lo establece en la sesión como atributo
//	* @param List<RegistradosDto> frmRfRegistradosDto
//	* @return void
//	*/
//	public static void generarReporteMigracion(List<EstudianteEstadoJdbcDto> listaEstados){
//		 List<Map<String, Object>> frmCrpCampos = null;
//		 Map<String, Object> frmCrpParametros = null;
//		 String frmCrpNombreReporte = null;
//		// ****************************************************************//
//		// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
//		// ****************************************************************//
//		// ****************************************************************//
//		frmCrpNombreReporte = "ReporteMigracion";
//		java.util.Date date= new java.util.Date();
//		frmCrpParametros = new HashMap<String, Object>();
//		String fecha = new Timestamp(date.getTime()).toString();
//		frmCrpParametros.put("fecha",fecha);
//		
//		frmCrpCampos = new ArrayList<Map<String, Object>>();
//		Map<String, Object> dato = null;
////		GeneralesConstantesForm auxEstados = new GeneralesConstantesForm();
//		for (EstudianteEstadoJdbcDto item : listaEstados) {
//			dato = new HashMap<String, Object>();
//			dato.put("tipo_identificacion",String.valueOf(item.getPrsTipoIdentificacion()));
//			dato.put("cedula", item.getPrsIdentificacion());
//			dato.put("apellido1", item.getPrsPrimerApellido());
//			dato.put("apellido2", item.getPrsSegundoApellido());
//			dato.put("nombres", item.getPrsNombres());
//			dato.put("sexo", String.valueOf(item.getPrsSexo()));
//			dato.put("email", item.getPrsMailPersonal());
//			dato.put("mail_institucional", "N/A");
//			dato.put("nota_enes", "-99");
//			dato.put("carrera", String.valueOf(item.getCrrId()));
//			dato.put("area", "-99");
//			dato.put("nuevo", "0");
//			frmCrpCampos.add(dato);
//		}
//		// Establecemos en el atributo de la sesión la lista de mapas de
//		// datos frmCrpCampos
//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//		HttpSession httpSession = request.getSession(false);
//		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
//		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
//		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
//		// ******************FIN DE GENERACION DE REPORTE ************//	
//	} 
//	
//	/**
//	* Genera el reporte de registrados y lo establece en la sesión como atributo
//	* @param List<RegistradosDto> frmRfRegistradosDto
//	* @return void
//	*/
//	public static void generarReporteCalificaciones(List<EstudiantePostuladoJdbcDto> listaCalificaciones, String nombresCoordinador){
//		 List<Map<String, Object>> frmCrpCampos = null;
//		 Map<String, Object> frmCrpParametros = null;
//		 String frmCrpNombreReporte = null;
//		// ****************************************************************//
//		// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
//		// ****************************************************************//
//		// ****************************************************************//
//		frmCrpNombreReporte = "reportePosgrado";
//		frmCrpParametros = new HashMap<String, Object>();
//		frmCrpCampos = new ArrayList<Map<String, Object>>();
//		Map<String, Object> dato = null;
//		frmCrpParametros.put("programa",listaCalificaciones.get(0).getCrrDescripcion());
//		
//		frmCrpParametros.put("coordinador",nombresCoordinador);
//		StringBuilder pathGeneralReportes = new StringBuilder();
//		pathGeneralReportes.append(FacesContext.getCurrentInstance()
//				.getExternalContext().getRealPath("/"));
//		pathGeneralReportes.append("/postgrado/reportes/archivosJasper/reporteCalificaciones");
//		frmCrpParametros.put("logoUce", pathGeneralReportes+"/logoUce.png");
//		for (EstudiantePostuladoJdbcDto item : listaCalificaciones) {
//			dato = new HashMap<String, Object>();
//			dato.put("nombres", item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres());
//			dato.put("meritos", item.getMrtCalificacion().toString());
//			dato.put("oposicion", item.getOpsNotaOposicion().toString());
//			dato.put("entrevista", item.getOpsEntrevista().toString());
//			dato.put("total", item.getOpsNotaFinal().toString());
//			frmCrpCampos.add(dato);
//		}
//		// Establecemos en el atributo de la sesión la lista de mapas de
//		// datos frmCrpCampos
//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//		HttpSession httpSession = request.getSession(false);
//		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
//		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
//		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
//		// ******************FIN DE GENERACION DE REPORTE ************//	
//	} 
//	
//	
//
//	/**
//	* Genera el reporte de registrados y lo establece en la sesión como atributo
//	* @param List<RegistradosDto> frmRfRegistradosDto
//	* @return void
//	*/
//	public static void generarReporteMeritos(List<EstudiantePostuladoJdbcDto> listaMeritos){
//		 List<Map<String, Object>> frmCrpCampos = null;
//		 Map<String, Object> frmCrpParametros = null;
//		 String frmCrpNombreReporte = null;
//		// ****************************************************************//
//		// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
//		// ****************************************************************//
//		// ****************************************************************//
//		frmCrpNombreReporte = "meritosPostulantes";
//		java.util.Date date= new java.util.Date();
//		frmCrpParametros = new HashMap<String, Object>();
//		String fecha = new Timestamp(date.getTime()).toString();
//		frmCrpParametros.put("fecha",fecha);
//		
//		frmCrpCampos = new ArrayList<Map<String, Object>>();
//		Map<String, Object> dato = null;
//		for (EstudiantePostuladoJdbcDto item : listaMeritos) {
//			dato = new HashMap<String, Object>();
//			dato.put("total",item.getMrtCalificacion().toString());
//			dato.put("cedula", item.getPrsIdentificacion());
//			dato.put("apellido1", item.getPrsPrimerApellido());
//			dato.put("apellido2", item.getPrsSegundoApellido());
//			dato.put("nombres", item.getPrsNombres());
//			dato.put("facultad", item.getFclDescripcion());
//			dato.put("carrera", item.getCrrDescripcion());
//			int comparador ;
//			comparador=item.getMrtCertificadoAnalitico().compareTo(new BigDecimal(GeneralesConstantes.APP_ID_BASE));
//			if(comparador!=0){
//				dato.put("indicador", item.getMrtCertificadoAnalitico().toString());	
//			}else{
//				dato.put("indicador", "0");
//			}
//			if(item.getMrtPublicaciones()==0){
//				dato.put("articulos", item.getMrtTotalPublicaciones().toString());	
//			}else{
//				dato.put("articulos", "0");
//			}
//			if(item.getMrtLibros()==0){
//				dato.put("libros", item.getMrtTotalLibros().toString());	
//			}else{
//				dato.put("libros", "0");
//			}
//			if(item.getMrtDoctorado()==0){
//				dato.put("doctorado", item.getMrtDoctorado().toString());	
//			}else{
//				dato.put("doctorado", "0");
//			}
//			if(item.getMrtEspecialidad()==0){
//				dato.put("especialidad", item.getMrtTotalLibros().toString());	
//			}else{
//				dato.put("especialidad", "0");
//			}
//			if(item.getMrtMaestria()==0){
//				dato.put("maestria", item.getMrtMaestria().toString());	
//			}else{
//				dato.put("maestria", "0");
//			}
//			BigDecimal nuevaSumaCursos;
//			if(item.getMrtCantidadCursos()!=GeneralesConstantes.APP_ID_BASE){
//				nuevaSumaCursos = new BigDecimal(0);
//				nuevaSumaCursos=(nuevaSumaCursos.add(MeritosOposicionConstantes.PUNTOS_CURSOS_AVALADOS_MEDICINA.multiply(new BigDecimal(item.getMrtCantidadCursos()))).setScale(1, RoundingMode.DOWN));	
//			}else{
//				nuevaSumaCursos = new BigDecimal(0);
//			}
//			dato.put("cursos", nuevaSumaCursos.toString());	
//			if(item.getMrtAyudanteCatedra()==0){
//				dato.put("ayudantia", item.getMrtTotalAyudantia().toString());	
//			}else{
//				dato.put("ayudantia", "0");
//			}
//			if(item.getMrtMejorEgresado()==0){
//				dato.put("mejorEgresado", String.valueOf(MeritosOposicionConstantes.PUNTOS_MEJOR_EGRESADO));	
//			}else{
//				dato.put("mejorEgresado", "0");
//			}
//			if(item.getMrtTercerEgresado()==0){
//				dato.put("tercerEgresado", MeritosOposicionConstantes.PUNTOS_TERCER_LUGAR_MEDICINA.toString());	
//			}else{
//				dato.put("tercerEgresado", "0");
//			}
//			if(item.getMrtSegundoEgresado()==0){
//				dato.put("segundoEgresado", String.valueOf(MeritosOposicionConstantes.PUNTOS_SEGUNDO_LUGAR));	
//			}else{
//				dato.put("segundoEgresado", "0");
//			}
//			if(item.getMrtPremioInvestigacion()==0){
//				dato.put("premio", item.getMrtTotalPremios().toString());	
//			}else{
//				dato.put("premio", "0");
//			}
//			if(item.getMrtBecas()==0){
//				dato.put("becas", item.getMrtTotalBecas().toString());	
//			}else{
//				dato.put("becas", "0");
//			}
//			if(item.getMrtResidencia()==0){
//				dato.put("residencia", item.getMrtTotalResidencia().toString());	
//			}else{
//				dato.put("residencia", "0");
//			}
//			if(item.getMrtParticipacion()==0){
//				dato.put("proyectos", item.getMrtTotalParticipacion().toString());	
//			}else{
//				dato.put("proyectos", "0");
//			}
//			if(item.getMrtConferencista()==0){
//				dato.put("certificados", item.getMrtTotalConferencia().toString());	
//			}else{
//				dato.put("certificados", "0");
//			}
//			if(item.getMrtMovilidadHumana()==0){
//				dato.put("movilidad", "0.5");	
//			}else{
//				dato.put("movilidad", "0");
//			}
//			if(item.getMrtDiscapacidad()==0){
//				dato.put("discapacidad", "0.5");	
//			}else{
//				dato.put("discapacidad", "0");
//			}
//			if(item.getMrtRuralidad()==0){
//				dato.put("ruralidad", "0.5");	
//			}else{
//				dato.put("ruralidad", "0");
//			}
//			if(item.getMrtPobreza()==0){
//				dato.put("pobreza", "0.5");	
//			}else{
//				dato.put("pobreza", "0");
//			}
//			if(item.getMrtEtnia()==0){
//				dato.put("etnia", "0.5");	
//			}else{
//				dato.put("etnia", "0");
//			}
//			
//			
//			
//			frmCrpCampos.add(dato);
//		}
//		// Establecemos en el atributo de la sesión la lista de mapas de
//		// datos frmCrpCampos
//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//		HttpSession httpSession = request.getSession(false);
//		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
//		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
//		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
//		// ******************FIN DE GENERACION DE REPORTE ************//	
//	} 
//	
//	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	/** Método que limpia la lista de convocatorias y deshabilita el botón Exportar
	 * 
	 */
	public void cambiarConvocatoria(){
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
//		frmRfListaEstadosPostulantesVisualizar=null;
		try {
			if(convocatoriaReporteEstado.getCnvId()==GeneralesConstantes.APP_ID_BASE && rfComplexivo){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cargar.notas.error.seleccionar.convocatoria")));
				facultadReporteEstado=new Facultad();
				carreraReporteEstado= new Carrera();
				rfSeleccionarFacultad=true;
				rfSeleccionarCarrera=true;
				refDeshabilitado = true;
				try {
					rfListFacultades=srvFacultadServicio.listarTodos();
				} catch (FacultadNoEncontradoException e) {
				}
			}else {
				rfSeleccionarFacultad=false;
			}
		} catch (Exception e) {
		}
		
	}

	public void cambiarConvocatoriaCalificaciones(){
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
		frmRfListaCalificacionPostulantes= new ArrayList<EstudiantePostuladoJdbcDto>();
		try {
			if(convocatoriaReporteEstado.getCnvId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cargar.notas.error.seleccionar.convocatoria")));
				refDeshabilitado = true;
			}else if (carreraReporteEstado.getCrrId()==GeneralesConstantes.APP_ID_BASE){
				refDeshabilitado=true;
			}else{
				refDeshabilitado=false;
			}
		} catch (Exception e) {
		}
		
	}
	
	/** Método que limpia la lista de facultades y deshabilita el botón Exportar
	 * 
	 */
	public void cambiarFacultad(){
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
//		frmRfListaEstadosPostulantesVisualizar=null;
		frmRfCedulaPostulante=null;
		try {
			if(facultadReporteEstado.getFclId()==GeneralesConstantes.APP_ID_BASE){
				rfSeleccionarCarrera=true;
				if(rfComplexivo){
					rfBuscar=false;
				}
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cargar.notas.error.seleccionar.facultad")));
			}else{
				try {
					rfListCarrera=servCarreraServicio.listarCarrerasXFacultad(facultadReporteEstado);
					rfSeleccionarCarrera=false;
				} catch (CarreraNoEncontradoException e) {
					rfSeleccionarCarrera=true;
				}
				
			}
		} catch (Exception e) {
		}
		
	}
	
	/** Método que limpia la lista de facultades y deshabilita el botón Exportar
	 * 
	 */
	public void cambiarCarrera(){
		refDeshabilitado = false;
		refDeshabilitadoExportar = true;
//		frmRfListaEstadosPostulantesVisualizar=null;
		frmRfListaCalificacionPostulantes=null;
		try {
			if(carreraReporteEstado.getCrrId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cargar.notas.error.seleccionar.carrera")));
				rfBuscar=true;	
				refDeshabilitado=true;
			}else{
				rfBuscar=false;	
				refDeshabilitado=false;
			}
		} catch (Exception e) {
		}
		
	}
	
	private String transformarResultSet(ResultSet rs) throws SQLException{
			Carrera retorno = new Carrera();
			retorno.setCrrDescripcion(rs.getString("carrera"));
			return retorno.getCrrDescripcion();
		}
	/*
	 * =========================================================================
	 * ====================== Métodos Getters y Setters ========================
	 * =========================================================================
	 */

	public List<SelectItem> getListaConvocatoria() {
		return listaConvocatoria;
	}

	public void setListaConvocatoria(List<SelectItem> listaConvocatoria) {
		this.listaConvocatoria = listaConvocatoria;
	}

	public List<SelectItem> getListaFacultad() {
		return listaFacultad;
	}

	public void setListaFacultad(List<SelectItem> listaFacultad) {
		this.listaFacultad = listaFacultad;
	}

	public List<SelectItem> getListaCarrera() {
		return listaCarrera;
	}

	public void setListaCarrera(List<SelectItem> listaCarrera) {
		this.listaCarrera = listaCarrera;
	}

	public String getConvocatoriaSeleccionada() {
		return convocatoriaSeleccionada;
	}

	public void setConvocatoriaSeleccionada(String convocatoriaSeleccionada) {
		this.convocatoriaSeleccionada = convocatoriaSeleccionada;
	}

	public String getFacultadSeleccionada() {
		return facultadSeleccionada;
	}

	public void setFacultadSeleccionada(String facultadSeleccionada) {
		this.facultadSeleccionada = facultadSeleccionada;
	}

	public String getCarreraSeleccionada() {
		return carreraSeleccionada;
	}

	public void setCarreraSeleccionada(String carreraSeleccionada) {
		this.carreraSeleccionada = carreraSeleccionada;
	}

	public String getFacultadSecretaria() {
		return facultadSecretaria;
	}

	public void setFacultadSecretaria(String facultadSecretaria) {
		this.facultadSecretaria = facultadSecretaria;
	}

	public Boolean getRefDeshabilitado() {
		return refDeshabilitado;
	}

	public void setRefDeshabilitado(Boolean refDeshabilitado) {
		this.refDeshabilitado = refDeshabilitado;
	}

	public Boolean getRefDeshabilitadoExportar() {
		return refDeshabilitadoExportar;
	}

	public void setRefDeshabilitadoExportar(Boolean refDeshabilitadoExportar) {
		this.refDeshabilitadoExportar = refDeshabilitadoExportar;
	}

	public String getFrmRfCedulaPostulante() {
		return frmRfCedulaPostulante;
	}

	public void setFrmRfCedulaPostulante(String frmRfCedulaPostulante) {
		this.frmRfCedulaPostulante = frmRfCedulaPostulante;
	}


	public Usuario getUsuarioQueConsulta() {
		return usuarioQueConsulta;
	}

	public void setUsuarioQueConsulta(Usuario usuarioQueConsulta) {
		this.usuarioQueConsulta = usuarioQueConsulta;
	}

	public Facultad getFacultadReporteEstado() {
		return facultadReporteEstado;
	}

	public void setFacultadReporteEstado(Facultad facultadReporteEstado) {
		this.facultadReporteEstado = facultadReporteEstado;
	}

	public Carrera getCarreraReporteEstado() {
		return carreraReporteEstado;
	}

	public void setCarreraReporteEstado(Carrera carreraReporteEstado) {
		this.carreraReporteEstado = carreraReporteEstado;
	}

	public Convocatoria getConvocatoriaReporteEstado() {
		return convocatoriaReporteEstado;
	}

	public void setConvocatoriaReporteEstado(Convocatoria convocatoriaReporteEstado) {
		this.convocatoriaReporteEstado = convocatoriaReporteEstado;
	}

	public List<Convocatoria> getRfListConvocatorias() {
		return rfListConvocatorias;
	}

	public void setRfListConvocatorias(List<Convocatoria> rfListConvocatorias) {
		this.rfListConvocatorias = rfListConvocatorias;
	}

	public List<Facultad> getRfListFacultades() {
		return rfListFacultades;
	}

	public void setRfListFacultades(List<Facultad> rfListFacultades) {
		this.rfListFacultades = rfListFacultades;
	}

	public List<Carrera> getRfListCarrera() {
		return rfListCarrera;
	}

	public void setRfListCarrera(List<Carrera> rfListCarrera) {
		this.rfListCarrera = rfListCarrera;
	}

	public Integer getRfTipoComplexivo() {
		return rfTipoComplexivo;
	}

	public void setRfTipoComplexivo(Integer rfTipoComplexivo) {
		this.rfTipoComplexivo = rfTipoComplexivo;
	}

	public boolean isRfSeleccionarConvocatoria() {
		return rfSeleccionarConvocatoria;
	}

	public void setRfSeleccionarConvocatoria(boolean rfSeleccionarConvocatoria) {
		this.rfSeleccionarConvocatoria = rfSeleccionarConvocatoria;
	}

	public boolean isRfSeleccionarFacultad() {
		return rfSeleccionarFacultad;
	}

	public void setRfSeleccionarFacultad(boolean rfSeleccionarFacultad) {
		this.rfSeleccionarFacultad = rfSeleccionarFacultad;
	}

	public boolean isRfSeleccionarCarrera() {
		return rfSeleccionarCarrera;
	}

	public void setRfSeleccionarCarrera(boolean rfSeleccionarCarrera) {
		this.rfSeleccionarCarrera = rfSeleccionarCarrera;
	}

	public boolean isRfBuscar() {
		return rfBuscar;
	}

	public void setRfBuscar(boolean rfBuscar) {
		this.rfBuscar = rfBuscar;
	}

	public boolean isRfComplexivo() {
		return rfComplexivo;
	}

	public void setRfComplexivo(boolean rfComplexivo) {
		this.rfComplexivo = rfComplexivo;
	}

	public List<EstudiantePostuladoJdbcDto> getFrmRfListaCalificacionPostulantes() {
		return frmRfListaCalificacionPostulantes;
	}

	public void setFrmRfListaCalificacionPostulantes(List<EstudiantePostuladoJdbcDto> frmRfListaCalificacionPostulantes) {
		this.frmRfListaCalificacionPostulantes = frmRfListaCalificacionPostulantes;
	}

	public List<CarreraDto> getRfListCarreras() {
		return rfListCarreras;
	}

	public void setRfListCarreras(List<CarreraDto> rfListCarreras) {
		this.rfListCarreras = rfListCarreras;
	}

	public Usuario getRfUsuarioQueCambia() {
		return rfUsuarioQueCambia;
	}

	public void setRfUsuarioQueCambia(Usuario rfUsuarioQueCambia) {
		this.rfUsuarioQueCambia = rfUsuarioQueCambia;
	}

	public List<EstudiantePostuladoJdbcDto> getFrmRfListaCalificacionPostulantesOrdenada() {
		return frmRfListaCalificacionPostulantesOrdenada;
	}

	public void setFrmRfListaCalificacionPostulantesOrdenada(
			List<EstudiantePostuladoJdbcDto> frmRfListaCalificacionPostulantesOrdenada) {
		this.frmRfListaCalificacionPostulantesOrdenada = frmRfListaCalificacionPostulantesOrdenada;
	}

	public List<EstudiantePostuladoJdbcDto> getRfListMeritos() {
		return rfListMeritos;
	}

	public void setRfListMeritos(List<EstudiantePostuladoJdbcDto> rfListMeritos) {
		this.rfListMeritos = rfListMeritos;
	}

	public List<EstudianteEstadoJdbcDto> getFrmRfListaEstadosPostulantes() {
		return frmRfListaEstadosPostulantes;
	}

	public void setFrmRfListaEstadosPostulantes(List<EstudianteEstadoJdbcDto> frmRfListaEstadosPostulantes) {
		this.frmRfListaEstadosPostulantes = frmRfListaEstadosPostulantes;
	}

	public List<EstudianteEstadoJdbcDto> getFrmRfListaEstadosPostulantesVisualizar() {
		return frmRfListaEstadosPostulantesVisualizar;
	}

	public void setFrmRfListaEstadosPostulantesVisualizar(
			List<EstudianteEstadoJdbcDto> frmRfListaEstadosPostulantesVisualizar) {
		this.frmRfListaEstadosPostulantesVisualizar = frmRfListaEstadosPostulantesVisualizar;
	}

	
	
}


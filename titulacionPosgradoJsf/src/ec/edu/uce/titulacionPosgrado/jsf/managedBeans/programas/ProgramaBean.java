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
   
 ARCHIVO:     ProgramaBean.java	  
 DESCRIPCION: Bean que maneja las peticiones de programas de los usuarios. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17/12/2020				Carlos Delgado 						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.programas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProgramaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UbicacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Duracion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Modalidad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TipoFormacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TipoSede;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Titulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Vigencia;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

@ManagedBean(name = "programasForm")
@SessionScoped
public class ProgramaBean implements Serializable {
	private static final long serialVersionUID = 5869128123295204551L;

	// *****************************************************************/
	// ********************** Variables de programaForm*****************/
	// *****************************************************************/
	//Datos de la tabla carrera (programa)
	private String crrNombrePrograma;
	private String crrCodSniese;
	private Integer crrNivel;
	private String crrResolucionHcu;
	private String crrResolucionCes;
	private Integer crrEspeCodigo;
	private Integer crrTipoEvaluacion;
	private Integer crrNumActaGrado;
	private Carrera crr;
	
	//Lista Datos de la tabla configuracion_carrera
	private List<Facultad> listFacultad;
	private List<TipoFormacion> listTipoFormacion;
	private List<Modalidad> listModalidad;
	private List<TipoSede> listTipoSede;
	private List<Ubicacion> listUbicacion;
	private List<Duracion> listDuracion;
	private List<Titulo> listTitulo;
	private List<Vigencia> listVigencia;
	
	//Datos de la tabla configuracion_carrera
	private Facultad slcFacultad;
	private TipoFormacion slcTipoFormacion;
	private Modalidad slcModadlidad;
	private TipoSede slcTipoSede;
	private Ubicacion slcUbicacion;
	private Carrera slcCarrera;
	private Duracion slcDuracion;
	private Titulo slcTitulo;
	private Vigencia slcVigencia;
	private ConfiguracionCarrera cncr;
	// *****************************************************************/
	// ********************** Servicios de programaForm*****************/
	// *****************************************************************/
	@EJB private UsuarioRolServicio servUsuarioRolServicio;
	@EJB private UbicacionServicio servUbicacionServicio;
	@EJB private CarreraServicio servCarreraServicio;
	@EJB private TituloServicio servTituloServicio;
	@EJB private FacultadServicio servFacultadServicio;
	//////El siguiente servicio fue creado para traer Tipo Sede, Tipo Formacion, Modalidad y Vigencia por Programa o Carrera
	@EJB private ProgramaServicio servProgramaServicio;
	
	
	// *****************************************************************/
	// ********************* Metodos de programaForm********************/
	// *****************************************************************/
	public String irIngresarPrograma(Usuario usuario) {
		try {
			UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionActivos(usuario.getUsrIdentificacion());
			if (usro.getUsroEstado().intValue() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades("Programas.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
		} catch (UsuarioRolNoEncontradoException e) {
		} catch (UsuarioRolException e) {
		}

		return "irCrearPrograma";

	}

	public void cargarPrograma() throws Exception{
		listFacultad=servFacultadServicio.listarTodos();
		listTitulo= servTituloServicio.listarTodos();
		listUbicacion=servUbicacionServicio.listarTodos();
		listTipoSede=servProgramaServicio.ListarTodosTipoSede();
		listTipoFormacion=servProgramaServicio.ListarTodosTipoFormacion();
		listModalidad=servProgramaServicio.ListarTodosModalidad();
		listVigencia=servProgramaServicio.ListarTodosVigencia();
		listDuracion=servProgramaServicio.ListarTodosDuracion();
	}


	public String irCancelarVer(){
		return "irCancelarVer";	
	}
	
	public void guardarPrograma(){
		System.out.println("Entro a guardar programa");	
		//Seteamos Carrera
		crr.setCrrFacultad(slcFacultad);
		crr.setCrrDescripcion(crrNombrePrograma);
		crr.setCrrCodSniese(crrCodSniese);
		crr.setCrrEspeCodigo(crrEspeCodigo);
		crr.setCrrNivel(crrNivel);
		crr.setCrrTipoEvaluacion(crrTipoEvaluacion);
		
		//Seteamos Configuracion Carrera
		cncr.setCncrCarrera(crr);
		cncr.setCncrTipoFormacion(slcTipoFormacion);
		cncr.setCncrModalidad(slcModadlidad);
		cncr.setCncrTipoSede(slcTipoSede);
		cncr.setCncrUbicacion(slcUbicacion);
		cncr.setCncrDuracion(slcDuracion);
		cncr.setCncrTitulo(slcTitulo);
		cncr.setCncrVigencia(slcVigencia);
		try{
			servProgramaServicio.guardarPrograma(crr, cncr);
			FacesUtil.mensajeInfo("Autoridad creada exitosamente");
		}catch (Exception e) {
			FacesUtil.mensajeError("Autoridad no se creo");
		}
		
	}
	
	// *****************************************************************/
	// *********************** Getters and Setters**********************/
	// *****************************************************************/
	


	public String getCrrCodSniese() {
		return crrCodSniese;
	}

	public String getCrrNombrePrograma() {
		return crrNombrePrograma;
	}

	public void setCrrNombrePrograma(String crrNombrePrograma) {
		this.crrNombrePrograma = crrNombrePrograma;
	}

	public ConfiguracionCarrera getCncr() {
		return cncr;
	}

	public void setCncr(ConfiguracionCarrera cncr) {
		this.cncr = cncr;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	public Integer getCrrNivel() {
		return crrNivel;
	}

	public void setCrrNivel(Integer crrNivel) {
		this.crrNivel = crrNivel;
	}

	public String getCrrResolucionHcu() {
		return crrResolucionHcu;
	}

	public void setCrrResolucionHcu(String crrResolucionHcu) {
		this.crrResolucionHcu = crrResolucionHcu;
	}

	public String getCrrResolucionCes() {
		return crrResolucionCes;
	}

	public void setCrrResolucionCes(String crrResolucionCes) {
		this.crrResolucionCes = crrResolucionCes;
	}

	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	public Integer getCrrTipoEvaluacion() {
		return crrTipoEvaluacion;
	}

	public void setCrrTipoEvaluacion(Integer crrTipoEvaluacion) {
		this.crrTipoEvaluacion = crrTipoEvaluacion;
	}

	public Integer getCrrNumActaGrado() {
		return crrNumActaGrado;
	}

	public void setCrrNumActaGrado(Integer crrNumActaGrado) {
		this.crrNumActaGrado = crrNumActaGrado;
	}

	public Carrera getCrr() {
		return crr;
	}

	public void setCrr(Carrera crr) {
		this.crr = crr;
	}

	public List<Facultad> getListFacultad() {
		return listFacultad;
	}

	public void setListFacultad(List<Facultad> listFacultad) {
		this.listFacultad = listFacultad;
	}

	public Facultad getFacultad() {
		return slcFacultad;
	}

	public void setFacultad(Facultad slcFacultad) {
		this.slcFacultad = slcFacultad;
	}


	public List<TipoFormacion> getListTipoFormacion() {
		return listTipoFormacion;
	}

	public void setListTipoFormacion(List<TipoFormacion> listTipoFormacion) {
		this.listTipoFormacion = listTipoFormacion;
	}


	public List<TipoSede> getListTipoSede() {
		return listTipoSede;
	}

	public void setListTipoSede(List<TipoSede> listTipoSede) {
		this.listTipoSede = listTipoSede;
	}

	public List<Ubicacion> getListUbicacion() {
		return listUbicacion;
	}

	public void setListUbicacion(List<Ubicacion> listUbicacion) {
		this.listUbicacion = listUbicacion;
	}
	
	public List<Duracion> getListDuracion() {
		return listDuracion;
	}

	public void setListDuracion(List<Duracion> listDuracion) {
		this.listDuracion = listDuracion;
	}

	public List<Titulo> getListTitulo() {
		return listTitulo;
	}

	public void setListTitulo(List<Titulo> listTitulo) {
		this.listTitulo = listTitulo;
	}

	public List<Vigencia> getListVigencia() {
		return listVigencia;
	}

	public void setListVigencia(List<Vigencia> listVigencia) {
		this.listVigencia = listVigencia;
	}

	public TipoFormacion getSlcTipoFormacion() {
		return slcTipoFormacion;
	}

	public void setSlcTipoFormacion(TipoFormacion slcTipoFormacion) {
		this.slcTipoFormacion = slcTipoFormacion;
	}

	public Modalidad getSlcModadlidad() {
		return slcModadlidad;
	}

	public void setSlcModadlidad(Modalidad slcModadlidad) {
		this.slcModadlidad = slcModadlidad;
	}

	public TipoSede getSlcTipoSede() {
		return slcTipoSede;
	}

	public void setSlcTipoSede(TipoSede slcTipoSede) {
		this.slcTipoSede = slcTipoSede;
	}

	public Ubicacion getSlcUbicacion() {
		return slcUbicacion;
	}

	public void setSlcUbicacion(Ubicacion slcUbicacion) {
		this.slcUbicacion = slcUbicacion;
	}

	public Carrera getSlcCarrera() {
		return slcCarrera;
	}

	public void setSlcCarrera(Carrera slcCarrera) {
		this.slcCarrera = slcCarrera;
	}

	public Duracion getSlcDuracion() {
		return slcDuracion;
	}

	public void setSlcDuracion(Duracion slcDuracion) {
		this.slcDuracion = slcDuracion;
	}

	public Titulo getSlcTitulo() {
		return slcTitulo;
	}

	public void setSlcTitulo(Titulo slcTitulo) {
		this.slcTitulo = slcTitulo;
	}

	public Vigencia getSlcVigencia() {
		return slcVigencia;
	}

	public void setSlcVigencia(Vigencia slcVigencia) {
		this.slcVigencia = slcVigencia;
	}

	
	public List<Modalidad> getListModalidad() {
		return listModalidad;
	}

	public void setListModalidad(List<Modalidad> listModalidad) {
		this.listModalidad = listModalidad;
	}
}
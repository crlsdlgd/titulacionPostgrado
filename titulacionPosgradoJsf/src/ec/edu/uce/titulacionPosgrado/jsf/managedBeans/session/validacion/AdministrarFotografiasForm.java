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
   
 ARCHIVO:     AdministrarFotografiasForm.java	  
 DESCRIPCION: Bean de sesion que maneja la administración de las fotografías de los graduados. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 25-JUN-2019		 Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) AdministrarFotografiasForm. 
 * Bean de sesion que maneja la administración de las fotografías de los graduados.
 * @author dalbuja.
 * @version 1.0
 */
@ManagedBean(name = "administrarFotografiasForm")
@SessionScoped
public class AdministrarFotografiasForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	private String affformCedulaBuscar;
	private StreamedContent frmAeImagen;
	private byte[] frmAeBytesFoto;
	private DefaultStreamedContent image;
	private boolean affHabilitadorCarga;
	
	private List<RolFlujoCarrera> atfListCarreras;
	private UsuarioRol atfUsuarioRol;
	
	private EstudianteValidacionJdbcDto atfEstudianteBuscar;
	
	// ****************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	// ****************************************************************/
	@PostConstruct
	public void inicializar() {
	}
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB PersonaServicio servAtfPersonaServicio;
	@EJB PersonaDtoServicioJdbc servPersonaDtoServicioJdbc;
	@EJB UsuarioRolServicio servAtfUsuarioRol;
	@EJB RolFlujoCarreraServicio servAtfRolFlujoCarrera;
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de administración de tramites
	 * @return navegacion al listar de tramites
	 */
	public String iniciarAdministracionFotografias(Usuario usuario) {
		try {
			affformCedulaBuscar = null;
			//buscar el usuario rol perteneciente al usuario
			atfUsuarioRol = servAtfUsuarioRol.buscarXUsuarioXrol(usuario.getUsrId(), RolConstantes.ROL_BD_VALIDADOR_VALUE);
			
			try {
				atfListCarreras = servAtfRolFlujoCarrera.listarCarrerasXUsroId(atfUsuarioRol.getUsroId());
			} catch (RolFlujoCarreraException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RolFlujoCarreraNoEncontradoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean op=false;
			for (RolFlujoCarrera item : atfListCarreras) {
				if(item.getRoflcrCarrera().getCrrId()>=1000){
					op=true;
				}
			}
			if(!op){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Este opción solo está disponible para programas de posgrado.");
				return null;
			}

		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministrarTramites.iniciar.ususarioRol.exception")));
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministrarTramites.iniciar.ususarioRol.noEncontrado.exception")));
		}

		return "irAdministracionFotografias";
	}
	
	/**
	 * Busca tramites según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		try {
			affHabilitadorCarga=false;
			for (RolFlujoCarrera item : atfListCarreras) {
				atfEstudianteBuscar =  null;
				try {
					atfEstudianteBuscar =  new EstudianteValidacionJdbcDto();
					atfEstudianteBuscar= servPersonaDtoServicioJdbc.buscarPersonaXPrsId(affformCedulaBuscar,item.getRoflcrCarrera().getCrrId());
					if(atfEstudianteBuscar!=null){
						affHabilitadorCarga=true;
						break;	
					}
					
				} catch (Exception e) {
				}
			}
			if(affHabilitadorCarga){
				if(atfEstudianteBuscar.getPrsUbicacionFoto() != null){
					try {
						File archivo = new File(atfEstudianteBuscar.getPrsUbicacionFoto());
//						File archivo = new File("D://Descargas//archivos varios//FotoEdgarUlloa.png");
//						File archivo = new File("d:/archivos/Fotografia_1703844447.png");
						FileInputStream fis = new FileInputStream(archivo);
						ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						int nRead;
						byte[] data = new byte[16384]; 

						while ((nRead = fis.read(data, 0, data.length)) != -1) {
							buffer.write(data, 0, nRead);
						}

						buffer.flush();
						frmAeBytesFoto = buffer.toByteArray();
						InputStream isFoto = new ByteArrayInputStream(frmAeBytesFoto); 
						fis.close();
						isFoto.close();
						frmAeImagen = new DefaultStreamedContent(isFoto, "image/jpg");
					} catch (Exception e) {
						frmAeImagen = null;
						frmAeBytesFoto = null;
					}
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Por favor revise los datos cargados del sistema.");	
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontró estudiantes con la identificación ingresada para carga de fotografía.");
			}
			
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró estudiantes con la identificación ingresada para carga de fotografía.");
		}
	}
	
	public void cargarImagen(FileUploadEvent event){
		
		byte[] file = event.getFile().getContents();
		String contentType = event.getFile().getContentType();
		String extension =  event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf('.'), event.getFile().getFileName().length()); ;
//		ppfPersona.setPrsFoto(file);
		atfEstudianteBuscar.setPrsUbicacionFoto(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+atfEstudianteBuscar.getPrsIdentificacion()+extension) ;
		
		
		image = 
				new DefaultStreamedContent(
						new ByteArrayInputStream(file), 
						contentType);
		try {
			File f = new File("Fotografia_"
					+ atfEstudianteBuscar.getPrsIdentificacion() + extension);
			if(f.exists() && !f.isDirectory()) { 
			    f.delete();
			}
			FileOutputStream fos = new FileOutputStream(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"
					+ atfEstudianteBuscar.getPrsIdentificacion() + extension);
//				FileOutputStream fos = new FileOutputStream("d:/archivos/"+"Fotografia_"
//						+ atfEstudianteBuscar.getPrsIdentificacion() + extension); 
			fos.write(file);
			fos.close();
			servAtfPersonaServicio.actualizarFotoXPrsId(atfEstudianteBuscar);
			servAtfPersonaServicio.actualizarFotoXPrsIdEmisionTitulo(atfEstudianteBuscar,"Fotografia_"+atfEstudianteBuscar.getPrsIdentificacion()+extension);
			frmAeImagen = image;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Registro actualizado correctamente, recuerde que además deberá actualizar la fotografía en emisiontitulos.uce.edu.ec.");	
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ocurrió un error al cargar el archivo, intente más tarde nuevamente.");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ocurrió un error al cargar el archivo, intente más tarde nuevamente.");
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ocurrió un error al cargar el archivo, intente más tarde nuevamente.");
		}
		
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		atfEstudianteBuscar = new EstudianteValidacionJdbcDto();
		affformCedulaBuscar = null;
		frmAeBytesFoto = null;
		image = null;
		frmAeImagen=null;
		affHabilitadorCarga =false;
	}
	
	/**
	 * Método para cancelar la visualización del trámite, se limpia los parámetros para retornar a la administración de trámites
	 * @return navegacion a la administración de trámites.
	 */
	public String cancelarVerTramite(){
		limpiar();
		return "irAdministracionTramites"; 	
	}
	

	public String guardarTramite(){
		return "irAdministracionTramites";
	}

	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		atfListCarreras = null;
		frmAeImagen=null;
		atfEstudianteBuscar= new EstudianteValidacionJdbcDto();
		return "irInicio";
	}
	
	
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	

	public List<RolFlujoCarrera> getAtfListCarreras() {
		atfListCarreras = atfListCarreras==null?(new ArrayList<RolFlujoCarrera>()):atfListCarreras;
		return atfListCarreras;
	}

	public void setAtfListCarreras(List<RolFlujoCarrera> atfListCarreras) {
		this.atfListCarreras = atfListCarreras;
	}

	public EstudianteValidacionJdbcDto getAtfEstudianteBuscar() {
		return atfEstudianteBuscar;
	}

	public void setAtfEstudianteBuscar(EstudianteValidacionJdbcDto atfEstudianteBuscar) {
		this.atfEstudianteBuscar = atfEstudianteBuscar;
	}

	public String getAffformCedulaBuscar() {
		return affformCedulaBuscar;
	}

	public void setAffformCedulaBuscar(String affformCedulaBuscar) {
		this.affformCedulaBuscar = affformCedulaBuscar;
	}
	
	
	
	public void setFrmAeImagen(StreamedContent frmAeImagen) {
		this.frmAeImagen = frmAeImagen;
	}

	public StreamedContent getFrmAeImagen() {
		try {
			if(frmAeImagen == null || frmAeImagen.getStream().available() ==0){
				if(frmAeBytesFoto!=null){
					InputStream isFoto = new ByteArrayInputStream(frmAeBytesFoto);
					isFoto.close();
					frmAeImagen = new DefaultStreamedContent(isFoto, "image/jpg");
				}else{
					frmAeImagen = null;
				}
			}
		} catch (IOException e) {
		}
		return frmAeImagen;
	}

	public DefaultStreamedContent getImage() {
		return image;
	}

	public void setImage(DefaultStreamedContent image) {
		this.image = image;
	}

	public boolean isAffHabilitadorCarga() {
		return affHabilitadorCarga;
	}

	public void setAffHabilitadorCarga(boolean affHabilitadorCarga) {
		this.affHabilitadorCarga = affHabilitadorCarga;
	}
	
	
	
	
}

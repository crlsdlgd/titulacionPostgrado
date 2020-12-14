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
   
 ARCHIVO:     ImprimirActaGradoBloqueComplexivoForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones del rol Secretaria para imprimir 
 el acta de grado en bloque de los alumnos que pertenecen a la modalidad Complexivo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     		AUTOR          					COMENTARIOS
 05-JUNIO-2017		 	Freddy Guzmán Alarcón				  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.AutoridadDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEmisionActaJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PersonaDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UbicacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AsentamientoNotaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AutoridadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteActaGradoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.MecanismoTitulacionCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RegimenAcademicoConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (managed bean) ImprimirActaGradoBloqueComplexivoForm. Managed Bean que
 * maneja las peticiones del rol Validador para imprimir el acta de grado en
 * bloque de alumnos que aprobaron por modalidad complexivo.
 * 
 * @author fgguzman.
 * @version 1.0
 * @author dalbuja.
 * @version 1.1
 * Modificaciones
 * Funcionalidades de generación de actas en bloque
 * Generación de lista de carreras por usuario
 * Validación de modal
 */

@ManagedBean(name = "imprimirActaGradoBloqueComplexivoForm")
@SessionScoped
public class ImprimirActaGradoBloqueComplexivoForm implements Serializable {

	private static final long serialVersionUID = 8026853400702897855L;

	// ****************************************************************/
	// ********************* ATRIBUTOS DE LA CLASE ********************/
	// ****************************************************************/
	private Usuario iagbfUsuario;
	private Integer iagbfMes;
	private Integer iagbfBanderaImprimir;
	private Integer iagbfValidador;
	private CarreraDto iagfCarrera;
	private List<CarreraDto> iagfListaCarrera;
	private List<EstudianteEmisionActaJdbcDto> iagbfListEstudianteActaGradoJdbcDto;

	// ****************************************************************/
	// ********************** SERVICIOS GENERALES *********************/
	// ****************************************************************/

	@EJB
	private UsuarioRolServicio servicoUsuarioRol;
	@EJB
	private AsentamientoNotaDtoServicioJdbc servicioJdbcAsentamientoNotaDto;
	@EJB 
	private RolFlujoCarreraServicio servicioRolFlujoCarrera;
	@EJB
	private EstudianteActaGradoDtoServicioJdbc servicioJdbcEstudianteActaGradoDto;
	@EJB
	private FichaEstudianteServicio servicioFichaEstudiante;
	@EJB
	private AutoridadDtoServicioJdbc servicioJdbcAutoridadDto;
	@EJB
	private PersonaDtoServicioJdbc servicioJdbcPersonaDto;
	@EJB 
	private EstudianteValidacionDtoServicioJdbc servIagfEstudianteValidacionDtoServicioJdbc;
	@EJB 
	private ConfiguracionCarreraServicio servIagfConfiguracionCarreraServicio;
	@EJB 
	private UbicacionServicio servIagfUbicacionServicio;
	@EJB
	private MecanismoTitulacionCarreraDtoServicioJdbc servMecanismoTitulacionCarreraDtoServicioJdbc;
	// ****************************************************************/
	// ************** METODO INICIALIZACION DE VARIABLES **************/
	// ****************************************************************/
	@PostConstruct
	public void inicializar() {
	}

	public void iniciarBloqueComplexivoForm() {
		limpiarBloqueComplexivoForm();
	}

	// ****************************************************************/
	// ******************** METODOS NAVEGACION ************************/
	// ****************************************************************/
	
	/**
	 * Método que dirige a la página de Impresión de actas de grado en bloque
	 * @param usuario
	 * @return
	 */
	public String irBloqueComplexivo(Usuario usuario) {
		iagbfUsuario = usuario;
		iagbfValidador=0;
		try {
			UsuarioRol usro = servicoUsuarioRol.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if (usro.getUsroEstado().intValue() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE) {
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarBloqueComplexivoForm();
			iagfListaCarrera=servIagfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.no.result.exception")));
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.exception")));
		} catch (CarreraDtoJdbcNoEncontradoException e) {
		} catch (SQLException e) {
		}
		return "irBloqueComplexivo";
	}

	/**
	 * Método que dirige a la página principal
	 * @param 
	 * @return
	 */
	public String irMenuPrincipal() {
		iagbfListEstudianteActaGradoJdbcDto = null;
		iagfListaCarrera=null;
		iagfCarrera=null;
		return "irMenuPrincipal";
	}
	
	/**
	 * Método que dirige a la página principal
	 * @param 
	 * @return
	 */
	public String irMenuPrincipalImpresion() {
		for (EstudianteEmisionActaJdbcDto item : iagbfListEstudianteActaGradoJdbcDto) {
			actualizarEstadoTramitetitulo(item);
		}
		iagbfListEstudianteActaGradoJdbcDto = null;
		iagfListaCarrera=null;
		iagfCarrera=null;
		return "irMenuPrincipal";
	}
	
	
	/**
	 * Método que genera las actas de grado y las guarda en la sesión.
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unused")
	public void irModalActaGradoBloqueComplexivo(){
		 JRPdfExporter PdfExporter = new JRPdfExporter ();
		 List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>() ;
		 String archivoJasper = null;
		for (EstudianteEmisionActaJdbcDto item : iagbfListEstudianteActaGradoJdbcDto) {

			List<Map<String, Object>> frmCrpCampos = new ArrayList<Map<String,Object>>();
			Map<String, Object> frmCrpParametros =  new HashMap<String, Object>();
			frmCrpParametros.put("usuario",iagbfUsuario.getUsrNick());
			String frmCrpNombreReporte = null;
			JasperReport jasperReport = null;
			JasperPrint jasperPrint;
			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance()
					.getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/titulacion/reportes/archivosJasper/actasGrado");
			StringBuilder sb = new StringBuilder();
			sb.append("En tal virtud, la <style isBold=\"true\">");
			switch (item.getFclId()) {
			case 18:
				frmCrpParametros.put("tipoSede", "Sede:");
				frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+item.getPrsPrimerApellido()+"_"+item.getPrsSegundoApellido()+"_"+item.getPrsNombres();
				frmCrpParametros.put("facultad",item.getFclDescripcion());
				sb.append(item.getFclDescripcion());
				archivoJasper="ActaDeGradoComplexivoSedes";
				break;
			case 19:
				frmCrpParametros.put("tipoSede", "Sede:");
				frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+item.getPrsPrimerApellido()+"_"+item.getPrsSegundoApellido()+"_"+item.getPrsNombres();
				frmCrpParametros.put("facultad",item.getFclDescripcion());
				sb.append(item.getFclDescripcion());
				archivoJasper="ActaDeGradoComplexivoSedes";
				break;
			case 20:
				frmCrpParametros.put("tipoSede", "Sede:");
				frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+item.getPrsPrimerApellido()+"_"+item.getPrsSegundoApellido()+"_"+item.getPrsNombres();
				frmCrpParametros.put("facultad",item.getFclDescripcion());
				sb.append(item.getFclDescripcion());
				archivoJasper="ActaDeGradoComplexivoSedes";
				break;
			default:
				frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+item.getPrsPrimerApellido()+"_"+item.getPrsSegundoApellido()+"_"+item.getPrsNombres();
				frmCrpParametros.put("tipoSede", "Facultad:");
				frmCrpParametros.put("facultad",item.getFclDescripcion());
				sb.append("FACULTAD DE ");
				sb.append(item.getFclDescripcion());
				archivoJasper="ActaDeGradoComplexivo";
				break;
			}
			sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style>, en su nombre y por autoridad de la ley,");
			frmCrpParametros.put("facultadCadena",sb.toString());
			SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
			String fecha=formateador.format(item.getFcesFechaActaGrado());
			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			dato = new HashMap<String, Object>();
			frmCrpParametros.put("carrera", item.getCrrDescripcion());
			frmCrpParametros.put("modalidad", item.getMdlDescripcion());
			frmCrpParametros.put("folio", item.getFcesNumActaGrado());
			frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
			frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
			//texto que requiere construir de acuerdo a los datos de postulante
			StringBuilder texto = new StringBuilder(); 
			texto.append("En ");
			try {
				ConfiguracionCarrera cncrAux = servIagfConfiguracionCarreraServicio.buscarPorId(item.getFcesCncrId());
				Ubicacion ubc = servIagfUbicacionServicio.buscarPorId(cncrAux.getCncrUbicacion().getUbcId());
				if(ubc.getUbcDescripcion().equals("QUITO")){
					texto.append("Quito D.M.,");	
				}else{
					texto.append(ubc.getUbcDescripcion());
				}
			} catch (ConfiguracionCarreraNoEncontradoException
					| ConfiguracionCarreraException e2) {
			} catch (UbicacionNoEncontradoException e) {
			} catch (UbicacionException e) {
			}
			texto.append(" hoy ");texto.append(fecha);
			texto.append(" a las ");texto.append(item.getFcesHoraActaGrado().replace("H", ":"));texto.append("h");
			texto.append(", se procedió a receptar el Examen Complexivo, como mecanismo de titulación ");
			if(item.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
				texto.append( "del señor ");
			}else if(item.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
				texto.append( "de la señorita ");
			}else{
				texto.append( "de ");
			}
			texto.append(item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
			texto.append(" con identificación N.° ");texto.append(item.getPrsIdentificacion());
			try {
				Ubicacion ubcAux = servIagfUbicacionServicio.buscarPorId(item.getPrsUbcId());
				texto.append(" de nacionalidad ");
				texto.append(ubcAux.getUbcGentilicio());
			} catch (UbicacionNoEncontradoException | UbicacionException e2) {
			}
			texto.append(" de conformidad con el marco legal vigente del Reglamento de Régimen Académico expedido por el Consejo de Educación Superior el ");
			texto.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(RegimenAcademicoConstantes.DIA_REGIMEN_ACADEMICO,false));
			texto.append(" de ");texto.append(GeneralesUtilidades.tranformaNumeroEnMesesMinusculas(RegimenAcademicoConstantes.MES_REGIMEN_ACADEMICO));texto.append(" de ");
			texto.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(RegimenAcademicoConstantes.ANIO_REGIMEN_ACADEMICO,false));
			texto.append(" para la aplicación del examen complexivo, habiendo obtenido las calificaciones que a continuación se detallan:");
			frmCrpParametros.put("texto", texto.toString());
			frmCrpParametros.put("fecha_actual", fecha);
			frmCrpParametros.put("postulante", item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
			// Nota record Académico 
			StringBuilder recordAcademico = new StringBuilder();
			String nota=item.getFcesNotaPromAcumulado().toString();
			String[] partesNotas =  nota.split("\\.");
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
			recordAcademico.append(" PUNTO ");
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) recordAcademico.append("CERO ");
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
			frmCrpParametros.put("recordAcademico",recordAcademico.toString());
			// Nota trabajo titulación
			StringBuilder trabajoTitulacion = new StringBuilder();
			String notaTitulacion=item.getFcesNotaTrabTitulacion().toString();
			partesNotas = notaTitulacion.split("\\.");
			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
			trabajoTitulacion.append(" PUNTO ");
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) trabajoTitulacion.append("CERO ");
			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
			frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
			// Nota promedio
			StringBuilder promedioFinal = new StringBuilder();
			// Solo para las carreras de Medicina y Odontología se permite las notas con decimales
			BigDecimal notaFinal = new BigDecimal(0.0);
//			if(item.getCrrId()==CarreraConstantes.CARRERA_MEDICINA_VALUE
//					|| item.getCrrId()==CarreraConstantes.CARRERA_ODONTOLOGIA_VALUE
//					|| item.getCrrId()==CarreraConstantes.CARRERA_OBSTETRICIA_VALUE
//					|| item.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_VALUE
//					|| item.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_VALUE
//					|| item.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_VALUE){
				notaFinal=(item.getFcesNotaPromAcumulado().add(item.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 2,RoundingMode.UP);
				partesNotas = notaFinal.toString().split("\\.");
				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0].toString(),true));
				promedioFinal.append(" PUNTO ");
				if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) promedioFinal.append("CERO ");
				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1].toString(),true));
				frmCrpParametros.put("promedioFinal",promedioFinal.toString());
				frmCrpParametros.put("notaPromedio",notaFinal.toString());
				try {
					servicioFichaEstudiante.guardarNotaFinalGraduacion(item.getFcesId(), notaFinal, item.getTrttId());
				} catch (FichaEstudianteNoEncontradoException e1) {
				} catch (FichaEstudianteException e1) {
					FacesUtil.mensajeError(e1.getMessage());
				}
//			}else{
//				notaFinal=(item.getFcesNotaPromAcumulado().add(item.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 0, RoundingMode.HALF_UP);
//				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
//				promedioFinal.append(" PUNTO CERO");
//				frmCrpParametros.put("promedioFinal",promedioFinal.toString());
//				frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
//				try {
//					servicioFichaEstudiante.guardarNotaFinalGraduacion(item.getFcesId(), notaFinal,item.getTrttId());
//				} catch (FichaEstudianteNoEncontradoException e1) {
//				} catch (FichaEstudianteException e1) {
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError(e1.getMessage());
//				}
//			}
			frmCrpParametros.put("notaRecord",nota);
			frmCrpParametros.put("notaTrabajo",notaTitulacion);
			int res= notaFinal.compareTo(new BigDecimal(19.50));
			String resultado=null;
			if(res==0 || res==1){
				resultado="EXCELENTE";
			}else {
				int res1= notaFinal.compareTo(new BigDecimal(17.50));
				if(res1==1 || res1==0){
					resultado="MUY BUENO";
				}else{
					int res2= notaFinal.compareTo(new BigDecimal(15.50));
					if(res2==1 || res2==0){
						resultado="BUENO";
					}else {
						int res3= notaFinal.compareTo(new BigDecimal(14.00));
						if(res3==1 || res3==0){
							resultado="REGULAR";
						}else{
							resultado="DEFICIENTE";
						}
					}
				}
			}
			try {
				List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
				listaAutoridades = servicioJdbcAutoridadDto.buscarAutoridades(item.getCrrId());
				StringBuilder autoridadDecano = new StringBuilder();
				StringBuilder autoridadSecretario = new StringBuilder();
				if(item.getFcesDecano().equals(GeneralesConstantes.APP_ID_BASE.toString())){
					PersonaDto director = servicioJdbcPersonaDto.buscarEvaluadorXCarrera(item.getCrrId());
					autoridadDecano.append(listaAutoridades.get(0).getAtrNombres());autoridadDecano.append(" ");
					autoridadDecano.append(listaAutoridades.get(0).getAtrPrimerApellido());autoridadDecano.append(" ");
					if(!listaAutoridades.get(0).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
						autoridadDecano.append(listaAutoridades.get(0).getAtrSegundoApellido());
					}
					autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
					autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
					if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
						autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
					}
					MecanismoTitulacionCarreraDto mcttcrAux = new MecanismoTitulacionCarreraDto();
					mcttcrAux = servMecanismoTitulacionCarreraDtoServicioJdbc.buscarXCarreraMecanismoExamenComplexivo(iagfCarrera.getCrrId());
					switch (item.getFclId()) {
					case 18:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						
						frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
							frmCrpParametros.put("sexoDecano","Decana");
							if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretario Abogado");
							}else{
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
							}
							
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA, 1, 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
							if(director.getPrsSegundoApellido()!=null){
								servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(), director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
							}else{
								servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(), item.getFcesDirectorSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
							}
							
						break;
					case 19:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
							frmCrpParametros.put("sexoDecano","Decana");
							if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretario Abogado");
							}else{
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
							}
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA, 1, 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
							if(director.getPrsSegundoApellido()!=null){
								servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
							}else{
								servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(), item.getFcesDirectorSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
							}
						break;
					case 20:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
							frmCrpParametros.put("sexoDecano","Decana");
							if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretario Abogado");
							}else{
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
							}
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA, 1, 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
							if(director.getPrsSegundoApellido()!=null){
								servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
							}else{
								servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(), item.getFcesDirectorSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
							}
						break;
					default:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						frmCrpParametros.put("decano",autoridadDecano.toString());
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
						if(listaAutoridades.get(0).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDecano","Decano");
						}else{
							frmCrpParametros.put("sexoDecano","Decana");
						}
						if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretario Abogado");
						}else{
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
						}
						servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), autoridadDecano.toString(), listaAutoridades.get(0).getAtrSexo(), 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
						servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
						if(director.getPrsSegundoApellido()!=null){
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
						}else{
							servicioJdbcEstudianteActaGradoDto.actualizarAutoridadesComplexivo(item.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(), item.getFcesDirectorSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
						}
						break;
					}
				}else{
					autoridadDecano.append(item.getFcesDecano());
					autoridadSecretario.append(item.getFcesSecretario());
						frmCrpParametros.put("director",item.getFcesDirector());	
						
						frmCrpParametros.put("decano",autoridadDecano.toString());
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(item.getFcesDirectorSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
						if(item.getFcesDecanoSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDecano","Decano");
						}else{
							frmCrpParametros.put("sexoDecano","Decana");
						}
						if(item.getFcesSecretarioSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretario Abogado");
						}else{
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
						}
						break;
				}
				
				
			} catch (PersonaDtoJdbcException
					| PersonaDtoJdbcNoEncontradoException e) {
			} catch (AutoridadNoEncontradoException e) {
			} catch (AutoridadException e) {
			}catch (Exception e) {
			}
			frmCrpParametros.put("equivalencia",resultado);
			frmCrpParametros.put("titulo",item.getTtlDescripcion());
			frmCrpCampos.add(dato);
			StringBuilder pathDeReporte = new StringBuilder();
			pathDeReporte.append(pathGeneralReportes);
			pathDeReporte.append("/");
			pathDeReporte.append(archivoJasper);
			pathDeReporte.append(".jasper");
			JRDataSource dataSource = new JRBeanCollectionDataSource(frmCrpCampos);
			try {
				jasperReport = (JasperReport)JRLoader.loadObject(new File(pathDeReporte.toString()));
				JasperPrint printedReport = JasperFillManager.fillReport(jasperReport, frmCrpParametros, dataSource);
				jasperPrintList.add(printedReport);
			} catch (JRException e) {
			}
		}
		int contadorArchivos=0;
		for (JasperPrint jasperPrint : jasperPrintList) {
			contadorArchivos++;
			jasperPrint.setName(archivoJasper+contadorArchivos);
		}
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmJasperList", jasperPrintList);
		httpSession.setAttribute("frmCargaNombreReporte", archivoJasper);
		iagbfValidador=1;
	}
	
	// ****************************************************************/
	// ********************* METODOS GENERALES ************************/
	// ****************************************************************/

//	public void controlBanderaModal(){
//	}
	
//	public void imprimirActaGradoBloqueComplexivo(){
//		
//		
//	}
	
	public void actualizarEstadoTramitetitulo(EstudianteEmisionActaJdbcDto entidad) {
		RolFlujoCarrera roflcrr;
		try {
				roflcrr = servicioRolFlujoCarrera.buscarPorCarrera(entidad.getTrttCarreraId(),iagbfUsuario.getUsrId(), RolConstantes.ROL_BD_VALIDADOR_VALUE);
				entidad.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				servicioJdbcEstudianteActaGradoDto.modificarEstadoActaGradoFinal(entidad, roflcrr);
		} catch (RolFlujoCarreraNoEncontradoException
				| RolFlujoCarreraException e) {
		}catch (EstudianteActaGradoNoEncontradoException
				| EstudianteActaGradoException e) {
		}		
	}

	public void buscarActasComplexivo() {
		if(iagfCarrera.getCrrId()==GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor seleccione una carrera para continuar.");
		}else{
			try {
				iagbfListEstudianteActaGradoJdbcDto = servicioJdbcAsentamientoNotaDto.buscarActaBloqueComplexivo(iagfCarrera.getCrrId(),iagbfMes.intValue());
				iagbfMes = GeneralesConstantes.APP_ID_BASE;
				
				if (iagbfListEstudianteActaGradoJdbcDto.size() > Integer.valueOf(0)) {
					iagbfBanderaImprimir = Integer.valueOf(0);
				}else {
					limpiarBloqueComplexivoForm();
					FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
				}
			} catch (AsentamientoNotaNoEncontradoException e) {
			} catch (AsentamientoNotaException e) {
			} 	
		}
	}
	
	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarCarrera(){
		limpiarBloqueComplexivoForm();
		if(iagfCarrera.getCrrId()==GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor seleccione una carrera para continuar.");
		}
	}
	
	public void verificarClickAceptarTramiteNo(){
		iagbfValidador=0;
	}
	
	public void limpiarBloqueComplexivoForm(){
		iagfCarrera= new CarreraDto();
		iagbfMes = GeneralesConstantes.APP_ID_BASE;
		iagbfBanderaImprimir = Integer.valueOf(1);
		iagbfListEstudianteActaGradoJdbcDto = new ArrayList<EstudianteEmisionActaJdbcDto>();
	}

	// ******************************************************************/
	// ******************* METODOS GETTER Y SETTER **********************/
	// ******************************************************************/

	public Usuario getIagbfUsuario() {
		return iagbfUsuario;
	}

	public void setIagbfUsuario(Usuario iagbfUsuario) {
		this.iagbfUsuario = iagbfUsuario;
	}


	public List<EstudianteEmisionActaJdbcDto> getIagbfListEstudianteActaGradoJdbcDto() {
		iagbfListEstudianteActaGradoJdbcDto = iagbfListEstudianteActaGradoJdbcDto == null ? (new ArrayList<EstudianteEmisionActaJdbcDto>())	: iagbfListEstudianteActaGradoJdbcDto;
		return iagbfListEstudianteActaGradoJdbcDto;
	}

	public void setIagbfListEstudianteActaGradoJdbcDto(
			List<EstudianteEmisionActaJdbcDto> iagbfListEstudianteActaGradoJdbcDto) {
		this.iagbfListEstudianteActaGradoJdbcDto = iagbfListEstudianteActaGradoJdbcDto;
	}

	public Integer getIagbfMes() {
		return iagbfMes;
	}

	public void setIagbfMes(Integer iagbfMes) {
		this.iagbfMes = iagbfMes;
	}

	public Integer getIagbfBanderaImprimir() {
		return iagbfBanderaImprimir;
	}

	public void setIagbfBanderaImprimir(Integer iagbfBanderaImprimir) {
		this.iagbfBanderaImprimir = iagbfBanderaImprimir;
	}

	public Integer getIagbfValidador() {
		return iagbfValidador;
	}

	public void setIagbfValidador(Integer iagbfValidador) {
		this.iagbfValidador = iagbfValidador;
	}

	public CarreraDto getIagfCarrera() {
		return iagfCarrera;
	}

	public void setIagfCarrera(CarreraDto iagfCarrera) {
		this.iagfCarrera = iagfCarrera;
	}

	public List<CarreraDto> getIagfListaCarrera() {
		iagfListaCarrera = iagfListaCarrera == null ? (new ArrayList<CarreraDto>())	: iagfListaCarrera;
		return iagfListaCarrera;
	}

	public void setIagfListaCarrera(List<CarreraDto> iagfListaCarrera) {
		this.iagfListaCarrera = iagfListaCarrera;
	}

	
	

}
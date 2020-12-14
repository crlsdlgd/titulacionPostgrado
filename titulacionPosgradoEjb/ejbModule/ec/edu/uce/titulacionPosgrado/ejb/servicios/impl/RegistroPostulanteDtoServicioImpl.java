/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     RegistroEstudianteDtoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre el dto Registro estudiante. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 21/02/2018					Daniel Albuja                    Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.RegistroPostulanteDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RegistroPostulanteDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Rol;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;


/**
 * Clase (Bean)RegistroPostulanteDtoServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class RegistroPostulanteDtoServicioImpl implements RegistroPostulanteDtoServicio{
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return boolean - TRUE si se registró exitosamente, FALSE caso contrario
	 * @throws RegistroPostulanteDtoValidacionException - lanzada cuando la validación de la entidad RegistroPostulanteDto falla.
	 * @throws RegistroPostulanteDtoException - Excepcion general.
	 */
	@Override
	public boolean anadir(RegistroPostulanteDto entidad) throws RegistroPostulanteDtoValidacionException,RegistroPostulanteDtoException{
		boolean retorno = false;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		if (entidad != null) {
			try {
			//*************************************************************
			//********************* VALIDACIONES **************************
			//*************************************************************
				Persona persona = new Persona();
				persona.setPrsIdentificacion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsIdentificacion()).toUpperCase());
				persona.setPrsPrimerApellido(GeneralesUtilidades.eliminarTildes(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsPrimerApellido()).toUpperCase()));
				persona.setPrsMailPersonal(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsMailPersonal()));
				if(persona.getPrsPrimerApellido().length()==0){
					throw new RegistroPostulanteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.general.apellido.null.exception")));
				}
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.identificacion.persona.validacion.exception")));
				}
				//validacion de constraint de mail
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.mail.persona.validacion.exception")));
				}	
					
					StringBuilder sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" SELECT ");
					sbSqlPersona.append(" max(");sbSqlPersona.append(JdbcConstantes.PRS_ID);
					sbSqlPersona.append(") AS id FROM ");
					sbSqlPersona.append(JdbcConstantes.TABLA_PERSONA);
					con = ds.getConnection();
					
					pstmt = con.prepareStatement(sbSqlPersona.toString());
					
					rs = pstmt.executeQuery();
					Integer prsId=0;
					while (rs.next()){
						prsId=rs.getInt("id");
					}
					if(rs!=null){
						rs.close();
					}
					sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO PERSONA (prs_id, prs_tipo_identificacion,prs_tipo_identificacion_sniese,"
							+ "prs_identificacion,prs_nombres, prs_primer_apellido, prs_segundo_apellido, prs_mail_personal,prs_telefono,"
							+ "prs_fecha_nacimiento,prs_sexo,prs_sexo_sniese,etn_id,ubc_id"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ? , ? , ? , ? , ? , ?, ? , ? , ? , ?, ?, ?, ? )");
					pstmt2 = con.prepareStatement(sbSqlPersona.toString());
					pstmt2.setInt(1, prsId+1);
					pstmt2.setInt(2, entidad.getPrsTipoIdentificacion());
					pstmt2.setInt(3, entidad.getPrsTipoIdentificacion()+1);
					pstmt2.setString(4, entidad.getPrsIdentificacion());
					pstmt2.setString(5, GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsNombres()).toUpperCase());
					pstmt2.setString(6, GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsPrimerApellido()).toUpperCase());
					pstmt2.setString(7, GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsSegundoApellido()).toUpperCase());
					pstmt2.setString(8, entidad.getPrsMailPersonal());
					
					pstmt2.setString(9, entidad.getPrsTelefono());
					pstmt2.setDate(10, new java.sql.Date(entidad.getPrsFechaNacimiento().getTime()));
					pstmt2.setInt(11, entidad.getPrsSexo());
					pstmt2.setInt(12, entidad.getPrsSexo()+1);
					pstmt2.setInt(13, entidad.getPrsEtnia());
					pstmt2.setInt(14, entidad.getPrsUbcNacionalidad());
					
					pstmt2.executeUpdate();
					if (pstmt2 != null){
						pstmt2.close();
					}
					
					//creacion del objeto usuario
					Usuario usuario = new Usuario();
					usuario.setUsrIdentificacion(persona.getPrsIdentificacion());
					usuario.setUsrNick(usuario.getUsrIdentificacion());
					
					//validacion de constraint de identificacion
					if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
						throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.identificacion.usuario.validacion.exception")));
					}
					//validacion de constraint de nick
					if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
						throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.nick.usuario.validacion.exception")));
					}
					Integer usrId=0;
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" max(");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(") AS id FROM ");
					sbSql.append(JdbcConstantes.TABLA_USUARIO);
					//FIN QUERY
					pstmt.close();
					
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs1 = pstmt.executeQuery();
					
					while (rs1.next()){
						usrId=rs1.getInt("id");
					}
					if(rs1!=null){
						rs1.close();
					}
					sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO USUARIO (usr_id, usr_identificacion,usr_nick,"
							+ "usr_password,usr_fecha_creacion, usr_fecha_caducidad, usr_fecha_cad_pass, usr_estado,usr_est_sesion,"
							+ "usr_active_directory,prs_id"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ? , ? , ? , ? , ? , ?, ? , ? , ? , ? )");
					pstmt1 = con.prepareStatement(sbSqlPersona.toString());
					
					
					pstmt1.setInt(1, usrId+1);
					pstmt1.setString(2, entidad.getPrsIdentificacion());
					pstmt1.setString(3, entidad.getPrsIdentificacion());
					pstmt1.setString(4, entidad.getUsrPassword());
					pstmt1.setDate(5, new java.sql.Date(entidad.getUsrFechaCreacion().getTime()));
					pstmt1.setDate(6, new java.sql.Date(entidad.getUsrFechaCaducidad().getTime()));
					pstmt1.setDate(7, new java.sql.Date(entidad.getUsrFechaCadPass().getTime()));
					pstmt1.setInt(8, entidad.getUsrEstado());
					pstmt1.setInt(9, UsuarioConstantes.ESTADO_SESSION_SI_LOGGEADO_VALUE);
					pstmt1.setInt(10, entidad.getUsrActiveDirectory());
					pstmt1.setInt(11, prsId+1);
					
					pstmt1.executeUpdate();
					if (pstmt1 != null){
						pstmt1.close();
					}
					Integer usroId=0;
					sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" max(");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(") AS id FROM ");
					sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
					//FIN QUERY
					pstmt.close();
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs2 = pstmt.executeQuery();
					
					while (rs2.next()){
						usroId=rs2.getInt("id");
					}
					if(rs2!=null){
						rs2.close();
					}
					sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO USUARIO_ROL (usro_id, usr_id,usro_estado,rol_id"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ?, ? , ? )");
					pstmt3 = con.prepareStatement(sbSqlPersona.toString());
					pstmt3.setInt(1, usroId+1);
					pstmt3.setInt(2, usrId+1);
					pstmt3.setInt(3, UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
					pstmt3.setInt(4, RolConstantes.ROL_BD_POSTULANTE_VALUE);
					pstmt3.executeUpdate();
					if (pstmt3 != null){
						pstmt3.close();
					}
					
				
//					
//						//**********************************************************************
//						//********** Creo Nueva Pesona  ****************
//						//**********************************************************************
//						Persona nuevaPrs = new Persona();
//						nuevaPrs = em.find(Persona.class, prsId+1);
//										
//						//**********************************************************************
//						//********** Creo Nueva Ficha Docente            ****************
//						//**********************************************************************
//						
//						
//						Integer fcdcId=0;
//						try {
//							StringBuilder sbSql = new StringBuilder();
//							sbSql.append(" SELECT ");
//							sbSql.append(" max(");sbSql.append(JdbcConstantes.FCDC_ID);
//							sbSql.append(") AS id FROM ");
//							sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);
//							
//							//FIN QUERY
//							con = ds.getConnection();
//							
//							pstmt = con.prepareStatement(sbSql.toString());
//							
//							rs = pstmt.executeQuery();
//							
//							while (rs.next()){
//								fcdcId=rs.getInt("id");
//							}
//							sbSql = new StringBuilder();
//							sbSql.append(" INSERT INTO FICHA_DOCENTE (fcdc_id, fcdc_estado,crr_id, prs_id) VALUES( ");
//							sbSql.append(fcdcId+1);sbSql.append(",");
//							sbSql.append(FichaDocenteConstantes.ACTIVO_VALUE);sbSql.append(",");
//							sbSql.append(carreraAux.getCrrId());sbSql.append(",");
//							sbSql.append(nuevaPrs.getPrsId());sbSql.append(")");
//							PreparedStatement pstmt1 = null;
//							pstmt1 = con.prepareStatement(sbSql.toString());
//							pstmt1.executeUpdate();
//							if (pstmt1 != null){
//								pstmt1.close();
//							}
//						} catch (SQLException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						} finally {
//							try {
//								if( rs != null){
//									rs.close();
//								}
//								if (pstmt != null){
//									pstmt.close();
//								}
//								if (con != null) {
//									con.close();
//								}
//							} catch (SQLException e) {
//							}
//						}
//				
//
//			
//			
//			
//			
//			//creacion del objeto usuario_rol
//			UsuarioRol usuarioRol = new UsuarioRol();
//			usuarioRol.setUsroEstado(entidad.getUsroEstado());
//			usuarioRol.setUsroRol(new Rol(entidad.getUsroRol()));
//			
//			
//			//*************************************************************
//			//************************** INSERCIONES **********************
//			//*************************************************************
//			//*********      Inserción de persona
//			em.persist(persona);
//			em.flush();
//			
//			//*********      Inserción de usuario
//			//asignacion de la persona en el usuario
//			usuario.setUsrPersona(persona);
//			em.persist(usuario);
//			em.flush();
//			
//			//*********      Inserción de usuariorol
//			//asignacion del usuario al usuario_rol
//			usuarioRol.setUsroUsuario(usuario);
//			em.persist(usuarioRol);
//			em.flush();
//			
			retorno = true;
			
			} catch (PersonaValidacionException e) {
				try {
					
					Persona persona = verificarPersonaDocente(entidad.getPrsIdentificacion());
					persona.setPrsTipoIdentificacion(entidad.getPrsTipoIdentificacion());
					persona.setPrsTipoIdentificacionSniese( entidad.getPrsTipoIdentificacion()+1);
					persona.setPrsNombres(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsNombres()).toUpperCase());
					persona.setPrsPrimerApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsPrimerApellido()).toUpperCase());
					persona.setPrsSegundoApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsSegundoApellido()).toUpperCase());
					persona.setPrsMailPersonal(entidad.getPrsMailPersonal());
					
					persona.setPrsTelefono(entidad.getPrsTelefono());
					persona.setPrsFechaNacimiento(new java.sql.Date(entidad.getPrsFechaNacimiento().getTime()));
					persona.setPrsSexo(entidad.getPrsSexo());
					persona.setPrsSexoSniese(entidad.getPrsSexo()+1);
					Etnia etn = em.find(Etnia.class, entidad.getPrsEtnia());
					persona.setPrsEtnia(etn);
					Ubicacion ubc = em.find(Ubicacion.class, entidad.getPrsUbcNacionalidad());
					persona.setPrsUbicacionNacionalidad(ubc);
					em.merge(persona);
					em.flush();
					
					//creacion del objeto usuario
					Usuario usuario = new Usuario();
					usuario.setUsrIdentificacion(persona.getPrsIdentificacion());
					usuario.setUsrNick(usuario.getUsrIdentificacion());
					
					//validacion de constraint de identificacion
					if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
						throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.identificacion.usuario.validacion.exception")));
					}
					//validacion de constraint de nick
					if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
						throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.nick.usuario.validacion.exception")));
					}
					Integer usrId=0;
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" max(");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(") AS id FROM ");
					sbSql.append(JdbcConstantes.TABLA_USUARIO);
					//FIN QUERY
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs1 = pstmt.executeQuery();
					
					while (rs1.next()){
						usrId=rs1.getInt("id");
					}
					if(rs1!=null){
						rs1.close();
					}
					StringBuilder sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO USUARIO (usr_id, usr_identificacion,usr_nick,"
							+ "usr_password,usr_fecha_creacion, usr_fecha_caducidad, usr_fecha_cad_pass, usr_estado,usr_est_sesion,"
							+ "usr_active_directory,prs_id"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ? , ? , ? , ? , ? , ?, ? , ? , ? , ? )");
					pstmt1 = con.prepareStatement(sbSqlPersona.toString());
					
					
					pstmt1.setInt(1, usrId+1);
					pstmt1.setString(2, entidad.getPrsIdentificacion());
					pstmt1.setString(3, entidad.getPrsIdentificacion());
					pstmt1.setString(4, entidad.getUsrPassword());
					pstmt1.setDate(5, new java.sql.Date(entidad.getUsrFechaCreacion().getTime()));
					pstmt1.setDate(6, new java.sql.Date(entidad.getUsrFechaCaducidad().getTime()));
					pstmt1.setDate(7, new java.sql.Date(entidad.getUsrFechaCadPass().getTime()));
					pstmt1.setInt(8, entidad.getUsrEstado());
					pstmt1.setInt(9, UsuarioConstantes.ESTADO_SESSION_SI_LOGGEADO_VALUE);
					pstmt1.setInt(10, entidad.getUsrActiveDirectory());
					pstmt1.setInt(11, persona.getPrsId());
					
					pstmt1.executeUpdate();
					if (pstmt1 != null){
						pstmt1.close();
					}
					Integer usroId=0;
					sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" max(");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(") AS id FROM ");
					sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
					//FIN QUERY
					pstmt.close();
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs2 = pstmt.executeQuery();
					
					while (rs2.next()){
						usroId=rs2.getInt("id");
					}
					if(rs2!=null){
						rs2.close();
					}
					sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO USUARIO_ROL (usro_id, usr_id,usro_estado,rol_id"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ?, ? , ? )");
					pstmt3 = con.prepareStatement(sbSqlPersona.toString());
					pstmt3.setInt(1, usroId+1);
					pstmt3.setInt(2, usrId+1);
					pstmt3.setInt(3, UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
					pstmt3.setInt(4, RolConstantes.ROL_BD_POSTULANTE_VALUE);
					pstmt3.executeUpdate();
					if (pstmt3 != null){
						pstmt3.close();
					}
				} catch (Exception e2) {
					throw new RegistroPostulanteDtoValidacionException(e.getMessage());
				}
				
				
				
			} catch (UsuarioValidacionException e) {
				throw new RegistroPostulanteDtoValidacionException(e.getMessage());
			} catch (Exception e) {
				throw new RegistroPostulanteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.general.exception")));
			}finally {
				try {
					if( rs != null){
						rs.close();
					}
					if( rs1 != null){
						rs1.close();
					}
					if( rs2 != null){
						rs2.close();
					}
					if (pstmt != null){
						pstmt.close();
					}
					if (pstmt1 != null){
						pstmt1.close();
					}
					if (pstmt2 != null){
						pstmt2.close();
					}
					if (pstmt3 != null){
						pstmt3.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (Exception e2) {
				}
				
			}
			
		}
		return retorno;
	}
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return boolean - TRUE si se registró exitosamente, FALSE caso contrario
	 * @throws RegistroPostulanteDtoValidacionException - lanzada cuando la validación de la entidad RegistroPostulanteDto falla.
	 * @throws RegistroPostulanteDtoException - Excepcion general.
	 */
	@Override
	public boolean anadirUsuarioRolFlujoCarrera(UsuarioCreacionDto entidad, Integer rolId, Integer carreraId) throws RegistroPostulanteDtoValidacionException,RegistroPostulanteDtoException{
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		boolean retorno = false;
		if (entidad != null) {
			try {
			//*************************************************************
			//********************* VALIDACIONES **************************
			//*************************************************************
				Persona persona = new Persona();
				persona.setPrsIdentificacion(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsIdentificacion()).toUpperCase());
				persona.setPrsPrimerApellido(GeneralesUtilidades.eliminarTildes(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsPrimerApellido()).toUpperCase()));
				persona.setPrsMailPersonal(GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsMailPersonal()));
				if(persona.getPrsPrimerApellido().length()==0){
					throw new RegistroPostulanteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.general.apellido.null.exception")));
				}
				//validacion de constraint de identificacion
				if(!verificarConstraintIdenfificadorPersona(persona, GeneralesConstantes.APP_NUEVO)){
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.identificacion.persona.validacion.exception")));
				}
				//validacion de constraint de mail
				if(!verificarConstraintMailPersona(persona, GeneralesConstantes.APP_NUEVO)){
					throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.mail.persona.validacion.exception")));
				}	
					
					StringBuilder sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" SELECT ");
					sbSqlPersona.append(" max(");sbSqlPersona.append(JdbcConstantes.PRS_ID);
					sbSqlPersona.append(") AS id FROM ");
					sbSqlPersona.append(JdbcConstantes.TABLA_PERSONA);
					con = ds.getConnection();
					
					pstmt = con.prepareStatement(sbSqlPersona.toString());
					
					rs = pstmt.executeQuery();
					Integer prsId=0;
					while (rs.next()){
						prsId=rs.getInt("id");
					}
					if(rs!=null){
						rs.close();
					}
					sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO PERSONA (prs_id, prs_tipo_identificacion,prs_tipo_identificacion_sniese,"
							+ "prs_identificacion,prs_nombres, prs_primer_apellido, prs_segundo_apellido, prs_mail_personal,prs_telefono,"
							+ "prs_fecha_nacimiento,prs_sexo,prs_sexo_sniese, prs_mail_institucional"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ? , ? , ? , ? , ? , ?, ? , ? , ? , ?, ?,? )");
					pstmt2 = con.prepareStatement(sbSqlPersona.toString());
					pstmt2.setInt(1, prsId+1);
					pstmt2.setInt(2, entidad.getPrsTipoIdentificacion());
					pstmt2.setInt(3, entidad.getPrsTipoIdentificacion()+1);
					pstmt2.setString(4, entidad.getPrsIdentificacion());
					pstmt2.setString(5, GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsNombres()).toUpperCase());
					pstmt2.setString(6, GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsPrimerApellido()).toUpperCase());
					pstmt2.setString(7, GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsSegundoApellido()).toUpperCase());
					pstmt2.setString(8, entidad.getPrsMailPersonal());
					
					pstmt2.setString(9, entidad.getPrsTelefono());
					try {
						pstmt2.setDate(10, new java.sql.Date(entidad.getPrsFechaNacimiento().getTime()));
					} catch (Exception e) {
						pstmt2.setDate(10, null);
					}
					
					pstmt2.setInt(11, entidad.getPrsSexo());
					pstmt2.setInt(12, entidad.getPrsSexo()+1);
					pstmt2.setString(13, entidad.getPrsMailInstitucional());
					pstmt2.executeUpdate();
					if (pstmt2 != null){
						pstmt2.close();
					}
					
					
					int indice = entidad.getPrsMailInstitucional().indexOf("@");
					String usrNick = entidad.getPrsMailInstitucional().substring(0,indice);
					
					//creacion del objeto usuario
					Usuario usuario = new Usuario();
					usuario.setUsrIdentificacion(persona.getPrsIdentificacion());
					usuario.setUsrNick(usrNick);
					
					//validacion de constraint de identificacion
					if(!verificarConstraintIdenficadorUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
						throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.identificacion.usuario.validacion.exception")));
					}
					//validacion de constraint de nick
					if(!verificarConstraintNickUsuario(usuario, GeneralesConstantes.APP_NUEVO)){
						throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.nick.usuario.validacion.exception")));
					}
					Integer usrId=0;
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" max(");sbSql.append(JdbcConstantes.USR_ID);
					sbSql.append(") AS id FROM ");
					sbSql.append(JdbcConstantes.TABLA_USUARIO);
					//FIN QUERY
					pstmt.close();
					
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs1 = pstmt.executeQuery();
					
					while (rs1.next()){
						usrId=rs1.getInt("id");
					}
					if(rs1!=null){
						rs1.close();
					}
					sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO USUARIO (usr_id, usr_identificacion,usr_nick,"
							+ "usr_password,usr_fecha_creacion, usr_fecha_caducidad, usr_fecha_cad_pass, usr_estado,usr_est_sesion,"
							+ "usr_active_directory,prs_id"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ? , ? , ? , ? , ? , ?, ? , ? , ? , ? )");
					pstmt1 = con.prepareStatement(sbSqlPersona.toString());
					
					
					pstmt1.setInt(1, usrId+1);
					pstmt1.setString(2, entidad.getPrsIdentificacion());
					pstmt1.setString(3, usuario.getUsrNick());
					pstmt1.setString(4, entidad.getUsrPassword());
					Date fecha = new Date();
					pstmt1.setDate(5, new java.sql.Date(fecha.getTime()));
					pstmt1.setDate(6, new java.sql.Date(fecha.getTime()));
					pstmt1.setDate(7, new java.sql.Date(fecha.getTime()));
					pstmt1.setInt(8, entidad.getUsrEstado());
					pstmt1.setInt(9, UsuarioConstantes.ESTADO_SESSION_NO_LOGGEADO_VALUE);
					pstmt1.setInt(10, entidad.getUsrActiveDirectory());
					pstmt1.setInt(11, prsId+1);
					
					pstmt1.executeUpdate();
					if (pstmt1 != null){
						pstmt1.close();
					}
					Integer usroId=0;
					sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" max(");sbSql.append(JdbcConstantes.USRO_ID);
					sbSql.append(") AS id FROM ");
					sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
					//FIN QUERY
					pstmt.close();
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs2 = pstmt.executeQuery();
					
					while (rs2.next()){
						usroId=rs2.getInt("id");
					}
					if(rs2!=null){
						rs2.close();
					}
					sbSqlPersona = new StringBuilder();
					sbSqlPersona.append(" INSERT INTO USUARIO_ROL (usro_id, usr_id,usro_estado,rol_id"
							+ ") VALUES( ");
					sbSqlPersona.append(" ? , ?, ? , ? )");
					pstmt3 = con.prepareStatement(sbSqlPersona.toString());
					pstmt3.setInt(1, usroId+1);
					pstmt3.setInt(2, usrId+1);
					pstmt3.setInt(3, UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
					pstmt3.setInt(4, rolId);
					pstmt3.executeUpdate();
					if (pstmt3 != null){
						pstmt3.close();
					}
					UsuarioRol usuarioRol = em.find(UsuarioRol.class, usroId+1);
					RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
					Carrera roflcrCarrera = em.find(Carrera.class, carreraId);
					roflcrAux.setRoflcrCarrera(roflcrCarrera);
					roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
					roflcrAux.setRoflcrUsuarioRol(usuarioRol);
					em.persist(roflcrAux);
					em.flush();
				
			
			} catch (PersonaValidacionException e) {
				try{
					@SuppressWarnings("unused")
					Usuario usrAux= new Usuario();
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" Select u from Usuario u where ");
					sbSql.append(" usrIdentificacion= :identificacion ");
					
					Query q = em.createQuery(sbSql.toString());
					q.setParameter("identificacion",entidad.getPrsIdentificacion());
					usrAux = (Usuario)q.getSingleResult();
					
					throw new RegistroPostulanteDtoValidacionException(e.getMessage());
				}catch(NoResultException e1){
					//creacion del objeto usuario
					Usuario usuario = new Usuario();
					usuario.setUsrIdentificacion(entidad.getPrsIdentificacion());
					usuario.setUsrNick(entidad.getUsrNick());
					usuario.setUsrPassword(entidad.getUsrPassword());
					usuario.setUsrFechaCreacion(entidad.getUsrFechaCreacion());
					usuario.setUsrFechaCaducidad(entidad.getUsrFechaCaducidad());
					usuario.setUsrFechaCadPass(entidad.getUsrFechaCadPass());
					usuario.setUsrEstado(entidad.getUsrEstado());
					usuario.setUsrEstSesion(entidad.getUsrEstSesion());
					usuario.setUsrActiveDirectory(entidad.getUsrActiveDirectory());
					Persona prsAux= new Persona();
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" Select p from Persona p where ");
					sbSql.append(" prsIdentificacion= :identificacion ");
					
					Query q = em.createQuery(sbSql.toString());
					q.setParameter("identificacion",entidad.getPrsIdentificacion());
					prsAux = (Persona)q.getSingleResult();
					usuario.setUsrPersona(prsAux);
					//creacion del objeto usuario_rol
					Rol rol = em.find(Rol.class, rolId);
					UsuarioRol usuarioRol = new UsuarioRol();
					usuarioRol.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
					usuarioRol.setUsroRol(rol);
					//*********      Insercion de usuariorol
					//asignacion del usuario al usuario_rol
					usuarioRol.setUsroUsuario(usuario);
					em.persist(usuario);
					em.flush();
					em.persist(usuarioRol);
					em.flush();
					RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
					Carrera roflcrCarrera = em.find(Carrera.class, carreraId);
					roflcrAux.setRoflcrCarrera(roflcrCarrera);
					roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
					roflcrAux.setRoflcrUsuarioRol(usuarioRol);
					em.persist(roflcrAux);
					em.flush();
					
					retorno = true;
					
				}catch(Exception e1){
				}
			} catch (UsuarioValidacionException e) {
				throw new RegistroPostulanteDtoValidacionException(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RegistroPostulanteDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.dto.anadir.general.exception")));
			}finally {
				try {
					if( rs != null){
						rs.close();
					}
					if( rs1 != null){
						rs1.close();
					}
					if( rs2 != null){
						rs2.close();
					}
					if (pstmt != null){
						pstmt.close();
					}
					if (pstmt1 != null){
						pstmt1.close();
					}
					if (pstmt2 != null){
						pstmt2.close();
					}
					if (pstmt3 != null){
						pstmt3.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (Exception e2) {
				}
				
			}
			
		}
				
		return retorno;
	}
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return boolean - TRUE si se registró exitosamente, FALSE caso contrario
	 * @throws RegistroPostulanteDtoValidacionException - lanzada cuando la validación de la entidad RegistroPostulanteDto falla.
	 * @throws RegistroPostulanteDtoException - Excepcion general.
	 */
	@Override
	public boolean anadirRolFlujoCarrera(UsuarioRolJdbcDto entidad,Integer rolId, Integer carreraId) throws RegistroPostulanteDtoValidacionException,RegistroPostulanteDtoException{
		boolean retorno = false;
			if(entidad.getRolId()==rolId){
				UsuarioRol usroAux = new UsuarioRol();
				usroAux = em.find(UsuarioRol.class, entidad.getUsroId());
				RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
				Carrera roflcrCarrera = em.find(Carrera.class, carreraId);
				roflcrAux.setRoflcrCarrera(roflcrCarrera);
				roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
				roflcrAux.setRoflcrUsuarioRol(usroAux);
				em.persist(roflcrAux);
				retorno=true;
			}else{
				UsuarioRol usroCreacion = new UsuarioRol();
				Rol rolAux = em.find(Rol.class, rolId);
				Usuario usrAux = em.find(Usuario.class, entidad.getUsrId());
				usroCreacion.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				usroCreacion.setUsroRol(rolAux);
				usroCreacion.setUsroUsuario(usrAux);
				em.persist(usroCreacion);
				RolFlujoCarrera roflcrAux = new RolFlujoCarrera();
				Carrera roflcrCarrera = em.find(Carrera.class, carreraId);
				roflcrAux.setRoflcrCarrera(roflcrCarrera);
				roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
				roflcrAux.setRoflcrUsuarioRol(usroCreacion);
				em.persist(roflcrAux);
				retorno=true;
			}
		return retorno;
	}
	
	
	
	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintIdenficadorUsuario(Usuario entidad, int tipo){
		boolean retorno = false;
		Usuario usuAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select u from Usuario u where ");
			sbSql.append(" upper(u.usrIdentificacion)= :identificacion ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND u.usrId != :usuarioId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion",entidad.getUsrIdentificacion().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("usuarioId",entidad.getUsrId());
			}
			usuAux = (Usuario)q.getSingleResult();
		}catch(NoResultException nre){
			usuAux = null;
		}catch(NonUniqueResultException nure){
			usuAux = new Usuario();
		}
		
		if(usuAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	
	/**
	 * Metodo que verifica la existencia de un nick en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintNickUsuario(Usuario entidad, int tipo){
		boolean retorno = false;
		Usuario usuAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select u from Usuario u where ");
			sbSql.append(" upper(u.usrNick)= :nickName ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND u.usrId != :usuarioId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("nickName",entidad.getUsrNick().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("usuarioId",entidad.getUsrId());
			}
			usuAux = (Usuario)q.getSingleResult();
		}catch(NoResultException nre){
			usuAux = null;
		}catch(NonUniqueResultException nure){
			usuAux = new Usuario(-99);
		}
		
		if(usuAux==null){
			retorno = true;
		}
		return retorno;
	}

	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintIdenfificadorPersona(Persona entidad, int tipo){
		boolean retorno = false;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsIdentificacion)= :identificacion ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND p.prsId != :personaId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion",entidad.getPrsIdentificacion().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("personaId",entidad.getPrsId());
			}
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintMailPersona(Persona entidad, int tipo){
		boolean retorno = false;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsMailPersonal)= :mailPersonal ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND p.prsId != :personaId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("mailPersonal",entidad.getPrsMailPersonal().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("personaId",entidad.getPrsId());
			}
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	
	
	private Persona verificarPersonaDocente(String identificacion){
		Persona retorno = null;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p , FichaDocente fcdc where ");
			sbSql.append(" upper(p.prsIdentificacion)= :identificacion ");
				sbSql.append(" AND p.prsId = fcdc.fcdcPersona.prsId ");
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion",identificacion);
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		if(perAux==null){
			retorno = null;
		}else{
			retorno = perAux;
		}
		return retorno;
	}
}

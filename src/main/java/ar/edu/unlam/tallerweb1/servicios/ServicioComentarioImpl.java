package ar.edu.unlam.tallerweb1.servicios;

import ar.edu.unlam.tallerweb1.modelo.Comentario;
import ar.edu.unlam.tallerweb1.modelo.Derivacion;
import ar.edu.unlam.tallerweb1.modelo.SolicitudDerivacion;
import ar.edu.unlam.tallerweb1.modelo.Usuario;
import ar.edu.unlam.tallerweb1.repositorios.RepositorioComentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Transactional
@Service("servicioComentario")
public class ServicioComentarioImpl implements ServicioComentario {
    private RepositorioComentario repositorioComentario;
    private ServicioDerivacion servicioDerivacion;
    private ServicioUsuario servicioUsuario;
    private ServicioSolicitudDerivacion servicioSolicitudDerivacion;


    @Autowired
    public ServicioComentarioImpl(RepositorioComentario repositorioComentario, ServicioDerivacion servicioDerivacion,
                                  ServicioUsuario servicioUsuario, ServicioSolicitudDerivacion servicioSolicitudDerivacion){
        this.repositorioComentario = repositorioComentario;
        this.servicioDerivacion = servicioDerivacion;
        this.servicioUsuario = servicioUsuario;
        this.servicioSolicitudDerivacion=servicioSolicitudDerivacion;
    }

    @Override
    public void guardarComentario(Comentario comentario) {
        repositorioComentario.guardarComentario(comentario);
    }

    @Override
    public void guardarComentarioDerivacion(Long idDerivacion, String mensaje ,HttpServletRequest request, String funcion) throws Exception {
        Derivacion derivacion = servicioDerivacion.verDerivacion(idDerivacion);
        Usuario usuario = servicioUsuario.consultarUsuarioPorId((Long) request.getSession().getAttribute("ID_USUARIO"));
        if(derivacion != null && usuario != null){
            Comentario comentario = new Comentario();
            comentario.setDerivacion(derivacion);
            comentario.setAutor(usuario);
            comentario.setFechaCreacion(new Date());

            switch (funcion.toUpperCase()){

                case "R": {
                    comentario.setMensaje(mensaje);
                    comentario.setAsunto("Registrar comentario");
                    this.guardarComentario(comentario);
                }

                case "G": {
                    comentario.setMensaje("Se ha generado la derivacion "+derivacion.getId()+" para la cobertura "+derivacion.getCobertura().getNombre());;
                    comentario.setAsunto("Inicia");
                    this.guardarComentario(comentario);
                    break;
                }

                case "F": {
                    comentario.setMensaje("Ha finalizado la derivación "+derivacion.getId());
                    comentario.setAsunto("Finalizado");
                    this.guardarComentario(comentario);
                    break;
                }

                case "C": {
                    comentario.setMensaje(mensaje);
                    comentario.setAsunto("Cancelado");
                    this.guardarComentario(comentario);
                    break;
                }

                default:{
                    break;
                }
            }

        }
    }

    @Override
    public void guardarComentarioSolicitudDerivacion(Long idSolicitudDerivacion, String mensaje, HttpServletRequest request, String funcion) {
        SolicitudDerivacion solicitudDerivacion = servicioSolicitudDerivacion.obtenerSolicitudDerivacionPorId(idSolicitudDerivacion);
        Usuario usuario = servicioUsuario.consultarUsuarioPorId((Long) request.getSession().getAttribute("ID_USUARIO"));

        if (solicitudDerivacion != null && usuario != null){
            Comentario comentario = new Comentario();
            comentario.setSolicitudDerivacion(solicitudDerivacion);
            comentario.setAutor(usuario);
            comentario.setFechaCreacion(new Date());


            switch (funcion.toUpperCase()){

                case "G": {
                    comentario.setMensaje("Se ha generado la solicitud "+solicitudDerivacion.getId()+" al centro médico "+solicitudDerivacion.getCentroMedico().getNombre());;
                    comentario.setAsunto("Inicia");
                    this.guardarComentario(comentario);
                    break;
                }

                case "R": {
                    comentario.setMensaje(mensaje);;
                    comentario.setAsunto("Rechazar");
                    this.guardarComentario(comentario);
                    break;
                }

                case "A": {
                    comentario.setMensaje(mensaje);
                    comentario.setAsunto("Aceptado");
                    this.guardarComentario(comentario);
                    break;
                }

                case "F": {
                    comentario.setMensaje("Ha finalizado la solicitud " +solicitudDerivacion.getId());
                    comentario.setAsunto("Finalizado");
                    this.guardarComentario(comentario);
                    break;
                }

                default:{
                    break;
                }
            }
        }
    }




}
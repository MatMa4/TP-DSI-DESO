/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.UsuarioDAO;
import TP_Back.appSpringTP.DTOs.UsuarioDTO;
import TP_Back.appSpringTP.excepciones.ContrasenaInvalidaException;
import TP_Back.appSpringTP.excepciones.UsuarioExistenteException;
import TP_Back.appSpringTP.excepciones.UsuarioNoEncontradoException;
import TP_Back.appSpringTP.modelo.usuario.Usuario;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author mateo
 */
@ExtendWith(MockitoExtension.class)
public class GestorDeUsuarioTest {
    
    public GestorDeUsuarioTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Mock
    private UsuarioDAO usuarioDAO;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private GestorDeUsuario gestorUsuario;
    
    /**
     * Test of autenticarUsuario method, of class GestorDeUsuario.
     */
    @Test
    public void testAutenticarUsuario_casoIdeal() throws Exception {
        UsuarioDTO userDto = new UsuarioDTO();
        userDto.setPassw("pass");
        userDto.setRol("Admin");
        userDto.setUsername("nombre");
        
        Usuario usuario = new Usuario();
        usuario.setContrasena("pass");
        usuario.setRol("Admin");
        usuario.setUsuario("nombre");
        
        when(usuarioDAO.findById(userDto.getUsername()))
                .thenReturn(Optional.of(usuario));
        
        when(passwordEncoder.matches(userDto.getPassw(), usuario.getContrasena()))
                .thenReturn(Boolean.TRUE);
        
        gestorUsuario.autenticarUsuario(userDto);
        
        verify(usuarioDAO).findById(userDto.getUsername());
        verify(passwordEncoder).matches(userDto.getPassw(), usuario.getContrasena());
    }

    @Test
    public void testAutenticarUsuario_usuarioNoEncontrado() throws Exception {
        UsuarioDTO userDto = new UsuarioDTO();
        userDto.setPassw("pass");
        userDto.setRol("Admin");
        userDto.setUsername("nombre");
        
        Usuario usuario = new Usuario();
        usuario.setContrasena("pass");
        usuario.setRol("Admin");
        usuario.setUsuario("nombre");
        
        when(usuarioDAO.findById(userDto.getUsername()))
                .thenReturn(Optional.empty());
        
        assertThrows(UsuarioNoEncontradoException.class,
            () -> gestorUsuario.autenticarUsuario(userDto));
        
        verify(usuarioDAO).findById(userDto.getUsername());
        verify(passwordEncoder, never()).matches(userDto.getPassw(), usuario.getContrasena());
    }
   
    @Test
    public void testAutenticarUsuario_contraseniaincorrecta() throws Exception {
        UsuarioDTO userDto = new UsuarioDTO();
        userDto.setPassw("pass");
        userDto.setRol("Admin");
        userDto.setUsername("nombre");
        
        Usuario usuario = new Usuario();
        usuario.setContrasena("pass");
        usuario.setRol("Admin");
        usuario.setUsuario("nombre");
        
        when(usuarioDAO.findById(userDto.getUsername()))
                .thenReturn(Optional.of(usuario));
        
        when(passwordEncoder.matches(userDto.getPassw(), usuario.getContrasena()))
                .thenReturn(Boolean.FALSE);
        
        assertThrows(ContrasenaInvalidaException.class,
            () -> gestorUsuario.autenticarUsuario(userDto));
        
        verify(usuarioDAO).findById(userDto.getUsername());
        verify(passwordEncoder).matches(userDto.getPassw(), usuario.getContrasena());
    }
    
    /**
     * Test of crearUsuario method, of class GestorDeUsuario.
     */
    @Test
    public void testCrearUsuario_casoIdeal() throws Exception {
        UsuarioDTO userDto = new UsuarioDTO();
        userDto.setPassw("pass");
        userDto.setRol("Admin");
        userDto.setUsername("nombre");
        
        Usuario usuario = new Usuario();
        usuario.setContrasena("contraseniaEjemplo");
        usuario.setRol("Admin");
        usuario.setUsuario("nombre");
        
        when(usuarioDAO.existsById(userDto.getUsername()))
                .thenReturn(Boolean.FALSE);
        
        when(passwordEncoder.encode(userDto.getPassw()))
                .thenReturn("contrseniaEjemplo");
        
        when(usuarioDAO.save(any(Usuario.class)))
                .thenReturn(usuario);
        
        Usuario resultado = gestorUsuario.crearUsuario(userDto);
        
        verify(usuarioDAO).existsById(userDto.getUsername());
        verify(passwordEncoder).encode(userDto.getPassw());
        verify(usuarioDAO).save(any(Usuario.class));
        
        assertEquals(usuario, resultado);
    }
    
    @Test
    public void testCrearUsuario_usuarioExistente() throws Exception {
        UsuarioDTO userDto = new UsuarioDTO();
        userDto.setPassw("pass");
        userDto.setRol("Admin");
        userDto.setUsername("nombre");
        
        Usuario usuario = new Usuario();
        usuario.setContrasena("pass");
        usuario.setRol("Admin");
        usuario.setUsuario("nombre");
        
        when(usuarioDAO.existsById(userDto.getUsername()))
                .thenReturn(Boolean.TRUE);
        
        assertThrows(UsuarioExistenteException.class,
            () -> gestorUsuario.crearUsuario(userDto));
        
        verify(usuarioDAO).existsById(userDto.getUsername());
        verify(passwordEncoder, never()).encode(userDto.getPassw());
        verify(usuarioDAO, never()).save(usuario);
    }
    
}

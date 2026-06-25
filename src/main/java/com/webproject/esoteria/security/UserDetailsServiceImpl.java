package com.webproject.esoteria.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Método adaptado: si solicita "admin", devolvemos su usuario virtual con la clave fija
        if ("admin".equals(username)) {
            // {noop} le indica a Spring Security que la contraseña no requiere codificador adicional
            return new User("admin", "{noop}password", Collections.emptyList());
        }
        throw new UsernameNotFoundException("Usuario no encontrado en el sistema: " + username);
    }
}

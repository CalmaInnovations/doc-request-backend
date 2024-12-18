package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.dto.request.SignInRequest;
import com.calma.DocManagerServer.dto.request.SignUpRequest;
import com.calma.DocManagerServer.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO crearUsuarioIn(SignUpRequest signUpRequest);

    Object signIn(SignInRequest signInRequest);

    List<UsuarioDTO> listarUsuarios();

    UsuarioDTO buscarUsuarioPorId(long usuarioId);

    UsuarioDTO crearUsuario(SignUpRequest signUpRequest);


}

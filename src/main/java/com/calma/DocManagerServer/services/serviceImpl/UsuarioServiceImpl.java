package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.dto.UsuarioDTO;
import com.calma.DocManagerServer.dto.request.SignInRequest;
import com.calma.DocManagerServer.dto.request.SignUpRequest;
import com.calma.DocManagerServer.mapper.UsuarioMapper;
import com.calma.DocManagerServer.model.Usuario;
import com.calma.DocManagerServer.repository.UsuarioRepository;
import com.calma.DocManagerServer.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioDTO crearUsuarioIn(SignUpRequest signUpRequest) {
        return null;
    }

    @Override
    public Object signIn(SignInRequest signInRequest) {
        return null;
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return null;
    }

    @Override
    public UsuarioDTO buscarUsuarioPorId(long usuarioId) {
        return null;
    }

    @Override
    public UsuarioDTO crearUsuario(SignUpRequest signUpRequest) {
        return null;
    }
}

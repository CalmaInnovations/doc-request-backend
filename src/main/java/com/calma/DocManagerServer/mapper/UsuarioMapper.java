package com.calma.DocManagerServer.mapper;

import com.calma.DocManagerServer.dto.UsuarioDTO;
import com.calma.DocManagerServer.model.Usuario;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
@Service
public class UsuarioMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public UsuarioDTO mapToDto(Usuario entity){
        return modelMapper.map(entity, UsuarioDTO.class);
    }

    public Usuario mapToEntity(UsuarioDTO dto){
        return modelMapper.map(dto, Usuario.class);
    }
}

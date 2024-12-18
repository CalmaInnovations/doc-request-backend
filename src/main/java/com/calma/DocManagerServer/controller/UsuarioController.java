package com.calma.DocManagerServer.controller;


import com.calma.DocManagerServer.dto.request.SignInRequest;
import com.calma.DocManagerServer.dto.request.SignUpRequest;
import com.calma.DocManagerServer.dto.UsuarioDTO;
import com.calma.DocManagerServer.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @PostMapping("/signup")
    public ResponseEntity<UsuarioDTO> singUpUser(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<UsuarioDTO> signIn( @RequestBody SignInRequest signInRequest) throws Exception {
        return ResponseEntity.ok((UsuarioDTO) usuarioService.signIn(signInRequest));
    }

    @GetMapping("/admin/list")
    public ResponseEntity<List<UsuarioDTO>>listUsers(){
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }
    @GetMapping("/admin/usuario/verinfo/{id}")
    public ResponseEntity<?> obtenerusuarioPorId(@PathVariable("id") long usuarioId) {
        UsuarioDTO usuarioDTO = usuarioService.buscarUsuarioPorId(usuarioId);
        if (usuarioDTO == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Usuario con ID " + usuarioId + " no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.ok(usuarioDTO);
    }}
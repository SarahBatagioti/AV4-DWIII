package com.autobots.automanager.excecoes;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorGlobalDeExcecoes {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErroResposta> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ApiErroResposta> tratarRequisicaoInvalida(
            RequisicaoInvalidaException ex,
            HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ConflitoDeRecursoException.class)
    public ResponseEntity<ApiErroResposta> tratarConflito(
            ConflitoDeRecursoException ex,
            HttpServletRequest request) {
        return responder(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErroResposta> tratarValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatarErroDeCampo)
                .collect(Collectors.joining("; "));
        return responder(HttpStatus.BAD_REQUEST, mensagem, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErroResposta> tratarCorpoInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, "Corpo da requisicao invalido", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErroResposta> tratarIntegridade(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return responder(HttpStatus.CONFLICT, "Os dados informados entram em conflito com um registro existente", request);
    }

    private String formatarErroDeCampo(FieldError erro) {
        return erro.getField() + ": " + erro.getDefaultMessage();
    }

    private ResponseEntity<ApiErroResposta> responder(HttpStatus status, String mensagem, HttpServletRequest request) {
        ApiErroResposta corpo = new ApiErroResposta(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI());
        return ResponseEntity.status(status).body(corpo);
    }
}

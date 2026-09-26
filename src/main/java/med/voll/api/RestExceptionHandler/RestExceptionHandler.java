package med.voll.api.RestExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	public record DadosErro(int status, String erro, List<String> mensagens) {
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
		var mensagens = ex.getBindingResult().getFieldErrors().stream()
				.map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
				.toList();

		return new ResponseEntity<>(
				new DadosErro(HttpStatus.BAD_REQUEST.value(), "Requisição inválida", mensagens), headers, statusCode);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<DadosErro> dadosDuplicados(DataIntegrityViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new DadosErro(HttpStatus.CONFLICT.value(), "Conflito de dados",
						List.of("Já existe um registro com este CPF ou email.")));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<DadosErro> naoEncontrado(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new DadosErro(HttpStatus.NOT_FOUND.value(), "Registro não encontrado",
						List.of("O ID informado não existe no banco de dados.")));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<DadosErro> inesperado(Exception ex) {
		return ResponseEntity.internalServerError()
				.body(new DadosErro(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno",
						List.of(ex.getClass().getSimpleName() + ": " + ex.getMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		var mensagens = new ArrayList<String>();

		if (body instanceof ProblemDetail detalhe && detalhe.getDetail() != null) {
			mensagens.add(detalhe.getDetail());
		} else if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
			mensagens.add(ex.getMessage());
		} else {
			mensagens.add(frase(statusCode.value()));
		}

		return new ResponseEntity<>(
				new DadosErro(statusCode.value(), frase(statusCode.value()), mensagens), headers, statusCode);
	}

	private static String frase(int codigo) {
		var status = HttpStatus.resolve(codigo);
		return status == null ? "Erro" : status.getReasonPhrase();
	}
}

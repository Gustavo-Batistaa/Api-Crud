package med.voll.api.RequestLogging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "app.log-requests", havingValue = "true", matchIfMissing = true)
public class LogRequestFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(LogRequestFilter.class);
	private static final Set<String> HEADERS_SENSIVEIS = Set.of("authorization", "cookie", "set-cookie");
	private static final int TAMANHO_MAXIMO_LOG = 4000;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var requisicao = new ContentCachingRequestWrapper(request);
		var resposta = new ContentCachingResponseWrapper(response);
		var inicio = System.currentTimeMillis();

		MDC.put("req", UUID.randomUUID().toString().substring(0, 6));
		try {
			log.info("--> {} {} | ip={} | headers={}", request.getMethod(), caminho(request),
					request.getRemoteAddr(), headers(requisicao));

			filterChain.doFilter(requisicao, resposta);
		} finally {
			var corpoRequisicao = corpo(requisicao.getContentAsByteArray());
			if (!corpoRequisicao.isBlank()) {
				log.info("--> body: {}", corpoRequisicao);
			}

			log.info("<-- {} {} | status={} | tempo={}ms", request.getMethod(), caminho(request),
					resposta.getStatus(), System.currentTimeMillis() - inicio);

			resposta.copyBodyToResponse();
			MDC.remove("req");
		}
	}

	private String caminho(HttpServletRequest request) {
		var query = request.getQueryString();
		return query == null || query.isBlank() ? request.getRequestURI() : request.getRequestURI() + "?" + query;
	}

	private String headers(HttpServletRequest request) {
		Map<String, List<String>> todos = Collections.list(request.getHeaderNames()).stream()
				.collect(Collectors.toMap(nome -> nome.toLowerCase(), nome -> Collections.list(request.getHeaders(nome)), (a, b) -> a));

		return todos.entrySet().stream()
				.map(e -> HEADERS_SENSIVEIS.contains(e.getKey()) ? e.getKey() + "=[oculto]" : e.getKey() + "=" + e.getValue())
				.collect(Collectors.joining(", ", "{", "}"));
	}

	private String corpo(byte[] conteudo) {
		if (conteudo == null || conteudo.length == 0) {
			return "";
		}
		var texto = new String(conteudo, StandardCharsets.UTF_8);
		return texto.length() > TAMANHO_MAXIMO_LOG ? texto.substring(0, TAMANHO_MAXIMO_LOG) + "...(truncado)" : texto;
	}

}

package med.voll.api.Medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.Endereco.DadosCadastroEndereco;

public record DadosAtualizarMedicos(
        @NotNull
        Long id,

        String nome ,

        String telefone,

        DadosCadastroEndereco endereco) {
}

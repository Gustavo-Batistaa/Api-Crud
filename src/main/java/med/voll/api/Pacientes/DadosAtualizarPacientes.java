package med.voll.api.Pacientes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import med.voll.api.Endereco.DadosCadastroEndereco;

public record DadosAtualizarPacientes(
        @NotNull
        Long id,
        String nome ,

        String telefone,

        @Valid DadosCadastroEndereco endereco) {
}

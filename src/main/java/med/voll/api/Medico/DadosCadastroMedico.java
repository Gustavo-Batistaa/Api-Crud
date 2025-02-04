package med.voll.api.Medico;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.Endereco.DadosCadastroEndereco;

public record DadosCadastroMedico(


        @NotBlank
        String nome,

        @NotBlank @Email
        String email,

        @NotBlank @Pattern(regexp = "\\d{4,6}")
        String crm,

        @NotNull
        Especialidade especialidade,


        String telefone ,
        @NotNull@Valid
        DadosCadastroEndereco endereco) {
}

package med.voll.api.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import med.voll.api.Pacientes.DadosPaciente;

public record DadosCadastroEndereco(

        @NotBlank
        String logradouro,

        @NotBlank
        String bairro ,


        @NotBlank
                @Pattern(regexp = "\\d{8}")
        String cep ,


        @NotBlank
        String cidade ,

        @NotBlank
        String uf ,
        @NotBlank
        String estado ,

        String numero ,

        String complemento



) {
}

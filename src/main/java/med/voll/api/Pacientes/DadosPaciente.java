package med.voll.api.Pacientes;

import med.voll.api.Endereco.DadosCadastroEndereco;
import med.voll.api.Endereco.Endereco;

public record DadosPaciente(

        String nome,

        String cpf,

        String email,

        String telefone,

        DadosCadastroEndereco endereco
        ) {
}

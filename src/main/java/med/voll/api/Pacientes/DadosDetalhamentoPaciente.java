package med.voll.api.Pacientes;

import med.voll.api.Endereco.Endereco;

public record DadosDetalhamentoPaciente(
        Long id,

        String nome ,

        String cpf ,

        String email ,

        String telefone ,


        Endereco endereco ,

        Boolean ativo) {

    public DadosDetalhamentoPaciente (Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getCpf(), paciente.getEmail(), paciente.getTelefone(), paciente.getEndereco(), paciente.getAtivo());
    }
    }

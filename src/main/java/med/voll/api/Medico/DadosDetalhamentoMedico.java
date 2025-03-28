package med.voll.api.Medico;

import med.voll.api.Endereco.Endereco;

public record DadosDetalhamentoMedico(Long id, String nome, String email, String crm,String telefone, Especialidade especialidade, Endereco endereco, Boolean ativo) {

    public DadosDetalhamentoMedico (Medico medico){
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(),medico.getTelefone(), medico.getEspecialidade(), medico.getEndereco(), medico.getAtivo());
    }
}

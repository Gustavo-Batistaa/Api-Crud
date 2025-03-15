package med.voll.api.Pacientes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.Endereco.Endereco;
import med.voll.api.Endereco.DadosCadastroEndereco;

@Entity(name = "Paciente")
@Table(name = "pacientes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cpf;

    private String email;

    private String telefone;
    @Embedded
    private Endereco endereco;

    private Boolean ativo;

    public Paciente(DadosPaciente dadosPaciente){
        this.nome = dadosPaciente.nome();
        this.ativo= true;
        this.cpf = dadosPaciente.cpf();
        this.email = dadosPaciente.email();
        this.telefone = dadosPaciente.telefone();
        this.endereco = new Endereco(dadosPaciente.endereco());


    }

    public void atualizarDados(DadosAtualizarPacientes dados) {
        if(dados.nome() != null){
            this.nome = dados.nome();
        }

        if (dados.telefone() != null){
            this.telefone = dados.telefone();
        }
        if (dados.endereco() != null){
            this.endereco = endereco.atualizarInformacoes(dados.endereco());
        }

    }

    public void excluir() {
        this.ativo = false;
    }
}

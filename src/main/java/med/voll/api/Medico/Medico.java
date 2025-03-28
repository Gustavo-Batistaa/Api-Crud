package med.voll.api.Medico;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.Endereco.DadosCadastroEndereco;
import med.voll.api.Endereco.Endereco;


@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nome;
        private String email;
        private String crm;

        private String telefone;
        @Enumerated(EnumType.STRING)
        private Especialidade especialidade;

        @Embedded
        private Endereco endereco;

        private Boolean ativo;

        public Medico(DadosCadastroMedico dados) {
                this.nome= dados.nome();
                this.ativo=true;
                this.email= dados.email();
                this.crm= dados.crm();
                this.telefone= dados.telefone();
                this.especialidade= dados.especialidade();
                this.endereco = new Endereco(dados.endereco());

        }

        public void atualizarInformacoes(DadosAtualizarMedicos dados) {
            if (dados.nome()!= null){
                    this.nome= dados.nome();
            }
            if (dados.telefone()!= null){
                    this.telefone= dados.telefone();

            }
            if(dados.ativo() != null){
                this.ativo= dados.ativo();

            }

            if (dados.endereco()!= null){
                this.endereco= endereco.atualizarInformacoes(dados.endereco());
            }
        }

        public void excluir (){
            this.ativo=false;
        }
}

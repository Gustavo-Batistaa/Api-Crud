package med.voll.api.PacienteController;

import jakarta.validation.Valid;
import med.voll.api.Pacientes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;
    @PostMapping
    @Transactional
    public void cadastrarPaciente(@RequestBody DadosPaciente paciente) {
        pacienteRepository.save(new Paciente(paciente));
    }
    @GetMapping
    public Page<DadosListagemPaciente> listar (Pageable paginacao){
      return  pacienteRepository.findAll(paginacao).map(DadosListagemPaciente::new);

    }

    @PutMapping()
    @Transactional
    public void atualizarPaciente(@RequestBody @Valid DadosAtualizarPacientes dados) {
        var paciente =pacienteRepository.getReferenceById(dados.id());
        paciente.atualizarDados(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirPaciente(@PathVariable Long id) {
        var paciente =pacienteRepository.getReferenceById(id);
        paciente.excluir();

    }
}

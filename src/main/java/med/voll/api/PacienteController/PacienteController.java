package med.voll.api.PacienteController;

import jakarta.validation.Valid;
import med.voll.api.Pacientes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarPaciente(@RequestBody DadosPaciente dados , UriComponentsBuilder uriBuilder) {
      var paciente = new Paciente(dados);
      pacienteRepository.save(paciente);
      var uri = uriBuilder.path("pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
      return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));





    }
    @GetMapping
    public ResponseEntity <Page<DadosListagemPaciente> >listar (Pageable paginacao){
      var page = pacienteRepository.findAll(paginacao).map(DadosListagemPaciente::new);
      return ResponseEntity.ok(page);

    }

    @PutMapping()
    @Transactional
    public ResponseEntity atualizarPaciente(@RequestBody @Valid DadosAtualizarPacientes dados) {
        var paciente =pacienteRepository.getReferenceById(dados.id());
        paciente.atualizarDados(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirPaciente(@PathVariable Long id) {
        var paciente =pacienteRepository.getReferenceById(id);
        paciente.excluir();
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity DetalharPaciente(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));

    }}

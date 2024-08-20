package com.example.apibdestoque.controllers;

import com.example.apibdestoque.model.Produtos;
import com.example.apibdestoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final Validator validator;

    public ProdutoController(ProdutoService produtoService, Validator validator) {
        this.produtoService = produtoService;
        this.validator = validator;
    }

    @GetMapping("/selecionar")
    @Operation(summary = "Retorna todos os produtos", description = "Retorna todos os produtos legais, ponto final.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produtos.class)))
    })
    public List<Produtos> listarProdutos() {
        return produtoService.buscarTodosProdutos();
    }

    @PostMapping ("/nome") List<Produtos> selecionarPorNome(@RequestBody Map<String, Object> updates) {
        return produtoService.buscarPorNome(updates.get("nome").toString());
    }

    @PostMapping("/nomePreco") List<Produtos> selecionarPorNomePreco(@RequestBody Map<String, Object> updates) {
        return produtoService.buscarPorNomePreco(updates.get("nome").toString(), Double.parseDouble(updates.get("preco").toString()));
    }
    @PostMapping("/inserir")
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody Produtos produtos, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors.toString());
        }
            produtoService.salvarProduto(produtos);
            return ResponseEntity.ok("Produto inserido com sucesso!");
    }

    @GetMapping("/excluir/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produtos.class)))
    })
    @Transactional
    public ResponseEntity<String> excluirProduto(@Parameter(description = "Identificador do produto", example = "1") @PathVariable Long id) {
        try {
            produtoService.excluirProduto(id);
            return ResponseEntity.ok("Produto excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir o produto");
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @Valid @RequestBody Produtos produtoAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors.toString());
        }
        Produtos produtoExistente = produtoService.buscarProdutoPorId(id);
        if (produtoExistente!=null) {
            produtoExistente.setNome(produtoAtualizado.getNome());
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
            produtoExistente.setPreco(produtoAtualizado.getPreco());
            produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
            produtoService.salvarProduto(produtoExistente);
            return ResponseEntity.ok("Produto alterado com sucesso");
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("atualizarParcial/{id}")
    public ResponseEntity<String> atualizarProdutoParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Produtos produtoExistente = produtoService.buscarProdutoPorId(id);
        if (produtoExistente!=null) {
            if (updates.containsKey("nome")) {
                produtoExistente.setNome((String) updates.get("nome"));
            }

            if (updates.containsKey("descricao")) {
                produtoExistente.setNome((String) updates.get("descricao"));
            }

            if (updates.containsKey("preco")) {
                String preco = String.valueOf(updates.get("preco"));
                produtoExistente.setPreco(Double.parseDouble(preco));
            }

            if (updates.containsKey("quantidadeEstoque")) {
                produtoExistente.setQuantidadeEstoque((Integer) updates.get("quantidadeEstoque"));
            }
            DataBinder binder = new DataBinder(produtoExistente);
            binder.addValidators(validator);
            binder.validate();
            if (binder.getBindingResult().hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : binder.getBindingResult().getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                return ResponseEntity.badRequest().body(errors.toString());
            }
            produtoService.salvarProduto(produtoExistente);
            return ResponseEntity.ok("Produto atualizado parcialmente com sucesso");
            } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poduto com ID " + id + " não encontrado");
        }
    }
}


package com.example.apibdestoque.service;

import com.example.apibdestoque.model.Produtos;
import com.example.apibdestoque.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public List<Produtos> buscarTodosProdutos(){
        return produtoRepository.findAll();
    }

    public List<Produtos> buscarPorNome(String nome){
        return produtoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public List<Produtos> buscarPorNomePreco(String nome, double preco){
        return produtoRepository.findByNomeLikeIgnoreCaseAndPrecoLessThanEqual(nome, preco);
    }

    @Transactional
    public Produtos salvarProduto(Produtos produtos){
        return produtoRepository.save(produtos);
    }

    public Produtos buscarProdutoPorId(Long id){
        return produtoRepository.findById(id).orElseThrow(()->
                new RuntimeException("Produto não encontrado"));
    }

    @Transactional
    public Long excluirProduto(Long id){
        try {
            Produtos produtos = buscarProdutoPorId(id);
            produtoRepository.deleteById(id);
            return id;
        }
        catch (Exception e){
            long result = -1;
            return result;
        }
    }



}

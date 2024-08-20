package com.example.apibdestoque.repository;

import com.example.apibdestoque.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produtos, Long> {
    List<Produtos> findByNomeLikeIgnoreCase(String nome);

    int countByQuantidadeEstoqueLessThan(int quantidadeEstoque);

    void deleteByQuantidadeEstoqueIsLessThanEqual(int quantidadeEstoque);

    List<Produtos> findByNomeLikeIgnoreCaseAndPrecoLessThanEqual(String nome, double preco);
}

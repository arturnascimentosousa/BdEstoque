package com.example.apibdestoque.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "produto")
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador do produto", example = "1")
    private long id;

    @NotNull(message= "O nome não pode ser nulo")
    @Size(min= 2, message = "O nome deve ter pelo menos 2 caractéres")
    private String nome;

    private String descricao;

    @NotNull(message= "O preço não pode ser nulo")
    @Min(value = 1, message = "O preço deve ser maior que zero")
    private double preco;

    @Column(name = "quantidadeestoque")
    @NotNull(message= "A quantidade não pode ser nulo")
    private int quantidadeEstoque;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidade) {
        this.quantidadeEstoque = quantidade;
    }

    @Override
    public String toString() {
        return "Produtos{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", quantidade=" + quantidadeEstoque +
                '}';
    }
}

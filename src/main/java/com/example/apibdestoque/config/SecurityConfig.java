package com.example.apibdestoque.config;

public class SecurityConfig {
    public static void main(String[] args) {
        try {
            // Lançar a exceção personalizada
            throw new Cassuiraga();
        } catch (Cassuiraga e) {
            // Capturar e tratar a exceção
            System.out.println(e.getMessage());
        }
    }
}

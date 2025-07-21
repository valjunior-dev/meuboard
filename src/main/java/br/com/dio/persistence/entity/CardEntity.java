package br.com.dio.persistence.entity;

import lombok.Data;

@Data
public class CardEntity {

    private Long id;
    private String title;
    private String description;
    private int columnOrder; // ✅ Adicionado
    private BoardColumnEntity boardColumn = new BoardColumnEntity();
}


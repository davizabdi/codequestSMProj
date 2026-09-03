package com.codequest.model;

public class Modulo {

    private Integer id;
    private int numero;
    private String titulo;
    private String descricao;
    private int topicos;
    private boolean disponivel;
    private int ordem;

    public Modulo() {
    }

    public Modulo(Integer id, int numero, String titulo, String descricao,
                  int topicos, boolean disponivel, int ordem) {
        this.id = id;
        this.numero = numero;
        this.titulo = titulo;
        this.descricao = descricao;
        this.topicos = topicos;
        this.disponivel = disponivel;
        this.ordem = ordem;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getTopicos() { return topicos; }
    public void setTopicos(int topicos) { this.topicos = topicos; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }
}

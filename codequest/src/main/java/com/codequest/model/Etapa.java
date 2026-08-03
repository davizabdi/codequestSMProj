package com.codequest.model;

public class Etapa {

    private Integer id;
    private int moduloId;
    private int numero;
    private String titulo;
    private String objetivo;
    private String explicacao;
    private String exemplo;
    private int ordem;

    public Etapa() {
    }

    public Etapa(Integer id, int moduloId, int numero, String titulo, String objetivo, String explicacao, String exemplo, int ordem) {
        this.id = id;
        this.moduloId = moduloId;
        this.numero = numero;
        this.titulo = titulo;
        this.objetivo = objetivo;
        this.explicacao = explicacao;
        this.exemplo = exemplo;
        this.ordem = ordem;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getModuloId() { return moduloId; }
    public void setModuloId(int moduloId) { this.moduloId = moduloId; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getExplicacao() { return explicacao; }
    public void setExplicacao(String explicacao) { this.explicacao = explicacao; }

    public String getExemplo() { return exemplo; }
    public void setExemplo(String exemplo) { this.exemplo = exemplo; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }
}

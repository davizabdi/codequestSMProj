package com.codequest.model;

public class Questao {

    private Integer id;
    private int moduloId;
    private String enunciado;
    private String alternativaA;
    private String alternativaB;
    private String alternativaC;
    private String alternativaD;
    private String correta; // "A", "B", "C" ou "D"
    private int ordem;

    public Questao() {
    }

    public Questao(Integer id, int moduloId, String enunciado,
                   String alternativaA, String alternativaB, String alternativaC, String alternativaD,
                   String correta, int ordem) {
        this.id = id;
        this.moduloId = moduloId;
        this.enunciado = enunciado;
        this.alternativaA = alternativaA;
        this.alternativaB = alternativaB;
        this.alternativaC = alternativaC;
        this.alternativaD = alternativaD;
        this.correta = correta;
        this.ordem = ordem;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getModuloId() { return moduloId; }
    public void setModuloId(int moduloId) { this.moduloId = moduloId; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getAlternativaA() { return alternativaA; }
    public void setAlternativaA(String alternativaA) { this.alternativaA = alternativaA; }

    public String getAlternativaB() { return alternativaB; }
    public void setAlternativaB(String alternativaB) { this.alternativaB = alternativaB; }

    public String getAlternativaC() { return alternativaC; }
    public void setAlternativaC(String alternativaC) { this.alternativaC = alternativaC; }

    public String getAlternativaD() { return alternativaD; }
    public void setAlternativaD(String alternativaD) { this.alternativaD = alternativaD; }

    public String getCorreta() { return correta; }
    public void setCorreta(String correta) { this.correta = correta; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }
}

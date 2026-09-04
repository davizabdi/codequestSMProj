package com.codequest.controller;

import com.codequest.model.Modulo;
import com.codequest.model.QuizQuestionDTO;
import com.codequest.model.QuizResultDTO;
import com.codequest.service.ModuloService;
import com.codequest.service.QuizService;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class QuizController {

    private final ModuloService moduloService;
    private final QuizService quizService;

    public QuizController() {
        this.moduloService = new ModuloService();
        this.quizService = new QuizService();
    }

    public QuizController(ModuloService moduloService, QuizService quizService) {
        this.moduloService = moduloService;
        this.quizService = quizService;
    }

    public void verQuizPage(Context ctx) {
        try {
            int moduloId = Integer.parseInt(ctx.pathParam("moduloId"));
            Modulo modulo = moduloService.buscar(moduloId);

            ctx.render("quiz.html", Map.of("modulo", modulo));
        } catch (NoSuchElementException | NumberFormatException e) {
            ctx.status(404).result("Módulo não encontrado");
        }
    }

    public void obterQuestoes(Context ctx) {
        try {
            int moduloId = Integer.parseInt(ctx.pathParam("moduloId"));
            List<QuizQuestionDTO> questoes = quizService.gerarQuizParaModulo(moduloId);
            ctx.json(questoes);
        } catch (Exception e) {
            ctx.status(500).result("Erro ao gerar quiz: " + e.getMessage());
        }
    }

    public static class QuizSubmission {
        public Map<Integer, String> respostas = new HashMap<>();
        public int vidasRestantes = 3;
        public String usuarioId = "aluno_demo";
    }

    public void finalizarQuiz(Context ctx) {
        try {
            int moduloId = Integer.parseInt(ctx.pathParam("moduloId"));
            QuizSubmission submission = ctx.bodyAsClass(QuizSubmission.class);

            QuizResultDTO resultado = quizService.processarResultado(
                    moduloId,
                    submission.usuarioId != null ? submission.usuarioId : "aluno_demo",
                    submission.respostas,
                    submission.vidasRestantes
            );

            ctx.json(resultado);
        } catch (Exception e) {
            ctx.status(400).result("Erro ao processar quiz: " + e.getMessage());
        }
    }
}

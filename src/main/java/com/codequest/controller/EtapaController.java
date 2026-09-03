package com.codequest.controller;

import com.codequest.model.Etapa;
import com.codequest.model.Modulo;
import com.codequest.service.EtapaService;
import com.codequest.service.ModuloService;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class EtapaController {

    private final ModuloService moduloService;
    private final EtapaService etapaService;

    public EtapaController() {
        this.moduloService = new ModuloService();
        this.etapaService = new EtapaService();
    }

    public EtapaController(ModuloService moduloService, EtapaService etapaService) {
        this.moduloService = moduloService;
        this.etapaService = etapaService;
    }

    public void verModuloDetails(Context ctx) {
        try {
            int moduloId = Integer.parseInt(ctx.pathParam("id"));
            Modulo modulo = moduloService.buscar(moduloId);
            List<Etapa> etapas = etapaService.listarPorModulo(moduloId);

            ctx.render("modulo-detalhes.html", Map.of(
                    "modulo", modulo,
                    "etapas", etapas
            ));
        } catch (NoSuchElementException | NumberFormatException e) {
            ctx.status(404).result("Módulo não encontrado");
        }
    }

    public void verEtapa(Context ctx) {
        try {
            int moduloId = Integer.parseInt(ctx.pathParam("moduloId"));
            int numero = Integer.parseInt(ctx.pathParam("numero"));

            Modulo modulo = moduloService.buscar(moduloId);
            Etapa etapa = etapaService.buscarPorModuloENumero(moduloId, numero);
            List<Etapa> todasEtapas = etapaService.listarPorModulo(moduloId);

            Map<String, Object> model = new HashMap<>();
            model.put("modulo", modulo);
            model.put("etapa", etapa);
            model.put("totalEtapas", todasEtapas.size());
            model.put("etapaAnterior", (numero > 1) ? numero - 1 : null);
            model.put("proximaEtapa", (numero < todasEtapas.size()) ? numero + 1 : null);

            ctx.render("etapa-detalhes.html", model);
        } catch (NoSuchElementException | NumberFormatException e) {
            ctx.status(404).result("Etapa não encontrada");
        }
    }
}

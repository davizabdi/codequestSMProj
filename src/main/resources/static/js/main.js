/**
 * CodeQuest - Efeito de Onda (Ripple Effect) e Indicador de Carregamento nos Botões
 */
document.addEventListener('DOMContentLoaded', () => {

    // 1. EFEITO DE ONDA (RIPPLE EFFECT)
    document.addEventListener('click', (e) => {
        const btn = e.target.closest('.btn, .btn-restart, .btn-next, .option-btn, button:not(.nav-toggle)');
        if (!btn) return;

        // Se o botão já estiver carregando, não dispara outro efeito
        if (btn.classList.contains('btn-loading')) return;

        const rect = btn.getBoundingClientRect();
        const ripple = document.createElement('span');
        ripple.className = 'btn-ripple';

        // Posição do clique relativo ao botão
        const size = Math.max(rect.width, rect.height) * 2;
        const x = e.clientX - rect.left - size / 2;
        const y = e.clientY - rect.top - size / 2;

        ripple.style.width = `${size}px`;
        ripple.style.height = `${size}px`;
        ripple.style.left = `${x}px`;
        ripple.style.top = `${y}px`;

        btn.appendChild(ripple);

        // Remove após terminar a animação CSS
        ripple.addEventListener('animationend', () => {
            ripple.remove();
        });
    });

    // 2. INDICADOR DE CARREGAMENTO NOS FORMULÁRIOS
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', (e) => {
            // Se o formulário for inválido no HTML5, o navegador não submete
            if (!form.checkValidity()) return;

            const submitBtn = form.querySelector('button[type="submit"], input[type="submit"], .btn-primary');
            if (submitBtn && !submitBtn.classList.contains('btn-loading')) {
                // Guarda o conteúdo original
                submitBtn.dataset.originalContent = submitBtn.innerHTML;
                submitBtn.classList.add('btn-loading');
                submitBtn.setAttribute('disabled', 'true');

                // Cria spinner + texto animado
                submitBtn.innerHTML = `
                    <span class="btn-spinner"></span>
                    <span class="btn-loading-text">Processando...</span>
                `;
            }
        });
    });
});

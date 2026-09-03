## Visão geral

O ControlHard permite selecionar regras independentes de balanceamento que entram em vigor na dificuldade Difícil, transformando perigos conhecidos em provações dignas do Cavaleiro Príncipe sem criar uma dificuldade separada nem abandonar a identidade vanilla.

## Opções globais

| Opção | Descrição |
| --- | --- |
| ORDEM ARCANA | Faz a ordenação alfabética do Inventory Profiles Next manter Livro encantado no L e Frasco sombrio no F, usando a descrição apenas para distinguir esses itens. Livros são ordenados pelo nome do encantamento e depois pelo nível; frascos, pelo nível. Níveis romanos seguem o valor numérico, como I, II, III, IV e V. |
| SABER ENCANTADO | Ao segurar Shift, exibe descrição, equipamentos e incompatibilidades em Livros encantados com exatamente um encantamento; livros com vários não recebem dados. Na Mesa de encantamentos, exibe as informações somente com o nível necessário, após os custos e uma linha vazia. Ambos os tooltips possuem largura máxima de 600 px. |
| NOME SOBERANO | Remove o itálico dos nomes personalizados de todos os itens, inclusive os que já estavam renomeados antes da ativação. |
| GRIMÓRIOS SOBERANOS | Mantém estados independentes de aberto ou fechado para o livro de receitas do Inventário e da Bancada de trabalho. Cada escolha persiste entre telas, mundos e reinicializações do jogo. Os demais livros de receitas preservam o comportamento vanilla. |
| LEGADO ETERNO | Preserva 50% dos pontos de experiência atuais após o jogador renascer. Os orbes que o Minecraft normalmente gera permanecem no local da morte sem alteração. A regra keepInventory continua preservando 100% da experiência. |
| ALJAVA INFINITA | Permite disparar arcos encantados com Infinidade sem nenhuma flecha no inventário. Na ausência de munição, o disparo usa uma Flecha comum e preserva o comportamento vanilla do encantamento. |
| ÉGIDE MAGNÉTICA | Protege os drops processados pelo Magnetic contra falhas de compatibilidade. Se uma exceção interromper a coleta magnética, o item permanece no chão e o erro é registrado no log. Não altera a coleta quando o Magnetic conclui o processamento normalmente. |
| PASSAGEM SOBERANA | Usar uma Bússola vinculada a uma Magnetita teleporta o jogador exatamente para cima da Magnetita correspondente, inclusive entre dimensões. O teleporte exige 15 Esmeraldas no inventário e consome todas elas ao acontecer. |
| SALTO COROADO | Substitui o teleporte vanilla da Fruta do coro por um destino aleatório na dimensão atual, a até 5.000 blocos da posição de consumo. Gera ou carrega o chunk antes da busca e exige piso sólido e não perigoso, sem líquidos nem colisões no espaço do corpo e da cabeça. No Nether, busca abaixo do teto; no End, exige terreno de uma ilha. Após 32 tentativas inválidas, a fruta é consumida sem teleporte. |
| AURORA SOBERANA | Mantém a iluminação, as cores e a neblina do tempo limpo durante chuva e tempestade, incluindo céu, nuvens e astros. Suprime os clarões dos raios. Preserva o ciclo de dia e noite, a luz dos blocos, a precipitação, os sons e as mecânicas do clima. Aplica-se à renderização vanilla. |
| TORRENTE DOMADA | Reduz em 40% a densidade média dos riscos de chuva e a quantidade de respingos, mantendo 60% dos efeitos vanilla durante chuva e tempestade. Preserva neve, sons, iluminação e mecânicas do clima. Funciona junto de AURORA SOBERANA. |
| COLHEITA COROADA | Concede 30% de chance de obter 1 Trigo extra ao colher Trigo completamente crescido com uma ferramenta encantada com Fortuna III. |
| RUBI GENEROSO | Concede 30% de chance de obter 1 Beterraba extra ao colher Beterraba completamente crescida com uma ferramenta encantada com Fortuna III. |
| DOMÍNIO INVIOLÁVEL | Impede Abóboras e Melancias de nascerem sobre Terra Arada, com ou sem plantação. Os frutos continuam crescendo normalmente sobre os demais solos permitidos pelo Minecraft. |
| VISÃO SOBERANA | Enquanto o jogador usa uma Luneta, destaca através dos blocos baús de Tesouro enterrado, Areia suspeita e Cascalho suspeito situados em um raio de 20 blocos. O destaque desaparece imediatamente ao interromper o uso. Remove totalmente o zoom da Luneta e preserva a sensibilidade padrão do mouse. |
| DESTINOS INÉDITOS | Faz novos Mapas do tesouro enterrado ignorarem Buried Treasures já escolhidos por outros mapas no mesmo mundo. A reserva ocorre quando o saque que contém o mapa é gerado e vale para todos os jogadores. Se não houver destino disponível no raio vanilla de 50 chunks, o item permanece um mapa vazio com nome de mapa do tesouro. |

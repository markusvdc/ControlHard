# Migração para Minecraft 26.3 — ControlHard

Data: 2026-09-18. Build final concluído com Temurin 25.0.3+9 e Gradle 9.6.0.

Dependências: Loader 0.19.5, Fabric API 0.160.7+26.3 e Mod Menu 21.0.0-beta.1. Loom 1.17.14 e versão 1.0 preservados.

Adaptações: constantes do mouse e nova assinatura de tooltip; assinatura da quebra de blocos; setter de invulnerabilidade e swingForAttack; critério de piso do teleporte reproduzido a partir da implementação antiga de blocksMotion (isSolid, exceto teia e muda de bambu), mantendo as demais proteções. Uso de isSolid produz aviso de API obsoleta, sem impedir o build.

Controles de dificuldade migrados para WorldOptionsScreen.DifficultyButtons com access widener apenas da classe. Luneta adaptada aos pacotes RenderPearl e à montagem de linhas da renderização vanilla 26.3; busca de estruturas por Holder. Mapas de tesouro usam HolderSet mantendo a identificação da tag; assinatura de geração de monstros atualizada; neblina agora usa Vector3fc.

Validação: clean build --warning-mode all, validateAccessWidener e auditoria estática do JAR com 91 verificações de seletores, parâmetros de callbacks, campos, instruções e constantes, sem divergências detectadas nos pontos verificados. Isso não substitui aplicação dos mixins em execução. Minecraft não foi iniciado. Integrações opcionais Inventory Profiles Next e Magnetic não foram validadas com seus JARs 26.3.

Artefato: build/libs/ControlHard-1.0.jar. Instalação adiada até a atualização da NEBULOSA. Nenhum commit, push ou release. Alterações locais anteriores, inclusive remoção das regras de invasão, preservadas. AGENTS.md preservado, com versões compartilhadas ainda da 26.2.

Teste manual pendente: menus e dificuldade; tooltips e rolagem em GUI Scale 2x; chão seguro do teleporte; luneta e contornos através de blocos; chuva, céu e neblina; mapas de tesouro sem repetição; geração de bruxas/Endermen; combate e imunidade de montaria; folhas, alimentos e demais regras habilitadas.

Fontes: https://www.fabricmc.net/2026/09/15/263.html ; https://feedback.minecraft.net/hc/en-us/articles/48913133328013-Minecraft-Java-Edition-26-3 ; APIs Fabric/Modrinth e classes/dados 26.2/26.3 resolvidos pelo Loom.

Logs desta etapa: build/reports/migration-26.3/build.log e static-audit.txt. JSONs empacotados verificados sintaticamente.

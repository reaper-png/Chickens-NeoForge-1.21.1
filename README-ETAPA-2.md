# Chickens (CreeperHost 1.0.32) -> NeoForge 1.21.1 — Etapa 2 (WIP)

Base: somente o mod original **CreeperHost Chickens 1.0.32**, licença MIT.
Nenhum código ou recurso de Modern Chickens foi incorporado.

## O que mudou

- Corrigidos os diretórios de texturas do atlas da versão 1.21.1 (`textures/blocks` -> `textures/block`, `textures/items` -> `textures/item`) e todos os modelos JSON relacionados.
- As 89 texturas PNG do JAR original foram verificadas com SHA-256 e permanecem byte a byte idênticas. Há mais um alias de textura para um modelo legado de Roost.
- O catálogo imutável contém exatamente 46 galinhas e seus parentes, 46 loot tables e 46 skins originais.
- Registradas as 46 entidades sob seus IDs `chickens:*` originais.
- Adaptados os dados NBT legados `ChickenType.id`, `gain`, `growth` e `strength` para `CUSTOM_DATA` do ItemStack 1.21.1.
- Port inicial de spawn via `chicken_item`/`spawn_egg`, reprodução, produção de recursos, captura pelo Catcher e Analyzer.
- Renderizador de entidade reutiliza o modelo vanilla Chicken e lê a textura de cada tipo original.
- A aba Chickens lista 46 galinhas e 46 ovos de spawn, além dos itens e blocos originais.
- Novos testes de catálogo e genética.

## ATENÇÃO: ainda não é versão de produção

**Esta etapa NÃO foi compilada contra os JARs reais do NeoForge neste ambiente.**
O Java 21 compilou as classes de domínio isoladas e passou em 1.000 cruzamentos simulados, mas faltam o build completo e o teste no jogo. APIs NeoForge podem exigir ajustes adicionais.

**Ainda pendente:** as lógicas reais de Roost, Breeder, Henhouse, inventários e interfaces; ovos coloridos arremessáveis; líquidos dos ovos, configurações JSON antigas, spawn natural por bioma, integração JEI/TOP e renderer customizado 3D do item galinha. A implementação de entidades e seus drops ainda precisa de homologação. **Não teste em mundo antigo sem backup.**

## Aplicação

1. Faça backup do diretório atual `chickensNeo` e do seu mundo.
2. Extraia o conteúdo deste ZIP **diretamente na raiz do projeto Gradle** (`C:\Users\Allan\Desktop\chickensNeo`), substituindo arquivos.
3. Remova os diretórios antigos `src/main/resources/assets/chickens/textures/blocks` e `textures/items` se ainda existirem. O ZIP já contém os diretórios singulares corretos.
4. Execute `gradle clean build` com Java 21.
5. Se passar, coloque **somente o JAR** de `build/libs` em um perfil NeoForge 1.21.1 de teste. Não deixe a versão anterior do port junto.
6. No criativo, confirme os blocos com texturas e teste os nomes das galinhas, spawn eggs, reprodução e captura. Veja eventuais erros no `latest.log`.

### Resultado da verificação estática

- 89 texturas originais preservadas byte a byte;
- 27 modelos JSON analisados, sem referências locais diretas ausentes;
- 46 nomes de galinhas = 46 loot tables = 46 arquivos de skin;
- catálogo puro Java compilado com Java 21 e 1000 cruzamentos simulados.

Este port continua em desenvolvimento incremental. A validação definitiva depende da compilação Gradle e do Minecraft executando no seu computador.

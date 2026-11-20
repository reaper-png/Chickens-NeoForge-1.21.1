# Chickens 1.0.32 — NeoForge 1.21.1 | Etapa 5 (WIP)

Base: **port direto do CreeperHost Chickens 1.0.32**. Não usa Modern Chickens.
Aplicar sobre a versão `Chickens-1.21.1-GALINHAS-3D-NEOFORGE-API-FIX-PROJETO.zip`.

## Alterações

### Water Egg / Lava Egg

- `chickens:liquid_egg` agora é um `ItemLiquidEgg`, não um item vazio.
- O campo **legado** `id` em `CUSTOM_DATA` distingue `minecraft:water` e `minecraft:lava`.
- Os drops naturais e a saída do Roost das respectivas galinhas continuam passando pelo mesmo `ChickenProducts.produce`, portanto saem com `id` preenchido.
- Ovos de água e lava ficam acessíveis no final da aba *Chickens*, **separados dos itens de galinha 3D**.
- Capacidade: **1.000 mB**, drenagem só de uma unidade completa (nunca parcial), consumo de **um ovo por transferência efetiva**.
- Tanques de outros mods usam `Capabilities.FluidHandler.ITEM` via `IFluidHandlerItem`.
- Uso no chão delega ao `FluidUtil.tryPlaceFluid(IFluidHandler, ...)` do NeoForge para respeitar regras de colocação e vaporização em dimensão quente. O método trabalha diretamente sobre a pilha da mão, sem sobrescrever os ovos restantes.
- Nomes `Water Egg` / `Lava Egg`; `Ovo de Água` / `Ovo de Lava` em PT-BR.

### Roost / Breeder — barra de progresso

- Restabelecido o sprite de avanço do **GUI original** (`x=176, y=0` na textura), com as coordenadas originais: Roost `(48,20)`, Breeder `(84,22)`.
- Progresso sincronizado pelo `ContainerData` já existente; barra de 0–26 pixels para 0–1.000 pontos.
- Exibe porcentagem no tooltip ao passar o mouse sobre a barra.
- Sem alterar inventários, NBT, modelos ou texturas de galinhas.

### Integração

- Corrigido o caso de cruzamento sem descendente, que poderia acessar um `ChickenSpecies` nulo.

## Instalar/testar

1. Faça backup da pasta `chickensNeo` e do mundo de teste.
2. Extraia o ZIP **incremental** na raiz do projeto (`C:\Users\Allan\Desktop\chickensNeo`) preservando caminhos de pasta.
3. Rode `gradle clean build` (Java 21).
4. Apague o JAR de teste anterior de `mods` e copie o novo de `build/libs`.
5. No criativo, procure Water Egg e Lava Egg no final da aba Chickens. Valide:
   - Water Chicken e Lava Chicken, livres ou no Roost, produzem o ovo correto.
   - Clique com Water Egg em terreno vazio: uma fonte de água é colocada e **somente um ovo consumido**.
   - Clique com Lava Egg: fonte de lava; no Nether, valide as regras de vaporização do NeoForge.
   - Em tanque que exponha `Capabilities.FluidHandler.BLOCK`, um ovo transfere exatamente **1.000 mB**. O teste depende do comportamento do tanque, portanto teste mais de um mod se necessário.
   - Com 16 ovos na mão, cada interação de sucesso deve reduzir a pilha para 15, 14, etc.; falhas não devem consumir ovos.
   - Roost e Breeder: com ingredientes válidos, a seta enche e o tooltip mostra progresso em %; sem ingredientes pode permanecer zerado.
   - Feche e reabra telas; verifique atualização de progresso e salvamento após reiniciar o mundo.
6. Se houver erro, envie o `compileJava`, `latest.log` ou as capturas — não tente corrigir com outro loader.

## Testes executados aqui

- Java 21 puro: **4.037** asserções para os tipos líquidos e o percentual da barra.
- Java 21 + **stubs**: **35** asserções simuladas do `IFluidHandlerItem` (incluindo simulação, transferência e consumo de pilha).
- JSON de idioma e modelos de item: todos parseados.
- Análise da implementação **oficial** de `FluidUtil` do ramo NeoForge 1.21.1 para escolher a sobrecarga que não apaga a pilha restante.
- **Não** foi possível executar `gradle build` com dependências reais do NeoForge nem iniciar um servidor de teste neste ambiente. Resultado em jogo e com tanques de terceiros **não homologado**.

### Dívidas do port

Compatibilidade total de saves antigos, sistema de spawn natural, receitas/JEI e testes integrados em cliente e servidor dedicados ainda precisam de revisão. Esta etapa não afirma que o port inteiro está concluído.

# Chickens 1.0.32 / NeoForge 1.21.1 — correção dos itens de galinha

## O problema
A renderização anterior de `chickens:chicken_item` usava `models/item/chicken_item.json` com as texturas de um ovo. Esse item representa uma **galinha capturada**, não um ovo.

## O que foi alterado
- `chicken_item` agora usa `minecraft:builtin/entity` e um renderizador de item específico (`ChickenItemRenderer`) com o modelo 3D original de galinha do Minecraft e a textura da variante de `assets/chickens/textures/entity/<id>.png`.
- O renderizador é registrado **somente** para `chickens:chicken_item`. Os ovos `spawn_egg` e `colored_egg` continuam usando seus próprios modelos 2D.
- A aba `Chickens` apresenta apenas as 46 galinhas capturadas e ferramentas/blocos. Os ovos não são mais listados nessa aba, mas continuam registrados para uso via outras mecânicas ou comandos.
- Não altera as entidades no mundo, seus atributos, Roost, Breeder, Henhouse, inventários ou texturas PNG.
- Cache de modelo é limpo no recarregamento dos pacotes de recursos.

## Aplicação
Extraia o conteúdo do ZIP incremental na **raiz do projeto** (`C:\Users\Allan\Desktop\chickensNeo`), mesclando as pastas `src/` e sobrescrevendo os arquivos. O ZIP completo é alternativa se quiser trocar todo o projeto.

Compile com:

```powershell
gradle clean build
```

Copie apenas o JAR de `build/libs` para a pasta `mods` do NeoForge 1.21.1, removendo a versão antiga do port. Teste em um mundo separado.

## Verificações desta entrega
- `ChickenItemRenderer.java`: compilação de sintaxe com Java 21 e classes stub: OK.
- 46/46 texturas das espécies presentes: OK.
- 91/91 texturas PNG comparadas por SHA-256 com a entrega anterior: idênticas.
- O modelo de `chicken_item` é de entidade; os modelos de ovos permanecem 2D: OK.
- Aba criativa sem ovos: OK.

**Limite:** o build Gradle com NeoForge real e o teste visual no cliente não puderam ser executados neste ambiente (dependências indisponíveis). Se o compilador apresentar incompatibilidade da API de renderização, envie a saída completa. Caso o ícone 3D fique deslocado ou pequeno, o ajuste será somente nas transformações do renderizador.

# Chickens — NeoForge 1.21.1

Port não oficial do mod **Chickens** 1.0.32 (Minecraft 1.18.2) para **Minecraft 1.21.1 / NeoForge / Java 21**.

## Objetivo

Preservar a experiência e os identificadores do mod original, com compatibilidade funcional na 1.21.1. Referência original: CreeperHost Chickens 1.0.32.

## Estado do port

- Migração e registro do catálogo de 46 espécies e recursos associados.
- Migração de receitas, reprodução, crescimento e produção.
- Adaptação de dados NBT para os componentes da versão atual.
- Ovos líquidos, Roost e Breeder: implementações e correções em teste.
- Renderização 3D de galinhas em itens e no Roost: ajustes recentes, pendentes de integração com a cópia local definitiva.
- **Etapa 6:** spawn natural, em desenvolvimento / validação.

> **Atenção:** este repositório está em fase de inicialização. O código-fonte completo ainda precisa ser enviado a partir da cópia local mais recente. Este README não significa que o port já esteja pronto para publicação.

## Organização sugerida

O projeto deve conter `src/main/java`, `src/main/resources`, `build.gradle`, `settings.gradle`, `gradle.properties` e, quando disponível, o Gradle Wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper`).

Não versionar as pastas `build/`, `.gradle/` ou `run/`, nem JARs gerados. Usar commits separados por etapa e testar cada alteração antes de consolidar na `main`.

## Créditos e licença

Projeto derivado do Chickens original da CreeperHost. Preservar os avisos e a licença do projeto original nos arquivos relevantes.

![Coverage](https://img.shields.io/codecov/c/github/AmineSidki/Sprout)
![Java Version](https://img.shields.io/badge/Java-17%2B-blue)


# Sprout

**Sprout** is a small, focused CLI tool that generates the boring parts of a Spring Boot app from your JPA `@Entity` classes. Point it at your entity package and it will produce repositories, services, DTOs (as records), projections, MapStruct mappers, controllers and a shared `ResourceNotFoundException`, so you can spend time on business logic, not boilerplate.


## Changelog

### 2.0

- **New `sprout init` command.** Generates the Sprout annotations (`@SproutProjection`, `@SproutLargeDataField`, `@SproutPaginated`, `@SproutCached`, `@SproutIgnore`) as real `@interface` classes in a `sproutannotation` package of your project, so your IDE can resolve and autocomplete them. No generation happens in this mode.
- **New `sprout clean` command.** Deletes the `sproutannotation` package that `init` created.
- **Entity package auto-detection.** Sprout now looks for `entity`, `entities`, `domain`, `model` or `models` instead of only `entity`, and the generated code's imports and base package follow the package it found.
- **Safe partial updates.** Mappers gain an `updateEntityFromDto` method configured with `NullValuePropertyMappingStrategy.IGNORE`, and the generated `update` loads the existing entity and patches it, so `null` fields in the DTO no longer overwrite stored values.
- **Reworked services.** Lookups go through a single private `getElementById` helper; `save`, `update` and `delete` are shorter and read the same way across every entity.
- **Controllers return `ResponseEntity`**, and updates are now `PUT /{id}` with the id in the path instead of being read from the body.
- **One shared `ResourceNotFoundException`** replaces the per-entity `{Entity}Exception` classes, and it is generated once per run instead of once per entity.
- **`HELP.md` is generated in your project**, listing the dependencies the generated code needs (Spring Data JPA, Validation, Lombok, MapStruct, a database driver) and the Lombok + MapStruct annotation-processor setup.
- **New `sprout --help` / `-h` table now covers every command and flag.**


## Quick highlights

- Parses your Java source with **JavaParser** (symbol resolution included).
- Produces type-safe DTO records, projections and MapStruct mappers with automatic dependency wiring for associations.
- Supports partial generation (generate only DTOs + mappers, or any subset you want).
- Distributed as a zero-config binary (wrapper scripts + embedded JRE) via Homebrew and Scoop.
- Built with Picocli, Mustache.java templates and packaged with JReleaser.


## Table of Contents
- [Install](#install)
- [Quick Start](#quick-start)
- [Commands and flags](#commands-and-flags)
- [Annotations](#annotations)
- [What gets generated](#what-gets-generated)
- [How it works](#how-it-works)
- [FAQ](#faq)
- [Contributing](#contributing)
- [License](#license)


## Install

### macOS / Linux (Homebrew)

```bash
brew tap AmineSidki/homebrew-sprout
brew install sprout
```

### Windows (Scoop)

```powershell
scoop bucket add sprout https://github.com/AmineSidki/scoop-sprout.git
scoop install sprout
```

### From source

```bash
git clone git@github.com:AmineSidki/Sprout.git
cd Sprout/Sprout
mvn clean package
```


## Quick start

Run Sprout in the root of your project (or pass `--dir` to point it elsewhere). Your entities must live in a package named `entity`, `entities`, `domain`, `model` or `models`.

```bash
# (optional) generate the annotations so your IDE can resolve them
sprout init

# generate everything
sprout

# remove the generated annotations when you no longer need them
sprout clean
```

Partial generation example, only DTOs and mappers:

```bash
sprout -p -d -m
```

After the first run, open the generated `HELP.md` to make sure your `pom.xml` has everything the generated code needs.


## Commands and flags

| Command | What it does |
| :------ | :----------- |
| `sprout` | Generate code from your entities |
| `sprout init` | Generate the Sprout annotations into a `sproutannotation` package |
| `sprout clean` | Delete the `sproutannotation` package |

| Flag | Short | What it does |
| :--- | :---- | :----------- |
| `--dir` | | Target directory (defaults to `.`) |
| `--partial` | `-p` | Enable partial generation mode |
| `--repository` | `-r` | Generate repositories (needs `-p`) |
| `--service` | `-s` | Generate services (needs `-p`) |
| `--dto` | `-d` | Generate DTOs, Java records (needs `-p`) |
| `--mapper` | `-m` | Generate MapStruct mappers (needs `-p`) |
| `--controller` | `-c` | Generate REST controllers (needs `-p`) |
| `--exception` | `-e` | Generate `ResourceNotFoundException` (needs `-p`) |
| `--help` | `-h` | Show the help table |
| `--version` | `-v` | Show version |


## Annotations

Sprout reads a set of marker annotations directly from your entity source to drive generation. Run `sprout init` to generate them into your project so your IDE can resolve them. Sprout never strips the annotations from your entities, so removing them is your responsibility. `sprout clean` deletes the package, so remove the annotations (and their imports) from your entities first, otherwise they will no longer compile.

**`@SproutProjection`** — Tells Sprout to generate a separate `{Entity}Projection` record alongside the standard DTO. The projection is placed in its own `projection/` package and the mapper gains a `toProjection()` method. The service and controller `findAll` endpoints return the projection type instead of the full DTO, which is useful when you want a leaner read model without exposing every field.

**`@SproutLargeDataField`** — Marks a field that should be excluded from the projection. Use this on fields like `byte[]` blobs or long text columns that you never want in a list view.

**`@SproutPaginated`** — Generates paginated `findAll` variants in the service and controller in addition to the standard list endpoint.

**`@SproutCached`** — Accepted by the generator, but it does not change the generated output yet. Caching support is planned.

**`@SproutIgnore`** — Tells Sprout to skip this entity entirely. Entities marked with this annotation are filtered out before generation begins, so they don't produce any output even in full-generation mode.


## What gets generated

For each entity `Foo` found in your entity package:

| Layer | Output |
| :---- | :----- |
| Repository | `FooRepository` |
| DTO | `FooDto` record (plus `FooProjection` with `@SproutProjection`) |
| Mapper | `FooMapper` (MapStruct): `toDto`, `toEntity`, `updateEntityFromDto` |
| Service | `FooService` with `findAll`, `findById`, `save`, `update(id, dto)`, `delete` |
| Controller | `FooController` under `/api/v1/foo`, returning `ResponseEntity` |

Generated once per run, not per entity:

- `ResourceNotFoundException`, thrown by the services when an id doesn't exist.
- `HELP.md`, the dependency and annotation-processor checklist for your `pom.xml`.


## How it works

Sprout runs like a tiny compiler across five stages:

1. **Resolution** — finds your entity package (`entity`, `entities`, `domain`, `model` or `models`) and computes the base package for generated code.
2. **AST Analysis** — parses `.java` files with JavaParser; builds `EntityMetadata` objects (fields, ID, associations, annotation flags).
3. **Metadata Mapping** — resolves Java types and maps Hibernate associations to internal enums for generation logic.
4. **Template Orchestration** — picks which layers to generate, wires import and dependency generators into file generators.
5. **Emission** — executes Mustache templates through `FileCreator` and writes the generated `.java` files into `dto/`, `projection/`, `service/`, `repository/`, etc.

This pipeline is intentionally modular so you can add more generators or tweak templates without touching parsing logic.

```mermaid
flowchart LR
    A[Entity Source Code] --> B[AST Parsing]
    B --> C[Metadata Mapping]
    C --> D[Template Engine]
    D --> E[Generated Layers]
```


## Project layout (templates)

Templates live under `src/main/resources/templates/` and include:

- `RepositoryTemplate.mustache`
- `ServiceTemplate.mustache`
- `DtoTemplate.mustache`
- `MapperTemplate.mustache`
- `ControllerTemplate.mustache`
- `ExceptionTemplate.mustache`
- `AnnotationTemplate.mustache` (used by `sprout init`)
- `HelpMdTemplate.mustache`

The templates expect the metadata records produced by the parser (`EntityMetadata`, `FieldMetadata`, `TypeMetadata` and so on). The DTO template doubles as the projection template: the `isProjection` flag switches the package, class name suffix and Javadoc header accordingly.


## Design notes

- **Smart imports** — generators add `java.util.List/Set` or primary key types only when needed.
- **Mapper dependencies via `@Autowired`** — mappers are abstract classes, and MapStruct doesn't support constructor injection for them, so association lookups are wired with field injection.
- **Null-safe updates** — updates patch the loaded entity through MapStruct and ignore `null` DTO properties.
- **Strict ID resolution** — an `@Id` annotated field is required to determine the repository's primary key type.
- **Ignored entities are filtered early** — `@SproutIgnore` entities are dropped before generation starts, so no generator ever sees them.
- **Zero-config distribution** — wrapper scripts (`sprout`, `sprout.bat`) include an embedded JRE, so users don't need to set the classpath.


## Examples

Generate repository + service only:

```bash
sprout -p -r -s
```

Generate controller and the shared exception:

```bash
sprout -p -c -e
```

Target a specific directory:

```bash
sprout --dir /path/to/my/project
```


## Templates & customization

If you want different code style or method signatures, edit the Mustache templates under `src/main/resources/templates/`. Because templates receive the same metadata records the generators use, changes there let you reshape the output without touching parsing or generator code.

## FAQ
- _Why AST instead of reflection ?_
>Sprout analyzes your actual Java source files using JavaParser, not compiled bytecode and not reflection.
>That decision is intentional.
>
>- No need to compile first.
>
>- No classpath gymnastics.
>
>- No runtime surprises.

>By working directly on the AST, Sprout understands structure, annotations, associations, and types exactly as they are written, before the JVM ever gets involved.
>
>_This keeps generation deterministic and transparent_

- _Why compile-time generation instead of runtime-magic ?_
>Sprout generates real, explicit Java code.
>It does not introduce proxies, dynamic behavior, or hidden wiring.
>
>Once generation is complete, what you see is what you own.
>
>There is:
>
>- No runtime dependency on Sprout.
>
>- No hidden reflection.
>
>- No framework lock-in.

- _Why MapStruct ?_
>MapStruct takes the concept of Lombok annotations and applies it to mappers, giving you intelligent mapping code that won't break easily and that supports multiple output formats — full DTO, projection, entity — without the heavy lifting of classical mappers.
>Unlike reflection-based mappers, MapStruct generates plain Java classes.
>
>There is:
>
>- No reflection
>
>- No dynamic proxies
>
>- No runtime performance penalty

>The generated mapper code is as fast as hand-written mapping logic.

## Contributing

- Fork, make a branch, open a pull request.
- Keep changes modular: prefer adding a new generator or template over modifying core parsing behavior.
- Update or add tests that cover parsing and generation outcomes.

If you want help integrating a new feature (new template layer, an alternative mapper strategy, etc.) open an issue describing which pipeline step you want to change.


## Releases

Releases are handled by JReleaser (GitHub Actions); artifacts include a ZIP with `bin/` (scripts), `lib/` (jar), and `jre/` (embedded runtime). Use the GitHub Releases tab for the full history. The changelog at the top of this file covers the highlights of the current major version.


## License

This project is licensed under the MIT License — see the `LICENSE` file.

# Sprout

**Sprout** is a small, focused CLI tool that generates the boring parts of a Spring Boot app from your JPA `@Entity` classes. Point it at your `entity` package and it will produce repositories, services, DTOs (as records), MapStruct mappers, controllers and convenient NotFound exceptions — so you can spend time on business logic, not boilerplate.

---

## Quick highlights

- Parses your Java source with **JavaParser** (symbol resolution included).
- Produces type-safe DTO records and MapStruct mappers with automatic dependency wiring.
- Supports partial generation (generate only DTOs + mappers, or any subset you want).
- Distributed as a zero-config binary (wrapper scripts + embedded JRE) via Homebrew and Scoop.
- Built with Picocli, Mustache.java templates and packaged with JReleaser.

---

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
git clone <your-repo>
mvn clean package
```

---

## Quick start

Run Sprout in the root of your project (or pass `--dir` to point it elsewhere):

```bash
# generate everything
sprout
```

Partial generation example — only DTOs and mappers:

```bash
sprout -p -d -m
```

---

## CLI options

| Flag | Short | What it does |
| --- | --- | --- |
| `--dir` |  | Target directory (defaults to `.`) |
| `--partial` | `-p` | Enable partial generation mode |
| `--repository` | `-r` | Generate repositories |
| `--service` | `-s` | Generate services |
| `--dto` | `-d` | Generate DTOs (Java records) |
| `--mapper` | `-m` | Generate MapStruct mappers |
| `--controller` | `-c` | Generate REST controllers |
| `--exception` | `-e` | Generate NotFoundException classes |
| `--version` |  | Show version |

---

## How it works (high level)

Sprout runs like a tiny compiler across five stages:

1. **Resolution** — finds your `/entity` package and computes the base package for generated code.
2. **AST Analysis** — parses `.java` files with JavaParser; builds `EntityMetadata` objects (fields, ID, associations).
3. **Metadata Mapping** — resolves Java types and maps Hibernate associations to internal enums for generation logic.
4. **Template Orchestration** — picks which layers to generate, wires import/dependency generators into file generators.
5. **Emission** — executes Mustache templates and writes the generated `.java` files into `dto/`, `service/`, `repository/`, etc.

This pipeline is intentionally modular so you can add more generators or tweak templates without touching parsing logic.

---

## Project layout (templates)

Templates live under `src/main/resources/templates/` and include:

- `RepositoryTemplate.mustache`
- `ServiceTemplate.mustache`
- `DtoTemplate.mustache`
- `MapperTemplate.mustache`
- `ControllerTemplate.mustache`
- `ExceptionTemplate.mustache`

The templates expect metadata records (e.g. `EntityMetadata`, `FieldMetadata`, `TypeMetadata`) produced by the parser.

---

## Design notes & features

- **Smart imports** — generators add `java.util.List/Set` or primary key types only when needed.
- **Mapper dependency injection** — mapper generators scan imports and automatically add `@Autowired` repository fields for MapStruct mappers.
- **Strict ID resolution** — an `@Id` annotated field is required to determine the repository’s primary key type.
- **Zero-config distribution** — wrapper scripts (`sprout`, `sprout.bat`) include an embedded JRE, so users don’t need to set the classpath.

---

## Examples

Generate repository + service only:

```bash
sprout -r -s
```

Generate controller and custom exceptions:

```bash
sprout -c -e
```

Target a specific directory:

```bash
sprout --dir /path/to/my/project
```

---

## Templates & customization

If you want different code style or method signatures, edit the Mustache templates under `src/main/resources/templates/`. Because templates receive the same metadata records the generators use, changes there let you reshape the output without touching parsing or generator code.

---

## Contributing

- Fork, make a branch, open a pull request.
- Keep changes modular: prefer adding a new generator or template over modifying core parsing behavior.
- Update or add tests that cover parsing/generation outcomes.

If you want help integrating a new feature (new template layer, an alternative mapper strategy, etc.) tell me which pipeline step you want to change and I’ll outline the implementation.

---

## Changelog & Releases

Releases are handled by JReleaser (GitHub Actions); artifacts include a ZIP with `bin/` (scripts), `lib/` (jar), and `jre/` (embedded runtime). Use the GitHub Releases tab to check what's new.

---

## License

This project is licensed under the MIT License — see the `LICENSE` file.
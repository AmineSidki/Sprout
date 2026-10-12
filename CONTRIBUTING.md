# Contributing to Sprout

Thanks for your interest in Sprout! Sprout is intentionally opinionated: it generates idiomatic Spring Boot boilerplate and isn't meant to be a general-purpose, configurable tool.


## Ways to Contribute

**Bug reports** — open an issue with what happened vs. what you expected, a minimal entity that reproduces it, the command you ran with the full output, and your OS and Java version.

**Feature requests** — welcome if they fit the opinionated philosophy above. Requests that add heavy configuration or widen the scope are unlikely to be accepted. Explain the problem you're solving and how you'd expect it to work.

**Template contributions** — the generated code style lives in the Mustache templates under `Sprout/src/main/resources/templates/`. Better idioms and cleaner output are very welcome. Each template receives the variables below from its generator:

| Template | Variables |
|---|---|
| `RepositoryTemplate` | `PackageName`, `EntityPackageName`, `ClassName`, `IdType`, `Imports` |
| `ServiceTemplate` | `PackageName`, `EntityPackageName`, `ClassName`, `className`, `IdType`, `Id`, `Paginated`, `hasProjection`, `Imports` |
| `ControllerTemplate` | `PackageName`, `ClassName`, `className`, `IdType`, `Paginated`, `hasProjection`, `Imports` |
| `DtoTemplate` | `PackageName`, `ClassName`, `IdType`, `Fields`, `Imports`, `isProjection` (projection output only) |
| `MapperTemplate` | `PackageName`, `EntityPackageName`, `ClassName`, `IdType`, `Fields`, `Associations`, `Dependencies`, `Imports`, `hasProjection` |
| `ExceptionTemplate` | `PackageName` |
| `AnnotationTemplate` | `PackageName`, `ClassName`, `ElementType` |
| `HelpMdTemplate` | none |

**Core pipeline** — the flow is `entity sources → AST (JavaParser) → metadata records → generators → output files`. The key abstractions:

- `SproutParser<T>` parses a `CompilationUnit` into a metadata record.
- `SproutImportsGenerator` and `SproutDependencyGenerator` compute imports and injected dependencies.
- `SproutFileGenerator` writes one output file per entity from a Mustache template.
- `SproutOncePerPipelineGenerator` writes files needed only once per run (`ResourceNotFoundException`, `HELP.md`).

To add a new layer, implement one of the two generator interfaces and register it in `GenerationHandlerInitializer`. Follow the existing generators as a reference.


## Building and Testing

Requirements: Java 17 and Maven 3.8+.

```bash
git clone https://github.com/AmineSidki/Sprout.git
cd Sprout/Sprout
mvn clean install
mvn test
```

To try your build, run the jar from `target/` with `--dir` pointing at the directory that contains your entity package (`entity`, `entities`, `domain`, `model` or `models`).

The tests cover the parsing layer and utilities, using the fixture entities in `src/main/resources/test-entities/`. The generators aren't covered yet, so tests for them are welcome.


## Code Conventions

- Packages are all lowercase; models are immutable records.
- Use constructor injection with Lombok's `@RequiredArgsConstructor`. The one exception is generated mappers, which use `@Autowired` because MapStruct doesn't support constructor injection.
- Exceptions must carry a meaningful message that names the entity and what failed.
- Javadoc is required on public interfaces, records and non-trivial utilities.
- Keep formatting consistent with the surrounding code: 4-space indentation, no new wildcard imports.


## Customization

If you want a different mapper library or controller style, fork the repository. Import and dependency generation live in the Java layer, so a fork gives you control over both the templates and the pipeline.

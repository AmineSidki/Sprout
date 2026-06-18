# Contributing to Sprout

Thank you for your interest in Sprout! This document covers everything you need to know to contribute effectively.


## Table of Contents

- [Ways to Contribute](#ways-to-contribute)
- [Building Locally](#building-locally)
- [Running Tests](#running-tests)
- [Code Conventions](#code-conventions)


## Ways to Contribute

### Bug Reports

If you encounter unexpected behavior, please open an issue with:

- A clear description of what happened vs. what you expected
- The entity class(es) you ran Sprout against (a minimal reproduction is ideal)
- The command you ran and the full error output
- Your OS and Java version

### Feature Requests

Sprout is intentionally opinionated — it generates idiomatic Spring Boot boilerplate and doesn't aim to be a general-purpose configurable tool. Feature requests that align with that philosophy are welcome. Requests that would significantly expand the scope or add heavy configuration are unlikely to be accepted, but discussion is always open.

When opening a feature request, please explain:

- What problem you're trying to solve
- How you'd expect it to work from a user's perspective
- Whether you'd be willing to implement it yourself

### Template Contributions

Sprout's generated code style lives in the Mustache templates under `src/main/resources/templates/`. If you think the generated output could be improved — better idioms, cleaner structure, edge cases handled more gracefully — template contributions are very welcome.

Each template receives a specific data context from its generator. The available variables for each template are:

| Template | Key variables |
|---|---|
| `RepositoryTemplate` | `PackageName`, `ClassName`, `IdType`, `Imports` |
| `ServiceTemplate` | `PackageName`, `ClassName`, `className`, `IdType`, `Id`, `hasLightDTO`, `Imports` |
| `ControllerTemplate` | `PackageName`, `ClassName`, `className`, `IdType`, `hasLightDTO`, `Imports` |
| `DtoTemplate` | `PackageName`, `ClassName`, `IdType`, `Fields`, `Imports` |
| `MapperTemplate` | `PackageName`, `ClassName`, `IdType`, `hasLightDTO`, `Fields`, `Associations`, `Dependencies`, `Imports` |
| `ExceptionTemplate` | `PackageName`, `ClassName` |

### Core Pipeline Contributions

The generation pipeline follows a strict flow:

```
Entity source files → AST (JavaParser) → Metadata records → Generators → Output files
```

The key abstractions are:

- `SproutParser<T>` — parses a `CompilationUnit` into a metadata record
- `SproutImportsGenerator` — computes the import set for a given entity
- `SproutDependencyGenerator` — computes injected dependencies (currently mapper only)
- `SproutFileGenerator` — writes the final output file using a Mustache template

If you want to add a new generated layer (e.g. a new file type), implement `SproutFileGenerator` and wire it up in `GenerationHandlerInitializer`. Follow the existing generator implementations as reference.


## Building Locally

**Requirements:**
- Java 17
- Maven 3.8+

**Steps:**

```bash
# Clone the repository
git clone https://github.com/amine-dev/Sprout.git
cd Sprout/Sprout

# Build the project
mvn clean install

# Run locally against a target directory
java -jar target/sprout-<version>.jar --dir path/to/your/project/src/main/java/com/example
```


## Running Tests

```bash
mvn test
```

The test suite currently covers the parsing layer — `EntityParser` and `HelperParser` — using the fixture entities in `src/main/resources/test-entities/`. These fixtures cover the main JPA association types, different ID types, and Sprout-specific annotations.


## Code Conventions

Sprout follows standard Java conventions with a few specifics worth noting:

**Packages** are all lowercase (`filegenerator`, `importsgenerator`, `dependencygenerator`).

**Models** are immutable records. Don't introduce mutable state in the model layer.

**Lombok** is used for `@RequiredArgsConstructor`. Keep constructor injection — don't use field injection or `@Autowired`.

**Exceptions** must carry a meaningful message. `throw new FileSystemException("")` is not acceptable — always include the entity class name and the nature of the failure.

**Javadoc** is required on all public interfaces, records, and non-trivial utility classes.

**Formatting** — no strict formatter is enforced, but keep it consistent with the surrounding code. 4-space indentation, no wildcard imports except where already present.

## Customization

If Sprout's opinionated defaults don't fit your needs — for example, you want to use ModelMapper instead of MapStruct, or generate a different controller style — the recommended approach is to **fork the repository** and modify it freely. The imports and dependency generation logic lives in the Java layer and cannot be overridden without code changes, so a fork gives you full control over both the templates and the pipeline.

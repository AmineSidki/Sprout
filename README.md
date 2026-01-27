# 🌱 Sprout

Sprout is a lightweight, opinionated **CLI tool** designed to scaffold **Spring Boot layers** (Repositories, Services, DTOs) directly from your **JPA entities**.

By parsing your source code **AST**, Sprout automates the repetitive boilerplate required by a classic layered architecture — without relying on compiled classes or reflection.

## Key Features

- Java code parsing (source → AST)
- Entity vs. Helper class detection
- Context gathering
- Templating (Using mustache)
- Extensibility (**`SproutParser`** , **`SproutGenerator`**)

## 🛠 Installation & Setup

### Prerequisites

- **JDK**: 1.8 or higher *(JDK 17+ recommended)*
- **Build Tool**: Maven 3.6.3+

### Building the Executable

1. **Clone the repository**

```bash
git clone https://github.com/AmineSidki/Sprout.git
cd Sprout
```

1. **Build the shaded JAR**

```bash
mvn clean package
```

The standalone executable will be generated at:

```
target/Sprout-*.*.jar
```

## Usage

Sprout operates **relative to a project root**.

- Use the `d` flag to point to your Spring Boot project directory.

```bash
java -jar Sprout-*.*.jar -d "/path/to/my-spring-project"
```

## ⚠️ Read before running

- **Package Naming Convention**
    
    Sprout assumes your entities are located in a package ending with **`.entity`**.
    
    It will automatically strip this suffix and replace it with the target layer name.
    
    **Example**:
    
    ```
    Input : com.example.project.entity
    Output: com.example.project.repository
    ```
    
- **Mandatory `@Id` Annotation**
    
    Sprout scans for the `@Id` annotation to determine the generic type of `JpaRepository`.
    
    - If an entity **does not** declare an `@Id`, it will be **skipped**.
    - if a class figures in the package and isn’t annotated with `@Entity` it will be accounted for as a helper class
    - A warning message will be displayed.
- **Directory Mapping Assumptions**
    
    Currently, Sprout expects a **flat directory structure** during generation.
    
    Example:
    
    ```
    my-project/
    ├── entity/          <-- Source
    └── repository/      <-- Destination (generated)
    ```
    
    Make sure the destination directory is writable.
    

## Roadmap

- [x]  Repository layer — Full JPA interface generation
- [x]  Service layer — CRUD business logic scaffolding
- [ ]  Controller layer — Exposing routes
- [ ]  Exception handling — generating custom exceptions
- [x]  DTO generation — Request / Response object mapping
- [x]  Multiplicity support — `@OneToMany`, `@ManyToOne` handling
- [x]  Context gathering — Making sure all classes in the project know of the others
- [ ]  Dynamic dependency assigning
- [ ]  Dynamic imports generation
- [x]  Parsing parallelizations — Using multithreading to accelerate java parsing
- [ ]  `@Incremental` for services to indicate if following code generations should create new implementations
- [ ]  Make the generated service an interface with its implementation for more modularity
- [ ]  `@DtoIgnore`, `@RecordDto` — Using annotations to further customize the DTO layer generation
- [ ]  Accounting for `@JsonIgnore`
- [ ]  Custom configuration — De-coupling the program from the pre-defined project structure
- [ ]  Recursively course the entity package’s sub directories to get all the present classes (Still in discussion)
- [ ]  Path resolution — Mapping to `src/main/java` directory trees

## 🤝 Contributing

- **Rules :**
    - If the contribution regards an **unreported** issue, open an issue, then if you are willing to propose a solution for it put it in regards the aforementioned issue.
    - The PRs that will be prioritized are those about **application-breaking bugs** rather than additional features.
    - Opened issues may concern :
        - Bugs
        - New Features
        - README improvements
        - Discussions regarding current/planned features
    - When issuing a PR, refer to the **concerned issue**, and provide details on the way the issue’s solution was implemented.
- **Issuing a pull request :**
    1. Fork the project
    2. Create your feature branch
        
        ```bash
        git checkout -b feature/NewGenerator
        ```
        
    3. Commit your changes
    4. Push to the branch
    5. Open a Pull Request

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for details.

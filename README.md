# 🌱 Sprout

Sprout is a lightweight, opinionated **CLI tool** designed to scaffold **Spring Boot layers** (Repositories, Services, DTOs) directly from your **JPA entities**.

By parsing your source code **AST**, Sprout automates the repetitive boilerplate required by a classic layered architecture — without relying on compiled classes or reflection.

---

## 🚀 Key Features

- **AST Analysis**
    
    Uses **JavaParser** to analyze Java source code directly. No compilation step required.
    
- **Entity Metadata Extraction**
    
    Automatically detects `@Id` fields and infers identifier types for generated layers.
    
- **Mustache Templating**
    
    Clean separation between generation logic and code structure through Mustache templates.
    
- **Extensible Architecture**
    
    Built around pluggable `SproutGenerator`s, making it easy to add new layers (Services, Controllers, etc.).
    

---

## 🛠 Installation & Setup

### Prerequisites

- **JDK**: 1.8 or higher
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
target/Sprout-1.0.jar

```

---

## 💻 Usage

Sprout operates **relative to a project root**.

- Use the `d` flag to point to your Spring Boot project directory.

```bash
java -jar Sprout-1.0.jar -d "/path/to/my-spring-project"

```

---

## ⚙️ CLI Options

| Flag | Long Form | Description | Default |
| --- | --- | --- | --- |
| `-d` | `--dir` | Base directory containing the entity package | Required |
| `-v` | `--version` | Print version information | — |
| `-h` | `--help` | Display usage guide | — |

---

## 💡 Pro Tips (Read This Before Running)

Sprout is designed for **speed and convention-over-configuration**. Following these rules avoids crash-first scenarios.

### 1️⃣ Package Naming Convention

Sprout assumes your entities are located in a package ending with **`.entity`**.

It will automatically strip this suffix and replace it with the target layer name.

**Example**:

```
Input : com.example.project.entity
Output: com.example.project.repository

```

---

### 2️⃣ Mandatory `@Id` Annotation

Sprout scans for the `@Id` annotation to determine the generic type of `JpaRepository`.

- ❌ If an entity **does not** declare an `@Id`, it will be **skipped**.
- ✅ A clear, descriptive error message will be displayed.

---

### 3️⃣ Directory Mapping Assumptions

Currently, Sprout expects a **flat directory structure** during generation.

Example:

```
my-project/
├── entity/          <-- Source
└── repository/      <-- Destination (auto-created)

```

Make sure the destination directory is writable.

---

## 🗺 Roadmap

- [x]  Repository Layer — Full JPA interface generation
- [ ]  Service Layer — CRUD business logic scaffolding
- [ ]  DTO Generation — Request / Response object mapping
- [ ]  Multiplicity Support — `@OneToMany`, `@ManyToOne` handling
- [ ]  Path Resolution — Mapping to `src/main/java` directory trees

---

## 🤝 Contributing

Contributions are what make open-source ecosystems thrive.

1. Fork the project
2. Create your feature branch
    
    ```bash
    git checkout -b feature/NewGenerator
    
    ```
    
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

---

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for details.

---

> README written in Vim btw :D
>
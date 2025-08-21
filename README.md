# MyLibreLab

[![Build Status](https://github.com/MyLibreLab/MyLibreLab/workflows/Deployment/badge.svg)](https://github.com/MyLibreLab/MyLibreLab/actions)
[![SonarCloud](https://github.com/MyLibreLab/MyLibreLab/workflows/SonarCloud/badge.svg)](https://github.com/MyLibreLab/MyLibreLab/actions)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

MyLibreLab is a modern fork of [MyOpenLab](https://myopenlab.org/), a visual programming environment for electronics and embedded systems development. This project aims to modernize the codebase while maintaining compatibility with existing MyOpenLab projects.

> **⚠️ Development Status**: MyLibreLab is currently in early alpha stage and under heavy development. For a stable, production-ready version, please use the [archived MyOpenLab](https://github.com/MyLibreLab/MyOpenLab).

## Features

- **Visual Programming**: Drag-and-drop interface for creating electronic circuits and programs
- **Cross-Platform**: Runs on Windows, macOS, and Linux
- **Modern Java**: Built with Java 15 and modern development practices
- **Self-Contained**: No need to install separate Java runtime
- **Gradle Build System**: Modern build and dependency management
- **Dark Theme Support**: Modern UI with light and dark themes

## Key Improvements Over MyOpenLab

- **Self-contained distribution** - No separate Java installation required
- **Modern Java 15** technology stack
- **Gradle-based build system** for better dependency management
- **GitHub-based development** with CI/CD pipelines
- **Updated dependencies** and security improvements
- **Modern development practices** with code quality tools

## Quick Start

### Prerequisites

- **Java 15** (OpenJDK recommended)
- **Git** for cloning the repository

### Building from Source

1. **Clone the repository**:
   ```bash
   git clone https://github.com/MyLibreLab/MyLibreLab.git
   cd MyLibreLab
   ```

2. **Build the project**:
   ```bash
   # Windows
   gradlew.bat clean build -PskipAutostyle -PskipSonarlint
   
   # Linux/macOS
   ./gradlew clean build -PskipAutostyle -PskipSonarlint
   ```

3. **Run the application**:
   ```bash
   # Windows
   gradlew.bat run
   
   # Linux/macOS
   ./gradlew run
   ```

### Creating Distribution Packages

Create a runtime image:
```bash
./gradlew :mylibrelab-application:runtimeZip
```

Create an installer:
```bash
./gradlew :mylibrelab-application:jpackage
```

## Development Setup

### Recommended IDE: Visual Studio Code

The project includes VS Code configuration for optimal development experience:

1. **Install recommended extensions** when prompted
2. **Open the project folder** in VS Code
3. **Use Ctrl+Shift+P** → "Java: Reload Projects"
4. **Build with Ctrl+Shift+B** or run tasks via Ctrl+Shift+P → "Tasks: Run Task"

### Available Gradle Tasks

| Task | Description |
|------|-------------|
| `./gradlew build` | Build the entire project |
| `./gradlew run` | Run the application |
| `./gradlew test` | Run unit tests |
| `./gradlew autostyleApply` | Format code according to project standards |
| `./gradlew autostyleCheck` | Check code formatting |
| `./gradlew sonarlintMain` | Run static code analysis |

## Project Structure

```
MyLibreLab/
├── application/          # Main application module
├── annotations/          # Custom annotations for code generation
├── dependencies-bom/     # Bill of Materials for dependency management
├── settings-api/         # Settings and configuration API
├── service-manager/      # Service management framework
├── util/                 # Utility classes
├── config/              # Build configuration and code style
├── distribution/        # Distribution scripts and resources
└── docs/               # Documentation
```

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

### Development Workflow

1. **Fork the repository** and create a feature branch
2. **Follow code guidelines** in [docs/code-guidlines.md](docs/code-guidlines.md)
3. **Run quality checks**: `./gradlew autostyleCheck sonarlintMain`
4. **Write tests** for new functionality
5. **Submit a pull request** with clear description

### Code Quality Standards

- **Java 15** compatible code
- **Code formatting** enforced via Autostyle
- **Static analysis** with SonarLint
- **Unit tests** for new functionality
- **Documentation** for public APIs

## System Requirements

### Runtime Requirements
- **Java 15+** (bundled in self-contained distributions)
- **2GB RAM** minimum, 4GB recommended
- **1GB disk space** for installation
- **OpenGL 2.0** compatible graphics

### Development Requirements
- **Java 15 JDK** (OpenJDK recommended)
- **Git** version control
- **Internet connection** for dependency downloads

### Platform Support
| Platform | Architecture | Status |
|----------|-------------|---------|
| Windows | x64 | ✅ Supported |
| macOS | x64, ARM64 | ✅ Supported |
| Linux | x64 | ✅ Supported |

## Documentation

- **[User Documentation](https://mylibrelab.github.io/user-documentation/)** - End-user guides
- **[Code Guidelines](docs/code-guidlines.md)** - Development standards
- **[Software Quality Standards](docs/software-quality-standards.md)** - Quality requirements
- **[Contributing Guide](CONTRIBUTING.md)** - How to contribute

## Community & Support

- **[Discord Server](https://discord.gg/rcvvuxt)** - Community chat and support
- **[GitHub Issues](https://github.com/MyLibreLab/MyLibreLab/issues)** - Bug reports and feature requests
- **[GitHub Discussions](https://github.com/MyLibreLab/MyLibreLab/discussions)** - General discussions

## Security

For security vulnerabilities, please see our [Security Policy](SECURITY.md).

## License

This project is licensed under the **GNU General Public License v3.0** - see the [LICENSE](LICENSE) file for details.

### Attribution

MyLibreLab is based on MyOpenLab by Carmelo Salafia (www.myopenlab.de)
- Original Copyright (C) 2004 Carmelo Salafia cswi@gmx.de
- MyLibreLab Copyright (C) 2020 MyLibreLab Contributors

## Release History

| Version | Status | Java Version | Release Date |
|---------|--------|--------------|--------------|
| 1.0.0 | 🚧 In Development | Java 15 | TBD |

---

**Made with ❤️ by the MyLibreLab community**

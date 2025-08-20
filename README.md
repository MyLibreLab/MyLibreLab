# MyLibreLab

**MyLibreLab** is a powerful, open-source graphical development environment for measurement technology and automation control. It provides a modern, intuitive platform for creating complex applications without writing a single line of code, serving as a robust alternative to proprietary software like LabVIEW.

This project continues the legacy of MyOpenLab, revitalizing it with modern tools, a streamlined build system, and a commitment to creating monolithic, portable executables for all major operating systems.

## Project History and Attributions

MyLibreLab stands on the shoulders of giants. Its existence is a testament to years of development from multiple talented individuals and projects across the open-source community. We gratefully acknowledge their foundational work:

  * **MyOpenLab (2004):** The project originated as MyOpenLab, created by **Carmelo Salafia** (`cswi@gmx.de`). His vision and foundational work are the core upon which MyLibreLab is built.
  * **MiG Layout (2004):** The powerful and flexible layout manager used throughout the user interface was created by **Mikael Grev, MiG InfoCom AB** (`miglayout@miginfocom.com`), and is used under a BSD license.
  * **JetBrains s.r.o. (2000-2019):** Portions of the Swing component wrapping logic are derived from code developed by JetBrains, used under the Apache 2.0 license.
  * **Darklaf:** The modern, themeable look-and-feel is powered by the excellent Darklaf library.

MyLibreLab aims to integrate these powerful technologies and continue their evolution under a clear and unified open-source license.

## Licensing

A primary goal of this project is to provide legal clarity. Historically, the codebase contained a mix of licenses inherited from its constituent parts, including BSD and Apache 2.0.

To simplify this, the **MyLibreLab project as a whole is distributed under the GNU General Public License, version 3 (GPL-3.0)**.

While we retain the original license text in the respective source files for attribution and compliance, any new contributions and the collective work are governed by the GPL-3.0. This ensures that MyLibreLab and its derivatives will always remain free and open-source.

## Lessons Learned: A Guide for Future Developers

The journey to modernize this project and create a stable, cross-platform build process was challenging and educational. We are documenting the key issues encountered and their solutions here to serve as a guide for future contributors.

#### 1\. **Environment: JDK vs. JRE**

  * **Problem:** Early builds failed with cryptic errors like "Kotlin could not find the required JDK tools".
  * **Root Cause:** The build was being run with a Java Runtime Environment (JRE), which lacks the compiler and other tools necessary for building software.
  * **Lesson:** A build environment **must** have a full **Java Development Kit (JDK)** installed. The `JAVA_HOME` environment variable must point to the JDK's root directory, not a JRE.

#### 2\. **Dependencies: The Perils of Inconsistency**

  * **Problem:** The build was plagued by "Unresolved reference" errors, even when dependencies seemed correct.
  * **Root Cause:** This was a multi-layered issue:
    1.  **Outdated BOM:** An external Bill of Materials (BOM) for the Darklaf library was specified with a version that didn't exist in public repositories.
    2.  **Internal BOM Override:** The project's own internal BOM (`:mylibrelab-dependencies-bom`) was enforcing an older, incompatible version of the libraries, silently overriding the correct versions specified in the application module.
    3.  **Transitive Conflicts:** Even with explicit versions, one library would request a different, incompatible version of another (its "transitive dependency").
  * **Lesson:** Dependency management must be centralized and consistent. **A project's internal BOM is the single source of truth**. When debugging, don't just trust the versions in the build script; use `./gradlew dependencies` to see what versions Gradle is *actually* resolving.

#### 3\. **Gradle Lifecycle: Configuration Order Matters**

  * **Problem:** The build failed with a `MissingValueException` because the `mainClass` property was `null`, even though it was defined.
  * **Root Cause:** Gradle configures the root project *before* its subprojects. A plugin applied at the root level was trying to access the `mainClass` property before the `:application` subproject had a chance to define it.
  * **Lesson:** Configuration that is specific to a subproject (like the `mainClass` of an executable) should be defined *only* in that subproject's build script. Avoid applying plugins like `application` to the root project unless the root project is itself an executable.

#### 4\. **Plugin Compatibility: A Moving Target**

  * **Problem:** The build failed with Java `IllegalAccessError` or `NoSuchMethodError`.
  * **Root Cause:** Several Gradle plugins (for SonarLint and native packaging) were outdated and incompatible with Java 17's module system and Gradle 8.8's APIs.
  * **Lesson:** When upgrading your JDK or Gradle version, you **must** review your plugins and update them to modern, compatible versions. The `badass-runtime-plugin` required an update from `1.8.1` to `1.13.0` to function correctly.

#### 5\. **CI/CD: Simulation vs. Reality**

  * **Problem:** The GitHub Actions workflow passed locally with `act` but failed in the real CI environment, and vice-versa.
  * **Root Cause:** The local simulation environment provided by `act` is not a perfect replica of the GitHub-hosted runners. It may use a minimal Docker image that lacks tools like `sudo` or system libraries like `binutils` (required for `jpackage`).
  * **Lesson:** Local CI simulation is a valuable tool, but always treat the production CI environment (GitHub Actions itself) as the final authority. Be prepared to install missing system dependencies in your workflow file. For `act`, use a more complete Docker image (e.g., `catthehacker/ubuntu:act-latest`) for a more accurate simulation.

## Building MyLibreLab

This project is built using Gradle and is configured for cross-platform compilation.

#### Prerequisites

1.  **Git:** To clone the repository.
2.  **JDK 17:** A full Java Development Kit, version 17. Ensure your `JAVA_HOME` environment variable points to it.
3.  **(Windows Only)** [**WiX Toolset**](https://wixtoolset.org/): Required by `jpackage` to create `.msi` installers. Make sure its `bin` directory is on your system's `PATH`.
4.  **(Linux Only)** [**binutils**](https://www.gnu.org/software/binutils/): Required by `jpackage`. Install it using your package manager (e.g., `sudo apt-get install binutils`).

#### Instructions

1.  **Clone the Repository**

    ```shell
    git clone <repository_url>
    cd MyLibreLab
    ```

2.  **Make the Gradle Wrapper Executable (Linux/macOS)**

    ```shell
    chmod +x gradlew
    ```

3.  **Build the Project**
    This command compiles the code, runs tests, and creates the library JARs.

    ```shell
    # On Windows
    .\gradlew build

    # On Linux/macOS
    ./gradlew build
    ```

    *Note: You can add `-PskipAutostyle -PskipSonarlint` to speed up the build by skipping code style checks.*

4.  **Create a Portable Package**
    This creates a self-contained ZIP file with the application and a bundled Java runtime. It does not require Java to be installed on the target machine.

    ```shell
    # On Windows
    .\gradlew :mylibrelab-application:runtimeZip

    # On Linux/macOS
    ./gradlew :mylibrelab-application:runtimeZip
    ```

    The output will be in `application/build/image/`.

5.  **Create a Native Installer**
    This creates a native installer for your platform (`.msi` on Windows, `.dmg` on macOS, `.deb` on Debian/Ubuntu).

    ```shell
    # On Windows
    .\gradlew :mylibrelab-application:jpackage

    # On Linux/macOS
    ./gradlew :mylibrelab-application:jpackage
    ```

    The output will be in `application/build/jpackage/`.

### Automated Builds with GitHub Actions

This repository is configured with a complete CI/CD pipeline using GitHub Actions. The workflow in `.github/workflows/release.yml` will automatically build, package, and release native installers for:

  * Windows (x64)
  * Linux (x64)
  * macOS (x64)
  * macOS (ARM64)

These builds are triggered automatically on pushes to the `dev` branch or can be run manually from the "Actions" tab in GitHub.